package me.xjqsh.lrbackroom.init;


import me.xjqsh.lrbackroom.Reference;
import me.xjqsh.lrbackroom.block.Object7Block;
import me.xjqsh.lrbackroom.block.Object7TileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
     public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
     public static final RegistryObject<Object7Block> obj7Block = REGISTER.register("object_7", Object7Block::new);


     public static final DeferredRegister<TileEntityType<?>> TILE = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

     public static final RegistryObject<TileEntityType<Object7TileEntity>> obj7tile = TILE.register("object_7",
             ()-> TileEntityType.Builder.of(Object7TileEntity::new, obj7Block.get()).build(null));

}
