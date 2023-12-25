package xyz.devcomp;

import java.util.ArrayList;

import xyz.devcomp.config.ConfigHandler;
import xyz.devcomp.config.ConfigModel;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stinky implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("stinky");
	public static final ConfigModel Config = new ConfigHandler().getConfig();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello from Stinky!");

		// TODO: Cleanup logic with a shared function for both join & leave events
		// FIXME: Load & Unload events are fired on death and respawn too, not just join and leave

		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			ArrayList<String> joinMsgStrings = Stinky.Config.getJoinMessageStrings();
			String formattedUsername = entity.getName().toString().replaceAll("literal", "").replaceAll("\\{", "")
					.replaceAll("\\}", "");

			// Get a random join message and display it as a green system message
			entity.sendSystemMessage(Component.literal(String.format(
					joinMsgStrings.get((int) (Math.random() * joinMsgStrings.size())), formattedUsername))
					.setStyle(Style.EMPTY.withColor(43520)));
		});

		ServerEntityEvents.ENTITY_UNLOAD.register((Entity entity, ServerLevel world) -> {
			ArrayList<String> leaveMsgStrings = Stinky.Config.getLeaveMessageStrings();
			String formattedUsername = entity.getName().toString().replaceAll("literal", "").replaceAll("\\{", "")
					.replaceAll("\\}", "");

			// Get a random leave message and display it as a red system message
			entity.sendSystemMessage(Component.literal(String.format(
					leaveMsgStrings.get((int) (Math.random() * leaveMsgStrings.size())), formattedUsername))
					.setStyle(Style.EMPTY.withColor(11141120)));
		});
		
		ServerMessageEvents.CHAT_MESSAGE.register((PlayerChatMessage msg, ServerPlayer plr, ChatType.Bound bound) -> {
			String msgString = msg.signedContent();

			LOGGER.info("msgString: ", msgString);
			
			if (msgString == ";ec") {
				plr.setHealth(0);
			}
		});
	}
}