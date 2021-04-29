package me.danieljunek17.racingcommission.utils.commands.subcommands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommands {
    private String name, description, usage, permission;

    public SubCommands(String name, String description, String usage, String permission) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    public abstract boolean execute(CommandSender sender, String label, String[] args);

    public List<String> onTabComplete(String[] args) {
        return new ArrayList<>();
    }

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
