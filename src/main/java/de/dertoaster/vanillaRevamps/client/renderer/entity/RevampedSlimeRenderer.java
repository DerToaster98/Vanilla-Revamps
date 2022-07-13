package de.dertoaster.vanillaRevamps.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.client.init.VRModelLayers;
import de.dertoaster.vanillaRevamps.client.model.entity.ModelRevampedSlime;
import de.dertoaster.vanillaRevamps.entity.RevampedSlime;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RevampedSlimeRenderer extends MobRenderer<RevampedSlime, ModelRevampedSlime<RevampedSlime>>{

	public RevampedSlimeRenderer(Context renderManager) {
		super(renderManager, new ModelRevampedSlime<>(renderManager.bakeLayer(VRModelLayers.REVAMPED_SLIME_LAYER_LOCATION)), 0);
	}
	
	static final ResourceLocation TEXTURE = VanillaRevampsMod.prefixEntityTexture("slime/revamped_slime.png"); 

	@Override
	public ResourceLocation getTextureLocation(RevampedSlime pEntity) {
		return TEXTURE;
	}
	
	@Override
	protected void scale(RevampedSlime pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
		pMatrixStack.scale(pLivingEntity.getScale(), pLivingEntity.getScale(), pLivingEntity.getScale());
	}
	
	@Override
	public void render(RevampedSlime pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}
	
	@Override
	protected RenderType getRenderType(RevampedSlime p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
		return RenderType.entityTranslucent(TEXTURE);
	}

}
