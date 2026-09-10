<script>
export default {
    data() {
        return {
            username: "",
            email: "",
            password: "",
            confirmedPassword: "",
            message: ""
        }
    },
    methods: {
        handleSubmit(event) {
            event.preventDefault()

            this.message = ""

            if (!this.username || !this.email || !this.password) {
                this.message = "Please enter a username, email, and password.";

                return;
            }

            if (this.password.length < 8) {
                this.message = "Password must be at least 8 characters";

                return;
            }

            if (this.password !== this.confirmedPassword) {
                this.message = "Passwords do not match. Please try again.";

                return;
            }

            console.log("Sign Up button pressed")
            console.log("Username: " + this.username)

            this.message = "Account creation successful!"
        }
    }
}
</script>

<template>
    <h1>Sign Up</h1>

    <form @submit="handleSubmit" class="sign-up-form">
        <div class="input">
            <input type="text" v-model="username" placeholder="Username" />
            <input type="email" v-model="email" placeholder="Email" />
            <input type="password" v-model="password" placeholder="Password" />
            <input type="password" v-model="confirmedPassword" placeholder="Confirm Password" />
            <button type="submit" class="submit-button">Sign Up</button>
            <p v-if="message">{{ message }}</p>
            <router-link to="/login" class="login-button">Already have an account? Login</router-link>
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
.login-button {
    color: #330066;
}
</style>