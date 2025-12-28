package com.swirlysys.gamepadhotbar.event;

import com.swirlysys.gamepadhotbar.GamepadHotbar;
import com.swirlysys.gamepadhotbar.config.GamepadHotbarClientConfig;
import com.swirlysys.gamepadhotbar.util.HotbarPos;
import com.swirlysys.gamepadhotbar.util.HotbarScale;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.joml.Vector2i;

@EventBusSubscriber(modid = GamepadHotbar.MOD_ID, value = Dist.CLIENT)
public class GamepadHotbarClientEvents {
    private static final Identifier HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar");
    private static final Identifier HOTBAR_SELECTION_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_selection");
    private static final Identifier HOTBAR_OFFHAND_LEFT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_left");
    private static final Identifier HOTBAR_OFFHAND_RIGHT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_right");
    private static final Identifier HOTBAR_0 = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "container/slot/hotbar_0");
    private static final Identifier HOTBAR_1 = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "container/slot/hotbar_1");
    private static final Identifier HOTBAR_2 = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "container/slot/hotbar_2");
    private static final Identifier HOTBAR_3 = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "container/slot/hotbar_3");
    private static final Identifier HOTBAR_4 = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "container/slot/hotbar_4");
    private static final Identifier HOTBAR_5 = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "container/slot/hotbar_5");
    private static final Identifier HOTBAR_6 = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "container/slot/hotbar_6");
    private static final Identifier HOTBAR_7 = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "container/slot/hotbar_7");
    private static final Identifier TAP_HOTBAR_LEFT = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "hud/tap_hotbar_left");
    private static final Identifier TAP_HOTBAR_UP = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "hud/tap_hotbar_up");
    private static final Identifier TAP_HOTBAR_RIGHT = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "hud/tap_hotbar_right");
    private static final Identifier TAP_HOTBAR_DOWN = Identifier.fromNamespaceAndPath(GamepadHotbar.MOD_ID, "hud/tap_hotbar_down");
    private static final Identifier HOTBAR_8 = Identifier.withDefaultNamespace("container/slot/sword");
    private static final Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace(
            "hud/hotbar_attack_indicator_background"
    );
    private static final Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace(
            "hud/hotbar_attack_indicator_progress"
    );
    private static Vector2i iteratePos(int index, int scale, int arm) {
        Vector2i vec = new Vector2i(0, 0);
        switch (index) {
            case 1 -> {return vec.add(-41 + scale, -31);}
            case 2 -> {return vec.add(-20 + scale, -41);}
            case 3 -> {return vec.add(scale, -41);}
            case 4 -> {return vec.add(21 + scale, -31);}
            case 5 -> {return vec.add(21 + scale, -11);}
            case 6 -> {return vec.add(scale, 0);}
            case 7 -> {return vec.add(-20 + scale, 0);}
            case 8 -> {return vec.add(4 - arm - scale, -20);}
            default -> {return vec.add(-41 + scale, -11);}
        }
    }
    private static Identifier iterateSlotIcons(int index) {
        switch (index) {
            case 1 -> {return HOTBAR_1;}
            case 2 -> {return HOTBAR_2;}
            case 3 -> {return HOTBAR_3;}
            case 4 -> {return HOTBAR_4;}
            case 5 -> {return HOTBAR_5;}
            case 6 -> {return HOTBAR_6;}
            case 7 -> {return HOTBAR_7;}
            case 8 -> {return HOTBAR_8;}
            default -> {return HOTBAR_0;}
        }
    }
    private static void renderSlot(GuiGraphics guiGraphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack stack, int seed) {
        if (!stack.isEmpty()) {
            float f = stack.getPopTime() - deltaTracker.getGameTimeDeltaPartialTick(false);
            if (f > 0.0F) {
                float f1 = 1.0F + f / 5.0F;
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().translate(x + 8, y + 12);
                guiGraphics.pose().scale(1.0F / f1, (f1 + 1.0F) / 2.0F);
                guiGraphics.pose().translate(-(x + 8), -(y + 12));
            }

            guiGraphics.renderItem(player, stack, x, y, seed);
            if (f > 0.0F) {
                guiGraphics.pose().popMatrix();
            }

            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (GamepadHotbarClientConfig.GAMEPAD_HOTBAR_TOGGLE.isFalse() || player == null) return;
        if (player.isSpectator()) return;
        int current = player.getInventory().getSelectedSlot();

        while (GamepadHotbarModBusEvents.LEFT.get().consumeClick())
            player.getInventory().setSelectedSlot(current == 0 ? 1 : 0);
        while (GamepadHotbarModBusEvents.UP.get().consumeClick())
            player.getInventory().setSelectedSlot(current == 2 ? 3 : 2);
        while (GamepadHotbarModBusEvents.RIGHT.get().consumeClick())
            player.getInventory().setSelectedSlot(current == 5 ? 4 : 5);
        while (GamepadHotbarModBusEvents.DOWN.get().consumeClick())
            player.getInventory().setSelectedSlot(current == 7 ? 6 : 7);
        while (GamepadHotbarModBusEvents.WEAPON.get().consumeClick()) player.getInventory().setSelectedSlot(8);
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiLayerEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (GamepadHotbarClientConfig.GAMEPAD_HOTBAR_TOGGLE.isFalse() || mc.options.hideGui) return;

        GuiGraphics guiGfx = event.getGuiGraphics();
        DeltaTracker parTick = event.getPartialTick();

        // Base variables
        int screenLeft = 43;
        int screenRight = guiGfx.guiWidth() - screenLeft;
        int flipVar1 = 1;
        int flipVar2 = 0;
        int scaleVar = GamepadHotbarClientConfig.PAD_X.get();

        Entity entity = mc.getCameraEntity();
        if (event.getName() == VanillaGuiLayers.HOTBAR && entity instanceof Player player) {
            if (player.isSpectator()) return;
            // Vanilla hotbar is not rendered
            event.setCanceled(true);

            ItemStack offHand = player.getOffhandItem();
            HumanoidArm offArm = player.getMainArm().getOpposite();

            // Configuration adjustments
            if (GamepadHotbarClientConfig.MIRROR_MODE.getAsBoolean()) {
                screenRight = screenLeft;
                screenLeft = guiGfx.guiWidth() - screenRight;
                scaleVar *= -1;
                flipVar1 *= -1;
                flipVar2 = 100;
            }
            if (GamepadHotbarClientConfig.SCALE_X.get() == HotbarScale.TYPE2) {
                screenLeft = (guiGfx.guiWidth() / 2) - (140 * flipVar1);
                screenRight = (guiGfx.guiWidth() / 2) + (140 * flipVar1);
            }
            if (GamepadHotbarClientConfig.SCALE_X.get() == HotbarScale.TYPE3) {
                screenLeft = (guiGfx.guiWidth() / 2) - (50 * flipVar1);
                screenRight = (guiGfx.guiWidth() / 2) + (50 * flipVar1);
            }
            if (GamepadHotbarClientConfig.SCALE_X.get() == HotbarScale.TYPE4) {
                screenLeft = 43 + flipVar2;
                screenRight = 143 - flipVar2;
            }
            if (GamepadHotbarClientConfig.SCALE_X.get() == HotbarScale.TYPE5) {
                screenLeft = guiGfx.guiWidth() - 143 + flipVar2;
                screenRight = guiGfx.guiWidth() - 43 - flipVar2;
            }
            int baseY = GamepadHotbarClientConfig.POS_Y.get() == HotbarPos.TOP ? 64 + GamepadHotbarClientConfig.PAD_Y.get() : guiGfx.guiHeight() - GamepadHotbarClientConfig.PAD_Y.get();

            // Adjustment variables
            int uWidth;
            int uPos;
            int xPos;
            int yPos;
            int slotY = baseY - 22;
            int selectY = slotY - 1;
            int itemY = slotY + 3;
            int baseX;
            int armVar = offArm == HumanoidArm.RIGHT ? 28 : 0;

            // Hotbar slots 1-9
            // Extra blitSprite calls are made here to fill out each slot pair with the outline portion of the hotbar texture
            for (int i = 0; i < 5; i++) {
                switch (i) {
                    case 1 -> {
                        //  X X
                        // .   .
                        // .   .         .
                        //  . .
                        uWidth = 40;
                        uPos = 41;
                        xPos = -20 + scaleVar;
                        yPos = -41;
                        baseX = screenLeft;
                        guiGfx.pose().rotateAbout((float) Math.toRadians(90.0F), baseX, slotY);
                        guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 0, 0, baseX + xPos - 1, slotY + yPos, 1, 22);
                        guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 181, 0, baseX + xPos + uWidth, slotY + yPos, 1, 22);
                    }
                    case 2 -> {
                        //  . .
                        // .   X
                        // .   X         .
                        //  . .
                        uWidth = 40;
                        uPos = 81;
                        xPos = -30;
                        yPos = -42 - scaleVar;
                        baseX = screenLeft;
                        guiGfx.pose().rotateAbout((float) Math.toRadians(90.0F), baseX, slotY);
                        guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 0, 0, baseX + xPos - 1, slotY + yPos, 1, 22);
                        guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 181, 0, baseX + xPos + uWidth, slotY + yPos, 1, 22);
                    }
                    case 3 -> {
                        //  . .
                        // .   .
                        // .   .         .
                        //  X X
                        uWidth = 40;
                        uPos = 121;
                        xPos = -20 + scaleVar;
                        yPos = 0;
                        baseX = screenLeft;
                        guiGfx.pose().rotateAbout((float) Math.toRadians(-90.0F), baseX, slotY);
                        guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 0, 0, baseX + xPos - 1, slotY + yPos, 1, 22);
                        guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 181, 0, baseX + xPos + uWidth, slotY + yPos, 1, 22);
                    }
                    case 4 -> {
                        //  . .
                        // .   .
                        // .   .         X
                        //  . .
                        uWidth = 21;
                        uPos = 161;
                        xPos = 4 - armVar - scaleVar;
                        yPos = -20;
                        baseX = screenRight;
                        guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 0, 0, baseX + xPos - 1, slotY + yPos, 1, 22);
                    }
                    default -> {
                        //  . .
                        // X   .
                        // X   .         .
                        //  . .
                        uPos = 0;
                        uWidth = 41;
                        xPos = -11;
                        yPos = -42 + scaleVar;
                        baseX = screenLeft;
                        guiGfx.pose().rotateAbout((float) Math.toRadians(-90.0F), baseX, slotY);
                        guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22, 181, 0, baseX + xPos + uWidth, slotY + yPos, 1, 22);
                    }
                }
                guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, 182, 22,
                        uPos, 0, baseX + xPos, slotY + yPos, uWidth, 22
                );
            }

            // Selected hotbar slot
            int selected = player.getInventory().getSelectedSlot();
            if (selected >= 0 && selected < 8) {
                baseX = screenLeft;
            } else baseX = screenRight;

            int selectX = baseX - 2;
            xPos = iteratePos(selected, scaleVar, armVar).x;
            yPos = iteratePos(selected, scaleVar, armVar).y;
            guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_SPRITE, selectX + xPos, selectY + yPos, 24, 23);

            // Off-hand hotbar slot
            if (!offHand.isEmpty()) {
                if (offArm == HumanoidArm.LEFT) {
                    guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_SPRITE, screenRight + 4 - 29 - scaleVar, slotY - 21, 29, 24);
                } else {
                    guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_SPRITE, screenRight + 4 - armVar + 20 - scaleVar, slotY - 21, 29, 24);
                }
            }

            // Custom tap button indicator
            Identifier tap = null;
            int tapOffset = 8;
            int color = ARGB.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F);
            if (GamepadHotbarModBusEvents.LEFT.get().isDown() || GamepadHotbarModBusEvents.RIGHT.get().isDown() || GamepadHotbarModBusEvents.UP.get().isDown() || GamepadHotbarModBusEvents.DOWN.get().isDown()) {
                color = ARGB.colorFromFloat(1.0F, 1.0F, 0.8F, 0.0F);
            }
            if (selected == 0 || selected == 1) {
                tap = TAP_HOTBAR_LEFT;
                tapOffset = 17;
            }
            if (selected == 2 || selected == 3) {
                tap = TAP_HOTBAR_UP;
            }
            if (selected == 4 || selected == 5) {
                tap = TAP_HOTBAR_RIGHT;
                tapOffset = -1;
            }
            if (selected == 6 || selected == 7) {
                tap = TAP_HOTBAR_DOWN;
            }
            if (selected >= 0 && selected < 8) {
                guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, tap, baseX - tapOffset + scaleVar, slotY - 18, 16, 16, color);
            }

            // Items
            int itemX;
            int l = 1;
            for (int j = 0; j < 9; j++) {
                xPos = iteratePos(j, scaleVar, armVar).x;
                yPos = iteratePos(j, scaleVar, armVar).y;
                if (j < 8) {
                    itemX = screenLeft + 2;
                } else itemX = screenRight + 2;
                renderSlot(guiGfx, itemX + xPos, itemY + yPos, parTick, player, player.getInventory().getItem(j), l++);
            }

            // Off-hand slot
            if (!offHand.isEmpty()) {
                itemX = screenRight + 2;
                renderSlot(guiGfx, itemX - 24 + armVar - scaleVar, itemY - 20, parTick, player, offHand, l);
            }

            // Hotbar attack indicator
            if (mc.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
                float f = player.getAttackStrengthScale(0.0F);
                if (f < 1.0F) {
                    int j2 = baseY - 62;
                    int k2 = screenRight + 5 - armVar - scaleVar;

                    int l1 = (int)(f * 19.0F);
                    guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, k2, j2, 18, 18);
                    guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - l1, k2, j2 + 18 - l1, 18, l1);
                }
            }
        }

        if (GamepadHotbarClientConfig.LOWER_STATUS.isFalse()) return;
        if (event.getName() == VanillaGuiLayers.PLAYER_HEALTH) guiGfx.pose().translate(0.0F, 24.0F);
        if (event.getName() == VanillaGuiLayers.SPECTATOR_TOOLTIP) guiGfx.pose().translate(0.0F, -24.0F);
    }

    @SubscribeEvent
    public static void inventoryScreenIcons(ContainerScreenEvent.Render.Foreground event) {
        AbstractContainerScreen<?> screen = event.getContainerScreen();
        if (GamepadHotbarClientConfig.GAMEPAD_HOTBAR_TOGGLE.isFalse() || screen.getMenu().getCarried().isEmpty()) return;

        GuiGraphics guiGfx = event.getGuiGraphics();

        // Ensures slot icons are always occupying the same slots on each screen
        int adjuster = 0;
        if (screen instanceof InventoryScreen) adjuster = 1;
        if (screen instanceof CreativeModeInventoryScreen creativeScreen)
            if (creativeScreen.isInventoryOpen()) adjuster = 2;

        guiGfx.pose().pushMatrix();
        guiGfx.pose().translate(0.0F, 0.0F);
        int slotCount = screen.getMenu().slots.size();

        for (int i = 0; i < 9; i++) {
            Slot slot = screen.getMenu().getSlot(i + slotCount - 9 - adjuster);
            if (slot.hasItem()) continue;

            Identifier icon = iterateSlotIcons(i);
            guiGfx.blitSprite(RenderPipelines.GUI_TEXTURED, icon, slot.x, slot.y, 16, 16);
        }
        guiGfx.pose().popMatrix();
    }
}
