package me.danieljunek17.racingcommission.gui;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.utils.Utils;
import me.danieljunek17.racingcommission.utils.YAMLFile;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AdminSelectGUI {

    private static YAMLFile settings = Racingcommission.getSettings();

    public static void AdminSelectMenu(Player player) {
        String[] guiSetup = {
                "xxMxxxPxx"
        };

        InventoryGui gui = new InventoryGui(Racingcommission.getInstance(), Utils.color("&cRace Computer Manager"), guiSetup);

        gui.setFiller(Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, 1, true, 0, ""));

        gui.addElement(new StaticGuiElement('M', Utils.createItem(Material.valueOf(settings.getString("settings.ControllerBlock").toUpperCase()), 1, true, 0, "&cManager"), click -> {
            AdminGUI.AdminMenu(player);
            gui.close();
            return true;
        }));

        gui.addElement(new StaticGuiElement('P', Utils.createItem(Material.valueOf(settings.getString("settings.PitstopWheelChangeBlock").toUpperCase()), 1, true, 0, "&cPitStop"), click -> {
            AdminPitStopGUI.AdminPitStopMenu(player);
            gui.close();
            return true;
        }));


        gui.show(player);
    }

}
