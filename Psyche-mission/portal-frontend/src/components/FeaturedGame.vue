<script>
import GameLink from './GameLink.vue'

export default {
    props: ['game', 'isDark', 'textColor'],
    components: { GameLink }
}
</script>

<template>
    <div class="featured-game">
        <h2>Featured Game of the Day</h2>
        <GameLink :game="game" :isDark="isDark" :textColor="textColor" />
    </div>
</template>

<style scoped>
.featured-game {
    text-align: center;
    margin: 2rem 0;
}
</style>