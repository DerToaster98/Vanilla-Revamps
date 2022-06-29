package de.dertoaster.vanillaRevamps.init;

import java.util.HashMap;
import java.util.Map;

import de.dertoaster.vanillaRevamps.VanillaRevampsMod;
import de.dertoaster.vanillaRevamps.entity.RevampedCreeper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = VanillaRevampsMod.MODID, bus = Bus.MOD)
public class VREntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, VanillaRevampsMod.MODID);

	public static final RegistryObject<EntityType<RevampedCreeper>> CREEPER = registerSized(RevampedCreeper::new, "creeper", 0.6F, 1.7F, 3);

	protected static <T extends Entity> RegistryObject<EntityType<T>> registerSizedHumanoid(EntityFactory<T> factory, final String entityName, int updateInterval) {
		return registerSized(factory, entityName, 0.6F, 1.875F, updateInterval);
	}

	protected static <T extends Entity> RegistryObject<EntityType<T>> registerSized(EntityFactory<T> factory, final String entityName, float width, float height, int updateInterval) {
		return ENTITY_TYPES.register(entityName, () -> EntityType.Builder.<T>of(factory, MobCategory.MISC).sized(width, height).setTrackingRange(128).clientTrackingRange(64).updateInterval(updateInterval).setShouldReceiveVelocityUpdates(true).build(
				VanillaRevampsMod.prefix(entityName).toString()));
	}

	@SubscribeEvent
	public static void initializeAttributes(EntityAttributeCreationEvent event) {
		event.put(CREEPER.get(), Creeper.createAttributes().build());
	}

	public static void registerEntityTypes() {
		ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	static final Map<EntityType<?>, EntityType<?>> REPLACER_MAP;

	static {
		REPLACER_MAP = new HashMap<>();
	}

	public static class EventHandler {
		@SubscribeEvent
		public void onEntityJoin(final EntityJoinWorldEvent event) {
			if (REPLACER_MAP.isEmpty()) {
				fillReplacementMap();
			}
			if (event.getEntity() == null || !(event.getWorld() instanceof ServerLevel)) {
				return;
			}
			final EntityType<?> originalType = event.getEntity().getType();
			final EntityType<?> replacement = REPLACER_MAP.getOrDefault(originalType, null);
			if (replacement != null) {
				Entity replacedEnt = replacement.create((ServerLevel) event.getWorld(), event.getEntity().getPersistentData(), event.getEntity().getCustomName(), null, event.getEntity().blockPosition(), MobSpawnType.NATURAL, true, true);

				if (replacedEnt != null) {
					event.getWorld().addFreshEntity(replacedEnt);
					event.setCanceled(true);
					event.getEntity().remove(RemovalReason.DISCARDED);
				}
			}
		}
	}

	static void fillReplacementMap() {
		// Add in the replacements
		REPLACER_MAP.put(EntityType.CREEPER, CREEPER.get());
	}

}
