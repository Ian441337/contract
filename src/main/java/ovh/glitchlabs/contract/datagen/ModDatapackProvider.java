package ovh.glitchlabs.contract.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import ovh.glitchlabs.contract.Contract;
import ovh.glitchlabs.contract.worldgen.ModBiomeModifiers;
import ovh.glitchlabs.contract.worldgen.ModConfiguredFeatures;
import ovh.glitchlabs.contract.worldgen.ModPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
    .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
    .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);


    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Contract.MODID));
    }
}
