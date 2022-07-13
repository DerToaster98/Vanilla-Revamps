package de.dertoaster.vanillaRevamps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Mixin(PrimedTnt.class)
public abstract class PrimedTNTMixin extends Entity {
	
	protected boolean isFlying = false;
	protected double vLastTick = 0D;
	
	protected final double MIN_IMPULSE = 0.35D;
	protected final double MAX_IMPULSE_FACTOR = 10.0D;

	public PrimedTNTMixin(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	private void handleContactExplosive() {
		if(((PrimedTnt)((Object)this)).getFuse() <= 0 || !this.isAlive() || this.isRemoved()) {
			return;
		}
		
		final double v = this.getDeltaMovement().lengthSqr();
		final double vOld = this.vLastTick;
		this.isFlying = this.isFlying || (v > MIN_IMPULSE);
		if(this.isFlying) {
			System.out.println("flying");
			if(v < (vOld / MAX_IMPULSE_FACTOR)) {
				this.setNoGravity(true);
				this.setDeltaMovement(Vec3.ZERO);
				((PrimedTnt)((Object)this)).setFuse(0);
				System.out.println("boom");
			}
		}
		this.vLastTick = v;
	}

	@Inject(at = @At("HEAD"), method = "tick()V", cancellable = true)
	private void mxinTick(CallbackInfo ci) {
		this.handleContactExplosive();
	}

}
