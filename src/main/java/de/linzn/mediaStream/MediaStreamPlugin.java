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


import de.stem.stemSystem.modules.pluginModule.STEMPlugin;


public class MediaStreamPlugin extends STEMPlugin {

    public static MediaStreamPlugin mediaStreamPlugin;


    public MediaStreamPlugin() {
        mediaStreamPlugin = this;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
    }
}
