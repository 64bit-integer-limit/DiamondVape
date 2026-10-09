package com.example;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_3675.class_307;
import org.joml.Matrix4f;

public class ReachMod implements ClientModInitializer {
   public static boolean enabled = false;
   public static double ATTACK_RANGE = 3.8;
   private class_304 toggleKey;
   private final class_310 client = class_310.method_1551();

   public void onInitializeClient() {
      this.toggleKey = new class_304("key.hellish.reachmod", class_307.field_1668, 72, "Hellish Client");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (client.field_1724 != null && client.field_1687 != null) {
            while (this.toggleKey.method_1436()) {
               enabled = !enabled;
               client.field_1724.method_7353(class_2561.method_43470("§b[DiamondVape]§f AutoHit: " + (enabled ? "§aAN" : "§cAUS")), false);
            }

            if (enabled && client.field_1755 == null && client.field_1724.method_7261(0.5F) >= 1.0F) {
               this.findAndAttackTarget();
            }
         }
      });
      WorldRenderEvents.AFTER_ENTITIES.register(this::renderReachCircle);
   }

   private void findAndAttackTarget() {
      class_238 searchBox = this.client.field_1724.method_5829().method_1014(ATTACK_RANGE);
      List<class_1309> targets = this.client
         .field_1687
         .method_8390(
            class_1309.class, searchBox, entity -> entity != this.client.field_1724 && entity.method_5805() && !entity.method_5722(this.client.field_1724)
         );
      class_1309 closestTarget = targets.stream()
         .filter(entity -> this.client.field_1724.method_5739(entity) <= ATTACK_RANGE)
         .min(Comparator.comparingDouble(entity -> this.client.field_1724.method_5739(entity)))
         .orElse(null);
      if (closestTarget != null) {
         this.attackEntity(closestTarget);
      }
   }

   private void attackEntity(class_1297 target) {
      if (this.client.field_1761 != null && this.client.field_1724 != null) {
         this.client.field_1761.method_2918(this.client.field_1724, target);
         this.client.field_1724.method_6104(class_1268.field_5808);
      }
   }

   private void renderReachCircle(WorldRenderContext context) {
      if (enabled && this.client.field_1724 != null) {
         class_4587 matrices = context.matrixStack();
         class_243 cameraPos = context.camera().method_19326();
         class_243 playerPos = this.client.field_1724.method_19538();
         matrices.method_22903();
         matrices.method_22904(-cameraPos.field_1352, -cameraPos.field_1351, -cameraPos.field_1350);
         Matrix4f matrix = matrices.method_23760().method_23761();
         RenderSystem.setShader(class_757::method_34540);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableCull();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.lineWidth(2.0F);
         class_289 tessellator = class_289.method_1348();
         class_287 bufferBuilder = tessellator.method_1349();
         int colorInt = HellishHUD.colors[HellishHUD.colorIndex];
         float r = (colorInt >> 16 & 0xFF) / 255.0F;
         float g = (colorInt >> 8 & 0xFF) / 255.0F;
         float b = (colorInt & 0xFF) / 255.0F;
         float alpha = 0.6F;
         bufferBuilder.method_1328(class_5596.field_29345, class_290.field_1576);
         int segments = 60;
         double radius = ATTACK_RANGE;
         double yLevel = playerPos.field_1351 - 0.05;

         for (int i = 0; i <= segments; i++) {
            double angle = (Math.PI * 2) * i / segments;
            double x = playerPos.field_1352 + radius * Math.cos(angle);
            double z = playerPos.field_1350 + radius * Math.sin(angle);
            bufferBuilder.method_22918(matrix, (float)x, (float)yLevel, (float)z).method_22915(r, g, b, alpha).method_1344();
         }

         tessellator.method_1350();
         RenderSystem.lineWidth(1.0F);
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         matrices.method_22909();
      }
   }
}
