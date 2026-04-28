<script>
   export default {
       props: {
           isDark: Boolean
       },
       data() {
           return {
                isOpen: false,
               filters: {
                   class: "",
                   genre: "",
                   age: "",
                   difficulty: ""
               },
               sortBy: "",
               search: ""
           };
       },
       watch: {
           filters: {
               deep: true,
               handler(newFilters) {
                   this.$emit('update-filter', newFilters)
               }
           }
       },
       methods: {
           updateSort() {  // emit the selected sort option to the HomePage component
               this.$emit("sort-games", this.sortBy);
           }
       }
   };
</script>


<template>
   <div class="filter" :class="{ dark: isDark }">

        <button @click="isOpen = !isOpen" class="filter-toggle">
            More Filter Options {{ isOpen ? '▲' : '▼' }}
        </button>

        <div v-if="isOpen" class="filter-dropdown">
            <span class="filter-title">Filter by</span>
            <select v-model="filters.class">
                <option value="">Class</option>
                <option value="Iridium - 2025">Iridium - 2025</option>
                <option value="Tungsten - 2024">Tungsten - 2024</option>
                <option value="Silver - 2023">Silver - 2023</option>
                <option value="Copper - 2022">Copper - 2022</option>
                <option value="Nickel - 2021">Nickel - 2021</option>
            </select>


            <select v-model="filters.genre">
                <option value="">Genre</option>
                <option value="Arcade">Arcade</option>
                <option value="Adventure">Adventure</option>
                <option value="Simulation">Simulation</option>
                <option value="Trivia">Trivia</option>
                <option value="AR Experience">AR Experience</option>
            </select>


            <select v-model="filters.age">
                <option value="">Age</option>
                <option value="Elementary">Elementary</option>
                <option value="Middle School">Middle School</option>
                <option value="High School">High School</option>
            </select>


            <select v-model="filters.difficulty">
                <option value="">Difficulty</option>
                <option value="Easy">Easy</option>
                <option value="Medium">Medium</option>
                <option value="Hard">Hard</option>
            </select>


            <!-- Sorter allows users to sort games' list based on a selected criteria -->


            <select v-model="sortBy" @change="updateSort">
            <option disabled value="">Sort By</option>
            <option value="title-asc">Title A-Z</option> <!-- Sort title alphabetically (A to Z) -->
            <option value="title-desc">Title Z-A</option> <!-- Sort title alphabetically (Z to A) -->
            <option value="class-newest">Class Newest</option> <!-- Sort class by year (newest first) -->
            <option value="class-oldest">Class Oldest</option><!-- Sort class by year (oldest first) -->
            <option value="difficulty-easy">Difficulty Easy-Hard</option> <!-- Sort by difficulty (easy to hard) -->
            <option value="difficulty-hard">Difficulty Hard-Easy</option> <!-- Sort by difficulty (hard to easy) -->
            </select>
        </div>
   </div>
</template>


<style scoped>
.filter {
    position: relative;
    display: inline-block;
    padding: 20px;
    z-index: 10;
}

.filter-toggle {
    background-color: #330066;
    color: white;
    padding: 12px 24px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 1.1rem;
    font-weight: bold;
}

.filter-dropdown {
    position: absolute;
    top: 100%;
    left: 0;
    background-color: #330066;
    padding: 20px;
    border-radius: 0 8px 8px 8px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: row;
    gap: 15px;
    min-width: max-content;
    z-index: 100;    
}

.filter-title {
   color: white;
   font-size: 20px;
}


.filter select {
   color: #ffffff;
   padding: 10px;
   border-radius: 5px;
   background-color: #10071a;
}

@media (max-width: 768px) {
    .filter-dropdown {
        flex-direction: column;
        align-items: stretch;
    }
}
</style>
