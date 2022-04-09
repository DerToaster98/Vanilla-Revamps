package de.dertoaster.vanillaRevamps.entity;

import static de.dertoaster.vanillaRevamps.entity.EntityUtil.isWearingAnyArmor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
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
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.creeper.idle", true));
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
		if (!this.isIgnited()) {
			this.ignite();
		}
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (pSource.isFire() && isWearingAnyArmor(this)) {
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

}
