<script>
export default {
    name: 'Asteroids',
    props: {
        width: {
            type: Number,
            default: 600
        },
        height: {
            type: Number,
            default: 600
        }
    },
    data() {
        return {
            canvas: null,
            context: null,
            
            ship: {
                x: 300,
                y: 300,
                angle: 0,           // radians
                width: 36,
                height: 18,
                rotationSpeed: 0.07 // adjust as needed
            },
            
            keys: {
                left: false,
                right: false
            }
        };
    },
    mounted() {
        this.canvas = this.$refs.gameCanvas;
        this.context = this.canvas.getContext('2d'); // 2D drawing API

        // ship located in center
        this.ship.x = this.width / 2;
        this.ship.y = this.height / 2;

        window.addEventListener('keydown', this.handleKeyDown);
        window.addEventListener('keyup', this.handleKeyUp);

        this.gameLoop();
    },
    methods: {
        // event handlers for key input
        handleKeyDown(e) {
            if (e.code === 'KeyA') {
            this.keys.left = true;
            }
            if (e.code === 'KeyD') {
            this.keys.right = true;
            }
        },
        handleKeyUp(e) {
            if (e.code === 'KeyA') {
            this.keys.left = false;
            }
            if (e.code === 'KeyD') {
            this.keys.right = false;
            }
        },
        
        gameLoop() {
            this.update();    // update state
            this.draw();      // draw new frame
            requestAnimationFrame(() => this.gameLoop()); // run game loop before next repaint
        },
        
        update() {
            // rotate left
            if (this.keys.left) {
                this.ship.angle -= this.ship.rotationSpeed;
            }
            // rotate right
            if (this.keys.right) {
                this.ship.angle += this.ship.rotationSpeed;
            }
        },
        
        draw() {
            // clear frame
            this.context.clearRect(0, 0, this.width, this.height);

            // save current coordinate grid
            this.context.save();

            // rotate based on ships center
            // rotates entire coordinate grid which is why save() is needed
            this.context.translate(this.ship.x, this.ship.y);
            this.context.rotate(this.ship.angle);

            // draw the ship (rectangle)
            this.context.fillStyle = '#ffffff';
            this.context.fillRect(
                -this.ship.width / 2,
                -this.ship.height / 2,
                this.ship.width,
                this.ship.height
            );

            // draw front of ship for visual confirmation of correct movement
            this.context.fillStyle = '#00ffff';
            this.context.fillRect(
                this.ship.width / 4,
                -this.ship.height / 4,
                this.ship.width / 4,
                this.ship.height / 2
            );

            // restore original canvas coordinates
            this.context.restore();
        }
    }
};
</script>

<template>
    <div class="canvas-wrapper">
        <canvas 
            ref="gameCanvas" 
            :width="width" 
            :height="height"
        ></canvas>
    </div>
</template>

<style scoped>
.canvas-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 1.5rem;
}

canvas {
    background-color: #0b0c10;
    border: 2px solid #1f2833;
    border-radius: 6px;
    box-shadow: 0 0 16px rgba(0, 255, 255, 0.12);
    outline: none;
}
</style>