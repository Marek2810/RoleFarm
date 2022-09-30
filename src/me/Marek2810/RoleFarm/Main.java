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
	public static List<String> yamlCropsList;
	public static List<String> yamlNeedWatherList;
	public static List<String> yamlWateredList;
	public static int waterBucketMaxUsages;
    
    @Override
    public void onEnable() {
    	inst = this;
    	cropsData = new DataManager(this, "crops.yml");
    	this.saveDefaultConfig();    	
    	harvest = new Harvest();    	
    	this.getServer().getPluginManager().registerEvents(harvest, this);  
    	watering = new Watering();
    	this.getServer().getPluginManager().registerEvents(watering, this);
    	//Loading crops from config.yml
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading crops...");
    	yamlCropsList = this.getConfig().getStringList("crops");
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Crops loaded.");
    	//Loading need wather from config.yml
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading need wather...");
    	yamlNeedWatherList = this.getConfig().getStringList("need_wather");
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Need wather loaded.");
    	//Loading watered from crops.yml
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading wathered...");
    	yamlWateredList = cropsData.getConfig().getStringList("crop.watered");
    	this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Wathered loaded.");
    	waterBucketMaxUsages = this.getConfig().getInt("options.water-bucket-max-usages");
    }

    @Override
    public void onDisable() {

    }

}
