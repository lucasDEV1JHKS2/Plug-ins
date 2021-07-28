package me.puti.armazem.manager.menu;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;

import me.puti.armazem.api.itemstack.Item;
import me.puti.armazem.api.itemstack.ItemType;
import me.puti.armazem.manager.Manager;
import me.puti.armazem.manager.player.Armazem;
import me.puti.armazem.manager.player.friends.Friend;
import me.puti.armazem.manager.player.friends.Permissoes;
import me.puti.chat.manager.ChatManager;
import me.puti.chat.manager.api.ChatAction;

import org.bukkit.inventory.ItemStack;

public class Menu implements Listener {


	public static void MenuPrincipal(Player p) {

		Inventory inv = Bukkit.createInventory(null, 9 * 4, "Meu Armazem");

		ItemStack item_01 = new Item("§eAmigos").setItemType(ItemType.Material_Item).setMaterial(Material.BOOK)
				.addLore("").addLore("§7Adicionar amigos so seu armazem.").addLore("").Build();
		ItemStack item_02 = new Item("§eMeus Itens").setItemType(ItemType.Skull_Item).setSkull("MHF_iron").addLore("")
				.addLore("§7Todos os itens da categoria 'MINÉRIO'.").addLore("").Build();

		Armazem am = Manager.getArmazem(p.getName());
		if (am != null) {
			ItemStack item_05 = new Item("§eUpgrade").setItemType(ItemType.Skull_Item).setSkull("MHF_arrowup")
					.setLore(am.getNextArmazem()).Build();

			ItemStack item = new Item("§eMeu Armazem").setItemType(ItemType.Skull_Item).setSkull(p.getName())
					.addLore("").addLore("§7Tamanho: §f" + am.getTipo())
					.addLore("§7Maximo de itens acumulados: §f" + am.getStack()).addLore("").Build();
			inv.setItem(4, item);
			inv.setItem(20, item_05);
		}
		inv.setItem(22, item_01);
		inv.setItem(24, item_02);

		p.openInventory(inv);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	void Event(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (ChatManager.getAction(p.getName()) != null && ChatManager.getAction(p.getName()) != ChatAction.Livre) {
			e.setCancelled(true);
			Armazem am = Manager.getArmazem(p.getName());
			String mensagem = e.getMessage();
			if (Bukkit.getOfflinePlayer(mensagem) == null) {
				p.sendMessage("§cEste jogador não está registrado no servidor.");
				return;
			}
			if (am != null) {
				if (am.getAmigo(mensagem) != null) {
					p.sendMessage("§cVocê já possui este jogador como amigo.");
					return;
				}
				List<Permissoes> perms = new ArrayList<Permissoes>();
				perms.add(Permissoes.PERMISSAO_DOAR);
				perms.add(Permissoes.PERMISSAO_PEGAR);
				perms.add(Permissoes.PERMISSAO_VENDER);
				perms.add(Permissoes.PERMISSAP_COLOCAR);
				perms.add(Permissoes.PERMISSAO);
				Friend amigo = new Friend(mensagem);
				amigo.setPermissoes(perms);
				am.addAmigo(amigo);
				p.sendMessage("§eVocê adicionou §f"+mensagem+"§e como seu amigo em seu armazem!");
				Armazem am2 = Manager.getArmazem(mensagem);
				if (am2 != null) {
					Friend amigo2 = new Friend(p.getName());
					amigo2.setPermissoes(perms);
					am2.addAmigo(amigo2);
					if (Bukkit.getPlayer(mensagem) != null){
						Bukkit.getPlayer(mensagem).sendMessage("§eO jogador §f"+p.getName()+"§e adicionou você como amigo no armazem.");
					}
				}
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
				ChatManager.setAction(p.getName(), ChatAction.Livre);
				return;
			}
		}
	}

	@EventHandler
	void Event(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getCurrentItem() == null) {
			return;
		}

		if (!(e.getCurrentItem().hasItemMeta())) {
			return;
		}
		String inventory = e.getInventory().getTitle();
		String item = e.getCurrentItem().getItemMeta().getDisplayName();

		if (inventory.startsWith("Armazem -")) {
			e.setCancelled(true);
			String i = item.replace("§e", "").replace("[", "").replace("]", "");
			Armazem am = Manager.getArmazem(p.getName());
			if (am != null && am.getItem(i) != null) {
				if (item.equalsIgnoreCase("§eProxima")) {
					am.getPagination().getPaginas().get(0).next();
				}

				ItemIcon icon = am.getItem(i);

				if (e.getClick() == ClickType.RIGHT) {
					icon.pegar(icon);
					return;
				}

				if (e.getClick() == ClickType.LEFT) {
					icon.vender(icon);
					return;
				}

			}
			p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);
			if (item.equals("§eVoltar")) {
				Menu.MenuPrincipal(p);
				return;
			}
			return;
		}
		if (inventory.equals("Amigos")) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);

			if (item.equals("§eVoltar")) {
				Menu.MenuPrincipal(p);
				return;
			}

			if (item.equalsIgnoreCase("§eAdicionar")) {
				p.closeInventory();
				p.sendMessage("");
				p.sendMessage("");
				p.sendMessage("§eInsira o nome do jogador:");
				p.sendMessage("§7(Digite no chat...)");
				ChatManager.setAction(p.getName(), ChatAction.Cancelar);
				return;
			}
		}
		if (inventory.equals("Meu Armazem")) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);
			Armazem am = Manager.getArmazem(p.getName());

			if (item.equals("§eUpgrade")) {
				p.closeInventory();
				am.Upar();
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
				return;
			}

			if (item.equals("§eAmigos")) {
				p.closeInventory();
				am.openFriends();
				p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);
				return;
			}

			if (item.equals("§eMeus Itens")) {
				p.closeInventory();
				try {
					am.getPagination().getPagina(0).open();
				} catch (Exception e2) {
					p.sendMessage("§cNão foi possível abrir a página 1 ou não existe itens há ser carregados.");
				}
			}

			return;
		}

	}
}
