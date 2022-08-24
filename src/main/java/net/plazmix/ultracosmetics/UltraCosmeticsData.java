package net.plazmix.ultracosmetics;

import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.ultracosmetics.version.VersionManager;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

/**
 * This class is only for cleaning main class a bit.
 *
 * @author iSach
 * @since 08-05-2016
 */
public class UltraCosmeticsData {

    private static UltraCosmeticsData instance;
    /**
     * A String that items that shouldn't be picked up are given. Randomly generated each time the server starts.
     */
    private final String itemNoPickupString;
    /**
     * If true, the server is using Spigot and not CraftBukkit/Bukkit.
     */
    private boolean usingSpigot = false;

    /**
     * True -> should execute custom command when going back to main menu.
     */
    private boolean customCommandBackArrow;

    /**
     * Command to execute when going back to Main Menu.
     */
    private String customBackMenuCommand;

    /**
     * Determines if Ammo Use is enabled.
     */
    private boolean ammoEnabled;

    /**
     * Determines if File Storage is enabled.
     */
    private boolean fileStorage = true;

    /**
     * Determines if Gadget Cooldown should be shown in action bar.
     */
    private boolean cooldownInBar;

    /**
     * Should the GUI close after Cosmetic Selection?
     */
    private boolean closeAfterSelect;

    /**
     * If true, the color will be removed in placeholders.
     */
    private boolean placeHolderColor;

    /**
     * Server NMS version.
     */
    private ServerVersion serverVersion;

    /**
     * NMS Version Manager.
     */
    private VersionManager versionManager;

    private UltraCosmetics ultraCosmetics;

    /**
     * For bStats stuff
     */
    private boolean cosmeticsProfilesEnabled;

    public UltraCosmeticsData(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;
        this.usingSpigot = ultraCosmetics.getServer().getVersion().contains("Spigot");
        this.itemNoPickupString = UUID.randomUUID().toString();
    }

    public static UltraCosmeticsData get() {
        return instance;
    }

    public static void init(UltraCosmetics ultraCosmetics) {
        instance = new UltraCosmeticsData(ultraCosmetics);
    }

    void initModule() {
        versionManager = new VersionManager(serverVersion);
        try {
            versionManager.load();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        versionManager.getModule().enable();
    }

    boolean checkServerVersion() {
        String mcVersion = "1.8.0";

        try {
            mcVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        ServerVersion serverVersion;

        if (mcVersion.startsWith("v")) {
            try {
                serverVersion = ServerVersion.valueOf(mcVersion);
            } catch (Exception exc) {
                Bukkit.getPluginManager().disablePlugin(ultraCosmetics);
                return false;
            }
        } else serverVersion = ServerVersion.v1_8_R1;

        UltraCosmeticsData.get().setServerVersion(serverVersion);

        return true;
    }

    public void initConfigFields() {
//        this.fileStorage = SettingsManager.getConfig().getString("Ammo-System-For-Gadgets.System").equalsIgnoreCase("file");
//        this.placeHolderColor = SettingsManager.getConfig().getBoolean("Chat-Cosmetic-PlaceHolder-Color");
//        this.ammoEnabled = SettingsManager.getConfig().getBoolean("Ammo-System-For-Gadgets.Enabled");
//        this.cooldownInBar = SettingsManager.getConfig().getBoolean("Categories.Gadgets.Cooldown-In-ActionBar");
        this.customCommandBackArrow = ultraCosmetics.getConfig().getBoolean("Categories.Back-To-Main-Menu-Custom-Command.Enabled");
        this.customBackMenuCommand = ultraCosmetics.getConfig().getString("Categories.Back-To-Main-Menu-Custom-Command.Command").replace("/", "");
        this.closeAfterSelect = ultraCosmetics.getConfig().getBoolean("Categories.Close-GUI-After-Select");
//        this.enabledWorlds = ultraCosmetics.getConfig().getStringList("Enabled-Worlds");
        this.cosmeticsProfilesEnabled = ultraCosmetics.getConfig().getBoolean("Auto-Equip-Cosmetics.is-enabled");
    }

    public boolean isAmmoEnabled() {
        return ammoEnabled;
    }

    public boolean shouldCloseAfterSelect() {
        return closeAfterSelect;
    }

    public boolean displaysCooldownInBar() {
        return cooldownInBar;
    }

    public boolean arePlaceholdersColored() {
        return placeHolderColor;
    }

    public boolean isUsingSpigot() {
        return usingSpigot;
    }

    public VersionManager getVersionManager() {
        return versionManager;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(ServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    /**
     * Should be only used for running Bukkit Runnables.
     *
     * @return UltraCosmetics instance. (As Plugin)
     */
    public UltraCosmetics getPlugin() {
        return ultraCosmetics;
    }

    public final String getItemNoPickupString() {
        return this.itemNoPickupString;
    }


    public boolean areCosmeticsProfilesEnabled() {
        return cosmeticsProfilesEnabled;
    }
}
