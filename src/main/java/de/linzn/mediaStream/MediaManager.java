package de.linzn.mediaStream;

import de.linzn.wiimJavaApi.WiimAPI;
import de.stem.stemSystem.STEMSystemApp;

import java.util.concurrent.TimeUnit;

public class MediaManager {
    private String ipAddress;
    private WiimAPI wiimAPI;

    public MediaManager() {
        this.ipAddress = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("wiimDevice.ipAddress", "10.50.0.99");
        this.wiimAPI = new WiimAPI(this.ipAddress);
        this.wiimAPI.setWiimLogger(new WiimCustomLogger());
        this.wiimAPI.setSslCheck(false);
        this.wiimAPI.setPullInterval(500, TimeUnit.MILLISECONDS);
        this.wiimAPI.connect();
        STEMSystemApp.getInstance().getScheduler().runRepeatScheduler(MediaStreamPlugin.mediaStreamPlugin, new WiimMonitor(this), 20, 1, TimeUnit.SECONDS);
    }

    public WiimAPI getWiimAPI() {
        return this.wiimAPI;
    }
}
