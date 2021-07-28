package me.puti.chat.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class S_Config {
  private JavaPlugin plugin;
  
  private String name;
  
  private File file;
  
  private YamlConfiguration config;
  
  public S_Config(JavaPlugin plugin, String nome) {
    this.plugin = plugin;
    setName(nome);
    reloadConfig();
  }
  
  public JavaPlugin getPlugin() {
    return this.plugin;
  }
  
  public void setPlugin(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public File getFile() {
    return this.file;
  }
  
  public YamlConfiguration getConfig() {
    return this.config;
  }
  
  public void saveConfig() {
    try {
      getConfig().save(getFile());
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public void saveDefault() {
    getConfig().options().copyDefaults(true);
  }
  
  public void saveDefaultConfig() {
    getPlugin().saveResource(getName(), false);
  }
  
  public void reloadConfig() {
    this.file = new File(getPlugin().getDataFolder(), getName());
    this.config = YamlConfiguration.loadConfiguration(getFile());
  }
  
  public void deleteConfig() {
    getFile().delete();
  }
  
  public boolean existeConfig() {
    return getFile().exists();
  }
  
  public String getString(String path) {
    return getConfig().getString(path);
  }
  
  public int getInt(String path) {
    return getConfig().getInt(path);
  }
  
  public boolean getBoolean(String path) {
    return getConfig().getBoolean(path);
  }
  
  public double getDouble(String path) {
    return getConfig().getDouble(path);
  }
  
  public List<?> getList(String path) {
    return getConfig().getList(path);
  }
  
  public boolean contains(String path) {
    return getConfig().contains(path);
  }
  
  public void set(String path, Object value) {
    getConfig().set(path, value);
  }
}
