package ru.givler.seasonalexpansion.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.givler.seasonalexpansion.entity.EntityDireWolf;

@SideOnly(Side.CLIENT)
public class RenderDireWolf extends RenderLiving {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("textures/entity/wolf/wolf_angry.png");

    public RenderDireWolf() {
        super(new ModelWolf(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entity, float partialTickTime) {
        GL11.glScalef(2.0F, 2.0F, 2.0F);
    }
}
