package de.dertoaster.vanillaRevamps.client.renderer;

import java.util.function.Function;

import javax.annotation.Nullable;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.entity.ISizable;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public abstract class RenderEntityGeo<T extends LivingEntity & IAnimatable> extends ExtendedGeoEntityRenderer<T> {

	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(VanillaRevampsMod.MODID, "textures/entity/magic_armor/mages.png");

	public final Function<T, ResourceLocation> TEXTURE_GETTER;
	public final Function<T, ResourceLocation> MODEL_ID_GETTER;

	public RenderEntityGeo(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1F, 1F, 0);
	}
	
	@SuppressWarnings("resource")
	protected void bindTexture(ResourceLocation textureLocation) {
		//Minecraft.getInstance().textureManager.bindForSetup(textureLocation);
	}

	protected RenderEntityGeo(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider, float widthScale, float heightScale, float shadowSize) {
		super(renderManager, modelProvider);

		this.MODEL_ID_GETTER = modelProvider::getModelResource;
		this.TEXTURE_GETTER = modelProvider::getTextureResource;

		this.shadowRadius = shadowSize;
		this.widthScale = widthScale;
		this.heightScale = heightScale;

		// layers
		//this.addLayer(new LayerMagicArmorGeo<T>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
	}

	@Override
	protected float getWidthScale(T entity) {
		if(entity instanceof ISizable) {
			return this.widthScale * ((ISizable) entity).getSizeVariation();
		}
		return this.widthScale;
	}

	@Override
	protected float getHeightScale(T entity) {
		if(entity instanceof ISizable) {
			return this.heightScale * ((ISizable) entity).getSizeVariation();
		}
		return this.heightScale;
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return this.TEXTURE_GETTER.apply(entity);
	}

	/*
	 * Return null if there is no item
	 */
	@Nullable
	protected abstract ItemStack getHeldItemForBone(String boneName, T currentEntity);

	protected abstract TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName);

	/*
	 * Return null if there is no held block
	 */
	@Nullable
	protected abstract BlockState getHeldBlockForBone(String boneName, T currentEntity);

	protected abstract void preRenderItem(ItemStack item, String boneName, T currentEntity);

	protected abstract void preRenderBlock(BlockState block, String boneName, T currentEntity);

	protected abstract void postRenderItem(ItemStack item, String boneName, T currentEntity);

	protected abstract void postRenderBlock(BlockState block, String boneName, T currentEntity);

	/*
	 * Return null, if the entity's texture is used
	 */
	@Nullable
	protected abstract ResourceLocation getTextureForBone(String boneName, T currentEntity);

}
