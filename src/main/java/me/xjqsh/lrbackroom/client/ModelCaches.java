package me.xjqsh.lrbackroom.client;


import me.xjqsh.lrbackroom.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum ModelCaches {
    CUBE("block/object_7_light"),
    ;

    private final ResourceLocation rl;
    private IBakedModel cachedModel;
    ModelCaches(String rl){
        this.rl = new ResourceLocation("lrbr", rl);
    }
    @OnlyIn(Dist.CLIENT)
    public IBakedModel getModel() {
        if (cachedModel == null) {
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(rl);
            if (model == Minecraft.getInstance().getModelManager().getMissingModel()) {
                return model;
            }
            cachedModel = model;
        }
        return cachedModel;
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void register(ModelRegistryEvent event) {
        for (ModelCaches c : ModelCaches.values()) {
            ModelLoader.addSpecialModel(c.rl);
        }
        IResourceManager manager = Minecraft.getInstance().getResourceManager();
        ((SimpleReloadableResourceManager) manager).registerReloadListener(
                (ISelectiveResourceReloadListener) (resourceManager, resourcePredicate) -> {
                    for (ModelCaches c : ModelCaches.values()) {
                        c.cachedModel = null;
                    }
                }
        );
    }
}
