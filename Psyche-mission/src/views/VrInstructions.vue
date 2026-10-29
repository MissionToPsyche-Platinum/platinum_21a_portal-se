<script>
export default {
    name: 'VrInstructions',
    data() {
        return {
            activeTab: 'quest',
            platforms: [
                {
                    id: 'quest',
                    name: 'Meta Quest (2 / 3 / Pro)',
                    title: 'Meta Quest Sideloading Instructions',
                    steps: [
                        {
                            title: 'Enable Developer Mode',
                            description: 'Open the Meta Quest mobile app on your smartphone. Navigate to Menu > Devices, pick your paired headset, scroll to Headset Settings > Developer Mode, and toggle it ON.'
                        },
                        {
                            title: 'Install SideQuest or Platform Tools',
                            description: 'Download the SideQuest Advanced Installer onto your PC from sidequestvr.com, or ensure Android Debug Bridge (adb) is configured in your terminal.'
                        },
                        {
                            title: 'Connect & Authorize USB Debugging',
                            description: 'Attach your Quest to your computer with a USB-C data cable. Put on the headset and click "Allow USB Debugging" on the prompt (check "Always allow from this computer").'
                        },
                        {
                            title: 'Install the APK Package',
                            description: 'In SideQuest, select the top-bar icon titled "Install APK file from folder on computer" and choose your downloaded game APK. Alternatively, execute: adb install -r <game>.apk'
                        },
                        {
                            title: 'Launch from Unknown Sources',
                            description: 'Inside the headset, open your App Library, click the category filter dropdown in the top right corner, select "Unknown Sources", and launch your game.'
                        }
                    ]
                },
                {
                    id: 'pico',
                    name: 'Pico (Pico 4 / Neo 3)',
                    title: 'Pico Headset Sideloading Instructions',
                    steps: [
                        {
                            title: 'Unlock Developer Options',
                            description: 'Wear your Pico headset and open Settings > General > About. Scroll down to Software Version and click it 7 times in a row until Developer Options unlock.'
                        },
                        {
                            title: 'Enable USB Debugging',
                            description: 'Head to Settings > System > Developer Options and toggle USB Debugging ON.'
                        },
                        {
                            title: 'Method A: Direct File Transfer (No PC Tools Required)',
                            description: 'Connect the Pico to your PC with USB-C and select "File Transfer Mode" in the headset. Drag the downloaded APK file into the headset\'s internal Download directory. Inside the headset, open File Manager and click the APK to install.'
                        },
                        {
                            title: 'Method B: Command Line ADB',
                            description: 'With the headset plugged in and debugging allowed, open your terminal and run: adb install -r <game>.apk'
                        },
                        {
                            title: 'Launch from Unknown Sources',
                            description: 'Open your Pico App Library, switch the filter tab to "Unknown Sources", and start the game.'
                        }
                    ]
                },
                {
                    id: 'vive',
                    name: 'HTC Vive (Focus 3 / XR Elite)',
                    title: 'HTC Vive (Focus 3 / XR Elite) Instructions',
                    steps: [
                        {
                            title: 'Enable Developer Mode',
                            description: 'Wear your Vive headset and open Settings > General > About. Scroll down to Build number and click it 7 times until Developer Mode is activated.'
                        },
                        {
                            title: 'Enable USB Debugging',
                            description: 'Navigate to Settings > Advanced > Developer options and toggle USB debugging ON.'
                        },
                        {
                            title: 'Connect and Verify Drivers',
                            description: 'Plug your Vive headset into your PC using a USB-C cable. Ensure you have HTC Vive USB drivers or Android Platform Tools installed. Accept the USB debugging prompt inside the lenses.'
                        },
                        {
                            title: 'Install Package & Companion OBB Assets',
                            description: 'Run "adb install -r <game>.apk" in terminal. If the title contains supplemental OBB asset archives, push them to: /sdcard/Android/obb/<package_name>/'
                        },
                        {
                            title: 'Launch the Title',
                            description: 'Navigate to Library > Custom / Sideloaded Apps (or Unknown Sources depending on firmware release) and select the game.'
                        }
                    ]
                }
            ]
        };
    },
};
</script>

<template>
    <div class="vr-guide-container">
        <header class="guide-header">
            <h1>VR Sideloading & Installation Guide</h1>
            <p class="subtitle">
                Follow the step-by-step instructions below to install and run VR games on your headset
            </p>
        </header>

        <!-- Headset Tabs -->
        <nav class="platform-tabs">
            <button
                v-for="platform in platforms"
                :key="platform.id"
                :class="['tab-btn', { active: activeTab === platform.id }]"
                @click="activeTab = platform.id"
            >
                {{ platform.name }}
            </button>
        </nav>

        <!-- Heaset Instruction Section -->
        <section
            v-for="platform in platforms"
            :key="platform.id"
            v-show="activeTab === platform.id"
            class="instructions-card"
        >
            <h2>{{ platform.title }}</h2>
            <ol class="step-list">
                <li
                    v-for="(step, index) in platform.steps"
                    :key="index"
                    class="step-item"
                >
                    <div class="step-num">{{ index + 1 }}</div>
                    <div class="step-content">
                        <h3>{{ step.title }}</h3>
                        <p>{{ step.description }}</p>
                    </div>
                </li>
            </ol>
        </section>
    </div>
</template>

<style scoped>
.vr-guide-container {
    max-width: 900px;
    margin: 0 auto;
    padding: 48px 24px 80px;
}

.guide-header h1 {
    font-size: clamp(1.8rem, 4vw, 2.6rem);
    margin: 0 0 10px;
    line-height: 1.2;
}

.subtitle {
    font-size: 1.05rem;
    opacity: 0.85;
    margin: 0 0 24px;
}

.platform-tabs {
    display: flex;
    gap: 12px;
    border-bottom: 1px solid currentColor;
    padding-bottom: 8px;
    margin-bottom: 32px;
    overflow-x: auto;
}

.tab-btn {
    padding: 10px 18px;
    background: transparent;
    border: none;
    border-bottom: 3px solid transparent;
    color: inherit;
    font-size: 1rem;
    font-weight: 600;
    cursor: pointer;
    opacity: 0.65;
    transition: all 0.2s ease;
    white-space: nowrap;
}

.tab-btn:hover {
    opacity: 1;
}

.tab-btn.active {
    opacity: 1;
    border-bottom-color: #00bcd4;
    color: #00bcd4;
}

.instructions-card h2 {
    font-size: 1.5rem;
    margin: 0 0 24px;
}

.step-list {
    list-style: none;
    padding: 0;
    margin: 0;
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.step-item {
    display: flex;
    align-items: flex-start;
    gap: 16px;
    background: rgba(255, 255, 255, 0.03);
    border: 1px solid rgba(128, 128, 128, 0.25);
    border-radius: 8px;
    padding: 16px 20px;
}

.step-num {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 32px;
    height: 32px;
    background: #00bcd4;
    color: #ffffff;
    font-weight: 700;
    font-size: 0.95rem;
    border-radius: 50%;
    margin-top: 2px;
}

.step-content h3 {
    margin: 0 0 6px;
    font-size: 1.1rem;
}

.step-content p {
    margin: 0;
    line-height: 1.6;
    opacity: 0.88;
}
</style>