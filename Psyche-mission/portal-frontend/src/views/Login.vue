<script>
import { setSession } from "@/utils/auth";
import { loadFavoritesFromServer } from "@/utils/favoriteService";

export default {
    data() {
        return {
            username: "",
            password: "",
            message: ""
        }
    },
    methods: {
        handleSubmit(event) {
            //Temporary test login
            //Replace with API call
            event.preventDefault();

            //User test login
            const username = "user"
            const password = "test"

            //Admin test login
            const adminUsername = "admin"
            const adminPassword = "test"

            const enteredUsername = this.username.trim();
            const enteredPassword = this.password.trim();

            if (!enteredUsername || !enteredPassword) {
                this.message = "Please enter a username and password."
                return;
            }

            if ((enteredUsername === username && enteredPassword === password) ||
                (enteredUsername === adminUsername && enteredPassword === adminPassword)) {
                this.message = "Login successful!"
                console.log("Login success.")

                const isAdminLogin = enteredUsername === adminUsername && enteredPassword === adminPassword;
                setSession({
                    username: enteredUsername,
                    role: isAdminLogin ? "admin" : "user"
                });
                loadFavoritesFromServer();
                this.$router.push(isAdminLogin ? "/admin" : "/profile");
                return;
            }

            fetch("http://localhost:8080/api/users/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: enteredUsername,
                    password: enteredPassword
                })
            })
                .then(async (response) => {
                    if (!response.ok) {
                        this.message = "Invalid credentials."
                        return;
                    }

                    const data = await response.json();
                    this.message = "Login successful!"
                    console.log("Login success.")
                    setSession({
                        username: data.username,
                        role: "user"
                    });
                    loadFavoritesFromServer();
                    this.$router.push("/profile");
                })
                .catch(() => {
                    this.message = "Could not reach the server. Make sure the backend is running."
                })
        }
    }
}
</script>

<template>
    <h1>Login</h1>
    <form @submit.prevent="handleSubmit" class="login-form">
        <div class="input">
            <input type="text" v-model="username" placeholder="Username" required />
            <input type="password" v-model="password" placeholder="Password" required />
            <button type="submit" class="submit-button">Login</button>
            <p v-if="message">{{ message }}</p>
            <router-link to="/signup" class="sign-up-button">Sign Up</router-link>
        </div>
    </form>
</template>

<style>
h1 {
    text-align: center;
}
.input {
    display: flex;
    flex-direction: column;
    margin: 0 auto;
    gap: 12px;
    max-width: 300px;
}
.sign-up-button {
    color: #330066;
}
</style>