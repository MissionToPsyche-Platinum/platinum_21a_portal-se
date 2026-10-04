<template>
    <!-- Navigation Bar -->
    <nav class="nav-bar">
        <!-- Logo that links to the home page -->
        <router-link to="/" class="logo-link">
            <img src="@/assets/PsycheLogo.png" alt="Logo" class="logo"/>
        </router-link>

        <!-- "Home" and "About" links -->
        <div class="nav-links">
            <a href="/" class="nav-link">Home</a>
            <a href="https://psyche.ssl.berkeley.edu/" class="nav-link" target="_blank" rel="noopener noreferrer">About</a>

            <router-link v-if="!loggedIn" to="/login" class="nav-link">Login</router-link>
            <router-link v-if="loggedIn" to="/profile" class="nav-link">Profile</router-link>
            <router-link v-if="admin" to="/admin" class="nav-link">Admin</router-link>
            <button v-if="loggedIn" type="button" class="nav-link logout-button" @click="logout">Logout</button>
        </div>
    </nav>
 </template>

<script>
import { isLoggedIn, isAdmin, clearSession } from "@/utils/auth";

export default {
    name: "NavBar",
    data() {
        return {
            loggedIn: isLoggedIn(),
            admin: isAdmin()
        };
    },
    mounted() {
        window.addEventListener("session-changed", this.refreshSession);
    },
    beforeUnmount() {
        window.removeEventListener("session-changed", this.refreshSession);
    },
    methods: {
        refreshSession() {
            this.loggedIn = isLoggedIn();
            this.admin = isAdmin();
        },
        logout() {
            clearSession();
            this.$router.push("/");
        }
    }
};
</script>

<style scoped>
.nav-bar {
    position: sticky;
    top: 0;
    z-index: 1000;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 20px;
    background-color: #330066;
    border: 2px solid #ddd;
    border-bottom: 2px solid #ddd;
}

.nav-links {
   display: flex;
   gap: 20px;
}

.nav-link {
   color: #ffffff;
   font-weight: bold;
   transition: color 0.3s ease;
}

.nav-link:hover {
   color: #007bff;
}

.logo {
   height: 40px;
   cursor: pointer;
}

.logo-link {
   display: flex;
   align-items: center;
}

.logout-button {
    background: none;
    border: none;
    padding: 0;
    cursor: pointer;
    font: inherit;
}
 </style>