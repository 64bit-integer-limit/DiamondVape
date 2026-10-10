package com.example;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.class_1297;
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

public class TracerMod implements ClientModInitializer {
   public static boolean enabled = false;
   private class_304 toggleKey;
   private class_310 client;

   public void onInitializeClient() {
      this.client = class_310.method_1551();
      this.toggleKey = new class_304("key.hellish.tracers", class_307.field_1668, 75, "category.hellish");
      KeyBindingHelper.registerKeyBinding(this.toggleKey);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (this.toggleKey.method_1436() && client.field_1724 != null) {
            enabled = !enabled;
            client.field_1724.method_7353(class_2561.method_43470("[DiamondVape] Tracers " + (enabled ? "§aON" : "§cOFF")), false);
         }
      });
      WorldRenderEvents.LAST.register(this::onRender);
   }

   private void onRender(WorldRenderContext context) {
      if (enabled && this.client.field_1687 != null && this.client.field_1724 != null) {
         class_4587 matrices = context.matrixStack();
         class_243 cameraPos = context.camera().method_19326();
         Matrix4f worldMatrix = matrices.method_23760().method_23761();
         Matrix4f identityMatrix = new Matrix4f();
         class_289 tessellator = class_289.method_1348();
         class_287 bufferBuilder = tessellator.method_1349();
         RenderSystem.setShader(class_757::method_34540);
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         bufferBuilder.method_1328(class_5596.field_29344, class_290.field_1576);

         for (class_1297 entity : this.client.field_1687.method_18112()) {
            if (entity != this.client.field_1724) {
               double x = entity.method_30950(context.tickDelta()).field_1352 - cameraPos.field_1352;
               double y = entity.method_30950(context.tickDelta()).field_1351 - cameraPos.field_1351;
               double z = entity.method_30950(context.tickDelta()).field_1350 - cameraPos.field_1350;
               float targetY = (float)y + entity.method_17682() / 2.0F;
               
               // Origin point: White (RGB: 255, 255, 255)
               bufferBuilder.method_22918(identityMatrix, 0.0F, 0.0F, -0.1F).method_1336(255, 255, 255, 255).method_1344();
               
               // Target destination point: Cyan (RGB: 0, 255, 255)
               bufferBuilder.method_22918(worldMatrix, (float)x, targetY, (float)z).method_1336(0, 255, 255, 255).method_1344();
            }
         }

         tessellator.method_1350();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
      }
   }
}
