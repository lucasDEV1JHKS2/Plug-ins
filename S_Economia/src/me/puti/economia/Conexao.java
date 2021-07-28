package me.puti.economia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import me.puti.economia.database.MetodosEconomy;


public class Conexao {
  public static Connection con = null;
  
  public static ConsoleCommandSender sc = Bukkit.getConsoleSender();
  public static boolean IsConnected = false;
  
  public static void open() {
	  String url = "jdbc:mysql://"+Main.Andress+":3306/"+Main.Database;
	    String user = Main.user;
	    String password = Main.password;
    try {
      con = DriverManager.getConnection(url, user, password);
      sc.sendMessage(String.valueOf(MetodosEconomy.prefix) + "§aConfiguração com banco de dados iniciada.");
      IsConnected = true;
      createTable();
    } catch (SQLException e) {
    	IsConnected = false;
      sc.sendMessage(String.valueOf(MetodosEconomy.prefix) + "§cConfiguração do banco de dados não indentificada.");
      Main.getPlugin().getPluginLoader().disablePlugin((Plugin)Main.getPlugin());
    } 
  }
  
  public static void close() {
    if (con != null)
      try {
        con.close();
      } catch (SQLException e) {
      }  
  }
  
  public static void createTable() {
    if (con != null) {
      PreparedStatement stm = null;
      try {
        stm = con.prepareStatement(
            "CREATE TABLE IF NOT EXISTS `money` (`id` INT NOT NULL AUTO_INCREMENT,`player` VARCHAR(24) NULL,`quantia` DOUBLE NULL,PRIMARY KEY (`id`));");
        stm.executeUpdate();
      } catch (SQLException e) {
        sc.sendMessage(String.valueOf(MetodosEconomy.prefix) + "§cNão foi possivel carregar a tabela");
      } 
    } 
  }
}
