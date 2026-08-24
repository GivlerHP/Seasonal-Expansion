package ru.givler.seasonalexpansion.registry;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ru.givler.seasonalexpansion.block.BlockTelescope;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class BlockRegistry {
    public static Block telescope;
    public static Block mdtelescope;

    private BlockRegistry() {}

    public static void preLoad(FMLPreInitializationEvent event) {
        if (!Loader.isModLoaded("mbo")) {
            telescope = new BlockTelescope(Material.iron, "telescope");
        }
    }

    public static void init(FMLInitializationEvent event) {
        if (Loader.isModLoaded("mbo")) {
            // BlockModels adds every instance to MBO's global model list. Creating this
            // block during preInit lets MBO register it first under the "mbo" namespace.
            // Create it only after MBO has processed that list, then register it while
            // SeasonalExpansion is the active mod container.
            mdtelescope = createMboTelescope();
            mdtelescope.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 1.4F, 0.7F);
            invokeNoArgs(mdtelescope, "register");
        }
    }

    public static void bindMboTelescopeRenderer() {
        if (mdtelescope == null) return;
        try {
            Class<?> proxy = Class.forName("ru.givler.mbo.proxy.ClientProxy");
            for (Method method : proxy.getMethods()) {
                if (method.getName().equals("bindDefaultRender") && method.getParameterTypes().length == 1) {
                    method.invoke(null, mdtelescope);
                    return;
                }
            }
            throw new NoSuchMethodException("bindDefaultRender");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to bind MBO telescope renderer", e);
        }
    }

    private static Block createMboTelescope() {
        try {
            Class<?> type = Class.forName("ru.givler.seasonalexpansion.block.ModelTelescope");
            Constructor<?> constructor = type.getConstructor(
                    Material.class, String.class, String.class, String.class
            );
            return (Block) constructor.newInstance(Material.iron, "mdtelescope", "telescope", "telescope");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create MBO telescope", e);
        }
    }

    private static void invokeNoArgs(Object target, String methodName) {
        try {
            target.getClass().getMethod(methodName).invoke(target);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to invoke " + methodName, e);
        }
    }
}
