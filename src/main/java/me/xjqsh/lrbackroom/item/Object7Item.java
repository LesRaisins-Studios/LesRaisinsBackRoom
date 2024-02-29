package me.xjqsh.lrbackroom.item;


import me.xjqsh.lrbackroom.client.ClientMemBottleManager;
import me.xjqsh.lrbackroom.common.data.MemBottleManager;
import me.xjqsh.lrbackroom.common.data.block.Object7Data;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class Object7Item extends BlockItem{
    public Object7Item(Block block, Properties properties) {
        super(block, properties);
    }

    public String getDescriptionId(ItemStack stack) {
        if(stack.getTag()!=null){
            CompoundNBT nbt = stack.getTag();
            if(nbt.contains("mem", Constants.NBT.TAG_STRING)){
                ResourceLocation rl = ResourceLocation.tryParse(nbt.getString("mem"));
                if(rl!=null){
                    Object7Data data = MemBottleManager.getMeta(rl);
                    if(data!=null){
                        return data.getDescriptionId();
                    }
                }
            }
        }
        return super.getDescriptionId();
    }

    public static final ITextComponent EMPTY = new TranslationTextComponent("tooltip.lrbr.object7.empty").withStyle(TextFormatting.GRAY);
    public static final ITextComponent FULL = new TranslationTextComponent("tooltip.lrbr.object7.full").withStyle(TextFormatting.GRAY);

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        list.add( stack.getTag()!=null && stack.getTag().contains("mem", Constants.NBT.TAG_STRING) ? FULL : EMPTY );

        super.appendHoverText(stack, world, list, flag);
    }
}
