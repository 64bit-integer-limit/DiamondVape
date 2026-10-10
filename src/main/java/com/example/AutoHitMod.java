package com.example;

import java.lang.reflect.Method;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
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
   private boolean clickQueued = false;

   @Override
   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.toggleKey = new class_304("key.hellish.autohit", class_307.field_1668, 72, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      
      // 1. Use the standard tick loop ONLY to toggle the mod state and check cooldowns
      ClientTickEvents.END_CLIENT_TICK.register(client -> {
         if (client.field_1724 != null) {
            while (this.toggleKey.method_1436()) {
               enabled = !enabled;
               client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] AutoHit " + (enabled ? "§aON" : "§cOFF")), false);
            }

            if (enabled && client.field_1755 == null && client.field_1765 != null && client.field_1765.method_17783() == class_240.field_1331) {
               class_3966 entityHit = (class_3966) client.field_1765;
               class_1297 target = entityHit.method_17782();
               
               if (target instanceof class_1309 && client.field_1724.method_7261(0.5F) >= 1.0F) {
                  // Queue the hit instead of attacking instantly in the late tick phase
                  this.clickQueued = true;
               }
            }
         }
      });

      // 2. Intercept the engine BEFORE player movement packets fire by hijacking the early render pass
      WorldRenderEvents.START.register(context -> {
         if (this.clickQueued && this.client.field_1724 != null && this.client.field_1761 != null) {
            this.clickQueued = false; // Reset the queue flag instantly
            this.executePreTickAttack();
         }
      });
   }

   private void executePreTickAttack() {
      try {
         // Get the vanilla left-click key binding instance natively
         class_304 attackKeyBind = this.client.field_1690.field_1904; 

         // Set the keybind state to active
         attackKeyBind.method_23481(true); // setPressed(true)

         // Force the game engine to evaluate the keypress sequence natively
         if (attackKeyBind.method_1436()) {
             try {
                 // Use Reflection to execute the private doAttack() routine
                 Method doAttackMethod;
                 try {
                     doAttackMethod = this.client.getClass().getDeclaredMethod("method_1536"); // Dev mapping
                 } catch (NoSuchMethodException e) {
                     doAttackMethod = this.client.getClass().getDeclaredMethod("doAttack"); // Prod fallback
                 }
                 doAttackMethod.setAccessible(true);
                 doAttackMethod.invoke(this.client);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

         // Release the key state so inputs don't lock down or freeze your character
         attackKeyBind.method_23481(false); // setPressed(false)

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
