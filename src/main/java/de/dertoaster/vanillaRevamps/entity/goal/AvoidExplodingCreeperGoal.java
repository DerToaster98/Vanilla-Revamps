package de.dertoaster.vanillaRevamps.entity.goal;

import com.google.common.base.Predicates;

import de.dertoaster.vanillaRevamps.entity.RevampedCreeper;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;

public class AvoidExplodingCreeperGoal extends AvoidEntityGoal<Creeper> {

	public AvoidExplodingCreeperGoal(PathfinderMob pMob, float pMaxDistance, double pWalkSpeedModifier, double pSprintSpeedModifier) {
		super(pMob, Creeper.class, (toAvoid) -> {
			if(toAvoid instanceof RevampedCreeper rc) {
				return rc.isIgnited() || rc.getSwell() != 0.0F;
			}
			return false;
		}, pMaxDistance, pWalkSpeedModifier, pSprintSpeedModifier, Predicates.alwaysTrue());
	}

	@Override
	public boolean canUse() {
		if(this.mob instanceof INotAfraidOfExplodingCreepers inaoec) {
			if(!inaoec.isAfraidOfExplodingCreepers()) {
				return false;
			}
		}
		return super.canUse();
	}
	
	public static interface INotAfraidOfExplodingCreepers {
		public default boolean isAfraidOfExplodingCreepers() {
			return false;
		}
	}

}
