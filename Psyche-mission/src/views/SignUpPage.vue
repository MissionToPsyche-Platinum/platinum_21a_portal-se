<script>
export default {
    data() {
        return {
            username: "",
            email: "",
            password: ""
        }
    },
    methods: {
        handleSubmit(event) {
            event.preventDefault()
            console.log("Sign Up button pressed")
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
            <button type="submit" class="submit-button">Sign Up</button>
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