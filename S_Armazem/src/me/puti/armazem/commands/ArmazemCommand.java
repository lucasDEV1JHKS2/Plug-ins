package me.puti.armazem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.puti.armazem.manager.Manager;
import me.puti.armazem.manager.player.Armazem;

public class ArmazemCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSomente jogadores.");
			return true;
		}

		Player p = (Player) sender;
		if (args.length > 0) {
			if (args.length == 1) {
				p.sendMessage("§cUse: /armazem ver <jogador>.");
				return true;
			}

			String jogador = "";
			try {
				jogador = args[1];
			} catch (Exception e) {
				p.sendMessage("§cInsira um jogador válido.");
			}

			Armazem am = Manager.getArmazem(jogador);
			if (am == null) {
				p.sendMessage("§cEsté jogador não possuí um armazem ou ele está totalmente vázio.");
				return true;
			}

			try {
				am.Open();
			} catch (Exception e) {
				p.sendMessage("§cNão foi possível abrir seu armazem.");
			}
		} else {
			Armazem am = Manager.getArmazem(p.getName());
			if (am == null) {
				p.sendMessage("§cNão foi possível encontrar o seu armazem.");
				return true;
			}

			try {
				am.Open();
			} catch (Exception e) {
				p.sendMessage("§cNão foi possível abrir seu armazem.");
			}
		}
		return true;
	}
}
