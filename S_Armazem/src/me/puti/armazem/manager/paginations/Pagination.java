package me.puti.armazem.manager.paginations;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.puti.armazem.manager.Manager;
import me.puti.armazem.manager.menu.ItemIcon;
import me.puti.armazem.manager.player.Armazem;

public class Pagination {

	Player player;
	int indexPagina = 0;
	private static List<Pagina> paginas = null;

	public Pagination(Player player) {
		this.player = player;
		try {
			loadPaginas();
		} catch (Exception ignored) {
			player.sendMessage("§cNão foi possível carregar as páginas do seu armazem.");
		}
	}

	public List<Pagina> getPaginas() {
		return paginas;
	}

	public void loadPaginas() {
		this.paginas = new ArrayList<Pagina>();
		Armazem am = Manager.getArmazem(player.getName());
		if (am != null) {
			int slot = 10;
			int space = 3;
			int passed = 0;
			int line = 0;
			int maxItens = 0;
			int escala = am.getEscala() - 5;
			List<ItemIcon> itens = new ArrayList<ItemIcon>();
			for (int i = 0; i < am.getItens().size(); i++) {
				if (line <= 5) {
					ItemIcon item = am.getItens().get(i);
					item.setStack(am.getStack());
						item.setSlot(slot);
						itens.add(item);
						if (maxItens < escala) {
							maxItens++;
							if (passed < 6) {
								passed++;
								slot++;
							} else {
								line++;
								passed = 0;
								slot = slot + space;
							}
						}else {
							indexPagina++;
						}
				}
			}
			Pagina p = new Pagina(indexPagina, itens, am.getEscala(), player);
			paginas.add(p);
		}
	}

	public static Pagina getPagina(int index) {
		for (Pagina p : paginas) {
			if (p.getIndex() == index) {
				return p;
			}
		}
		return null;
	}

	public void print(int index) {
		player.closeInventory();
		Pagina p = getPagina(index);
		if (p != null) {
			p.open();
		}
	}

}
