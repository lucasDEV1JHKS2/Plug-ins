package me.puti.caixas.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.caixas.Main;
import me.puti.caixas.api.S_Config;
import me.puti.caixas.commands.CaixaCommand;
import me.puti.caixas.events.CaixaEvents;
import me.puti.caixas.manager.caixa.Caixa;

public class Manager {

	private static S_Config caixas;
	private static HashMap<String, Caixa> CacheCaixas = new HashMap<String, Caixa>();

	public Manager() {
	}

	public Manager(JavaPlugin plugin) {
		Load_configs(plugin);
		Load_Events(plugin);
		Load_Commands(plugin);
		Load_Caixas();

		Main.Log("&e[S_Caixas] O plugin foi habilitado.");
	}

	public void Load_Events(JavaPlugin plugin) {
		PluginManager pl = Bukkit.getPluginManager();
		pl.registerEvents(new CaixaEvents(), plugin);
	}

	public void Load_Commands(JavaPlugin plugin) {
		plugin.getCommand("caixa").setExecutor(new CaixaCommand());
	}

	private static void Load_configs(JavaPlugin plugin) {
		caixas = new S_Config(plugin, "caixas.yml");
		if (!(caixas.existeConfig())) {
			caixas.saveDefaultConfig();
		}
	}

	private static void Load_Caixas() {
		if (getConfig().getString("caixas") != null) {
			caixas.getConfig().getConfigurationSection("caixas").getKeys(false).forEach(caixaName -> {
				new Caixa(caixaName);
			});
		} else {
			Main.Log("&c[S_Caixas] Nenhuma caixa criada.");
		}
	}

	public static void recarregar() {
		CacheCaixas = new HashMap<String, Caixa>();
		Load_configs(Main.getInstance());
		Load_Caixas();
	}

	public static S_Config getConfig() {
		return caixas;
	}

	public static HashMap<String, Caixa> getCaixas() {
		return CacheCaixas;
	}

	public static String getCCaixas() {
		String Ccaixas = "";
		for (Caixa caixa : getCaixas().values()) {
			if (Ccaixas.length() > 0) {
				Ccaixas = Ccaixas + "," + caixa.SpecifcName();
			} else {
				Ccaixas = caixa.SpecifcName();
			}
		}
		return Ccaixas;
	}

	public static Caixa getCaixa(String name) {
		for (Caixa caixa : getCaixas().values()) {
			if (!(name.contains("§"))) {
				if (caixa.SpecifcName().equals(name)) {
					return caixa;
				}
			} else {
				if (caixa.getName().equals(name)) {
					return caixa;
				}
			}
		}
		return null;
	}

	public static Caixa getCaixa(Short primaryId, int segundaryId) {
		for (Caixa caixa : getCaixas().values()) {
			if (caixa.getId().equalsIgnoreCase(primaryId+":"+segundaryId)) {
				return caixa;
			}
		}
		return null;
	}
}
