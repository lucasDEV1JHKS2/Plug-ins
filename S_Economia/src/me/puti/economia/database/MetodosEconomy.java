package me.puti.economia.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import me.puti.economia.Conexao;
import me.puti.economia.manager.menu.PlayerIcon;
import me.puti.economia.publish.EconomiaAPI;

public class MetodosEconomy extends Conexao {
	public static String prefix = "§6(Sistema) §7> ";

	public static ConsoleCommandSender sc = Bukkit.getConsoleSender();

	public static final NavigableMap<Long, String> suffixes = new TreeMap<>();

	public static String format(Double value) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String formatted = formatter.format(value);
		return formatted;
	}

	public static boolean contains(String player) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("SELECT * FROM `money` WHERE `player` = ?");
			stm.setString(1, player);
			ResultSet rs = stm.executeQuery();
			if (rs.next())
				return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}

	public static void setPlayer(String player) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("INSERT INTO `money`(`player`, `quantia`) VALUES (?,?)");
			stm.setString(1, player);
			stm.setDouble(2, 0.0D);
			stm.executeUpdate();
		} catch (SQLException e) {
			sc.sendMessage(String.valueOf(prefix) + "§cNão foi possivel inserir o player: §f" + player
					+ "§a no banco de dados!");
		}
	}

	public static void setar(String player, Double quantia) {
		if (contains(player)) {
			PreparedStatement stm = null;
			try {
				stm = con.prepareStatement("UPDATE `money` SET `quantia` = ? WHERE `player` = ?");
				stm.setDouble(1, quantia.doubleValue());
				stm.setString(2, player);
				stm.executeUpdate();
			} catch (SQLException e) {
				sc.sendMessage(String.valueOf(prefix) + "§cNão foi possivel setar o cash do jogador");
			}
		} else {
			setPlayer(player);
		}
	}

	public static Double PegarMoney(String player) {
		if (contains(player)) {
			PreparedStatement stm = null;
			try {
				stm = con.prepareStatement("SELECT * FROM `money` WHERE `player` = ?");
				stm.setString(1, player);
				ResultSet rs = stm.executeQuery();
				if (rs.next())
					return Double.valueOf(rs.getDouble("quantia"));
				return Double.valueOf(0.0D);
			} catch (SQLException e) {
				return Double.valueOf(0.0D);
			}
		}
		setPlayer(player);
		return Double.valueOf(0.0D);
	}

	public static void adicionar(String player, Double quantia) {
		if (contains(player)) {
			setar(player, Double.valueOf(PegarMoney(player).doubleValue() + quantia.doubleValue()));
		} else {
			setPlayer(player);
		}
	}

	public static void remove(String player, Double quantia) {
		if (contains(player)) {
			setar(player, Double.valueOf(PegarMoney(player).doubleValue() - quantia.doubleValue()));
		} else {
			setPlayer(player);
		}
	}

	public static void delete(String player) {
		if (contains(player)) {
			PreparedStatement stm = null;
			try {
				stm = con.prepareStatement("DELETE FROM `money` WHERE `player` = ?");
				stm.setString(1, player);
				stm.executeUpdate();
			} catch (SQLException e) {
				sc.sendMessage(String.valueOf(prefix) + "§cNão foi possivel remover o jogador §f" + player
						+ "§c do banco de dados!");
			}
		}
	}

	public static HashMap<Integer, PlayerIcon> GetMoneyTop() {
		PreparedStatement stm = null;
		HashMap<Integer, PlayerIcon> tops = new HashMap<Integer, PlayerIcon>();

		try {
			stm = con.prepareStatement("SELECT * FROM `money` ORDER BY `quantia` DESC");
			ResultSet rs = stm.executeQuery();
			int i = 0;
			while (rs.next()) {
				if (i <= 10) {
					i++;
					String player = rs.getString("player");
					boolean Magnata = (EconomiaAPI.getmagnata().equals(player) ? true : false);
					PlayerIcon icon = new PlayerIcon(player, Magnata, i);
					tops.put(i, icon);
				}
			}
		} catch (SQLException e) {
			sc.sendMessage(String.valueOf(prefix) + "§cNão foi possivel carregar o top money");
		}
		return tops;
	}

	public static String Maganta() {
		PreparedStatement stm = null;
		String magnata = "Nenhum";
		try {
			stm = con.prepareStatement("SELECT * FROM `money` ORDER BY `quantia` DESC");
			ResultSet rs = stm.executeQuery();
			int i = 0;
			while (rs.next()) {
				if (i < 1) {
					i++;
					magnata = rs.getString("player");
				}
			}
		} catch (SQLException e) {
			sc.sendMessage(String.valueOf(prefix) + "§cNão foi possivel pegar o magnata!");
		}
		return magnata;
	}
}
