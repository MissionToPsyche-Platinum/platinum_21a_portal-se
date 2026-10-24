<script>
import { getSession } from "@/utils/auth";

export default {
    data() {
        const session = getSession();

        return {
            //Account information data
            username: session ? session.username : "Test Username",
            email: session ? session.email : "",
            showUploadForm: false,

            //Edit Profile data
            isEditing: false,
            editedUsername: session ? session.username : "",
            editedEmail: session ? session.email : "",
            message: ""
        }
    },
    methods: {
        editProfile() {
            this.isEditing = true;
            this.editedUsername = "";
            this.editedEmail = "";
            this.message = "";
        },
        saveProfile() {
            console.log("Save profile to be implemented later.")
        },
        cancelEditProfile() {
            this.editedUsername = this.username
            this.editedEmail = this.email
            this.isEditing = false;
            this.message = "";
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

        <!--Display current account info and button to edit-->
        <div v-if="!isEditing">
            <p><strong>Username:</strong> {{ username }}</p>
            <p><strong>Email:</strong> {{ email || "Not available"}}</p>

            <button @click="editProfile">Edit Profile</button>
        </div>

        <!--Display edit profile form-->
        <form v-else @submit.prevent="saveProfile">
            <!--Username-->
            <div>
                <label for="username">Username</label>
                <input id="username" type="text" v-model.trim="editedUsername" :placeholder="username" />
            </div>

            <!--Email-->
            <div>
                <label for="email">Email</label>
                <input id="email" type="email" v-model.trim="editedEmail" :placeholder="email" />
            </div>

            <button type="submit" @click="saveProfile">Save</button>
            <button type="button" @click="cancelEditProfile">Cancel</button>
        </form>
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