package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_465;
import net.minecraft.class_3675.class_307;

public class CheststealerMod implements ClientModInitializer {
   public static boolean enabled = false;
   public static int itemsPerTick = 1;
   private class_304 toggleKey;
   private class_310 client;

   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.toggleKey = new class_304("key.hellish.cheststealer", class_307.field_1668, 67, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (this.toggleKey.method_1436()) {
            enabled = !enabled;
            client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] ChestStealer " + (enabled ? "§aON" : "§cOFF")), false);
         }

         if (enabled && client.field_1724 != null) {
            if (client.field_1755 instanceof class_465<?> screen && screen.method_17577() instanceof class_1707 handler) {
               int chestSize = handler.method_7629().method_5439();

               for (int count = 0; count < itemsPerTick; count++) {
                  for (int i = 0; i < chestSize; i++) {
                     if (handler.method_7611(i).method_7681()) {
                        client.field_1761.method_2906(handler.field_7763, i, 0, class_1713.field_7794, client.field_1724);
                        break;
                     }
                  }
               }
            }
         }
      });
   }
}
