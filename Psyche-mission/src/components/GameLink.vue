<script>
export default {
  props: {
    game: { type: Object },
    isDark: { type: Boolean},
  },
  data() {
    return {
      dark: this.isDark,
    };
  },

};
</script>

<template>
  <div :class="{'dark-mode': isDark}" class="game-link-card">



    <h2>{{ game.title }}</h2>

    <!-- make the preview clickable-->
    <router-link :to="{ name: 'GamePage', params: {id: game.id }}">

      <!--      play is removed since images is now clickable-->
      <!--      <h3 :class="{'dark-link': isDark}" class="game-link">Play</h3>-->
      <div class="preview">

        <!--show image if thumbnail exists and not "None"-->
        <img
            v-if="game.thumbnail && game.thumbnail !== 'None'"
            :src="game.thumbnail"
            class="media"
            :alt="game.title"
        />

      </div>
    </router-link>
  </div>
</template>

<style scoped>

.preview {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.media {
  width: 100%;
  max-width: 250px;
  height: 150px;
  object-fit: cover;
  border-radius: 10px;
}


</style>