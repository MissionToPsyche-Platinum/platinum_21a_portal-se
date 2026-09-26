<script>
export default {
  name: 'GamePreviewCard',
  props: {
    game: {
      type: Object,
      required: true,
      default: () => ({
      })
    }
  },
}
</script>

<template>
  <div class="game-preview">

    <div class="video-container">
      <iframe
        v-if="game.video"
        :src="game.video"
        class="preview-iframe"
        frameborder="0"
        allow="autoplay; encrypted-media"
      ></iframe>
      
      <div v-else class="preview-placeholder">
        <span>No Preview Available</span>
      </div>
    </div>

    <div class="preview-game-details">
      <h4 class="preview-title">{{ game.title }}</h4>
      
      <div v-if="game.genre" class="preview-genre-container">
        <span class="genre-badge">{{ game.genre }}</span>
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
  background-color: #1e1e24;
  color: #ffffff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  border: 1px solid #33333d;
  font-family: Arial, sans-serif;
}

.video-container {
  width: 100%;
  height: 155px;
  background-color: #0d0d11;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.preview-iframe {
  width: 100%;
  height: 190px;
  border: none;
  pointer-events: none;
}

.preview-placeholder {
  color: #888899;
  font-size: 0.85rem;
  font-weight: 500;
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

.preview-game-details {
  padding: 12px 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.preview-title {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 600;
  color: #ffffff;
}

.preview-genre-container {
  display: flex;
  flex-wrap: wrap;
}

.genre-badge {
  background-color: #2b384e;
  color: #64b5f6;
  font-size: 0.75rem;
  padding: 3px 8px;
  border-radius: 4px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.preview-description {
  margin: 0;
  font-size: 0.85rem;
  line-height: 1.35;
  color: #cccccc;
  /* Line clamping to keep the card uniform */
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>