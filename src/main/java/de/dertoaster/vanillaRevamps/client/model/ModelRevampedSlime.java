package de.dertoaster.vanillaRevamps.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class ModelRevampedSlime<T extends Entity> extends HierarchicalModel<T> {
	   private final ModelPart root;

	   public ModelRevampedSlime(ModelPart pRoot) {
	      this.root = pRoot;
	   }

	   public static LayerDefinition createLayer() {
	      MeshDefinition meshdefinition = new MeshDefinition();
	      PartDefinition partdefinition = meshdefinition.getRoot();
	      partdefinition.addOrReplaceChild("outer", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
	      partdefinition.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
	      return LayerDefinition.create(meshdefinition, 32, 32);
	   }

	   /**
	    * Sets this entity's model rotation angles
	    */
	   public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
	   }

	   public ModelPart root() {
	      return this.root;
	   }

}