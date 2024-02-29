package me.xjqsh.lrbackroom.init;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoods {
    public static Food obj1= new Food.Builder().nutrition(4).saturationMod(0.4f)
                            .effect(()->new EffectInstance(Effects.REGENERATION,100),1.0f).build();
    public static Food obj5_i= new Food.Builder().nutrition(1).saturationMod(0.2f)
            .effect(()->new EffectInstance(Effects.DAMAGE_RESISTANCE,600),1.0f).build();
    public static Food obj5_ii= new Food.Builder().nutrition(4).saturationMod(0.6f).build();
}
