package me.danieljunek17.racingcommission.commands.subcommandwheels;

import me.danieljunek17.racingcommission.gui.WheelsGUI;
import me.danieljunek17.racingcommission.utils.commands.subcommands.SubCommands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Get extends SubCommands {
    public Get() {
        super("get", "get the wheels", "get", "racemenu.wheels.get");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = ((Player) sender).getPlayer();

        WheelsGUI.WheelsMenu(player);
        return false;
    }
}
