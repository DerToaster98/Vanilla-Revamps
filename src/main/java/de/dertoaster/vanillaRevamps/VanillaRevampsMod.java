package de.dertoaster.vanillaRevamps;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import de.dertoaster.vanillaRevamps.entity.goal.AvoidExplodingCreeperGoal;
import de.dertoaster.vanillaRevamps.entity.goal.AvoidExplodingCreeperGoal.INotAfraidOfExplodingCreepers;
import de.dertoaster.vanillaRevamps.init.VREntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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
	
	@SubscribeEvent
	protected void onEntityJoin(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		if(entity == null) {
			return;
		}
		if(entity instanceof PathfinderMob && !(entity instanceof INotAfraidOfExplodingCreepers)) {
			PathfinderMob monster = (PathfinderMob)entity;
			monster.goalSelector.addGoal(3, new AvoidExplodingCreeperGoal(monster, 9.0F, 1.2, 1.5));
		}
	}

	public static ResourceLocation prefix(String entityName) {
		return new ResourceLocation(MODID, entityName);
	}

	public static ResourceLocation prefixEntityTexture(String string) {
		return prefix("textures/entity/" + string);
	}
}
