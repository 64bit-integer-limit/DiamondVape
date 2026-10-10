package com.example;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.class_1297;
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
import net.minecraft.class_761;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_3675.class_307;

public class ESPMod implements ClientModInitializer {
   public static boolean enabled = false;
   private class_304 toggleKey;
   private class_310 client;

   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.toggleKey = new class_304("key.hellish.esp", class_307.field_1668, 86, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (this.toggleKey.method_1436() && client.field_1724 != null) {
            enabled = !enabled;
            client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] ESP " + (enabled ? "§aON" : "§cOFF")), false);
         }
      });
      WorldRenderEvents.LAST.register(this::onRender);
   }

   private void onRender(WorldRenderContext context) {
      if (enabled && this.client.field_1687 != null && this.client.field_1724 != null) {
         class_4587 matrices = context.matrixStack();
         class_243 cameraPos = context.camera().method_19326();

         for (class_1297 entity : this.client.field_1687.method_18112()) {
            if (entity != this.client.field_1724) {
               matrices.method_22903();
               double x = entity.method_30950(context.tickDelta()).field_1352 - cameraPos.field_1352;
               double y = entity.method_30950(context.tickDelta()).field_1351 - cameraPos.field_1351;
               double z = entity.method_30950(context.tickDelta()).field_1350 - cameraPos.field_1350;
               matrices.method_22904(x, y, z);
               class_238 box = entity.method_5829().method_989(-entity.method_23317(), -entity.method_23318(), -entity.method_23321());
               this.renderEspBox(matrices, box);
               matrices.method_22909();
            }
         }
      }
   }

   private void renderEspBox(class_4587 matrices, class_238 box) {
      class_289 tessellator = class_289.method_1348();
      class_287 bufferBuilder = tessellator.method_1349();
      RenderSystem.setShader(class_757::method_34540);
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      bufferBuilder.method_1328(class_5596.field_29344, class_290.field_1576);
      
      // Changed color parameters from Red (1.0F, 0.0F, 0.0F) to Cyan (0.0F, 1.0F, 1.0F)
      class_761.method_22982(matrices, bufferBuilder, box, 0.0F, 1.0F, 1.0F, 1.0F);
      
      tessellator.method_1350();
      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }
}
