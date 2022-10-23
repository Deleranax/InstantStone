package fr.dwightstudio.instantstone;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(InstantStone.MODID)
@Mod.EventBusSubscriber
public class InstantStone {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "instantstone";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "InstantStone" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "InstantStone" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    // BLOCKS & ITEMS
    // Repeater
    public static final RegistryObject<Block> INSTANT_REPEATER = BLOCKS.register("instant_repeater", InstantRepeaterBlock::new);

    public static final RegistryObject<Item> INSTANT_REPEATOR_ITEM = ITEMS.register("instant_repeater", () -> new BlockItem(INSTANT_REPEATER.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));

    // Comparator
    public static final RegistryObject<Block> INSTANT_COMPARATOR = BLOCKS.register("instant_comparator", InstantComparatorBlock::new);

    public static final RegistryObject<Item> INSTANT_COMPARATOR_ITEM = ITEMS.register("instant_comparator", () -> new BlockItem(INSTANT_COMPARATOR.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));

    // Torch
    public static final RegistryObject<Block> INSTANT_REDSTONE_TORCH = BLOCKS.register("instant_redstone_torch", InstantRedstoneTorchBlock::new);
    public static final RegistryObject<Block> INSTANT_REDSTONE_WALL_TORCH = BLOCKS.register("instant_redstone_wall_torch", InstantRedstoneWallTorchBlock::new);

    public static final RegistryObject<Item> INSTANT_REDSTONE_TORCH_ITEM = ITEMS.register("instant_redstone_torch", () -> new StandingAndWallBlockItem(INSTANT_REDSTONE_TORCH.get(), INSTANT_REDSTONE_WALL_TORCH.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_REDSTONE)));



    public InstantStone() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(InstantStone.class);
    }
}
