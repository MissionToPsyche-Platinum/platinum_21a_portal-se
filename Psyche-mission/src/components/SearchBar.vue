<script>
export default {
    props: {
        isDark: Boolean
    },
    data() {
        searchRequest: ""
    },
    methods: {

    }
}
</script>


<template>
    <div class="searchBar">
        <input type="text" v-model="searchRequest" placeholder="Search" class="searchInput" />
        <button class="searchButton">Search</button>
    </div>
</template>


<style scoped>
.searchBar {
    display: flex;
    align-items: center;
    width: 100%;
    max-width: 500px;
    margin: 0 auto;
    border-radius: 10px;
    background-color: #f1f1f1;
    padding: 5px;
}
.searchInput {
    flex: 1;
    padding: 10px;
    border: none;
    outline: none;
    background: transparent;
    border-radius: 10px;
}
.searchButton {
    padding: 10px;
    border: none;
    cursor: pointer;
    border-radius: 10px;
    color: #fff;
    background-color: #330066;
}
</style>