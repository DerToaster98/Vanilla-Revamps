package de.dertoaster.vanillaRevamps.entity;

import de.dertoaster.vanillaRevamps.entity.goal.slime.SlimeMergeGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RevampedSlime extends Slime {
	
	public static final int MIN_TIME_BEFORE_MERGING = 400;

	private static final EntityDimensions BASE_DIMENSIONS = EntityDimensions.scalable(0.5F, 0.5F);
	//Max slimes of 512 results in a maximum size of 4x4x4 blocks
	//Max side length: <MAX_SLIMES>^(1/3) * 0.5
	private static final int MAX_SLIMES = 512;
	private static final float[] SIZE_SCALE_PRE_COMPUTED = new float[MAX_SLIMES];

	protected static final float[] COLOR_VARIANTS = new float[] { 
		//Red	
		1F, 0F, 0F,
		//Green
		0F, 1F, 0F, 
		//Blue
		0F, 0F, 1F, 
		//White
		1F, 1F, 1F,
		//Light blue
		0F, 1F, 1F,
		//Orange
		1F, 0.5F, 0F,
		//Yellow
		1F, 1F, 0F,
		//Purple
		0.4F, 0F, 0.8F,
		//Pink
		1F, 0.2F, 1F,
		//Light green
		0F, 1F, 0.5F
	};
	protected static final EntityDataAccessor<Float> COLOR_RED = SynchedEntityData.defineId(RevampedSlime.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> COLOR_GREEN = SynchedEntityData.defineId(RevampedSlime.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> COLOR_BLUE = SynchedEntityData.defineId(RevampedSlime.class, EntityDataSerializers.FLOAT);

	static {
		for (int i = 0; i < SIZE_SCALE_PRE_COMPUTED.length; i++) {
			SIZE_SCALE_PRE_COMPUTED[i] = (float) Math.cbrt(i + 1);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	public boolean canCollideWith(Entity pEntity) {
		return !(pEntity instanceof RevampedSlime);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();

		int variant = this.getRandom().nextInt(COLOR_VARIANTS.length / 3);
		variant *= 3;

		this.entityData.define(COLOR_RED, COLOR_VARIANTS[variant]);
		this.entityData.define(COLOR_GREEN, COLOR_VARIANTS[variant + 1]);
		this.entityData.define(COLOR_BLUE, COLOR_VARIANTS[variant + 2]);
	}
	
	@Override
	protected void registerGoals() {
	      this.goalSelector.addGoal(1, new Slime.SlimeFloatGoal(this));
	      //this.goalSelector.addGoal(2, new SlimeAvoidLargerMobsGoal(this, 8F, 1.0D, 1.0D));
	      this.goalSelector.addGoal(3, new SlimeMergeGoal(this, 1.0, 64));
	      //this.goalSelector.addGoal(4, new Slime.SlimeAttackGoal(this));
	      //this.goalSelector.addGoal(5, new Slime.SlimeRandomDirectionGoal(this));
	      this.goalSelector.addGoal(6, new Slime.SlimeKeepOnJumpingGoal(this));
	      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (p_33641_) -> {
	         return Math.abs(p_33641_.getY() - this.getY()) <= 4.0D;
	      }));
	      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	   }
	
	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		
		pCompound.putFloat("rs_color_red", this.getColorRed());
		pCompound.putFloat("rs_color_green", this.getColorGreen());
		pCompound.putFloat("rs_color_blue", this.getColorBlue());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		
		this.setColorRed(pCompound.getFloat("rs_color_red"));
		this.setColorGreen(pCompound.getFloat("rs_color_green"));
		this.setColorBlue(pCompound.getFloat("rs_color_blue"));
	}

	private void setColorBlue(float float1) {
		if(!this.level.isClientSide()) {
			this.entityData.set(COLOR_BLUE, float1);
		}
	}

	private void setColorGreen(float float1) {
		if(!this.level.isClientSide()) {
			this.entityData.set(COLOR_GREEN, float1);
		}
	}

	private void setColorRed(float float1) {
		if(!this.level.isClientSide()) {
			this.entityData.set(COLOR_RED, float1);
		}
	}

	// Size: Amount of slimes absorbed
	// Base size: <smallest size> (0.5x0.5x0.5) * (size^(1/3))

	public RevampedSlime(EntityType<? extends Slime> p_33588_, Level p_33589_) {
		super(p_33588_, p_33589_);
	}

	@Override
	public int getSize() {
		int sr = super.getSize();
		return Mth.clamp(sr, 0, SIZE_SCALE_PRE_COMPUTED.length - 1);
	}

	@Override
	public EntityDimensions getDimensions(Pose pPose) {
		return BASE_DIMENSIONS.scale(this.getScale());
	}
	
	@Override
	public float getScale() {
		return SIZE_SCALE_PRE_COMPUTED[this.getSize()];
	}

	@Override
	public void push(Entity pEntity) {
		if (pEntity instanceof RevampedSlime rs && this.tickCount > MIN_TIME_BEFORE_MERGING && pEntity.tickCount > MIN_TIME_BEFORE_MERGING) {
			this.mergeColors(rs);
			this.merge(rs);

			rs.setSize(0, false);
			rs.remove(RemovalReason.DISCARDED);
		} else {
			super.push(pEntity);
		}
	}

	private void merge(RevampedSlime rs) {
		final int mySize = this.getSize();
		final int theirSize = rs.getSize();

		int size = Mth.clamp(mySize + theirSize, mySize, MAX_SLIMES);
		this.setSize(size, true);
		
		//Now, relocate
		Vec3 newPos = this.position().add(rs.position().subtract(this.position()).multiply(0.5, 0.5, 0.5));
		this.setPos(newPos);
	}

	@Override
	public void remove(RemovalReason pReason) {
		if (pReason == RemovalReason.KILLED) {
			this.splitOnDeath();
		}
		// Avoid calling the super class "splitting"
		this.setSize(1, false);
		super.remove(pReason);
	}

	private void splitOnDeath() {
		if (this.getSize() <= 1) {
			return;
		}
		int splits = Mth.clamp(this.getSize(), 1, 8);
		int sizePerSplit = this.getSize() / splits;
		Component component = this.getCustomName();
		boolean flag = this.isNoAi();

		final double halfHeight = this.getBbHeight() / 2;
		final double quarterWidth = this.getBbWidth() / 4;
		Vec3 center = this.position().add(0, halfHeight, 0);

		Vec3[] positions = new Vec3[] {
				center.subtract(0, halfHeight, 0).add(quarterWidth, 0, quarterWidth),
				center.subtract(0, halfHeight, 0).add(quarterWidth, 0, -quarterWidth),
				center.subtract(0, halfHeight, 0).add(-quarterWidth, 0, quarterWidth),
				center.subtract(0, halfHeight, 0).add(-quarterWidth, 0, -quarterWidth),
				center.add(quarterWidth, 0, quarterWidth),
				center.add(quarterWidth, 0, -quarterWidth),
				center.add(-quarterWidth, 0, quarterWidth),
				center.add(-quarterWidth, 0, -quarterWidth), };
		for (int i = 0; i < splits; i++) {
			Vec3 spawnPos = positions[i];
			Vec3 direction = positions[i].subtract(center).normalize().scale(0.05 * this.getSize());

			RevampedSlime slime = this.getType().create(this.level);
			if (this.isPersistenceRequired()) {
				slime.setPersistenceRequired();
			}

			slime.setCustomName(component);
			slime.setNoAi(flag);
			slime.setInvulnerable(this.isInvulnerable());
			slime.setSize(sizePerSplit, true);
			slime.setPos(spawnPos);
			slime.setDeltaMovement(direction);
			
			this.level.addFreshEntity(slime);
		}
	}

	@SuppressWarnings("unchecked")
	public EntityType<? extends RevampedSlime> getType() {
		return (EntityType<? extends RevampedSlime>) super.getType();
	}

	private void mergeColors(RevampedSlime rs) {
		final float myRed = this.getColorRed();
		final float myGreen = this.getColorGreen();
		final float myBlue = this.getColorBlue();
		final float theirRed = rs.getColorRed();
		final float theirGreen = rs.getColorGreen();
		final float theirBlue = rs.getColorBlue();

		if (!this.level.isClientSide()) {
			this.entityData.set(COLOR_RED, 0.5F * (myRed + theirRed));
			this.entityData.set(COLOR_GREEN, 0.5F * (myGreen + theirGreen));
			this.entityData.set(COLOR_BLUE, 0.5F * (myBlue + theirBlue));
		}
	}

	public float getColorRed() {
		return this.entityData.get(COLOR_RED);
	}

	public float getColorGreen() {
		return this.entityData.get(COLOR_GREEN);
	}

	public float getColorBlue() {
		return this.entityData.get(COLOR_BLUE);
	}
	
}
