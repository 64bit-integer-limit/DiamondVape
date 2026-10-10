package com.example;

import java.lang.reflect.Field;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3966;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3675.class_307;

public class AutoHitMod implements ClientModInitializer {
   public static boolean enabled = false;
   private class_304 toggleKey;
   private class_310 client;

   @Override
   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.toggleKey = new class_304("key.hellish.autohit", class_307.field_1668, 72, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      
      ClientTickEvents.END_CLIENT_TICK.register(client -> {
         if (client.field_1724 != null) {
            while (this.toggleKey.method_1436()) {
               enabled = !enabled;
               client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] AutoHit " + (enabled ? "§aON" : "§cOFF")), false);
            }

            if (enabled && client.field_1755 == null && client.field_1765 != null && client.field_1765.method_17783() == class_240.field_1331) {
               class_3966 entityHit = (class_3966) client.field_1765;
               class_1297 target = entityHit.method_17782();
               
               // Attack cooldown check (Vanilla 1.0 threshold validation)
               if (target instanceof class_1309 && client.field_1724.method_7261(0.5F) >= 1.0F) {
                  this.attackEntity(target);
               }
            }
         }
      });
   }

   private void attackEntity(class_1297 target) {
      if (this.client.field_1761 == null || this.client.field_1724 == null || target == null) {
         return;
      }

      try {
         // Get the vanilla left-click key binding instance
         class_304 attackKeyBind = this.client.field_1690.field_1904; // client.options.attackKey

         // 1. Force the physical press state to true
         attackKeyBind.method_23481(true); // setPressed(true)

         // 2. Inject a click directly into the KeyBinding's internal input stream queue (field_1653 = timesPressed)
         try {
             Field timesPressedField = class_304.class.getDeclaredField("field_1653");
             timesPressedField.setAccessible(true);
             
             // Tell the game loop the key was clicked 1 time natively
             timesPressedField.setInt(attackKeyBind, 1); 
         } catch (Exception ignored) {}

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
