package me.puti.economia.manager.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.puti.economia.api.Item;
import me.puti.economia.api.ItemType;
import me.puti.economia.database.MetodosEconomy;
import me.puti.economia.publish.EconomiaAPI;

public class Top implements Listener{

	private static HashMap<Integer, PlayerIcon> playersIcon = new HashMap<Integer, PlayerIcon>();
	private static HashMap<Integer, List<PlayerIcon>> newTops = new HashMap<Integer, List<PlayerIcon>>();
	private static List<PlayerIcon> list = new ArrayList<PlayerIcon>();

	public static void Open(Player p) {
		if (playersIcon.size() == 0) {
			Top.setTopList(MetodosEconomy.GetMoneyTop());
		}
		if (EconomiaAPI.TopList().size() == 0) {
			p.sendMessage("§cNão possui jogadores no top money para mostra.");
			return;
		}
		Inventory inv = Bukkit.createInventory(null, 9 * 5, "Money Top");
		int slot = 10, prioridade = 1, passed = 1, line = 0;
		while (getPrioridade(prioridade) != null) {
			PlayerIcon player = getPrioridade(prioridade);
			ItemStack item = player.getItem();
			int newList = 1;
			if (line <= 5) {
				inv.setItem(slot, item);
				if (passed < 7) {
					slot++;
					passed++;
				} else {
					line++;
					passed = 0;
					slot = slot + 3;
				}
				prioridade++;
			}else {
				list.add(player);
				newTops.put(newList, list);
				newList++;
				
				ItemStack proxima = new Item("§eProximo").setItemType(ItemType.Material_Item).setMaterial(Material.SIGN).addLore("").addLore("§7Ir para proxima página").addLore("").Build();
				inv.setItem(38, proxima);
			}
		}

		p.openInventory(inv);
	}

	public static void setTopList(HashMap<Integer, PlayerIcon> playersIcon) {
		Top.playersIcon = playersIcon;
	}

	public static PlayerIcon getPrioridade(int p) {
		return playersIcon.get(p);
	}

	@EventHandler
	void Event(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) {
			return;
		}
		if (!(e.getCurrentItem().hasItemMeta())) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		String inventory = e.getInventory().getTitle();

		if (inventory.equalsIgnoreCase("Money Top")) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);

		}
	}
}
