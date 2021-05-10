package me.danieljunek17.racingcommission.utils.commands;

import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.commands.Admin;
import me.danieljunek17.racingcommission.commands.Wheels;
import me.danieljunek17.racingcommission.utils.commands.subcommands.SubCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor{

    public static List<Commands> command = new ArrayList<>();

    public CommandHandler() {
        command.addAll(Arrays.asList(
                new Wheels(),
                new Admin()
        ));
    }

    public void registerCommands() {
        for(Commands commands : CommandHandler.command) {
            String name = commands.getName();
            Bukkit.getConsoleSender().sendMessage(name);
            Racingcommission.getInstance().getCommand(name).setExecutor(new SubCommandHandler());
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for(Commands commands : CommandHandler.command) {
            if (label.equalsIgnoreCase(commands.getName())) {
                if(sender instanceof Player) {
                    Player player = ((Player) sender).getPlayer();
                    if (player.hasPermission(commands.getPermission())) {
                        commands.onCommand(sender, command, label, args);
                    }
                } else {
                    commands.onCommand(sender, command, label, args);
                }
            }
        }
        return false;
    }
}
