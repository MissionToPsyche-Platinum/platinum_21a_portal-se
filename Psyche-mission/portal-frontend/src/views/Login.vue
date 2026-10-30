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
     async handleSubmit() {

            const enteredUsername = this.username.trim();
            const enteredPassword = this.password

            if (!enteredUsername || !enteredPassword) {
                this.message = "Please enter a username and password."
                return;
            }

       let csrf;

       try {
         const csrfResponse = await fetch(
             "http://localhost:8080/api/auth/csrf",
             {
               credentials: "include"
             }
         );

         if (!csrfResponse.ok) {
           throw new Error("Could not retrieve CSRF token.");
         }

         csrf = await csrfResponse.json();

       } catch (error) {
         console.error("CSRF initialization failed:", error);
         this.message = "Could not initialize login security.";
         return;
       }



       fetch("http://localhost:8080/api/users/login", {
         method: "POST",
         credentials: "include",
         headers: {
           "Content-Type": "application/json",
           [csrf.headerName]: csrf.token
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

                  const sessionResponse = await fetch(
                      "http://localhost:8080/api/auth/me",
                      {
                        method: "GET",
                        credentials: "include"
                      }
                  );

                  if (!sessionResponse.ok) {
                    this.message = "Login succeeded, but the authenticated session could not be verified.";
                    return;
                  }

                  const sessionUser = await sessionResponse.json();

                  if (
                      sessionUser.username !== data.username ||
                      sessionUser.role !== role
                  ) {
                    this.message = "Authenticated session does not match the logged-in user.";
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