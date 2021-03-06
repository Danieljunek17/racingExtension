package me.danieljunek17.racingcommission.commands;

import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.gui.AdminSelectGUI;
import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.utils.Utils;
import me.danieljunek17.racingcommission.utils.YAMLFile;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.addons.Part;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.addons.seats.Seat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.locks.Lock;

public class Admin implements CommandExecutor {

    private YAMLFile data = Racingcommission.getDataFile();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = ((Player) sender).getPlayer();
        if(!player.hasPermission("racemenu.admin")) return false;

        if(args.length != 0) {
            if(args[0].equalsIgnoreCase("lockspeed")) {
                LockSpeed.lockSpeed(player, args[1]);
                return false;
            }else if(args[0].equalsIgnoreCase("setteam")) {
                if(!player.hasPermission("racemenu.admin.setteam")) {
                    player.sendMessage(Utils.color("&cje moet een getal opgeven om de speed op te limiteren"));
                    return false;
                }
                if (args[1].isEmpty()) {
                    player.sendMessage(Utils.color("&cJe moet een team opgeven"));
                    return false;
                }
                for (Team team : Team.Manager.teamdata) {
                    for (VehicleData vehicleData : team.getVehicleDataList()) {
                        for (Part part : vehicleData.getStorageVehicle().getSpawnedVehicle().getPartList()) {
                            if (part instanceof Seat) {
                                if (((Seat) part).getSteer()) {
                                    if (((Seat) part).getPassenger() == player) {
                                        Team newTeam = Team.Manager.getTeamByName(args[1]).join();
                                        if (newTeam == null) {
                                            player.sendMessage(Utils.color("&cJe moet een geldig team opgeven"));
                                            return false;
                                        }
                                        team.getVehicleDataList().remove(vehicleData);
                                        data.set(vehicleData.getStorageVehicle().getUuid() + ".team", newTeam.getName());
                                        newTeam.getVehicleDataList().add(vehicleData);
                                        player.sendMessage(Utils.color("&aJe hebt deze auto in team&c " + newTeam.getName() + "&a gezet"));
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
                player.sendMessage(Utils.color("&cJe moet in een vehicle zitten op de driverseat om die in een team te zetten"));
            }
        } else {
            AdminSelectGUI.AdminSelectMenu(player);
        }
        return false;
    }
}
