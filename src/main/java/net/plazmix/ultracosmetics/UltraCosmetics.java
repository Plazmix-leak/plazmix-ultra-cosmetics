package net.plazmix.ultracosmetics;

import net.plazmix.ultracosmetics.listeners.MainListener;
import net.plazmix.ultracosmetics.listeners.PlayerListener;
import net.plazmix.ultracosmetics.listeners.v1_9.PlayerSwapItemListener;
import net.plazmix.ultracosmetics.manager.ArmorStandManager;
import net.plazmix.ultracosmetics.menu.Menus;
import net.plazmix.ultracosmetics.mysql.CosmeticsSqlHandler;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.player.CosmeticPlayerManager;
import net.plazmix.ultracosmetics.run.FallDamageManager;
import net.plazmix.ultracosmetics.run.MovingChecker;
import net.plazmix.ultracosmetics.util.EntitySpawningManager;
import net.plazmix.ultracosmetics.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/*  Leaked by https://t.me/leak_mine
    - Все слитые материалы вы используете на свой страх и риск.

    - Мы настоятельно рекомендуем проверять код плагинов на хаки!
    - Список софта для декопиляции плагинов:
    1. Luyten (последнюю версию можно скачать можно тут https://github.com/deathmarine/Luyten/releases);
    2. Bytecode-Viewer (последнюю версию можно скачать можно тут https://github.com/Konloch/bytecode-viewer/releases);
    3. Онлайн декомпиляторы https://jdec.app или http://www.javadecompilers.com/

    - Предложить свой слив вы можете по ссылке @leakmine_send_bot или https://t.me/leakmine_send_bot
*/

public class UltraCosmetics extends JavaPlugin {
    private CosmeticPlayerManager playerManager;

    private Menus menus;
    private ArmorStandManager armorStandManager;

    @Override
    public void onEnable() {
        UltraCosmeticsData.init(this);

        if (!UltraCosmeticsData.get().checkServerVersion()) {
            return;
        }

        // Initialize Mysql integration
        CosmeticsSqlHandler.createTable();

        // Create UltraPlayer Manager.
        this.playerManager = new CosmeticPlayerManager(this);

        this.armorStandManager = new ArmorStandManager(this);

        // Initialize NMS Module
        UltraCosmeticsData.get().initModule();

        // Register Listeners.
        registerListeners();

        // Set up Cosmetics config.
        new CosmeticManager(this).setupCosmeticsConfigs();

        // Initialize UltraPlayers and give chest (if needed).

        // Start the Fall Damage and Invalid World Check Runnables.
        new FallDamageManager().runTaskTimerAsynchronously(this, 0, 1);
        new MovingChecker(this).runTaskTimerAsynchronously(this, 0, 1);

        this.menus = new Menus(this);

        playerManager.initPlayers();
        Bukkit.getLogger().info(String.format("%s V%s success enable", this.getDescription().getName(),
                this.getDescription().getVersion()));


    }

    /**
     * Called when plugin disables.
     */
    @Override
    public void onDisable() {
        // TODO Purge Pet Names. (and Treasure Chests bugged holograms).
        // TODO Use Metadatas for that!

        try {
            if (playerManager != null) {
                playerManager.dispose();
            }

            UltraCosmeticsData.get().getVersionManager().getModule().disable();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        Bukkit.getLogger().info(String.format("%s V%s success disable", this.getDescription().getName(),
                this.getDescription().getVersion()));
    }

    /**
     * Registers Listeners.
     */
    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new MainListener(), this);
        pluginManager.registerEvents(new EntitySpawningManager(), this);

        if (UltraCosmeticsData.get().getServerVersion().compareTo(ServerVersion.v1_9_R1) >= 0) {
            pluginManager.registerEvents(new PlayerSwapItemListener(this), this);
        }


    }
    /**
     * Gets the Custom Player Manager.
     *
     * @return the Custom Player Manager.
     */
    public CosmeticPlayerManager getPlayerManager() {
        return playerManager;
    }


    /**
     * @return The menus.
     */
    public Menus getMenus() {
        return menus;
    }

    public ArmorStandManager getArmorStandManager() {
        return armorStandManager;
    }

    public void openMainMenu(CosmeticPlayer cosmeticPlayer) {
//        if (getConfig().getBoolean("Categories.Back-To-Main-Menu-Custom-Command.Enabled")) {
//            String command = getConfig().getString("Categories.Back-To-Main-Menu-Custom-Command.Command").replace("/", "").replace("{player}", cosmeticPlayer.getBukkitPlayer().getName()).replace("{playeruuid}", cosmeticPlayer.getUuid().toString());
//            getServer().dispatchCommand(getServer().getConsoleSender(), command);
//        } else {
//            getMenus().getMainMenu().open(cosmeticPlayer);
//        }
    }

}
