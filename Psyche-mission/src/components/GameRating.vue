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
  <div class="rating-box" :class="{ dark: isDark, rated: currentRating > 0 }">
    <p class="rating-kicker">Mission feedback</p>
    <h3 class="rating-title">Rate this game</h3>

    <!-- loop and generate 5 stars (button), apply active class if star is selected or hovered
    and dynamically sets the label and add 's' for more than 1 star -->
    <div class="stars">
      <button
          v-for="star in 5"
          :key="star"
          type="button"
          class="star-button"
          :class="{ active: star <= (hoverRating || currentRating) }"
          @click="setRating(star)"
          @mouseover="hoverRating = star"
          @mouseleave="hoverRating = 0"
          :aria-label="`Rate ${star} star${star > 1 ? 's' : ''}`"
      >
        <span class="star-icon">{{ star <= (hoverRating || currentRating) ? "\u2605" : "\u2606" }}</span>
        <span class="star-num">{{ star }}</span>
      </button>
    </div>

    <p class="rating-text">
      <span v-if="currentRating > 0">{{ currentRating }} out of 5</span>
      <span v-else>Tap a star to rate</span>
    </p>
    <button
        v-if="currentRating > 0"
        type="button"
        class="clear-button"
        @click="clearRating"
    >
      Clear rating
    </button>

  </div>
</template>

<style scoped>
.rating-box {
  margin-top: 28px;
  padding: 18px 0 4px;
  border: none;
  border-top: 1px solid #444444;
  text-align: left;
  max-width: none;
}

.rating-kicker {
  margin: 0 0 4px;
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  opacity: 0.55;
}

.rating-title {
  margin: 0 0 14px;
  font-size: 1.25rem;
}

.stars {
  display: flex;
  justify-content: flex-start;
  gap: 4px;
  margin-bottom: 12px;
}

.star-button {
  flex: 1;
  max-width: 56px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 8px 4px 6px;
  background: transparent;
  border: 1px solid #444444;
  color: inherit;
  font-size: 26px;
  line-height: 1;
  cursor: pointer;
  opacity: 1;
  transition: border-color 0.2s ease, background 0.2s ease, color 0.2s ease;
}

.star-icon {
  color: inherit;
  opacity: 0.4;
}

.star-num {
  font-size: 0.65rem;
  font-weight: 600;
  letter-spacing: 0.04em;
  opacity: 0.45;
}

.star-button:hover,
.star-button.active {
  border-color: #e8c547;
  background: rgba(232, 197, 71, 0.12);
}

.star-button.active .star-icon,
.star-button:hover .star-icon {
  color: #e8c547;
  opacity: 1;
}

.star-button.active .star-num,
.star-button:hover .star-num {
  color: #e8c547;
  opacity: 1;
}

.rating-text {
  margin: 0 0 8px;
  font-size: 0.9rem;
  font-weight: 600;
  opacity: 0.8;
}

.clear-button {
  padding: 0;
  border: none;
  background: transparent;
  color: inherit;
  cursor: pointer;
  font-size: 0.8rem;
  font-weight: 600;
  letter-spacing: 0.04em;
  text-transform: uppercase;
  text-decoration: underline;
  text-underline-offset: 3px;
  opacity: 0.7;
}

.clear-button:hover {
  opacity: 1;
  background: transparent;
  color: inherit;
  border-color: transparent;
}
</style>