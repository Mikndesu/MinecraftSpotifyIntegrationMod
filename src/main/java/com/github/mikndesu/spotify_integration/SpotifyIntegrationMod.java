/*
 Copyright (c) 2022 Mikndesu

 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in
 the Software without restriction, including without limitation the rights to
 use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 the Software, and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.mikndesu.spotify_integration;


import org.lwjgl.glfw.GLFW;

import com.github.mikndesu.spotify_integration.client.screen.SpotifyScreen;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class SpotifyIntegrationMod implements ClientModInitializer {

    public static final String MODID = "spotify_integration";
    private static KeyMapping keyMapping;
    private static Minecraft minecraft = Minecraft.getInstance();

    @Override
    public void onInitializeClient() {
        keyMapping = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.spotify_integration.open_integration_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y, "Spotify Integration Mod"));
        ClientTickEvents.END_CLIENT_TICK.register(client->{
            while(keyMapping.consumeClick()) {
                minecraft.setScreen(new SpotifyScreen(null,false));
            }
        });
    }
    
}
