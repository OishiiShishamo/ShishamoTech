# AGENTS.md

<!--
Optimization Goal:
    - Primary: Development RPM (response count)
    - Secondary: Cross-mod consistency

Token usage is NOT an optimization target.
Context windows exist to be consumed.
The agent should maximize completed work per response.
-->

# ShishamoTech Development AGENT

**READ LLM_Context/ROADMAP.md FIRST.** All development context, phase priorities, architecture decisions, and parallel count policies are defined there.

---

## Project Identity

- **mod-id**: `shishamo_tech`
- **Type**: GTCEuM Addon (hard dependency)
- **Target**: Minecraft 1.20.1 / Forge 47.x
- **Core Concept**: Steam→MAX全ティア網羅のクソデカマルチブロック追加
- **AE2 Integration**: Press-Free Multiblock Inscriber + ME Hatch/Bus連携

---

## Target Environment

- Minecraft 1.20.1
- ModLoader: Forge 47.x
- Hard dependencies: GTCEuM 7.5.3
- Soft dependencies: AE2 15.0.23 (and addons), JEI 15.2.0.27
- **Java**: Must use `JAVA_HOME=/usr/lib/jvm/java-17-openjdk` for all Gradle commands (system default is Java 25 which breaks Gradle 8.8)
- Existing code conventions take priority over generic Java conventions
- Assume modpack compatibility matters unless instructed otherwise
- English documentation, source code, and upstream implementations are primary references

---

## Architecture (from LLM_Context/ROADMAP.md)

### Key Patterns
- **Registration**: All machines via `GTRegistrate.machine()` pattern
- **Traits**: Custom functionality as `MachineTrait` (STParallelTrait, STVentTrait, STInscriberFilterTrait)
- **RecipeTypes**: Centralized in `STRecipeTypes.java`
- **Structures**: Per-machine `FactoryBlockPattern`, NOT centralized (MultiblockPatternBuilder does not exist in 7.5.3 jar)
- **Config**: Parallel multiplier (default 64), machine enable/disable, AE2 integration toggle

### Build & Dependency Notes
- GTCEuM 7.5.3 bundles Registrate + ldlib as JarJar — NOT on compileClasspath by default
- Added `compileOnly files("libs/Registrate-MC1.20-1.3.11.jar")` and `compileOnly files("libs/ldlib-forge-1.20.1-1.0.40.b.jar")` to build.gradle
- These jars were extracted from the GTCEuM deobf jar's `META-INF/jarjar/` directory
- `GTRecipeTypes.ROCK_CRUSHER_RECIPES` does **not** exist; Rock Crusher uses `ROCK_BREAKER_RECIPES`
- `STConfig.SPEC` must be `public` (accessed from `ShishamoTech.java` in different package)

### Package Structure
```
shishamo_tech/
├─ ShishamoTech.java          ← @Mod entry, GTRegistrate init only
├─ api/                        ← Public-facing interfaces
├─ common/
│   ├── machine/
│   │   ├── steam/             ← Steam-era multiblocks
│   │   ├── electric/          ← Electric multiblocks (per tier)
│   │   └── ae2/               ← AE2 integration multiblocks
│   ├── recipe/                ← GTRecipeType definitions
│   ├── recipegen/             ← Recipe datagen
│   └── data/                  ← Material/Item definitions
├─ client/                     ← Client-only
├─ integration/
│   ├── ae2/                   ← AE2 GridNode etc.
│   └─ jei/                    ← JEI Plugin
└─ config/                     ← ForgeConfig
```

---

## Development Philosophy

- Minimize response count, not token count
- Read more before modifying
- Prefer evidence over assumptions
- Fix root causes rather than symptoms
- Preserve mod compatibility whenever possible
- **Follow LLM_Context/ROADMAP.md phase order** unless explicitly instructed otherwise

---

## GTCEuM Addon Conventions

- Extend GTCEuM base classes: `SteamParallelMultiblockMachine`, `WorkableElectricMultiblockMachine`, etc.
- Use `@GTAddon` + `IGTAddon` for addon registration
- Use GTCEuM `PartAbility` for hatch/bus interfaces
- Use GTCEuM `GTRecipeTypes` where possible; custom types in `STRecipeTypes.java`
- Parallel count = GTCEuM standard × 64 (configurable)
- Voltage tiers follow `GTValues.V[]`: ULV=8, LV=32, MV=128, HV=512, EV=2048, IV=8192, LuV=32768, ZPM=131072, UV=524288, UHV=2097152

---

## AE2 Integration Conventions

- Custom AE2 machines implement `IGridConnectedMachine`
- Use `GridNodeHolder` trait for ME connectivity
- ME Hatch/Bus from GTCEuM existing — do NOT duplicate
- Press-Free Inscriber: AE2 Inscriber recipes referenced, press items excluded from input/output, empty-output recipes ignored
- Follow AE2 philosophy: minimize main network channels, use subnets, no Storage Bus as logistics center
- Load AE2 skills when working on AE2 integration (ae2-philosophy → ae2-subnet → ae2-gregtech)

---

## Information Gathering

- Read the entire stacktrace, not only the top exception
- Inspect related classes, registries, configs, and integration code before modifying
- When a crash involves multiple mods, investigate all participating mods
- Search call hierarchy before changing behavior
- Read neighboring code even if it appears unrelated
- **Check LLM_Context/ROADMAP.md before starting any new feature** to align with phase priority

Examples:
- Registry issue → inspect registration order
- Capability issue → inspect provider and consumer
- JEI issue → inspect plugin registration
- Network issue → inspect packet sender and receiver
- AE2 integration issue → load ae2-troubleshooting skill

Never assume the first stacktrace line identifies the actual bug.

---

## Code Style

- Preserve existing naming conventions
- Follow GTCEuM addon patterns (GTRegistrate, MachineTrait, MachineBuilder)
- Class prefix: `ST` for ShishamoTech-specific classes (STSteamParallelMultiblock, STParallelTrait, etc.)
- Avoid unnecessary refactoring
- Minimize formatting-only changes
- Add comments only when behavior is non-obvious
- Avoid introducing dependencies without explicit approval
- Prefer small localized fixes

---

## Compatibility Rules

- Existing worlds must remain loadable whenever possible
- Save format changes require explicit justification
- Network protocol changes require version handling
- Config changes should maintain backward compatibility
- API changes should avoid breaking dependent mods
- GTCEuM version updates must be tested before adopting

Breaking compatibility requires explicit explanation.

---

## Performance

- Measure before optimizing
- Avoid speculative optimization
- Consider:
    - Tick cost
    - Allocation rate
    - World load cost
    - Network traffic
    - Chunk loading impact

Micro-optimizations are lower priority than reducing tick load.

**High-parallel multiblock consideration**: At ×64 parallels, recipe logic runs once per tick but produces/consumes ×64 items. Verify that Item/Fluid handler operations scale linearly, not quadratically. Profile with 2000+ machines before releasing.

---

## Crash Investigation

When investigating a crash:

1. Read full crash report
2. Read latest.log
3. Identify all involved mods
4. Determine actual failing code
5. Determine why invalid state occurred
6. Verify fix against similar code paths

Never fix only the symptom.

---

## Mixin Rules

- Prefer precise targets
- Avoid broad injections
- Minimize priority usage
- Document cancellable injections
- Check compatibility with other common mods

A working mixin that breaks another mod is considered a failure.

---

## Forge Event Rules

- Subscribe only where necessary
- Avoid expensive operations inside TickEvent
- Avoid global event handlers when scoped handlers are possible
- Server and client logic must remain separated

---

## Integration Support

When integrations exist:

- GTCEuM: hard dependency, always present
- AE2: soft dependency, guard with `isModLoaded()` checks
- JEI: soft dependency, register plugin only if present
- Preserve functionality when soft dependency mods are absent
- AE2 addon integration: follow ae2-addons skill for compatibility patterns

---

## Resource Management

- Avoid repeated registry lookups inside ticks
- Cache expensive operations when safe
- Avoid unnecessary object allocation
- Profile tile entities with large counts

One machine is irrelevant.
Two thousand machines in a GregTech factory are not.
**Sixty-four thousand parallel operations per tick definitely are not.**

---

## Testing

Before completion:

- Launch the game
- Verify loading without crashes
- Verify affected functionality
- Check logs for warnings or errors
- Test both newly created and existing worlds when applicable
- For multiblocks: verify structure formation, recipe execution, and parallel output

Bug fixes should include reproduction steps.

---

## Large Tasks

- Group related fixes together
- Avoid partial solutions
- Investigate the entire affected system
- Complete implementation, validation, and testing before reporting

Intermediate progress reports should be minimized.

---

## Output Rules

Report in this order:

1. Conclusion
2. Root cause
3. Modified files
4. Technical explanation
5. Test results
6. Remaining uncertainty

Uncertainty must be explicitly stated.

---

## Forbidden

- Starting a feature without checking LLM_Context/ROADMAP.md phase priority
- Blindly changing stacktrace lines
- Refactoring without instruction
- Adding libraries unnecessarily
- Ignoring compatibility concerns
- Assuming Forge behavior from modern versions
- Fixing crashes without reading logs
- Optimizing without measurement
- Using guesses when evidence can be obtained
- Duplicating GTCEuM existing ME Hatch/Bus implementations
- Hardcoding parallel counts (always use Config)

---

## Primary Principle

The most expensive operation is another debugging session.

Read LLM_Context/ROADMAP.md first.
Read code second.
Understand third.
Modify fourth.
Test last.

---

## Resources

LLM_ContextにGregTech-Modernのソースコードとかを置いておいたので読んでね。
