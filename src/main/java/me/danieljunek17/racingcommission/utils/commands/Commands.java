package me.danieljunek17.racingcommission.utils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class Commands {
    private String name, description, usage, permission;

    public Commands(String name, String description, String usage, String permission) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getUsage() {
        return this.usage;
    }
}
