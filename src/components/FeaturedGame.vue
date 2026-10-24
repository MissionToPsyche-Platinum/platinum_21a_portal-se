<script>
import psycheLogo from '@/assets/PsycheLogo.png'

export default {
    name: 'FeaturedGame',
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
    <section class="hero" v-if="game">
        <div class="hero-toolbar">
            <slot name="toolbar" />
        </div>

        <div class="hero-body">
            <div class="hero-header">
                <p class="hero-kicker">Featured Game of the Day</p>
                <h2 class="hero-title">{{ game.title }}</h2>
            </div>

            <router-link
                class="hero-media"
                :to="{ name: 'GamePage', params: { id: game.id } }"
            >
                <img :src="gameImage" :alt="game.title" @error="handleImgError" />
            </router-link>

            <div class="details">
                <div class="description">
                    <h3>Game Description</h3>
                    <p>{{ game.description || "No description available for this experience." }}</p>
                </div>

                <div class="metadata">
                    <h3>Details</h3>
                    <p><strong>Difficulty</strong>: {{ game.difficulty || "Unknown" }}</p>
                    <p><strong>Genre</strong>: {{ game.genre || "N/A" }}</p>
                    <p><strong>Age</strong>: {{ game.age || "All Ages" }}</p>
                    <p><strong>Class</strong>: {{ game.class || "N/A" }}</p>
                    <p v-if="game.credits"><strong>Credits</strong>: {{ game.credits }}</p>
                </div>
            </div>

            <div class="hero-actions">
                <router-link
                    class="hero-play"
                    :to="{ name: 'GamePage', params: { id: game.id } }"
                >
                    Play
                </router-link>
                <a class="hero-play" href="#games">Browse games</a>
                <slot />
            </div>
        </div>
    </section>
</template>

<style scoped>
.hero {
    position: relative;
    z-index: 10;
    display: flex;
    flex-direction: column;
    padding: 64px 48px;
    box-sizing: border-box;
}

.hero-toolbar {
    position: absolute;
    top: 20px;
    right: 48px;
}

.hero-body {
    display: flex;
    flex-direction: column;
    gap: 20px;
    max-width: 1200px;
    width: 100%;
    margin: 0 auto;
}

.hero-kicker {
    margin: 0 0 8px;
    font-size: 0.9rem;
    font-weight: 800;
    text-transform: uppercase;
    letter-spacing: 0.18em;
    color: #00bcd4;
    text-shadow: 0 0 12px rgba(0, 188, 212, 0.45);
}

.hero-title {
    margin: 0;
    font-size: clamp(2.2rem, 5vw, 3.8rem);
    line-height: 1.1;
    font-weight: bold;
    width: 100%;
}

.hero-media {
    display: block;
    width: 100%;
}

.hero-media img {
    display: block;
    width: 100%;
    aspect-ratio: 16 / 9;
    max-height: 520px;
    object-fit: cover;
    border: 1px solid currentColor;
}

.details {
    display: flex;
    gap: 40px;
    margin-top: 8px;
    padding: 0;
}

.description,
.metadata {
    flex: 1;
}

.description h3,
.metadata h3 {
    margin-top: 0;
    margin-bottom: 12px;
    font-size: 1.25rem;
}

.description p,
.metadata p {
    margin: 0 0 8px;
    opacity: 0.85;
    line-height: 1.6;
}

.hero-actions {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px;
    margin-top: 8px;
}

.hero-play {
    display: inline-block;
    padding: 12px 20px;
    border: 1px solid currentColor;
    color: inherit;
    text-decoration: none;
    font-weight: 600;
    background: transparent;
    cursor: pointer;
    transition: background 0.2s ease;
}

.hero-play:hover {
    background: rgba(128, 128, 128, 0.2);
}

@media (max-width: 800px) {
    .hero {
        padding: 40px 20px;
    }

    .hero-toolbar {
        position: static;
        margin-bottom: 20px;
    }

    .details {
        flex-direction: column;
        gap: 20px;
    }

    .hero-actions {
        flex-direction: column;
        align-items: stretch;
    }

    .hero-play {
        text-align: center;
    }
}
</style>