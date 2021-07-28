package me.puti.economia.api;

import java.lang.reflect.Constructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionBar {
  public static void sendAction(Player p, String message) {
    try {
      message = ChatColor.translateAlternateColorCodes('&', message);
      Class<?> playOutChat = getNMSClass("PacketPlayOutChat");
      Class<?> baseComponent = getNMSClass("IChatBaseComponent");
      Class<?> chatMsg = getNMSClass("ChatMessage");
      Constructor<?> chatConstructor = chatMsg.getDeclaredConstructor(new Class[] { String.class, Object[].class });
      Constructor<?> playOutConstructor = playOutChat.getConstructor(new Class[] { baseComponent, byte.class });
      Object ichatbc = chatConstructor.newInstance(new Object[] { message, new Object[0] });
      Object packet = playOutConstructor.newInstance(new Object[] { ichatbc, Byte.valueOf((byte)2) });
      sendPacket(p, packet);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private static void sendPacket(Player p, Object packet) {
    try {
      Object handle = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
      Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
      playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static Class<?> getNMSClass(String name) {
    String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    try {
      return Class.forName("net.minecraft.server." + version + "." + name);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    } 
  }
}
