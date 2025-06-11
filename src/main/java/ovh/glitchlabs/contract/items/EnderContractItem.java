package ovh.glitchlabs.contract.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class EnderContractItem extends Item {
    public EnderContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (!isBound(stack)) {
                // Bind contract to player
                CompoundTag tag = new CompoundTag();
                tag.putString("BoundPlayer", player.getUUID().toString());
                tag.putString("BoundPlayerName", player.getName().getString());
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                player.sendSystemMessage(Component.literal("Ender Contract bound to: " + player.getName().getString())
                        .withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                UUID boundUUID = getBoundPlayerUUID(stack);
                if (boundUUID != null) {
                    Player boundPlayer = level.getServer().getPlayerList().getPlayer(boundUUID);
                    PlayerEnderChestContainer enderChest;
                    String playerName;
                    final Path[] playerFilePath = {null}; // Use array to make it effectively final

                    if (boundPlayer != null) {
                        // Online player
                        enderChest = boundPlayer.getEnderChestInventory();
                        playerName = boundPlayer.getName().getString();
                    } else {
                        // Offline player
                        try {
                            MinecraftServer server = level.getServer();
                            Path playerDataDir = server.getWorldPath(LevelResource.PLAYER_DATA_DIR);
                            playerFilePath[0] = playerDataDir.resolve(boundUUID.toString() + ".dat");

                            if (java.nio.file.Files.exists(playerFilePath[0])) {
                                CompoundTag playerData = NbtIo.readCompressed(playerFilePath[0], NbtAccounter.unlimitedHeap());
                                if (playerData != null) {
                                    enderChest = new PlayerEnderChestContainer();
                                    if (playerData.contains("EnderItems", 9)) {
                                        ListTag enderItems = playerData.getList("EnderItems", 10);
                                        enderChest.fromTag(enderItems, server.registryAccess());
                                    }
                                    playerName = stack.get(DataComponents.CUSTOM_DATA).copyTag().getString("BoundPlayerName");
                                } else {
                                    player.sendSystemMessage(Component.literal("Player data not found!")
                                            .withStyle(ChatFormatting.RED));
                                    return InteractionResultHolder.fail(stack);
                                }
                            } else {
                                player.sendSystemMessage(Component.literal("Player has never been found on the server!")
                                        .withStyle(ChatFormatting.RED));
                                return InteractionResultHolder.fail(stack);
                            }
                        } catch (Exception e) {
                            player.sendSystemMessage(Component.literal("Error loading Enderchest!")
                                    .withStyle(ChatFormatting.RED));
                            e.printStackTrace();
                            return InteractionResultHolder.fail(stack);
                        }
                    }

                    // Store final references for lambda
                    final PlayerEnderChestContainer finalEnderChest = enderChest;
                    final MinecraftServer finalServer = level.getServer();

                    player.openMenu(new SimpleMenuProvider(
                            (id, playerInv, playerEntity) -> {
                                ChestMenu menu = ChestMenu.threeRows(id, playerInv, finalEnderChest);

                                if (playerFilePath[0] != null) {
                                    menu.addSlotListener(new ContainerListener() {
                                        @Override
                                        public void slotChanged(AbstractContainerMenu containerMenu, int slotId, ItemStack stack) {
                                            saveEnderChest(finalEnderChest, playerFilePath[0], finalServer);
                                        }

                                        @Override
                                        public void dataChanged(AbstractContainerMenu container, int id, int value) {
                                            // Not needed for this implementation
                                        }
                                    });
                                }

                                return menu;
                            },
                            Component.literal(playerName + "'s Enderchest")
                    ));
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private void saveEnderChest(PlayerEnderChestContainer enderChest, Path playerFile, MinecraftServer server) {
        try {
            CompoundTag playerData = NbtIo.readCompressed(playerFile, NbtAccounter.unlimitedHeap());
            if (playerData != null) {
                playerData.put("EnderItems", enderChest.createTag(server.registryAccess()));
                NbtIo.writeCompressed(playerData, playerFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);

        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("BoundPlayer")) {
                String playerName = tag.getString("BoundPlayerName");
                tooltip.add(Component.literal("Connected with: " + playerName)
                        .withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(Component.literal("Right-click to open the Enderchest")
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