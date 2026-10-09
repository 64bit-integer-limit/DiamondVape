package com.example;

import java.util.Random;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_1268;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675.class_307;

public class AutoClickerMod implements ClientModInitializer {
   public static boolean enabled = false;
   private class_304 holdKey;
   private class_304 toggleKey;
   private final Random random = new Random();
   private final double minCPS = 8.0;
   private final double maxCPS = 14.0;
   private long lastClickTime = 0L;
   private long nextDelay = 0L;

   public void onInitializeClient() {
      this.toggleKey = new class_304("key.hellish.autoclicker_toggle", class_307.field_1668, 74, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      this.holdKey = new class_304("key.hellish.autoclicker_hold", class_307.field_1668, 342, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.holdKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (client.field_1724 != null) {
            while (this.toggleKey.method_1436()) {
               enabled = !enabled;
               client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] AutoClicker " + (enabled ? "§aON" : "§cOFF")), false);
            }

            if (enabled && client.field_1755 == null) {
               boolean isMining = client.field_1690.field_1886.method_1434();
               if (this.holdKey.method_1434() && !isMining) {
                  long currentTime = System.currentTimeMillis();
                  if (currentTime - this.lastClickTime >= this.nextDelay) {
                     this.doClick(client);
                     this.lastClickTime = currentTime;
                     this.nextDelay = this.calculateNextDelay();
                  }
               }
            }
         }
      });
   }

   private void doClick(class_310 client) {
      if (client.field_1761 != null && client.field_1724 != null) {
         client.execute(() -> {
            if (client.field_1765 != null) {
               client.field_1724.method_6104(class_1268.field_5808);
            }
         });
      }
   }

   private long calculateNextDelay() {
      double randomCPS = 8.0 + 6.0 * this.random.nextDouble();
      return (long)(1000.0 / randomCPS);
   }
}
