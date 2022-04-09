package de.dertoaster.vanillaRevamps.client.model;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.entity.ITextureVariants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public abstract class ModelGeoBase<T extends LivingEntity & IAnimatable & IAnimationTickable> extends AnimatedTickingGeoModel<T> {

	protected final ResourceLocation MODEL_RESLOC;
	protected final ResourceLocation TEXTURE_DEFAULT;
	protected final String ENTITY_REGISTRY_PATH_NAME;

	protected ResourceLocation[] textureVariantCache = null;

	public ModelGeoBase(ResourceLocation model, ResourceLocation textureDefault, final String entityName) {
		super();
		this.MODEL_RESLOC = model;
		this.TEXTURE_DEFAULT = textureDefault;
		this.ENTITY_REGISTRY_PATH_NAME = entityName;
	}

	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		// Custom texture end
		if (entity instanceof ITextureVariants) {
			if (((ITextureVariants) entity).getTextureCount() > 1) {
				if (this.textureVariantCache == null) {
					this.textureVariantCache = new ResourceLocation[((ITextureVariants) entity).getTextureCount()];
				}
				final int index = ((ITextureVariants) entity).getTextureIndex();
				if (this.textureVariantCache[index] == null) {
					this.textureVariantCache[index] = new ResourceLocation(VanillaRevampsMod.MODID, "textures/entity/" + this.ENTITY_REGISTRY_PATH_NAME + "_" + index + ".png");
				}
				return this.textureVariantCache[index];
			}
		}
		return this.TEXTURE_DEFAULT;
	}

	@Override
	public ResourceLocation getModelLocation(T object) {
		return this.MODEL_RESLOC;
	}
	
	protected abstract float getModelInflateValue();

}
