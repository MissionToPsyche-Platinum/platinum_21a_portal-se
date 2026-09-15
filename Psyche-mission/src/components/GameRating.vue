<script>
export default {
  name: "GameRating",
  props: {
    gameId: {
      type: String,
      required: true
    },
    isDark: {
      type: Boolean,
      default: false
    },
    foreGround: {
      type: String,

    },
    backGround: {
      type: String,

    }
  },

  data() {
    return {
      currentRating: 0, // user's selected rating
      hoverRating: 0 // temporarily stores current star (hovered)
    };
  },
  computed: {
    storageKey() {
      return `gameRating-${this.gameId}`; // create localStorage key for each game
    }
  },
  mounted() { // load saved rating when component is mounted
    const savedRating = localStorage.getItem(this.storageKey);
    if (savedRating) {
      this.currentRating = Number(savedRating);
    }
  },
  methods: {
    setRating(rating) { // save selected rating locally
      this.currentRating = rating;
      localStorage.setItem(this.storageKey, rating);// update the selected rating
    },
    clearRating() {// clear selected rating and delete from localstorage
      this.currentRating = 0;
      localStorage.removeItem(this.storageKey);
    }
  }


};
</script>

<template>
  <div class="rating-box">
    <h3 class="rating-title">Rate this game</h3>

    <!-- loop and generate 5 stars (button), apply active class if star is selected or hovered
    and dynamically sets the label and add 's' for more than 1 star -->
    <div class="stars">
      <button
          v-for="star in 5"
          :key="star"
          type="button"
          class="star-button"
          :style="{ color: foreGround }"
          :class="{ active: star <= (hoverRating || currentRating) }"
          @click="setRating(star)"
          @mouseover="hoverRating = star"
          @mouseleave="hoverRating = 0"
          :aria-label="`Rate ${star} star${star > 1 ? 's' : ''}`"

      >
        &#9733 <!-- unicode for star character -->
      </button>
    </div>

    <p class="rating-text" :style="{ color: foreGround }">
      <!-- if user selected a rating -->
      <span v-if="currentRating > 0"> Your rating: {{ currentRating }}/5</span>
      <!-- otherwise no rating yet -->
      <span v-else>No rating yet</span>

    </p>
    <button
        v-if="currentRating > 0"
        type="button"
        class="clear-button"
        @click="clearRating"
        :style="{ color: foreGround, borderColor: foreGround }"

    >
      Clear Rating
    </button>

  </div>
</template>

<style scoped>
.rating-box {
  margin-top: 20px;
  padding: 16px;
  border: 1px solid #666666;
  border-radius: 4px;
  text-align: center;
  max-width: 320px;
}

.rating-title {
  margin: 0 0 10px;
  font-size: 1rem;
}

.stars {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 10px;
}

.star-button {
  background: transparent;
  border: none;
  font-size: 32px;
  cursor: pointer;
  opacity: 0.35;
  transition: transform 0.2s ease, opacity 0.2s ease;
}

.star-button.active {
  opacity: 1;
  transform: scale(1.1);
}

.rating-text {
  margin: 8px 0;
  font-weight: 600;
}

.clear-button {
  padding: 10px 16px;
  border: 1px solid currentColor;
  background: transparent;
  cursor: pointer;
  border-radius: 4px;
  font-weight: 600;
}

.clear-button:hover {
  background: #111111;
  color: #ffffff;
  border-color: #111111;
}
</style>