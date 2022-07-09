package de.dertoaster.vanillaRevamps.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.client.model.ModelRevampedSlime;
import de.dertoaster.vanillaRevamps.entity.RevampedSlime;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RevampedSlimeRenderer extends MobRenderer<RevampedSlime, ModelRevampedSlime<RevampedSlime>>{

	public RevampedSlimeRenderer(Context p_174304_, ModelRevampedSlime<RevampedSlime> p_174305_, float p_174306_) {
		super(p_174304_, p_174305_, p_174306_);
	}

	@Override
	public ResourceLocation getTextureLocation(RevampedSlime pEntity) {
		return VanillaRevampsMod.prefixEntityTexture("slime/revamped_slime.png");
	}
	
	@Override
	protected void scale(RevampedSlime pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
		pMatrixStack.scale(pLivingEntity.getScale(), pLivingEntity.getScale(), pLivingEntity.getScale());
	}

}
