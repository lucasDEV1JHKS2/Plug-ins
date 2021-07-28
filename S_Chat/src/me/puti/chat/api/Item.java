package me.puti.chat.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Item {

	String itemName = "§c[Sem nome]";
	List<String> lore = new ArrayList<String>();
	Material material = Material.AIR;
	String ownerSkull = "SrPuti_";
	ItemType customItem = ItemType.None;
	Enchantment enchant = null;
	int enchatLevel = 0;
	boolean glow = false;

	public Item(String itemName) {
		this.itemName = itemName;
	}

	public Item addLore(String value) {
		lore.add(value.replace("&", "§"));
		return this;
	}

	public Item setEnchant(Enchantment value) {
		this.enchant = value;
		return this;
	}

	public Item setItemType(ItemType value) {
		customItem = value;
		return this;
	}

	public Item setMaterial(Material value) {
		this.material = value;
		return this;
	}

	public Item setSkull(String value) {
		this.ownerSkull = value;
		return this;
	}

	public Item setLore(List<String> value) {
		this.lore = value;
		return this;
	}

	public ItemStack Build() {
		ItemStack item_01 = null;
		if (customItem == ItemType.Skull_Item) {
			item_01 = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta item_01Meta = (SkullMeta) item_01.getItemMeta();
			item_01Meta.setDisplayName(itemName);
			item_01Meta.setLore(lore);
			item_01Meta.setOwner(ownerSkull);

			item_01.setItemMeta(item_01Meta);
		} else {
			item_01 = new ItemStack(material);
			ItemMeta item_01Meta = item_01.getItemMeta();
			item_01Meta.setDisplayName(itemName);
			item_01Meta.setLore(lore);

			item_01Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item_01Meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
			item_01Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item_01Meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
			item_01Meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			item_01Meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

			if (enchant != null) {
				item_01Meta.addEnchant(enchant, enchatLevel, glow);
			}
			item_01.setItemMeta(item_01Meta);
		}

		return item_01;
	}
}
