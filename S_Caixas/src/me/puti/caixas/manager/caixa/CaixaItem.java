package me.puti.caixas.manager.caixa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.puti.economia.publish.EconomiaAPI;

public class CaixaItem {

	String name;
	String customName;
	String id;
	ItemStack item;
	List<String> lore = new ArrayList<String>();
	int porcentagem = 0;

	// ECONOMIA
	Double valor = 0D;

	public CaixaItem(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public CaixaItem setPorcentagem(String p) {
		this.porcentagem = Integer.valueOf(p.replace("%", ""));
		return this;
	}

	public CaixaItem setCustomName(String name) {
		this.customName = name;
		return this;
	}

	public CaixaItem setLore(List<String> lore) {
		this.lore = lore;
		return this;
	}

	public List<String> getLore() {
		List<String> newlore = new ArrayList<String>();
		for (String line : lore) {
			newlore.add(line.replace("&", "§").replace("<valor>", EconomiaAPI.formatDefault(getValor())));
		}
		return newlore;
	}

	public Double getValor() {
		return valor;
	}

	public int getPorcentagem() {
		return porcentagem;
	}

	public String getCustomName() {
		return customName.replace("&", "§");
	}

	public boolean ComparePorcentagem(int p) {
		int maxP = getPorcentagem() + 5;
		int minP = getPorcentagem() - 5;

		if (p == getPorcentagem() || p >= minP && p <= maxP) {
			return true;
		} else {
			return false;
		}
	}

	public CaixaItem setId(String id) {
		this.id = id;
		return this;
	}

	public Material getMaterial() {
		String newid = "";
		if (id.contains(":0")) {
			newid = id.replace(":0", "");
			Material material = Material.getMaterial(Integer.valueOf(newid));
			return material;
		} else {
			String[] splitId = id.split(":");
			Material material = Material.getMaterial(Integer.valueOf(splitId[0]));
			return material;
		}
	}

	public Short getData() {
		if (!(id.contains(":0"))) {

			String[] splitId = id.split(":");
			return Short.valueOf(splitId[0]);
		} else {
			return 0;
		}
	}

	public ItemStack getItem() {
		Material material = getMaterial();
		ItemStack item = null;
		if (getData() != 0) {
			item = new ItemStack(material, 1, (short) getData());
		} else {
			item = new ItemStack(material);
		}
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(getCustomName());
		itemMeta.setLore(getLore());
		item.setAmount(1);
		item.setItemMeta(itemMeta);

		return item;
	}
	
	public ItemStack getItemInfo() {
		Material material = getMaterial();
		ItemStack item = null;
		if (getData() != 0) {
			item = new ItemStack(material, 1, (short) getData());
		} else {
			item = new ItemStack(material);
		}
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(getCustomName());
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add("§7Porcentagem: "+getPorcentagem()+"%");
		lore.add("§7Id: "+material.getId()+":"+getData());
		lore.add("");
		
		itemMeta.setLore(lore);
		item.setAmount(1);
		item.setItemMeta(itemMeta);

		return item;
	}

}
