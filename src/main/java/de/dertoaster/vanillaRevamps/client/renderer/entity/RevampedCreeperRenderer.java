package de.dertoaster.vanillaRevamps.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.client.model.entity.RevampedCreeperModel;
import de.dertoaster.vanillaRevamps.client.renderer.RenderBipedBaseGeo;
import de.dertoaster.vanillaRevamps.client.renderer.layer.entity.RevampedCreeperArmorLayer;
import de.dertoaster.vanillaRevamps.entity.RevampedCreeper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.core.util.Color;

public class RevampedCreeperRenderer extends RenderBipedBaseGeo<RevampedCreeper> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(VanillaRevampsMod.MODID, "textures/entity/creeper/creeper.png");
	private static final ResourceLocation ARMOR_TEXTURE = new ResourceLocation(VanillaRevampsMod.MODID, "textures/entity/creeper/creeper_armor.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(VanillaRevampsMod.MODID, "geo/entity/creeper.geo.json");
	
	private float currentInflation;

	public RevampedCreeperRenderer(Context renderManager) {
		super(renderManager, new RevampedCreeperModel(MODEL_RESLOC, TEXTURE, "creeper/creeper"));
		if(this.modelProvider instanceof RevampedCreeperModel rcm) {
			rcm.trySetInflationGetter(this::getCurrentInflation);
		}
		
		this.addLayer(new RevampedCreeperArmorLayer(this, TEXTURE_GETTER, MODEL_ID_GETTER, ARMOR_TEXTURE, (f) -> this.currentInflation = f));
	}
	
	public float getCurrentInflation() {
		return this.currentInflation;
	}
	
	@Override
	public Color getRenderColor(RevampedCreeper animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn) {
		Color sr = super.getRenderColor(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn);
		if(animatable.getSwelling(partialTicks) != 0.0F) {
			float f = animatable.getSwelling(partialTicks);
		    f = (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
		    
		    f = 1 - f;
		    return Color.ofRGB(f * 0.9F + 0.1F, 0, 0);
		}
		return sr;
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, RevampedCreeper currentEntity) {
		switch(boneName) {
		case "armorBody":
			this.currentArmorAtBone = chestplate;
			this.currentArmorPartAtBone = GET_ARMOR_BODY;
			this.currentSlotAtBone = EquipmentSlot.CHEST;
			break;
		case "armorHead":
			this.currentArmorAtBone = helmet;
			this.currentArmorPartAtBone = GET_ARMOR_HEAD;
			this.currentSlotAtBone = EquipmentSlot.HEAD;
			break;
		case "armorFootLeft1":
		case "armorFootLeft2":
			this.currentArmorAtBone = boots;
			this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
			this.currentSlotAtBone = EquipmentSlot.FEET;
			break;
		case "armorFootRight1":
		case "armorFootRight2":
			this.currentArmorAtBone = boots;
			this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
			this.currentSlotAtBone = EquipmentSlot.FEET;
			break;
		case "armorLegLeft1":
		case "armorLegLeft2":
			this.currentArmorAtBone = leggings;
			this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
			this.currentSlotAtBone = EquipmentSlot.LEGS;
			break;
		case "armorLegRight1":
		case "armorLegRight2":
			this.currentArmorAtBone = leggings;
			this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
			this.currentSlotAtBone = EquipmentSlot.LEGS;
			break;
		default:
			break;
		}
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, RevampedCreeper currentEntity) {
		
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, RevampedCreeper currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, RevampedCreeper currentEntity) {
		
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, RevampedCreeper currentEntity) {
		
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, RevampedCreeper currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, RevampedCreeper currentEntity) {
		
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, RevampedCreeper currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(PoseStack matrixStack, ItemStack item, String boneName, RevampedCreeper currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, RevampedCreeper currentEntity, IBone bone) {
		
	}
	

	@Override
	public void render(RevampedCreeper entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
		this.currentInflation = 0.0F;
		float f = entity.getSwelling(partialTicks);
		float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
		f = Mth.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		stack.scale(f2, f3, f2);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}
	
}
