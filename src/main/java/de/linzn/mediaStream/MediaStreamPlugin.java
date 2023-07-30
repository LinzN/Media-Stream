/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mediaStream;


import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.modules.pluginModule.STEMPlugin;


public class MediaStreamPlugin extends STEMPlugin {

    public static MediaStreamPlugin mediaStreamPlugin;
    private MediaManager mediaManager;

    public MediaStreamPlugin() {
        mediaStreamPlugin = this;
    }

    @Override
    public void onEnable() {
        this.mediaManager = new MediaManager();
        STEMSystemApp.getInstance().getEventModule().getStemEventBus().register(new MediaListener());
        this.initConfig();
    }

    @Override
    public void onDisable() {
    }

    public MediaManager getMediaManager() {
        return mediaManager;
    }

    private void initConfig() {
        this.getDefaultConfig().getString("wiimDevice.ipAddress", "10.50.0.99");
        this.getDefaultConfig().getString("amplifier.hardwareAddress", "xxxxx");
        this.getDefaultConfig().save();
    }
}
