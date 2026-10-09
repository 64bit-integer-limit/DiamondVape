package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_1268;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_3675.class_307;

@Environment(EnvType.CLIENT)
public class ScaffoldMod implements ClientModInitializer {
   private class_310 client;
   private class_304 toggleKey;
   public static boolean enabled = false;

   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.toggleKey = new class_304("key.scaffold.toggle", class_307.field_1668, 72, "category.scaffold");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (this.toggleKey.method_1436()) {
            enabled = !enabled;
            if (client.field_1724 != null) {
               client.field_1705.method_1743().method_1812(class_2561.method_43470("[SCAFFOLD] " + (enabled ? "§aON" : "§cOFF")));
            }
         }

         if (enabled) {
            this.placeBlockUnderPlayer();
         }
      });
   }

   private void placeBlockUnderPlayer() {
      if (this.client.field_1724 != null && this.client.field_1687 != null && this.client.field_1761 != null) {
         class_2338 below = this.client.field_1724.method_24515().method_10074();
         if (this.client.field_1687.method_8320(below).method_26215()) {
            int slot = this.findBlockInHotbar();
            if (slot != -1) {
               this.client.field_1724.method_31548().field_7545 = slot;
               class_243 hitVec = class_243.method_24953(below);
               class_3965 hitResult = new class_3965(hitVec, class_2350.field_11036, below, false);
               this.client.field_1761.method_2896(this.client.field_1724, class_1268.field_5808, hitResult);
            }
         }
      }
   }

   private int findBlockInHotbar() {
      for (int i = 0; i < 9; i++) {
         class_1799 stack = this.client.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960() && stack.method_7909() instanceof class_1747 blockItem) {
            class_2248 block = blockItem.method_7711();
            if (!block.method_9564().method_26215()) {
               return i;
            }
         }
      }

      return -1;
   }
}
