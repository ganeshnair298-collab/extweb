# ExtWeb - Android External Web Remote

ExtWeb is a specialized Android application designed to transform your smartphone into a powerful remote control for web presentations on external displays or projectors. It leverages the Android Presentation API to provide a seamless dual-screen experience.

## 🚀 Features

### 📺 Presentation Mode
* **External Display Projection**: Instantly project a full-featured WebView onto any connected external screen or projector.
* **Fullscreen Support**: Native handling for website fullscreen requests (e.g., YouTube, Vimeo) with a dedicated overlay container.
* **Loading Indicator**: Wavy Material 3 progress indicator for web loading states.

### 🖱️ Hardware-Grade Remote Control
* **Real-time Trackpad**: A responsive trackpad interface on your phone to control a cursor on the external display.
* **Hover Support**: Dispatches native `SOURCE_MOUSE` events, allowing websites to trigger hover effects (highlights, dropdowns) just like a real PC mouse.
* **Precision Clicking**: Tap the trackpad to simulate hardware mouse clicks on web elements.

### ⌨️ Virtual Keyboard Bridge
* **Remote Text Injection**: Tap the keyboard icon to use your phone's soft keyboard to type directly into text fields on the external display.
* **Special Key Support**: Native mapping for Backspace and Enter keys to ensure full compatibility with web forms.

### 🎮 Navigation & Utility
* **Directional Paging**: Dedicated buttons for Page UP, DOWN, LEFT, and RIGHT for rapid document navigation.
* **Smart Zoom**: One-touch Zoom + and Zoom - to adjust content visibility for the audience.
* **Browser Essentials**: Integrated Back and Refresh controls.
* **Smart Back Button**: The back button intelligently exits fullscreen mode before navigating through browser history.

### 🎨 Design & Experience
* **Material 3 UI**: Modern, expressive design using the latest Material Components.
* **Custom Typography**: System-wide integration of custom fonts for a consistent look.
* **Adaptive Themes**: Full support for Light and Dark modes.

## 🛠️ Installation & Setup

### Prerequisites
* Android Studio Iguana or newer.
* An Android device running Android 8.0 (API 26) or higher.
* Hardware for external display (USB-C to HDMI adapter, Chromecast, or Miracast-enabled display).

### Getting Started
1. **Clone the Project**:
   ```bash
   git clone https://github.com/yourusername/ExtWeb.git
   ```
2. **Open in Android Studio**:
   Open Android Studio and select `File > Open`, then navigate to the cloned directory.
3. **Gradle Sync**:
   Allow the project to sync and download dependencies (FlexboxLayout, Material Components, etc.).
4. **Build & Run**:
   Connect your Android device and click the **Run** button.

### Usage
1. Connect your phone to an external display.
2. Open ExtWeb and tap **Start Presentation** (the app will detect the external screen automatically).
3. Tap **Remote Control** to open the control interface.
4. Use the trackpad, buttons, and keyboard icon to interact with the web content on the big screen!

## 🧪 Technologies Used
* **Languages**: Java, XML
* **Core APIs**: Android Presentation API, WebView, DisplayManager.
* **Libraries**: Google Material Components (M3), FlexboxLayout, AndroidX Activity/Appcompat.

---
Developed by Ganesh
