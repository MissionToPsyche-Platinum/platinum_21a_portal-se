<script>
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

            if (!this.username || !this.password) {
                this.message = "Please enter a username and password."
                return;
            }

            if ((this.username === username && this.password === password) ||
                (this.username === adminUsername && this.password === adminPassword)) {
                this.message = "Login successful!"
                console.log("Login success.")
            }
            else {
                this.message = "Invalid credentials."
            }
        }
    }
}
</script>

<template>
    <h1>Login</h1>
    <form @submit="handleSubmit" class="login-form">
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