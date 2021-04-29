package me.danieljunek17.racingcommission.utils.commands.subcommands;

import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.commands.subcommandwheels.Get;
import me.danieljunek17.racingcommission.utils.Utils;
import me.danieljunek17.racingcommission.utils.YAMLFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SubCommandHandler implements CommandExecutor, TabCompleter {

    public static List<SubCommands> commands = new ArrayList<>();
    YAMLFile messages = Racingcommission.getMessages();

    public SubCommandHandler() {
        commands.addAll(Arrays.asList(
            new Get()
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0) {
            Racingcommission.commandHandler.onCommand(sender, command, label, args);
            return false;
        }

        SubCommands subCommand = getCommand(args[0]);
        if (subCommand == null) {
            sender.sendMessage(Utils.color(messages.getString("Errors.Unknowcommand")));
        } else if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
            sender.sendMessage(Utils.color(messages.getString("Errors.Nopermission")));
        } else if (subCommand.execute(sender, label, Arrays.copyOfRange(args, 1, args.length))) {
            sender.sendMessage(Utils.color(messages.getString("Errors.Invalidargument")));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return commands.stream()
                    .map(SubCommands::getName)
                    .filter(name -> name.startsWith(args[0]))
                    .collect(Collectors.toList());
        }

        SubCommands subCommand = getCommand(args[0]);
        if (subCommand == null || subCommand.getPermission() == null || !sender.hasPermission(subCommand.getPermission())) return null;

        return subCommand.onTabComplete(Arrays.copyOfRange(args, 1, args.length))
                .stream()
                .filter(name -> name.toLowerCase().startsWith(args[args.length - 1]))
                .collect(Collectors.toList());
    }

    public static List<SubCommands> getCommands() {
        return commands;
    }

    public SubCommands getCommand(String name) {
        return commands.stream().filter(subCmd -> subCmd.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
