package de.dertoaster.vanillaRevamps.entity.goal.slime;

import de.dertoaster.vanillaRevamps.entity.RevampedSlime;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;

public class SlimeMergeGoal extends FollowMobGoal {

	public SlimeMergeGoal(Mob pMob, double pSpeedModifier, float pAreaSize) {
		super(pMob, pSpeedModifier, 0, pAreaSize);
		this.followPredicate = (followCandidate) -> {
			final LivingEntity mob = SlimeMergeGoal.this.mob;
			if(mob.tickCount < RevampedSlime.MIN_TIME_BEFORE_MERGING) {
				return false;
			}
			
			if(!(followCandidate instanceof RevampedSlime)) {
				return false;
			}
			if(followCandidate.tickCount < RevampedSlime.MIN_TIME_BEFORE_MERGING) {
				return false;
			}
			final float mySize = mob.getBbWidth() * mob.getBbHeight() * mob.getBbWidth();
			final float theirSize = followCandidate.getBbWidth() * followCandidate.getBbHeight() * followCandidate.getBbWidth();
			return mySize <= theirSize;
		};
	}
	
	@Override
	public boolean canUse() {
		if(super.canUse()) {
			return this.mob.tickCount > RevampedSlime.MIN_TIME_BEFORE_MERGING;
		} 
		return false;
	}

}
