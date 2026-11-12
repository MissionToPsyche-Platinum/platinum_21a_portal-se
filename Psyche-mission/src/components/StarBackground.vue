<script>
export default {
    name: "StarBackground",
    props: {
        starColor: {
            type: String,
            default: "#ffffff"
        }
    },
    data() {
        return {
            animationId: null
        };
    },
    mounted() {
        this.initStars();
    },
    beforeUnmount() {
        if (this.animationId) {
            cancelAnimationFrame(this.animationId);
        }
    },
    methods: {
        initStars() {
            const canvas = this.$refs.bgCanvas;
            const context = canvas.getContext("2d");
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
            const stars = [];

            // create small stars
            for (let i = 0; i < 40; i++) {
                stars.push({
                    x: Math.random() * canvas.width,
                    y: Math.random() * canvas.height,
                    radius: 1.5,
                    vx: -0.03,
                    vy: -0.03,
                    alpha: 0.45
                });
            }

            // create large stars
            for (let i = 0; i < 20; i++) {
                stars.push({
                    x: Math.random() * canvas.width,
                    y: Math.random() * canvas.height,
                    radius: 3.0,
                    vx: -0.07,
                    vy: -0.07,
                    alpha: 0.5
                });
            }

            const animate = () => {
                context.clearRect(0, 0, canvas.width, canvas.height);

                context.fillStyle = this.starColor;
                stars.forEach(star => {
                    // Move the stars
                    star.x += star.vx;
                    star.y += star.vy;

                    // wrap back around once they reach the left side or top of screen
                    if (star.x < -star.radius) {
                        star.x = canvas.width + star.radius;
                    }
                    if (star.y < -star.radius) {
                        star.y = canvas.height + star.radius;
                    }

                    // draw the stars
                    context.globalAlpha = star.alpha;
                    context.beginPath();
                    context.arc(star.x, star.y, star.radius, 0, Math.PI * 2);
                    context.fill();
                });

                context.globalAlpha = 1;
                this.animationId = requestAnimationFrame(animate);
            };

            animate();
        }
    }
};
</script>

<template>
    <canvas ref="bgCanvas" class="star-canvas"></canvas>
</template>

<style scoped>
.star-canvas {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    pointer-events: none;
    z-index: 0;
}
</style>