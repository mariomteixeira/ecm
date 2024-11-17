package com.koomplo.ecm;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomeCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LOGGER.info("Registrando comando /home");
        dispatcher.register(Commands.literal("home")
                .executes(HomeCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        LOGGER.info("Executando o comando /home para o jogador: {}", player.getName().getString());

        CompoundTag tag = player.getPersistentData();

        if (!tag.contains("homeX") || !tag.contains("homeY") || !tag.contains("homeZ") || !tag.contains("homeDimension")) {
            context.getSource().sendFailure(Component.literal("Você ainda não definiu sua posição inicial! Use /sethome primeiro."));
            return 1;
        }

        double x = tag.getDouble("homeX");
        double y = tag.getDouble("homeY");
        double z = tag.getDouble("homeZ");
        String dimension = tag.getString("homeDimension");

        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));
        ServerLevel level = player.getServer().getLevel(dimensionKey);

        if (level != null) {
            LOGGER.info("Teleportando o jogador para a posição definida: [{}] ({}, {}, {})", dimension, x, y, z);
            player.teleportTo(level, x, y, z, player.getYRot(), player.getXRot());
            context.getSource().sendSuccess(() -> Component.literal("Você foi teleportado para sua posição inicial!"), true);
        } else {
            LOGGER.error("Falha ao teleportar: dimensão inválida.");
            context.getSource().sendFailure(Component.literal("Erro ao teleportar: dimensão inválida."));
        }

        return 1;
    }
}
