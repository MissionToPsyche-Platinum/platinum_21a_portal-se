<script>
import { getSession, setSession } from "@/utils/auth";

export default {
    data() {
        const session = getSession();

        return {
            //Account information data
            userId: session ? session.userId : null,
            username: session ? session.username : "Test Username",
            email: session ? session.email : "",
            showUploadForm: false,

            //Edit Profile data
            isEditing: false,
            editedUsername: "",
            editedEmail: "",
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
        async saveProfile() {
            //Get current session
            const session = getSession();

            console.log("Session on profile page:", session);
            console.log("User ID:", session ? session.userId : "No session");

            if (!session || !session.userId) {
                this.message = "Please login."

                return;
            }

            try {
                const response = await fetch(`http://localhost:8080/api/users/${session.userId}/profile`, {
                    method: "PUT",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({
                        username: this.editedUsername, email: this.editedEmail
                    })
                })
                const data = await response.json();

                console.log("Backend login response:", data);

                if (!response.ok) {
                    this.message = data.error || "Failed to update profile.";

                    return;
                }

                this.username = data.username;
                this.email = data.email;
                this.userId = data.userId;

                setSession({
                    userId: data.userId,
                    username: data.username,
                    email: data.email,
                    role: data.role.toLowerCase()});

                this.isEditing = false;
                this.message = "Profile successfully updated!";
            }
            catch (error) {
                console.log(error)
                this.message = "Could not connect to server. Please try again."
            }
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

            <button type="submit">Save</button>
            <button type="button" @click="cancelEditProfile">Cancel</button>

            <p v-if="message">{{ message }}</p>
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