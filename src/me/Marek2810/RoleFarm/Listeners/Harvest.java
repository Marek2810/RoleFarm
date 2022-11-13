package me.Marek2810.RoleFarm.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Strings;

import me.Marek2810.RoleFarm.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Harvest implements Listener {
	
	public static List<Player> frozenPlayer = new ArrayList<Player>();
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		String blockType = event.getBlock().getType().toString();		
		Player player = (Player) event.getPlayer();		
		if (frozenPlayer.contains(player)) {
			event.setCancelled(true);
			return;
		}		
		if (Main.yamlCrops == null) return;
		if (Main.yamlCrops.contains(blockType) ) {				
			if ( isCorrectTool(player, blockType ) ) {
				double harvestTime = Main.yamlCropsSection.getDouble(blockType + ".harvest-time")*1000;
				long doneAt = (long)(System.currentTimeMillis()+harvestTime);				
				frozenPlayer.add(player);
				new BukkitRunnable() {		
					boolean done = false;
					double percentage;
					int left;
					public void run() {
						if (done) return;						
						left = (int) (doneAt-System.currentTimeMillis());	
						if (left < 0 ) left = 0;
						percentage = (harvestTime-left)/harvestTime*100;
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
								new TextComponent("§e§lZBIERANIE: §8[§r" + getProgressBar((int)percentage, 100, 20, "|", ChatColor.GREEN, ChatColor.RED) + "§8]"));
						//player.sendMessage("§e§lZBIERANIE: §8[§r" + getProgressBar((int)percentage, 100, 20, "|", ChatColor.GREEN, ChatColor.RED) + "§8]");
						if ( System.currentTimeMillis() >= doneAt ) {
							done = true;
							frozenPlayer.remove(player);	
							cancel();
						}
					}
				}.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
				
//				//good tool
//				//freez player
//				
//				frozenPlayer.add(player);
//				player.sendMessage( ChatColor.translateAlternateColorCodes('&',
//						Main.inst.getConfig().getString("messages.harvest") ));
//				new BukkitRunnable() {
//					public void run() {
//						frozenPlayer.remove(player);	
//						player.sendMessage("§e§lYOUR PROGRESS: §8[§r" + getProgressBar(20, 100, 40, "|", ChatColor.GREEN, ChatColor.RED) + "§8]");
//						cancel();
//					}
//				}.runTaskLater(Main.getPlugin(Main.class), (int)harvestTime);
			}
			else {
				//bad tool
				player.sendMessage( ChatColor.translateAlternateColorCodes('&',
						Main.inst.getConfig().getString("messages.harvest-bad-tool")) );
				event.setCancelled(true);
                

				return;
			}
		}
		return;
	}
	
	public String getProgressBar(int current, int max, int totalBars, String symbol, ChatColor completedColor,
            ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (frozenPlayer.contains(event.getPlayer())) event.setCancelled(true);
		return;
	}
	
	public void onBlockPlace(BlockPlaceEvent event) {
		if (frozenPlayer.contains(event.getPlayer())) event.setCancelled(true);
		return;
	}
	
	public boolean isCorrectTool(Player player, String blockType) {
		ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		for (String yamlTool : Main.yamlCropsSection
				.getConfigurationSection(blockType + ".harvest-tools").getKeys(false) ) {
			//Is tool good?
			if (Main.yamlCropsSection == null) return true;
			if ( Main.yamlCropsSection.getString(blockType 
					+ ".harvest-tools." + yamlTool + ".type").equalsIgnoreCase(itemInMainHand.getType().toString()) ) {
				//Good tool
				//Need custom model data?				
				if (Main.yamlCropsSection.get(blockType + ".harvest-tools." + yamlTool + ".customModelData") != null ) {
					//Need custom model data
					ItemMeta meta = itemInMainHand.getItemMeta();
					//Has custom model data?
					if (meta.hasCustomModelData()) {
						//Custom model data correct?
						if (meta.getCustomModelData() == Main.yamlCropsSection
								.getInt( blockType + ".harvest-tools" + yamlTool + ".customModelData" )) {
							//good tool
							return true;
						}
					}
				}
				//No needed custom model data
				else {
					//good tool, not custom model data needed
					return true;
				}
			}
		}
		return false;
	}
	
	
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
			Main.console.sendMessage(ChatColor.translateAlternateColorCodes('&', 
					Main.logPrefix + "&aHráč&6 " + player.getName() + " &apozbieral políčko na súradniciach &6" + check + "&a."));
			dropItem.setAmount((int)newDropAmount);
			world.dropItemNaturally(loc, dropItem);
			secondItem.setAmount(secondMatAmount);
			world.dropItem(loc, secondItem);
		}					
	}
}
