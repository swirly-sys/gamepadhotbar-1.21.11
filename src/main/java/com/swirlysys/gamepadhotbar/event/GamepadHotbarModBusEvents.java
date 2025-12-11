package com.swirlysys.gamepadhotbar.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.swirlysys.gamepadhotbar.GamepadHotbar;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.apache.logging.log4j.util.Lazy;

@EventBusSubscriber(modid = GamepadHotbar.MODID)
public class GamepadHotbarModBusEvents {
    public static final KeyMapping.Category CYCLE_HOTBAR = new KeyMapping.Category(Identifier.fromNamespaceAndPath(
            "gamepadhotbar", "cycle_hotbar"
    ));

    public static final Lazy<KeyMapping> LEFT = Lazy.value(new KeyMapping("key.gamepadhotbar.cyclehotbar_0",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CYCLE_HOTBAR
    ));
    public static final Lazy<KeyMapping> RIGHT = Lazy.value(new KeyMapping("key.gamepadhotbar.cyclehotbar_1",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CYCLE_HOTBAR
    ));
    public static final Lazy<KeyMapping> UP = Lazy.value(new KeyMapping("key.gamepadhotbar.cyclehotbar_2",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CYCLE_HOTBAR
    ));
    public static final Lazy<KeyMapping> DOWN = Lazy.value(new KeyMapping("key.gamepadhotbar.cyclehotbar_3",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CYCLE_HOTBAR
    ));
    public static final Lazy<KeyMapping> WEAPON = Lazy.value(new KeyMapping("key.gamepadhotbar.cyclehotbar_4",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CYCLE_HOTBAR
    ));

    @SubscribeEvent
    public static void registerControlBinds(RegisterKeyMappingsEvent event) {
        event.registerCategory(CYCLE_HOTBAR);
        event.register(LEFT.get());
        event.register(UP.get());
        event.register(RIGHT.get());
        event.register(DOWN.get());
        event.register(WEAPON.get());
    }
}
