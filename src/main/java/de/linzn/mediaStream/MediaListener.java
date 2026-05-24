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

import de.linzn.homeDevices.HomeDevicesPlugin;
import de.linzn.homeDevices.devices.exceptions.DeviceNotInitializedException;
import de.linzn.homeDevices.devices.interfaces.MqttSwitch;
import de.linzn.homeDevices.devices.other.LEDDevice;
import de.linzn.stem.STEMApp;
import de.linzn.wiim.api.DeviceMonitor;
import de.linzn.wiim.api.WiiMClient;
import de.linzn.wiim.api.model.PlayerStatus;

public class MediaListener extends DeviceMonitor {

    public MediaListener(WiiMClient client) {
        super(client);
    }

    @Override
    protected void onStartedPlaying(PlayerStatus status) {
        String ledDeviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("led.hardwareAddress");
        LEDDevice ledDevice = (LEDDevice) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(ledDeviceName);
        if (status.getPlaybackMode() == PlayerStatus.PlaybackMode.AIRPLAY || status.getPlaybackMode() == PlayerStatus.PlaybackMode.CAST) {
            ledDevice.setLEDMode(2, 255, 0, 0);
        }
        else if(status.getPlaybackMode() == PlayerStatus.PlaybackMode.OPTICAL_IN){
            ledDevice.setLEDMode(1, 0, 0, 10);
        }else {
            ledDevice.setLEDMode(1, 10, 10, 10);
        }

        String deviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("amplifier.hardwareAddress");
        MqttSwitch mqttSwitch = (MqttSwitch) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(deviceName);
        try {
            if (!mqttSwitch.getDeviceStatus()) {
                mqttSwitch.switchDevice(true);
            }
        } catch (DeviceNotInitializedException e) {
            STEMApp.LOGGER.WARNING("Unable to power up device " + mqttSwitch.getDeviceHardAddress() + ". Not ready yet!");
        }
    }

    @Override
    protected void onStopped(PlayerStatus status) {
        String ledDeviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("led.hardwareAddress");
        LEDDevice ledDevice = (LEDDevice) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(ledDeviceName);
        ledDevice.setLEDMode(5, 255, 0, 0);
    }

    @Override
    protected void onStandby() {
        STEMApp.LOGGER.CORE("Wiim Device is going to standby. Switching off other hardware!");
        String deviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("amplifier.hardwareAddress");
        MqttSwitch mqttSwitch = (MqttSwitch) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(deviceName);
        mqttSwitch.switchDevice(false);
        String ledDeviceName = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("led.hardwareAddress");
        LEDDevice ledDevice = (LEDDevice) HomeDevicesPlugin.homeDevicesPlugin.getDeviceManager().getMqttDevice(ledDeviceName);
        ledDevice.setLEDMode(0, 0, 0, 0);
    }

}
