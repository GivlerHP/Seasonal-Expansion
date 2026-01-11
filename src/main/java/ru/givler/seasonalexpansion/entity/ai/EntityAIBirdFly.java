package ru.givler.seasonalexpansion.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import ru.givler.seasonalexpansion.entity.EntityTinyBird;

public class EntityAIBirdFly  extends EntityAIBase {

    private EntityTinyBird  entity;

    public EntityAIBirdFly(EntityTinyBird  par1EntityCreature)
    {
        this.entity = par1EntityCreature;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        return !entity.isBirdLanded();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return !entity.isBirdLanded();
    }

}