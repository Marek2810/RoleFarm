package me.Marek2810.RoleFarm;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.RoleFarm.Listeners.Harvest;

public class Main extends JavaPlugin implements Listener {

	public static Main inst;
	public static Harvest harvest;
    
    @Override
    public void onEnable() {
    	inst = this;
    	this.saveDefaultConfig();
    	harvest = new Harvest();
    	this.getServer().getPluginManager().registerEvents(harvest, this);   	
    }

    @Override
    public void onDisable() {

    }

}
