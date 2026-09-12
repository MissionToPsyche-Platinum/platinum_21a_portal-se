<script>
import psycheLogo from '@/assets/PsycheLogo.png'

export default {
    props: ['game', 'isDark', 'textColor'],
    computed: {
        gameImage() {
            if (this.game && this.game.thumbnail && this.game.thumbnail !== "None") {
                return this.game.thumbnail
            }
            return psycheLogo
        }
    },
    methods: {
        handleImgError(e) {
            e.target.src = psycheLogo
        }
    }
}
</script>

<template>
    <section class="hero">
        <div class="hero-toolbar">
            <slot name="toolbar" />
        </div>

        <div class="hero-body">
            <div class="hero-copy">
                <p class="hero-kicker">Psyche Mission Game Portal</p>
                <h1 class="hero-title">{{ game.title }}</h1>
                <p class="hero-meta">Featured game of the day · {{ game.difficulty || "Unknown" }}</p>
                <p class="hero-blurb">Take a quiz to find a game, or play today's pick.</p>
                <div class="hero-actions">
                    <slot />
                    <router-link
                        class="hero-play"
                        :to="{ name: 'GamePage', params: { id: game.id } }"
                    >
                        Play
                    </router-link>
                    <a class="hero-play" href="#games">Browse games</a>
                </div>
            </div>

            <router-link
                class="hero-media"
                :to="{ name: 'GamePage', params: { id: game.id } }"
            >
                <img :src="gameImage" :alt="game.title" @error="handleImgError" />
            </router-link>
        </div>
    </section>
</template>

<style scoped>
.hero {
    position: relative;
    z-index: 10;
    min-height: calc(100vh - 60px);
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 72px 48px 48px;
    box-sizing: border-box;
}

.hero-toolbar {
    position: absolute;
    top: 20px;
    right: 48px;
}

.hero-body {
    display: grid;
    grid-template-columns: minmax(0, 1fr) minmax(220px, 38%);
    gap: 40px;
    align-items: center;
    max-width: 1080px;
}

.hero-kicker {
    margin: 0 0 10px;
    font-size: 0.95rem;
    opacity: 0.7;
}

.hero-title {
    margin: 0 0 16px;
    font-size: clamp(2.1rem, 5vw, 3.75rem);
    line-height: 1.1;
    font-weight: bold;
}

.hero-meta,
.hero-blurb {
    margin: 0 0 10px;
    max-width: 36rem;
    line-height: 1.5;
    opacity: 0.85;
}

.hero-actions {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
    margin-top: 18px;
}

.hero-play {
    display: inline-block;
    padding: 12px 20px;
    border: 1px solid currentColor;
    color: inherit;
    text-decoration: none;
    font-weight: 600;
}

.hero-play:hover {
    background: rgba(128, 128, 128, 0.2);
}

.hero-media {
    display: block;
}

.hero-media img {
    display: block;
    width: 100%;
    height: 280px;
    object-fit: cover;
    border: 1px solid currentColor;
}

@media (max-width: 800px) {
    .hero {
        padding: 88px 20px 40px;
        min-height: auto;
    }

    .hero-toolbar {
        right: 20px;
        top: 16px;
    }

    .hero-body {
        grid-template-columns: 1fr;
    }

    .hero-media img {
        height: 200px;
    }
}
</style>
