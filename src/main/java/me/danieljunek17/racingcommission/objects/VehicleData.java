package me.danieljunek17.racingcommission.objects;

import de.themoep.inventorygui.InventoryGui;
import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.gui.BatteryGUI;
import me.danieljunek17.racingcommission.gui.ChangeGUI;
import me.danieljunek17.racingcommission.gui.FuelGUI;
import me.danieljunek17.racingcommission.gui.SelectorGUI;
import me.danieljunek17.racingcommission.utils.YAMLFile;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.BaseVehicle;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.StorageVehicle;
import org.bukkit.inventory.ItemStack;

public class VehicleData {

    private YAMLFile data = Racingcommission.getDataFile();

    private BaseVehicle baseVehicle;
    private StorageVehicle storageVehicle;
    private BatteryState batteryState;
    private FuelState fuelState;
    private int savedspeed, cachespeed, batteryboost, fuelboost, wheelboost, regenpenalty, lockedspeed;
    private double batteryPercentage;
    private InventoryGui batteryMenu, fuelMenu, selectorMenu, changeMenu;
    private boolean offgrid;
    private ItemStack wheelsItem;
    private WheelsData wheelsData;

    public VehicleData(BaseVehicle baseVehicle, StorageVehicle storageVehicle, int savedspeed, ItemStack wheelsItem, WheelsData wheelsData) {
        this.baseVehicle = baseVehicle;
        this.storageVehicle = storageVehicle;
        this.batteryState = BatteryState.BatteryStateEnum.STANDARD.getState();
        this.fuelState = FuelState.FuelStateEnum.BALANCE.getState();
        this.batteryPercentage = 100;
        this.batteryMenu = BatteryGUI.batteryMenu(this);
        this.fuelMenu = FuelGUI.fuelMenu(this);
        this.selectorMenu = SelectorGUI.selectorMenu(this);
        this.changeMenu = ChangeGUI.changeMenu(Team.Manager.getTeamByVehicle(this).join(), this);
        this.savedspeed = savedspeed;
        this.cachespeed = savedspeed;
        this.fuelboost = 0;
        this.batteryboost = 0;
        this.wheelboost = 0;
        this.regenpenalty = 0;
        this.wheelsItem = wheelsItem;
        this.wheelsData = wheelsData;
        this.lockedspeed = 0;
    }

    public BaseVehicle getBaseVehicle() {
        return baseVehicle;
    }

    public StorageVehicle getStorageVehicle() {
        return storageVehicle;
    }

    public BatteryState getBatteryState() {
        return batteryState;
    }

    public void setBatteryState(BatteryState batteryState) {
        this.batteryState = batteryState;
        setBatteryboost(batteryState.getSpeed());
    }

    public FuelState getFuelState() {
        return fuelState;
    }

    public void setFuelState(FuelState fuelState) {
        this.fuelState = fuelState;
    }

    public double getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(double batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
        if(batteryPercentage == 0D) {
            this.batteryState = BatteryState.Manager.getBatteryState("standard").join();
        }
    }

    public int getLockedspeed() {
        return lockedspeed;
    }

    public void setLockedspeed(int lockedspeed) {
        this.lockedspeed = lockedspeed;
    }

    public InventoryGui getBatteryMenu() {
        return batteryMenu;
    }

    public InventoryGui getFuelMenu() {
        return fuelMenu;
    }

    public InventoryGui getSelectorMenu() { return selectorMenu; }

    public InventoryGui getChangeMenu() {
        return changeMenu;
    }

    public boolean isOffgrid() {
        return offgrid;
    }

    public void setOffgrid(boolean offgrid, SpeedLimitData speedLimitData) {
        if(offgrid) {
            enableOffGrid(speedLimitData);
        } else {
            disableOffGrid();
        }
    }

    public void enableOffGrid(SpeedLimitData speedLimitData) {
        this.offgrid = true;
        setBatteryState(BatteryState.BatteryStateEnum.STANDARD.getState());
        getStorageVehicle().getVehicleStats().setSpeed(speedLimitData.getSpeedLimit());
    }

    public void disableOffGrid() {
        this.offgrid = false;
        getStorageVehicle().getVehicleStats().setSpeed(getCachespeed() + getBatteryboost() + getFuelboost() + getRegenpenalty() + getWheelboost());
    }

    public void setSavedspeed(int savedspeed) {
        this.savedspeed = savedspeed;
        data.set(getStorageVehicle().getUuid() + ".speed", savedspeed);
        data.saveFile();
    }

    public int getCachespeed() {
        return cachespeed;
    }

    public void setCachespeed(int cachespeed) {
        this.cachespeed = cachespeed;
    }

    public int getRegenpenalty() {
        if(!wheelsData.isRegenband()) {
            return regenpenalty;
        } else {
            return 0;
        }
    }

    public void setRegenpenalty(int regenpenalty) {
        this.regenpenalty = regenpenalty;
    }

    public int getBatteryboost() {
        return batteryboost;
    }

    public void setBatteryboost(int batteryboost) {
        this.batteryboost = batteryboost;
    }

    public int getFuelboost() {
        return fuelboost;
    }

    public void setFuelboost(int fuelboost) {
        this.fuelboost = fuelboost;
    }

    public int getWheelboost() {
        return wheelboost;
    }

    public void setWheelboost(int wheelboost) {
        this.wheelboost = wheelboost;
    }

    public ItemStack getWheelsItem() {
        return wheelsItem;
    }

    public void setWheelsItem(ItemStack wheelsItem) {
        this.wheelsItem = wheelsItem;
        data.set(getStorageVehicle().getUuid() + ".wheelItem", wheelsItem);
        data.saveFile();
    }

    public WheelsData getWheelsData() {
        return wheelsData;
    }

    public void setWheelsData(WheelsData wheelsData) {
        this.wheelsData = wheelsData;
        data.set(getStorageVehicle().getUuid() + ".wheelData", wheelsData);
        data.saveFile();
    }

}
