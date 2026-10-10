package com.example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
         // 1. Back up the player's true crosshair states
         class_1297 originalTargetedEntity = this.client.field_1692; // targetedEntity
         net.minecraft.class_239 originalCrosshairTarget = this.client.field_1765; // crosshairTarget

         // 2. Spoof crosshair fields smoothly inline with the current client physics tick pass
         this.client.field_1692 = target;
         this.client.field_1765 = new class_3966(target); // Fake EntityHitResult pointing to target

         // 3. Spoof the physical left-click key binding press state
         class_304 attackKeyBind = this.client.field_1690.field_1904; // client.options.attackKey
         attackKeyBind.method_23481(true); // setPressed(true)

         // 4. Clear attackCooldown (field_1740) via reflection so doAttack fires natively without skipping
         try {
             Field cooldownField = this.client.getClass().getDeclaredField("field_1740");
             cooldownField.setAccessible(true);
             cooldownField.setInt(this.client, 0); 
         } catch (Exception ignored) {}

         // 5. Look up and trigger the private doAttack() process natively
         Method doAttackMethod;
         try {
             doAttackMethod = this.client.getClass().getDeclaredMethod("method_1536"); // dev mapping
         } catch (NoSuchMethodException e) {
             doAttackMethod = this.client.getClass().getDeclaredMethod("doAttack"); // prod fallback
         }
         doAttackMethod.setAccessible(true);
         doAttackMethod.invoke(this.client);

         // 6. Clean up: Release the click state and instantly restore genuine crosshair vectors
         attackKeyBind.method_23481(false); // setPressed(false)
         this.client.field_1692 = originalTargetedEntity; 
         this.client.field_1765 = originalCrosshairTarget; 

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
