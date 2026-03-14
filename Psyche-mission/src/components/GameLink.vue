<script>
export default {
    props: {
        game: { type: Object }
    },
    data() {
        return {};
    }
};
</script>

<template>
    <div class="game-link-card">
        <h2>{{ game.title }}</h2>
        <router-link :to="{ name: 'GamePage', params: {id: game.id }}">Play</router-link>
    </div>
</template>

<style>

</style>