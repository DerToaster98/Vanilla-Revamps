package de.dertoaster.vanillaRevamps.entity;

import static de.dertoaster.vanillaRevamps.entity.EntityUtil.isWearingAnyArmor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RevampedCreeper extends Creeper implements IAnimatable, IAnimationTickable {

	protected final AnimationFactory factory = new AnimationFactory(this);

	public RevampedCreeper(EntityType<? extends Creeper> type, Level level) {
		super(type, level);
	}

	@Override
	public int tickTimer() {
		return this.tickCount;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller-main", 10, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.creeper.walk", true));
			event.getController().setAnimationSpeed(1.5D);
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.creeper.idle", true));
			event.getController().setAnimationSpeed(1.0D);
		}
		return PlayState.CONTINUE;
	}
	
	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	// New logic
	@Override
	public void setSecondsOnFire(int pSeconds) {
		if (!this.isIgnited() && !isWearingAnyArmor(this) && !this.isPowered() && !this.isInWaterOrRain()) {
			this.ignite();
		}
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (pSource.isExplosion()) {
			if (!this.isIgnited()) {
				this.ignite();
			}
			pAmount = 0;
		}
		if (pSource.isFire() && !isWearingAnyArmor(this) && !this.isPowered() && !this.isIgnited()) {
			this.ignite();
		}
		return super.hurt(pSource, pAmount);
	}

	@Override
	public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
		if(this.isPowered()) {
			this.setHealth(this.getMaxHealth());
		}
		else {
			this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(4 * this.getMaxHealth());
			this.setHealth(this.getMaxHealth());
		}
		this.entityData.set(DATA_IS_POWERED, true);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, SpawnGroupData pSpawnData, CompoundTag pDataTag) {
		this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
		this.populateDefaultEquipmentEnchantments(pLevel.getRandom(), pDifficulty);
		
		//this.setItemSlot(EquipmentSlot.HEAD, Items.NETHERITE_HELMET.getDefaultInstance());
		//this.setItemSlot(EquipmentSlot.CHEST, Items.NETHERITE_CHESTPLATE.getDefaultInstance());
		//this.setItemSlot(EquipmentSlot.LEGS, Items.NETHERITE_LEGGINGS.getDefaultInstance());
		//this.setItemSlot(EquipmentSlot.FEET, Items.NETHERITE_BOOTS.getDefaultInstance());

		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	public float getSwell() {
		return this.swell;
	}
	
	@Override
	protected void explodeCreeper() {
		if(!this.level.isClientSide()) {
			for(EquipmentSlot slot : EquipmentSlot.values()) {
				if(slot.getType() == Type.ARMOR) {
					ItemStack stack = this.getItemBySlot(slot);
					
					ItemEntity itemEnt = this.spawnAtLocation(stack, this.getBbHeight() * 0.66F);
					if(itemEnt != null) {
						this.setItemSlot(slot, ItemStack.EMPTY);
						
						float vx = -0.5F * this.level.getRandom().nextFloat();
						vx *= 1.0F + 0.5F * this.level.getRandom().nextFloat();
						
						float vz = -0.5F * this.level.getRandom().nextFloat();
						vz *= 1.0F + 0.5F * this.level.getRandom().nextFloat();
						
						float vy = 0.125F + 0.25F* this.level.getRandom().nextFloat();
						
						Vec3 velocity = new Vec3(vx, vy, vz);
						velocity.scale((double)(this.getBbWidth() * 0.5F));
						itemEnt.setPos(itemEnt.position().add(velocity));
						
						itemEnt.setDeltaMovement(vx, vy, vz);
						itemEnt.setInvulnerable(true);
					}
				}
			}
		}
		super.explodeCreeper();
	}
	
	@Override
	protected void dropEquipment() {
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemStack getPickResult() {
		SpawnEggItem spawneggitem = SpawnEggItem.byId(EntityType.CREEPER);
		return spawneggitem == null ? null : new ItemStack(spawneggitem);
	}
	
	@Override
	public boolean wantsToPickUp(ItemStack p_21546_) {
		return super.wantsToPickUp(p_21546_) || (p_21546_ != null && !p_21546_.isEmpty() && p_21546_.getItem() instanceof ArmorItem);
	}
	
	@Override
	protected boolean canReplaceCurrentItem(ItemStack pCandidate, ItemStack pExisting) {
		return this.wantsToPickUp(pCandidate) && (pExisting == null || (pExisting != null && pExisting.isEmpty()));
	}
	

}
