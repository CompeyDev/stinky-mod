package xyz.devcomp.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import xyz.devcomp.Stinky;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    private ServerPlayer currentPlayer;
    private ChatFormatting currentFormattingStyle;
    private static final ArrayList<String> JoinMessages = Stinky.Config.getJoinMessageStrings();

    @ModifyVariable(method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V", at = @At("HEAD"), ordinal = 0)
    private ServerPlayer captureServerPlayer(ServerPlayer plyr) {
        this.currentPlayer = plyr;
        return plyr;
    }

    @ModifyVariable(method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V", at = @At("STORE"), ordinal = 0)
    private MutableComponent injectCustomMessage(MutableComponent base) {
        this.currentFormattingStyle = ChatFormatting.DARK_GREEN;

        return Component.literal(String.format(JoinMessages.get((int) (Math.random() * JoinMessages.size())), this.currentPlayer.getDisplayName().getString()));        
    }

    @ModifyVariable(method = "broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V", at = @At("HEAD"), ordinal = 0)
    private Component injectCustomBroadcastFormatting(Component component) {
        ChatFormatting currentChatFormattingStyle = this.currentFormattingStyle;

        // We assume the next call is a disconnect, because we never really 
        // know when a disconnect is initiated. In case it's not a disconnect,
        // the above injectCustomMessage will reset it back to DARK_GREEN
        // for connection
        this.currentFormattingStyle = ChatFormatting.DARK_RED; 

        return component.copy().withStyle(currentChatFormattingStyle);
    }
}
