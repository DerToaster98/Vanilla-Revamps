package de.dertoaster.vanillaRevamps.client.renderer.layer.entity;

import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import de.dertoaster.vanillaRevamps.client.renderer.layer.AbstractLayerGeo;
import de.dertoaster.vanillaRevamps.entity.RevampedCreeper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RevampedCreeperArmorLayer extends AbstractLayerGeo<RevampedCreeper> {

	protected final ResourceLocation ARMOR_TEXTURE;
	protected final Consumer<Float> INFLATION_SETTER;

	public RevampedCreeperArmorLayer(GeoEntityRenderer<RevampedCreeper> renderer, Function<RevampedCreeper, ResourceLocation> funcGetCurrentTexture, Function<RevampedCreeper, ResourceLocation> funcGetCurrentModel,
			final ResourceLocation armorTexture, final Consumer<Float> inflationSetter) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
		this.INFLATION_SETTER = inflationSetter;
		this.ARMOR_TEXTURE = armorTexture;
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, RevampedCreeper entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entityLivingBaseIn.isPowered()) {
			float f = (float) entityLivingBaseIn.tickCount + partialTicks;
			
			INFLATION_SETTER.accept(5F);
			
			this.reRenderCurrentModelInRenderer(
					entityLivingBaseIn, 
					partialTicks, 
					matrixStackIn, 
					bufferIn, 
					packedLightIn, 
					RenderType.energySwirl(ARMOR_TEXTURE, this.xOffset(f) % 1.0F, f * 0.01F % 1.0F)
			);
		}
	}

	private float xOffset(float f) {
		return f * 0.01F;
	}

}
