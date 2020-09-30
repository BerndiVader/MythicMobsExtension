package com.gmail.berndivader.mythicmobsext.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.MythicMobs;
import net.md_5.bungee.api.ChatColor;

public class VersionCommand implements CommandExecutor {
	
	private final Main plugin;

	public VersionCommand(Main plugin) {
	    this.plugin = plugin;
	    plugin.getCommand("mme").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		List<String> commands = Arrays.asList("version");
		
		if (args.length > 0 && args[0].equals("version")) {
	        String MMEV = ChatColor.YELLOW +"MMExtension Version: "+ChatColor.WHITE+ plugin.getDescription().getVersion()+"\n";
	        String SV = ChatColor.YELLOW +"Server Version: "+ChatColor.WHITE+Bukkit.getServer().getClass().getName()+ "\n";
	        String MMV = ChatColor.YELLOW +"MM Version: "+ChatColor.WHITE+MythicMobs.inst().getVersion()+ "\n";
	        String MMB = ChatColor.YELLOW +"MM Build: "+ChatColor.WHITE+MythicMobs.inst().getBuildNumber();
	        
	        String msg = SV + MMEV + MMV + MMB;
	        
	        sender.sendMessage("\n"+msg);
	        
	        return true;}
		
		String x = commands.stream().collect(Collectors.joining(","));
		sender.sendMessage(ChatColor.YELLOW +"[MME] §rAvaliable commands: "+ChatColor.WHITE+x);
		return true;
		
    }
}
