package me.Marek2810.RoleFarm.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Marek2810.RoleFarm.Main;
import net.md_5.bungee.api.ChatColor;

public class Watering implements Listener {

	public static HashMap<Player, Long> wateringCD = new HashMap<Player, Long>();
	
	@EventHandler
	public void onClick (PlayerInteractEvent event) {
		if ( event.getPlayer().getInventory().getItemInMainHand() == null ) return;
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {			
			Player player = (Player) event.getPlayer();			
			ItemStack itemInMainHand = new ItemStack(player.getInventory().getItemInMainHand());
			if ( itemInMainHand.getType().equals(Material.WATER_BUCKET) ) {
				if (wateringCD.get(player) != null) {
					event.setCancelled(true);
					if (System.currentTimeMillis() <= wateringCD.get(player)) return;
					if (System.currentTimeMillis() > wateringCD.get(player)) wateringCD.remove(player);
				}	
				if ( Main.yamlNeedWaterList.contains(event.getClickedBlock().getType().toString()) ) {	
					Location loc = event.getClickedBlock().getLocation();
					String check = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
					if ( Main.yamlWateredList == null ) {
						Main.yamlWateredList = new ArrayList<String>();
					}					
					if (Main.yamlWateredList.contains(check)) {
						String msg = Main.inst.getConfig().getString("messages.watered");
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));	
						event.setCancelled(true);
						return;
					}
					else {
						ItemMeta meta = itemInMainHand.getItemMeta();
						if ( !meta.hasLore() ) {
							List<String> lore = new ArrayList<String>();						
							lore.add(" ");
							int newUsages = Main.waterBucketMaxUsages-1;
							lore.add(ChatColor.AQUA + "Použitia: " + newUsages + "/" + Main.waterBucketMaxUsages);
							player.getInventory().remove(itemInMainHand);
							meta.setLore(lore);
							itemInMainHand.setItemMeta(meta);						
							player.getInventory().addItem(itemInMainHand);
						}
						else {
							List<String> lore = meta.getLore();
							String[] lore1 = lore.get(1).split("Použitia: ");
							String[] loreSplit = lore1[1].split("/");
							int usages = Integer.valueOf(loreSplit[0]);
							if (usages > 1 ) {
								player.getInventory().removeItem(itemInMainHand);
								int newUsages = usages-1;
								lore.set(1, ChatColor.AQUA + "Použitia: " + String.valueOf(newUsages) + "/" + Main.waterBucketMaxUsages);
								meta.setLore(lore);
								itemInMainHand.setItemMeta(meta);
								player.getInventory().addItem(itemInMainHand);
							}
							else if (usages == 1) {
								player.getInventory().removeItem(itemInMainHand);
								ItemStack bucket = new ItemStack(Material.BUCKET);
								player.getInventory().addItem(bucket);
							}
							else {
								player.sendMessage(ChatColor.RED + "Error!");
								event.setCancelled(true);
								return;
							}
						}
						Main.yamlWateredList.add(check);
						Main.cropsData.getConfig().set("crops.watered", Main.yamlWateredList);
						Main.cropsData.saveConfig();
						String msg = Main.inst.getConfig().getString("messages.on-watering");
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));	
						event.setCancelled(true);
						wateringCD.put(player, System.currentTimeMillis()+30);
						return;
					}						
				}								
			}
		}
	}
}
