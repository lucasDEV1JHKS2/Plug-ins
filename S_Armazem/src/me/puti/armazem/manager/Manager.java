package me.puti.armazem.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.armazem.Main;
import me.puti.armazem.api.config.Config;
import me.puti.armazem.api.config.S_Config;
import me.puti.armazem.commands.ArmazemCommand;
import me.puti.armazem.events.ItemDrop;
import me.puti.armazem.events.PlayerEvent;
import me.puti.armazem.manager.menu.ItemIcon;
import me.puti.armazem.manager.menu.Menu;
import me.puti.armazem.manager.player.Armazem;
import me.puti.armazem.manager.player.ArmazemType;
import me.puti.armazem.manager.player.friends.Friend;
import me.puti.armazem.manager.player.friends.Permissoes;

public class Manager {

	private static HashMap<String, Armazem> armazens = new HashMap<String, Armazem>();
	private static Config config;

	// Configs
	private static ArmazemType armazemPadrao = ArmazemType.PEGUENO;

	public Manager(JavaPlugin plugin) {
		// Load_Configs(plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerEvent(), plugin);
		Bukkit.getPluginManager().registerEvents(new Menu(), plugin);
		Bukkit.getPluginManager().registerEvents(new ItemDrop(), plugin);
		plugin.getCommand("armazem").setExecutor(new ArmazemCommand());
		Load_Configs(plugin);
		Load_Armazens();
		Load_Valores();

	}

	public void Load_Configs(JavaPlugin plugin) {
		config = new Config(plugin, "armazens.yml");

		if (!(config.existeConfig())) {
			config.saveDefaultConfig();
		}

	}

	public static void update_armazem(String name) {
		for (File file : getFiles()) {
			if (file.getName().equalsIgnoreCase(name + ".yml")) {
				S_Config c = getConfig(file.getName());
				String fileName = file.getName().replace(".yml", "");
				if (c != null) {
					List<ItemIcon> itens = new ArrayList<ItemIcon>();
					List<Friend> amigos = new ArrayList<Friend>();
					if (c.getConfig().getConfigurationSection(fileName + ".itens") != null)

						itens = Load_Itens(c, fileName);
					amigos = Load_Friends(c, fileName);

					ArmazemType type = ArmazemType.PEGUENO;
					try {
						ArmazemType.valueOf(c.getString(fileName + ".tipo"));
					} catch (Exception ignorado) {
					}
					Armazem armazem = new Armazem(fileName, type, itens);
					armazem.setItens(itens);
					armazem.setAmigos(amigos);
					armazens.put(fileName, armazem);
				}
			}
		}
	}

	private static List<Friend> Load_Friends(S_Config c, String fileName) {
		List<Friend> amigos = new ArrayList<Friend>();
		if (c.getConfig().getConfigurationSection(fileName + ".amigos") != null) {
			c.getConfig().getConfigurationSection(fileName + ".amigos").getKeys(false).forEach(amigoName -> {
				Friend amigo = new Friend(amigoName);
				List<String> permsName = c.getConfig().getStringList(fileName + ".amigos." + amigoName + ".permissoes");
				List<Permissoes> perms = new ArrayList<Permissoes>();
				for (String perm : permsName) {
					try {
						perms.add(Permissoes.valueOf(perm));
					} catch (Exception e) {
						Main.Log("§c[S_Armazem] Não possível encontrar a permissão " + perm + ".");
					}
				}
				amigo.setPermissoes(perms);
				amigos.add(amigo);
			});
			return amigos;
		} else {
			Main.Log("§cO jogador " + fileName + " não possuí nenhum amigo em seu armazem.");
		}
		return amigos;
	}

	public void Load_Armazens() {
		if (getFiles().length > 0) {
			for (File file : getFiles()) {
				S_Config c = getConfig(file.getName());
				String fileName = file.getName().replace(".yml", "");
				if (c != null) {
					List<ItemIcon> itens = new ArrayList<ItemIcon>();
					List<Friend> amigos = new ArrayList<Friend>();
					if (c.getConfig().getConfigurationSection(fileName + ".itens") != null)

						itens = Load_Itens(c, fileName);
					amigos = Load_Friends(c, fileName);
					ArmazemType type = ArmazemType.PEGUENO;
					try {
						type = ArmazemType.valueOf(c.getString(fileName + ".tipo"));
					} catch (Exception ignorado) {
					}
					Armazem armazem = new Armazem(fileName, type, itens);
					armazem.setItens(itens);
					armazem.setAmigos(amigos);
					armazens.put(fileName, armazem);
				}

			}
		} else {
			Main.Log("&c[S_Armazem] Nenhum armazem carregado.");
		}
	}

	private static List<ItemIcon> Load_Itens(S_Config c, String fileName) {
		List<ItemIcon> itens = new ArrayList<ItemIcon>();
		for (String itemName : c.getConfig().getConfigurationSection(fileName + ".itens").getKeys(false)) {
			try {
				if (itemName != null) {
					String name = c.getString(fileName + ".itens." + itemName + ".name").replace("&", "§");
					String ids = c.getString(fileName + ".itens." + itemName + ".id");
					boolean retiravel = c.getBoolean(fileName + ".itens." + itemName + ".retiravel");
					boolean negociavel = c.getBoolean(fileName + ".itens." + itemName + ".negociavel");
					int ammount = c.getInt(fileName + ".itens." + itemName + ".quantia");

					ItemStack block = null;
					if (!(ids.contains(":0"))) {
						String[] id = ids.split(":");
						Material material = Material.getMaterial(Integer.valueOf(id[0]));
						block = new ItemStack(material, 1, (short) Byte.valueOf(id[1]));
						Main.Log(block.getType().name() + "-" + fileName);
					} else {
						Material material = Material.getMaterial(Integer.valueOf(ids.replace(":0", "")));
						Main.Log(material.name() + "-" + fileName);
						block = new ItemStack(material);
					}
					ItemIcon item = new ItemIcon(fileName, name, block, retiravel, negociavel);
					itens.add(item);
				}
			} catch (Exception e) {
				Main.Log("&c[S_Armazem] " + fileName + " não possui nenhum item e seu armazem.");
			}
		}
		return itens;
	}

	public void Load_Valores() {
		String vmedio = config.getString("armazens.MEDIO.valor");
		String vgrande = config.getString("armazens.GRANDE.valor");

		ArmazemType.addValor(ArmazemType.GRANDE, Double.valueOf(vgrande));
		ArmazemType.addValor(ArmazemType.MEDIO, Double.valueOf(vmedio));
	}

	public static ArmazemType getArmazemPadrao() {
		return armazemPadrao;
	}

	public static HashMap<String, Armazem> getArmazens() {
		return armazens;
	}

	public static Armazem getArmazem(String name) {
		for (Armazem am : armazens.values()) {
			if (am.getName().equalsIgnoreCase(name.replace(".yml", ""))) {
				return am;
			}
		}
		return null;
	}

	public static S_Config getConfig(String name) {
		S_Config c = new S_Config(Main.getInstance(), name);
		return c;
	}

	public static void PlayerConfig(String name) {
		if (name.contains(".yml") && getArmazem(name) != null) {
			S_Config config = getConfig(name);
			ItemStack item = new ItemStack(Material.DIAMOND);
			config.set(name.replace(".yml", "") + ".tipo", getArmazemPadrao().name());
			config.set(name.replace(".yml", "") + ".amigos", "Sem amigos");
			config.set(name.replace(".yml", "") + ".itens", "Sem Itens");

			config.saveConfig();
			List<ItemIcon> itens = new ArrayList<ItemIcon>();
			Armazem armazem = new Armazem(name, armazemPadrao, itens);
			getArmazens().put(name.replace(".yml", ""), armazem);
		}
	}

	public static boolean IsConfig(String name) {
		if (getFile(name) != null) {
			return true;
		}
		return false;
	}

	public static void saveConfig(YamlConfiguration c, File file) {
		try {
			c.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File[] getFiles() {
		String direct = (Main.getInstance().getDataFolder() + "\\jogadores");
		File files = new File(direct);
		if (!(files.exists())) {
			files.mkdir();
		}
		return files.listFiles();
	}

	public static File getFile(String name) {
		Main.Log(name);
		for (File file : getFiles()) {
			if (file.getName().equals(name)) {
				return file;
			}
		}
		return null;
	}
}
