package com.example;

import java.awt.Robot;
import java.awt.event.InputEvent;
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


private void lockOnEntity(class_310 client, class_1309 entity) {
    // Target position calculation
    class_243 targetPos = entity.method_33571().method_1023(0.0, 0.2, 0.0);
    class_243 playerPos = client.field_1724.method_33571();
    
    double diffX = targetPos.field_1352 - playerPos.field_1352;
    double diffY = targetPos.field_1351 - playerPos.field_1351;
    double diffZ = targetPos.field_1350 - playerPos.field_1350;
    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
    
    // Calculate the absolute target angles
    float targetYaw = class_3532.method_15393((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F);
    float targetPitch = class_3532.method_15393((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))));
    
    // Define the speed factor (0.0f = no movement, 1.0f = instant lock-on)
    float speedFactor = 0.15F; 

    // FIXED: Using getter methods instead of private fields
    float currentYaw = client.field_1724.method_36454(); 
    float currentPitch = client.field_1724.method_36455(); 

    // Safely interpolate angles by finding the shortest path distance
    float yawDiff = class_3532.method_15393(targetYaw - currentYaw);
    float pitchDiff = class_3532.method_15393(targetPitch - currentPitch);

    float interpolatedYaw = currentYaw + yawDiff * speedFactor;
    float interpolatedPitch = currentPitch + pitchDiff * speedFactor;

    // Apply the smoothed angles
    client.field_1724.method_36456(interpolatedYaw);
    client.field_1724.method_36457(interpolatedPitch);
    client.field_1724.field_6241 = interpolatedYaw;
    client.field_1724.field_6283 = interpolatedYaw;
    client.field_1724.field_5982 = interpolatedYaw;
    client.field_1724.field_6004 = interpolatedPitch;
   }
}
}
