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
  <div class="rating-box" :style="{borderColor: backGround}">
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

<style>
.rating-box {
  margin-top: 20px;
  padding: 16px;
  border: 2px solid #330066;
  border-radius: 12px;
  text-align: center;
  max-width: 320px;
}


.rating-title {
  margin-bottom: 10px;
}
/*star container*/
.stars {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 10px;
}
/* star styling*/
.star-button {
  background: transparent;
  border: none;
  font-size: 32px;
  cursor: pointer;
  opacity: 0.35;
  transition: transform 0.2s ease, opacity 0.2s ease;

}

/*star appearance*/
.star-button.active {
  opacity: 1;
  transform: scale(1.1);

}

.rating-text {
  margin: 8px 0;
  font-weight: 600;
}

.clear-button {
  padding: 10px 15px;
  border: 1px solid currentColor;
  background: transparent;
  cursor: pointer;
  border-radius: 999px;
  font-weight: 600;
  transition: transform 0.2s ease, opacity 0.2s ease, box-shadow 0.2s ease;


}
.clear-button:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.12);
}


</style>