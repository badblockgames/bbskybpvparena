package fr.ustaN.main.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.ustaN.main.SkybPvpArena;
import fr.ustaN.main.utils.Play;

public class AcceptPariGui {

	private String guiName;
	private Inventory invCheckGui;
	private Player player;
	public ItemStack headCheck;
	private boolean LePlayerPeutAccepter;
	
	public AcceptPariGui() {
		this.guiName = "Accepter le pari";
		this.headCheck = Play.createHead(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkyZTMxZmZiNTljOTBhYjA4ZmM5ZGMxZmUyNjgwMjAzNWEzYTQ3YzQyZmVlNjM0MjNiY2RiNDI2MmVjYjliNiJ9fX0=",
				1, "Accepter le Pari", "");
	}
	public void init(Player player_) {
		this.player = player_;
		this.LePlayerPeutAccepter = true;
		this.player.closeInventory();
		this.invCheckGui = Bukkit.createInventory(null, 36, this.guiName);
		inv();
		this.player.openInventory(this.invCheckGui);
		this.player.updateInventory();
	}

	public String getName() {
		return this.guiName;
	}

	public void inv() {
		if (SkybPvpArena.getMain().getPariObj().getPariList().isEmpty()) {
			this.invCheckGui.setItem(0, Play.createGuiItem("Aucun item parié", null, Material.BARRIER, (short) 0));
		} else {
			for (int i = 0; i < SkybPvpArena.getMain().getPariObj().getPariList().size(); i++) {
				this.invCheckGui.setItem(9 + i, SkybPvpArena.getMain().getPariObj().getPariList().get(i));
				if (SkybPvpArena.getMain().getPariObj().countItemInPlayerInv(this.player,
						SkybPvpArena.getMain().getPariObj().getPariList().get(i).getType()) >= SkybPvpArena.getMain().getPariObj().getPariList().get(i).getAmount()) {
					this.invCheckGui.setItem(i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 5));
					this.invCheckGui.setItem(18 + i,
							Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 5));
				} else {
					this.invCheckGui.setItem(i, Play.createGuiItem("ressources insuffisantes", null, Material.STAINED_GLASS_PANE, (short) 14));
					this.invCheckGui.setItem(18 + i,
							Play.createGuiItem("ressources insuffisantes", null, Material.STAINED_GLASS_PANE, (short) 14));
					LePlayerPeutAccepter = false;
				}
			}
		}
		for (int i = 0; i < 9; i++)
			this.invCheckGui.setItem(27 + i, Play.createGuiItem("", null, Material.STAINED_GLASS_PANE, (short) 15));
		if(LePlayerPeutAccepter)this.invCheckGui.setItem(35, this.headCheck);
		this.invCheckGui.setItem(32, Play.createGuiItem("refuser le pari", null, Material.BARRIER, (short) 0));
	}
	public boolean getLePlayerPeutAccepter() {
		return LePlayerPeutAccepter;
	}
}
