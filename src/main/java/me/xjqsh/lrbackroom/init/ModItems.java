package me.xjqsh.lrbackroom.init;

import me.xjqsh.lrbackroom.Reference;
import me.xjqsh.lrbackroom.item.Object1Item;
import me.xjqsh.lrbackroom.item.Object7Item;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final ItemGroup LR_BACKROOM = new ItemGroup(Reference.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.obj7Block.get());
        }
    };
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final RegistryObject<Object7Item> obj7Block = REGISTER.register("object_7",
            ()->new Object7Item(ModBlocks.obj7Block.get(), new Item.Properties().tab(LR_BACKROOM)));
    public static final RegistryObject<Item> obj1 = REGISTER.register("object_1",
            ()->new Object1Item((new Item.Properties()).tab(LR_BACKROOM).food(ModFoods.obj1)));
    public static final RegistryObject<Item> obj5_i = REGISTER.register("object_5_bullet",
            ()->new Item((new Item.Properties()).tab(LR_BACKROOM).food(ModFoods.obj5_i)));
    public static final RegistryObject<Item> obj5_ii = REGISTER.register("object_5_mint",
            ()->new Item((new Item.Properties()).tab(LR_BACKROOM).food(ModFoods.obj5_ii)));

}
