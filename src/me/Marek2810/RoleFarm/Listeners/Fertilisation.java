package me.Marek2810.RoleFarm.Listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Marek2810.RoleFarm.Main;
import net.md_5.bungee.api.ChatColor;

public class Fertilisation implements Listener {

	
	@EventHandler
	public void onClick (PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getPlayer().getInventory().getItemInMainHand() == null) return;
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {				
				//je hnojivo
				Player player = (Player) event.getPlayer();
				if ( isFertilizer(player, event.getClickedBlock().getLocation() )) {
					//pohnojiteľný blok
					if (Main.yamlNeedFertilizerList.contains( event.getClickedBlock().getType().toString() )) {		
						Location loc = event.getClickedBlock().getLocation();
						String check = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
						ItemStack itemInMainHand = new ItemStack(event.getPlayer().getInventory().getItemInMainHand());
						if (Main.yamlFertilizedList == null) {
							Main.yamlFertilizedList = new ArrayList<String>();
						}
						else if (Main.yamlFertilizedList.contains(check)) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.yamlMessages.getString("fertilized")));
							event.setCancelled(true);
							return;
						}
						else {
							Main.yamlFertilizedList.add(check);
							Main.cropsData.getConfig().set("crops.fertilized", Main.yamlFertilizedList);
							Main.cropsData.saveConfig();	
							itemInMainHand.setAmount(1);
							player.getInventory().removeItem(itemInMainHand);
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
									Main.yamlMessages.getString("on-fertilisation")));
							event.setCancelled(true);	
							return;
						}
					}
				}
			}
			return;
	}
	
	public boolean isFertilizer(Player player, Location loc) {
		ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		if (Main.yamlFertilizersSection == null) return false;
		if (Main.yamlFertilizers == null) return false;
		//for (String yamlFertilizer : Main.yamlFertilizers) {
		for (String fertilizers : Main.yamlFertilizers ) {
			if  (Main.yamlFertilizersSection.getString(fertilizers + ".material").equals(itemInMainHand.getType().toString()) ) {
				if (Main.yamlFertilizersSection.get(fertilizers + ".customModelData") != null ) {
					ItemMeta meta = itemInMainHand.getItemMeta();
					if (meta.hasCustomModelData()) {
						if (meta.getCustomModelData() == Main.yamlFertilizersSection.getInt(fertilizers + ".customModelData"))
							return true;
					}
				}
				else {
					return true;
				}
			}
		}
		return false;
	}
}
