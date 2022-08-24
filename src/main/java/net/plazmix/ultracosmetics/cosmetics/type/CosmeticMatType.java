package net.plazmix.ultracosmetics.cosmetics.type;

import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.ultracosmetics.util.UCMaterial;

/**
 * A cosmetic material type.
 *
 * @author iSach
 * @since 08-04-2016
 */
public abstract class CosmeticMatType<T extends Cosmetic> extends CosmeticType<T> {
    private UCMaterial material;

    public CosmeticMatType(Category category, String signature, UCMaterial material, Class clazz, ServerVersion baseVersion, Boolean isEnable) {
        super(category, signature, clazz, baseVersion, isEnable);
        this.material = material;
    }

    public UCMaterial getMaterial() {
        return material;
    }
}
