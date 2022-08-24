package net.plazmix.ultracosmetics.cosmetics;

import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.type.CosmeticType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;


public abstract class Cosmetic<T extends CosmeticType> extends BukkitRunnable implements Listener {
    private CosmeticPlayer owner;
    private Category category;
    private UltraCosmetics ultraCosmetics;
    protected boolean equipped;
    private T cosmeticType;

    public Cosmetic(UltraCosmetics ultraCosmetics, Category category, CosmeticPlayer owner, T type) {
        this.owner = owner;
        this.category = category;
        this.ultraCosmetics = ultraCosmetics;
        this.cosmeticType = type;
    }

    public void equip() {
        if (!owner.hasAccessCosmetic(cosmeticType) && PlazmixCoreApi.GROUP_API.isDonate(owner.getBukkitPlayer().getName())) {
            owner.getPlazmixUser().localization().sendMessage("NO_PERM");
            return;
        }

        ultraCosmetics.getServer().getPluginManager().registerEvents(this, ultraCosmetics);

        this.equipped = true;
        onEquip();
    }

    public void clear() {
        owner.getPlazmixUser().localization().sendMessage("COSMETIC_CLEAR");
        HandlerList.unregisterAll(this);

        try {
            cancel();
        } catch (Exception ignored) {
        }

        onClear();
        // ВТФ ?
//        owner = null;
    }

    @Override
    public void run() {
    }

    protected abstract void onEquip();

    protected abstract void onClear();

    public final CosmeticPlayer getOwner() {
        return owner;
    }

    public final UltraCosmetics getUltraCosmetics() {
        return ultraCosmetics;
    }

    public final Category getCategory() {
        return category;
    }

    public final CosmeticPlayer getPlayer() {
        if (owner == null) {
            return null;
        }
        return owner;
    }

    public boolean isEquipped() {
        return equipped;
    }


    public T getType() {
        return cosmeticType;
    }

    protected String getTypeSignature() {
        return getType().getSignature();
    }
}