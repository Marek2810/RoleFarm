package me.Marek2810.RoleFarm.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.RoleFarm.Main;
import net.md_5.bungee.api.ChatColor;

public class Harvest implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = (Player) event.getPlayer();
		//player.sendMessage( event.getBlock().getType().toString() );
		if (Main.inst.getConfig().getList("crops").toString()
				.contains(event.getBlock().getType().toString())) {
			player.sendMessage(ChatColor.GREEN + "yep");
			player.sendMessage(event.getBlock().getDrops().toString() );
			for (ItemStack drop : event.getBlock().getDrops()) {
				player.sendMessage(drop.toString());	
				player.sendMessage(String.valueOf(drop.getAmount()));
			}			
			//player.sendMessage( Main.inst.getConfig().getList("crops").toString() );			
			//player.sendMessage( event.getBlock().getDrops().toString() );	
			event.setCancelled(true);
		}
	}
}
