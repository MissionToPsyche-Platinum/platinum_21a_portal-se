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
    background: white;
    padding: 20px;
    text-align: center;
}
.footer i {
    font-size: 50px;
    padding: 10px;
}
.footer i.fa-facebook {
    color: #1877f2;
}
.footer i.fa-instagram {
    color: #e1306c;
}
.footer i.fa-x-twitter {
    color: #1da1f2;
}
.footer i.fa-youtube {
    color: #ff0000;
}
</style>
