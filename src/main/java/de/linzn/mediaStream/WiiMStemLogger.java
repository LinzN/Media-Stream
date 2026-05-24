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

import de.linzn.stem.STEMApp;
import de.linzn.wiim.api.IWiiMLogger;

public class WiiMStemLogger implements IWiiMLogger {
    @Override
    public void error(Object o, Exception e) {
        STEMApp.LOGGER.ERROR(o);
        if(e != null){
            STEMApp.LOGGER.ERROR(e);
        }
    }

    @Override
    public void warning(Object o, Exception e) {
        STEMApp.LOGGER.WARNING(o);
        if(e != null){
            STEMApp.LOGGER.WARNING(e);
        }
    }

    @Override
    public void info(Object o) {
        STEMApp.LOGGER.INFO(o);
    }

    @Override
    public void debug(Object o) {
        STEMApp.LOGGER.DEBUG(o);
    }
}
