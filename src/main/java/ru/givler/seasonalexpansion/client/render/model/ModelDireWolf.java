package ru.givler.seasonalexpansion.client.render.model;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.Field;

public class ModelDireWolf extends ModelWolf {
    private static final float TAIL_ANGLE = (float) Math.PI / 5.0F;
    private static final Field WOLF_TAIL = ReflectionHelper.findField(
            ModelWolf.class, "wolfTail", "field_78180_g"
    );

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing,
                                    float limbSwingAmount, float partialTickTime) {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTickTime);
        stabilizeTail();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks,
                netHeadYaw, headPitch, scaleFactor, entity);
        stabilizeTail();
    }

    private void stabilizeTail() {
        try {
            ModelRenderer tail = (ModelRenderer) WOLF_TAIL.get(this);
            tail.rotateAngleX = TAIL_ANGLE;
            tail.rotateAngleY = 0.0F;
            tail.rotateAngleZ = 0.0F;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access dire wolf tail model", e);
        }
    }
}
