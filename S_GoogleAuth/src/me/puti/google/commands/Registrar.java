package me.puti.google.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.puti.google.manager.Manager;
import me.puti.google.manager.player.UserLogin;

public class Registrar implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSomente jogadores podem utilizar este comando.");
			return true;
		}

		Player p = (Player) sender;
		if (args.length == 0) {
			p.sendMessage("§cUse: /registrar <senha> <confirma-senha>.");
			return true;
		}

		String senha = "";
		String confirma_senha = "";
try {
	senha = args[0];
	confirma_senha = args[1];
} catch (Exception e) {
	p.sendMessage("§cInsira a senha.");
}
		if (senha != null) {
			
			if (Manager.getUser(p.getName()).isOriginal() && Manager.getLoginConfig().IsOriginal()) {
				p.sendMessage("§cVocê possui uma conta original não precisa logar-se.");
				return true;
			}
			
			if (Manager.getUser(p.getName()).IsLoged() || Manager.getUser(p.getName()) != null) {
				p.sendMessage("§cVocê já está logado no servidor.");
				return true;
			}

			if (!(senha.equals(confirma_senha))) {
				p.sendMessage("§cAs senhas devem ser iguais.");
				return true;
			}
			
			if (senha.length() < 5) {
				p.sendMessage("§cA senha deve conter no mínimo 5 características.");
				return true;
			}

			try {
				UserLogin user = new UserLogin(p.getName());
				user.setAccount(Manager.isOriginal(p.getName()));
				user.setSenha(senha);
				user.setState(true);
				user.save();
				p.sendMessage("§eRegistrado com sucesso.");
			} catch (Exception e) {
				p.sendMessage("§cNão foi possível registra está conta.");
			}
		} else {
			p.sendMessage("§cInsira a senha.");
		}

		return true;
	}
}
