package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675.class_307;

@Environment(EnvType.CLIENT)
public class VelocityMod implements ClientModInitializer {
   public static boolean enabled = false;
   private class_304 toggleKey;
   private class_310 client;
   private float velocityMultiplier = 0.3F;

   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.toggleKey = new class_304("key.hellish.velocity", class_307.field_1668, 86, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (this.toggleKey.method_1436()) {
            enabled = !enabled;
            if (client.field_1724 != null) {
               client.field_1724.method_7353(class_2561.method_43470("[VelocityMod] " + (enabled ? "§aEnabled" : "§cDisabled")), true);
            }
         }

         if (enabled && client.field_1724 != null) {
            client.field_1724.method_18799(client.field_1724.method_18798().method_1021(this.velocityMultiplier));
         }
      });
   }
}
