package xyz.devcomp.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.network.ServerStatusPacketListenerImpl;
import xyz.devcomp.Stinky;

@Mixin(ServerStatusPacketListenerImpl.class)
public class ServerStatusPacketListenerImplMixin {
    private static ArrayList<String> MOTDs = Stinky.Config.getMOTDs();

    @ModifyVariable(method = "<init>(Lnet/minecraft/network/protocol/status/ServerStatus;Lnet/minecraft/network/Connection;)V", at = @At("HEAD"), ordinal = 0)
    private static ServerStatus injected(ServerStatus serverStatus) {
        return new ServerStatus(
                Component.literal(ServerStatusPacketListenerImplMixin.MOTDs
                        .get((int) (Math.random() * ServerStatusPacketListenerImplMixin.MOTDs.size()))),
                serverStatus.players(), serverStatus.version(),
                serverStatus.favicon(), serverStatus.enforcesSecureChat());
    }
}
