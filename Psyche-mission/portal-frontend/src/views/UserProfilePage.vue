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
            email: session ? session.email : "",
            role: session ? session.role : "",
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
    <div class="account-info">
        <h2>Account Information</h2>
        <p><strong>Username:</strong> {{ username }}</p>
        <p><strong>Email:</strong> {{ email || "Not available"}}</p>
        <p><strong>Role:</strong> {{ role || "Not available"}}</p>
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
            <router-link to="/profile/requests">View My Requests</router-link>
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
.account-info {
    margin: 24px auto;
    max-width: 500px;
}
</style>