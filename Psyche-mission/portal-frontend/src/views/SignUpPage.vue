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
        async handleSubmit(event) {
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
            this.message = "Could not initialize signup security.";
            return;
          }

            fetch("http://localhost:8080/api/users", {
                method: "POST",
              credentials: "include",
              headers: {
                    "Content-Type": "application/json",
                  [csrf.headerName]: csrf.token
                },
                body: JSON.stringify({
                    username: this.username,
                    email: this.email,
                    password: this.password
                })
            })
                .then(async (response) => {
                    const data = await response.json().catch(() => ({}));

                    if (response.status === 201) {
                        this.message = "Account creation successful!"
                        this.$router.push("/login");
                        return;
                    }

                    this.message = data.error || "Account creation failed.";
                })
                .catch(() => {
                    this.message = "Could not reach the server. Make sure the backend is running."
                })
        }
    }
}
</script>

<template>
    <h1>Sign Up</h1>

    <form @submit="handleSubmit" class="sign-up-form">
        <div class="input">
            <input type="text" v-model="username" placeholder="Username" required />
            <input type="email" v-model="email" placeholder="Email" required />
            <input type="password" v-model="password" placeholder="Password" required />
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