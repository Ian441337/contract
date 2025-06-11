package ovh.glitchlabs.contract.datagen;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import ovh.glitchlabs.contract.Contract;

public class ModBlockStateProvidor extends BlockStateProvider {
    public ModBlockStateProvidor(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Contract.MODID, exFileHelper);
    }


    @Override
    protected void registerStatesAndModels() {
//        BlockWithItem(ModBlocks.STEEL_BLOCK);

    }

    private void BlockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
