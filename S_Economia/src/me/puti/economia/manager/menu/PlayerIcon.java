package me.puti.economia.manager.menu;

import org.bukkit.inventory.ItemStack;

import me.puti.economia.api.Item;
import me.puti.economia.api.ItemType;
import me.puti.economia.manager.Users;
import me.puti.economia.publish.EconomiaAPI;

public class PlayerIcon {

	String name;
	boolean magnata = false;
	int pos = 0;
	
	public PlayerIcon(String name, boolean magnata, int pos) {
		this.name = name;
		this.magnata = magnata;
		this.pos  = pos;
	}
	
	
	public ItemStack getItem() {
		ItemStack item = new Item("§e"+name).setItemType(ItemType.Skull_Item).setSkull(name).addLore("  §f["+getPos()+"° Lugar]").addLore(" §7"+EconomiaAPI.formatDefault(Users.getUser(name).getSaldo())).addLore("").Build();
		return item;
	}
	
	public int getPos() {
		return pos;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isMagnata() {
		return magnata;
	}
	
	public Double getSaldo() {
		return Users.getUser(name).getSaldo();
	}
}
