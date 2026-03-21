<script>
import NavBar from "./components/NavBar.vue";

export default {
  data() {
    return {};
  },
  components: {
    NavBar
  },
};
</script>

<template>
  <div id="app">
    <NavBar />
    <router-view />
  </div>
</template>

<style>

</style>