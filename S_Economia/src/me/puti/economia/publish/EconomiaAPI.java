package me.puti.economia.publish;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import me.puti.economia.database.MetodosEconomy;
import me.puti.economia.manager.User;
import me.puti.economia.manager.Users;
import me.puti.economia.manager.menu.PlayerIcon;

public class EconomiaAPI {

	public static final NavigableMap<Long, String> suffixes = new TreeMap<>();

	public static String format(Long valor) {
		suffixes.put(Long.valueOf(1000L), " K");
		suffixes.put(Long.valueOf(1000000L), " KK");
		suffixes.put(Long.valueOf(1000000000L), " T");
		suffixes.put(Long.valueOf(1000000000000L), " Q");
		suffixes.put(Long.valueOf(1000000000000000L), " QQ");
		suffixes.put(Long.valueOf(1000000000000000000L), " S");
		suffixes.put(Long.valueOf(Long.MAX_VALUE), " SS");
		if (valor == Long.MIN_VALUE)
			return format(-9999999999999L);
		if (valor < 0D)
			return format(0L);// Valor Negativo -0
		if (valor < 1000)
			return valor.toString();
		Map.Entry<Long, String> e = suffixes.floorEntry(Long.valueOf(valor));
		Long divideBy = e.getKey();
		String suffix = e.getValue();
		Long truncated = valor / divideBy.longValue();
		boolean hasDecimal = (truncated < 100 && truncated / 1L != (truncated));
		return hasDecimal ? (String.valueOf(truncated / 1L) + suffix) : (String.valueOf(truncated / 1L) + suffix);
	}

	public static boolean compare(String player, Double valor) {
		if (valor == 0.0) {
			return false;
		} else {
			if (getMoney(player) >= valor) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static void SetarMoney(String jogador, Double money) {
		Users.getUser(jogador).setSaldo(money);
	}

	public static void RemoverMoney(String jogador, Double money) {
		Users.getUser(jogador).removeSaldo(money);
	}

	public static void adicionarMoney(String jogador, Double money) {
		Users.getUser(jogador).adicionarSaldo(money);
	}

	public static HashMap<Integer, PlayerIcon> TopList() {
		return MetodosEconomy.GetMoneyTop();
	}

	public static void Enviar(String jogador, String JogadorReceber, Double money) {
		if (!(Users.Contains(jogador))) {
			setarPlayer(jogador);
		} else {
			Users.getUser(JogadorReceber).adicionarSaldo(money);
			Users.getUser(jogador).removeSaldo(money);
		}
	}

	public static void setMagnata(String userName, boolean value) {
		Users.getUser(userName).setMaganta(value);
	}

	public static void Magnata() {
		String rec = MetodosEconomy.Maganta();
		for (User user : Users.getUsers().values()) {
			if (rec.equalsIgnoreCase(user.getName())) {
				user.setMaganta(true);
			} else {
				user.setMaganta(false);
			}
		}
	}

	public static String IsMagnata(String name) {
		if (getmagnata().equalsIgnoreCase(name)) {
			return "§a[$]";
		} else {
			return "";
		}
	}

	public static Double getMoney(String jogador) {
		if (!(Users.Contains(jogador))) {
			setarPlayer(jogador);
			return Users.getUser(jogador).getSaldo();
		}
		return Users.getUser(jogador).getSaldo();
	}

	public static void setarPlayer(String jogador) {
		User user = new User(jogador, MetodosEconomy.PegarMoney(jogador));
		Users.addUser(user);
	}

	public static String getmagnata() {
		Magnata();
		for (User user : Users.getUsers().values()) {
			if (user.IsMagnata()) {
				return user.getName();
			}
		}
		return "";
	}

	public static String formatDefault(Double valor) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String _valor = formatter.format(valor.longValue());

		return _valor.replace(".", ",");
	}
}
