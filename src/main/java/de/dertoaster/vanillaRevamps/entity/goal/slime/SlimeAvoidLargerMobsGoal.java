package de.dertoaster.vanillaRevamps.entity.goal.slime;

import com.google.common.base.Predicates;

import de.dertoaster.vanillaRevamps.entity.RevampedSlime;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class SlimeAvoidLargerMobsGoal extends AvoidEntityGoal<LivingEntity> {

	public SlimeAvoidLargerMobsGoal(PathfinderMob pMob, float pMaxDistance, double pWalkSpeedModifier, double pSprintSpeedModifier) {
		super(pMob, LivingEntity.class, (toAvoid) -> {
			if (toAvoid instanceof RevampedSlime) {
				return false;
			}
			return true;
		}, pMaxDistance, pWalkSpeedModifier, pSprintSpeedModifier, Predicates.alwaysTrue());
	}
	
	@Override
	public boolean canUse() {
		if(super.canUse()) {
			final double myVolume = this.mob.getBbWidth() * this.mob.getBbHeight() * this.mob.getBbWidth();
			final double theirVolume = this.toAvoid.getBbWidth() * this.toAvoid.getBbHeight() * this.toAvoid.getBbWidth();
			
			return myVolume < theirVolume;
		}
		return false;
	}

}
