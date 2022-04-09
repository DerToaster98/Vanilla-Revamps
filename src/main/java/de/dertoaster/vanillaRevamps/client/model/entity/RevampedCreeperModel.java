package de.dertoaster.vanillaRevamps.client.model.entity;

import java.util.function.Supplier;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.client.model.ModelGeoBase;
import de.dertoaster.vanillaRevamps.entity.RevampedCreeper;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;

public class RevampedCreeperModel extends ModelGeoBase<RevampedCreeper> {
	
	public Supplier<Float> INFLATION_GETTER = null;

	public RevampedCreeperModel(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}
	
	public void trySetInflationGetter(Supplier<Float> inflateSupplier) {
		if(this.INFLATION_GETTER == null) {
			this.INFLATION_GETTER = inflateSupplier;
		}
	}

	static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(VanillaRevampsMod.MODID, "animations/entity/creeper.animation.json");

	@Override
	public ResourceLocation getAnimationFileLocation(RevampedCreeper animatable) {
		return ANIMATION_RESLOC;
	}
	
	private static final String BONE_IDENT_ROOT = "root";
	
	@Override
	public void setLivingAnimations(RevampedCreeper entity, Integer uniqueID) {
		super.setLivingAnimations(entity, uniqueID);
		
		if(entity.isPowered()) {
			IBone rootBone = this.getAnimationProcessor().getBone(BONE_IDENT_ROOT);
			rootBone.setScaleX(1 + this.INFLATION_GETTER.get());
			rootBone.setScaleY(1 + this.INFLATION_GETTER.get());
			rootBone.setScaleZ(1 + this.INFLATION_GETTER.get());
		}
	}

	@Override
	protected float getModelInflateValue() {
		return this.INFLATION_GETTER.get();
	}

}
