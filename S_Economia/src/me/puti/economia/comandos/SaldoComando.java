package me.puti.economia.comandos;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.puti.economia.database.MetodosEconomy;
import me.puti.economia.manager.Users;
import me.puti.economia.manager.menu.Top;

public class SaldoComando implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
    if (!(sender instanceof Player)) {
      Bukkit.getConsoleSender().sendMessage("§c[!] Comando apenas para jogaodres da rede!");
      return false;
    } 
    if (cmd.getName().equalsIgnoreCase("saldo")) {
      Player p = (Player)sender;
      if (args.length == 0) {
        Double quantia = Users.getUser(p.getName()).getSaldo();
        p.sendMessage("§eCoins: §a" + MetodosEconomy.format(quantia));
        return false;
      } 
      if (args[0].equalsIgnoreCase("ver")) {
        if (args.length <= 1) {
          p.sendMessage("§c[!] Use: /saldo ver <jogador>");
          return false;
        } 
        Player jogador = Bukkit.getPlayerExact(args[1]);
        if (jogador == null) {
          p.sendMessage("§c[!] Jogador não encontrado!");
          return false;
        } 
        Double quantia = Users.getUser(p.getName()).getSaldo();
        p.sendMessage("§7" + jogador.getName() + " §epossue §a" + MetodosEconomy.format(quantia) + " coins");
        return false;
      } 
      if (args[0].equalsIgnoreCase("ajuda")) {
        p.sendMessage("§7Lista De Comandos:");
        p.sendMessage("§f");
        p.sendMessage("§e/saldo ajuda §7- Ver todos os comandos do plugin.");
        p.sendMessage("§e/saldo adicionar §7- Adicionar uma quantia de saldo.");
        p.sendMessage("§e/saldo enviar §7- Envia uma quantia para outro jogador.");
        p.sendMessage("§e/saldo top §7- Lista de Jogadores mais ricos.");
        p.sendMessage("§e/saldo ver §7- Visualizar saldo de outro jogador.");
        p.sendMessage("§f");
        return false;
      } 
      if (args[0].equalsIgnoreCase("top")) {
        Top.Open(p);
        return false;
      } 
      if (args[0].equalsIgnoreCase("enviar")) {
        Double quantia;
        if (args.length <= 2) {
          p.sendMessage("§c[!] Use: /saldo enviar <jogador> <quantia>.");
          return false;
        } 
        Player jogador = Bukkit.getPlayerExact(args[1]);
        try {
          quantia = Double.valueOf(args[2]);
        } catch (NumberFormatException e) {
          sender.sendMessage("§c[!] Digite um numero valido!");
          return true;
        } 
        if (jogador == null) {
          p.sendMessage("§c[!] Jogador não encontrado!");
          return false;
        } 
        if (jogador.getName() == p.getName()) {
          p.sendMessage("§c[!] Você não pode enviar saldo para Você mesmo!");
          return false;
        } 
        int nblock = 100000000;
        if (nblock >= quantia.doubleValue()) {
          p.sendMessage("§c[!] Você não pode setar essa quantia!");
          return false;
        } 
        if (quantia.doubleValue() < 0.0D) {
          p.sendMessage("§c[!] Você não pode enviar a quantia de 0 coins!");
          return false;
        } 
        if (Users.getUser(jogador.getName()) == null || Users.getUser(p.getName()) == null) {
        	p.sendMessage("§cNão foi possível competar a transferência para o jogador.");
        	return true;
        }
        Users.getUser(jogador.getName()).adicionarSaldo(quantia);
        Users.getUser(p.getName()).removeSaldo(quantia);
        p.sendMessage("§eVocê envio §a" + MetodosEconomy.format(quantia) + " coins §e para conta §f" + 
            jogador.getName() + "§e com sucesso!");
      } 
      if (!sender.hasPermission("economia.admin")) {
        p.sendMessage("§c[!] Você não tem permissÃ£o para executar esse comando!");
        return false;
      } 
      if (args[0].equalsIgnoreCase("setar")) {
        Double quantia;
        if (args.length <= 2) {
          p.sendMessage("§c[!] Use: /saldo setar <jogador> <quantia>.");
          return false;
        } 
        Player jogador = Bukkit.getPlayerExact(args[1]);
        try {
          quantia = Double.valueOf(args[2]);
        } catch (NumberFormatException e) {
          sender.sendMessage("§c[!] Digite um numero valido!");
          return true;
        } 
        if (jogador == null) {
          p.sendMessage("§c[!] Jogador não encontrado!");
          return false;
        } 
        if (quantia.doubleValue() < 0.0D) {
          p.sendMessage(
              "§c[!] O Jogador já está com a quantia de 0 coins não e possível setar um quantia negativa!");
          return false;
        } 
        Users.getUser(jogador.getName()).setSaldo(quantia);
        p.sendMessage("§aVocê setou §f" + MetodosEconomy.format(quantia) + "§a coins na conta §f" + 
            jogador.getName() + "§a com sucesso!");
        return false;
      } 
      if (args[0].equalsIgnoreCase("remover")) {
        Double quantia;
        if (args.length <= 2) {
          p.sendMessage("§c[!] Use: /saldo remover <jogador> <quantia>.");
          return false;
        } 
        try {
          quantia = Double.valueOf(args[2]);
        } catch (NumberFormatException e) {
          sender.sendMessage("§c[!] Digite um numero valido!");
          return true;
        } 
        Player jogador = Bukkit.getPlayerExact(args[1]);
        if (jogador == null) {
          p.sendMessage("§c[!] Jogador não encontrado!");
          return false;
        } 
        if (quantia.doubleValue() > Users.getUser(p.getName()).getSaldo()) {
          p.sendMessage(
              "§c[!] não e possivel remover essa quantia da conta do jogador.Pois e quantia e maior que a que o jogador possue!");
          return false;
        } 
        Users.getUser(p.getName()).removeSaldo(quantia);
        p.sendMessage("§aVocê remover §f" + MetodosEconomy.format(quantia) + "§a coins da conta §f" + 
            jogador.getName() + "§a com sucesso!");
        return false;
      } 
      if (args[0].equalsIgnoreCase("adicionar")) {
        Double quantia;
        if (args.length <= 2) {
          p.sendMessage("§c[!] Use: /saldo adicionar <jogador> <quantia>.");
          return false;
        } 
        Player jogador = Bukkit.getPlayerExact(args[1]);
        try {
          quantia = Double.valueOf(args[2]);
        } catch (NumberFormatException e) {
          sender.sendMessage("§c[!] Digite um numero valido!");
          return true;
        } 
        if (jogador == null) {
          p.sendMessage("§c[!] Jogador não encontrado!");
          return false;
        } 
        if (quantia.doubleValue() < 0.0D) {
          p.sendMessage("§c[!] O Jogador já está com a quantia de 0 coins não e possível setar um quantia negativa!");
          return false;
        } 
        Users.getUser(p.getName()).adicionarSaldo(quantia);
        p.sendMessage("§aVocê adicionou  §f" + MetodosEconomy.format(quantia) + "§a coins da conta §f" + 
            jogador.getName() + "§a com sucesso!");
        return false;
      } 
      return false;
    } 
    return false;
  }
}
