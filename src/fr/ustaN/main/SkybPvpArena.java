package fr.ustaN.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ustaN.adds.YMLFile;
import fr.ustaN.main.listeners.CommandListener;
import fr.ustaN.main.listeners.pluginListeners;
import fr.ustaN.main.utils.Pari;
import fr.ustaN.main.utils.Play;

public class SkybPvpArena extends JavaPlugin {
	private static SkybPvpArena main;
	private YMLFile config;
	private Pari pari;
	private Play play;
	private int etatDuJeu;// 0=pas de combat ; 1=pari en cour ; 2=pari en attente d'acceptation; 3=pari en cours d'acceptation ; 4=combat en cours

	public void onEnable() {
		main = this;
		this.config = new YMLFile("plugins", "BBSkyBPvpArena", "config");
		this.etatDuJeu = 0;
		this.pari = new Pari();
		this.play = new Play();
		getServer().getPluginManager().registerEvents(new pluginListeners(), this);
		getCommand("arene").setExecutor(new CommandListener());
		System.out.println("plugin SkyBlock Pvp Arena ON!");
	}

	public void onDisable() {
		System.out.println("plugin SkyBlock Pvp Arena OFF!");
	}

	public Pari getPariObj() {return this.pari;}
	public Play getPlayObj() {return this.play;}
	public static SkybPvpArena getMain() {
		return main;
	}

	public YMLFile getYMLConfig() {
		return config;
	}

	public int getEtatDuJeu() {
		return etatDuJeu;
	}
	public void setEtatDuJeu(int i) {
		etatDuJeu = i;
	}

	public List<ItemStack> getListItemAParier() {
		List<ItemStack> itemStacks = new ArrayList<>();
		config.getConfiguration().getStringList("bet.allowedItems").forEach(s -> itemStacks.add(new ItemStack(Material.valueOf(s))));
		return itemStacks;
	}

	public Material getMaterial(String rawMaterial) {
		assert rawMaterial != null;
		for (Material material : Material.values())
			if (material.name().equalsIgnoreCase(rawMaterial))
				return material;
		return null;
	}

	public void broadCast(String BC) {
		getServer().broadcastMessage(BC);
	}
	public void resetAll()
	{
		this.etatDuJeu = 0;
		this.pari = new Pari();
		this.play = new Play();
	}
}