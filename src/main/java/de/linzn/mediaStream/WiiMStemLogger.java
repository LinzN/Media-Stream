/*
 * Copyright (c) 2026 MirraNET, Niklas Linz. All rights reserved.
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

import de.linzn.stem.modules.pluginModule.STEMPlugin;
import de.linzn.wiim.api.IWiiMLogger;

import java.util.Objects;

/**
 * Routes log output of the WiiM library to the plugin logger. The library logs every failed
 * poll (several times per second while the device is offline), so repeated identical warnings
 * are only logged once every 10 minutes.
 */
public class WiiMStemLogger implements IWiiMLogger {

    private static final long REPEAT_INTERVAL_MS = 10 * 60 * 1000;

    private final STEMPlugin plugin;
    private String lastWarning;
    private long lastWarningTime;

    public WiiMStemLogger(STEMPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void error(Object o, Exception e) {
        if (e != null) {
            this.plugin.getPluginLogger().error(String.valueOf(o), e);
        } else {
            this.plugin.getPluginLogger().error(String.valueOf(o));
        }
    }

    @Override
    public void warning(Object o, Exception e) {
        String message = e != null ? o + ": " + e.getMessage() : String.valueOf(o);
        if (this.shouldLog(message)) {
            this.plugin.getPluginLogger().warning(message);
        } else {
            this.plugin.getPluginLogger().debug(message);
        }
    }

    private synchronized boolean shouldLog(String message) {
        long now = System.currentTimeMillis();
        if (Objects.equals(message, this.lastWarning) && now - this.lastWarningTime < REPEAT_INTERVAL_MS) {
            return false;
        }
        this.lastWarning = message;
        this.lastWarningTime = now;
        return true;
    }

    @Override
    public void info(Object o) {
        this.plugin.getPluginLogger().info(String.valueOf(o));
    }

    @Override
    public void debug(Object o) {
        this.plugin.getPluginLogger().debug(String.valueOf(o));
    }
}
