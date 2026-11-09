<script>
export default {
   props: {
       isDark: Boolean
   },
   data() {
       return {
           searchRequest: ""
       }
   },
   methods: {
       emitSearch() {
           this.$emit('search', this.searchRequest)
       }
   }
}
</script>

<template>
   <div class="searchBar" :class="{ dark: isDark }">
       <input type="text" v-model="searchRequest" placeholder="Search" class="searchInput" @input="emitSearch" />
       <button class="searchButton" @click="emitSearch">Search</button>
   </div>
</template>

<style scoped>
.searchBar {
   display: flex;
   align-items: center;
   width: 100%;
   max-width: none;
   margin: 0;
   border-radius: 4px;
   background-color: #eeeeee;
   padding: 4px;
   border: 1px solid #cccccc;
}

.searchBar.dark {
   background-color: #1a1a1a;
   border: 1px solid #444444;
}

.searchInput {
   flex: 1;
   padding: 10px;
   border: none;
   outline: none;
   background: transparent;
   color: inherit;
}

.searchButton {
   padding: 10px 14px;
   border: none;
   cursor: pointer;
   border-radius: 4px;
   color: #fff;
   background-color: #111111;
   font-weight: 600;
}

.searchButton:hover {
   background-color: #333333;
}
</style>
