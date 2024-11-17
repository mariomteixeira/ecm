package com.koomplo.ecm;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetHomeCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LOGGER.info("Registrando comando /sethome");
        dispatcher.register(Commands.literal("sethome")
                .executes(SetHomeCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        LOGGER.info("Executando o comando /sethome para o jogador: {}", player.getName().getString());

        CompoundTag tag = player.getPersistentData();
        Vec3 position = player.position();
        tag.putDouble("homeX", position.x);
        tag.putDouble("homeY", position.y);
        tag.putDouble("homeZ", position.z);
        tag.putString("homeDimension", player.level().dimension().location().toString());

        context.getSource().sendSuccess(() -> Component.literal("Posição inicial definida com sucesso!"), true);
        return 1;
    }
}
