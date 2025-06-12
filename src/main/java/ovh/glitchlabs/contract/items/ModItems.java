package ovh.glitchlabs.contract.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ovh.glitchlabs.contract.Contract;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Contract.MODID);

    public static final DeferredItem<Item> CONTRACT = ITEMS.register("contract",
            () -> new BloodContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BLOOD_CONTRACT = ITEMS.register("blood_contract",
            () -> new BloodContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> ENDER_CONTRACT = ITEMS.register("ender_contract",
            () -> new EnderContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> DEATH_CONTRACT = ITEMS.register("death_contract",
            () -> new DeathContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> REVENGE_CONTRACT = ITEMS.register("revenge_contract",
            () -> new RevengeContractItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}