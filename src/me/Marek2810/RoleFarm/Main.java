package me.Marek2810.RoleFarm;

import java.util.List;
import java.util.Set;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.RoleFarm.Files.DataManager;
import me.Marek2810.RoleFarm.Listeners.Fertilisation;
import me.Marek2810.RoleFarm.Listeners.Harvest;
import me.Marek2810.RoleFarm.Listeners.Watering;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {

	public static Main inst;
	public static DataManager cropsData;
	public static Harvest harvest;
	public static Watering watering;	
	public static Fertilisation fertilisation;
	public static ConfigurationSection yamlCropsSection;
	public static Set<String> yamlCrops;
	public static List<String> yamlDropsList;
	public static List<String> yamlNeedWaterList;
	public static List<String> yamlNeedFertilizerList;
	public static List<String> yamlWateredList;
	public static List<String> yamlFertilizedList;
	public static int waterBucketMaxUsages;
	public static double wateringMulti;
	public static double fertilisationMulti;
    
    @Override
    public void onEnable() {
    	inst = this;
    	cropsData = new DataManager(this, "crops.yml");
    	this.saveDefaultConfig();    	
    	harvest = new Harvest();    	
    	this.getServer().getPluginManager().registerEvents(harvest, this);  
    	watering = new Watering();
    	this.getServer().getPluginManager().registerEvents(watering, this);
    	fertilisation = new Fertilisation();
    	this.getServer().getPluginManager().registerEvents(fertilisation, this);    
    	ConsoleCommandSender console = this.getServer().getConsoleSender();
    	//Loading crops from config.yml
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading crops...");
    	yamlCropsSection = this.getConfig().getConfigurationSection("crops");
    	yamlCrops = yamlCropsSection.getKeys(false); 	
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Crops loaded.");  	
    	//Loading drops from config.yml
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading drops...");
    	yamlDropsList = this.getConfig().getStringList("drops");
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Drops loaded."); 	    	    	
    	//Loading need water from config.yml
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading need water...");
    	yamlNeedWaterList = this.getConfig().getStringList("need-water");
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Need water loaded.");     	
    	//Loading need fertilizer from config.yml
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading need fertilizer...");    	
    	yamlNeedFertilizerList = this.getConfig().getStringList("need-fertilizer");
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Need fertilizer loaded.");  	
    	//Loading watered from crops.yml
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading watered...");  
    	yamlWateredList = cropsData.getConfig().getStringList("crop.watered");
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Watered loaded.");
    	//Loading fertilized from crops.yml
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading fertilized...");
    	yamlFertilizedList = cropsData.getConfig().getStringList("crop.fertilized");
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Fertilized loaded.");
    	// Loading options variables   	
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.YELLOW + "Loading options variables...");
    	waterBucketMaxUsages = this.getConfig().getInt("options.water-bucket-max-usages");   
    	wateringMulti = this.getConfig().getDouble("options.watering-multiplier");
    	fertilisationMulti = this.getConfig().getDouble("options.fertilisation-multiplier");
    	console.sendMessage(ChatColor.AQUA + "[RoleFarm] " + ChatColor.GREEN + "Options variables loaded.");
    }

    @Override
    public void onDisable() {

    }
}