package de.dertoaster.vanillaRevamps.client.renderer;

import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.ARMOR_BODY;
import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.ARMOR_HEAD;
import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.ARMOR_LEFT_ARM;
import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.ARMOR_LEFT_FOOT;
import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.ARMOR_LEFT_LEG;
import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.ARMOR_RIGHT_ARM;
import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.ARMOR_RIGHT_FOOT;
import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.ARMOR_RIGHT_LEG;
import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.LEFT_HAND;
import static de.dertoaster.vanillaRevamps.client.renderer.StandardBipedBones.RIGHT_HAND;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public abstract class RenderBipedBaseGeo<T extends LivingEntity & IAnimatable> extends RenderEntityGeo<T> {

	public RenderBipedBaseGeo(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1.0F, 1.0F);
	}
	
	protected RenderBipedBaseGeo(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider, final float widthScale, final float heightScale) {
		super(renderManager, modelProvider, widthScale, heightScale, widthScale / 2);
	}

	protected ItemStack currentItemAtBone = null;
	protected TransformType currentTransformTypeAtBone = null;
	
	protected ItemStack currentArmorAtBone = null;
	protected EquipmentSlot currentSlotAtBone = null;
	protected Function<HumanoidModel<?>, ModelPart> currentArmorPartAtBone = null;
	
	protected final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_HEAD = (armorModel) -> armorModel.head;
	protected final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_BODY = (armorModel) -> armorModel.body;
	protected final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_ARM_LEFT = (armorModel) -> armorModel.leftArm;
	protected final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_ARM_RIGHT = (armorModel) -> armorModel.rightArm;
	protected final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_LEG_LEFT = (armorModel) -> armorModel.leftLeg;
	protected final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_LEG_RIGHT = (armorModel) -> armorModel.rightLeg;
	
	//Call order (armor): 
	/**
	 * 1) getArmorForBone
	 * 2) getEquipmentSlotForArmorBone
	 * 3) getArmorPartForBone
	 */
	
	//Call order (items):
	/**
	 * 1) getHeldItemForBone
	 * 2) getCameraTransformForItemAtBone
	 */
	
	protected abstract void calculateArmorStuffForBone(String boneName, T currentEntity);
	protected abstract void calculateItemStuffForBone(String boneName, T currentEntity);
	
	protected void standardArmorCalculationForBone(String boneName, T currentEntity) {
		switch(boneName) {
			case ARMOR_BODY:
				this.currentArmorAtBone = chestplate;
				this.currentArmorPartAtBone = GET_ARMOR_BODY;
				this.currentSlotAtBone = EquipmentSlot.CHEST;
				break;
			case ARMOR_HEAD:
				this.currentArmorAtBone = helmet;
				this.currentArmorPartAtBone = GET_ARMOR_HEAD;
				this.currentSlotAtBone = EquipmentSlot.HEAD;
				break;
			case ARMOR_LEFT_ARM:
				this.currentArmorAtBone = chestplate;
				this.currentArmorPartAtBone = GET_ARMOR_ARM_LEFT;
				this.currentSlotAtBone = EquipmentSlot.CHEST;
				break;
			case ARMOR_RIGHT_ARM:
				this.currentArmorAtBone = chestplate;
				this.currentArmorPartAtBone = GET_ARMOR_ARM_RIGHT;
				this.currentSlotAtBone = EquipmentSlot.CHEST;
				break;
			case ARMOR_LEFT_FOOT:
				this.currentArmorAtBone = boots;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
				this.currentSlotAtBone = EquipmentSlot.FEET;
				break;
			case ARMOR_RIGHT_LEG:
				this.currentArmorAtBone = boots;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
				this.currentSlotAtBone = EquipmentSlot.FEET;
				break;
			case ARMOR_LEFT_LEG:
				this.currentArmorAtBone = leggings;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
				this.currentSlotAtBone = EquipmentSlot.LEGS;
				break;
			case ARMOR_RIGHT_FOOT:
				this.currentArmorAtBone = leggings;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
				this.currentSlotAtBone = EquipmentSlot.LEGS;
				break;
			default:
				break;
		}
	}
	
	protected void standardItemCalculationForBone(String boneName, T currentEntity) {
		switch(boneName) {
			case LEFT_HAND:
				this.currentItemAtBone = this.offHand;
				this.currentTransformTypeAtBone = TransformType.THIRD_PERSON_RIGHT_HAND;
				break;
			case RIGHT_HAND:
				this.currentItemAtBone = this.mainHand;
				this.currentTransformTypeAtBone = TransformType.THIRD_PERSON_RIGHT_HAND;
				break;
			default: break;
		}
	}
	
	@Override
	protected ItemStack getArmorForBone(String boneName, T currentEntity) {
		this.currentArmorAtBone = null;
		this.currentSlotAtBone = null;
		this.currentArmorPartAtBone = null;
		
		this.calculateArmorStuffForBone(boneName, currentEntity);
		
		return this.currentArmorAtBone;
	}
	
	@Override
	protected EquipmentSlot getEquipmentSlotForArmorBone(String boneName, T currentEntity) {
		return this.currentSlotAtBone;
	}

	@Override
	@Nullable
	protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
		if(this.currentArmorPartAtBone != null) {
			return this.currentArmorPartAtBone.apply(armorModel);
		}
		return null;
	}
	
	@Override
	protected ItemStack getHeldItemForBone(String boneName, T currentEntity) {
		this.currentItemAtBone = null;
		this.currentTransformTypeAtBone = null;
		
		this.calculateItemStuffForBone(boneName, currentEntity);
		
		return this.currentItemAtBone;
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		return this.currentTransformTypeAtBone;
	}

}
