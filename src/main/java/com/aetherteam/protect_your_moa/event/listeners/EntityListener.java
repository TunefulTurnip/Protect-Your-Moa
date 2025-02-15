package com.aetherteam.protect_your_moa.event.listeners;

import com.aetherteam.protect_your_moa.ProtectYourMoa;
import com.aetherteam.protect_your_moa.event.hooks.EntityHooks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = ProtectYourMoa.MODID)
public class EntityListener {
    @SubscribeEvent
    public static void onInteractWithEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        Entity targetEntity = event.getTarget();
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        Optional<InteractionResult> result = EntityHooks.interactionOpenMoaInventory(targetEntity, player);
        if (result.isEmpty()) {
            result = EntityHooks.equipMoaArmor(targetEntity, player, itemStack);
        }
        if (result.isEmpty()) {
            result = EntityHooks.equipMoaChest(targetEntity, player, itemStack);
        }
        result.ifPresent(event::setCancellationResult);
        event.setCanceled(result.isPresent());
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        EntityHooks.onJoinLevel(entity);
    }

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        Entity entity = event.getEntity();
        EntityHooks.onUpdate(entity);
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        Entity entity = event.getEntity();
        Collection<ItemEntity> drops = event.getDrops();
        EntityHooks.onDrops(entity, drops);
    }
}
