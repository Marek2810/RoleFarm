package me.Marek2810.RoleFarm;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public static Main inst;
    
    @Override
    public void onEnable() {
    	this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {

    }

}
