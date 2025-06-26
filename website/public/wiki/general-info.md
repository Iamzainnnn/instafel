## 📌 Before You Start

Before diving into the codebase, there’s one important thing to understand:  
**Instafel does not share the source code for private APIs or internal services** that users aren’t supposed to access (like private Instagram APIs). These make up about **20% of the whole project**, so if you come across references to unknown APIs, microservices, or endpoints, don’t be surprised — they’re intentionally left out.

---

## 🚀 Overview

You can find the full Instafel source code here: [**mamiiblt/instafel**](https://github.com/mamiiblt/instafel)  
The project uses **Gradle** to manage its dependencies and modular structure. In total, Instafel consists of **five separate subprojects**, each serving a specific role.

---

### 📱 App

This is where most of the user-facing components live — like the **Instafel menu**, UI dialogs, and custom screens.  
Keep in mind: any code you write here should be **compatible with Instagram's internal libraries** since it integrates directly with their UI system.

---

### 🔄 Updater

A completely **standalone app** that automates updates.  
Its aim is to make sure users don’t need to manually download and install APKs. It uses **Shizuku** or **root access** to apply updates directly from inside the device.

---

### 🧩 Patcher

This one’s mainly for developers — **not end-users**.  
It’s used to generate Instafel APKs by injecting the App module’s compiled code (like `.dex` and `.arsc` files) into the official Instagram Alpha APK. It also takes care of hooking and wiring everything together.

---

### 🧪 GPlayAPI

A helper module that fetches the latest **Instagram Alpha builds** directly from Google Play.  
Once it detects a new build, it automatically triggers the patching process — this makes the whole release pipeline as hands-free as possible.

---

### 🌐 Website

This contains the code for the **Instafel website** you’re visiting right now.  
Built with **Next.js** and styled using **shadcn/ui**.

---

## ⚙️ What Is the Instafel Config File?

The config file can be found here:  
`config/example.ifl_config.json`

It’s used during the build process to define:
- Version numbers for each module
- Tag names
- Gradle dependency behavior
- Which subprojects to include

Gradle reads this file to know **how to compile and link everything properly**.

> 🛠️ If you're just working on UI or testing features, you probably won’t need to touch this file.

---
