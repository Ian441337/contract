package ovh.glitchlabs.contract.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import ovh.glitchlabs.contract.Contract;
import ovh.glitchlabs.contract.items.ModItems;

public class ModItemProvidor extends ItemModelProvider {
    public ModItemProvidor(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Contract.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.CONTRACT.get());
        basicItem(ModItems.BLOOD_CONTRACT.get());
        basicItem(ModItems.DEATH_CONTRACT.get());
        basicItem(ModItems.ENDER_CONTRACT.get());
    }
}
