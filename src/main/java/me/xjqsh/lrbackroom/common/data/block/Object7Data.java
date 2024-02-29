package me.xjqsh.lrbackroom.common.data.block;

import me.xjqsh.lrbackroom.common.data.IMeta;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class Object7Data implements IMeta {
    private ResourceLocation id;
    private String descriptionId = "block.lrbr.object7";
    private int color = -1;
    private List<String> texts = new ArrayList<>();

    public int getColor() {
        return color;
    }

    public List<String> getTexts() {
        return texts;
    }
    public int getLineCount(){
        return texts.size();
    }

    public ResourceLocation getId() {
        return id;
    }

    public Object7Data setId(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public ITextComponent getLine(int i){
        if(i >= texts.size() || i < 0) {
            return new StringTextComponent("error");
        }
        return new TranslationTextComponent(texts.get(i));
    }

    @Override
    public void writeBuffer(PacketBuffer buffer) {
        buffer.writeResourceLocation(id);
        buffer.writeInt(color);
        buffer.writeUtf(descriptionId);
        buffer.writeInt(texts.size());
        for(String s : texts){
            buffer.writeUtf(s);
        }
    }

    @Override
    public void readBuffer(PacketBuffer buffer) {
        this.id = buffer.readResourceLocation();
        this.color = buffer.readInt();
        this.descriptionId = buffer.readUtf();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            texts.add(buffer.readUtf());
        }
    }
}
