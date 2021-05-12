package me.danieljunek17.racingcommission.commands;

import me.danieljunek17.racingcommission.gui.AdminSelectGUI;
import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.utils.Utils;
import me.danieljunek17.racingcommission.utils.commands.Commands;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.addons.Part;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.addons.seats.Seat;
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

        if(args[0].equalsIgnoreCase("setteam")) {
            if(args[1].isEmpty()) {
                player.sendMessage(Utils.color("&cJe moet een team opgeven"));
                return false;
            }
            for(Team team : Team.Manager.teamdata) {
                for(VehicleData vehicleData : team.getVehicleDataList()) {
                    for(Part part : vehicleData.getStorageVehicle().getSpawnedVehicle().getPartList()) {
                        if(part instanceof Seat) {
                            if(((Seat) part).getSteer()) {
                                if(((Seat) part).getPassenger() == player) {
                                    team.getVehicleDataList().remove(vehicleData);
                                    Team newTeam = Team.Manager.getTeamByName(args[1]).join();
                                    if(newTeam == null) {
                                        player.sendMessage(Utils.color("&cJe moet een geldig team opgeven"));
                                        return false;
                                    }
                                    newTeam.getVehicleDataList().add(vehicleData);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            player.sendMessage(Utils.color("&cJe moet in een vehicle zitten op de driverseat om die in een team te zetten"));
        } else {
            AdminSelectGUI.AdminSelectMenu(player);
        }
        return false;
    }
}
