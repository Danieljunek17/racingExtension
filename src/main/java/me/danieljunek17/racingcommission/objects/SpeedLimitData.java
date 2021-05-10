package me.danieljunek17.racingcommission.objects;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SpeedLimitData {

    private Material material;
    private int speedLimit;

    public SpeedLimitData(String material, int speedLimit) {
        this.material = Material.valueOf(material.toUpperCase());
        this.speedLimit = speedLimit;
    }

    public Material getMaterial() {
        return material;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public static class Manager {
        public static Map<Material, SpeedLimitData> speedLimits = new HashMap<>();

        public static void newSpeedLimit(SpeedLimitData speedLimitData){
            speedLimits.put(speedLimitData.getMaterial(), speedLimitData);
        }
        public static CompletableFuture<Map<Material, SpeedLimitData>> getSpeedLimits() {
            return CompletableFuture.supplyAsync(() -> speedLimits);
        }
    }
}
