package me.Marek2810.RoleFarm.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Marek2810.RoleFarm.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Planting implements Listener {
	
	@EventHandler
	public void onBlockPlaceEvent (BlockPlaceEvent event) {	
		String blockType = event.getBlock().getType().toString();		
		Player player = (Player) event.getPlayer();
		if (Harvest.frozenPlayer.contains(player)) {
			event.setCancelled(true);
			return;
		}			
		if (Main.yamlPlantsList == null) return;
		if (Main.yamlPlantsList.contains(blockType) ) {
			double plantingTime = Main.plantingtTime*1000;
			long doneAt = (long)(System.currentTimeMillis()+plantingTime);				
			Harvest.frozenPlayer.add(player);
			new BukkitRunnable() {		
				boolean done = false;
				double percentage;
				int left;
				public void run() {
					if (done) return;						
					left = (int) (doneAt-System.currentTimeMillis());	
					if (left < 0 ) left = 0;
					percentage = (plantingTime-left)/plantingTime*100;
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
							new TextComponent("§e§lSADENIE: §8[§r" + Harvest.getProgressBar((int)percentage, 100, 40, "|", ChatColor.GREEN, ChatColor.RED) + "§8]"));
					if ( System.currentTimeMillis() >= doneAt ) {
						done = true;
						Harvest.frozenPlayer.remove(player);	
						cancel();
					}
				}
			}.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
		}		
	}
}