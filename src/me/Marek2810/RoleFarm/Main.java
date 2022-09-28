package me.Marek2810.RoleFarm;

import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.RoleFarm.Files.DataManager;
import me.Marek2810.RoleFarm.Listeners.Harvest;
import me.Marek2810.RoleFarm.Listeners.Watering;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {

	public static Main inst;
	public static DataManager cropsData;
	public static Harvest harvest;
	public static Watering watering;	
	public static List<String> ymlCrops;
    
    @Override
    public void onEnable() {
    	inst = this;
    	cropsData = new DataManager(this, "crops.yml");
    	this.saveDefaultConfig();    	
    	harvest = new Harvest();    	
    	this.getServer().getPluginManager().registerEvents(harvest, this);  
    	watering = new Watering();
    	this.getServer().getPluginManager().registerEvents(watering, this);
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading crops...");
    	ymlCrops = this.getConfig().getStringList("crops");
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Crops loaded.");
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading farming...");
    	ymlCrops = this.getConfig().getStringList("farming");
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Farming loaded.");
    }

    @Override
    public void onDisable() {

    }

}
