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

        <!--Admin navigation -->
          <router-link to="/admin" class="nav-link">Admin</router-link>
        </div>
    </nav>
 </template>

<script>
export default {
    name: "NavBar"
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
 </style>