package ovh.glitchlabs.contract.items;

import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.util.List;
import java.util.UUID;

public class LinkContractItem extends Item {
    public LinkContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (!isBound(stack)) {
                CompoundTag tag = new CompoundTag();
                tag.putString("BoundPlayer", player.getUUID().toString());
                tag.putString("BoundPlayerName", player.getName().getString());
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                
                player.sendSystemMessage(Component.literal("Link Contract bound to: " + player.getName().getString())
                        .withStyle(ChatFormatting.GOLD));
            } else {
                UUID boundUUID = getBoundPlayerUUID(stack);
                if (boundUUID != null && !boundUUID.equals(player.getUUID())) {
                    // Start damage linking
                    startDamageLinking(level, player, boundUUID);
                    stack.setCount(0);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private void startDamageLinking(Level level, Player activator, UUID boundUUID) {
        Player boundPlayer = level.getServer().getPlayerList().getPlayer(boundUUID);
        if (boundPlayer != null) {
            new Thread(() -> {
                try {
                    int duration = 30 * 20; // 30 seconds
                    float lastHealthActivator = activator.getHealth();
                    float lastHealthBound = boundPlayer.getHealth();

                    while (duration > 0) {
                        Thread.sleep(50);
                        duration--;

                        // Share damage between players
                        if (activator.getHealth() < lastHealthActivator) {
                            float damage = lastHealthActivator - activator.getHealth();
                            boundPlayer.hurt(boundPlayer.damageSources().magic(), damage / 2);
                        }
                        if (boundPlayer.getHealth() < lastHealthBound) {
                            float damage = lastHealthBound - boundPlayer.getHealth();
                            activator.hurt(activator.damageSources().magic(), damage / 2);
                        }

                        lastHealthActivator = activator.getHealth();
                        lastHealthBound = boundPlayer.getHealth();
                    }
                } catch (InterruptedException e) {
                    // Handle interruption
                }
            }).start();

            activator.sendSystemMessage(Component.literal("You are now linked with " + boundPlayer.getName().getString() + " for 30 seconds!")
                    .withStyle(ChatFormatting.GOLD));
            boundPlayer.sendSystemMessage(Component.literal("You are now linked with " + activator.getName().getString() + " for 30 seconds!")
                    .withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);

        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("BoundPlayer")) {
                String playerName = tag.getString("BoundPlayerName");
                tooltip.add(Component.literal("Bound to: " + playerName)
                        .withStyle(ChatFormatting.GOLD));
                tooltip.add(Component.literal("Right-click to link damage with bound player")
                        .withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(Component.literal("Not bound")
                        .withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.literal("Right click to bind")
                        .withStyle(ChatFormatting.YELLOW));
            }
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }

    private static UUID getBoundPlayerUUID(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("BoundPlayer")) {
                try {
                    return UUID.fromString(tag.getString("BoundPlayer"));
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private static boolean isBound(ItemStack stack) {
        return getBoundPlayerUUID(stack) != null;
    }
}
