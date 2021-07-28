package me.puti.caixas.manager.task;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.puti.caixas.manager.caixa.Caixa;
import me.puti.caixas.manager.caixa.CaixaItem;
import me.puti.caixas.manager.caixa.Caixa.CaixaStatus;

public class CaixaMenuTask extends BukkitRunnable {

	Inventory inv = null;
	Caixa caixa = null;
	int sort = 0, time = 15;
	int recent_color = 0;
	CaixaItem premio;

	public CaixaMenuTask(Inventory inv, Caixa caixa) {
		this.inv = inv;
		this.caixa = caixa;
	}

	@Override
	public void run() {
		if (caixa != null) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (caixa.IsStatus(p.getName()) == CaixaStatus.Fechada) {
					cancel();
				} else {
					if (inv != null) {
						DecorationMenu();
						updateMenu(inv);
						p.updateInventory();
						if (sort == time) {
							p.closeInventory();
							if (premio != null) {
								p.getInventory().addItem(premio.getItem());
							} else {
								p.sendMessage("§cParece que você não consegui nenhum prêmio. "+premio);
							}
							caixa.setStatus(p.getName(), CaixaStatus.Fechada);
							SendAll("&6[CAIXA] &eO jogador &f" + p.getName() + " &eabriu um caixa §5[" + caixa.getType()
									+ "]&e.");
						} else {
							sort++;
						}
					}
				}
			}
		}

	}

	public void SendAll(String message) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.sendMessage(message.replace("&", "§"));
		}
	}

	public void updateMenu(Inventory inv) {
		int itemNumber = (caixa.RandomNex() + 2);

		for (CaixaItem item : caixa.getItens()) {
			//Main.Log("Valor ->" + itemNumber);
			//Main.Log("Comparado ->" + item.getPorcentagem());
			if (item.ComparePorcentagem(itemNumber)) {
					this.premio = item;
					inv.setItem(13, item.getItem());
			}
		}
	}
	
	public ItemStack getColor() {
		ItemStack item = null;
		switch (recent_color) {
		case 10:
			item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
			break;
		case 4:
			item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);
			break;
		case 3:
			item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
			break;
		default:
			item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10);
			break;
		}
		return item;
	}

	public void DecorationMenu() {
		ItemStack item = getColor();
		//SET PANELS
		inv.setItem(9, item);
		inv.setItem(10, item);
		inv.setItem(11, item);
		inv.setItem(12, item);

		inv.setItem(14, item);
		inv.setItem(15, item);
		inv.setItem(16, item);
		inv.setItem(17, item);
	}

}
