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
	//config.yml Crops
	public static ConfigurationSection yamlCropsSection;
	public static Set<String> yamlCrops;
	//config.yml Fertilizer
	public static ConfigurationSection yamlFertilizersSection;
	public static Set<String> yamlFertilizers;
	//config.yaml Drops
	public static List<String> yamlDropsList;
	//config.yml Need Water/Fertilizer
	public static List<String> yamlNeedWaterList;
	public static List<String> yamlNeedFertilizerList;
	//config.yml options
	public static int waterBucketMaxUsages;
	public static double wateringMulti;
	public static double fertilisationMulti;
	//config.yml messages
	public static ConfigurationSection yamlMessages;
	//crops.yml
	public static List<String> yamlWateredList;
	public static List<String> yamlFertilizedList;
	public static ConsoleCommandSender console;
	public static String logPrefix;
    
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
    	//Console
    	console = this.getServer().getConsoleSender();
    	logPrefix = "&7[&6RoleFarm&7] ";
    
    	//Loading fertilizer from config.yml
    	yamlFertilizersSection = inst.getConfig().getConfigurationSection("fertilizer");
    	yamlFertilizers = yamlFertilizersSection.getKeys(false);
    	
    	//Loading crops from config.yml
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading crops..."));
    	//console.sendMessage(ChatColor.BLUE + "[RoleFarm] " + ChatColor.YELLOW + "Loading crops...");
    	yamlCropsSection = inst.getConfig().getConfigurationSection("crops");
    	yamlCrops = yamlCropsSection.getKeys(false);
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aCrops loaded."));
    	//console.sendMessage(ChatColor.BLUE + "[RoleFarm] " + ChatColor.GREEN + "Crops loaded.");  	
    	//Loading drops from config.yml
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading drops..."));
    	yamlDropsList = this.getConfig().getStringList("drops");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aDrops loaded."));

    	//Loading need water from config.yml    	
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading need water..."));
    	yamlNeedWaterList = this.getConfig().getStringList("need-water");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aNeed water loaded."));
    	//Loading need fertilizer from config.yml
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading need fertilizer..."));
    	yamlNeedFertilizerList = this.getConfig().getStringList("need-fertilizer");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aNeed fertilizer loaded."));
    	//Loading watered from crops.yml
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading watered..."));
    	yamlWateredList = cropsData.getConfig().getStringList("crops.watered");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aWatered loaded."));
    	//Loading fertilized from crops.yml
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading fertilized..."));
    	yamlFertilizedList = cropsData.getConfig().getStringList("crops.fertilized");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aFertilized loaded."));
    	// Loading options variables   	
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading options variables..."));
    	waterBucketMaxUsages = this.getConfig().getInt("options.water-bucket-max-usages");   
    	wateringMulti = this.getConfig().getDouble("options.watering-multiplier");
    	fertilisationMulti = this.getConfig().getDouble("options.fertilisation-multiplier");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aOptions variables loaded."));
    	// Loading messages
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eoading messages..."));
    	yamlMessages = this.getConfig().getConfigurationSection("messages");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aMessages loaded."));
    }

    @Override
    public void onDisable() {

    }
}