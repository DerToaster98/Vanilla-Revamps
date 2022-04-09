package de.dertoaster.vanillaRevamps.entity;

import static de.dertoaster.vanillaRevamps.entity.EntityUtil.isWearingAnyArmor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
		data.addAnimationController(new AnimationController(this, "controller-main", 20, this::predicate));
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
		this.entityData.set(DATA_IS_POWERED, true);

		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(2 * this.getMaxHealth());
		this.setHealth(this.getMaxHealth());
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, SpawnGroupData pSpawnData, CompoundTag pDataTag) {
		this.populateDefaultEquipmentSlots(pDifficulty);
		this.populateDefaultEquipmentEnchantments(pDifficulty);

		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	public float getSwell() {
		return this.swell;
	}

}
