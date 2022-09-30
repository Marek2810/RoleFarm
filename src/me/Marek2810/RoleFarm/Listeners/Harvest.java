package me.Marek2810.RoleFarm.Listeners;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.RoleFarm.Main;
import net.md_5.bungee.api.ChatColor;

public class Harvest implements Listener {
	
	@EventHandler
	public void onBlockDrop(BlockDropItemEvent event) {
		Player player = (Player) event.getPlayer();		
		if (player.getGameMode().equals(GameMode.CREATIVE)) return;
		if (event.getItems().isEmpty() ) return;
		if (Main.yamlDropsList.contains(event.getItems().get(0).getItemStack().getType().toString() )) {	
			World world = event.getPlayer().getWorld();
			double newDropAmount = 0;
			Material harvestItem = Material.AIR;
			int secondMatAmount = 0;
			Material secondMat = Material.AIR;
			Location loc = event.getBlock().getLocation();
			String check = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
			for (Item drop : event.getItems() ) {
				if ( Main.yamlDropsList.contains( drop.getItemStack().getType().toString() ) ) {					
					harvestItem = drop.getItemStack().getType();
					if ( harvestItem == Material.WHEAT || harvestItem == Material.BEETROOT ) {
						newDropAmount =  ThreadLocalRandom.current().nextInt(1, 4 + 1);
					}
					else {
						newDropAmount = newDropAmount + drop.getItemStack().getAmount();
					}
				}
				else {
					secondMat = drop.getItemStack().getType();
					secondMatAmount = drop.getItemStack().getAmount();
				}
			}	
			event.setCancelled(true);
			if (harvestItem == Material.AIR ) {
				player.sendMessage(ChatColor.RED + "Chyba...");
				return;
			}
			ItemStack dropItem = new ItemStack(harvestItem);	
			ItemStack secondItem = new ItemStack(secondMat);
			if (Main.yamlWateredList.contains(check)) {				
				newDropAmount = newDropAmount*Main.wateringMulti;
				Main.yamlWateredList.remove(check);
				Main.cropsData.getConfig().set("crops.watered", Main.yamlWateredList);			
				Main.cropsData.saveConfig();
			}
			if (Main.yamlFertilizedList.contains(check)) {
				newDropAmount = newDropAmount*Main.fertilisationMulti;
				Main.yamlFertilizedList.remove(check);
				Main.cropsData.getConfig().set("crops.fertilized", Main.yamlFertilizedList);
				Main.cropsData.saveConfig();
			}			
			dropItem.setAmount((int)newDropAmount);
			world.dropItemNaturally(loc, dropItem);
			secondItem.setAmount(secondMatAmount);
			world.dropItem(loc, secondItem);
		}					
	}
}
