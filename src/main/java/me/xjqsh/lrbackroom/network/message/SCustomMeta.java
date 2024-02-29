package me.xjqsh.lrbackroom.network.message;

import me.xjqsh.lrbackroom.client.ClientMemBottleManager;
import me.xjqsh.lrbackroom.common.data.block.Object7Data;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SCustomMeta {
    private Map<ResourceLocation, Object7Data> metaMap;
    public SCustomMeta(Map<ResourceLocation, Object7Data> metaMap){
        this.metaMap = metaMap;
    }
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(metaMap.size());
        for(Map.Entry<ResourceLocation, Object7Data> e : metaMap.entrySet()){
            e.getValue().writeBuffer(buffer);
        }
    }
    public static SCustomMeta decode(PacketBuffer buffer) {
        int length = buffer.readInt();
        Map<ResourceLocation, Object7Data> map = new HashMap<>();
        for (int i = 0; i < length; i++) {
            Object7Data data = new Object7Data();
            data.readBuffer(buffer);

            map.put(data.getId(),data);
        }
        return new SCustomMeta(map);
    }

    public static void handle(SCustomMeta msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(()-> {
            ClientMemBottleManager.getInstance().handleCustomMeta(msg);
        });

        context.setPacketHandled(true);
    }

    public Map<ResourceLocation, Object7Data> getMetaMap() {
        return metaMap;
    }
}
