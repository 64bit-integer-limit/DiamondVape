package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_3675.class_307;

public class MovementMod implements ClientModInitializer {
   public static boolean enabled = false;
   private class_304 toggleKey;
   private final float speedMultiplier = 1.6F;
   private final float jumpHeight = 0.8F;

   public void onInitializeClient() {
      this.toggleKey = new class_304("key.hellish.movement", class_307.field_1668, 86, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK
         .register(
            (EndTick)client -> {
               if (client.field_1724 != null) {
                  while (this.toggleKey.method_1436()) {
                     enabled = !enabled;
                     client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] Movement " + (enabled ? "§aON" : "§cOFF")), false);
                  }

                  if (enabled) {
                     if (client.field_1724.method_24828()
                        && !client.field_1724.method_5715()
                        && (client.field_1724.field_6250 != 0.0F || client.field_1724.field_6212 != 0.0F)) {
                        float strength = 0.0352F;
                        client.field_1724.method_5724(strength, new class_243(client.field_1724.field_6212, 0.0, client.field_1724.field_6250));
                     }

                     if (client.field_1690.field_1903.method_1434() && client.field_1724.method_24828()) {
                        class_243 vel = client.field_1724.method_18798();
                        client.field_1724.method_18800(vel.field_1352, 0.8F, vel.field_1350);
                     }
                  }
               }
            }
         );
   }
}
