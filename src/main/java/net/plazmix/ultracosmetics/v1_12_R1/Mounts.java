package net.plazmix.ultracosmetics.v1_12_R1;

import net.plazmix.ultracosmetics.cosmetics.elements.mounts.Mount;
import net.plazmix.ultracosmetics.v1_12_R1.mount.*;
import net.plazmix.ultracosmetics.version.IMounts;
import org.bukkit.entity.EntityType;

/**
 * @author RadBuilder
 */
public class Mounts implements IMounts {
    @Override
    public Class<? extends Mount> getSpiderClass() {
        return MountSpider.class;
    }

    @Override
    public Class<? extends Mount> getSlimeClass() {
        return MountSlime.class;
    }

    @Override
    public Class<? extends Mount> getHorrorClass() {
        return MountInfernalHorror.class;
    }

    @Override
    public Class<? extends Mount> getWalkingDeadClass() {
        return MountWalkingDead.class;
    }

    @Override
    public Class<? extends Mount> getRudolphClass() {
        return MountRudolph.class;
    }

    @Override
    public EntityType getHorrorType() {
        return EntityType.SKELETON_HORSE;
    }

    @Override
    public EntityType getWalkingDeadType() {
        return EntityType.ZOMBIE_HORSE;
    }

    @Override
    public EntityType getRudolphType() {
        return EntityType.MULE;
    }
}
