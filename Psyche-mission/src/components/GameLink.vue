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

    <div class="preview">

<!--      show image if thumbnail exists and not "None"-->
      <img
          v-if="game.thumbnail && game.thumbnail !== 'None'"
          :src="game.thumbnail"
          class="media"
          :alt="game.title"
      />

    </div>

    <h2>{{ game.title }}</h2>
    <router-link :to="{ name: 'GamePage', params: {id: game.id }}">
      <h3 :class="{'dark-link': isDark}" class="game-link">Play</h3>
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



/*.game-link-card {
    border: 2px solid black;
    background-color:honeydew;
    border-radius: 20px;
    padding: 10px;
    font-family:'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}*/
/*
    .dark-mode {
        border: 2px solid white;
        background-color: #190d1b;
    }

    .dark-link {
        color: rgb(5, 233, 17);
    }*/
</style>