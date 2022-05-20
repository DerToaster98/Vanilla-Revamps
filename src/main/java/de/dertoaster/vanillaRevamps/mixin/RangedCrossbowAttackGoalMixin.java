package de.dertoaster.vanillaRevamps.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal.CrossbowState;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

@Mixin(RangedCrossbowAttackGoal.class)
public abstract class RangedCrossbowAttackGoalMixin<T extends Monster & RangedAttackMob & CrossbowAttackMob> extends Goal {

	@Shadow
	@Final
	T mob;
	@Shadow
	int attackDelay;
	@Shadow
	RangedCrossbowAttackGoal.CrossbowState crossbowState;

	@Inject(at = @At("TAIL"), method = "isValidTarget()Z", cancellable = true)
	private void mixinIsValidTarget(CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			RangedCrossbowAttackGoal<?> self = (RangedCrossbowAttackGoal<?>) (Object) this;
			cir.setReturnValue(self.crossbowState == CrossbowState.CHARGING || self.crossbowState == CrossbowState.UNCHARGED);
		}
	}

	@Inject(at = @At("HEAD"), method = "tick()V", cancellable = true)
	private void mixinTick(CallbackInfo ci) {
		// Necessary to not conflict with vanilla, vanilla does this at the first line
		if (this.mob != null && this.mob.getTarget() == null) {
			if (this.crossbowState == RangedCrossbowAttackGoal.CrossbowState.UNCHARGED) {
				this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem));
				this.crossbowState = RangedCrossbowAttackGoal.CrossbowState.CHARGING;
				this.mob.setChargingCrossbow(true);

				ci.cancel();
			} else if (this.crossbowState == RangedCrossbowAttackGoal.CrossbowState.CHARGING) {
				if (!this.mob.isUsingItem()) {
					this.crossbowState = RangedCrossbowAttackGoal.CrossbowState.UNCHARGED;
				}

				int i = this.mob.getTicksUsingItem();
				ItemStack itemstack = this.mob.getUseItem();
				if (i >= CrossbowItem.getChargeDuration(itemstack)) {
					this.mob.releaseUsingItem();
					this.crossbowState = RangedCrossbowAttackGoal.CrossbowState.CHARGED;
					this.attackDelay = 20 + this.mob.getRandom().nextInt(20);
					this.mob.setChargingCrossbow(false);
				}
				
				ci.cancel();
			}
		}
	}

}
