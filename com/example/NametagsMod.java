package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import net.minecraft.class_327.class_6415;
import org.joml.Matrix4f;

public class NametagsMod implements ClientModInitializer {
   public static boolean enabled = false;
   private final class_310 client = class_310.method_1551();

   public void onInitializeClient() {
      WorldRenderEvents.AFTER_ENTITIES.register(this::renderNametags);
   }

   private void renderNametags(WorldRenderContext context) {
      if (enabled && this.client.field_1687 != null && this.client.field_1724 != null) {
         for (class_742 player : this.client.field_1687.method_18456()) {
            if (player != this.client.field_1724 && !(player.method_5739(this.client.field_1724) > 64.0F)) {
               this.drawNametag(context, player);
            }
         }
      }
   }

   private void drawNametag(WorldRenderContext context, class_1657 target) {
      class_4587 matrices = context.matrixStack();
      class_243 cameraPos = context.camera().method_19326();
      class_327 textRenderer = this.client.field_1772;
      double x = target.field_6014 + (target.method_23317() - target.field_6014) * context.tickDelta() - cameraPos.field_1352;
      double y = target.field_6036 + (target.method_23318() - target.field_6036) * context.tickDelta() - cameraPos.field_1351 + target.method_17682() + 0.5;
      double z = target.field_5969 + (target.method_23321() - target.field_5969) * context.tickDelta() - cameraPos.field_1350;
      matrices.method_22903();
      matrices.method_22904(x, y, z);
      matrices.method_22907(class_7833.field_40716.rotationDegrees(-context.camera().method_19330()));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(context.camera().method_19329()));
      float distance = this.client.field_1724.method_5739(target);
      float scale = Math.max(0.025F, distance * 0.005F);
      matrices.method_22905(-scale, -scale, scale);
      Matrix4f model = matrices.method_23760().method_23761();
      class_4597 vcp = context.consumers();
      String name = target.method_5820();
      float health = target.method_6032();
      String healthColor = health > 15.0F ? "§a" : (health > 8.0F ? "§e" : "§c");
      String fullText = name + " " + healthColor + String.format("%.1f", health);
      int width = textRenderer.method_1727(fullText) / 2;
      textRenderer.method_27521(fullText, -width, 0.0F, 16777215, false, model, vcp, class_6415.field_33994, Integer.MIN_VALUE, 15728880);
      if (!target.method_6047().method_7960()) {
         String itemText = "§7" + target.method_6047().method_7964().getString();
         int itemWidth = textRenderer.method_1727(itemText) / 2;
         matrices.method_46416(0.0F, 10.0F, 0.0F);
         textRenderer.method_27521(
            itemText, -itemWidth, 0.0F, 16777215, false, matrices.method_23760().method_23761(), vcp, class_6415.field_33994, Integer.MIN_VALUE, 15728880
         );
      }

      matrices.method_22909();
   }
}
