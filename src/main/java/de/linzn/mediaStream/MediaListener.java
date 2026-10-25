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
import de.linzn.homeDevices.devices.interfaces.MqttDevice;
import de.linzn.homeDevices.devices.interfaces.MqttSwitch;
import de.linzn.homeDevices.devices.other.LEDDevice;
import de.linzn.stem.modules.pluginModule.STEMPlugin;
import de.linzn.wiim.api.DeviceMonitor;
import de.linzn.wiim.api.WiiMClient;
import de.linzn.wiim.api.model.PlayerStatus;

public class MediaListener extends DeviceMonitor {

    private final STEMPlugin plugin;
    /* only log the first poll error of an outage */
    private volatile boolean pollErrorLogged;

    public MediaListener(STEMPlugin plugin, WiiMClient client) {
        super(client);
        this.plugin = plugin;
    }

    @Override
    protected void onStartedPlaying(PlayerStatus status) {
        this.pollErrorLogged = false;
        PlayerStatus.PlaybackMode mode = status.getPlaybackMode();
        this.plugin.getPluginLogger().info("WiiM device started playing on playback mode: " + (mode != null ? mode.name() : "unknown"));
        LEDDevice ledDevice = this.getLedDevice();
        if (ledDevice != null) {
            if (mode == PlayerStatus.PlaybackMode.AIRPLAY || mode == PlayerStatus.PlaybackMode.CAST) {
                ledDevice.setLEDMode(2, 255, 0, 0);
            } else if (mode == PlayerStatus.PlaybackMode.OPTICAL_IN) {
                ledDevice.setLEDMode(1, 0, 0, 10);
            } else {
                ledDevice.setLEDMode(1, 10, 10, 10);
            }
        }
        MqttSwitch amplifier = this.getAmplifier();
        if (amplifier != null) {
            if (!amplifier.hasData()) {
                this.plugin.getPluginLogger().warning("Unable to power up device " + amplifier.getDeviceHardAddress() + ". Not ready yet!");
            } else if (!amplifier.isSwitchedOn()) {
                amplifier.switchDevice(true);
            }
        }
    }

    @Override
    protected void onStopped(PlayerStatus status) {
        this.plugin.getPluginLogger().info("WiiM device stopped playing!");
        LEDDevice ledDevice = this.getLedDevice();
        if (ledDevice != null) {
            ledDevice.setLEDMode(5, 255, 0, 0);
        }
    }

    @Override
    protected void onPollError(Exception error) {
        if (!this.pollErrorLogged) {
            this.pollErrorLogged = true;
            this.plugin.getPluginLogger().warning("WiiM device polling error: " + error.getMessage());
        }
    }

    @Override
    protected void onStandby() {
        this.plugin.getPluginLogger().info("WiiM device is now in standby. Power off all hardware devices!");
        MqttSwitch amplifier = this.getAmplifier();
        if (amplifier != null) {
            amplifier.switchDevice(false);
        }
        LEDDevice ledDevice = this.getLedDevice();
        if (ledDevice != null) {
            ledDevice.setLEDMode(0, 0, 0, 0);
        }
    }

    private MqttDevice getDevice(String configPath) {
        String deviceName = this.plugin.getDefaultConfig().getString(configPath);
        HomeDevicesPlugin homeDevices = HomeDevicesPlugin.homeDevicesPlugin;
        MqttDevice device = homeDevices != null && homeDevices.getDeviceManager() != null ? homeDevices.getDeviceManager().getMqttDevice(deviceName) : null;
        if (device == null) {
            this.plugin.getPluginLogger().warning("No home device '" + deviceName + "' configured in " + configPath);
        }
        return device;
    }

    private LEDDevice getLedDevice() {
        MqttDevice device = this.getDevice("led.hardwareAddress");
        if (device != null && !(device instanceof LEDDevice)) {
            this.plugin.getPluginLogger().warning("Device " + device.getConfigName() + " is no LED device");
            return null;
        }
        return (LEDDevice) device;
    }

    private MqttSwitch getAmplifier() {
        MqttDevice device = this.getDevice("amplifier.hardwareAddress");
        if (device != null && !(device instanceof MqttSwitch)) {
            this.plugin.getPluginLogger().warning("Device " + device.getConfigName() + " is no switch");
            return null;
        }
        return (MqttSwitch) device;
    }
}
