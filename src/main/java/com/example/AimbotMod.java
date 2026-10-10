package com.example;

import java.util.Comparator;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3675.class_307;

public class AimbotMod implements ClientModInitializer {
   public static boolean enabled = false;
   private class_304 toggleKey;
   private final float range = 5.0F;

   public void onInitializeClient() {
      this.toggleKey = new class_304("key.hellish.aimbot", class_307.field_1668, 66, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK
         .register(
            (EndTick)client -> {
               if (client.field_1724 != null && client.field_1687 != null) {
                  while (this.toggleKey.method_1436()) {
                     enabled = !enabled;
                     client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] Aimbot " + (enabled ? "§aON" : "§cOFF")), false);
                  }

                  if (enabled && client.field_1755 == null) {
                     class_1309 target = client.field_1687
                        .method_8390(
                           class_1309.class,
                           client.field_1724.method_5829().method_1014(5.0),
                           entity -> entity != client.field_1724 && entity.method_5805() && !entity.method_5767()
                        )
                        .stream()
                        .min(Comparator.comparingDouble(entity -> client.field_1724.method_5739(entity)))
                        .orElse(null);
                     if (target != null) {
                        this.lockOnEntity(client, target);
                     }
                  }
               }
            }
         );
   }

private void lockOnEntity(class_310 client, class_1309 entity) {
    class_243 targetPos = entity.method_33571().method_1023(0.0, 0.2, 0.0);
    class_243 playerPos = client.field_1724.method_33571();
    
    double diffX = targetPos.field_1352 - playerPos.field_1352;
    double diffY = targetPos.field_1351 - playerPos.field_1351;
    double diffZ = targetPos.field_1350 - playerPos.field_1350;
    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
    
    // 1. Calculate the absolute target angles
    float targetYaw = class_3532.method_15393((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F);
    float targetPitch = class_3532.method_15393((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))));
    
    // 2. Get the current player angles
    float currentYaw = client.field_1724.method_36454(); // Assuming method_36454() or direct field access reads current yaw
    float currentPitch = client.field_1724.method_36455(); // Assuming method_36455() or direct field access reads current pitch
    
    // 3. Smoothly interpolate angles (Adjust 'speed' between 0.05f and 0.3f. Lower = smoother/slower)
    float speed = 0.15f; 
    
    // Use wrapDegrees to make sure yaw doesn't spin 360 degrees the wrong way when passing the threshold
    float deltaYaw = class_3532.method_15393(targetYaw - currentYaw);
    float deltaPitch = targetPitch - currentPitch;
    
    float newYaw = currentYaw + deltaYaw * speed;
    float newPitch = currentPitch + deltaPitch * speed;
    
    // 4. Apply the smoothed values
    client.field_1724.method_36456(newYaw);
    client.field_1724.method_36457(newPitch);
    
    // Keep internal engine fields synced to prevent stuttering
    client.field_1724.field_6241 = newYaw;
    client.field_1724.field_6283 = newYaw;
    client.field_1724.field_5982 = newYaw;
    client.field_1724.field_6004 = newPitch;
   }

