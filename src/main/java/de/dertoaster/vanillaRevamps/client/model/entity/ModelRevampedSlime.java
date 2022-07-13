package de.dertoaster.vanillaRevamps.client.model.entity;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import de.dertoaster.vanillaRevamps.entity.RevampedSlime;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelRevampedSlime<T extends RevampedSlime> extends HierarchicalModel<T> {
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

	   @Nullable
	   protected T currentEntity;
	   
	   /**
	    * Sets this entity's model rotation angles
	    */
	   public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		   this.currentEntity = pEntity;
	   }

	   public ModelPart root() {
	      return this.root;
	   }
	   
	   @Override
	public void renderToBuffer(PoseStack p_170625_, VertexConsumer p_170626_, int p_170627_, int p_170628_, float pRed, float pGreen, float pBlue, float pAlpha) {
		   if(this.currentEntity != null) {
			   pRed = this.currentEntity.getColorRed();
			   pGreen = this.currentEntity.getColorGreen();
			   pBlue = this.currentEntity.getColorBlue();
		   }
		   super.renderToBuffer(p_170625_, p_170626_, p_170627_, p_170628_, pRed, pGreen, pBlue, pAlpha);
	}

}
