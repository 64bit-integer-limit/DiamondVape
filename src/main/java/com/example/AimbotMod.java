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
                     client.field_1724.method_7353(class_2561.method_43470("[HELLISH] Aimbot " + (enabled ? "§aON" : "§cOFF")), false);
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

private void lockOnEntity(class_310 client, class_1309 entity, float speed) {
   // 1. Calculate target positions
   class_243 targetPos = entity.method_33571().method_1023(0.0, 0.2, 0.0);
   class_243 playerPos = client.field_1724.method_33571();
   
   double diffX = targetPos.field_1352 - playerPos.field_1352;
   double diffY = targetPos.field_1351 - playerPos.field_1351;
   double diffZ = targetPos.field_1350 - playerPos.field_1350;
   double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
   
   // 2. Calculate the destination yaw and pitch
   float targetYaw = class_3532.method_15393((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F);
   float targetPitch = class_3532.method_15393((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))));
   
   // 3. Get the player's current rotation
   float currentYaw = client.field_1724.method_36454(); // Assuming method_36454() gets the current yaw
   float currentPitch = client.field_1724.method_36455(); // Assuming method_36455() gets the current pitch
   
   // 4. Calculate the shortest angular distance to avoid 360-degree snap spins
   float yawDiff = class_3532.method_15393(targetYaw - currentYaw);
   float pitchDiff = class_3532.method_15393(targetPitch - currentPitch);
   
   // 5. Interpolate (Clamp the rotation step by your speed multiplier)
   // Adjust 'speed' (e.g., 0.1F to 0.5F) depending on how fast or slow you want it to snap.
   float interpolatedYaw = currentYaw + yawDiff * speed;
   float interpolatedPitch = currentPitch + pitchDiff * speed;
   
   // 6. Apply smooth rotations
   client.field_1724.method_36456(interpolatedYaw);
   client.field_1724.method_36457(interpolatedPitch);
   client.field_1724.field_6241 = interpolatedYaw;
   client.field_1724.field_6283 = interpolatedYaw;
   client.field_1724.field_5982 = interpolatedYaw;
   client.field_1724.field_6004 = interpolatedPitch;
      };
   ]
