package me.Marek2810.RoleFarm.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.RoleFarm.Main;
import net.md_5.bungee.api.ChatColor;

public class Watering implements Listener {
	
	@EventHandler
	public void onClick (PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = (Player) event.getPlayer();
			ItemStack itemInMainHand = new ItemStack(event.getPlayer().getInventory().getItemInMainHand());
			ItemStack watter_bucket = new ItemStack(Material.WATER_BUCKET);
			if ( itemInMainHand.equals(watter_bucket) ) {
				if ( Main.inst.getConfig().getList("need_wather").contains(event.getClickedBlock().getType().toString()) ) {
					event.setCancelled(true);
					Location loc = event.getClickedBlock().getLocation();
					String check = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
					List<String> list = Main.cropsData.getConfig().getStringList("crops.watered");
					if ( list == null ) {
						list = new ArrayList<String>();
					}
					if (list.contains(check)) {
						player.sendMessage(ChatColor.AQUA + "Políčko už je zaliate.");
						return;
					}
					list.add(check);
					Main.cropsData.getConfig().set("crops.watered", list);
					Main.cropsData.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Zalial si políčko.");
					return;	
				}
				else {
					player.sendMessage(ChatColor.RED + "Nemôžeš rozlievať vodu.");
					event.setCancelled(true);
				}											
			}
		}
	}
}
