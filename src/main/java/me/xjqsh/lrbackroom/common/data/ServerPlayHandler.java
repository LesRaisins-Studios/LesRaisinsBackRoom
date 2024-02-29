package me.xjqsh.lrbackroom.common.data;


import me.xjqsh.lrbackroom.common.data.block.Object7Data;
import me.xjqsh.lrbackroom.network.PacketHandler;
import me.xjqsh.lrbackroom.network.message.SCustomMeta;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Map;

@Mod.EventBusSubscriber
public class ServerPlayHandler {
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent e) {
        if (e.getPlayer() == null) {
            PacketHandler.getPlayChannel().send(PacketDistributor.ALL.noArg(),
                    new SCustomMeta(MemBottleManager.getInstance().getMetaMap()));
            return;
        }
        Map<ResourceLocation, Object7Data> m = MemBottleManager.getInstance().getMetaMap();

        PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(e::getPlayer),
                new SCustomMeta(m));
    }


}
