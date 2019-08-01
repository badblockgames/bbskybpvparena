package fr.ustaN.main.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.ustaN.adds.YMLFile;
import fr.ustaN.main.SkybPvpArena;
import fr.ustaN.main.gui.AcceptPariGui;
import fr.ustaN.main.gui.PariGui;
import org.bukkit.scheduler.BukkitRunnable;

public class Play {
	private List<Player> players = new ArrayList<>();
	private YMLFile configFile;
	private Configuration config;
	private Player playerUn, playerDeux;
	private String namePlayerUn = "NoName", namePlayerDeux = "NoName", nameWinner = "NoName", nameLooser = "NoName";
	private PariGui pariGui;
	private AcceptPariGui acceptPariGui;
	private boolean fight = false;

	public Play() {
		this.configFile = SkybPvpArena.getMain().getYMLConfig();
		this.config = configFile.getConfiguration();
		this.pariGui = new PariGui();
		this.acceptPariGui = new AcceptPariGui();
	}

	public void playGame(Player player) {
		switch (SkybPvpArena.getMain().getEtatDuJeu()) {
		case 0:
			this.setPlayer(player, 1);
			this.pariGui.init(player);
			SkybPvpArena.getMain().setEtatDuJeu(1);
			break;
		case 1:
			player.sendMessage(getText(this.config.getString("texts.pariEnCours")));
			break;
		case 2:
			this.setPlayer(player, 2);
			this.acceptPariGui.init(player);
			SkybPvpArena.getMain().setEtatDuJeu(3);
			break;
		case 3:
			player.sendMessage(getText(this.config.getString("texts.gameUnavailable")));
			break;
		case 4:
			player.sendMessage(getText(this.config.getString("texts.gameUnavailable")));
			break;
		case 5:
			player.sendMessage(getText(this.config.getString("texts.gameUnavailable")));
			break;
		default:
			player.sendMessage(getText(this.config.getString("texts.Erreur.etat")));
			break;
		}
	}

	public void tp() {
		playerUn.teleport(new Location(Bukkit.getWorld(this.config.getString("SpawnAreas.1.world")),
				this.config.getDouble("SpawnAreas.1.x"), this.config.getDouble("SpawnAreas.1.y"),
				this.config.getDouble("SpawnAreas.1.z"), (float) this.config.getDouble("SpawnAreas.1.pitch"),
				(float) this.config.getDouble("SpawnAreas.1.yaw")));
		playerUn.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 7, 1));
		playerDeux.teleport(new Location(Bukkit.getWorld(this.config.getString("SpawnAreas.2.world")),
				this.config.getDouble("SpawnAreas.2.x"), this.config.getDouble("SpawnAreas.2.y"),
				this.config.getDouble("SpawnAreas.2.z"), (float) this.config.getDouble("SpawnAreas.2.pitch"),
				(float) this.config.getDouble("SpawnAreas.2.yaw")));
		playerDeux.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 7, 1));
		SkybPvpArena.getMain().setEtatDuJeu(4);
		timer();
	}

	public void setPlayer(Player player, int i) {
		if (i == 1) {
			this.playerUn = player;
			if (player != null)
				this.namePlayerUn = player.getDisplayName();
			players.put(playerUn);
		} else if (i == 2) {
			this.playerDeux = player;
			players.put(playerDeux);
			if (player != null)
				this.namePlayerDeux = player.getDisplayName();
		}
	}

	public Player getPlayer(int i) {
		if (i == 1)
			return this.playerUn;
		else if (i == 2)
			return this.playerDeux;
		else
			return null;
	}

	public String getText(String txt) {
		if (txt.contains("&"))
			txt = txt.replace("&", "§");
		if (txt.contains("PLAYERUN"))
			txt = txt.replace("PLAYERUN", this.namePlayerUn);
		if (txt.contains("PLAYERDEUX"))
			txt = txt.replace("PLAYERDEUX", this.namePlayerDeux);
		if (txt.contains("WINNER"))
			txt = txt.replace("WINNER", this.nameWinner);
		if (txt.contains("LOOSER"))
			txt = txt.replace("LOOSER", this.nameLooser);
		if (txt.contains("PARILIST")) {
			if (SkybPvpArena.getMain().getPariObj().getPariList().isEmpty())
				txt = txt.replace("PARILIST", "son honneur");
			else {
				String string = "";
				for (ItemStack item : SkybPvpArena.getMain().getPariObj().getPariList())
					if (SkybPvpArena.getMain().getPariObj().getPariList()
							.indexOf(item) == SkybPvpArena.getMain().getPariObj().getPariList().size() - 1)
						string += item.getAmount() + " " + item.getType().name() + " ";
					else if (SkybPvpArena.getMain().getPariObj().getPariList()
							.indexOf(item) == SkybPvpArena.getMain().getPariObj().getPariList().size() - 2)
						string += item.getAmount() + " " + item.getType().name() + "et ";
					else
						string += item.getAmount() + " " + item.getType().name() + ", ";
				txt = txt.replace("PARILIST", string);
			}
		}
		return txt;
	}

	public static ItemStack createGuiItem(String name, ArrayList<String> desc, Material mat, short id) {
		ItemStack itemStack = new ItemStack(mat, 1, id);
		ItemMeta iMeta = itemStack.getItemMeta();
		iMeta.setDisplayName(name);
		iMeta.setLore(desc);
		itemStack.setItemMeta(iMeta);
		return itemStack;
	}

	public static ItemStack createHead(String data, int amount, String name, String lore) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM);
		item.setDurability((short) 3);
		item.setAmount(amount);
		ItemMeta meta = item.getItemMeta();
		if (!lore.isEmpty()) {
			String[] loreListArray = lore.split("__");
			List<String> loreList = new ArrayList<>();
			for (String s : loreListArray) {
				loreList.add(s.replace("&", "Â§"));
			}
			meta.setLore(loreList);
		}
		if (!name.isEmpty()) {
			meta.setDisplayName(name.replace("&", "Â§"));
		}
		item.setItemMeta(meta);
		SkullMeta headMeta = (SkullMeta) item.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", data));
		Field profileField;
		try {
			profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		item.setItemMeta(headMeta);
		return item;
	}

	public PariGui getPariGui() {
		return this.pariGui;
	}

	public AcceptPariGui getAcceptPariGui() {
		return this.acceptPariGui;
	}

	public boolean getInFight() {
		return this.fight;
	}

	public void victory(Player winner_, Player looser_) {
		this.fight = false;
		this.playerUn.setInvulnerable(true);
		this.playerDeux.setInvulnerable(true);
		this.nameWinner = winner_.getDisplayName();
		this.nameLooser = looser_.getDisplayName();
		SkybPvpArena.getMain().broadCast(getText(config.getString("texts.victoryBC")));
		SkybPvpArena.getMain().getPariObj().rendrePari(winner_, 2);
		SkybPvpArena.getMain().setEtatDuJeu(5);
//TODO	this.playerUn.teleport(new Location(Bukkit.getWorld(config.getString("SpawnAreas.spawn.world")),config.getInt("SpawnAreas.spawn.x"), config.getInt("SpawnAreas.spawn.y"),config.getInt("SpawnAreas.spawn.z"),0,0));
		//this.playerDeux.teleport(new Location(Bukkit.getWorld(config.getString("SpawnAreas.spawn.world")),config.getInt("SpawnAreas.spawn.x"), config.getInt("SpawnAreas.spawn.y"),config.getInt("SpawnAreas.spawn.z"),0,0));
		for(Player pls : players){pls.teleport(new Location(Bukkit.getWorld(config.getString("SpawnAreas.spawn.world")),config.getInt("SpawnAreas.spawn.x"), config.getInt("SpawnAreas.spawn.y"),config.getInt("SpawnAreas.spawn.z"),0,0)); }
		players.remove(playerUn);
		players.remove(playerDeux);
		this.playerUn.setInvulnerable(false);
		this.playerDeux.setInvulnerable(false);
		SkybPvpArena.getMain().resetAll();
	}

	private int time = 10;

	public void timer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (time <= 0) {
					SkybPvpArena.getMain().getPlayObj().getPlayer(1).sendTitle("§bQue le bain de sang", "§bCOMMENCE",
							10, 15, 10);
					SkybPvpArena.getMain().getPlayObj().getPlayer(2).sendTitle("§bQue le bain de sang", "§bCOMMENCE",
							10, 15, 10);
					fight = true;
					cancel();
					return;
				}
				SkybPvpArena.getMain().getPlayObj().getPlayer(1).sendTitle("§bLe combat commence",
						"§Bdans " + time + "s", 5, 10, 5);
				SkybPvpArena.getMain().getPlayObj().getPlayer(2).sendTitle("§bLe combat commence",
						"§Bdans " + time + "s", 5, 10, 5);
				if (time == 3) {
					SkybPvpArena.getMain().getPlayObj().getPlayer(1).removePotionEffect(PotionEffectType.BLINDNESS);
					SkybPvpArena.getMain().getPlayObj().getPlayer(2).removePotionEffect(PotionEffectType.BLINDNESS);
				}
				time--;
			}
		}.runTaskTimer(SkybPvpArena.getMain(), 0, 20);
	}
}
