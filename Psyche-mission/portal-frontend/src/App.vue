<script>
import NavBar from "./components/NavBar.vue";
import Footer from "./components/Footer.vue";

export default {
  data() {
    return {};
  },
  components: {
    NavBar,
    Footer
  }
};
</script>

<template>
  <div id="app">
    <NavBar />
    <router-view :key="$route.fullPath"/>
    <Footer />
  </div>
</template>

<style>
</style>