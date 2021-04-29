package me.danieljunek17.racingcommission.commands;

import me.danieljunek17.racingcommission.utils.commands.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Wheels extends Commands {
    public Wheels() {
        super("wheels", "krijg de wielen", "wheels (get)", "racemenu.wheels.get");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        return false;
    }
}
