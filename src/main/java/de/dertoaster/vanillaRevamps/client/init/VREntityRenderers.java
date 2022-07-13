package de.dertoaster.vanillaRevamps.client.init;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.client.renderer.entity.RevampedSlimeRenderer;
import de.dertoaster.vanillaRevamps.client.renderer.entity.geo.RevampedCreeperRenderer;
import de.dertoaster.vanillaRevamps.init.VREntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = VanillaRevampsMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class VREntityRenderers {

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(VREntityTypes.CREEPER.get(), RevampedCreeperRenderer::new);
		event.registerEntityRenderer(VREntityTypes.SLIME.get(), RevampedSlimeRenderer::new);
	}

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
	}

	@SubscribeEvent
	public static void registerRenderers(final FMLClientSetupEvent event) {
	}

}
