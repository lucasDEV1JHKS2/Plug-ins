package me.puti.armazem.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import me.puti.armazem.ArmazemAPI;
import me.puti.armazem.api.message.ActionBar;
import me.puti.armazem.manager.menu.ItemIcon;
import me.puti.armazem.manager.player.Armazem;

public class ItemDrop implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	void Event(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Armazem am = ArmazemAPI.getArmazem(p.getName());
		if (am != null) {
			if (ArmazemAPI.IsEquip(p)) {
				Block b = e.getBlock();
				ItemStack block = b.getState().getData().toItemStack(1);
				int ammount = b.getDrops().size();
				if (block.getType() == null || block.getType() == Material.AIR) {
					return;
				}
				if (block.getType() == Material.SKULL) {
					return;
				}

				if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
					return;
				}
				try {
					ItemIcon item = new ItemIcon(p.getName(), "§e" + block.getType(), block, true, false);
					am.addItem(item);
					b.getDrops().clear();
					ActionBar.sendAction(p, "§fx" + ((block.getAmount() > 1) ? block.getAmount() + " itens coletados"
							: block.getAmount() + " item coletado") + "§e para seu armazem.");
				} catch (Exception e2) {
					ActionBar.sendAction(p, "§cOpa parece que você deixo item cair e desapareceu : D!");
					e2.printStackTrace();
				}
			}
		}

	}

	@EventHandler
	void Event(ItemSpawnEvent e) {
		e.setCancelled(true);
	}
}
