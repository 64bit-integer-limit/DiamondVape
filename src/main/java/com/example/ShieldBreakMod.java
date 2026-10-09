package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_1309;
import net.minecraft.class_1743;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3966;
import net.minecraft.class_3675.class_307;

public class ShieldBreakMod implements ClientModInitializer {
   public static boolean enabled = false;
   private class_304 toggleKey;

   public void onInitializeClient() {
      this.toggleKey = new class_304("key.hellish.shieldbreak", class_307.field_1668, 85, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK
         .register(
            (EndTick)client -> {
               if (client.field_1724 != null && client.field_1687 != null) {
                  while (this.toggleKey.method_1436()) {
                     enabled = !enabled;
                     client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] ShieldBreak " + (enabled ? "§aON" : "§cOFF")), false);
                  }

                  if (enabled
                     && client.field_1755 == null
                     && client.field_1765 instanceof class_3966 entityHit
                     && entityHit.method_17782() instanceof class_1309 target
                     && target.method_6039()) {
                     this.switchToAx(client);
                  }
               }
            }
         );
   }

   private void switchToAx(class_310 client) {
      for (int i = 0; i < 9; i++) {
         class_1799 stack = client.field_1724.method_31548().method_5438(i);
         if (stack.method_7909() instanceof class_1743) {
            if (client.field_1724.method_31548().field_7545 != i) {
               client.field_1724.method_31548().field_7545 = i;
            }
            break;
         }
      }
   }
}
