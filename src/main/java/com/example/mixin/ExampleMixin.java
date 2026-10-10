package com.example.mixin;

import net.minecraft.class_437; // The Intermediary class for Screen/TitleScreen
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_437.class)
public class ExampleMixin {
    // method_25426 is the obfuscated Intermediary name for the init() method in 1.19.4
    @Inject(at = @At("HEAD"), method = "method_25426()V")
    private void onInit(CallbackInfo info) {
        System.out.println("DiamondVape hooks injected into Title Screen successfully!");
    }
}
