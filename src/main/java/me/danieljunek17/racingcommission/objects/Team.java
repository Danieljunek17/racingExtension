package me.danieljunek17.racingcommission.objects;

import me.danieljunek17.racingcommission.gui.MainGUI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Team {

    private String name, permission;
    private List<VehicleData> vehicleDataList;

    public Team(String name, String permission, List<VehicleData> vehicleDataList) {
        this.name = name;
        this.permission = permission;
        this.vehicleDataList = vehicleDataList;
    }


    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public List<VehicleData> getVehicleDataList() {
        return vehicleDataList;
    }

    public static class Manager {
        public static List<Team> teamdata = new ArrayList<>();

        public static void newTeam(Team team){
            teamdata.add(team);
        }
        public static CompletableFuture<Team> getTeamByName(String name) {
            return CompletableFuture.supplyAsync(() -> teamdata.stream().filter(team -> team.getName().equalsIgnoreCase(name)).findFirst().orElse(null));
        }
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", permission='" + permission + '\'' +
                ", vehicleDataList=" + vehicleDataList +
                '}';
    }
}