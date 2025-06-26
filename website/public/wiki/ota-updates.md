### 🚀 What Are OTA Updates?

OTA (Over-The-Air) updates allow Instafel to update itself directly from within the app. When a new patched build is available, users receive a prompt inside Instafel -and also a notification- and can install the update with a single tap.

This eliminates the need for:

- Visiting download pages
- Manually installing APKs
- Visiting download pages
- Manually replacing APKs
- Losing app data during updates

---

### ⚙️ How the Update Process Works

1. **New Instagram Alpha Build:** Meta releases a new Alpha version.
2. **Patching:** Instafel’s automated patcher modifies this build to unlock developer tools and apply internal tweaks.
3. **Testing:** The patched build is tested by testers for critical issues like crashes or errors
4. **Bot Upload:** Once approved, the Instafel bot uploads the patched version to the update server.
5. **In-App Update Prompt:** Users are notified inside the app and can update immediately.

---

### 🧬 Clone vs. Unclone Updates

Instafel supports two build types:

- **Clone**
- **Unclone**

Updates are served based on which variant you're using:

- Clone users receive clone updates.
- Unclone users receive unclone updates.

There’s no need to manually select a channel — the updater knows your variant and provides the correct version automatically.

---

### 🛑 Limitations

- You **cannot switch between clone/unclone** via OTA. Changing variants requires uninstalling and reinstalling the desired type.
- OTA will only work if you're using an official Instafel build. Self-modified APKs or corrupted installs may not update properly.
- OTA does **not roll back** to previous versions — if a build is unstable, you must manually install an older one.

---
