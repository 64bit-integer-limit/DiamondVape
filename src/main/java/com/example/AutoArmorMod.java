package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_1304;
import net.minecraft.class_1738;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_1304.class_1305;
import net.minecraft.class_3675.class_307;

@Environment(EnvType.CLIENT)
public class AutoArmorMod implements ClientModInitializer {
   private class_310 client;
   private class_304 toggleKey;
   public static boolean enabled = false;

   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.toggleKey = new class_304("key.autoarmor.toggle", class_307.field_1668, 82, "category.autoarmor");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (this.toggleKey.method_1436()) {
            enabled = !enabled;
            if (client.field_1724 != null) {
               client.field_1724.method_7353(class_2561.method_43470("[AutoArmor] " + (enabled ? "§aEnabled" : "§cDisabled")), true);
            }
         }

         if (enabled) {
            this.autoEquipArmor();
         }
      });
   }

   public static boolean isEnabled() {
      return enabled;
   }

   private void autoEquipArmor() {
      if (this.client.field_1724 != null) {
         for (class_1304 slot : class_1304.values()) {
            if (slot.method_5925() == class_1305.field_6178) {
               class_1799 equipped = this.client.field_1724.method_6118(slot);
               if (equipped.method_7960()) {
                  int bestSlot = this.findBestArmor(slot);
                  if (bestSlot != -1) {
                     class_1799 stack = this.client.field_1724.method_31548().method_5438(bestSlot).method_7972();
                     this.client.field_1724.method_31548().field_7548.set(slot.method_5927(), stack);
                     this.client.field_1724.method_31548().method_5434(bestSlot, 1);
                  }
               }
            }
         }
      }
   }

   private int findBestArmor(class_1304 slot) {
      int bestSlot = -1;
      int bestProtection = -1;

      for (int i = 0; i < 36; i++) {
         class_1799 stack = this.client.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960() && stack.method_7909() instanceof class_1738 armor && armor.method_7685() == slot) {
            int prot = armor.method_7687();
            if (prot > bestProtection) {
               bestProtection = prot;
               bestSlot = i;
            }
         }
      }

      return bestSlot;
   }
}
