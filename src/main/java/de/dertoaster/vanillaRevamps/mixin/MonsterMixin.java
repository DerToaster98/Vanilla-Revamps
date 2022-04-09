package de.dertoaster.vanillaRevamps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import de.dertoaster.vanillaRevamps.entity.goal.AvoidExplodingCreeperGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

@Mixin(Monster.class)
public abstract class MonsterMixin extends PathfinderMob {
	
	protected MonsterMixin(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
		super(p_21683_, p_21684_);
	}

	@Inject(
			at = @At("TAIL"),
			method = "registerGoals()V",
			cancellable = true
	)
	private void mixinRegisterGoals() {
		this.goalSelector.addGoal(3, new AvoidExplodingCreeperGoal((PathfinderMob)this, 9.0F, 1.0, 1.2));
	}
	

}
