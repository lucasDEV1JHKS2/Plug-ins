package me.puti.caixas.manager.caixa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.puti.caixas.Main;
import me.puti.caixas.api.S_Config;
import me.puti.caixas.manager.Manager;
import me.puti.caixas.manager.task.CaixaMenuTask;

public class Caixa {

	public enum CaixaStatus {
		Aberta, Fechada;
	}

	String name, specifcName;
	List<CaixaItem> itens = new ArrayList<CaixaItem>();

	// Caixa Config
	String id;
	HashMap<String, CaixaStatus> ChestOpens = new HashMap<String, Caixa.CaixaStatus>();
	CaixaType caixatype = null;
	ItemStack item = null;
	Double valor = 0D;
	List<String> lore = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	public Caixa(String name) {
		S_Config config = Manager.getConfig();
		if (config != null) {
			this.specifcName = name;
			this.id = config.getString("caixas." + name + ".id");
			this.name = config.getString("caixas." + name + ".nome");
			Double valor = config.getDouble("caixas." + name + ".valor");
			this.lore = (List<String>) config.getList("caixas." + name + ".lore");
			caixatype = CaixaType.valueOf(config.getString("caixas." + name + ".tipo"));

			try {
				config.getConfig().getConfigurationSection("caixas." + name + ".itens").getKeys(false)
						.forEach(itemName -> {
							String id = config.getString("caixas." + name + ".itens." + itemName + ".id");
							String nome = config.getString("caixas." + name + ".itens." + itemName + ".name");
							String porcentagem = config
									.getString("caixas." + name + ".itens." + itemName + ".porcentagem");
							List<String> lore = (List<String>) config
									.getList("caixas." + name + ".itens." + itemName + ".lore");

							CaixaItem item = new CaixaItem(itemName);
							item.setId(id).setCustomName(nome).setLore(lore).setPorcentagem(porcentagem);
							itens.add(item);
						});
			} catch (Exception e) {
				Main.Log("&c[S_Caixas] Ocorreu um erro ao tentar carrega os itens da  caixa " + name.toUpperCase()
						+ ", de um olhada na config.");
			}

			try {
				Material material = getMaterial();
				if (getData() != 0) {
					item = new ItemStack(material, 1, (short) getData());
				} else {
					item = new ItemStack(material);
				}
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(getName());
				meta.setLore(getLore());

				meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
				meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

				item.setAmount(1);
				item.setItemMeta(meta);
			} catch (Exception e) {
				Main.Log("&c[S_Caixas] Ocorreu um erro ao tentar construir o item stack da caixa " + name.toUpperCase()
						+ ".");
			}

			Manager.getCaixas().put(name, this);
		}
	}

	public String getId() {
		return id;
	}

	public CaixaType getType() {
		return caixatype;
	}

	public CaixaStatus IsStatus(String name) {
		if (ChestOpens.get(name) != null) {
			return ChestOpens.get(name);
		} else {
			return CaixaStatus.Fechada;
		}
	}

	public void setStatus(String name, CaixaStatus status) {
		ChestOpens.put(name, status);
	}

	public void Ver(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9 * 5, SpecifcName() + " - Itens");

		int slot = 0;
		for (CaixaItem item : getItens()) {
			inv.setItem(slot, item.getItemInfo());
			slot++;
		}

		p.openInventory(inv);
	}

	public List<String> getLore() {
		List<String> nlore = new ArrayList<String>();
		for (String line : lore) {
			nlore.add(line.replace("&", "§").replace("<caixa>", specifcName).replace("<valor>", getValor() + " coins"));
		}

		return nlore;
	}

	public Double getValor() {
		return valor;
	}

	public void Dar(Player p) {
		p.getInventory().addItem(getItem());
	}

	public void Remove(Player p) {
		if (IsStatus(p.getName()) == CaixaStatus.Aberta) {
			return;
		}
		for (int i = 0; i < p.getInventory().getContents().length; i++) {
			if (p.getInventory().getItem(i) != null) {
				if (p.getInventory().getItem(i).getItemMeta().getDisplayName()
						.equals(getItem().getItemMeta().getDisplayName())) {
					if (p.getInventory().getItem(i).getAmount() > 1) {
						p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - 1);
					} else {
						p.getInventory().remove(getItem());
					}
				}
			}
		}
	}

	public void openMenu(Player p) {
		if (IsStatus(p.getName()) == CaixaStatus.Aberta) {
			p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);
			p.sendMessage("§cAguarde a até que a caixa termine de abrir.");
			return;
		}
		Inventory inv = Bukkit.createInventory(null, 9 * 3, SpecifcName() + " - Abrindo");

		ChestAnimation(inv);

		p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		p.openInventory(inv);
		ChestOpens.put(p.getName(), CaixaStatus.Aberta);
	}

	public void CloseMenu(Player p) {
		p.closeInventory();
		ChestOpens.put(p.getName(), CaixaStatus.Fechada);
	}

	public void ChestAnimation(Inventory inv) {
		(new CaixaMenuTask(inv, this)).runTaskTimer(Main.getInstance(), 2L, 3L);
	}

	public int RandomNex() {
		Random random = new Random();

		return random.nextInt(100) - 1;
	}

	public Short getData() {
		String[] nId = id.split(":");
		return Short.valueOf(nId[1]);
	}

	public Material getMaterial() {
		String[] nId = id.split(":");
		return Material.getMaterial(Integer.valueOf(nId[0]));
	}

	public List<CaixaItem> getItens() {
		return itens;
	}

	public String SpecifcName() {
		return specifcName;
	}

	public String getName() {
		return name.replace("&", "§");
	}

	public ItemStack getItem() {
		return item;
	}

}
