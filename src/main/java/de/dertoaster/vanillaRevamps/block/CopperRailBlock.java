package de.dertoaster.vanillaRevamps.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CopperRailBlock extends RailBlock implements WeatheringCopper {
	
	private final WeatheringCopper.WeatherState weatherState;
	
	public static final IntegerProperty MINECART_PASS_OVER = IntegerProperty.create("minecart_pass_over", 0, 20);

	public CopperRailBlock(WeatheringCopper.WeatherState weatherState, Properties props) {
		super(props);
		this.weatherState = weatherState;
		this.registerDefaultState(this.stateDefinition.any().setValue(MINECART_PASS_OVER, 0));
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
	
	@Override
	public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		float baseVal = super.getRailMaxSpeed(state, level, pos, cart);
		
		int negativeMultiplier = this.getAge().ordinal();
		baseVal *= 2 - negativeMultiplier;
		baseVal = Mth.clamp(baseVal, 0.2F, 0.6F);
		
		return baseVal;
	}
	
	@Override
	public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		super.onMinecartPass(state, level, pos, cart);
		
		if(level instanceof ServerLevel serverLevel) {
			int passOvers = state.getValue(MINECART_PASS_OVER);
			passOvers++;
			if(passOvers > 20) {
				//Oxidize
				passOvers = 0;
				state.setValue(MINECART_PASS_OVER, passOvers);
				this.applyChangeOverTime(state, serverLevel, pos, level.getRandom());
			} else {
				state.setValue(MINECART_PASS_OVER, passOvers);
			}
		}
	}

}
