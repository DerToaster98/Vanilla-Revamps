package de.dertoaster.vanillaRevamps;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import de.dertoaster.vanillaRevamps.init.VREntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(VanillaRevampsMod.MODID)
public class VanillaRevampsMod {
	public static final String MODID = "vanillarevamps";
	
	// Directly reference a slf4j logger
	public static final Logger LOGGER = LogUtils.getLogger();

	public VanillaRevampsMod() {
		GeckoLib.initialize();
		IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
		
		VREntityTypes.registerEntityTypes();
		MinecraftForge.EVENT_BUS.register(new VREntityTypes.EventHandler());
		
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
	}


	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// Do something when the server starts
	}

	// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
			// Register a new block here
		}
	}

	public static ResourceLocation prefix(String entityName) {
		return new ResourceLocation(MODID, entityName);
	}
}
