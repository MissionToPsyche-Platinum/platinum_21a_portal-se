<script>
export default {
    name: "SidePanel",
    data() {
        return {};
    }
};
</script>

<template>
    <div class="side-panel">
        <div class="panel-content">
            <slot></slot>
        </div>
    </div>
</template>

<style scoped>
    .side-panel {
        width: 200px;
        height: 100vh;
        position: sticky;
        /* top: 0; */
        border-right: 1px solid rgba(128, 128, 128, 0.2);
        display: flex;
        flex-direction: column;
        padding: 20px;
        z-index: 100;
    }
</style>