package fr.ustaN.main.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.ustaN.adds.YMLFile;
import fr.ustaN.main.SkybPvpArena;
import fr.ustaN.main.utils.Play;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class pluginListeners implements Listener {
	private YMLFile configFile = SkybPvpArena.getMain().getYMLConfig();
	private Configuration config = configFile.getConfiguration();
	private List<ItemStack> pariList = SkybPvpArena.getMain().getListItemAParier();
	private List<String> whiteListCommand = config.getStringList("whiteListConfig");

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();
			if (block.getState() instanceof Sign) {
				Sign sign = (Sign) block.getState();
				if (sign.getLine(0).equalsIgnoreCase(config.getStringList("sign.lines").get(0))) {
					Location signLoc = sign.getLocation().clone();
					if (signLoc.getWorld().getName().equals(config.getString("sign.BlockLoc.world"))
							&& signLoc.getX() == config.getInt("sign.BlockLoc.x")
							&& signLoc.getY() == config.getInt("sign.BlockLoc.y")
							&& signLoc.getZ() == config.getInt("sign.BlockLoc.z")) {
						if (player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(1)))
							SkybPvpArena.getMain().setEtatDuJeu(0);
						SkybPvpArena.getMain().getPlayObj().playGame(player);
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		ItemStack item = event.getCurrentItem();
		if (item == null) {
			return;
		}
		if (inv.getName().equals(SkybPvpArena.getMain().getPlayObj().getPariGui().getName())) {
			ClickType click = event.getClick();
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);
			if (SkybPvpArena.getMain().getPlayObj().getPariGui().getCheckMod()) {
				if (item.equals(SkybPvpArena.getMain().getPlayObj().getPariGui().headCheck)) {
					SkybPvpArena.getMain().getPlayObj().getPariGui().setCheckMod(false);
					SkybPvpArena.getMain().setEtatDuJeu(2);
					System.out.println(config.getString("texts.newGameBC"));
					String msg = SkybPvpArena.getMain().getPlayObj().getText(config.getString("texts.newGameBC"));
					IChatBaseComponent comp = ChatSerializer
							.a("{\"text\":\""+ SkybPvpArena.getMain().getPlayObj().getText(config.getString("texts.textCliquableBC"))
								+"\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/arene sign @p\"}}");
					PacketPlayOutChat packet = new PacketPlayOutChat(comp);
					for (Player player_ : Bukkit.getServer().getOnlinePlayers()) {
						player_.sendMessage(msg);
						((CraftPlayer) player_).getHandle().playerConnection.sendPacket(packet);
					}
					SkybPvpArena.getMain().getPariObj().setEnd(true);
					player.closeInventory();
					SkybPvpArena.getMain().getPariObj().takeItems(player);
				} else if (item.equals(Play.createGuiItem("Retour au pari", null, Material.BARRIER, (short) 0))) {
					SkybPvpArena.getMain().getPlayObj().getPariGui().setCheckMod(false);
					SkybPvpArena.getMain().getPlayObj().getPariGui().init(player);
				}
			} else if (SkybPvpArena.getMain().getPlayObj().getPariGui().getChoseBlockForBetMod()) {
				if (item.equals(Play.createGuiItem("Page suivante", null, Material.PAPER, (short) 0))) {
					SkybPvpArena.getMain().getPlayObj().getPariGui().changePage(1);
				} else if (item.equals(Play.createGuiItem("Page précédente", null, Material.PAPER, (short) 0))) {
					SkybPvpArena.getMain().getPlayObj().getPariGui().changePage(-1);
				} else if (item.equals(Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 15)))
					return;
				else if (item.equals(
						Play.createGuiItem("Annuler l'ajout d'item au pari", null, Material.BARRIER, (short) 0)))
					SkybPvpArena.getMain().getPlayObj().getPariGui().setChoseBlockForBetMod(false);
				else {
					SkybPvpArena.getMain().getPariObj().addNewItem(item, player);
					SkybPvpArena.getMain().getPlayObj().getPariGui().setChoseBlockForBetMod(false);
				}
				SkybPvpArena.getMain().getPlayObj().getPariGui().init(player);
			} else {
				if (item.equals(SkybPvpArena.getMain().getPlayObj().getPariGui().headPlus)) {
					SkybPvpArena.getMain().getPlayObj().getPariGui().setChoseBlockForBetMod(true);
					SkybPvpArena.getMain().getPlayObj().getPariGui().changePage(0);
				} else if (item.equals(SkybPvpArena.getMain().getPlayObj().getPariGui().headVUn)
						&& !click.equals(ClickType.MIDDLE)) {
					SkybPvpArena.getMain().getPariObj().addItem(1, player, event.getSlot());
				} else if (item.equals(SkybPvpArena.getMain().getPlayObj().getPariGui().headVCinq)
						&& !click.equals(ClickType.MIDDLE)) {
					SkybPvpArena.getMain().getPariObj().addItem(5, player, event.getSlot() - 9);
				} else if (pariList.contains(item) && click.equals(ClickType.MIDDLE)) {
					SkybPvpArena.getMain().getPariObj().addItem(64, player, event.getSlot() - 18);
				} else if (item.equals(SkybPvpArena.getMain().getPlayObj().getPariGui().headRCinq)
						&& !click.equals(ClickType.MIDDLE)) {
					SkybPvpArena.getMain().getPariObj().removeItem(5, event.getSlot() - 27);
				} else if (item.equals(SkybPvpArena.getMain().getPlayObj().getPariGui().headRUn)
						&& !click.equals(ClickType.MIDDLE)) {
					SkybPvpArena.getMain().getPariObj().removeItem(1, event.getSlot() - 36);
				} else if (item.equals(SkybPvpArena.getMain().getPlayObj().getPariGui().headCheck)
						&& !click.equals(ClickType.MIDDLE)) {
					SkybPvpArena.getMain().getPlayObj().getPariGui().setCheckMod(true);
				} else if (item
						.equals(Play.createGuiItem("Annuler le Pari", null, Material.DARK_OAK_DOOR_ITEM, (short) 0))) {
					player.sendMessage(
							SkybPvpArena.getMain().getPlayObj().getText(config.getString("texts.betCancelled")));
					SkybPvpArena.getMain().getPlayObj().setPlayer(null, 1);
					SkybPvpArena.getMain().setEtatDuJeu(0);
					SkybPvpArena.getMain().getPariObj().resetPari();
					SkybPvpArena.getMain().getPariObj().setEnd(true);
					player.closeInventory();
					return;
				} else if (item.equals(Play.createGuiItem("Supprimer ces item du pari", null, Material.BARRIER, (short) 0))) {
					SkybPvpArena.getMain().getPariObj().dellItem(event.getSlot() - 45);
				} else {
					return;
				}
				SkybPvpArena.getMain().getPlayObj().getPariGui().init(player);
			}
			event.setCancelled(true);
		} else if (inv.getName().equals(SkybPvpArena.getMain().getPlayObj().getAcceptPariGui().getName())) {
			Player player = (Player) event.getWhoClicked();
			if (item.equals(SkybPvpArena.getMain().getPlayObj().getAcceptPariGui().headCheck)) {
				System.out.println(config.getString("texts.unVSunStart"));
				SkybPvpArena.getMain().broadCast(SkybPvpArena.getMain().getPlayObj().getText(config.getString("texts.unVSunStartBC")));
				SkybPvpArena.getMain().getPariObj().setEnd(true);
				player.closeInventory();
				SkybPvpArena.getMain().getPariObj().takeItems(player);
				SkybPvpArena.getMain().getPlayObj().tp();
				return;
			} else if (item.equals(Play.createGuiItem("refuser le pari", null, Material.BARRIER, (short) 0))) {
				player.sendMessage(SkybPvpArena.getMain().getPlayObj().getText(config.getString("texts.confirmCancelled")));
				SkybPvpArena.getMain().getPlayObj().setPlayer(null, 2);
				SkybPvpArena.getMain().setEtatDuJeu(2);
				SkybPvpArena.getMain().getPariObj().setEnd(true);
				player.closeInventory();
				return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCommandProcess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (SkybPvpArena.getMain().getPlayObj().getInFight()) {
			if (player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(1))
					|| player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(2))) {
				if (whiteListCommand.contains(event.getMessage()))
					event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onCommandServerProcess(ServerCommandEvent event)
	{
		String[] args = event.getCommand().split(" ");
		if(args[0].equals("arene") && args[1].equals("sign"))
		{
			if(!args[2].isEmpty())
			{
				Player player = Bukkit.getServer().getPlayer(args[2]);
				if (SkybPvpArena.getMain().getPlayObj().getInFight()) {
					if (player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(1))
					|| player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(2))) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMoove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(1))
				|| player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(2)))
			if (SkybPvpArena.getMain().getEtatDuJeu() == 4 && !SkybPvpArena.getMain().getPlayObj().getInFight()) {
				event.setCancelled(true);
			}
	}

	@EventHandler
	public void onPlayerTP(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if (player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(1))
				|| player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(2)))
			if (SkybPvpArena.getMain().getEtatDuJeu() == 4 || SkybPvpArena.getMain().getPlayObj().getInFight()) {
				if(!event.getCause().equals(TeleportCause.ENDER_PEARL))event.setCancelled(true);
			}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerFakeDeath(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			if (SkybPvpArena.getMain().getPlayObj().getInFight()) {
				if ((player.getHealth()-event.getDamage())<=0)
				{
					player.setHealth(1);
					event.setDamage(0);
					if (player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(1))) {
						SkybPvpArena.getMain().getPlayObj().victory(SkybPvpArena.getMain().getPlayObj().getPlayer(2),
								SkybPvpArena.getMain().getPlayObj().getPlayer(1));
					} else if (player.equals(SkybPvpArena.getMain().getPlayObj().getPlayer(2))) {
						SkybPvpArena.getMain().getPlayObj().victory(SkybPvpArena.getMain().getPlayObj().getPlayer(1),
								SkybPvpArena.getMain().getPlayObj().getPlayer(2));
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getViewers().isEmpty())
			return;
		if (event.getInventory().getName().equals(SkybPvpArena.getMain().getPlayObj().getAcceptPariGui().getName())
				|| event.getInventory().getName().equals(SkybPvpArena.getMain().getPlayObj().getPariGui().getName())) {
			if (!SkybPvpArena.getMain().getPariObj().isEnd()) {
				Player player = (Player) event.getPlayer();
				Bukkit.getScheduler().runTaskLaterAsynchronously(SkybPvpArena.getMain(), () -> {
					player.openInventory(event.getView().getTopInventory());
				}, 1);
			}
		}
		SkybPvpArena.getMain().getPariObj().setEnd(false);
	}
}
