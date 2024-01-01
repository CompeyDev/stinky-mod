package xyz.devcomp.mixin;

import java.util.Map;

import xyz.devcomp.Stinky;
import xyz.devcomp.util.Strings.DeathStrings;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "die(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At("TAIL"))
    private void broadcastDeathMessage(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayer victim = (ServerPlayer) (Object) this;
        MinecraftServer server = this.getServer();

        if (damageSource.getDirectEntity() instanceof ServerPlayer aggressor) {
            if (aggressor == victim) {
                // suicide balls

                server.sendSystemMessage(
                        Component.literal(DeathStrings.Suicide[(int) (Math.random() * DeathStrings.Suicide.length)]));

                return;
            }

            Map<ResourceKey<DamageType>, String[]> relevantTypes = Map.of(
                    DamageTypes.ARROW, DeathStrings.Arrow,
                    DamageTypes.PLAYER_ATTACK, DeathStrings.Melee,
                    DamageTypes.MAGIC, DeathStrings.Potion,
                    DamageTypes.PLAYER_EXPLOSION, DeathStrings.Explosion);

            for (Map.Entry<ResourceKey<DamageType>, String[]> type : relevantTypes.entrySet()) {
                ResourceKey<DamageType> damageType = type.getKey();
                String[] msgs = type.getValue();

                if (damageSource.is(damageType)) {
                    server.getPlayerList().broadcastSystemMessage(Component.literal(msgs[(int) (Math.random() * msgs.length)]
                            .replace("player", victim.getDisplayName().getString())
                            .replace("killer", aggressor.getDisplayName().getString()) // check if null
                            .replace("weapon", (damageSource.getDirectEntity().getName() == null ? "" : damageSource.getDirectEntity().getName().getString()))), false);
                }
            }
            ;
        } else {
            // these death msgs dont work :(
            Stinky.LOGGER.info("killer: {}, weapon: {}", damageSource.getDirectEntity().getName().getString(), damageSource.getEntity().getName().getString());

            Map<ResourceKey<DamageType>, String[]> messageMappings = Map.ofEntries(
                    Map.entry(DamageTypes.FALL, DeathStrings.FallDamage),
                    Map.entry(DamageTypes.ON_FIRE, DeathStrings.Burned),
                    Map.entry(DamageTypes.IN_FIRE, DeathStrings.Burned),
                    Map.entry(DamageTypes.DROWN, DeathStrings.Drowned),
                    Map.entry(DamageTypes.IN_WALL, DeathStrings.Suffocation),
                    Map.entry(DamageTypes.MOB_ATTACK, DeathStrings.Mob),
                    Map.entry(DamageTypes.MOB_PROJECTILE, DeathStrings.Mob),
                    Map.entry(DamageTypes.MOB_ATTACK_NO_AGGRO, DeathStrings.Mob),
                    Map.entry(DamageTypes.GENERIC, DeathStrings.Wildcard),
                    Map.entry(DamageTypes.EXPLOSION, DeathStrings.Explosion),
                    Map.entry(DamageTypes.STARVE, DeathStrings.Starved));

            for (Map.Entry<ResourceKey<DamageType>, String[]> type : messageMappings.entrySet()) {
                ResourceKey<DamageType> damageType = type.getKey();
                String[] msgs = type.getValue();

                int idx = (int) (Math.random() * msgs.length);

                if (damageSource.is(damageType)) {
                    server.getPlayerList().broadcastSystemMessage(Component.literal(
                            msgs[idx]
                                    .replace("player", victim.getDisplayName().getString())
                                    .replace("killer", damageSource.getEntity().getName().getString())
                                    .replace("weapon", damageSource.getDirectEntity().getName().getString())), false);
                }
            }
            ;
        }
    }
}
