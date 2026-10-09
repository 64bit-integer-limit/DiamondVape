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
public class FlyMod implements ClientModInitializer {
   private class_304 flyToggle;
   private class_310 client;
   public static boolean enabled = false;

   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.flyToggle = new class_304("key.flymod.toggle", class_307.field_1668, 70, "category.flymod");
      KeyBindingHelper.registerKeyBinding(this.flyToggle);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (this.flyToggle.method_1436()) {
            this.toggleFly();
         }
      });
   }

   private void toggleFly() {
      if (this.client.field_1724 != null) {
         enabled = !this.client.field_1724.method_31549().field_7478;
         this.client.field_1724.method_31549().field_7478 = enabled;
         if (!enabled) {
            this.client.field_1724.method_31549().field_7479 = false;
         }

         this.client.field_1724.method_7355();
         this.client.field_1705.method_1743().method_1812(class_2561.method_43470("§b[DiamondVape]§r Fly " + (enabled ? "§2enabled§r" : "§4disabled§r")));
      }
   }
}
