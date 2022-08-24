package net.plazmix.ultracosmetics.version;

import net.plazmix.ultracosmetics.cosmetics.elements.mounts.Mount;
import org.bukkit.entity.EntityType;

public interface IMounts {
    Class<? extends Mount> getSpiderClass();

    Class<? extends Mount> getSlimeClass();

    Class<? extends Mount> getHorrorClass();

    Class<? extends Mount> getWalkingDeadClass();

    Class<? extends Mount> getRudolphClass();

    EntityType getHorrorType();

    EntityType getWalkingDeadType();

    EntityType getRudolphType();
}
