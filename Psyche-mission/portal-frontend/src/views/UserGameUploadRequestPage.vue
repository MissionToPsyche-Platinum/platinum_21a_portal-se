<script>
import GameUploadForm from "@/components/GameUploadForm.vue";
import UserPortalLayout from "@/components/UserPortalLayout.vue";

export default {
    components: {
        GameUploadForm,
        UserPortalLayout
    }
}
</script>

<template>
    <UserPortalLayout>
    <div class="request-page">
        <h1>Submit a Game Upload Request</h1>
        <GameUploadForm />
    </div>
    </UserPortalLayout>
</template>

<style scoped>
.request-page {
    width: 80%;
    margin: 30px auto;
    padding: 30px;
    box-sizing: border-box;
}

@media (max-width: 800px) {
    .request-page {
        width: 100%;
        margin: 16px auto;
        padding: 20px;
    }
}

@media (max-width: 500px) {
    .request-page {
        margin: 8px auto;
        padding: 12px;
    }
    .request-page h1 {
        font-size: 22px;
        text-align: center;
        overflow-wrap: anywhere;
    }
}
</style>