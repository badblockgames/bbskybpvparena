package fr.ustaN.main.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.ustaN.adds.YMLFile;
import fr.ustaN.main.SkybPvpArena;

public class Pari {
	private List<ItemStack> pariList;
	private YMLFile configFile;
	private Configuration config;
	private boolean pariEnd;

	public Pari() {
		this.pariList = new ArrayList<>();
		this.configFile = SkybPvpArena.getMain().getYMLConfig();
		this.config = this.configFile.getConfiguration();
	}

	public void addItem(int amount, Player player, int index) {
		int dispo = countItemInPlayerInv(player, this.pariList.get(index).getType())
				- countItemInPari(this.pariList.get(index).getType());
		if (dispo == 0) {
			player.sendMessage(
					SkybPvpArena.getMain().getPlayObj().getText(this.config.getString("texts.insufficientMaterials")));
			return;
		} else if (amount > dispo)
			amount = dispo;
		if (amount + this.pariList.get(index).getAmount() > 64)
			this.pariList.get(index).setAmount(64);
		else
			this.pariList.get(index).setAmount(this.pariList.get(index).getAmount() + amount);
	}

	public void addNewItem(ItemStack item, Player player) {
		if (countItemInPlayerInv(player, item.getType()) - countItemInPari(item.getType()) > 0) {
			this.pariList.add(this.pariList.size(), item);
		} else
			player.sendMessage(
					SkybPvpArena.getMain().getPlayObj().getText(this.config.getString("texts.insufficientMaterials")));
	}

	private int countItemInPari(Material type) {
		int somme = 0;
		if (!this.pariList.isEmpty())
			for (ItemStack item : this.pariList)
				if (item.getType().equals(type))
					somme += item.getAmount();
		return somme;
	}

	public void removeItem(int amount, int index) {
		if (this.pariList.get(index).getAmount() - amount < 1)
			this.pariList.get(index).setAmount(1);
		else
			this.pariList.get(index).setAmount(this.pariList.get(index).getAmount() - amount);
	}

	public void dellItem(int index) {
		this.pariList.remove(index);
	}

	public List<ItemStack> getPariList() {
		return this.pariList;
	}

	public void resetPari() {
		this.pariList.clear();
	}

	public int countItemInPlayerInv(Player player, Material material) {
		return Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull)
				.filter(itemStack -> material == itemStack.getType()).mapToInt(ItemStack::getAmount).sum();
	}

	public ItemStack getItemStack(String name, Material mat, short id) {
		ItemStack itemStack = new ItemStack(mat, 1, id);
		ItemMeta iMeta = itemStack.getItemMeta();
		iMeta.setDisplayName(name);
		iMeta.setLore(null);
		itemStack.setItemMeta(iMeta);
		return itemStack;
	}

	public void takeItems(Player player) {
		for (ItemStack item : this.pariList) {
			int count = countItemInPlayerInv(player, item.getType());
			player.getInventory().remove(item.getType());
			player.getInventory().addItem(new ItemStack(item.getType(), count-item.getAmount()));
		}
		player.updateInventory();
	}

	public void rendrePari(Player player, int coeff) {
		for (ItemStack item : this.pariList)
			player.getInventory().addItem(new ItemStack(item.getType(), coeff*item.getAmount()));
		player.updateInventory();
	}

	public boolean isEnd() {
		return this.pariEnd;
	}

	public void setEnd(boolean etat) {
		this.pariEnd = etat;
	}
}