package net.plazmix.ultracosmetics.menu.menus;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffectType;
import net.plazmix.ultracosmetics.menu.CosmeticMenu;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.menu.Menu;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * Particle Effect {@link Menu Menu}.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class MenuParticleEffects extends CosmeticMenu<ParticleEffectType> {

    public MenuParticleEffects(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.EFFECTS);
    }

    @Override
    protected void putItems(Inventory inventory, CosmeticPlayer cosmeticPlayer, int page) {
    }

    @Override
    public List<ParticleEffectType> enabled() {
        return ParticleEffectType.enabled();
    }

    @Override
    protected void toggleOn(CosmeticPlayer cosmeticPlayer, ParticleEffectType particleEffectType, UltraCosmetics ultraCosmetics) {
        particleEffectType.equip(cosmeticPlayer, ultraCosmetics);
    }

    @Override
    protected void toggleOff(CosmeticPlayer cosmeticPlayer) {
        cosmeticPlayer.removeParticleEffect();
    }

    @Override
    protected Cosmetic getCosmetic(CosmeticPlayer cosmeticPlayer) {
        return cosmeticPlayer.getCurrentParticleEffect();
    }
}
