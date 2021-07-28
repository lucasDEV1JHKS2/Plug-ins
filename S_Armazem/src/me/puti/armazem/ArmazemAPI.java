package me.puti.armazem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.puti.armazem.manager.Manager;
import me.puti.armazem.manager.menu.ItemIcon;
import me.puti.armazem.manager.player.Armazem;

public class ArmazemAPI {

	public static Armazem getArmazem(String name) {
		if (Manager.getArmazem(name) != null) {
			return Manager.getArmazem(name);
		} else {
			Armazem armazem = setPlayer(name);
			return armazem;
		}
	}
	
	public static Armazem setPlayer(String name) {
		List<ItemIcon> itens = new ArrayList<ItemIcon>();
		Armazem armazem = new Armazem(name, Manager.getArmazemPadrao(), itens);
		Manager.PlayerConfig(name+".yml");
		Manager.getArmazens().put(name, armazem);
		return armazem;
	}
	
	public static boolean IsEquip(Player p ) {
		boolean equip = false;
		switch (p.getItemInHand().getType()) {
		case DIAMOND_PICKAXE:
			equip = true;
			break;
		case IRON_PICKAXE:
			equip = true;
			break;
		case GOLD_PICKAXE:
			equip = true;
			break;
		case WOOD_PICKAXE:
			equip = true;
			break;
		case STONE_PICKAXE:
			equip = true;
			break;
		case DIAMOND_SPADE:
			equip = true;
			break;
		case GOLD_SPADE:
			equip = true;
			break;
		case IRON_SPADE:
			equip = true;
			break;
		case WOOD_SPADE:
			equip = true;
			break;
		case STONE_SPADE:
			equip = true;
			break;
		default:
			equip = false;
			break;
		}
		return equip;
	}
	
	public static void addItem(String name, ItemIcon item) {
		if (Manager.getArmazem(name) != null && item != null) {
			Manager.getArmazem(name).addItem(item);
		}else {
			setPlayer(name);
		}
	}

	public static void removeItem(String name, ItemIcon item) {
		if (Manager.getArmazem(name) != null && item != null) {
			Manager.getArmazem(name).removeItem(item);
		}else {
			setPlayer(name);
		}
	}

	public static void OpenMenu(String name, int index) {
		if (Manager.getArmazem(name) != null) {
			Armazem armazem = Manager.getArmazem(name);
			armazem.getPagination().print(index);
		}else {
			setPlayer(name);
		}
	}

}
