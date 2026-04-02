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

};
</script>

<template>
  <div class="rating-box" :style="{borderColor: backGround}">
    <h3 class="rating-title">Rate this game</h3>

    <!--    loop and display 5 stars (button)-->
    <div class="stars">
      <button
          v-for="star in 5"
          :key="star"
          type="button"
          class="star-button"
          :style="{ color: foreGround }"
          :aria-label="`Rate ${star} star${star > 1 ? 's' : ''}`"
      >
        &#9733
      </button>
    </div>

    <p class="rating-text" :style="{ color: foreGround }">

      No rating yet
    </p>

  </div>
</template>

<style>
/*styling for the main rating box and stars*/
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

.star-button {
  opacity: 1;
  transform: scale(1.1);
}

.rating-text {
  margin: 8px 0;
  font-weight: 600;
}

</style>