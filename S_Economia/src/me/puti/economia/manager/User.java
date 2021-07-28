package me.puti.economia.manager;

import me.puti.economia.database.MetodosEconomy;

public class User {

	String name = "";
	Double saldo = 0D;
	boolean magnata = false;
	
	public User(String name, Double saldo) {
		this.name = name;
		this.saldo = saldo;
	}
	
	
	public User setMaganta(boolean magnata) {
		this.magnata = magnata;
		return this;
	}
	
	public User setSaldo(Double saldo) {
		this.saldo = saldo;
		return this;
	}
	
	public User removeSaldo(Double saldo) {
		this.saldo = getSaldo()-saldo;
		return this;
	}
	
	
	public User adicionarSaldo(Double saldo) {
		this.saldo = getSaldo()+saldo;
		return this;
	}
	
	
	public String getName() {
		return name;
	}
	
	public Double getSaldo() {
		return saldo;
	}
	
	public boolean IsMagnata() {
		return magnata;
	}
	
	public void save() {
		MetodosEconomy.setar(getName(), getSaldo());
	}
}
