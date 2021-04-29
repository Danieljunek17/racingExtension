package me.danieljunek17.racingcommission.commands;

import me.danieljunek17.racingcommission.gui.AdminGUI;
import me.danieljunek17.racingcommission.gui.MainGUI;
import me.danieljunek17.racingcommission.utils.commands.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Admin extends Commands {
    public Admin() {
        super("admin", "open het admin menu", "admin", "racemenu.admin");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = ((Player) sender).getPlayer();
        AdminGUI.AdminMenu(player);
        return false;
    }
}
