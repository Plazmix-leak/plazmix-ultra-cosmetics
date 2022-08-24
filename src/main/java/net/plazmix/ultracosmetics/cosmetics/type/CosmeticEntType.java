package net.plazmix.ultracosmetics.cosmetics.type;

import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.ultracosmetics.util.UCMaterial;
import org.bukkit.entity.EntityType;

/**
 * Represents a Cosmetic Type with a Material, a Data, and an Entity Type.
 *
 * @author iSach
 * @since 08-04-2016
 */
public abstract class CosmeticEntType<T extends Cosmetic> extends CosmeticMatType<T> {

    private EntityType entityType;

    public CosmeticEntType(Category category, String signature, UCMaterial material, EntityType entityType, Class clazz, ServerVersion baseVersion, Boolean isEnable) {
        super(category, signature, material, clazz, baseVersion, isEnable);
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
