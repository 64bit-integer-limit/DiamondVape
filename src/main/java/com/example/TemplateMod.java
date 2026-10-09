package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class TemplateMod implements ClientModInitializer {
   public static final String MOD_ID = "template-mod";
   public static final Logger LOGGER = LoggerFactory.getLogger("template-mod");

   public void onInitializeClient() {
      HellishHUD.init();
      LOGGER.info("Hello Fabric world!");
   }
}
