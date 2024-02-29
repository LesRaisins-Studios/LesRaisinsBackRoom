package me.xjqsh.lrbackroom.common.data;

import com.google.common.collect.Maps;

import com.google.gson.*;
import me.xjqsh.lrbackroom.LesRaisinsBackRoom;
import me.xjqsh.lrbackroom.Reference;
import me.xjqsh.lrbackroom.client.ClientMemBottleManager;
import me.xjqsh.lrbackroom.common.data.block.Object7Data;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.util.*;


@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class MemBottleManager extends JsonReloadListener {
    public static final JsonDeserializer<ResourceLocation> RESOURCE_LOCATION = (json, typeOfT, context) -> new ResourceLocation(json.getAsString());
    public static final Gson GSON = Util.make(() -> {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ResourceLocation.class,RESOURCE_LOCATION);
        return builder.create();
    });
    private static final Random random = new Random();
    private MemBottleManager(){
        super(GSON, "mem_bottle");
    }
    private Map<ResourceLocation, Object7Data> metaMap = new HashMap<>();
    public List<ResourceLocation> metaList = new ArrayList<>();
    private static MemBottleManager INSTANCE;
    public static MemBottleManager getInstance() {
        return INSTANCE;
    }
    public Map<ResourceLocation, Object7Data> getMetaMap() {
        return metaMap;
    }
    public static Object7Data getMeta(ResourceLocation rl) {
        if(getInstance() != null){
            return INSTANCE.metaMap.get(rl);
        }
        return ClientMemBottleManager.getMeta(rl);
    }
    public static ResourceLocation getRandomMeta() {
        if(getInstance() != null && !INSTANCE.metaList.isEmpty()){
            return INSTANCE.metaList.get(random.nextInt(INSTANCE.metaList.size()));
        }
        return null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, IResourceManager resourceManager, IProfiler profiler) {
        Map<ResourceLocation, Object7Data> meta = Maps.newHashMap();
        map.forEach(((rl, jsonElement) -> {
            try {
                Object7Data data = GSON.fromJson(jsonElement, Object7Data.class);
                if(data != null) {
                    data.setId(rl);
                    meta.put(rl, data);
                    metaList.add(rl);
                }
            } catch (IllegalArgumentException | JsonParseException e) {
                LesRaisinsBackRoom.LOGGER.error("Parsing error loading custom memory bottle {}: {}", rl, e.getMessage());
            }
        }));
        this.metaMap = meta;
        LesRaisinsBackRoom.LOGGER.info("Loaded data for memory bottle from {} files", meta.size());
    }

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        MemBottleManager manager = new MemBottleManager();
        event.addListener(manager);
        MemBottleManager.INSTANCE = manager;
    }

    @SubscribeEvent
    public static void onClientDisconnect(FMLServerStoppedEvent event){
        INSTANCE = null;
    }
}
