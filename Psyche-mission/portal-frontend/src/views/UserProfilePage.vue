<script>
export default {
    data() {
        return {
            username: "Test Username"
        }
    }
}
</script>

<template>
<div class="profile-container">
    <div class="profile-header">
        <h1>Welcome, {{ username }}</h1>
    </div>
    <div class="upload-section">
        <h2>Game Upload Request</h2>
        <p>Submit a request to add a new game to the portal.</p>
        <button>Submit New Game Request</button>
    </div>
</div>
</template>

<style>
.profile-container {
    border: 2px solid black;
    padding: 30px;
    margin: 30px auto;
    width: 80%;
}
.profile-header {
    text-align: center;
    padding: 30px;
    border-bottom: 2px solid black;
}
.upload-section {
    border: 2px solid black;
    padding: 30px;
    margin: 30px auto;
    width: 80%;
    text-align: center;
}
button {
    padding: 10px 20px;
    cursor: pointer;
}
</style>