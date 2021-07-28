package me.puti.armazem.manager.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.puti.armazem.Main;
import me.puti.armazem.api.config.S_Config;
import me.puti.armazem.api.itemstack.Item;
import me.puti.armazem.api.itemstack.ItemType;
import me.puti.armazem.api.message.ActionBar;
import me.puti.armazem.manager.Manager;
import me.puti.armazem.manager.menu.ItemIcon;
import me.puti.armazem.manager.menu.ItemIcon.Save;
import me.puti.armazem.manager.menu.Menu;
import me.puti.armazem.manager.paginations.Pagina;
import me.puti.armazem.manager.paginations.Pagination;
import me.puti.armazem.manager.player.friends.Friend;
import me.puti.armazem.manager.player.friends.Friend.FriendSave;
import me.puti.economia.publish.EconomiaAPI;

public class Armazem {

	String name;
	ArmazemType type;
	List<ItemIcon> itens = new ArrayList<ItemIcon>();
	List<Friend> amigos = new ArrayList<Friend>();
	Pagination pagination = null;

	public Armazem(String name, ArmazemType type, List<ItemIcon> itens) {
		this.name = name;
		this.type = type;
		this.itens = itens;
	}

	public List<Friend> getAmigos() {
		return amigos;
	}

	public void addAmigo(Friend amigo) {
		if (getAmigos().size() < 10) {
			amigos.add(amigo);
		}else {
			getPlayer().sendMessage("§cVocê não pode adicionar mais amigos em seu armazem.");
		}
	}

	public void setAmigos(List<Friend> amigos) {
		this.amigos = amigos;
	}

	public void setItens(List<ItemIcon> itens) {
		this.itens = itens;
	}

	public Friend getAmigo(String name) {
		for (Friend amigo : getAmigos()) {
			if (amigo.getName().equals(name)) {
				return amigo;
			}
		}
		return null;
	}

	public void openArmazem(String name) {
		Friend amigo = getAmigo(name);
		if (amigo != null) {
			amigo.getArmazem().Open();
			getPlayer().updateInventory();
		} else {
			getPlayer().sendMessage("§cAmigo não encontrado.");
		}
	}

	public void addItem(ItemIcon item) {
		if (getItemId(item.getId()) == null) {
			this.itens.add(item);
		} else {
			ItemIcon items = getItemId(item.getId());
			items.addAmmount(item.getAmmount());
		}
		save();
	}

	public void Upar() {
		if (type == ArmazemType.PEGUENO) {
			Double valor = ArmazemType.getValor(ArmazemType.MEDIO);
			if (!(EconomiaAPI.compare(getName(), valor))) {
				sendMessage("§cVocê precisar ter " + EconomiaAPI.formatDefault(ArmazemType.getValor(ArmazemType.MEDIO))
						+ " para upar seu armazem.");
				return;
			} else {
				EconomiaAPI.RemoverMoney(getName(), valor);
				type = ArmazemType.MEDIO;
				sendMessageAll("&e[Armazem]§f Armazem upado para MÉDIO.");
				save();
				return;
			}

		} else if (type == ArmazemType.MEDIO) {
			Double valor = ArmazemType.getValor(ArmazemType.MEDIO);
			if (!(EconomiaAPI.compare(getName(), valor))) {
				sendMessage("§cVocê precisar ter " + EconomiaAPI.formatDefault(ArmazemType.getValor(ArmazemType.GRANDE))
						+ " para upar seu armazem.");
				return;
			} else {
				EconomiaAPI.RemoverMoney(getName(), valor);
				type = ArmazemType.GRANDE;
				sendMessageAll("&e[Armazem]§f Armazem upado para GRANDE.");
				save();
				return;
			}
		} else if (type == ArmazemType.GRANDE) {
			sendMessageAll("&c[Armazem] O seu armazem já está no nível maximo.");
			return;
		}
	}

	public List<String> getNextArmazem() {
		List<String> lista = new ArrayList<String>();

		if (getTipo() == ArmazemType.PEGUENO) {
			lista.add("");
			lista.add("§eProxim: §fMÉDIO");
			lista.add("§eValor: §f" + ArmazemType.getValor(ArmazemType.MEDIO));
			lista.add("");
			lista.add("§7Clique para upar seu armazem!");
			lista.add("");
		} else if (getTipo() == ArmazemType.MEDIO) {
			lista.add("");
			lista.add("§eProxim: §fGRANDE");
			lista.add("§eValor: §f" + ArmazemType.getValor(ArmazemType.GRANDE));
			lista.add("");
			lista.add("§7Clique para upar seu armazem!");
			lista.add("");

		} else if (getTipo() == ArmazemType.GRANDE) {
			lista.add("");
			lista.add("§cNão e possível upar mais.");
			lista.add("");
		}

		return lista;
	}

	public void sendMessageBar(String message) {
		ActionBar.sendAction(getPlayer(), message.replace("&", "§"));
	}

	public void sendMessage(String message) {
		getPlayer().sendMessage(message.replace("&", "§"));
	}

	public void sendMessageAll(String message) {
		sendMessage(message);
		sendMessageBar(message);
	}

	public void removeItem(ItemIcon item) {
		if (getItem(item.getMaterial()) != null && getItens().size() != 0) {
			List<ItemIcon> itens = new ArrayList<ItemIcon>();
			for (ItemIcon remove : getItens()) {
				if (remove.getId().equals(item.getId())) {
					if (item.getAmmount() <= remove.getAmmount()) {
						if (item.getAmmount() == remove.getAmmount()) {
							remove.save(Save.Remove);
						} else if (item.getAmmount() - remove.getAmmount() != 0) {
							remove.setAmmount(item.getAmmount());
						}
					}
				} else {
					itens.add(remove);
				}
			}
			this.itens = itens;
			update();
		}
	}

	public void update() {
		updatePaginas();
		// Manager.update_armazem(getName());
	}

	public void updatePaginas() {
		if (getPagination().getPaginas().size() == 1) {
			getPagination().getPaginas().get(0).updateItens(getItens());
		} else {
			updatePagination();
		}
	}

	public void updatePagination() {
		for (Pagina pag : getPagination().getPaginas()) {
			pag.updateItens(getItens());
		}
	}

	public List<ItemIcon> getItens() {
		return itens;
	}

	public ItemIcon getItem(Material material) {
		for (ItemIcon item : getItens()) {
			if (item.getMaterial().name().equals(material.name())) {
				return item;
			}
		}
		return null;
	}

	public ItemIcon getItem(ItemStack item) {
		for (ItemIcon i : getItens()) {
			if (i.getItem().equals(item)) {
				return i;
			}
		}
		return null;
	}

	public ItemIcon getItemId(String id) {
		for (ItemIcon i : getItens()) {
			if (i.getId().equals(id)) {
				return i;
			}
		}
		return null;
	}

	public void openFriends() {
		Inventory inv = Bukkit.createInventory(null, 9 * 4, "Amigos");

		int slot = 9;
		for (Friend amigo : getAmigos()) {
			inv.setItem(slot, amigo.getItem(getName()));
			slot++;
		}

		ItemStack item_01 = new Item("§eVoltar").setItemType(ItemType.Skull_Item).setSkull("MHF_arrowleft").addLore("")
				.addLore("§7Voltar ao menu principal.").addLore("").Build();
		ItemStack item_02 = new Item("§eAdicionar").setItemType(ItemType.Skull_Item).setSkull("MHF_arrowdown")
				.addLore("").addLore("§7Adicionar um novo amigo.").addLore("").Build();

		inv.setItem(0, item_01);
		inv.setItem(8, item_02);

		getPlayer().openInventory(inv);
	}

	public ItemIcon getItem(String name) {
		for (ItemIcon item : getItens()) {
			if (item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public int getEscala() {
		if (getTipo() == ArmazemType.PEGUENO) {
			return 9 * 4;
		}

		if (getTipo() == ArmazemType.MEDIO) {
			return 9 * 5;
		}

		if (getTipo() == ArmazemType.GRANDE) {
			return 9 * 6;
		}
		return 9 * 4;
	}

	public int getLine() {
		if (getTipo() == ArmazemType.PEGUENO) {
			return 4;
		}

		if (getTipo() == ArmazemType.MEDIO) {
			return 5;
		}

		if (getTipo() == ArmazemType.GRANDE) {
			return 6;
		}
		return 4;
	}

	public ArmazemType getTipo() {
		return type;
	}

	public int getStack() {
		return ArmazemType.getStack(getTipo());
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(getName());
	}

	public Pagination getPagination() {
		if (pagination == null) {
			pagination = new Pagination(getPlayer());
		}
		return pagination;
	}

	public void Open() {
		Menu.MenuPrincipal(getPlayer());
	}

	public void save() {
		S_Config config = Manager.getConfig(name + ".yml");
		config.set(name + ".tipo", getTipo().name());
		try {
			for (ItemIcon item : itens) {

				config.set(name.replace(".yml", "") + ".itens." + item.getName() + ".id", item.getId());
				config.set(name.replace(".yml", "") + ".itens." + item.getName() + ".name",
						item.getCustomName().replace("§", "&"));
				config.set(name.replace(".yml", "") + ".itens." + item.getName() + ".quantia", item.getAmmount());
				config.set(name.replace(".yml", "") + ".itens." + item.getName() + ".retiravel", item.getRetiravel());
				config.set(name.replace(".yml", "") + ".itens." + item.getName() + ".negociavel", item.getNegociavel());

			}
		} catch (Exception e) {
			Main.Log("§c[S_Armazem] Não foi possível salvar os itens do jogador " + getName());
		}
		try {
			for (Friend amigo : getAmigos()) {
				amigo.save(getPlayer().getName(), FriendSave.Save);
			}
			Main.Log("§e[S_Armazem] Amigos salvos.");
		} catch (Exception e) {
			Main.Log("§c[S_Armazem] Não foi possível salvar os amigos do jogador " + getName());
		}
		config.saveConfig();

		Main.Log("§e[S_Armazem] Armazem salvo.");
		//Manager.update_armazem(getName());
	}

}
