package ovh.glitchlabs.contract.events;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import ovh.glitchlabs.contract.Contract;
import ovh.glitchlabs.contract.items.BloodContractItem;
import ovh.glitchlabs.contract.items.ModItems;
import ovh.glitchlabs.contract.items.RevengeContractItem;

import java.util.UUID;

@EventBusSubscriber(modid = Contract.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onEntityAttacked(LivingIncomingDamageEvent event) {
        if (event.getEntity() == null || event.getSource() == null) return;

        // Blood contract logic
        if (event.getSource().getEntity() instanceof Player attacker && event.getEntity() instanceof Player defender) {
            try {
                // Check if defender has blood contract that protects against attacker
                boolean hasProtection = defender.getInventory().items.stream()
                        .filter(stack -> stack != null && stack.getItem() == ModItems.BLOOD_CONTRACT.get())
                        .anyMatch(stack -> {
                            UUID boundPlayerUUID = BloodContractItem.getBoundPlayerUUID(stack);
                            return boundPlayerUUID != null && boundPlayerUUID.equals(attacker.getUUID());
                        });

                if (hasProtection) {
                    event.setCanceled(true);
                    return;
                }
            } catch (Exception e) {
                // Log error but don't crash
                System.err.println("Error in blood contract logic: " + e.getMessage());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player deadPlayer) {
            // Check if dead player has revenge contract in inventory
            deadPlayer.getInventory().items.stream()
                .filter(stack -> stack.getItem() == ModItems.REVENGE_CONTRACT.get())
                .findFirst()
                .ifPresent(contract -> {
                    UUID boundPlayerUUID = RevengeContractItem.getBoundPlayerUUID(contract);
                    if (boundPlayerUUID != null) {
                        Player boundPlayer = deadPlayer.level().getServer().getPlayerList().getPlayer(boundPlayerUUID);
                        if (boundPlayer != null) {
                            // Give strength effect to bound player
                            boundPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0)); // 20 seconds
                            contract.setCount(0); // Remove the contract
                        }
                    }
                });
        }
    }
}