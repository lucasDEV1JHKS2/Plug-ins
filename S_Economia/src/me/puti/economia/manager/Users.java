package me.puti.economia.manager;

import java.util.HashMap;

public class Users {

	private static HashMap<String, User> users = new HashMap<String, User>();
	
	
	public static void addUser(User user) {
		if (users.get(user.getName()) == null) {
			users.put(user.getName(), user);
		}
	}
	
	public static User getUser(String userName) {
		return users.get(userName);
	}
	
	public static HashMap<String, User> getUsers(){
		return users;
	}
	
	public static  boolean Contains(String userName) {
		return users.containsKey(userName);
	}
	
	public static void saveAll() {
		for (User user : users.values()) {
			user.save();
		}
	}
	
}
