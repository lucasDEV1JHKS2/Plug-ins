package me.puti.economia.database;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.puti.economia.Main;
import me.puti.economia.api.TitleAPI;
import me.puti.economia.publish.EconomiaAPI;
import me.puti.perms.S_Perms;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MagnataTask extends BukkitRunnable {

	@Override
	public void run() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			EconomiaAPI.Magnata();
			String p = EconomiaAPI.getmagnata();
			String grupo = (Main.getPlugin().getAPI("S_Perms") == false? PermissionsEx.getUser(p).getPrefix().replace("&", "§"): S_Perms.getUser(all).getGrupo().getPrefix().replace("&", "§"));
			TitleAPI.sendTitle(all, 1, 1, 1, "§e[Magnata]", " §f" + grupo + " " + p + " é o magnata do servidor.");
			all.sendMessage("");
			all.sendMessage(
					" §a[$] §fParabéns " + grupo + " " + p + "§f você é o magnata do servidor!");
			all.sendMessage("");
			all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 0.5F, 0.5F);
		}
	}

}
