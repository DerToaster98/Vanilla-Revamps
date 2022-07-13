package de.dertoaster.vanillaRevamps.client.init;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.client.model.entity.ModelRevampedSlime;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = VanillaRevampsMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class VRModelLayers {
	
	public static final ModelLayerLocation REVAMPED_SLIME_LAYER_LOCATION = new ModelLayerLocation(VanillaRevampsMod.prefix("modellayer/slime/normal"), "revamped_slime"); 

	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(REVAMPED_SLIME_LAYER_LOCATION, ModelRevampedSlime::createLayer);
	}
	
}
