<script>
export default {
  props: {
    game: { type: Object },
    isDark: { type: Boolean},
    textColor: { type: String }, // task 93, text color passed from HomePage
  },
  data() {
    return {
      dark: this.isDark,
    };
  },
  //==================== task 93====================

  // compute numeric difficulty level, easy=1, medium=2, hard=3
  computed: {
    difficultyLevel() {
      const difficulty = (this.game.difficulty || "").toLowerCase();

      if (difficulty === "easy") return 1;// easy
      if (difficulty === "medium") return 2;// medium
      if (difficulty === "hard") return 3;// hard

      return 0; // if undefined
    }
  },


};
</script>

<template>
  <div :class="{'dark-mode': isDark}" class="game-link-card">



    <h2>{{ game.title }}</h2>

    <!-- ==================== task 92====================-->

    <!--Difficulty bar UI on game card-->
    <div class="difficulty-section">
  <span class="difficulty-label"> <!-- difficulty text label -->
    Difficulty: {{ game.difficulty || "Unknown" }}
  </span>

      <div class="difficulty-bar"><!-- visual difficulty bar -->

        <!-- easy -->
        <span
            :style="{
        backgroundColor: textColor,
        opacity: difficultyLevel >= 1 ? 1 : 0.2
      }"
        ></span>

        <!-- medium -->
        <span
            :style="{
        backgroundColor: textColor,
        opacity: difficultyLevel >= 2 ? 1 : 0.2
      }"
        ></span>

        <!-- hard -->
        <span
            :style="{
        backgroundColor: textColor,
        opacity: difficultyLevel >= 3 ? 1 : 0.2
      }"
        ></span>
      </div>
    </div>


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

/*Difficulty bar styling*/

.difficulty-section {
  margin-bottom: 12px;
  text-align: center;
}

.difficulty-label {
  display: block;
  margin-bottom: 6px;
  font-size: 14px;
  font-weight: 600;
}

.difficulty-bar {
  display: flex;
  justify-content: center;
  gap: 6px;
  margin-bottom: 6px;
}

.difficulty-bar span {
  width: 32px;
  height: 8px;
  border-radius: 999px;
  transition: all 0.3s ease;
}


</style>