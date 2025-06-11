package ovh.glitchlabs.contract.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class BloodContractItem extends Item {
    public BloodContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            CompoundTag tag = new CompoundTag();
            tag.putString("BoundPlayer", player.getUUID().toString());
            tag.putString("BoundPlayerName", player.getName().getString());

            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

            player.sendSystemMessage(Component.literal("Contract has been bound to " + player.getName().getString())
                    .withStyle(ChatFormatting.GREEN));
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);

        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("BoundPlayer")) {
                String playerName = tag.getString("BoundPlayerName");
                tooltip.add(Component.literal("Bound to: " + playerName)
                        .withStyle(ChatFormatting.DARK_RED));
            } else {
                tooltip.add(Component.literal("Not bound")
                        .withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.literal("Right click to bind")
                        .withStyle(ChatFormatting.YELLOW));
            }
        } else {
            tooltip.add(Component.literal("Not bound")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Right click to bind")
                    .withStyle(ChatFormatting.YELLOW));
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }

    public static UUID getBoundPlayerUUID(ItemStack stack) {
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

    public static String getBoundPlayerName(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("BoundPlayerName")) {
                return tag.getString("BoundPlayerName");
            }
        }
        return null;
    }

    public static boolean isBound(ItemStack stack) {
        return getBoundPlayerUUID(stack) != null;
    }

    public static boolean isBoundToPlayer(ItemStack stack, Player player) {
        UUID boundUUID = getBoundPlayerUUID(stack);
        return boundUUID != null && boundUUID.equals(player.getUUID());
    }
}
