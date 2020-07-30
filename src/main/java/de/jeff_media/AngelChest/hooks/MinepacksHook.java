package de.jeff_media.AngelChest.hooks;

import at.pcgamingfreaks.Minepacks.Bukkit.API.MinepacksPlugin;
import de.jeff_media.AngelChest.Main;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MinepacksHook {

	boolean disabled = false;
	boolean skipReflection = false;
	MinepacksPlugin minepacks = null;

	public boolean isMinepacksBackpack(ItemStack is,Main plugin) {
		if(disabled) return false;
		if(is==null) return false;
		if(skipReflection) {
			return minepacks.isBackpackItem(is);
		}
		if(Bukkit.getPluginManager().getPlugin("Minepacks") == null) {
			plugin.getLogger().warning("Minepacks is null");
			disabled = true;
			return false;
		}
		minepacks = (MinepacksPlugin) Bukkit.getPluginManager().getPlugin("Minepacks");
		if(!(minepacks instanceof MinepacksPlugin)) {
			plugin.getLogger().warning("You are using a version of Minepacks that is too old and does not implement or extend MinecpacksPlugin: "+minepacks.getClass().getName());
			disabled=true;
			return false;
		}


		try {
			if(minepacks.getClass().getMethod("isBackpackItem", ItemStack.class) != null) {
				skipReflection=true;
				return (minepacks.isBackpackItem(is));
			}
		} catch (NoSuchMethodException | SecurityException e) {
			plugin.getLogger().warning("You are using a version of Minepacks that is too old and does not implement every API method needed by AngelChest. Minepacks hook will be disabled.");
			disabled=true;
			return false;
		}
		return false;
	}
}