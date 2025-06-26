### 📦 What Does “Clone” Mean?

Cloning refers to modifying the app’s package name (and sometimes its digital signature) so that it can work with other Instagram-based apps on the same device. Clone version behaves like a completely separate application:

- It has its own app data and cache
- You can run multiple setups with different feature flags in parallel

This is especially valuable for power users and testers running experiments or comparing behaviors between builds.

---

### 🛠️ How Cloning Works in Instafel

When downloading or installing Instafel, you may see two different variants:

- **Unclone Version** (com.instagram.android)

  - This version **replaces** the stock Instagram app
  - Cannot be installed alongside the official app
  - Useful for those want the original Instagram environment (original logo and app name)

- **Clone Version** (com.instafel.android)
  - Has a different package name (and also a different logo and app name)
  - Can be installed side by side with the original app
  - Safe for testing and experimental use

You can install both types at once. For example:

- Use **stock Instagram** for daily use
- Use **Instafel Clone** for exploring developer flags and unreleased features

Or, you might want the unclone variant if you want Instafel to replace your official Instagram app.

**Note:** You cannot install the unclone build if the stock Instagram app is already installed. You must uninstall it first.

---
