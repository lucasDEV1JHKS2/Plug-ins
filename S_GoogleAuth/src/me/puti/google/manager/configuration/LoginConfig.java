package me.puti.google.manager.configuration;

public class LoginConfig {

	public enum Sistema {
		GoogleAuth, Login_Padrao;
	}

	Sistema sistema;
	String original = "";

	public LoginConfig(Sistema sistema, String original) {
		this.sistema = sistema;
		this.original = original;
	}

	public Sistema getSistema() {
		return sistema;
	}
	
	public String getOriginal() {
		return original;
	}
	
	public boolean IsOriginal() {
		if (getOriginal().equalsIgnoreCase("Sim")) {
			return true;
		}
		return false;
	}

	public boolean IsSistema(Sistema sistema) {
		if (getSistema() == sistema) {
			return true;
		}
		return false;
	}

}
