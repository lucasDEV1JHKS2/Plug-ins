package me.puti.economia;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.economia.api.K_Config;
import me.puti.economia.comandos.MagnataComando;
import me.puti.economia.comandos.SaldoComando;
import me.puti.economia.database.MagnataTask;
import me.puti.economia.events.Join;
import me.puti.economia.manager.menu.Top;
import me.puti.economia.publish.EconomiaAPI;

public class Main extends JavaPlugin implements Listener {
	public static Main instance;

	public EconomiaAPI economy;
	public List<String> apisCheck = new ArrayList<>();

	public static String Database = "";
	public static String Andress = "";
	public static String user = "";
	public static String password = "";

	public K_Config config = new K_Config(this, "config.yml");

	public static Main getPlugin() {
		return (Main) getPlugin(Main.class);
	}

	public boolean getAPI(String apiName) {
		return apisCheck.contains(apiName);
	}

	public boolean CheckAPI() {

		if (Bukkit.getPluginManager().getPlugin("S_Perms") != null) {
			apisCheck.add("S_Perms");
		}

		if (Bukkit.getPluginManager().getPlugin("S_Economia") != null) {
			apisCheck.add("S_Economia");
		}

		if (apisCheck.size() == 2) {
			return true;
		} else {
			ConsoleCommandSender console = Bukkit.getConsoleSender();

			if (Bukkit.getPluginManager().getPlugin("PermissionsEx") == null) {
				console.sendMessage("§c[S_Economia] APis não detectadas");
				return false;
			} else {
				apisCheck.add("PermissionsEx");
				return true;
			}
		}
	}

	
	public void Log(String message) {
	Bukkit.getConsoleSender().sendMessage(message.replace("&", "§"));	
	}
	
	public void onEnable() {
		if (CheckAPI() == false) {
			Main.getPlugin(Main.class).getPluginLoader().disablePlugin(this);
		}
		if (!config.existeConfig()) {
			config.saveDefaultConfig();
		}
		try {
			Database = config.getString("Tabela");
			user = config.getString("Usuario");
			password = config.getString("Senha");
			Andress = config.getString("Ip");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Conexao.open();
		if (!Conexao.IsConnected) {
			Log("§cConfiguração do bando de dados incompleta.");
			Log("");
			Log("&7Configura remanecente:");
			Log("");
			Log("&7Tabela: &f"+Database);
			Log("&7Usuario: &f"+user);
			Log("&7Senha: &f"+password);
			Log("&7Ip: &f"+Andress);
			Log("");
			
			Main.getPlugin(Main.class).getPluginLoader().disablePlugin(this);
		} else {
			this.economy = new EconomiaAPI();
			instance = this;
			Comandos();
			Eventos();
			Magnata();
			Log("&e[S_Economia] Plugin habilitado.");
		}
	}

	public void onDisable() {
		Conexao.close();
		Log("&c[S_Economia] Plugin desabilitado.");
	}

	private void Comandos() {
		getCommand("saldo").setExecutor((CommandExecutor) new SaldoComando());
		getCommand("magnata").setExecutor((CommandExecutor) new MagnataComando());
	}

	private void Eventos() {
		getServer().getPluginManager().registerEvents(this, (Plugin) this);
		getServer().getPluginManager().registerEvents(new Join(), (Plugin) this);
		getServer().getPluginManager().registerEvents(new Top(), (Plugin) this);
		
	}

	public void Magnata() {
		(new MagnataTask()).runTaskTimer((Plugin) this, 10L, 600L*10);	}
}
