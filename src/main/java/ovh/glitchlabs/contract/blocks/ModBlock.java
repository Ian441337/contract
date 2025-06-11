package ovh.glitchlabs.contract.blocks;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import ovh.glitchlabs.contract.Contract;
import ovh.glitchlabs.contract.items.ModItems;

import java.util.function.Supplier;

public class ModBlock {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Contract.MODID);

//    public static final DeferredBlock<Block> BISMUTH_BLOCK = registerBlock("bismuth_block",
//            () -> new Block(BlockBehaviour.Properties.of()
//                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
