<script>
import psycheLogo from '@/assets/PsycheLogo.png'

export default {
  props: {
    game: { type: Object },
    isDark: { type: Boolean},
    textColor: { type: String }, // task 93, text color passed from HomePage
  },
  data() {
    return {

      isFavorite: false, // true if the game is saved in favorite
    };
  },

  mounted() {
    this.loadFavoriteState();
    window.addEventListener("favorites-updated", this.loadFavoriteState);
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
    },

    gameImage() {
      if (this.game.thumbnail && this.game.thumbnail !== "None") {
        return this.game.thumbnail;
      } else {
        return psycheLogo;
      }
    },
  },

  methods: {
    handleImgError(e) {
      e.target.src = psycheLogo;
    },

    // read saved favorite list from localStorage
    getFavoriteGames() {
      //if favorite list exists, convert it to an array, otherwise return null
      const savedFavorites = localStorage.getItem("favoriteGames");
      return savedFavorites ? JSON.parse(savedFavorites) : [];
    },
    // load and check if the current game is saved
    loadFavoriteState() {
      const favorites = this.getFavoriteGames();
      this.isFavorite = favorites.includes(this.game.id);
    },
// switches between favorite/not favorite, if the current game is saved remove it
// if not, save it
    toggleFavorite() {
      let favorites = this.getFavoriteGames();

      if (favorites.includes(this.game.id)) {

        favorites = favorites.filter(id => id !== this.game.id); // remove from favorites
        this.isFavorite = false;
      } else {
        favorites.push(this.game.id);  // add to favorites

        this.isFavorite = true;
      }

      localStorage.setItem("favoriteGames", JSON.stringify(favorites));

      // notify other components that favorites changed
      window.dispatchEvent(new Event("favorites-updated"));
    },



},



};
</script>

<template>
  <router-link :to="{ name: 'GamePage', params: {id: game.id }}">
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

      <!--      play is removed since images is now clickable-->
      <!--      <h3 :class="{'dark-link': isDark}" class="game-link">Play</h3>-->
      <div class="preview">

        <!--show image if thumbnail exists and not "None"-->
        <img
            :src="gameImage"
            @error="handleImgError"
            class="media"
            :alt="game.title"
        />

      </div>
      <!--      favorite button that change color dynamically and prevent navigation when clicked.-->
      <div class="favorite-container">
        <button
            class="favorite-btn"
            :style="{ color: textColor }"
            :class="{ saved: isFavorite }"
            @click.prevent.stop="toggleFavorite"
            :aria-label="isFavorite ? 'Remove from favorites' : 'Save to favorites'"
            :title="isFavorite ? 'Unsave game' : 'Save game'"
        >
          {{ isFavorite ? "\u2665" : "\u2661 " }} <!--heart icon changes based on the state-->


        </button>
      </div>

    </div>
  </router-link>
</template>

<style scoped>
a {
  text-decoration: none;
  color: inherit;
}

.game-link-card {
  position: relative;
}

/* favorite button styling */
.favorite-container{

  display: flex;
  justify-content: flex-end;
  margin-top: 6px;

}

.favorite-btn {

  border: none;
  background: transparent;
  font-size: 24px;
  cursor: pointer;
  transition: transform 0.2s ease, opacity 0.2s ease;
  opacity: 0; /*initially hidden*/
  pointer-events: none;
}
/* show the button when hovering over the card */
.game-link-card:hover .favorite-btn {
  opacity: 1;
  pointer-events: auto;
}
.favorite-btn:hover {
  transform: scale(1.1);
  opacity: 0.85;
}
/* always show when saved */
.favorite-btn.saved {
  opacity: 1;
  pointer-events: auto;
}


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

