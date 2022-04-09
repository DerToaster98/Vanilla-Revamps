package de.dertoaster.vanillaRevamps.entity;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EntityUtil {

	public static boolean isWearingAnyArmor(LivingEntity entity) {
		for(EquipmentSlot slot : EquipmentSlot.values()) {
			if(slot.getType() == Type.ARMOR) {
				ItemStack item = entity.getItemBySlot(slot);
				if(item.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}
	
}
