package ru.givler.seasonalexpansion.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityDireWolf extends EntityWolf {

    public EntityDireWolf(World world) {
        super(world);

        this.setSize(1.2F, 1.1F);
        this.isImmuneToFire = false;
        this.experienceValue = 10;

        this.setAngry(true);

        initEntityAI();
    }

    protected void initEntityAI() {
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.4D, false));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 16.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D); // max
    }

    @Override
    public boolean isTamed() {
        return false;
    }

    @Override
    public boolean interact(EntityPlayer player) {
        return false;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if(!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
        {
            setDead();
        }
    }

    @Override
    public void setTamed(boolean tamed) {
        // полностью игнорируем стандартное поведение, чтобы не сбрасывало здоровье
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)(tamed ? 1 : 0)));
    }
    @Override
    public boolean isAngry() {
        return true;
    }
}
