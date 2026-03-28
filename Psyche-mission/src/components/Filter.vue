<script>
    export default {
        props: {
            isDark: Boolean
        }
    }
</script>

<template>
    <filter class="filter" :class="{ dark: isDark }">
        <span class="filter-title">Filter by</span>
            <select>
                <option disabled selected>Class</option>
                <option value="iridium">Iridium - 2025</option>
                <option value="tungsten">Tungsten - 2024</option>
                <option value="silver">Silver - 2023</option>
                <option value="copper">Copper - 2022</option>
                <option value="nickel">Nickel - 2021</option>
            </select>

            <select>
                <option disabled selected>Genre</option>
                <option value="arcade">Arcade</option>
                <option value="adventure">Adventure</option>
                <option value="simulation">Simulation</option>
                <option value="trivia">Trivia</option>
                <option value="AR">AR Experience</option>
            </select>

            <select>
                <option disabled selected>Age</option>
                <option value="elementary">Elementary</option>
                <option value="middle">Middle School</option>
                <option value="high">High School</option>
            </select>

            <select>
                <option disabled selected>Difficulty</option>
                <option value="easy">Easy</option>
                <option value="medium">Medium</option>
                <option value="hard">Hard</option>
            </select>

      <!-- Sorter allows users to sort games' list based on a selected criteria -->

      <select>
        <option disabled value="">Sort By</option><
        <option value="title-asc">Title A-Z</option> <!-- Sort title alphabetically (A to Z) -->
        <option value="title-desc">Title Z-A</option> <!-- Sort title alphabetically (Z to A) -->
        <option value="class-newest">Class Newest</option> <!-- Sort class by year (newest first) -->
        <option value="class-oldest">Class Oldest</option><!-- Sort class by year (oldest first) -->
        <option value="difficulty-easy">Difficulty Easy-Hard</option> <!-- Sort by difficulty (easy to hard) -->
        <option value="difficulty-hard">Difficulty Hard-Easy</option> <!-- Sort by difficulty (hard to easy) -->
      </select>

    </filter>
</template>

<style scoped>
.filter {
    display: flex;
    color: #ffffff;
    gap: 50px;
    padding: 20px;
}

.filter-title {
    color: #330066;
}

.filter.dark .filter-title {
    color: #ffffff;
}

.filter span {
    font-size: 20px;
    display: flex;
    color: #330066;
    align-items: center;
}

.filter select {
    color: #ffffff;
    padding: 10px;
    border-radius: 5px;
    background-color: #330066;
}
</style>