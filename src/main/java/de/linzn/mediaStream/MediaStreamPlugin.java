/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.mediaStream;


import de.linzn.stem.STEMApp;
import de.linzn.stem.modules.pluginModule.STEMPlugin;


public class MediaStreamPlugin extends STEMPlugin {

    public static MediaStreamPlugin mediaStreamPlugin;
    private MediaManager mediaManager;

    public MediaStreamPlugin() {
        mediaStreamPlugin = this;
    }

    @Override
    public void onEnable() {
        this.mediaManager = new MediaManager();
        STEMApp.getInstance().getEventModule().getStemEventBus().register(new MediaListener());
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
