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

    public static final DeferredItem<Item> SWAP_CONTRACT = ITEMS.register("swap_contract",
            () -> new SwapContractItem(new Item.Properties().stacksTo(1)));
    
    public static final DeferredItem<Item> CURSE_CONTRACT = ITEMS.register("curse_contract",
            () -> new CurseContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> LINK_CONTRACT = ITEMS.register("link_contract",
            () -> new LinkContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> FREEZE_CONTRACT = ITEMS.register("freeze_contract",
            () -> new FreezeContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BLIND_CONTRACT = ITEMS.register("blind_contract",
            () -> new BlindContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> POISON_CONTRACT = ITEMS.register("poison_contract",
            () -> new PoisonContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> HUNGER_CONTRACT = ITEMS.register("hunger_contract",
            () -> new HungerContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> LEVITATION_CONTRACT = ITEMS.register("levitation_contract",
            () -> new LevitationContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> GRAVITY_CONTRACT = ITEMS.register("gravity_contract",
            () -> new GravityContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> STORM_CONTRACT = ITEMS.register("storm_contract",
            () -> new StormContractItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> TIME_CONTRACT = ITEMS.register("time_contract",
            () -> new TimeContractItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}