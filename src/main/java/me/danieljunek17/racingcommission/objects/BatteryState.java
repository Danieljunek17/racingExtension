package me.danieljunek17.racingcommission.objects;

import org.bukkit.Material;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BatteryState {

    private double charge;
    private int speed;
    private String name, displayName;
    private Material material;

    public BatteryState(int speed, String name, double charge, String displayName, String material) {
        this.speed = speed;
        this.name = name;
        this.charge = charge;
        this.displayName = displayName;
        this.material = Material.valueOf(material.toUpperCase());
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public double getCharge() {
        return charge;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public static class Manager {
        public static Map<String, BatteryState> batteryStates = new LinkedHashMap<>();

        public static void newBatteryState(String state, BatteryState batteryState){
            batteryStates.put(state, batteryState);
        }
        public static CompletableFuture<BatteryState> getBatteryState(String state) {
            if(batteryStates.containsKey(state)) {
                return CompletableFuture.supplyAsync(() -> batteryStates.get(state));
            }
            return null;
        }
    }

    public enum BatteryStateEnum {
        CHARGING,
        STANDARD,
        BOOST;

        public BatteryState getState() {
            return BatteryState.Manager.getBatteryState(name().toLowerCase()).join();
        }
    }

    @Override
    public String toString() {
        return "BatteryState{" +
                "charge=" + charge +
                ", speed=" + speed +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
