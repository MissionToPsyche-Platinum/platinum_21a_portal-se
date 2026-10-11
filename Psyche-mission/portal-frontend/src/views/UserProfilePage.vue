<script>
import GameUploadForm from "@/components/GameUploadForm.vue";
import { getSession } from "@/utils/auth";

export default {
    components: {
        GameUploadForm
    },
    data() {
        const session = getSession();

        return {
            username: session ? session.username : "Test Username",
            showUploadForm: false
        }
    }
}
</script>

<template>
<div class="profile-container">
    <div class="profile-header">
        <h1>Welcome, {{ username }}</h1>
    </div>
    <div class="profile-actions">
        <section class="profile-card">
            <h2>Submit a Game Request</h2>
            <p>Submit a game for review and possible addition to the Psyche portal.</p>
            <router-link to="/profile/upload-request">Submit New Game Request</router-link>
        </section>
        <section class="profile-card">
            <h2>My Upload Requests</h2>
            <p>View the status of your previously submitted upload requests.</p>
            <button type="button" disabled>View My Requests</button>
            <p class="coming-soon">Request history is not available yet.</p>
        </section>
    </div>
</div>
</template>

<style>
.profile-container {
    border: 2px solid black;
    padding: 30px;
    margin: 30px auto;
    width: 80%;
}
.profile-header {
    text-align: center;
    padding: 30px;
    border-bottom: 2px solid black;
}
.profile-actions {
    text-align: center;
    padding: 30px;
    border-bottom: 1px solid black;
}
.profile-card {
    border: 1px solid black;
    border-radius: 8px;
    padding: 24px;
    text-align: center;
}
</style>