package com.example;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.class_1675;
import net.minecraft.class_1753;
import net.minecraft.class_1771;
import net.minecraft.class_1776;
import net.minecraft.class_1799;
import net.minecraft.class_1823;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_239.class_240;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_3675.class_307;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class ProjectileMod implements ClientModInitializer {
   private class_304 projectileToggle;
   private final class_310 client = class_310.method_1551();
   public static boolean enabled = false;
   private static final List<class_243> trajectoryPoints = new ArrayList<>();

   public void onInitializeClient() {
      this.projectileToggle = new class_304("key.hellish.projectile", class_307.field_1668, 80, "Hellish Client");
      KeyBindingHelper.registerKeyBinding(this.projectileToggle);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (this.projectileToggle.method_1436()) {
            this.toggleProjectile();
         }

         if (enabled) {
            this.calculateTrajectory();
         }
      });
      WorldRenderEvents.AFTER_ENTITIES.register(this::renderTrajectoryInWorld);
   }

   private void toggleProjectile() {
      enabled = !enabled;
      if (this.client.field_1724 != null) {
         this.client.field_1705.method_1743().method_1812(class_2561.method_43470("[§4HELLISH]§r Projectiles " + (enabled ? "§2enabled§r" : "§4disabled§r")));
      }
   }

   private void calculateTrajectory() {
      trajectoryPoints.clear();
      if (this.client.field_1724 != null && this.client.field_1687 != null) {
         class_1799 stack = this.client.field_1724.method_6047();
         double gravity;
         float speed;
         if (stack.method_7909() instanceof class_1753) {
            if (!this.client.field_1724.method_6115()) {
               return;
            }

            int useTicks = stack.method_7935() - this.client.field_1724.method_6014();
            float pull = class_1753.method_7722(useTicks);
            if (pull < 0.1F) {
               return;
            }

            gravity = 0.05;
            speed = pull * 3.0F;
         } else {
            if (!(stack.method_7909() instanceof class_1823) && !(stack.method_7909() instanceof class_1771) && !(stack.method_7909() instanceof class_1776)) {
               return;
            }

            gravity = 0.03;
            speed = 1.5F;
         }

         class_243 pos = this.client.field_1724.method_5836(1.0F);
         class_243 velocity = this.client.field_1724.method_5828(1.0F).method_1021(speed);
         trajectoryPoints.add(pos);

         for (int i = 0; i < 100; i++) {
            class_243 nextPos = pos.method_1019(velocity);
            class_3965 blockHit = this.client
               .field_1687
               .method_17742(new class_3959(pos, nextPos, class_3960.field_17558, class_242.field_1348, this.client.field_1724));
            class_238 box = new class_238(pos, nextPos).method_1014(0.5);
            class_3966 entityHit = class_1675.method_18075(
               this.client.field_1724, pos, nextPos, box, entity -> !entity.method_7325() && entity.method_5863(), nextPos.method_1025(pos)
            );
            if (entityHit != null) {
               trajectoryPoints.add(entityHit.method_17784());
               break;
            }

            if (blockHit.method_17783() != class_240.field_1333) {
               trajectoryPoints.add(blockHit.method_17784());
               break;
            }

            pos = nextPos;
            velocity = velocity.method_1021(0.99).method_1023(0.0, gravity, 0.0);
            trajectoryPoints.add(pos);
         }
      }
   }

   private void renderTrajectoryInWorld(WorldRenderContext context) {
      if (enabled && trajectoryPoints.size() >= 2) {
         class_4587 matrices = context.matrixStack();
         class_243 cameraPos = context.camera().method_19326();
         matrices.method_22903();
         matrices.method_22904(-cameraPos.field_1352, -cameraPos.field_1351, -cameraPos.field_1350);
         Matrix4f matrix = matrices.method_23760().method_23761();
         RenderSystem.setShader(class_757::method_34540);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableCull();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         class_289 tessellator = class_289.method_1348();
         class_287 bufferBuilder = tessellator.method_1349();
         int colorInt = HellishHUD.colors[HellishHUD.colorIndex];
         float r = (colorInt >> 16 & 0xFF) / 255.0F;
         float g = (colorInt >> 8 & 0xFF) / 255.0F;
         float b = (colorInt & 0xFF) / 255.0F;
         bufferBuilder.method_1328(class_5596.field_29345, class_290.field_1576);

         for (class_243 p : trajectoryPoints) {
            bufferBuilder.method_22918(matrix, (float)p.field_1352, (float)p.field_1351, (float)p.field_1350).method_22915(r, g, b, 0.8F).method_1344();
         }

         tessellator.method_1350();
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         matrices.method_22909();
      }
   }
}
