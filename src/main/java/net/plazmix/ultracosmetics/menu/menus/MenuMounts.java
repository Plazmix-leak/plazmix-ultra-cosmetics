package net.plazmix.ultracosmetics.menu.menus;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.elements.mounts.MountType;
import net.plazmix.ultracosmetics.menu.CosmeticMenu;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.menu.Menu;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * Mount {@link Menu Menu}.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class MenuMounts extends CosmeticMenu<MountType> {

    public MenuMounts(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.MOUNTS);
    }

    @Override
    protected void putItems(Inventory inventory, CosmeticPlayer cosmeticPlayer, int page) {
    }

    @Override
    public List<MountType> enabled() {
        return MountType.enabled();
    }

    @Override
    protected void toggleOn(CosmeticPlayer cosmeticPlayer, MountType mountType, UltraCosmetics ultraCosmetics) {
        mountType.equip(cosmeticPlayer, ultraCosmetics);
    }

    @Override
    protected void toggleOff(CosmeticPlayer cosmeticPlayer) {
        if (cosmeticPlayer.getCurrentMount() == null) {
            return;
        }
        cosmeticPlayer.getCurrentMount().setBeingRemoved(true);
        cosmeticPlayer.removeMount();
    }

    @Override
    protected String getTypeName(MountType cosmeticType, CosmeticPlayer cosmeticPlayer) {
        return cosmeticType.getMenuName();
    }

    @Override
    protected Cosmetic getCosmetic(CosmeticPlayer cosmeticPlayer) {
        return cosmeticPlayer.getCurrentMount();
    }
}
