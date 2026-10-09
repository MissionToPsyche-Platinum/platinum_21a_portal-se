<script>
export default {
    name: 'VrInstructions',
    data() {
        return {
            activeTab: 'quest',
            platforms: [
                { id: 'quest', name: 'Meta Quest (2 / 3 / Pro)', title: 'Meta Quest Sideloading Instructions' },
                { id: 'pico', name: 'Pico (Pico 4 / Neo 3)', title: 'Pico Headset Sideloading Instructions' },
                { id: 'vive', name: 'HTC Vive (Focus 3 / XR Elite)', title: 'HTC Vive (Focus 3 / XR Elite) Instructions' }
            ]
        };
    }
};
</script>

<template>
    <div class="vr-guide-container">
        <header class="guide-header">
            <router-link to="/" class="back-link">← Back to Games</router-link>
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

        <!-- Headset Instruction Section -->
        <section
            v-for="platform in platforms"
            :key="platform.id"
            v-show="activeTab === platform.id"
            class="instructions-card"
        >
            <h2>{{ platform.title }}</h2>
        </section>
    </div>
</template>

<style scoped>
.vr-guide-container {
    max-width: 900px;
    margin: 0 auto;
    padding: 48px 24px 80px;
}

.back-link {
    display: inline-block;
    margin-bottom: 20px;
    color: #00bcd4;
    text-decoration: none;
    font-weight: 600;
    font-size: 0.95rem;
}

.back-link:hover {
    text-decoration: underline;
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
    margin: 0 0 8px;
}
</style>