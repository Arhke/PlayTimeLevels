package net.waterraid.PlayTimeLevels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        if (commandSender instanceof Player && args.length == 0) {
            int[] stat = PlayTimeManager.getStat(((Player) commandSender).getUniqueId());
            int time = stat[0],
                    xp = stat[1],
                    level = stat[2],
                    relativexp = stat[3];
            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.GOLD+"============<"+ChatColor.AQUA+"Your PlayTime Stats"+ChatColor.GOLD+">============\n");
            sb.append(ChatColor.GOLD +"Name: "+((Player) commandSender).getDisplayName()+"        Played For "+time+" Hrs        Total Xp: "+ xp+"\n");
            sb.append(ChatColor.GOLD + "Current Level: "+level+"\nProgress Bar: ");
            int xpleft = PlayTimeManager.getLevelUpXp(level)-relativexp;
            int xppercent = relativexp*10/PlayTimeManager.getLevelUpXp(level);

            for (int i = 0; i<10;i++){
                if(i<xppercent){
                    sb.append(ChatColor.GREEN+"█");
                }else {
                    sb.append(ChatColor.RED+"█");
                }
            }
            sb.append("\n"+ChatColor.GOLD +"XP Left To Level Up: "+ xpleft);
            commandSender.sendMessage(sb.toString());
            return true;
        }
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.AQUA+"/playtime xp add/subtract <amount> <playername>\n/playtime level add/subtract <amount> <playername>");
        }
        else if (args.length < 4) {
            if (args[0].equalsIgnoreCase("xp")) {
                commandSender.sendMessage(ChatColor.RED+"Usage: /playtime xp add/subtract <amount> <playername>");
            }else if (args[0].equalsIgnoreCase("level")) {
                commandSender.sendMessage(ChatColor.RED+"Usage: /playtime level add/subtract <amount> <playername>");
            }
        }
        else if (args.length == 4) {
            if (!isNumber(args[2])) {
                commandSender.sendMessage(ChatColor.RED + args[2]+" is not a number please input a number");
                commandSender.sendMessage(ChatColor.AQUA+"/playtime xp add/subtract <amount> <playername>\n/playtime level add/subtract <amount> <playername>");
                return true;
            }
            Player player = Bukkit.getPlayer(args[3]);
            if (player == null){
                commandSender.sendMessage(ChatColor.RED+args[4]+" was not found on the server!");
            }
            int amount = Integer.parseInt(args[2]);
            if (args[1].equalsIgnoreCase("subtract")) {
                amount *=-1;
            }else if (!args[1].equalsIgnoreCase("add")) {
                commandSender.sendMessage(ChatColor.RED+args[1]+" is not recognized... assuming its add");
            }
            if (args[0].equalsIgnoreCase("xp")) {
                PlayTimeManager.addXp(player.getUniqueId(), amount);
                commandSender.sendMessage(ChatColor.AQUA+"Xp was Added to " + player.getName());
            }else if (args[0].equalsIgnoreCase("level")) {
                PlayTimeManager.addLevel(player.getUniqueId(), amount);
                commandSender.sendMessage(ChatColor.AQUA+"Level was Added to " + player.getName());
            }
        }
        else {
            commandSender.sendMessage(ChatColor.AQUA+"/playtime xp add/subtract <amount> <playername>\n/playtime level add/subtract <amount> <playername>");
        }
        return true;
    }
    public boolean isNumber(String s){
        try {
            int a = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
