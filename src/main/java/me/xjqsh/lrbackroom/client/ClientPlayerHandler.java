package me.xjqsh.lrbackroom.client;

import me.xjqsh.lrbackroom.common.data.block.Object7Data;
import me.xjqsh.lrbackroom.init.ModItems;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientPlayerHandler {
    @SubscribeEvent
    public static void onItemColorsInit(ColorHandlerEvent.Item e) {
        e.getItemColors().register((stack, color) -> {
            if(stack.getTag()!=null){
                CompoundNBT nbt = stack.getTag();
                if(nbt.contains("mem", Constants.NBT.TAG_STRING)){
                    ResourceLocation rl = ResourceLocation.tryParse(nbt.getString("mem"));
                    if(rl!=null){
                        Object7Data data = ClientMemBottleManager.getMeta(rl);
                        if(data!=null){
                            return data.getColor();
                        }
                    }
                }
            }
            return -1;
        }, ModItems.obj7Block.get());
    }
}
