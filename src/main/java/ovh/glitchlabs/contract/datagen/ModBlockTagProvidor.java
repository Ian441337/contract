package ovh.glitchlabs.contract.datagen;

import net.minecraft.tags.BlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import ovh.glitchlabs.contract.Contract;
import ovh.glitchlabs.contract.items.ModItems;
import ovh.glitchlabs.contract.blocks.ModBlock;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvidor extends BlockTagsProvider {
    public ModBlockTagProvidor(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Contract.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE);
//                .add(ModBlock.STEEL_BLOCK.get());
        tag(BlockTags.NEEDS_IRON_TOOL);
//                .add(ModBlocks.STEEL_BLOCK.get());
    }

}
