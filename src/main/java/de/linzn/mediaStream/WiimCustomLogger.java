package de.linzn.mediaStream;

import de.linzn.wiimJavaApi.IWiimLogger;
import de.stem.stemSystem.STEMSystemApp;

public class WiimCustomLogger implements IWiimLogger {
    @Override
    public void error(Object o) {
        STEMSystemApp.LOGGER.ERROR(o);
    }

    @Override
    public void warning(Object o) {
        STEMSystemApp.LOGGER.WARNING(o);
    }

    @Override
    public void info(Object o) {
        STEMSystemApp.LOGGER.INFO(o);
    }

    @Override
    public void debug(Object o) {
        STEMSystemApp.LOGGER.DEBUG(o);
    }
}
