<script>
import GamePage from './GamePage.vue';

export default {
  components: {
    GamePage
  },
  data() {
    return {
      viewGamePage: false
    }
  },
  methods: {
    toggleGamePage() {
      this.viewGamePage = !this.viewGamePage
    }
  }
}

</script>

<template>
  <h1>Welcome to the Psyche mission's web-based game portal!</h1>
  <button @click="toggleGamePage">view game page</button>

  <div v-if="viewGamePage">
    <GamePage />
  </div>

</template>

<style scoped></style>
