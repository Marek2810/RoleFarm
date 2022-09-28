package me.Marek2810.RoleFarm.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.Marek2810.RoleFarm.Main;

public class Harvest implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = (Player) event.getPlayer();
		if (Main.inst.getConfig().getList("crops").toString()
				.contains(event.getBlock().getType().toString())) {
			player.sendMessage("yep");
			player.sendMessage( Main.inst.getConfig().getList("crops").toString() );
			player.sendMessage( event.getBlock().getType().toString() );
			player.sendMessage( event.getBlock().getDrops().toString() );
			event.setCancelled(true);
		}
	}
}
