package com.github.mikndesu.spotify_integration.client.screen;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

@Environment(value=EnvType.CLIENT)
public class SpotifyScreen
extends Screen {
    private int delayTicker;
    private final Component causeOfDeath;
    private final boolean hardcore;
    private Component deathScore;
    private final List<Button> exitButtons = Lists.newArrayList();

    public SpotifyScreen(@Nullable Component component, boolean bl) {
        super(Component.translatable(bl ? "deathScreen.title.hardcore" : "deathScreen.title"));
        this.causeOfDeath = component;
        this.hardcore = bl;
    }

    @Override
    protected void init() {
        this.delayTicker = 0;
        this.exitButtons.clear();
        this.exitButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, this.hardcore ? Component.translatable("deathScreen.spectate") : Component.translatable("deathScreen.respawn"), button -> {
            this.minecraft.player.respawn();
            this.minecraft.setScreen(null);
        })));
        this.exitButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 96, 200, 20, Component.translatable("deathScreen.titleScreen"), button -> {
            if (this.hardcore) {
                this.exitToTitleScreen();
                return;
            }
            ConfirmScreen confirmScreen = new ConfirmScreen(this::confirmResult, Component.translatable("deathScreen.quit.confirm"), CommonComponents.EMPTY, Component.translatable("deathScreen.titleScreen"), Component.translatable("deathScreen.respawn"));
            this.minecraft.setScreen(confirmScreen);
            confirmScreen.setDelay(20);
        })));
        for (Button button2 : this.exitButtons) {
            button2.active = false;
        }
        this.deathScore = Component.translatable("deathScreen.score").append(": ").append(Component.literal(Integer.toString(this.minecraft.player.getScore())).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private void confirmResult(boolean bl) {
        if (bl) {
            this.exitToTitleScreen();
        } else {
            this.minecraft.player.respawn();
            this.minecraft.setScreen(null);
        }
    }

    private void exitToTitleScreen() {
        if (this.minecraft.level != null) {
            this.minecraft.level.disconnect();
        }
        this.minecraft.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
        this.minecraft.setScreen(new TitleScreen());
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        this.fillGradient(poseStack, 0, 0, this.width, this.height, 0x60500000, -1602211792);
        poseStack.pushPose();
        poseStack.scale(2.0f, 2.0f, 2.0f);
        DeathScreen.drawCenteredString(poseStack, this.font, this.title, this.width / 2 / 2, 30, 0xFFFFFF);
        poseStack.popPose();
        if (this.causeOfDeath != null) {
            DeathScreen.drawCenteredString(poseStack, this.font, this.causeOfDeath, this.width / 2, 85, 0xFFFFFF);
        }
        DeathScreen.drawCenteredString(poseStack, this.font, this.deathScore, this.width / 2, 100, 0xFFFFFF);
        if (this.causeOfDeath != null && j > 85 && j < 85 + this.font.lineHeight) {
            Style style = this.getClickedComponentStyleAt(i);
            this.renderComponentHoverEffect(poseStack, style, i, j);
        }
        super.render(poseStack, i, j, f);
    }

    @Nullable
    private Style getClickedComponentStyleAt(int i) {
        if (this.causeOfDeath == null) {
            return null;
        }
        int j = this.minecraft.font.width(this.causeOfDeath);
        int k = this.width / 2 - j / 2;
        int l = this.width / 2 + j / 2;
        if (i < k || i > l) {
            return null;
        }
        return this.minecraft.font.getSplitter().componentStyleAtWidth(this.causeOfDeath, i - k);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        Style style;
        if (this.causeOfDeath != null && e > 85.0 && e < (double)(85 + this.font.lineHeight) && (style = this.getClickedComponentStyleAt((int)d)) != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
            this.handleComponentClicked(style);
            return false;
        }
        return super.mouseClicked(d, e, i);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        ++this.delayTicker;
        if (this.delayTicker == 20) {
            for (Button button : this.exitButtons) {
                button.active = true;
            }
        }
    }
}


