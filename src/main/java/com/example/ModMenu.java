package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import net.minecraft.class_3675.class_307;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ModMenu implements ClientModInitializer {
   private final class_310 client = class_310.method_1551();
   private class_304 menuToggle;
   private class_304 upKey;
   private class_304 downKey;
   private class_304 selectKey;
   private static boolean menuOpen = false;
   private static int selectedIndex = 0;
   private static int menuX = -1;
   private static int menuY = -1;
   private static boolean dragging = false;
   private static double dragOffsetX;
   private static double dragOffsetY;
   private static boolean draggingSlider = false;
   private static final String[] modules = new String[]{
      "FlyMod",
      "ESPMod",
      "ScaffoldMod",
      "ChestStealer",
      "AutoArmor",
      "Velocity",
      "Fullbright",
      "Tracers",
      "Autoclicker",
      "Triggerbot",
      "Bhop",
      "Aimbot",
      "Shieldbreaker",
      "HUD Color",
      "Trajectories",
      "Reachaura",
      "Nametags"
   };
   private static final int[] chestStealerSpeeds = new int[]{1, 2, 5, 10};
   private static int currentSpeedIndex = 0;

   public void onInitializeClient() {
      this.menuToggle = new class_304("key.modmenu.toggle", class_307.field_1668, 344, "category.modmenu");
      this.upKey = new class_304("key.modmenu.up", class_307.field_1668, 265, "category.modmenu");
      this.downKey = new class_304("key.modmenu.down", class_307.field_1668, 264, "category.modmenu");
      this.selectKey = new class_304("key.modmenu.select", class_307.field_1668, 257, "category.modmenu");
      KeyBindingHelper.registerKeyBinding(this.menuToggle);
      KeyBindingHelper.registerKeyBinding(this.upKey);
      KeyBindingHelper.registerKeyBinding(this.downKey);
      KeyBindingHelper.registerKeyBinding(this.selectKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> this.handleMenu());
      HudRenderCallback.EVENT.register(this::renderMenu);
   }

   private void handleMenu() {
      if (this.menuToggle.method_1436()) {
         menuOpen = !menuOpen;
      }

      if (menuOpen) {
         if (this.upKey.method_1436()) {
            selectedIndex = (selectedIndex - 1 + modules.length) % modules.length;
         }

         if (this.downKey.method_1436()) {
            selectedIndex = (selectedIndex + 1) % modules.length;
         }

         if (this.selectKey.method_1436()) {
            String module = modules[selectedIndex];
            if (module.equals("ChestStealer")) {
               currentSpeedIndex = (currentSpeedIndex + 1) % chestStealerSpeeds.length;
               CheststealerMod.itemsPerTick = chestStealerSpeeds[currentSpeedIndex];
            } else if (module.equals("HUD Color")) {
               HellishHUD.colorIndex = (HellishHUD.colorIndex + 1) % HellishHUD.colors.length;
            } else {
               this.toggleModule(module);
            }
         }
      }
   }

   private void toggleModule(String module) {
      switch (module) {
         case "FlyMod":
            FlyMod.enabled = !FlyMod.enabled;
            break;
         case "ESPMod":
            ESPMod.enabled = !ESPMod.enabled;
            break;
         case "ScaffoldMod":
            ScaffoldMod.enabled = !ScaffoldMod.enabled;
            break;
         case "ChestStealer":
            CheststealerMod.enabled = !CheststealerMod.enabled;
            break;
         case "AutoArmor":
            AutoArmorMod.enabled = !AutoArmorMod.enabled;
            break;
         case "Velocity":
            VelocityMod.enabled = !VelocityMod.enabled;
            break;
         case "Fullbright":
            MyCheatMod.enabled = !MyCheatMod.enabled;
            break;
         case "Tracers":
            TracerMod.enabled = !TracerMod.enabled;
            break;
         case "Autoclicker":
            AutoClickerMod.enabled = !AutoClickerMod.enabled;
            break;
         case "Triggerbot":
            AutoHitMod.enabled = !AutoHitMod.enabled;
            break;
         case "Bhop":
            MovementMod.enabled = !MovementMod.enabled;
            break;
         case "Aimbot":
            AimbotMod.enabled = !AimbotMod.enabled;
            break;
         case "Shieldbreaker":
            ShieldBreakMod.enabled = !ShieldBreakMod.enabled;
            break;
         case "Trajectories":
            ProjectileMod.enabled = !ProjectileMod.enabled;
            break;
         case "Reachaura":
            ReachMod.enabled = !ReachMod.enabled;
            break;
         case "Nametags":
            NametagsMod.enabled = !NametagsMod.enabled;
      }
   }

   private void renderMenu(class_4587 matrices, float tickDelta) {
      if (menuOpen) {
         int menuWidth = 160;
         int menuHeight = 20 + modules.length * 12 + (modules[selectedIndex].equals("Triggerbot") ? 15 : 0);
         if (menuX == -1 || menuY == -1) {
            menuX = (this.client.method_22683().method_4486() - menuWidth) / 2;
            menuY = (this.client.method_22683().method_4502() - menuHeight) / 2;
         }

         double mouseX = this.client.field_1729.method_1603() * this.client.method_22683().method_4486() / this.client.method_22683().method_4480();
         double mouseY = this.client.field_1729.method_1604() * this.client.method_22683().method_4502() / this.client.method_22683().method_4507();
         boolean mouseDown = GLFW.glfwGetMouseButton(this.client.method_22683().method_4490(), 0) == 1;
         if (this.client.field_1755 instanceof class_408) {
            this.updateDragging(mouseX, mouseY, mouseDown, menuWidth);
            this.client.field_1772.method_1720(matrices, "§3[Drag Menu]", menuX, menuY - 10, 0x00AAAA);
         } else {
            dragging = false;
            draggingSlider = false;
         }

         int x = menuX;
         int y = menuY;
         
         // TITLE: Forced to Dark Aqua hex formatting natively (0x00AAAA)
         this.client.field_1772.method_1720(matrices, "§b§lDiamondVape", x, y, 0x00AAAA);
         y += 15;

         for (int i = 0; i < modules.length; i++) {
            String module = modules[i];
            boolean isSelected = i == selectedIndex;
            boolean enabled = this.getModuleStatus(module);
            
            // Text color customization matching our scheme selection rules
            int displayColor = enabled ? 0x00AAAA : 0x555555; 
            String text = (isSelected ? "§b> " : "  ") + module + (enabled ? " §bON" : " §7OFF");
            this.client.field_1772.method_1720(matrices, text, x, y, displayColor);
            
            if (isSelected && module.equals("Reachaura")) {
               y += 12;
               this.renderRangeSlider(matrices, x + 10, y, mouseX, mouseY, mouseDown);
               y += 5;
            }

            y += 12;
         }
      }
   }

   private void renderRangeSlider(class_4587 matrices, int x, int y, double mouseX, double mouseY, boolean mouseDown) {
      int sliderWidth = 100;
      int sliderHeight = 4;
      this.fill(matrices, x, y + 4, x + sliderWidth, y + 4 + sliderHeight, -11184811);
      double min = 3.0;
      double max = 12.0;
      double current = ReachMod.ATTACK_RANGE;
      double percentage = (current - min) / (max - min);
      int knobX = x + (int)(percentage * sliderWidth);
      
      // SLIDER KNOB: Shifted cleanly to dark aqua color array constants (0x00AAAA)
      this.fill(matrices, knobX - 2, y + 2, knobX + 2, y + 10, 0x00AAAA);
      this.client.field_1772.method_1720(matrices, String.format("§3Range: %.1f", current), x + sliderWidth + 5, y + 2, 0x00AAAA);
      
      if (this.client.field_1755 instanceof class_408 && mouseDown) {
         if (mouseX >= x && mouseX <= x + sliderWidth && mouseY >= y && mouseY <= y + 12) {
            draggingSlider = true;
         }

         if (draggingSlider) {
            double newPart = Math.max(0.0, Math.min(1.0, (mouseX - x) / sliderWidth));
            ReachMod.ATTACK_RANGE = min + newPart * (max - min);
         }
      } else {
         draggingSlider = false;
      }
   }

   private void fill(class_4587 matrices, int x1, int y1, int x2, int y2, int color) {
      class_332.method_25294(matrices, x1, y1, x2, y2, color);
   }

   private void updateDragging(double mouseX, double mouseY, boolean mouseDown, int menuWidth) {
      if (mouseDown) {
         if (!dragging && !draggingSlider) {
            if (mouseX >= menuX && mouseX <= menuX + menuWidth && mouseY >= menuY && mouseY <= menuY + 15) {
               dragging = true;
               dragOffsetX = mouseX - menuX;
               dragOffsetY = mouseY - menuY;
            }
         } else if (dragging) {
            menuX = (int)(mouseX - dragOffsetX);
            menuY = (int)(mouseY - dragOffsetY);
         }
      } else {
         dragging = false;
      }
   }

   private boolean getModuleStatus(String module) {
      return switch (module) {
         case "FlyMod" -> FlyMod.enabled;
         case "ESPMod" -> ESPMod.enabled;
         case "ScaffoldMod" -> ScaffoldMod.enabled;
         case "ChestStealer" -> CheststealerMod.enabled;
         case "AutoArmor" -> AutoArmorMod.enabled;
         case "Velocity" -> VelocityMod.enabled;
         case "Fullbright" -> MyCheatMod.enabled;
         case "Tracers" -> TracerMod.enabled;
         case "Autoclicker" -> AutoClickerMod.enabled;
         case "Triggerbot" -> AutoHitMod.enabled;
         case "Bhop" -> MovementMod.enabled;
         case "Aimbot" -> AimbotMod.enabled;
         case "Shieldbreaker" -> ShieldBreakMod.enabled;
         case "Trajectories" -> ProjectileMod.enabled;
         case "Reachaura" -> ReachMod.enabled;
         case "Nametags" -> NametagsMod.enabled;
         default -> false;
         };
      }
   }
