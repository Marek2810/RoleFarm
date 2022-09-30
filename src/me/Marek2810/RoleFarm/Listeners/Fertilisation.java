package me.Marek2810.RoleFarm.Listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.RoleFarm.Main;
import net.md_5.bungee.api.ChatColor;

public class Fertilisation implements Listener {

	@EventHandler
	public void onClick (PlayerInteractEvent event) {
		if ( event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
			if ( event.getHand() == EquipmentSlot.OFF_HAND ) return;
			Player player = (Player) event.getPlayer();
			ItemStack itemInMainHand = new ItemStack(event.getPlayer().getInventory().getItemInMainHand());
			if (Main.yamlNeedFertilizerList.contains( event.getClickedBlock().getType().toString() )) {
				if ( itemInMainHand.getType().toString().equals(Main.inst.getConfig().getString("fertilizer.material")) ) {
					Location loc = event.getClickedBlock().getLocation();
					String check = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
					if (Main.yamlFertilizedList == null) {
						Main.yamlFertilizedList = new ArrayList<String>();
					}
					else if (Main.yamlFertilizedList.contains(check)) {
						String msg = Main.inst.getConfig().getString("messages.fertilized");
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						event.setCancelled(true);
						return;
					}
					else {
						Main.yamlFertilizedList.add(check);
						Main.cropsData.getConfig().set("crops.fertilized", Main.yamlFertilizedList);
						Main.cropsData.saveConfig();	
						itemInMainHand.setAmount(1);
						player.getInventory().removeItem(itemInMainHand);
						String msg = Main.inst.getConfig().getString("messages.on-fertilisation");
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						event.setCancelled(true);
						return;
					}					
				}
			}			
		}
	}
}
