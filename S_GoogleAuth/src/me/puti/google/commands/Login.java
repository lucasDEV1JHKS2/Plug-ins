package me.puti.google.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.puti.google.manager.Manager;
import me.puti.google.manager.player.UserLogin;

public class Login implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSomente jogadores podem utilizar este comando.");
			return true;
		}

		Player p = (Player) sender;
		if (args.length == 0) {
			p.sendMessage("§cUse: /login <senha>.");
			return true;
		}

		String senha = "";
		try {
			senha = args[0];
		} catch (Exception e) {
			p.sendMessage("§cInsira a senha.");
			return true;
		}
		if (senha != null) {
			if (Manager.getUser(p.getName()) != null) {
				if (Manager.getUser(p.getName()).isOriginal() && Manager.getLoginConfig().IsOriginal()) {
					p.sendMessage("§cVocê possui uma conta original não precisa logar-se.");
					return true;
				}

				if (Manager.getUser(p.getName()).IsLoged()) {
					p.sendMessage("§cVocê já está logado no servidor.");
					return true;
				}
				
				if (!(Manager.getUser(p.getName()).getSenha().equals(senha))) {
					p.sendMessage("§cInsira há senha correta.");
					return true;
				}

				try {
					UserLogin user = Manager.getUser(p.getName());
					user.setState(true);
					user.save();
					p.sendMessage("§eLogado com sucesso.");
				} catch (Exception e) {
					p.sendMessage("§cNão foi possível entrar não sua conta.");
				}

			} else {
				p.sendMessage("§cVocê nao possui uma conta registrada no servidor.");
			}

		} else {
			p.sendMessage("§cInsira a senha.");
		}

		return true;
	}
}
