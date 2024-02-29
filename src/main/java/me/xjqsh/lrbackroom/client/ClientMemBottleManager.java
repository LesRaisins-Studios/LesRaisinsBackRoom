package me.xjqsh.lrbackroom.client;

import com.google.common.collect.ImmutableMap;
import me.xjqsh.lrbackroom.common.data.block.Object7Data;
import me.xjqsh.lrbackroom.network.message.SCustomMeta;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ClientMemBottleManager {
    private static ClientMemBottleManager instance;
    public static ClientMemBottleManager getInstance() {
        return instance;
    }
    public static void init(){
        instance = new ClientMemBottleManager();
    }
    private Map<ResourceLocation, Object7Data> metaMap;
    public void handleCustomMeta(SCustomMeta msg) {
        ImmutableMap.Builder<ResourceLocation, Object7Data> builder = ImmutableMap.builder();
        for(Map.Entry<ResourceLocation, Object7Data> e : msg.getMetaMap().entrySet()){
            builder.put(e.getKey(), e.getValue());
        }
        this.metaMap = builder.build();
    }
    public static Object7Data getMeta(ResourceLocation rl) {
        if(getInstance()!=null) {
            return getInstance().metaMap.get(rl);
        }
        return null;
    }
}
