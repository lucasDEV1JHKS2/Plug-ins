package me.puti.google.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.puti.google.Main;
import me.puti.google.api.config.S_Config;
import me.puti.google.commands.Login;
import me.puti.google.commands.Registrar;
import me.puti.google.events.PlayerEvents;
import me.puti.google.manager.configuration.LoginConfig;
import me.puti.google.manager.configuration.LoginConfig.Sistema;
import me.puti.google.manager.player.UserLogin;

public class Manager {

	private static S_Config google,logins,config;
	private static HashMap<String, UserLogin> players = new HashMap<String, UserLogin>();
	private static LoginConfig loginconfig = null;
	
	public Manager() {
		// TODO Auto-generated constructor stub
	}

	public Manager(JavaPlugin plugin) {
		Load_config(plugin);
		Load_Events(plugin);
		Load_Commands(plugin);
		Load_Config();
		Load_Logins();
		
		Main.Log("&e[S_GoogleAuth] O plugin foi habilitado.");
	}

	public void Load_Commands(JavaPlugin plugin) {
		plugin.getCommand("login").setExecutor(new Login());
		plugin.getCommand("registrar").setExecutor(new Registrar());
	}
	
	public static S_Config getGoogle() {
		return google;
	}

	public static S_Config getLogins() {
		return logins;
	}

	public static S_Config getConfig() {
		return config;
	}
	
	public static UserLogin getUser(String name) {
		if (players.get(name) == null) {
			return null;
		}
		return players.get(name);
	}
	
	public static HashMap<String, UserLogin> getUsers(){
		return players;
	}

	public void Load_config(JavaPlugin plugin) {
		google = new S_Config(plugin, "AuthGoogle.yml");
		logins = new S_Config(plugin, "logins.yml");
		config = new S_Config(plugin, "config.yml");

		if (!(google.existeConfig()))
			google.saveDefaultConfig();
		if (!(logins.existeConfig()))
			logins.saveDefaultConfig();
		if (!(config.existeConfig()))
			config.saveDefaultConfig();
	}

	public void Load_Events(JavaPlugin plugin) {
		PluginManager pl = Bukkit.getPluginManager();
		pl.registerEvents(new PlayerEvents(), plugin);
	}
	
	public void Load_Config() {
		S_Config config = getConfig();
		if (config.getString("configuracao") != null) {
			Sistema sistema = null;
			String original = "";
			
			try {
				sistema = Sistema.valueOf(config.getString("configuracao.Sistema").replace(" ", "_"));
			} catch (Exception e) {
				sistema = Sistema.Login_Padrao;
			}
			
			try {
				original = config.getString("configuracao.Conta_Original");
			} catch (Exception e) {
				original = "Sim";
			}
			
			
			loginconfig = new LoginConfig(sistema, original);
		}
	}
	
	public static LoginConfig getLoginConfig() {
		return loginconfig;
	}
	
	public void Load_Logins() {
		S_Config logins = getLogins();
		if (logins.getString("logins") != null) {
			logins.getConfig().getConfigurationSection("logins").getKeys(false).forEach( name -> {
					
				boolean original = logins.getBoolean("logins."+name+".original");
				String senha = logins.getString("logins."+name+".senha");
				
				UserLogin user = new UserLogin(name);
				user.setAccount(original);
				user.setSenha(senha);
				
				players.put(name, user);
			});
		}
	}

	
	public static UserLogin UpdateUserLogin(String name) {
		UserLogin userlogin = null;
		if (Manager.getUser(name) == null) {
			userlogin = new UserLogin(name);
			Manager.getUsers().put(name, userlogin);
		} else {
			userlogin = Manager.getUser(name);
		}
		return userlogin;
	}

	public static void updateOriginal(UUID id, String name) {
		if (Manager.getLoginConfig().IsSistema(Sistema.Login_Padrao)) {
			UserLogin userlogin = UpdateUserLogin(name);
			userlogin.setAccount(true);
		}
	}

	public static void newOriginal(String name, UUID id) {
		if (Manager.getLoginConfig().IsSistema(Sistema.Login_Padrao)) {
			UserLogin userlogin = UpdateUserLogin(name);
			userlogin.setAccount(true);
		}
	}

	public static boolean hasAccount(UUID id) {
		if (Manager.getLoginConfig().IsSistema(Sistema.Login_Padrao)) {
			String name = Bukkit.getPlayer(id).getName();
			UserLogin userlogin = UpdateUserLogin(name);

			return userlogin.IsLoged();
		} else {
			return false;
		}
	}

	public static boolean hasAccount(String name) {
		if (Manager.getLoginConfig().IsSistema(Sistema.Login_Padrao)) {
			if (Manager.getUser(name) != null) {
				return true;
			}
		}
		return false;
	}
	
	private static HttpURLConnection getConnection(String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(1000);
		connection.setReadTimeout(1000);

		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("User-Agent", "Premium-Checker");

		return connection;
	}

	
	public static boolean isOriginal(String name) {
		Player p = Bukkit.getPlayer(name);

		try {
			HttpURLConnection conn = getConnection("https://api.mojang.com/users/profiles/minecraft/" + name);
			if (conn.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = reader.readLine();
				if ((line != null) && (!line.equals("null")))
					return true;
			}
		} catch (Exception localException) {
		}
		return false;
	}

}
