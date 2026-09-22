<script>
import Asteroids from '../components/Asteroids.vue'
export default {
    name: "NotFound",
    components: {
        Asteroids
    },
    data() {
        return {};
    }
};
</script>

<template>
    <div class="not-found">
        <h1>404 - Page Not Found</h1>
    </div>

    <div class="asteroids-div">
        <!-- can change size of game window by passing width and height props -->
        <Asteroids />
    </div>
</template>

<style scoped>
    .not-found {
        font-size: x-large;
        font-weight: 600;
        text-align: center;
    }
</style>