<script>
export default {
    name: "BackgroundTest",
    data() {
        return{

        };
    },
    mounted() {

    },
    methods: {
        startBg() {
            const canvas = this.$refs.bgCanvas;
            const context = canvas.getContext('2d');
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
            const stars = [];

            // add 100 stars in random locations
            for (let i = 0; i < 100; i++) {
                stars.push({
                    x: Math.random() * canvas.width,
                    y: Math.random() * canvas.height,
                    z: Math.random() * 2
                });
            }

            const animate = () => {
                // color background
                context.fillStyle = '#12031d';
                context.fillRect(0, 0, canvas.width, canvas.height);

                // move the stars down the screen
                stars.forEach(star => {
                    star.y += star.z * 0.1;
                    if (star.y > canvas.height) {
                        star.y = 0;
                        star.x = Math.random() * canvas.width;
                    }
                    // draw the stars
                    context.fillStyle = '#f9a000';
                    context.beginPath();
                    context.arc(star.x, star.y, star.z * 1.2, 0, Math.PI * 2);
                    // change alpha to give depth
                    context.globalAlpha = star.z / 2;
                    context.fill();
                });

                context.globalAlpha = 1;
                this.animationId = requestAnimationFrame(animate);
            };
            animate();
        },
    },
}
</script>

<template>
    <div class="background-container">
        <canvas ref="bgCanvas" class="animated-bg"></canvas>
        <div class="ui">
            <h1>Background Test Page</h1>
            <div class="bg-buttons">
                <button class="bg1-button" @click="startBg">Background 1</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.background-container {
    position: relative;
    width: 100vw;
    height: 100vh;
    overflow: hidden;
}

.animated-bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 0;
}

.ui {
    position: relative;
    z-index: 10;
}

.bg1-button {
    border: 1px solid white;

}
</style>