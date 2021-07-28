package me.puti.chat.manager.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.puti.chat.api.Item;
import me.puti.chat.api.ItemType;
import me.puti.chat.manager.ChatManager;
import me.puti.chat.manager.player.ChatUser;
import me.puti.chat.manager.player.ChatUser.ChatStatus;
import me.puti.perms.S_Perms;

public class ChatMenu implements Listener {

	public ChatMenu() {
		// TODO Auto-generated constructor stub
	}

	public ChatMenu(ChatUser user) {

		Inventory inv = Bukkit.createInventory(null, 9 * 3, "Meu chat");

		ItemStack item_01 = new Item(((user.IsTell(ChatStatus.Ativado)) ? "§ePrivado Liberado" : "§cPrivado Bloqueado"))
				.setItemType(ItemType.Material_Item).setMaterial(Material.SIGN).addLore("")
				.addLore("§7Clique para liberar/bloquear o seu privado de mensagens.").addLore("").Build();

		// "§eChat Liberado"
		ItemStack item_02 = new Item(((user.IsChat(ChatStatus.Ativado)) ? "§eChat Liberado" : "§cChat Bloqueado"))
				.setItemType(ItemType.Material_Item).setMaterial(Material.SIGN).addLore("")
				.addLore("§7Clique para liberar/bloquear o seu chat geral de mensagens.").addLore("").Build();

		if (S_Perms.getUser(user.getName()).hasPermissao("chat.admin")) {
			ItemStack item_03 = new Item("§eChat Geral Liberado").setItemType(ItemType.Material_Item)
					.setMaterial(Material.SIGN).addLore("")
					.addLore("§7Clique para liberar/bloquear o chat do servidor.").addLore("").Build();
			inv.setItem(13, item_03);
		}

		inv.setItem(10, item_01);
		inv.setItem(15, item_02);

		user.getPlayer().openInventory(inv);
	}

	@EventHandler
	void event(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) {
			return;
		}

		if (e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		if (e.getCurrentItem().getType() == Material.AIR) {
			return;
		}

		String inventory = e.getInventory().getTitle();
		String item = e.getCurrentItem().getItemMeta().getDisplayName();
		Player p = (Player) e.getWhoClicked();
		ChatUser user = ChatManager.getUser(p.getName());

		if (user != null)
			if (inventory.equals("Meu chat")) {
				e.setCancelled(true);
				p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);
				if (item.equals("§ePrivado Liberado")) {
					user.setTell(ChatStatus.Desativado);
					p.closeInventory();
					p.sendMessage("§cTell bloqueado.");
					return;
				}

				if (item.equals("§eChat Liberado")) {
					user.setChat(ChatStatus.Desativado);
					p.closeInventory();
					p.sendMessage("§cChat bloqueado.");
					return;
				}

				if (item.equals("§cPrivado Bloqueado")) {
					user.setTell(ChatStatus.Ativado);
					p.closeInventory();
					p.sendMessage("§ePrivado desbloqueado.");
					return;
				}

				if (item.equals("§cChat Bloqueado")) {
					user.setChat(ChatStatus.Ativado);
					p.closeInventory();
					p.sendMessage("§eChat desbloqueado.");
					return;
				}

			}

	}

}
