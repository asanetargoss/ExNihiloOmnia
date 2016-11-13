package exnihiloomnia.registries.crucible;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.IRegistry;
import exnihiloomnia.registries.crucible.files.CrucibleRecipeLoader;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CrucibleRegistry implements IRegistry<CrucibleRegistryEntry> {
    public static CrucibleRegistry INSTANCE = new CrucibleRegistry();

    public static HashMap<String, CrucibleRegistryEntry> entries;

    public HashMap<String, CrucibleRegistryEntry> getEntries() {
        return entries;
    }

    public void initialize() {
        entries = new HashMap<String, CrucibleRegistryEntry>();

        if (ENORegistries.loadCrucibleDefaults)
            registerMeltables();

        List<CrucibleRegistryEntry> loaded = CrucibleRecipeLoader.load(ENO.path + File.separator + "registries" + File.separator + "crucible" + File.separator);

        if (loaded != null && !loaded.isEmpty()) {
            for (CrucibleRegistryEntry entry : loaded) {
                add(entry);
            }
        }
    }

    @Override
    public void clear() {
        entries = new HashMap<String, CrucibleRegistryEntry>();
    }

    public static void add(CrucibleRegistryEntry entry) {
        if (entry != null) {
            entries.put(entry.getKey(), entry);
        }
    }

    public static boolean containsItem(Block block, int meta) {
        return entries.containsKey(block + ":" + meta);
    }

    public static boolean containsFluid(Fluid fluid) {
        for (CrucibleRegistryEntry entry : entries.values()) {
            if (entry != null) {
                if (entry.getFluid() == fluid)
                    return true;
            }
        }
        
        return false;
    }

    public static boolean isMeltable(IBlockState state) {
        return getEntryForBlockState(state, EnumMetadataBehavior.SPECIFIC) != null || getEntryForBlockState(state, EnumMetadataBehavior.IGNORED) != null;
    }

    public static CrucibleRegistryEntry getEntryForBlockState(IBlockState state, EnumMetadataBehavior behavior) {
        if (behavior == EnumMetadataBehavior.SPECIFIC) {
            return entries.get(Block.REGISTRY.getNameForObject(state.getBlock()) + ":" + state.getBlock().getMetaFromState(state));
        }
        else {
            return entries.get(Block.REGISTRY.getNameForObject(state.getBlock())  + ":*");
        }
    }

    public static CrucibleRegistryEntry getItem(ItemStack item) {
        Block block = Block.getBlockFromItem(item.getItem());
        IBlockState state = block.getStateFromMeta(item.getMetadata());

        CrucibleRegistryEntry specific = getEntryForBlockState(state, EnumMetadataBehavior.SPECIFIC);
        CrucibleRegistryEntry generic = getEntryForBlockState(state, EnumMetadataBehavior.IGNORED);

        if (specific != null)
            return specific;
        else if (generic != null)
            return generic;
        else
            return null;
    }

    public static void registerMeltables() {
        new CrucibleRegistryEntry(Blocks.COBBLESTONE.getDefaultState(), EnumMetadataBehavior.IGNORED, 250, FluidRegistry.LAVA, 250);
        new CrucibleRegistryEntry(Blocks.STONE.getDefaultState(), EnumMetadataBehavior.IGNORED, 250, FluidRegistry.LAVA, 250);
        new CrucibleRegistryEntry(Blocks.GRAVEL.getDefaultState(), EnumMetadataBehavior.IGNORED, 250, FluidRegistry.LAVA, 250);
        new CrucibleRegistryEntry(Blocks.NETHERRACK.getDefaultState(), EnumMetadataBehavior.IGNORED, 250, FluidRegistry.LAVA, 1000);
        new CrucibleRegistryEntry(Blocks.MAGMA.getDefaultState(), EnumMetadataBehavior.IGNORED, 250, FluidRegistry.LAVA, 2000);

        new CrucibleRegistryEntry(Blocks.SNOW.getDefaultState(), EnumMetadataBehavior.IGNORED, 250, FluidRegistry.WATER, 250);
        new CrucibleRegistryEntry(Blocks.ICE.getDefaultState(), EnumMetadataBehavior.IGNORED, 250, FluidRegistry.WATER, 1000);
        new CrucibleRegistryEntry(Blocks.PACKED_ICE.getDefaultState(), EnumMetadataBehavior.IGNORED, 250, FluidRegistry.WATER, 2000);
    }
}
