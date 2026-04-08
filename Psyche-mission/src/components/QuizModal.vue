<script setup>
    const emit = defineEmits(['close'])
    const closeModal = () => emit('close')
</script>

<template>
    <div class="modal" @click="closeModal">
        <div class="modal-content" @click.stop>
            <h2>Quiz</h2>
            <p>Coming soon!</p>
            <div class="modal-actions">
                <button class="btn" @click="closeModal">Close</button>
                <button>Submit</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}
.modal-content {
    background: whitesmoke;
    color: black;
    padding: 24px;
    border-radius: 10px;
    text-align: center;
    width: 400px;
    max-width: 90%;
}
.modal-actions {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}
</style>
