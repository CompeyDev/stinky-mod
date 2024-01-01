package xyz.devcomp;

import xyz.devcomp.config.ConfigHandler;
import xyz.devcomp.config.ConfigModel;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stinky implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("stinky");
	public static final ConfigModel Config = new ConfigHandler().getConfig();
	// public static final ResourceLocation EC_SOUND_ID = new ResourceLocation("stinky:ping");
    // public static SoundEvent EC_SOUND_EVENT = SoundEvent.createVariableRangeEvent(EC_SOUND_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello from Stinky!");
		
		ServerMessageEvents.CHAT_MESSAGE.register((PlayerChatMessage msg, ServerPlayer plr, ChatType.Bound bound) -> {
			// NOTE: This makes this command dysfunctional on offline mode servers
			String msgString = msg.signedContent();
			
			if (msgString.trim().equalsIgnoreCase(";ec")) {
				// We're setting the health to 0, instead of plr.kill(), because this 
				// abuses a flaw in the graves VanillaTweaks datapack to make the player
				// respawn without creating a grave or losing their items
				
				// TODO: Play stinky:ping sound
				plr.setHealth(0);
			}
		});


		
	}
}