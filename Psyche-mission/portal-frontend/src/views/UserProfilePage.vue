<script>
import { getSession, setSession } from "@/utils/auth";
import UserPortalLayout from "@/components/UserPortalLayout.vue";

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
    },
    components: {
        UserPortalLayout
    }
}
</script>

<template>
    <UserPortalLayout :username="username">
    <div class="profile-container">
        <div class="account-info">
            <div class="account-info-header">
                <h3>Account Information</h3>
                <span class="account-status">Account Active</span>
            </div>

            <!--Display current account info and button to edit-->
            <div v-if="!isEditing">
                <div class="profile-detail">
                    <p><strong>Username:</strong> {{ username }}</p>
                </div>

                <div class="profile-detail">
                    <p><strong>Email:</strong> {{ email || "Not available"}}</p>
                </div>

                <div class="profile-actions">
                    <button class="button" @click="editProfile">Edit Profile</button>
                </div>
            </div>

            <!--Display edit profile form-->
            <form v-else @submit.prevent="saveProfile">
                <!--Username-->
                <div class="profile-detail">
                    <label for="username">Username</label>
                    <input id="username" type="text" v-model.trim="editedUsername" :placeholder="username" />
                </div>

                <!--Email-->
                <div class="profile-detail">
                    <label for="email">Email</label>
                    <input id="email" type="email" v-model.trim="editedEmail" :placeholder="email" />
                </div>

                <button class="button" type="submit">Save</button>
                <button class="button" type="button" @click="cancelEditProfile">Cancel</button>

                <p v-if="message">{{ message }}</p>
            </form>
        </div>
    </div>
    </UserPortalLayout>
</template>

<style>
.profile-container {
    padding: 30px;
}
.account-info {
    margin: 0 auto;
    max-width: 700px;
    text-align: center;
    padding: 28px;
    border: 1px solid #ddd;
    border-radius: 12px;
}
.account-info-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 16px;
    margin-bottom: 24px;
}
.account-info-header h3 {
    margin: 0;
}
.account-status {
    font-size: 12px;
    font-weight: 600;
    padding: 6px 12px;
    border-radius: 20px;
    background: #a3e635;
}
.profile-detail {
    margin-bottom: 20px;
}
.profile-detail label {
    display: block;
    margin-bottom: 6px;
    font-size: 13px;
    font-weight: 600;
}
.profile-detail p {
    margin: 0;
}
.profile-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    margin-top: 24px;
}
.button {
    padding: 10px 22px;
    border: none;
    border-radius: 999px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: transform;
}
.button:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
}
.button:active {
    transform: translateY(0);
}
.button:focus-visible {
    outline: 3px solid #94a3b8;
    outline-offset: 3px;
}
</style>