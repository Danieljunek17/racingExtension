package me.danieljunek17.racingcommission.utils;

import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static ItemStack createItem(Material material, int amount, boolean hideAttributes, int dura, String displayName, List lore) {
        //creeër een item en voeg de meta, lore en al de rest toe aan het item
        ItemStack stack = new ItemStack(material, amount);
        ItemMeta meta = stack.getItemMeta();
        if(dura != 0) {
            ((Damageable) meta).setDamage(dura);
        }
        if (hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        meta.setDisplayName(color(displayName));
        List<String> newLore = new ArrayList<>();
        for (int i = 0; i < lore.size() ;i++) {
            newLore.add(color((String) lore.get(i)));
        }
        meta.setLore(newLore);
        stack.setItemMeta(meta);
        // return het item om erin te kunnen zetten
        return stack;
    }
    public static ItemStack createItem(Material material, int amount, boolean hideAttributes, int dura, String displayName, String... lore) {
        return createItem(material, amount, hideAttributes, dura, displayName, Arrays.asList(lore));
    }

    public static ItemStack createHead(Player player, String displayName, List lore) {
        //creeër een item en voeg de meta, lore en al de rest toe aan het item
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        OfflinePlayer offplayer = Bukkit.getOfflinePlayer(player.getUniqueId());
        meta.setOwningPlayer(offplayer);
        meta.setDisplayName(color(displayName));
        List<String> newLore = new ArrayList<>();
        for (int i = 0; i < lore.size() ;i++) {
            newLore.add(color((String) lore.get(i)));
        }
        meta.setLore(newLore);
        head.setItemMeta(meta);
        // return het item om erin te kunnen zetten
        return head;
    }
    public static ItemStack createHead(Player player, String displayName, String... lore) {
        return createHead(player, displayName, Arrays.asList(lore));
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }


    public static void spawnParticles(Location location, Particle particle, int amount, double offset) {
        location.getWorld().spawnParticle(particle, location, amount, offset, offset, offset);
    }

    public static int getPermissionLevel(Player player, String permission) {
        for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
            String permissions = info.getPermission().toLowerCase();
            if (permissions.startsWith(permission + '.')) {
                try {
                    return Integer.parseInt(permissions.replace(permission + '.', ""));
                } catch (Exception ignored) { continue; }
            }
        }
        return 0;
    }
}
