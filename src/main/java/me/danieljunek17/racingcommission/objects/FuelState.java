package me.danieljunek17.racingcommission.objects;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FuelState {

    private double usage;
    private int speed;
    private String name, displayName;
    private Material material;

    public FuelState(int speed, String name, double usage, String displayName, String material) {
        this.speed = speed;
        this.name = name;
        this.usage = usage;
        this.displayName = displayName;
        this.material = Material.valueOf(material.toUpperCase());
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public double getUsage() {
        return usage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public static class Manager {
        public static Map<String, FuelState> fuelStates = new LinkedHashMap<>();

        public static void newFuelState(String state, FuelState fuelState){
            fuelStates.put(state, fuelState);
        }
        public static CompletableFuture<FuelState> getFuelState(String state) {
            if(fuelStates.containsKey(state)) {
                return CompletableFuture.supplyAsync(() -> fuelStates.get(state));
            }
            return null;
        }
    }

    public enum FuelStateEnum {
        ECONOMICAL,
        BALANCE,
        OVERDRIVE;

        public FuelState getState() {
            return FuelState.Manager.getFuelState(name().toLowerCase()).join();
        }
    }

    @Override
    public String toString() {
        return "FuelState{" +
                "charge=" + usage +
                ", speed=" + speed +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
