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
      handleSubmit() {

            const enteredUsername = this.username.trim();
            const enteredPassword = this.password

            if (!enteredUsername || !enteredPassword) {
                this.message = "Please enter a username and password."
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
                  const role = String(data.role || "").toLowerCase();

                  if (role !== "admin" && role !== "user") {
                    this.message = "Invalid account role.";
                    return;
                  }

                  this.message = "Login successful!";
                  console.log("Login success.");

                  setSession({
                    userId: data.userId,
                    username: data.username,
                    email: data.email,
                    role: role
                  });

                  if (role === "admin") {
                    this.$router.push("/admin");
                  } else {
                    loadFavoritesFromServer();
                    this.$router.push("/profile");
                  }
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