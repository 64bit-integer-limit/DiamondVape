package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_3675.class_307;

public class MyCheatMod implements ClientModInitializer {
   public static boolean enabled = false;
   private static double lastGamma = 1.0;
   private class_304 toggleKey;

   public void onInitializeClient() {
      this.toggleKey = new class_304("key.hellish.fullbright", class_307.field_1668, 66, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK
         .register(
            (EndTick)client -> {
               if (client.field_1724 != null && client.field_1690 != null) {
                  for (;
                     this.toggleKey.method_1436();
                     client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] Fullbright " + (enabled ? "§aON" : "§cOFF")), false)
                  ) {
                     enabled = !enabled;
                     if (enabled) {
                        lastGamma = (Double)client.field_1690.method_42473().method_41753();
                        client.field_1690.method_42473().method_41748(1.0);
                     } else {
                        client.field_1690.method_42473().method_41748(lastGamma);
                     }
                  }

                  if (enabled && (Double)client.field_1690.method_42473().method_41753() < 1.0) {
                     client.field_1690.method_42473().method_41748(1.0);
                  }
               }
            }
         );
   }
}
