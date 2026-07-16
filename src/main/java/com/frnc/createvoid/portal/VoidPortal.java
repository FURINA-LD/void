package com.frnc.createvoid.portal;

import com.frnc.createvoid.world.dimension.ModDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class VoidPortal extends Block {

    // 冷却时间（单位：游戏刻，20刻 = 1秒）
    private static final int COOLDOWN_TICKS = 60;

    public VoidPortal(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
                                          @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        // 只在服务端执行，且必须空手
        if (level.isClientSide || !player.getItemInHand(hand).isEmpty()) {
            return InteractionResult.PASS;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;

        // ---- 1. 冷却检查 ----
        CompoundTag persistentData = serverPlayer.getPersistentData();
        long lastTeleport = persistentData.getLong("lastVoidTeleport");
        long currentTime = level.getGameTime();
        if (currentTime - lastTeleport < COOLDOWN_TICKS) {
            long remainingTicks = COOLDOWN_TICKS - (currentTime - lastTeleport);
            serverPlayer.displayClientMessage(
                    Component.translatable("message.create_void.portal_cooldown", remainingTicks / 20.0), true);
            return InteractionResult.FAIL;
        }

        ServerLevel currentLevel = (ServerLevel) level;

        // ---- 2. 决定目标维度 ----
        boolean toVoid = !currentLevel.dimension().equals(ModDimension.VOID_LEVEL_KEY);
        ServerLevel targetLevel = toVoid ?
                currentLevel.getServer().getLevel(ModDimension.VOID_LEVEL_KEY) :
                currentLevel.getServer().getLevel(Level.OVERWORLD);

        if (targetLevel == null) {
            return InteractionResult.FAIL; // 维度不存在
        }

        // ---- 3. 计算目标坐标（使用玩家位置，限制在世界边界内） ----
        WorldBorder border = targetLevel.getWorldBorder();
        double clampedX = Math.max(border.getMinX() + 1, Math.min(border.getMaxX() - 1, player.getX()));
        double clampedZ = Math.max(border.getMinZ() + 1, Math.min(border.getMaxZ() - 1, player.getZ()));
        int x = (int) clampedX;
        int z = (int) clampedZ;
        int surfaceY = targetLevel.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        // 将传送门放置在目标维度地表处，玩家站在传送门上方
        BlockPos targetPos = new BlockPos(x, surfaceY, z);

        // ---- 4. 智能放置：只在目标位置没有 void_block 时才放置 ----
        BlockState existingState = targetLevel.getBlockState(targetPos);
        if (!existingState.is(this)) {
            targetLevel.setBlock(targetPos, defaultBlockState(), 3);
        }

        // ---- 5. 传送玩家 ----
        serverPlayer.teleportTo(targetLevel,
                targetPos.getX() + 0.5,
                targetPos.getY() + 1,
                targetPos.getZ() + 0.5,
                player.getYRot(),
                player.getXRot()
        );

        // ---- 6. 播放末影珍珠音效和粒子效果 ----
        // 在传送起点和终点各生成一次粒子，增强视觉反馈
        // 起点（当前方块位置）
        currentLevel.sendParticles(
                ParticleTypes.PORTAL,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                30, 0.5, 0.5, 0.5, 0.1
        );
        currentLevel.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);

        // 终点（目标位置）
        targetLevel.sendParticles(
                ParticleTypes.PORTAL,
                targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5,
                30, 0.5, 0.5, 0.5, 0.1
        );
        targetLevel.playSound(null, targetPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);

        // ---- 7. 更新冷却时间 ----
        persistentData.putLong("lastVoidTeleport", currentTime);

        return InteractionResult.SUCCESS;
    }
}