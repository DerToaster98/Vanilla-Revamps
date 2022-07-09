package de.dertoaster.vanillaRevamps.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.client.model.entity.RevampedCreeperModel;
import de.dertoaster.vanillaRevamps.client.renderer.RenderBipedBaseGeo;
import de.dertoaster.vanillaRevamps.entity.RevampedCreeper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RevampedCreeperRenderer extends RenderBipedBaseGeo<RevampedCreeper> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(VanillaRevampsMod.MODID, "textures/entity/creeper/creeper.png");
	private static final ResourceLocation ARMOR_TEXTURE = new ResourceLocation(VanillaRevampsMod.MODID, "textures/entity/creeper/creeper_armor.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(VanillaRevampsMod.MODID, "geo/entity/creeper.geo.json");
	private static final ResourceLocation MODEL_CHARGED_RESLOC = new ResourceLocation(VanillaRevampsMod.MODID, "geo/entity/creeper_charge_overlay.geo.json");
	
	private final boolean isEnergyRenderer;
	protected RevampedCreeperRenderer energyRenderer;
	
	public RevampedCreeperRenderer(Context renderManager) {
		super(renderManager, new RevampedCreeperModel(MODEL_RESLOC, TEXTURE, "creeper/creeper"));
		this.energyRenderer = new RevampedCreeperRenderer(renderManager, new RevampedCreeperModel(MODEL_CHARGED_RESLOC, ARMOR_TEXTURE, "creeper/creeper"), true);
		this.isEnergyRenderer = false;
		
		//this.addLayer(new RevampedCreeperArmorLayer(this, TEXTURE_GETTER, (e) -> MODEL_CHARGED_RESLOC, ARMOR_TEXTURE, CHARGED_MODEL));
	}
	
	private RevampedCreeperRenderer(Context renderManager, final AnimatedGeoModel<RevampedCreeper> model, final boolean isEnergyRenderer) {
		super(renderManager, model);
		this.isEnergyRenderer = isEnergyRenderer;
	}
	
	@Override
	public Color getRenderColor(RevampedCreeper animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn) {
		Color sr = super.getRenderColor(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn);
		if(this.isEnergyRenderer) {
			return sr;
		}
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
		if(this.isEnergyRenderer) {
			return;
		}
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
	public boolean shouldRender(RevampedCreeper pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
		return super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ) && (!this.isEnergyRenderer || (this.isEnergyRenderer && pLivingEntity.isPowered()));
	}
	

	@Override
	public void render(RevampedCreeper entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
		float f = entity.getSwelling(partialTicks);
		float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
		f = Mth.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		stack.scale(f2, f3, f2);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		if(this.energyRenderer != null && entity.isPowered()) {
			this.energyRenderer.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		}
	}
	
	@Override
	public void renderEarly(RevampedCreeper animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue,
			float partialTicks) {
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		if(this.energyRenderer != null && animatable.isPowered()) {
			this.energyRenderer.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		}
	}
	
	@Override
	public void renderLate(RevampedCreeper animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		super.renderLate(animatable, stackIn, ticks, renderTypeBuffer, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		if(this.energyRenderer != null && animatable.isPowered()) {
			this.energyRenderer.renderLate(animatable, stackIn, ticks, renderTypeBuffer, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		}
	}
	
	@Override
	public RenderType getRenderType(RevampedCreeper animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		if(!this.isEnergyRenderer) {
			return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
		}
		float f4 = (float) animatable.tickCount + partialTicks;
		
		return RenderType.energySwirl(ARMOR_TEXTURE, this.xOffset(f4) % 1.0F, f4 * 0.01F % 1.0F);
	}
	
	@Override
	public Integer getUniqueID(RevampedCreeper animatable) {
		Integer sr = super.getUniqueID(animatable);
		if(this.isEnergyRenderer) {
			sr++;
		}
		return sr;
	}
	
	private float xOffset(float f) {
		return f * 0.01F;
	}

	@Override
	protected boolean isArmorBone(GeoBone bone) {
		if(bone.getName().startsWith("armor")) {
			if(!bone.cubesAreHidden()) {
				bone.setCubesHidden(true);
			}
			return true;
		}
		return false;
	}

	@Override
	protected void preRenderBlock(PoseStack matrixStack, BlockState block, String boneName, RevampedCreeper currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack matrixStack, BlockState block, String boneName, RevampedCreeper currentEntity) {
		
	}
	
}
