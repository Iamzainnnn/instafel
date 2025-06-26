## 🧪 Developer Options: Unlocking Internal Instagram Experiments

Instagram Alpha includes a hidden menu named Developer Options for Meta's internal developers to test. That hidden menu is typically blocked from public either via gesture-based entry or access tokens tied to Meta employee accounts. The tools inside of Developer Options are being used by Developers to test experimental features, enable unreleased layouts, and debug server behavior.

Developer options include:

- A list of **active and inactive feature flags**
- Toggle switches for **experiments and test variants**
- Views of **client/server sync logs**
- Internal usage telemetry (crash stats, UI latency, etc.)

These options are critical to understanding how Meta builds, evaluates, and releases features.

---

### 🛠️ How Instafel Unlocks These Menus

Instafel uses a custom patching process to forcibly unlock developer menus. Rather than injecting external code, it modifies the Alpha APK’s layout and view tree to:

- Unhide hidden fragments and activities
- Remove internal-only access checks

This means the user sees _exactly_ what a Meta engineer sees that Alpha releases — including toggles for features that haven’t launched yet.

Examples of these unreleased features:

- UI redesigns
- New explore page layouts
- Reels-specific playback controls
- New features related to social interactions
- etc.

---

## 🛠️ Accessing Developer Options

### How to Open Developer Options in Instafel

1. **Open Instafel App:** Launch Instafel on your device.
2. **Open the Hidden Menu:** Press and hold on "Home" button to open the pop-up navigation menu.
3. **Enter the Developer Options:** Click on the option "Go to Developer Options Page"
4. **Navigate to Internal Settings:** Look for “MetaConfig Settings & Overrides under “Experimentation.”
5. **Explore Flags and Toggles:** Inside, you’ll find feature flags, toggle switches, and experimental options that you can enable or disable.

---
