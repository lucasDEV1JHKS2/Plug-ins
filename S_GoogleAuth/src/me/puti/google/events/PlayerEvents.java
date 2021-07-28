package me.puti.google.events;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import me.puti.google.manager.Manager;
import me.puti.google.manager.configuration.LoginConfig.Sistema;
import me.puti.google.manager.player.UserLogin;

public class PlayerEvents implements Listener {

	private ArrayList<UUID> authlocked = new ArrayList<UUID>();

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (Manager.getLoginConfig().IsSistema(Sistema.Login_Padrao)) {
			UUID id = player.getUniqueId();
			String name = player.getName();
			if (Manager.isOriginal(player.getName()) && Manager.getLoginConfig().IsOriginal()) {
				if (Manager.hasAccount(id)) {
					Manager.updateOriginal(id, name);
				} else {
					Manager.newOriginal(name, id);
				}
				UserLogin user = Manager.UpdateUserLogin(player.getName());
				user.setAccount(true);
				user.setSenha("");
				user.setState(true);
				user.save();
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				player.sendMessage("§eConta original detectada, você pode entrar no servidor sem precisar pelo processo registro/login.");
			} else {
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);

				if (Manager.hasAccount(name)) {
					player.sendMessage("");
					player.sendMessage(" §eLogin - Entre na sua conta");
					player.sendMessage("");
					player.sendMessage("§7Seja bem vindo ao servidor!");
					player.sendMessage("");

					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
					player.sendMessage("§c/login <senha>.");
				} else {

					player.sendMessage("");
					player.sendMessage(" §eRegistrar - registre sua conta");
					player.sendMessage("");
					player.sendMessage("§7Seja bem vindo ao servidor!");
					player.sendMessage("");

					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
					player.sendMessage("§c/registrar <senha> <confirma-senha>.");
				}
			}

		} else {
			if (!Manager.getGoogle().contains("authcodes." + player.getUniqueId())) {
				GoogleAuthenticator gAuth = new GoogleAuthenticator();
				GoogleAuthenticatorKey key = gAuth.createCredentials();
				UserLogin user = Manager.UpdateUserLogin(player.getName());
				user.setSenha("");
				user.setState(true);
				user.setAccount(Manager.isOriginal(player.getName()));
				user.save();
				player.sendMessage("§7Seu Código de autenticação do Google §7 é §a" + key.getKey());
				player.sendMessage(
						"§7Você deve inserir este código no aplicativo Google Authenticator antes de sair do servidor.");

				Manager.getGoogle().set("authcodes." + player.getUniqueId(), key.getKey());
				Manager.getGoogle().saveConfig();
			} else {
				authlocked.add(player.getUniqueId());
				player.sendMessage("§cAbra o aplicativo Google Authenticator e forneça o código de seis dígitos.");
			}
		}
	}

	private boolean playerInputCode(Player player, int code) {
		String secretkey = Manager.getGoogle().getString("authcodes." + player.getUniqueId());

		GoogleAuthenticator gAuth = new GoogleAuthenticator();
		boolean codeisvalid = gAuth.authorize(secretkey, code);

		if (codeisvalid) {
			authlocked.remove(player.getUniqueId());
			return codeisvalid;
		}

		return codeisvalid;
	}

	@EventHandler
	void Event(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		String name = p.getName();
		if (Manager.getUser(name) != null) {
			UserLogin user = Manager.getUser(name);
			user.setState(false);
			user.save();
		}
	}

	@EventHandler
	public void chat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		if (Manager.getLoginConfig().IsSistema(Sistema.Login_Padrao)) {
			UserLogin user = Manager.UpdateUserLogin(player.getName());

			if (user != null) {
				if (!(user.IsLoged())) {
					event.setCancelled(true);
					player.sendMessage("§cVocê não está logado no servidor.");
				}
			}
		} else {
			if (authlocked.contains(player.getUniqueId())) {
				try {
					Integer code = Integer.parseInt(message);
					if (playerInputCode(player, code)) {
						authlocked.remove(player.getUniqueId());
						player.sendMessage("§f *§a Acesso concedido * §eBem-vindo ao servidor!");
					} else {
						player.sendMessage("§cCódigo incorreto ou expirado (Um código conterá apenas números).");

					}
				} catch (Exception e) {
					player.sendMessage("§cCódigo incorreto ou expirado (Um código conterá apenas números)");
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void move(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (Manager.getLoginConfig().IsSistema(Sistema.Login_Padrao)) {
			UserLogin user = Manager.UpdateUserLogin(player.getName());

			if (user != null) {
				if (!(user.IsLoged())) {
					Location location = Bukkit.getWorld("world").getSpawnLocation();
					if (player.getLocation().distance(location) > 1) {
						player.teleport(location);
					}
					player.sendMessage("§cVocê não está logado no servidor.");
				}
			}
		} else {
			if (authlocked.contains(player.getUniqueId())) {
				Location location = player.getLocation();
				if (player.getLocation().distance(location) > 1) {
					player.teleport(location);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockbreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (Manager.getLoginConfig().IsSistema(Sistema.Login_Padrao)) {
			UserLogin user = Manager.UpdateUserLogin(player.getName());

			if (user != null) {
				if (!(user.IsLoged())) {
					event.setCancelled(true);
					player.sendMessage("§cVocê não está logado no servidor.");
				}
			}
		} else {
			if (authlocked.contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockplace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (Manager.getLoginConfig().IsSistema(Sistema.Login_Padrao)) {
			UserLogin user = Manager.UpdateUserLogin(player.getName());

			if (user != null) {
				if (!(user.IsLoged())) {
					event.setCancelled(true);
					player.sendMessage("§cVocê não está logado no servidor.");
				}
			}
		} else {
			if (authlocked.contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

}
