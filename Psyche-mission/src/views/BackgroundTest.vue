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
        // stop background animation
        cleanup() {
            if (this.animationId) {
                cancelAnimationFrame(this.animationId);
            }

            // reset background canvas
            const canvas = this.$refs.bgCanvas;
            if (canvas) {
                const context = canvas.getContext('2d');
                context.clearRect(0, 0, canvas.width, canvas.height);
            }
        },

        startBg1() {
            this.cleanup();
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

        startBg2() {
            this.cleanup();
            const canvas = this.$refs.bgCanvas;
            const context = canvas.getContext('2d');
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
            const stars = [];

            for (let i = 0; i < 500; i++) {
                // place stars on sides, top, and bottom of center point
                stars.push({
                    x: (Math.random() - 0.5) * canvas.width,
                    y: (Math.random() - 0.5) * canvas.height,
                    z: Math.random() * canvas.width
                })
            }

            const animate = () => {
                context.fillStyle = '#000000';
                context.fillRect(0, 0, canvas.width, canvas.height);

                // center of screen
                const cx = canvas.width / 2;
                const cy = canvas.height / 2;

                // star speed
                const speed = 0.5;

                stars.forEach(star => {
                    star.z -= speed;
                    
                    // reset star if out of view
                    if (star.z < 1) {
                        star.x = (Math.random() - 0.5) * canvas.width;
                        star.y = (Math.random() - 0.5) * canvas.height
                        star.z = canvas.width;
                    }

                    // move the stars outward as they approach the screen
                    const x = (star.x / star.z) * canvas.width + cx;
                    const y = (star.y / star.z) * canvas.height + cy;

                    // change the size of the star as it approaches the screen
                    // minimum size of 0.5, maximum size of 3
                    const radius = Math.max(0.5, (1 - star.z / canvas.width) * 3);

                    context.fillStyle = '#ffffff';
                    context.beginPath();
                    context.arc(x, y, radius, 0, Math.PI * 2);
                    context.fill();
                });

                this.animationId = requestAnimationFrame(animate);
            };

            if (this.animationId) {
                cancelAnimationFrame(this.animationId);
            }
            animate();
        }
    },
}
</script>

<template>
    <div class="background-container">
        <canvas ref="bgCanvas" class="animated-bg"></canvas>
        <div class="ui">
            <h1>Background Test Page</h1>
            <div class="bg-buttons">
                <button class="bg1-button" @click="startBg1">Background 1</button>
                <button class="bg2-button" @click="startBg2">Background 2</button>
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
    cursor: pointer;

}

.bg2-button {
    color: purple;
    border: 1px solid white;
    cursor: pointer;

}
</style>