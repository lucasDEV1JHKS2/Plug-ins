package me.puti.caixas.events;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.puti.caixas.manager.Manager;
import me.puti.caixas.manager.caixa.Caixa;

public class CaixaEvents implements Listener {

	@EventHandler
	void Event(PlayerInteractEvent e) {
		if (e.getItem() != null) {
			if (e.getItem().hasItemMeta()) {
				Player p = e.getPlayer();
				ItemStack item = e.getItem();
				if (item.hasItemMeta()) {
					if (Manager.getCaixa(Short.valueOf(item.getTypeId() + ""), item.getDurability()) != null) {
						Caixa caixa = Manager.getCaixa(Short.valueOf(item.getTypeId() + ""), item.getDurability());
						if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
							caixa.Remove(p);
							caixa.openMenu(p);
						}
					}
				}
			}
		}
	}

	@EventHandler
	void Event(BlockPlaceEvent e) {
		Block item = e.getBlock();
		if (Manager.getCaixa(Short.valueOf(item.getTypeId() + ""), item.getBlockPower()) != null) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	void Event(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) {
			return;
		}

		if (e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		String inventory = e.getInventory().getTitle();
		String item = e.getCurrentItem().getItemMeta().getDisplayName();
		Player p = (Player) e.getWhoClicked();

		if (inventory.contains("-")) {
			String[] caixaName = inventory.split(" - ");
			if (Manager.getCaixa(caixaName[0]) != null) {
				Caixa caixa = Manager.getCaixa(caixaName[0]);
				e.setCancelled(true);
				p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);
			}
		}

	}

}
