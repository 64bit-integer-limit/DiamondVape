package com.example;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.class_310;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import org.lwjgl.glfw.GLFW;

public class HellishHUD {
   public static int[] colors = new int[]{5592575, 65280, 4474111, 16776960, 16711935, 65535, 16777215};
   public static int colorIndex = 0;
   public static int hudX = 10;
   public static int hudY = 10;
   private static boolean dragging = false;
   private static double dragOffsetX;
   private static double dragOffsetY;
   private static final Map<String, Float> animationProgress = new HashMap<>();
   private static final float ANIMATION_SPEED = 0.15F;

   public static void init() {
      HudRenderCallback.EVENT.register(HellishHUD::render);
   }

   private static void render(class_4587 matrices, float tickDelta) {
      class_310 mc = class_310.method_1551();
      if (mc.field_1724 != null && !mc.field_1690.field_1842) {
         if (mc.field_1755 instanceof class_408) {
            handleDragging(mc);
            mc.field_1772.method_1720(matrices, "§7[Dragable]", hudX, hudY - 10, 16777215);
         } else {
            dragging = false;
         }

         int currentY = hudY;
         mc.field_1772.method_1720(matrices, "§lDiamondVape", hudX, currentY, colors[colorIndex]);
         currentY += 12;
         currentY = renderModule(mc, matrices, "FlyMod", FlyMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "ESPMod", ESPMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "ScaffoldMod", ScaffoldMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "ChestStealer", CheststealerMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "AutoArmor", AutoArmorMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Velocity", VelocityMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Fullbright", MyCheatMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Tracers", TracerMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Autoclicker", AutoClickerMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Triggerbot", AutoHitMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Bhop", MovementMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Aimbot", AimbotMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Shieldbreaker", ShieldBreakMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Trajectories", ProjectileMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Reachaura", ReachMod.enabled, hudX, currentY);
         currentY = renderModule(mc, matrices, "Nametags", NametagsMod.enabled, hudX, currentY);
      }
   }

   private static void handleDragging(class_310 mc) {
      double mouseX = mc.field_1729.method_1603() * mc.method_22683().method_4486() / mc.method_22683().method_4480();
      double mouseY = mc.field_1729.method_1604() * mc.method_22683().method_4502() / mc.method_22683().method_4507();
      boolean isMouseDown = GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 0) == 1;
      if (isMouseDown) {
         if (!dragging) {
            if (mouseX >= hudX && mouseX <= hudX + 60 && mouseY >= hudY && mouseY <= hudY + 12) {
               dragging = true;
               dragOffsetX = mouseX - hudX;
               dragOffsetY = mouseY - hudY;
            }
         } else {
            hudX = (int)(mouseX - dragOffsetX);
            hudY = (int)(mouseY - dragOffsetY);
         }
      } else {
         dragging = false;
      }
   }

   private static int renderModule(class_310 mc, class_4587 matrices, String name, boolean enabled, int x, int y) {
      animationProgress.putIfAbsent(name, 0.0F);
      float progress = animationProgress.get(name);
      if (enabled && progress < 1.0F) {
         progress = Math.min(1.0F, progress + 0.15F);
      } else if (!enabled && progress > 0.0F) {
         progress = Math.max(0.0F, progress - 0.15F);
      }

      animationProgress.put(name, progress);
      if (progress > 0.0F) {
         int alpha = (int)(progress * 255.0F);
         int color = enabled ? alpha << 24 | colors[colorIndex] & 16777215 : alpha << 24 | 5592575;
         int offsetX = (int)((1.0F - progress) * 10.0F);
         mc.field_1772.method_1720(matrices, name, x - offsetX, y, color);
         mc.field_1772.method_1720(matrices, "§8>", x + mc.field_1772.method_1727(name) + 2 - offsetX, y, color);
         return y + 12;
      } else {
         return y;
      }
   }
}
