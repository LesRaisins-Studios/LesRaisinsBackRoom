package me.xjqsh.lrbackroom.block;

import me.xjqsh.lrbackroom.common.data.MemBottleManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Object7Block extends Block implements IWaterLoggable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape shape;

    static {
        shape = Block.box(5, 0, 5, 11, 10, 11);
    }
    public Object7Block() {
        super(Properties.of(Material.GLASS).strength(0.2f).noOcclusion().lightLevel((blockState)->5));
        registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return shape;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new Object7TileEntity();
    }

    public FluidState getFluidState(BlockState p_204507_1_) {
        return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
    }


    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(WATERLOGGED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        IWorld iworld = ctx.getLevel();
        BlockPos blockpos = ctx.getClickedPos();
        boolean flag = iworld.getFluidState(blockpos).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, flag);
    }

    @Override
    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack stack) {
        if(!world.isClientSide()){
            Object7TileEntity tile = (Object7TileEntity) world.getBlockEntity(blockPos);
            if(tile!=null){
                if(stack.getTag()!=null && stack.getTag().contains("mem", Constants.NBT.TAG_STRING)){
                    tile.setMem(new ResourceLocation(stack.getOrCreateTag().getString("mem")));
                }else {
                    tile.setMem(MemBottleManager.getRandomMeta());
                }
            }
        }
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if(!world.isClientSide()) return ActionResultType.PASS;

        if(blockState.hasTileEntity() && world.getBlockEntity(blockPos) instanceof Object7TileEntity){
            Object7TileEntity tile = (Object7TileEntity) world.getBlockEntity(blockPos);
            if (tile != null) {
                tile.reset();
                tile.active = !tile.active;
            }
        }
        return ActionResultType.SUCCESS;
    }

    public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
        BlockPos blockpos = p_196260_3_.below();
        return canSupportRigidBlock(p_196260_2_, blockpos) || canSupportCenter(p_196260_2_, blockpos, Direction.UP);
    }

    @Override
    public List<ItemStack> getDrops(BlockState bs, LootContext.Builder ctx) {
        List<ItemStack> list = new ArrayList<>();
        TileEntity tileentity = ctx.getOptionalParameter(LootParameters.BLOCK_ENTITY);
        if (tileentity instanceof Object7TileEntity) {
            Object7TileEntity tile = (Object7TileEntity) tileentity;
            if(tile.getMem() != null){
                ItemStack stack = new ItemStack(this.asItem());
                stack.getOrCreateTag().putString("mem", tile.getMem().toString());

                list.add(stack);
            }
        }

        return list;
    }

}
