package com.koomplo.ecm;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EssentialsCommands.MOD_ID)
public class EssentialsCommands {
    public static final String MOD_ID = "ecm";
    public static final Logger LOGGER = LogManager.getLogger();

    public EssentialsCommands() {
        LOGGER.info("Inicializando o mod Essentials Commands");

        // Registra eventos na Forge Event Bus
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        LOGGER.info("Registrando comandos...");
        SetHomeCommand.register(event.getDispatcher());
        HomeCommand.register(event.getDispatcher());
    }
}
