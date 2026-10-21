<script>
export default {
  name: 'GamePreviewCard',
  props: {
    game: {
      type: Object,
      required: true,
      default: () => ({
      })
    },
    isDark: {
      type: Boolean,
      default: true
    },
    textColor: {
      type: String,
      default: "#ffffff"
    },
    bgColor: {
      type: String,
      default: "#000000"
    }
  },
  computed: {
    difficultyLevel() {
      const difficulty = (this.game.difficulty || "").toLowerCase();

      if (difficulty === "easy") return 1;
      if (difficulty === "medium") return 2;
      if (difficulty === "hard") return 3;

      return 0;
    }
  },
  methods: {
    isYoutube(url) {
      if (url == "") {
        return false;
      }

      return url.includes('youtube.com') || url.includes('youtu.be');
    },
  },
}
</script>

<template>
  <div
    class="game-preview"
    :class="{ 'dark-mode': isDark }"
    :style="{ backgroundColor: bgColor, color: textColor, borderColor: textColor }"
  >
    <div class="video-container">
      <iframe
        v-if="game.video && isYoutube(game.video)"
        :src="game.video"
        class="preview-iframe"
        frameborder="0"
        allow="autoplay; encrypted-media"
      ></iframe>

      <video
        v-else-if="game.video"
        :src="game.video"
        class="preview-video"
        muted
        autoplay
        loop
        playsinline
      ></video>

      <div v-else class="preview-placeholder">
        <span>No preview available</span>
      </div>
    </div>

    <div class="preview-game-details">
      <h4 class="preview-title">{{ game.title }}</h4>
      <p class="preview-meta">
        {{ game.genre || "Game" }}
        <span v-if="game.age"> · {{ game.age }}</span>
      </p>

      <div class="difficulty-section">
        <span class="difficulty-label">
          Difficulty: {{ game.difficulty || "Unknown" }}
        </span>
        <div class="difficulty-bar">
          <span :style="{ backgroundColor: textColor, opacity: difficultyLevel >= 1 ? 1 : 0.2 }"></span>
          <span :style="{ backgroundColor: textColor, opacity: difficultyLevel >= 2 ? 1 : 0.2 }"></span>
          <span :style="{ backgroundColor: textColor, opacity: difficultyLevel >= 3 ? 1 : 0.2 }"></span>
        </div>
      </div>

      <p class="preview-description">
        {{ game.description }}
      </p>
    </div>
  </div>
</template>

<style scoped>
.game-preview {
  width: 340px;
  overflow: hidden;
  border: 1px solid currentColor;
  border-radius: 4px;
  font-family: Arial, Helvetica, sans-serif;
}

.video-container {
  width: 100%;
  height: 170px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border-bottom: 1px solid currentColor;
  background: color-mix(in srgb, currentColor 8%, transparent);
}

.preview-iframe {
  width: 100%;
  height: 190px;
  border: none;
  pointer-events: none;
}

.preview-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  border: none;
}

.preview-placeholder {
  font-size: 0.85rem;
  opacity: 0.7;
}

.preview-game-details {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.preview-title {
  margin: 0;
  font-size: 1.1rem;
  font-weight: bold;
}

.preview-meta {
  margin: 0;
  font-size: 0.9rem;
  line-height: 1.4;
  opacity: 0.85;
}

.difficulty-section {
  text-align: left;
}

.difficulty-label {
  display: block;
  margin-bottom: 6px;
  font-size: 0.85rem;
  font-weight: 600;
}

.difficulty-bar {
  display: flex;
  gap: 6px;
}

.difficulty-bar span {
  width: 32px;
  height: 8px;
  border-radius: 999px;
}

.preview-description {
  margin: 0;
  font-size: 0.85rem;
  line-height: 1.5;
  opacity: 0.85;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>