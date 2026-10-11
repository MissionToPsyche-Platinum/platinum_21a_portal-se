<script>
import GameUploadForm from "@/components/GameUploadForm.vue";

export default {
    components: {
        GameUploadForm
    }
}
</script>

<template>
    <div class="request-page">
        <h1>Submit a Game Upload Request</h1>
        <GameUploadForm />
    </div>
</template>

<style scoped>
.request-page {
    width: 80%;
    margin: 30px auto;
    padding: 30px;
}
</style>