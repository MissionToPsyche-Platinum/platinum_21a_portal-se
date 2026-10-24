<script>
export default {
    data() {
        return {
            username: "",
            password: ""
        }
    },
    methods: {
        handleSubmit(event) {
            //Replace with API call
            event.preventDefault();
            console.log("Login button pressed")
        }
    }
}
</script>

<template>
    <h1>Login</h1>
    <form @submit="handleSubmit" class="login-form">
        <div class="input">
            <input type="text" v-model="username" placeholder="Username" />
            <input type="password" v-model="password" placeholder="Password" />
            <button type="submit" class="submit-button">Login</button>
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