日本語 | [English](README.md)

# ShishamoTech

**GTCEu Modern (GregTech CE Unofficial)** 向けのアドオンMODです。
Steam期からMAXティアまで、全ティアを網羅する超巨大パラレルマルチブロックを追加します。

- Mod ID: `shishamo_tech`
- 対象バージョン: Minecraft 1.20.1 / Forge 47.x
- ライセンス: [MIT License](LICENSE)

## 概要

ShishamoTechは、GTCEuMの通常マルチブロックを内部でラップし、大幅なパラレル倍率（デフォルト64倍、config変更可）をかけた「メガマルチブロック」を提供するアドオンです。Steam時代からElectric各ティア、さらにAE2連携機まで幅広くカバーします。

## 主な機能

### Steam系メガマルチブロック
- Mega Steam Furnace
- Mega Steam Alloy Smelter
- Mega Steam Extractor
- Mega Steam Compressor
- Mega Steam Grinder
- Mega Steam Hammer
- Mega Steam Rock Crusher

### Electric系メガマルチブロック
- Large Arc Furnace
- Large Assembly Plant
- Large Distillation Tower
- Large Electrolyzer
- Large Grinding Plant
- Large Smelting Plant
- Large Washing Plant
- LCR Cluster
- Battle Tower Centrifuge
- Eternal Force Freezer

### AE2連携（AE2導入時のみ有効）
- Press-Free Inscriber（MV / HV / IV / EV）

### Config
`config/shishamo_tech-common.toml` から以下を設定できます。

| 項目 | 説明 | デフォルト |
|---|---|---|
| `parallelMultiplier` | 全マシン共通のパラレル倍率 | 64 |
| `enableSteamMachines` | Steam系メガマルチブロックの有効化 | true |
| `enableElectricMachines` | Electric系メガマルチブロックの有効化 | true |
| `enableAE2Integration` | AE2連携マシンの有効化（AE2導入時のみ） | true |

## 依存関係

| Mod | バージョン | 必須/任意 |
|---|---|---|
| GTCEu Modern | 7.5.3以上 | 必須 |
| Applied Energistics 2 | 15.0.23以上 | 任意（Press-Free Inscriberに必要） |
| JEI | 15.20.0.115以上 | 任意 |

## ビルド方法

### 前提

- JDK 17（Forge 1.20.1 / Gradle 8.8の要件）
  - システムのデフォルトJavaが異なる場合は `JAVA_HOME` をJDK17に向けてください。
    ```bash
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
    ```

### 1. データ生成（datagen）

ブロックステート・モデル・言語ファイルなどの生成リソースを更新します。

```bash
./gradlew runData
```

Windowsの場合:

```bash
gradlew.bat runData
```

### 2. ビルド

```bash
./gradlew build
```

Windowsの場合:

```bash
gradlew.bat build
```

ビルドが完了すると、`build/libs/` 以下にMODのjarファイルが生成されます。

> **Note:** リソースを追加・変更した場合は、`build` の前に `runData` を実行してください。

## ディレクトリ構成（概要）

```
src/main/java/shishamo_tech/
├─ ShishamoTech.java        # @Mod エントリポイント
├─ STAddon.java             # GTCEuMアドオン登録
├─ STRegistration.java      # GTRegistrate 初期化
├─ api/                     # 外部公開用インターフェース
├─ client/                  # クライアント専用処理
├─ common/
│  ├─ machine/
│  │  ├─ steam/             # Steam系メガマルチブロック
│  │  ├─ electric/          # Electric系メガマルチブロック（各ティア）
│  │  └─ ae2/               # AE2連携マルチブロック
│  ├─ recipe/               # レシピタイプ定義
│  ├─ recipegen/            # レシピデータ生成
│  └─ data/                 # マテリアル・アイテム定義
├─ config/                  # Forge Config
└─ integration/
   ├─ ae2/                  # AE2連携
   └─ jei/                  # JEIプラグイン
```

## ライセンス

このプロジェクトは [MIT License](LICENSE) のもとで公開されています。
