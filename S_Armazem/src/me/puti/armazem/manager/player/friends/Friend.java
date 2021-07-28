package me.puti.armazem.manager.player.friends;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.puti.armazem.api.config.S_Config;
import me.puti.armazem.manager.Manager;
import me.puti.armazem.manager.player.Armazem;
import me.puti.chat.Main;

public class Friend {
	public enum FriendSave {
		Save, Remove;
	}

	String name;
	Armazem armazem;
	List<Permissoes> permissoes = new ArrayList<Permissoes>();

	public Friend(String name) {
		this.name = name;
		this.armazem = Manager.getArmazem(name);
	}

	public String getName() {
		return name;
	}

	public String getFormatPermissoes() {
		String perms = "";
		int vez = 0;
		for (Permissoes perm : getPermissoes()) {
			if (vez > 0) {
				perms = perms + "§f, §6" + perm.name();
			} else {
				vez = 1;
				perms = "§6" + perm.name();
			}
		}
		return perms;
	}

	public ItemStack getItem(String name) {

		ItemStack item = new ItemStack(Material.SKULL_ITEM);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(getName());
		meta.setDisplayName(getName());
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add("§ePermissões: §f" + getFormatPermissoes());
		lore.add("");
		lore.add(((getArmazem() != null) ? "§aEste jogador possui um armazem"
				: "§cEste jogador não possui um armazem."));
		lore.add("");
		lore.add("" + ((getName() == name) ? "§aEste e você!" : "§cEste jogador compartilha seu armazem com você!"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.setAmount(1);

		return item;
	}

	public void setPermissoes(List<Permissoes> permissoes) {
		this.permissoes = permissoes;
	}

	public void addPermissao(Permissoes permissoes) {
		this.permissoes.add(permissoes);
	}

	public List<Permissoes> getPermissoes() {
		return permissoes;
	}

	public boolean IsPermissao(Permissoes permissoes) {
		for (Permissoes p : getPermissoes()) {
			if (p.name().equalsIgnoreCase(permissoes.name())) {
				return true;
			}
		}
		return false;
	}

	public List<String> getPermissions() {
		List<String> perms = new ArrayList<String>();
		for (Permissoes perm : getPermissoes()) {
			perms.add(perm.name());
		}
		return perms;
	}

	public Armazem getArmazem() {
		return armazem;
	}

	public Friend update() {
		this.armazem = Manager.getArmazem(name);
		return this;
	}

	public void save(String owner, FriendSave save) {
		S_Config config = Manager.getConfig(owner + ".yml");
		
		//config.set(owner + ".amigos." + name + ".permissoes", getPermissions());
		
		config.set(owner + ".amigos." + name + ".nivel", 1);
		
		//Main.Log(config.getName() + " this filename!");
		
		config.saveConfig();
	}

}
