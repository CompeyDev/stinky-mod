package xyz.devcomp.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.network.chat.Component;
import xyz.devcomp.Stinky;

@Mixin(Component.class)
public interface ComponentMixin {
    @ModifyVariable(method = "translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;", at = @At("HEAD"), ordinal = 0)
    private static String transformLeaveMessage(String string) {
        // We cannot shadow this as a static member on the Component
        // class and ComponentMixin is an interface, so we just need to
        // fetch the messages on every leave. Not ideal, but whatever
        ArrayList<String> leaveMessages = Stinky.Config.getLeaveMessageStrings();

        // Since ServerGamePacketListenerImpl broadcasts the message without any 
        // intermediate variable for the component, the only thing we can do is 
        // intercept the message in Component#translatable and return a custom message

        if (string == "multiplayer.player.left") {
            return leaveMessages.get((int) (Math.random() * leaveMessages.size()));
        } 

        return string;
    }
}
