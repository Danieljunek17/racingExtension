package me.danieljunek17.racingcommission.objects;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class WheelsData implements ConfigurationSerializable {

    private String displayname, name;
    private int maxdurability, speed;
    private Material material;
    private boolean regenband;

    public WheelsData(String name, String displayname, int maxdurability, int speed, String material, boolean regenband) {
        this.name = name;
        this.displayname = displayname;
        this.maxdurability = maxdurability;
        this.speed = speed;
        this.material = Material.getMaterial(material.toUpperCase());
        this.regenband = regenband;
    }

    public String getName() {
        return name;
    }

    public String getDisplayname() {
        return displayname;
    }

    public int getMaxDurability() {
        return maxdurability;
    }

    public int getSpeed() {
        return speed;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isRegenband() {
        return regenband;
    }

    public static class Manager {
        public static Map<String, WheelsData> wheelsData = new LinkedHashMap<>();

        public static void newWheelsData(String state, WheelsData wheelData){
            wheelsData.put(state, wheelData);
        }
        public static CompletableFuture<WheelsData> getWheelsData(String state) {
            return CompletableFuture.supplyAsync(() -> wheelsData.get(state));
        }
//        public static CompletableFuture<WheelsData> getWheelsByMaterial(Material material) {
//            return CompletableFuture.supplyAsync(() -> wheelsData.values().stream().filter(wheel -> wheel.getMaterial() == material).findFirst().orElse(null));
//        }
    }

    @Override
    public String toString() {
        return "WheelsData{" +
                "displayname='" + displayname + '\'' +
                ", name='" + name + '\'' +
                ", maxdurability=" + maxdurability +
                ", speed=" + speed +
                ", material=" + material +
                '}';
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("displayname", displayname);
        map.put("name", name);
        map.put("maxdurability", maxdurability);
        map.put("speed", speed);
        map.put("material", material.name());
        map.put("regenband", regenband);
        return map;
    }

    public static WheelsData deserialize(Map<String, Object> map) {
        return new WheelsData((String) map.get("name"), (String) map.get("displayname"), (Integer) map.get("maxdurability"), (Integer) map.get("speed"), (String) map.get("material"), (boolean) map.get("regenband"));
    }

}

