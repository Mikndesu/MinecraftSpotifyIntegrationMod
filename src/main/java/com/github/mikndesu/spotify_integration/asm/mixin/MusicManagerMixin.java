package com.github.mikndesu.spotify_integration.asm.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MusicManager.class)
public class MusicManagerMixin {
    @Final
    @Shadow
    private RandomSource random;
    @Final
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private SoundInstance currentMusic;
    @Shadow
    private int nextSongDelay;
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void tick() {
        MusicManager musicManager = (MusicManager) (Object) this;
        Music music = new Music(new SoundEvent(new ResourceLocation("spotify_integration","sample")), 20, 600, true);
        if (currentMusic != null) {
            if (!music.getEvent().getLocation().equals(currentMusic.getLocation()) && music.replaceCurrentMusic()) {
                minecraft.getSoundManager().stop(currentMusic);
                nextSongDelay = Mth.nextInt(random, 0, music.getMinDelay() / 2);
            }
            if (!minecraft.getSoundManager().isActive(currentMusic)) {
                currentMusic = null;
                nextSongDelay = Math.min(nextSongDelay, Mth.nextInt(random, music.getMinDelay(), music.getMaxDelay()));
            }
        }
        nextSongDelay = Math.min(nextSongDelay, music.getMaxDelay());
        if (currentMusic == null && nextSongDelay-- <= 0) {
            musicManager.startPlaying(music);
        }
    }
}
