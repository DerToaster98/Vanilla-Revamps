package de.dertoaster.vanillaRevamps.client.model.entity;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.client.model.ModelGeoBase;
import de.dertoaster.vanillaRevamps.entity.RevampedCreeper;
import net.minecraft.resources.ResourceLocation;

public class RevampedCreeperModel extends ModelGeoBase<RevampedCreeper> {
	
	public RevampedCreeperModel(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}
	
	static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(VanillaRevampsMod.MODID, "animations/entity/creeper.animation.json");

	@Override
	public ResourceLocation getAnimationFileLocation(RevampedCreeper animatable) {
		return ANIMATION_RESLOC;
	}
	
}
