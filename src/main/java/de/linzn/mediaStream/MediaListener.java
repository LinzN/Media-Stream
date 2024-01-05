package de.linzn.mediaStream;

import de.linzn.homeDevices.HomeDevicesPlugin;
import de.linzn.homeDevices.devices.exceptions.DeviceNotInitializedException;
import de.linzn.homeDevices.devices.interfaces.MqttSwitch;
import de.linzn.homeDevices.devices.other.LEDDevice;
import de.linzn.mediaStream.events.WiimActiveEvent;
import de.linzn.mediaStream.events.WiimApiErrorEvent;
import de.linzn.mediaStream.events.WiimStandbyEvent;
import de.linzn.mediaStream.events.WiimStatusChangedEvent;
import de.linzn.wiimJavaApi.exceptions.WiimAPIDataPushException;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.modules.eventModule.handler.StemEventHandler;
import de.stem.stemSystem.modules.eventModule.handler.StemEventPriority;

public class MediaListener {

    @StemEventHandler(priority = StemEventPriority.NORMAL)
    public void onWiimStatusChanged(WiimStatusChangedEvent event) {
        if (event.isInitializeEvent()) {
            STEMSystemApp.LOGGER.CORE("Initialize WiiM Device status: " + event.getNewStatus());
            String ledDeviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("led.hardwareAddress");
            LEDDevice ledDevice = (LEDDevice) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(ledDeviceName);
            ledDevice.setLEDMode(4, 0, 0, 0);
        } else {
            STEMSystemApp.LOGGER.CORE("Wiim Device status changed from: " + event.getOldStatus() + " to: " + event.getNewStatus());
            if (!event.getOldStatus().equalsIgnoreCase(event.getNewStatus())) {
                if (event.getNewStatus().equalsIgnoreCase("stop") || event.getNewStatus().equalsIgnoreCase("none")) {
                    String ledDeviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("led.hardwareAddress");
                    LEDDevice ledDevice = (LEDDevice) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(ledDeviceName);
                    ledDevice.setLEDMode(5, 255, 0, 0);
                } else if (event.getNewStatus().equalsIgnoreCase("play")) {
                    String ledDeviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("led.hardwareAddress");
                    LEDDevice ledDevice = (LEDDevice) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(ledDeviceName);
                    if(event.getMode() == 5){
                        ledDevice.setLEDMode(2, 255,0,0);
                    } else {
                        ledDevice.setLEDMode(1, 10,10,10);
                    }

                    String deviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("amplifier.hardwareAddress");
                    MqttSwitch mqttSwitch = (MqttSwitch) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(deviceName);
                    try {
                        if(!mqttSwitch.getDeviceStatus()){
                            mqttSwitch.switchDevice(true);
                        }
                    } catch (DeviceNotInitializedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @StemEventHandler(priority = StemEventPriority.NORMAL)
    public void onWiimStandby(WiimStandbyEvent event) {
        STEMSystemApp.LOGGER.CORE("Wiim Device is going to standby. Switching off other hardware!");
        String deviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("amplifier.hardwareAddress");
        MqttSwitch mqttSwitch = (MqttSwitch) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(deviceName);
        mqttSwitch.switchDevice(false);
        String ledDeviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("led.hardwareAddress");
        LEDDevice ledDevice = (LEDDevice) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(ledDeviceName);
        ledDevice.setLEDMode(0, 0,0,0);
    }

    @StemEventHandler(priority = StemEventPriority.NORMAL)
    public void onWiimActive(WiimActiveEvent event) {
        STEMSystemApp.LOGGER.CORE("Wiim Device is wakeup from standby. Enable all hardware!");
        String deviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("amplifier.hardwareAddress");
        MqttSwitch mqttSwitch = (MqttSwitch) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(deviceName);
        mqttSwitch.switchDevice(true);
        String ledDeviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("led.hardwareAddress");
        LEDDevice ledDevice = (LEDDevice) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(ledDeviceName);
        if(event.getMode() == 5){
            ledDevice.setLEDMode(2, 255,0,0);
        } else {
            ledDevice.setLEDMode(1, 10,10,10);
        }

    }

    @StemEventHandler(priority = StemEventPriority.NORMAL)
    public void onWiimApiError(WiimApiErrorEvent event) {
        STEMSystemApp.LOGGER.CORE("Api Error in Wiim device. Fixing device...");
        String deviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("amplifier.hardwareAddress");
        MqttSwitch mqttSwitch = (MqttSwitch) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(deviceName);

        try {
            if (mqttSwitch.getDeviceStatus()) {
                mqttSwitch.switchDevice(false);
            }
        } catch (DeviceNotInitializedException ignored) {
        }
        try {
            MediaStreamPlugin.mediaStreamPlugin.getMediaManager().getWiimAPI().getWiimPlayer().set_stop();
        } catch (WiimAPIDataPushException ignored) {

        }
    }
}
