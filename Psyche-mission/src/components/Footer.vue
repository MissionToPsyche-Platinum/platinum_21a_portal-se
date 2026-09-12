<template>
    <footer class="footer">
        <a href="https://www.facebook.com/MissionToPsyche" target="_blank">
            <i class="fab fa-facebook"></i>
        </a>
        <a href="https://www.instagram.com/missiontopsyche/" target="_blank">
            <i class="fab fa-instagram"></i>
        </a>
        <a href="https://x.com/MissionToPsyche" target="_blank">
            <i class="fab fa-x-twitter"></i>
        </a>
        <a href="https://www.youtube.com/channel/UC2BGcbPW8mxryXnjQcBqk6A" target="_blank">
            <i class="fab fa-youtube"></i>
        </a>

        <p class="copyright">
            <strong>{{ new Date().getFullYear() }} Psyche Mission</strong>. All rights reserved.
        </p>
    </footer>
</template>

<style scoped>
.footer {
    background: #111111;
    padding: 20px;
    text-align: center;
    color: #dddddd;
    border-top: 1px solid #2a2a2a;
}
.footer i {
    font-size: 22px;
    padding: 8px;
    color: #aaaaaa;
}
.footer a {
    color: inherit;
}
.footer a:hover i {
    color: #ffffff;
}
.copyright {
    margin: 8px 0 0;
    font-size: 0.9rem;
    color: #888888;
}
</style>
