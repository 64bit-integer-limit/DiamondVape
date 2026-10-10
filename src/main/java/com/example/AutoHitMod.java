package com.example;

import java.lang.reflect.Method;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_1268;
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

   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.toggleKey = new class_304("key.hellish.autohit", class_307.field_1668, 72, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (client.field_1724 != null) {
            while (this.toggleKey.method_1436()) {
               enabled = !enabled;
               client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] AutoHit " + (enabled ? "§aON" : "§cOFF")), false);
            }

            if (enabled && client.field_1755 == null && client.field_1765 != null && client.field_1765.method_17783() == class_240.field_1331) {
               class_3966 entityHit = (class_3966)client.field_1765;
               class_1297 target = entityHit.method_17782();
               if (target instanceof class_1309 && client.field_1724.method_7261(0.5F) >= 1.0F) {
                  this.attackEntity(target);
               }
            }
         }
      });
   }


private void attackEntity(class_1297 target) {
   if (this.client.field_1761 != null && this.client.field_1724 != null && target != null) {
      
      // 1. Back up the player's true crosshair look-at states
      class_1297 originalTargetedEntity = this.client.field_1692; // targetedEntity
      net.minecraft.class_239 originalCrosshairTarget = this.client.field_1765; // crosshairTarget

      // 2. Spoof the crosshair fields completely so the game thinks we are targeting the entity
      this.client.field_1692 = target;
      this.client.field_1765 = new net.minecraft.class_3966(target); // Fake EntityHitResult

      // 3. Programmatically force the native left-click keybind to think it was physically pressed
      // field_1904 is the intermediary mapping for client.options.attackKey
      net.minecraft.class_304 attackKeyBind = this.client.field_1690.field_1904; 
      
      // Set the keypress state to true
      attackKeyBind.method_23481(true); // setPressed(true)

      // 4. Force the keybind's internal "timesPressed" tick increment loops via Reflection
      try {
          // Look up the private "timesPressed" or "wasPressed" tracking logic if needed,
          // but calling method_1536 directly while the crosshair AND key are pressed handles everything.
          Method doAttackMethod;
          try {
              doAttackMethod = this.client.getClass().getDeclaredMethod("method_1536");
          } catch (NoSuchMethodException e) {
              doAttackMethod = this.client.getClass().getDeclaredMethod("doAttack");
          }
          
          doAttackMethod.setAccessible(true); 
          doAttackMethod.invoke(this.client); 
          
      } catch (Exception e) {
          e.printStackTrace(); 
      }

      // 5. Instantly release the keybind and restore the player's actual crosshair 
      attackKeyBind.method_23481(false); // setPressed(false)
      this.client.field_1692 = originalTargetedEntity; 
      this.client.field_1765 = originalCrosshairTarget; 
      }
   }
}
