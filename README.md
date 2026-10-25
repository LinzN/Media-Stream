![MirraNET Logo](https://raw.githubusercontent.com/LinzN/Stem/master/stem-core/src/main/resources/header-logo.png)

# media-stream

> Switches the amplifier and LED light of a WiiM streamer from [STEM](https://github.com/LinzN/Stem).

media-stream watches a WiiM streamer (WiiM Pro, WiiM Mini, ...) with
[wiim-java-api](https://github.com/LinzN/wiim-java-api). When playback starts it switches on the amplifier
and sets an LED light effect that matches the source; when the streamer goes to standby it switches
everything off again. The devices are controlled through [home-devices](https://github.com/LinzN/home-devices).

---

## Features

| WiiM state                      | Amplifier | LED (mode, r, g, b)  |
|---------------------------------|-----------|----------------------|
| Starts playing (AirPlay / Cast) | on        | `2, 255, 0, 0`       |
| Starts playing (optical in)     | on        | `1, 0, 0, 10`        |
| Starts playing (other source)   | on        | `1, 10, 10, 10`      |
| Stopped                         | –         | `5, 255, 0, 0`       |
| Standby (2 minutes stopped)     | off       | `0, 0, 0, 0`         |

- Missing or wrongly configured devices are logged, they do not stop the monitor
- Poll errors while the streamer is offline are logged once, not on every poll

---

## Requirements

| Requirement | Version                                                         |
|-------------|-----------------------------------------------------------------|
| STEM        | 1.2.0 or newer (`stem-api`)                                     |
| Plugins     | [home-devices](https://github.com/LinzN/home-devices)           |
| Library     | [wiim-java-api](https://github.com/LinzN/wiim-java-api) 2.0.1 or newer |
| Devices     | a switch (amplifier) and an LED device in home-devices          |

---

## Installation

1. Build the plugin with `mvn clean package` (or take the build from Jenkins).
2. Copy `target/media-stream.jar` into the `plugins` folder of STEM and the libraries from
   `target/libraries` as `libraries.zip` (folder `libraries` inside) into `plugins/media-stream/`.
3. Start STEM once to create `plugins/media-stream/config.yml`.
4. Enter the address of the streamer and the device names, then restart STEM.

---

## Configuration (`config.yml`)

| Key                         | Default      | Description                                                  |
|-----------------------------|--------------|--------------------------------------------------------------|
| `wiimDevice.ipAddress`      | `10.50.0.99` | IP address of the WiiM streamer                              |
| `wiimDevice.pollIntervalMs` | `600`        | Poll interval in ms (minimum 200)                            |
| `amplifier.hardwareAddress` | `xxxxx`      | `deviceInfo.name` of the amplifier switch in home-devices    |
| `led.hardwareAddress`       | `xxxxx`      | `deviceInfo.name` of the LED device in home-devices          |

---

## License

Copyright (c) MirraNET, Niklas Linz. Licensed under the
[GNU Lesser General Public License v3.0](https://www.gnu.org/licenses/lgpl-3.0.html).
