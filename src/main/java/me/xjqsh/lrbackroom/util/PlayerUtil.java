package me.xjqsh.lrbackroom.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerUtil {
    public static PlayerEntity getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

}
