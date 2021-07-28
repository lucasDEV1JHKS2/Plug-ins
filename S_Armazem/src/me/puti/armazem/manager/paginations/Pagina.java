package me.puti.armazem.manager.paginations;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.puti.armazem.api.itemstack.Item;
import me.puti.armazem.api.itemstack.ItemType;
import me.puti.armazem.manager.Manager;
import me.puti.armazem.manager.menu.ItemIcon;
import me.puti.armazem.manager.player.Armazem;

public class Pagina {

	int escala;
	int maxItems;
	List<ItemIcon> itens;
	Player p;
	int index;

	public Pagina(int index, List<ItemIcon> itens, int escala, Player p) {
		this.escala = escala;
		this.p = p;
		this.maxItems = escala - 5;
		this.itens = itens;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public void updateItens(List<ItemIcon> itens) {
		this.itens = itens;
	}

	public int getMaxPaginas() {
		if (Manager.getArmazem(p.getName()) != null) {
			Armazem am = Manager.getArmazem(p.getName());
			return am.getPagination().getPaginas().size();
		} else {
			return 1;
		}
	}

	public Pagination getPagination() {
		Armazem am = Manager.getArmazem(p.getName());
		return am.getPagination();
	}

	public void next() {
		if (getPagination().getPagina(index + 1) != null) {
			Pagina pagina = getPagination().getPagina(index + 1);
			p.closeInventory();
			pagina.open();
		}
	}

	public void open() {
		Inventory inv = Bukkit.createInventory(null, escala, "Armazem - "+index+"/"+getMaxPaginas());
		int slot = 10;
		int pass = 0;
		for (ItemIcon item : itens) {
			ItemStack item_01 = item.getItem();

			inv.setItem(slot, item_01);
			if (pass == 6) {
				slot = slot+3;
				pass = 0;
			}else {
				slot++;
				pass++;
			}
		}

		ItemStack item = new Item((getMaxPaginas() > 1) ? "§eProxima" : "§cSem páginas")
				.setItemType(ItemType.Skull_Item).setSkull("MHF_arrowright").Build();

		inv.setItem(8, item);
		p.openInventory(inv);
	}

}
