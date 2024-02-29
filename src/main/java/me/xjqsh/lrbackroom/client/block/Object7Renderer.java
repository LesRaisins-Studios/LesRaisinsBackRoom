package me.xjqsh.lrbackroom.client.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.xjqsh.lrbackroom.block.Object7TileEntity;
import me.xjqsh.lrbackroom.client.ClientMemBottleManager;
import me.xjqsh.lrbackroom.client.ModelCaches;
import me.xjqsh.lrbackroom.common.data.block.Object7Data;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import net.minecraft.util.text.ITextComponent;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Object7Renderer extends TileEntityRenderer<Object7TileEntity> {
    private static final RenderType SOLID_L = RenderType.create("solid_light",
            DefaultVertexFormats.BLOCK, 7, 131072, true, false, RenderType.State.builder()
                    .setShadeModelState(new RenderState.ShadeModelState(false))
                    .setLightmapState(new RenderState.LightmapState(true))
                    .setTextureState(new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS, false, false))
                    .setAlphaState(new RenderState.AlphaState(0.5F))
                    .createCompositeState(true));
    public Object7Renderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(Object7TileEntity tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay) {
        if(tile.getMem() ==null) return;
        Object7Data data = ClientMemBottleManager.getMeta(tile.getMem());
        if(data==null) return;

        float f = (float)tile.time + partialTicks;
        Color color = new Color(data.getColor()).brighter();

        if(tile.active){
            stack.pushPose();
            {
                stack.translate(0.5,1.2,0.5);

                float f1 = tile.rot - tile.oRot;
                f1 = f1 % (float)Math.PI;
                if (f1 < -(float)Math.PI) {
                    f1 += (float)Math.PI * 2F;
                }

                float f2 = tile.oRot + f1 * partialTicks;
                stack.mulPose(Vector3f.YP.rotation(-f2));

                FontRenderer font = Minecraft.getInstance().font;

                for(Object7TileEntity.TextLine textLine : tile.lines){
                    stack.pushPose();
                    {
                        float x = (float) textLine.tick + partialTicks;

                        stack.mulPose(Vector3f.XP.rotationDegrees(textLine.rot));
                        stack.translate(0,0.0025*x,0);
                        stack.scale(0.015f,0.015f,0.015f);

                        stack.mulPose(Vector3f.YP.rotationDegrees(90));
                        stack.mulPose(Vector3f.XP.rotationDegrees(180));

                        int c = color.getRGB() & 0x00FFFFFF | ( textLine.getAlpha() << 24 );
                        ITextComponent text = data.getLine(textLine.line);

                        font.draw(stack,text,-font.width(text)/2f + textLine.xOffset,0f, c);
                    }
                    stack.popPose();
                }

            }
            stack.popPose();
        }



        IBakedModel model = ModelCaches.CUBE.getModel();
        if(model!=null){
            IVertexBuilder builder;
            builder = ItemRenderer.getFoilBufferDirect(buffer, SOLID_L, true, false);

            stack.pushPose();
            {
                float s1 = 0.90F + MathHelper.sin(f * 0.05F) * 0.01F;
                stack.scale(s1, s1,s1);
                stack.translate(0.05, 0.05, 0.05);
                renderQuads(stack, builder, model, color);
            }
            stack.popPose();

        }

    }

    private static void renderQuads(MatrixStack stack, IVertexBuilder builder, IBakedModel model, Color color) {
        Random random = new Random();
        Direction[] var9 = Direction.values();

        for (Direction direction : var9) {
            random.setSeed(42L);
            renderQuads(stack, builder, model.getQuads(null, direction, random),
                    15728880, OverlayTexture.NO_OVERLAY, color.getRGB());
        }

        random.setSeed(42L);
        renderQuads(stack, builder, model.getQuads(null, null, random),
                15728880, OverlayTexture.NO_OVERLAY, color.getRGB());
    }

    private static void renderQuads(MatrixStack matrixStack, IVertexBuilder buffer, List<BakedQuad> quads, int light, int overlay, int color) {
        MatrixStack.Entry entry = matrixStack.last();

        for (BakedQuad quad : quads) {
            float alpha = 1.0F;

            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;

            float x = Math.max(red,Math.max(green,blue));

            buffer.addVertexData(entry, quad, red/x, green/x, blue/x, alpha, light, overlay, false);
        }

    }
}
