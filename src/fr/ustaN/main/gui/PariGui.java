package fr.ustaN.main.gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import fr.ustaN.main.SkybPvpArena;
import fr.ustaN.main.utils.Play;

public class PariGui {
	private List<ItemStack> listItemAParier;
	private Inventory invPariGui;
	private Player player;
	private String guiName;
	private boolean choseBlockForBetMod, checkMod;
	private final int pageMax;
	private int page;
	public ItemStack headPlus;
	public ItemStack headVUn;
	public ItemStack headVCinq;
	public ItemStack headRUn;
	public ItemStack headRCinq;
	public ItemStack headCheck;
	public PariGui() {
		this.listItemAParier = SkybPvpArena.getMain().getListItemAParier();
		this.guiName = "Votre Pari";
		this.choseBlockForBetMod = false;
		this.checkMod = false;
		this.pageMax = 1 + listItemAParier.size() / 45;
		this.page = 1;
		this.headPlus = Play.createHead(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkZDIwYmU5MzUyMDk0OWU2Y2U3ODlkYzRmNDNlZmFlYjI4YzcxN2VlNmJmY2JiZTAyNzgwMTQyZjcxNiJ9fX0=",
				1, "Parier un nouvel item", "§3Click droit");
		this.headVUn = Play.createHead(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg5OTE2OTc0Njk2NTNjOWFmODM1MmZkZjE4ZDBjYzljNjc3NjNjZmU2NjE3NWMxNTU2YWVkMzMyNDZjNyJ9fX0=",
				1, "Ajouter 1 ", "§3Click droit");
		this.headVCinq = Play.createHead(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJjMTQyYWY1OWYyOWViMzVhYjI5YzZhNDVlMzM2MzVkY2ZjMmE5NTZkYmQ0ZDJlNTU3MmIwZDM4ODkxYjM1NCJ9fX0=",
				1, "Ajouter 5 ", "§3Click droit");
		this.headRUn = Play.createHead(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQyNDU0ZTRjNjdiMzIzZDViZTk1M2I1YjNkNTQxNzRhYTI3MTQ2MDM3NGVlMjg0MTBjNWFlYWUyYzExZjUifX19=",
				1, "retirer 1 ", "§3Click droit");
		this.headRCinq = Play.createHead(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGYzZjU2NWE4ODkyOGVlNWE5ZDY4NDNkOTgyZDc4ZWFlNmI0MWQ5MDc3ZjJhMWU1MjZhZjg2N2Q3OGZiIn19fQ==",
				1, "retirer 5 ", "§3Click droit");
		this.headCheck = Play.createHead(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkyZTMxZmZiNTljOTBhYjA4ZmM5ZGMxZmUyNjgwMjAzNWEzYTQ3YzQyZmVlNjM0MjNiY2RiNDI2MmVjYjliNiJ9fX0=",
				1, "Valider le Pari", "§3Click droit");
	}
	public void init(Player player_) {
		this.player = player_;
		if (this.checkMod)
			invCheck();
		else if (this.choseBlockForBetMod)
			invChoseBlock();
		else
			invPari();
		SkybPvpArena.getMain().getPariObj().setEnd(true);
		this.player.openInventory(invPariGui);
		this.player.updateInventory();
	}

	public String getName() {
		return this.guiName;
	}

	public void setPlaceBarreVerte(int i) {
		if (i < 9) {
			this.invPariGui.setItem(i, Play.createGuiItem("", null,Material.STAINED_GLASS_PANE, (short) 13));
			this.invPariGui.setItem(i + 9, Play.createGuiItem("", null,Material.STAINED_GLASS_PANE, (short) 5));
			this.invPariGui.setItem(i + 18, this.headPlus);
			this.invPariGui.setItem(i + 27, Play.createGuiItem("", null,Material.STAINED_GLASS_PANE, (short) 5));
			this.invPariGui.setItem(i + 36, Play.createGuiItem("", null,Material.STAINED_GLASS_PANE, (short) 13));
			this.invPariGui.setItem(i + 45, Play.createGuiItem("", null,Material.STAINED_GLASS_PANE, (short) 15));	
		}
	}

	public void invPari() {
		this.invPariGui = Bukkit.createInventory(null, 54, this.guiName);
		if (!SkybPvpArena.getMain().getPariObj().getPariList().isEmpty()) {
			for (int i = 0; i < SkybPvpArena.getMain().getPariObj().getPariList().size(); i++) {
				ItemStack itemStack = new ItemStack(SkybPvpArena.getMain().getPariObj().getPariList().get(i));
				String typeItem = SkybPvpArena.getMain().getPariObj().getPariList().get(i).getType().toString();
				ItemMeta itemMeta = itemStack.getItemMeta();
				String lore = "Click molette pour_parier tout les_" + typeItem + " en votre_posession";
				String[] loreListArray = lore.split("_");
				List<String> loreList = new ArrayList<>();
				for (String s : loreListArray) {
					loreList.add(s.replace("&", "§"));
				}
				itemMeta.setLore(loreList);
				itemStack.setItemMeta(itemMeta);
				this.headVUn.getItemMeta().setDisplayName("Ajouter 1 " + typeItem);
				this.headVCinq.getItemMeta().setDisplayName("Ajouter 5 " + typeItem);
				this.headRUn.getItemMeta().setDisplayName("retirer 1 " + typeItem);
				this.headRCinq.getItemMeta().setDisplayName("retirer 5 " + typeItem);
				this.invPariGui.setItem(i, this.headVUn);
				this.invPariGui.setItem(i + 9, this.headVCinq);
				this.invPariGui.setItem(i + 18, itemStack);
				this.invPariGui.setItem(i + 27, this.headRCinq);
				this.invPariGui.setItem(i + 36, this.headRUn);
				this.invPariGui.setItem(i + 45, Play
						.createGuiItem("Supprimer ces item du pari", null, Material.BARRIER, (short) 0));
			}
		}
		setPlaceBarreVerte(SkybPvpArena.getMain().getPariObj().getPariList().size());
		for (int i = SkybPvpArena.getMain().getPariObj().getPariList().size(); i < 8; i++) {
			this.invPariGui.setItem(1 + i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 14));
			this.invPariGui.setItem(10 + i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 7));
			this.invPariGui.setItem(19 + i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 9));
			this.invPariGui.setItem(28 + i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 7));
			this.invPariGui.setItem(37 + i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 14));
			this.invPariGui.setItem(46 + i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 15));
		}
		this.invPariGui.setItem(8, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 15));
		this.invPariGui.setItem(17, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 15));
		this.invPariGui.setItem(26, headCheck);
		this.invPariGui.setItem(35, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 15));
		this.invPariGui.setItem(44, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 15));
		this.invPariGui.setItem(53, Play.createGuiItem("Annuler le Pari", null, Material.DARK_OAK_DOOR_ITEM, (short) 0));
	}

	public void invChoseBlock() {
		this.invPariGui = Bukkit.createInventory(null, 54, this.guiName);
		if (this.page == this.pageMax)
			for (int i = 0; i < this.listItemAParier.size() - (this.page - 1) * 45; i++)
				this.invPariGui.setItem(i, this.listItemAParier.get(i));
		else
			for (int i = 0; i < 45; i++)
				this.invPariGui.setItem(i, this.listItemAParier.get((page - 1) * 45 + i));
		for (int i = 0; i < 9; i++) {
			this.invPariGui.setItem(45 + i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 15));
		}
		this.invPariGui.setItem(49, Play.createGuiItem("Annuler l'ajout d'item au pari", null, Material.BARRIER, (short) 0));
		if (this.page != this.pageMax) {
			this.invPariGui.setItem(53, Play.createGuiItem("Page suivante", null, Material.PAPER, (short) 0));
		}
		if (this.page != 1) {
			this.invPariGui.setItem(45, Play.createGuiItem("Page précédente", null, Material.PAPER, (short) 0));
		}
	}

	private void invCheck() {
		this.invPariGui = Bukkit.createInventory(null, 27, this.guiName);
		if (SkybPvpArena.getMain().getPariObj().getPariList().isEmpty()) {
			this.invPariGui.setItem(0, Play.createGuiItem("Aucun item parié", null, Material.BARRIER, (short) 0));
		} else
			for (int i = 0; i < SkybPvpArena.getMain().getPariObj().getPariList().size(); i++)
				this.invPariGui.setItem(i, SkybPvpArena.getMain().getPariObj().getPariList().get(i));
		for (int i = 0; i < 9; i++) {
			this.invPariGui.setItem(18 + i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 15));
		}
		this.invPariGui.setItem(24, this.headCheck);
		this.invPariGui.setItem(21, Play.createGuiItem("Retour au pari", null, Material.BARRIER, (short) 0));
	}

	public void changePage(int i) {
		if (i == 0)
			this.page = 1;
		else if (this.page + i < 1)
			this.page = 1;
		else if (this.page + i > this.pageMax)
			this.page = this.pageMax;
		else
			this.page += i;
	}

	public void setChoseBlockForBetMod(boolean etat) {
		this.choseBlockForBetMod = etat;
	}

	public boolean getChoseBlockForBetMod() {
		return this.choseBlockForBetMod;
	}

	public void setCheckMod(boolean etat) {
		this.checkMod = etat;
	}

	public boolean getCheckMod() {
		return this.checkMod;
	}
}