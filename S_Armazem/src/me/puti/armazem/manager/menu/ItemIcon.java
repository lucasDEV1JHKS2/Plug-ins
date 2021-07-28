package me.puti.armazem.manager.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.puti.armazem.api.config.S_Config;
import me.puti.armazem.manager.Manager;
import me.puti.armazem.manager.player.Armazem;

public class ItemIcon {

	String name, customname;
	int ammount = 1;
	int slot = 0;
	Material material = Material.AIR;
	boolean retiravel;
	boolean negociavel;
	ItemStack item = null;
	int stack;
	String owner = "";
	String id = "0:0";

	public ItemIcon(String owner, String name, ItemStack item, boolean retiravel, boolean negociavel) {
		this.name = material.name();
		this.owner = owner;
		this.ammount = ((item.getAmount() == 0) ? 1 : item.getAmount());
		this.material = item.getType();
		this.id = item.getTypeId() + ":" + item.getDurability();
		this.customname = name;
		this.item = item;
		this.retiravel = retiravel;
		this.negociavel = negociavel;
	}

	public String getId() {
		return id;
	}

	public int getStack() {
		return stack;
	}

	public void setStack(int stack) {
		this.stack = stack;
	}

	public void vender(ItemIcon item) {
		if (negociavel) {
			Armazem am = Manager.getArmazem(owner);
			if (am != null) {
				close();
				sendMessage("§eVocê vendeu §fx" + item.getAmmount() + " " + item.getName() + "§e por <valor>.");
				am.removeItem(item);
				am.save();
			} else {
				sendMessage("§cSeu armazem não foi carregado.");
			}
		} else {
			close();
			sendMessage("§cEste item não pode ser negociável na loja.");
		}
	}

	public void pegar(ItemIcon item) {
		if (retiravel) {

			if (Manager.getArmazem(owner) != null) {
				Armazem am = Manager.getArmazem(owner);
				close();
				AddItem(item);
				sendMessage("§eVocê retirou §fx" + item.getAmmount() + " " + item.getName() + "§e do armazem.");
				am.removeItem(item);
				am.save();
			} else {
				sendMessage("§cSeu armazem não foi carregado.");
			}
		} else {
			close();
			sendMessage("§cEste item não pode ser retirado.");
		}
	}

	public void sendMessage(String messge) {
		Bukkit.getPlayer(owner).sendMessage(messge.replace("&", "§"));
	}

	public void AddItem(ItemIcon item) {
		Bukkit.getPlayer(owner).getInventory().addItem(item.getItem());
	}

	public void removeItem(ItemIcon item, int amount) {
		ItemStack itemn = new ItemStack(this.item.getType(), 1, (short) this.item.getDurability());
		itemn.setAmount(amount);
		Bukkit.getPlayer(owner).getInventory().remove(itemn);
	}

	public void close() {
		Bukkit.getPlayer(owner).closeInventory();
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public boolean getRetiravel() {
		return retiravel;
	}

	public boolean getNegociavel() {
		return negociavel;
	}

	public Material getMaterial() {
		return material;
	}

	public String getName() {
		if (name == "AIR") {
			this.name = getItem().getType().name();
			this.material = getItem().getType();
		}
		return name;
	}

	public void addAmmount(int value) {
		this.ammount += value;
	}

	public void removeAmmount(int value) {
		if (value <= ammount) {
			this.ammount = ammount - value;
		}
	}

	public void setAmmount(int value) {
		if (ammount < stack && value < stack) {
			this.ammount = value;
		}
	}

	public String getCustomName() {
		return customname;
	}

	public int getAmmount() {
		return ammount;
	}

	public List<String> getLore() {
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add("§ePossui §f" + getAmmount() + ((getAmmount() > 1) ? " item" : " itens"));
		lore.add("");
		lore.add("");
		lore.add(((getRetiravel()) ? "§aClique com o direto parar retira-ló." : "§cNão possível retirar este item."));
		lore.add("");
		lore.add(((getNegociavel()) ? "§aClique com o esquerdo parar vender este item."
				: "§cNão possível vender este item na loja."));
		lore.add("");

		return lore;
	}

	public boolean compareItem(ItemStack item) {
		if (item.getType() == getItem().getType()) {
			if (item.getTypeId() == getItem().getTypeId()) {
				if (item.getDurability() == getItem().getDurability()) {
					return true;
				}
			}
		}
		return false;
	}

	public ItemStack getItem() {
		String[] d = getId().split(":");
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(getLore());
		itemMeta.setDisplayName(getCustomName());
		item.setDurability(Short.valueOf(d[1]));
		item.setAmount(getAmmount());
		item.setItemMeta(itemMeta);
		return item;
	}

	public void save(Save save) {
		S_Config c = Manager.getConfig(owner + ".yml");

		if (save == Save.Save) {
			c.set(name.replace(".yml", "") + ".itens." + getName() + ".id", getId());
			c.set(name.replace(".yml", "") + ".itens." + getName() + ".name", getCustomName().replace("§", "&"));
			c.set(name.replace(".yml", "") + ".itens." + getName() + ".quantia", getAmmount());
			c.set(name.replace(".yml", "") + ".itens." + getName() + ".retiravel", getRetiravel());
			c.set(name.replace(".yml", "") + ".itens." + getName() + ".negociavel", getNegociavel());
			c.saveConfig();

		} else if (save == Save.Remove) {
			c.set(owner + ".itens." + getName(), null);
			c.saveConfig();
		}
	}

	public enum Save {
		Remove, Save
	}
}
