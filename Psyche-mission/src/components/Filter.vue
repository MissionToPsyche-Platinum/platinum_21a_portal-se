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
   <div class="filter" :class="{ dark: isDark, open: isOpen }">

        <button @click="isOpen = !isOpen" class="filter-toggle">
            More Filter Options
            <span class="filter-caret" aria-hidden="true">{{ isOpen ? '▲' : '▼' }}</span>
        </button>

        <div v-if="isOpen" class="filter-dropdown">
            <p class="filter-title">Filter by</p>
            <div class="filter-fields">
                <label class="filter-field">
                    <span>Class</span>
                    <span class="select-wrap">
                        <select v-model="filters.class">
                            <option value="">All classes</option>
                            <option value="Iridium - 2025">Iridium - 2025</option>
                            <option value="Tungsten - 2024">Tungsten - 2024</option>
                            <option value="Silver - 2023">Silver - 2023</option>
                            <option value="Copper - 2022">Copper - 2022</option>
                            <option value="Nickel - 2021">Nickel - 2021</option>
                        </select>
                    </span>
                </label>

                <label class="filter-field">
                    <span>Genre</span>
                    <span class="select-wrap">
                        <select v-model="filters.genre">
                            <option value="">All genres</option>
                            <option value="Arcade">Arcade</option>
                            <option value="Adventure">Adventure</option>
                            <option value="Simulation">Simulation</option>
                            <option value="Trivia">Trivia</option>
                            <option value="AR Experience">AR Experience</option>
                        </select>
                    </span>
                </label>

                <label class="filter-field">
                    <span>Age</span>
                    <span class="select-wrap">
                        <select v-model="filters.age">
                            <option value="">All ages</option>
                            <option value="Elementary">Elementary</option>
                            <option value="Middle School">Middle School</option>
                            <option value="High School">High School</option>
                        </select>
                    </span>
                </label>

                <label class="filter-field">
                    <span>Difficulty</span>
                    <span class="select-wrap">
                        <select v-model="filters.difficulty">
                            <option value="">All difficulties</option>
                            <option value="Easy">Easy</option>
                            <option value="Medium">Medium</option>
                            <option value="Hard">Hard</option>
                        </select>
                    </span>
                </label>

                <label class="filter-field">
                    <span>Sort</span>
                    <span class="select-wrap">
                        <select v-model="sortBy" @change="updateSort">
                            <option disabled value="">Sort by</option>
                            <option value="title-asc">Title A-Z</option>
                            <option value="title-desc">Title Z-A</option>
                            <option value="class-newest">Class newest</option>
                            <option value="class-oldest">Class oldest</option>
                            <option value="difficulty-easy">Difficulty easy-hard</option>
                            <option value="difficulty-hard">Difficulty hard-easy</option>
                        </select>
                    </span>
                </label>
            </div>
        </div>
   </div>
</template>

<style scoped>
.filter {
    position: relative;
    display: inline-block;
    padding: 0;
    z-index: 100;
}

.filter-toggle {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 10px 16px;
    border: 1px solid currentColor;
    border-radius: 4px;
    background: transparent;
    color: inherit;
    cursor: pointer;
    font-size: 1rem;
    font-weight: 600;
}

.filter-toggle:hover,
.filter.open .filter-toggle {
    background: #111111;
    color: #ffffff;
    border-color: #111111;
}

.filter-caret {
    font-size: 0.7rem;
    opacity: 0.7;
}

.filter-dropdown {
    position: absolute;
    top: calc(100% + 8px);
    right: 0;
    left: auto;
    width: min(420px, calc(100vw - 40px));
    padding: 16px;
    border: 1px solid #666666;
    border-radius: 4px;
    background: #000000;
    color: #ffffff;
    display: flex;
    flex-direction: column;
    gap: 12px;
    z-index: 100;
    box-shadow: 0 12px 28px rgba(0, 0, 0, 0.45);
}

.filter:not(.dark) .filter-dropdown {
    background: #ffffff;
    color: #111111;
    box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
}

.filter-title {
    margin: 0;
    font-size: 0.8rem;
    font-weight: 600;
    letter-spacing: 0.06em;
    text-transform: uppercase;
    opacity: 0.7;
    text-align: left;
}

.filter-fields {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 12px;
}

.filter-field {
    display: flex;
    flex-direction: column;
    gap: 6px;
    text-align: left;
}

.filter-field > span:first-child {
    font-size: 0.75rem;
    font-weight: 600;
    opacity: 0.7;
}

.select-wrap {
    position: relative;
    display: block;
}

.select-wrap::after {
    content: "▾";
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
    pointer-events: none;
    opacity: 0.65;
    font-size: 0.75rem;
}

.filter select {
    appearance: none;
    -webkit-appearance: none;
    width: 100%;
    padding: 9px 28px 9px 10px;
    border-radius: 4px;
    border: 1px solid #666666;
    background: transparent;
    color: inherit;
    font: inherit;
    cursor: pointer;
}

.filter.dark select {
    color-scheme: dark;
}

.filter:not(.dark) select {
    color-scheme: light;
}

.filter select:hover,
.filter select:focus {
    border-color: currentColor;
    outline: none;
}

@media (max-width: 768px) {
    .filter-fields {
        grid-template-columns: 1fr;
    }
}
</style>
