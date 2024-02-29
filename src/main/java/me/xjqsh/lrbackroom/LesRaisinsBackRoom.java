package me.xjqsh.lrbackroom;

import me.xjqsh.lrbackroom.client.ClientMemBottleManager;
import me.xjqsh.lrbackroom.client.block.Object7Renderer;
import me.xjqsh.lrbackroom.init.ModBlocks;
import me.xjqsh.lrbackroom.init.ModItems;
import me.xjqsh.lrbackroom.network.PacketHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("lrbr")
public class LesRaisinsBackRoom {
    public static final Logger LOGGER = LogManager.getLogger();

    public LesRaisinsBackRoom() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::onClientSetup);
        bus.addListener(this::onCommonSetup);

        ModBlocks.REGISTER.register(bus);
        ModBlocks.TILE.register(bus);
        ModItems.REGISTER.register(bus);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModBlocks.obj7Block.get(), RenderType.cutout());
            ClientRegistry.bindTileEntityRenderer(ModBlocks.obj7tile.get(), Object7Renderer::new);
            ClientMemBottleManager.init();
        });
    }

    private void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(PacketHandler::init);
    }
}
