package me.xjqsh.lrbackroom.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class Object1Item extends Item {
   public Object1Item(Properties itemProperties) {
      super(itemProperties);
   }

   public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
      if (!world.isClientSide) {
         entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
         entity.curePotionEffects(stack);
      }

      if (entity instanceof ServerPlayerEntity) {
         ServerPlayerEntity serverPlayer = (ServerPlayerEntity)entity;
         serverPlayer.eat(world, stack);
      }

      return stack;
   }

   public int getUseDuration(ItemStack itemStack) {
      return 32;
   }

   public UseAction getUseAnimation(ItemStack itemStack) {
      return UseAction.DRINK;
   }

   public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
      return DrinkHelper.useDrink(world, player, hand);
   }

   @Override
   public SoundEvent getEatingSound() {
      return SoundEvents.GENERIC_DRINK;
   }
}
