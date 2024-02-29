package me.xjqsh.lrbackroom.block;

import me.xjqsh.lrbackroom.client.ClientMemBottleManager;
import me.xjqsh.lrbackroom.common.data.MemBottleManager;
import me.xjqsh.lrbackroom.common.data.block.Object7Data;
import me.xjqsh.lrbackroom.init.ModBlocks;
import me.xjqsh.lrbackroom.util.PlayerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Object7TileEntity extends TileEntity implements ITickableTileEntity {
    public static class TextLine{

        public float xOffset;
        public int rot;
        public int tick;
        public int line;
        private void tick(){
            tick++;
        }
        public int getAlpha(){
            if(tick<30){
                return (int) (tick*255f/40f);
            }else if(tick>170){
                return Math.max(0, 255 - (int) ((tick-170)*255f/40f));
            }
            return 255;
        }
    }
    private static final Random random = new Random();
    public int time;
    public float rot;
    public float oRot;
    public float tRot;
    public boolean active = false;
    private ResourceLocation mem;
    public Queue<TextLine> lines = new ArrayDeque<>();

    public Object7TileEntity() {
        super(ModBlocks.obj7tile.get());
    }
    public ResourceLocation getMem() {
        return mem;
    }

    public void setMem(ResourceLocation mem) {
        this.mem = mem;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        if(getMem() !=null){
            nbt.putString("mem", getMem().toString());
        }
        return super.save(nbt);
    }

    @Override
    public void load(BlockState bs, CompoundNBT nbt) {
        if(nbt.contains("mem", Constants.NBT.TAG_STRING)){
            setMem(new ResourceLocation(nbt.getString("mem")));
        }
        super.load(bs, nbt);
    }

    public void reset(){
        time = 0;
        lines.clear();
    }

    @OnlyIn(Dist.CLIENT)
    public void release(){
        if(getMem() != null){
            Object7Data data = MemBottleManager.getMeta(getMem());
            if(data!=null && getPage()<data.getLineCount()){
                TextLine line = new TextLine();
                line.xOffset = random.nextFloat()*80 - 40;
                line.rot = random.nextInt(30)-15;
                line.line = getPage();
                lines.add(line);
            }
        }
    }
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (level != null) {
            handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());
        }
    }


    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        if(getMem() !=null){
            nbt.putString("mem", getMem().toString());
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        if(nbt.contains("mem", Constants.NBT.TAG_STRING)){
            setMem(new ResourceLocation(nbt.getString("mem")));
        }
    }

    @Override
    public void tick() {
        if (level == null || !level.isClientSide() || getMem() ==null) return;

        if(active) {
            Object7Data data = ClientMemBottleManager.getMeta(getMem());
            if(data==null)return;

            this.oRot = this.rot;
            PlayerEntity playerentity = PlayerUtil.getLocalPlayer();

            if(playerentity != null && playerentity.distanceToSqr((double)this.worldPosition.getX() + 0.5D,
                    (double)this.worldPosition.getY() + 0.5D,
                    (double)this.worldPosition.getZ() + 0.5D) < 64.0D) {
                double d0 = playerentity.getX() - ((double)this.worldPosition.getX() + 0.5D);
                double d1 = playerentity.getZ() - ((double)this.worldPosition.getZ() + 0.5D);
                this.tRot = (float) MathHelper.atan2(d1, d0);
            } else {
                this.active = false;
            }

            float f2 = calcRot();

            this.rot += f2 * 0.4F;



            if(time >= data.getLineCount() * 80 + 115){
                this.active = false;
            }else {
                if(time%80 == 0){
                    release();
                }

                for(TextLine line : lines){
                    line.tick();
                }

                while (!lines.isEmpty() && lines.peek().tick>195){
                    lines.remove();
                }

                this.time++;
            }

        }
    }


    private int getPage(){
        return time/80;
    }

    private float calcRot() {
        while(this.rot >= (float)Math.PI) {
            this.rot -= ((float)Math.PI * 2F);
        }

        while(this.rot < -(float)Math.PI) {
            this.rot += ((float)Math.PI * 2F);
        }

        while(this.tRot >= (float)Math.PI) {
            this.tRot -= ((float)Math.PI * 2F);
        }

        while(this.tRot < -(float)Math.PI) {
            this.tRot += ((float)Math.PI * 2F);
        }

        float f2;
        for(f2 = this.tRot - this.rot; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {
        }

        while(f2 < -(float)Math.PI) {
            f2 += ((float)Math.PI * 2F);
        }
        return f2;
    }

}
