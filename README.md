English | [日本語](README.ja.md)

# ShishamoTech

An addon mod for **GTCEu Modern (GregTech CE Unofficial)**.
Adds massively-parallel "mega" multiblocks spanning every tier, from the Steam age all the way up to MAX.

- Mod ID: `shishamo_tech`
- Target version: Minecraft 1.20.1 / Forge 47.x
- License: [MIT License](LICENSE)

## Overview

ShishamoTech wraps GTCEuM's standard multiblocks internally and applies a large parallel multiplier (default 64x, configurable) to produce "mega multiblocks." It covers a wide range of tiers, from the Steam age through each Electric tier, plus AE2-integrated machines.

## Key Features

### Steam-era Mega Multiblocks
- Mega Steam Furnace
- Mega Steam Alloy Smelter
- Mega Steam Extractor
- Mega Steam Compressor
- Mega Steam Grinder
- Mega Steam Hammer
- Mega Steam Rock Crusher
- God Steam Boiler

### Electric-era Mega Multiblocks
- Large Arc Furnace
- Large Assembly Plant
- Large Distillation Tower
- Large Electrolyzer
- Large Grinding Plant
- Large Smelting Plant
- Large Washing Plant
- LCR Cluster
- Hyper Tower Centrifuge
- Eternal Force Freezer
- (Non) Omnipotent Universe Forge

### AE2 Integration (only active when AE2 is installed)
- Press-Free Inscriber (MV / HV / IV / EV)

### Config
The following options are available in `config/shishamo_tech-common.toml`:

| Option | Description | Default |
|---|---|---|
| `parallelMultiplier` | Parallel multiplier applied to all machines | 64 |
| `enableSteamMachines` | Enable Steam-era mega multiblocks | true |
| `enableElectricMachines` | Enable Electric-era mega multiblocks | true |
| `enableAE2Integration` | Enable AE2 integration machines (requires AE2) | true |

## Dependencies

| Mod | Version | Required/Optional |
|---|---|---|
| GTCEu Modern | 7.5.3+ | Required |
| Applied Energistics 2 | 15.0.23+ | Optional (required for Press-Free Inscriber) |
| JEI | 15.20.0.115+ | Optional |

## Building

### Prerequisites

- JDK 17 (required by Forge 1.20.1 / Gradle 8.8)
  - If your system's default Java is different, point `JAVA_HOME` at your JDK 17 install:
    ```bash
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
    ```

### 1. Data generation

Regenerates generated resources such as blockstates, models, and language files.

```bash
./gradlew runData
```

On Windows:

```bash
gradlew.bat runData
```

### 2. Build

```bash
./gradlew build
```

On Windows:

```bash
gradlew.bat build
```

Once the build finishes, the mod jar will be located under `build/libs/`.

> **Note:** If you've added or changed resources, run `runData` before `build`.

## Directory Structure (overview)

```
src/main/java/shishamo_tech/
├─ ShishamoTech.java        # @Mod entry point
├─ STAddon.java             # GTCEuM addon registration
├─ STRegistration.java      # GTRegistrate initialization
├─ api/                     # Public-facing interfaces
├─ client/                  # Client-only logic
├─ common/
│  ├─ machine/
│  │  ├─ steam/             # Steam-era mega multiblocks
│  │  ├─ electric/          # Electric-era mega multiblocks (per tier)
│  │  └─ ae2/               # AE2 integration multiblocks
│  ├─ recipe/               # Recipe type definitions
│  ├─ recipegen/            # Recipe datagen
│  └─ data/                 # Material/item definitions
├─ config/                  # Forge config
└─ integration/
   ├─ ae2/                  # AE2 integration
   └─ jei/                  # JEI plugin
```

## License

This project is released under the [MIT License](LICENSE).
