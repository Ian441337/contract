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
        basicItem(ModItems.FREEZE_CONTRACT.get());
        basicItem(ModItems.GRAVITY_CONTRACT.get());
        basicItem(ModItems.POISON_CONTRACT.get());
        basicItem(ModItems.CURSE_CONTRACT.get());
        basicItem(ModItems.LINK_CONTRACT.get());
        basicItem(ModItems.HUNGER_CONTRACT.get());
        basicItem(ModItems.LEVITATION_CONTRACT.get());
        basicItem(ModItems.STORM_CONTRACT.get());
        basicItem(ModItems.SWAP_CONTRACT.get());
        basicItem(ModItems.TIME_CONTRACT.get());
        basicItem(ModItems.REVENGE_CONTRACT.get());
        basicItem(ModItems.BLIND_CONTRACT.get());
    }
}
