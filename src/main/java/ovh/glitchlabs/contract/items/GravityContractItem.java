package ovh.glitchlabs.contract.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class GravityContractItem extends Item {
    public GravityContractItem(Properties properties) {
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
                
                player.sendSystemMessage(Component.literal("Gravity Contract bound to: " + player.getName().getString())
                        .withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                UUID boundUUID = getBoundPlayerUUID(stack);
                if (boundUUID != null) {
                    Player boundPlayer = level.getServer().getPlayerList().getPlayer(boundUUID);
                    if (boundPlayer != null && boundPlayer != player) {
                        // Reverse gravity with levitation effect
                        boundPlayer.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 1)); // 10 seconds
                        stack.setCount(0); // Consume the contract
                        
                        player.sendSystemMessage(Component.literal("You reversed gravity for " + boundPlayer.getName().getString())
                                .withStyle(ChatFormatting.DARK_PURPLE));
                        boundPlayer.sendSystemMessage(Component.literal("Your gravity has been reversed by " + player.getName().getString())
                                .withStyle(ChatFormatting.DARK_PURPLE));
                    }
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);

        if (customData != null && customData.copyTag().contains("BoundPlayer")) {
            String playerName = customData.copyTag().getString("BoundPlayerName");
            tooltip.add(Component.literal("Bound to: " + playerName)
                    .withStyle(ChatFormatting.DARK_PURPLE));
            tooltip.add(Component.literal("Right-click to reverse gravity for bound player")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.literal("Not bound")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Right click to bind")
                    .withStyle(ChatFormatting.YELLOW));
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
