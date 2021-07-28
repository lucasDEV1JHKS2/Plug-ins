package me.puti.google.manager.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.puti.google.Main;
import me.puti.google.api.config.S_Config;
import me.puti.google.manager.Manager;

public class UserLogin {

	String name;
	String senha;
	UUID id;
	boolean priemiumAccount = false, logado = false;

	public UserLogin(String name) {
		this.name = name;
		this.id = Bukkit.getOfflinePlayer(name).getUniqueId();
	}

	public void setAccount(boolean priemiumAccount) {
		this.priemiumAccount = priemiumAccount;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha() {
		return senha;
	}

	public boolean isOriginal() {
		return priemiumAccount;
	}

	public void setState(boolean logado) {
		this.logado = logado;
	}

	public boolean IsLoged() {
		return logado;
	}

	public String getName() {
		return name;
	}

	public UUID getId() {
		return id;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(name);
	}

	public void save() {
		try {
			S_Config logins = Manager.getLogins();
			logins.set("logins." + name + ".original", isOriginal());
			logins.set("logins." + name + ".senha", getSenha());
			logins.set("logins." + name + ".id", getId().toString());

			logins.saveConfig();
		} catch (Exception e) {
			Main.Log("&c[S_GoogleAuth] Não foi possível salvar o cache do usuário " + name + ".");
		}
	}

}
