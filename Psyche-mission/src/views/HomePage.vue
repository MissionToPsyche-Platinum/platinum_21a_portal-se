<script>
import gameData from '../assets/games.json'
import GameLink from '../components/GameLink.vue'
import Filter from '../components/Filter.vue'
import SearchBar from '../components/SearchBar.vue'
import QuizModal from '../components/QuizModal.vue'
import FeaturedGame from '../components/FeaturedGame.vue'

export default {
  name: "HomePage",
  components: {
    GameLink,
    Filter,
    SearchBar,
    QuizModal,
    FeaturedGame
  },
  data() {
    return {
      isDark: true,// variable to track if dark mode is active
      games: gameData.games, // creates an array of game objects from JSON file
      darkColor: "#000000",// variable for selected dark mode color with a default value
      lightColor: "#ffffff",// variable for selected light mode color with a default value
      activeFilters: {
        class: "",
        genre: "",
        age: "",
        difficulty: ""
      },
      sortBy: "" /*===task 83===*/,
      searchRequest: "",
      isQuizOpen: false,
      platforms: [
        {label: 'Web Games', genre: 'Web Based', desc: 'Browser-based games.'},
        {label: 'AR Experiences', genre: 'AR Experience', desc: 'Augmented reality.'},
        {label: 'VR Experiences', genre: 'VR Experience', desc: 'Virtual Reality.'},
        {label: 'All Experiences', genre: '', desc: 'Show everything.'},
      ],

      favoriteIds: [], // store saved favorite game IDs

      favoritesOnly: false, // true if main list shows only favorites

      quizResults: [], // stores quiz results

      showQuizResults: false
    };
  },
  /*==========task 83=========*/
  computed: {

    filteredGames() {
      return this.games.filter(game => {
        let genreMatch = true;


        if (this.activeFilters.genre === 'Web Based') {
          genreMatch = game.genre != 'AR Experience' && game.genre != 'VR Experience'
        } else if (this.activeFilters.genre) {
          genreMatch = game.genre === this.activeFilters.genre;
        }

        const classMatch = this.activeFilters.class ? game.class === this.activeFilters.class : true;
        const ageMatch = this.activeFilters.age ? game.age === this.activeFilters.age : true;
        const difficultyMatch = this.activeFilters.difficulty ? game.difficulty === this.activeFilters.difficulty : true;
        const searchMatch = this.searchRequest ? game.title.toLowerCase().includes(this.searchRequest.toLowerCase()) ||
            game.description.toLowerCase().includes(this.searchRequest.toLowerCase()) : true

        const favoriteMatch = this.favoritesOnly ? this.favoriteIds.includes(game.id) : true;

        return classMatch && genreMatch && ageMatch && difficultyMatch && searchMatch && favoriteMatch;
      });
    },
    //sorting logic
    sortGames() {
      // copy the array to preserve the original state
      let sorted = [...this.filteredGames];

      // Sort alphabetically by game title (A to Z)
      if (this.sortBy === "title-asc") {
        sorted.sort((a, b) => a.title.localeCompare(b.title));

        // Sort alphabetically by game title (Z to A)
      } else if (this.sortBy === "title-desc") {
        sorted.sort((a, b) => b.title.localeCompare(a.title));

        // Sort class by year (newest first)
      } else if (this.sortBy === "class-newest") {
        sorted.sort((a, b) => b.class.localeCompare(a.class));

        // Sort class by year (oldest first)
      } else if (this.sortBy === "class-oldest") {
        sorted.sort((a, b) => a.class.localeCompare(b.class));

        // Sort by difficulty (easy to hard)
      } else if (this.sortBy === "difficulty-easy") {
        const order = {Easy: 1, Medium: 2, Hard: 3};
        sorted.sort((a, b) => order[a.difficulty] - order[b.difficulty]);

        //Sort by difficulty (hard to easy)
      } else if (this.sortBy === "difficulty-hard") {
        const order = {Easy: 1, Medium: 2, Hard: 3};
        sorted.sort((a, b) => order[b.difficulty] - order[a.difficulty]);
      }

      return sorted;
    },
    // favorite games list filtered down to saved ones, also respect filtering options
    favoriteGames() {
      return this.filteredGames.filter(game => this.favoriteIds.includes(game.id));
    },
    // returns a formatted favorites count or returns an empty string if no favorites
    favoritesCount() {
      if (this.favoriteGames.length > 0)
        return `(${this.favoriteGames.length})`;

      return ""
    },


    displayGames() {
        return this.showQuizResults ? this.quizResults :this.sortGames;
    },

    featuredGame() {
      if (!this.games || this.games.length === 0) {
        return null;
      }

      const start = new Date("2026-04-22")
      const today = new Date()

      start.setHours(0,0,0,0)
      today.setHours(0,0,0,0)

      const days = Math.floor((today - start) / (1000 * 60 * 60 * 24))
      const idx = days % this.games.length

      return this.games[idx]
    }
  },
  mounted() {
    // get the previously saved mode from the browser local storage
    const savedMode = localStorage.getItem("savedMode");
    //check if previous mode is a dark mode, enable dark mode on page load
    if (savedMode === "Light") {
      this.isDark = false;
      this.darkColor = "#ffffff";
      this.lightColor = "#000000";
    } else {
      this.isDark = true;
      this.darkColor = "#000000";
      this.lightColor = "#ffffff";
    }
    this.loadFavorites();
    window.addEventListener("favorites-updated", this.loadFavorites);
  },
  methods: {

    /*==============task 83=================*/
    // retrieve the sort selection emitted by the Filter component and assign it to "sortBy"
    handleSort(value) {
      this.sortBy = value;// trigger computed
    },


    //this function toggle between dark mode and light mode
    toggleMode() {
      this.isDark = !this.isDark;//if dark mode is active change to light mode, if light mode is active change to dark mode
      if (this.isDark) {
        this.darkColor = "#000000";
        this.lightColor = "#ffffff";
        localStorage.setItem("savedMode", "Dark");// save dark mode in local storage
      } else {
        this.darkColor = "#ffffff";
        this.lightColor = "#000000";
        localStorage.setItem("savedMode", "Light");// save light mode in local storage
      }
      localStorage.setItem("savedDarkColor", this.darkColor);
      localStorage.setItem("savedLightColor", this.lightColor);
    },

    // these function update the localstorage with the current colors selection
    updateDarkColor() {

      localStorage.setItem("savedDarkColor", this.darkColor);
    },

    updateLightColor() {
      localStorage.setItem("savedLightColor", this.lightColor);
    },

    handleFilters(filters) {
      this.activeFilters = filters;
    },

    openQuiz() {
      this.isQuizOpen = true;
    },

    closeQuiz() {
      this.isQuizOpen = false;
    },

    setGenreFilter(genre) {
      if (this.activeFilters.genre === genre) {
        this.activeFilters.genre = "";
      } else {
        this.activeFilters.genre = genre;
      }
    },

    handleQuizResults(results) {
      let filtered = this.filterByDevice(this.games, results.device)
      let scored = this.scoreQuiz(filtered, results)
      let topGames = this.getTopGames(scored, results.device)

      //console.log("QUIZ RESULTS:", {filtered, scored, topGames})
      console.log(topGames.map(game => ({title: game.title, genre: game.genre, score: game.quizScore})))

      console.log("Scroll: ", document.querySelector(".games-placeholder"))

      this.showQuizResults = true
      this.quizResults = topGames
      this.isQuizOpen = false

      this.$nextTick(() => {
        requestAnimationFrame(() => {
           const el = document.querySelector(".games-placeholder")

           if (!el)
            return

           el.scrollIntoView({behavior:"smooth", block: "start"})
         })

      })
    },

    loadFavorites() {
      const saved = localStorage.getItem("favoriteGames");
      this.favoriteIds = saved ? JSON.parse(saved) : [];
    },

    // toggle for favorites view
    toggleFavoritesView() {
      this.favoritesOnly = !this.favoritesOnly;
    },

    normalizeText(text) {
      return text.toLowerCase()
    },

    //Difficulty is the difficulty of the game being looked at
    //Selected is the difficulty selected by the user
    getDifficultyScore(difficulty, selected) {
        if (!selected) {
            return 0;
        }

        const difficultyScores = {
            Easy: ["Easy", "Medium"],
            Medium: ["Easy", "Medium", "Hard"],
            Hard: ["Medium", "Hard"]
        }

        if (difficulty === selected) {
            return 2;
        }
        else if (difficultyScores[selected].includes(difficulty)) {
            return 1;
        }
        else {
            return 0;
        }
    },

    filterByDevice(games, device) {
        if (device === "VR Headset") {
            return games.filter(game => game.genre.includes("VR Experience"));
        }
        else if (device === "Phone/Tablet") {
            return games.filter(game => game.genre.includes("AR Experience"));
        }
        else {
            return games.filter(game => !game.genre.includes("VR Experience") && !game.genre.includes("AR Experience"));
        }

        return games;
    },

    keywordScoring(text, keywords) {
        const max = 2
        let score = 0

        for (let word of keywords) {
            if (text.includes(word)) {
                score++

                if (score >= max) {
                    break
                }

            }
        }

        return score
    },

    getQuizConfiguration() {
      return {
        fun: {
            "Fast-paced action": {
                genres: ["Arcade"],
                keywords: ["action", "fast", "quick", "speed", "surviv", "dodg", "avoid", "reflex", "navigat", "evade", "obstacle"]
            },
            "Exploring space": {
                genres: ["Adventure"],
                keywords: ["explor", "discover", "journey", "travel", "rover", "navigat", "mission"]
            },
            "Solving puzzles": {
                genres: ["Adventure", "Simulation"],
                keywords: ["escap", "solv", "puzzl", "clue", "unlock", "challeng"]
            },
            "Trivia and quizzes": {
                genres: ["Trivia"],
                keywords: ["quiz", "question", "trivia", "answer"]
            },
            "Doing science or collecting data": {
                genres: ["Simulation"],
                keywords: ["experiment", "discover", "collect", "data", "analy", "scan", "measur", "research", "sampl"]
            },
            "Building or managing things": {
                genres: ["Simulation"],
                keywords: ["build", "upgrad", "manag", "resource", "construct", "design", "creat"]
            }
        },
        interest: {
            "Psyche asteroid": ["psyche", "asteroid"],
            "Planets and moons": ["mars", "moon", "planet"],
            "Space missions and spacecraft": ["mission", "spacecraft", "satellite", "rocket", "ship", "launch", "orbit"],
            "Learning science concepts": ["learn", "experiment", "educat", "scien"],
            "Just playing for fun": []
        }
      }
    },

    scoreQuiz(games, answers) {
      const quizConfig = this.getQuizConfiguration()

      return games.map(game => {
        let score = 0
        const desc = this.normalizeText(game.description)

        //Conditional difficulty scoring for laptop/desktop games
        if (answers.device === "Laptop/Desktop") {
            score += this.getDifficultyScore(game.difficulty, answers.difficulty)
        }

        const q3Config = quizConfig.fun[answers.fun]

        if (q3Config) {
            if (q3Config.genres.includes(games.genre)) {
                score += 2
            }

            score += this.keywordScoring(desc, q3Config.keywords)
        }

        const q4Keywords = quizConfig.interest[answers.interest]

        if (q4Keywords) {
            score += this.keywordScoring(desc, q4Keywords)
        }

        return {
          ...game,
          quizScore: score
        }
      })
    },

    getTopGames(scoredGames, device) {
        const sorted = scoredGames.sort((g1, g2) => g2.quizScore - g1.quizScore)

        if (device === "VR Headset" || device === "Phone/Tablet") {
            return sorted.slice(0,2)
        }

        return sorted.slice(0,3)
    },
  }
}
</script>

<template>
  <!--dynamically adds either the "light" or "dark" class-->
  <!--root container-->
  <div class="main"
    :class="{ 'dark-mode': isDark, 'light-mode': !isDark }"
    :style="[{backgroundColor: darkColor},{ color: lightColor}]">

    <!-- Containers for background effects -->
    <div class="star-div stars-small" :class="{ 'stars-inverted': !isDark }"></div>
    <div class="star-div stars-large" :class="{ 'stars-inverted': !isDark }"></div>
    <FeaturedGame v-if="featuredGame" :game="featuredGame" :isDark="isDark" :textColor="lightColor">
      <template #toolbar>
        <!--      toggle button that change the theme based on the mode, calls th toggleMode() function-->
        <div class="theme-controls">
          <button class="toggle"
                  :style="[{backgroundColor: isDark? darkColor: lightColor},{color: isDark? lightColor: darkColor}]"
                  @click="toggleMode">
            <!--dynamically change thee text based on the mode-->
            {{ isDark ? "Switch to Light" : "Switch to Dark" }}
          </button>
          <div class="pickers">
            <label>
              Background
              <input type="color" v-model="darkColor" @input="updateDarkColor" class="picker"
                     :style="[{backgroundColor: isDark? lightColor: darkColor},{color: isDark? lightColor: darkColor}]"/>
            </label>
            <label>
              Text
              <input type="color" v-model="lightColor" @input="updateLightColor" class="picker"
                     :style="[{backgroundColor: isDark? lightColor: darkColor},{color: isDark? lightColor: darkColor}]"/>
            </label>
          </div>
        </div>
      </template>
      <div id="quiz-container" class="quiz-container">
        <button id="quiz-button" class="quiz-button" @click="openQuiz">Take Quiz</button>
      </div>
    </FeaturedGame>

    <!-- page header -->
    <!-- <header class="header">
      <h1>Psyche Mission Game Portal AR, VR, and XR</h1>
    </header> -->

    <!-- place holder for an introduction -->
    <!-- <section class="intro">
      <h2>Welcome to the Psyche Mission's Web-Based Portal By Team 1 platinum_21a_portal-se</h2>
      <p>Discover the latest web-based experiences from the Psyche Mission team.</p>
    </section> -->

    <QuizModal v-if="isQuizOpen" @quiz-complete="handleQuizResults" @close="closeQuiz"/>

    <h2 id="games" class="games-heading">Games</h2>
    <div class="browse-bar">
      <section class="search">
        <SearchBar :isDark="isDark" @search="searchRequest = $event"/>
      </section>
      <section class="filter-section">
        <Filter :isDark="isDark" @update-filter="handleFilters" @sort-games="handleSort"/>
        <button
            class="toggle"
            :style="[
                { backgroundColor: isDark ? darkColor : lightColor },
                { color: isDark ? lightColor : darkColor }
              ]"
            @click="toggleFavoritesView"
        >
          {{ favoritesOnly ? `Show All Games ${favoritesCount}` : `Show Favorite Games ${favoritesCount}` }}
        </button>
      </section>
    </div>

    <!--  main platforms section can be used to filter the displayed games -->
    <section class="platforms">
      <div
        v-for="platform in platforms"
        :key="platform.genre"
        class="platform"
        :class="{'active-platform': activeFilters.genre === platform.genre}"
        @click="setGenreFilter(platform.genre)"
      >
        <h3>{{ platform.label }}</h3>
      </div>
    </section>

    <!-- placeholder for Game Links -->
    <section id="games-list" class="games-placeholder">
      <!-- show only favorites games' section-->
      <section class="favorites-toggle-section">
<!--        <button
                    class="toggle"
                    :style="[
              { backgroundColor: isDark ? darkColor : lightColor },
              { color: isDark ? lightColor : darkColor }
            ]"
            @click="toggleFavoritesView"
        >

          {{ favoritesOnly ? `Show All Games ${favoritesCount}` : `Show Favorite Games ${favoritesCount}` }}

        </button>-->
        <section v-if="favoriteGames.length > 0" class="favorites-row-section">
          <h2 class="favorites-row-title">Favorite Games {{ favoritesCount }}</h2>

          <div class="favorites-scroll-row">
            <GameLink
                v-for="game in favoriteGames"
                :key="game.id"
                :game="game"
                :isDark="isDark"
                :textColor="lightColor"
                :compact="true"
            />
          </div>
        </section>

        <p v-else-if="favoritesOnly" class="empty-favorites-message">
          No favorite games saved yet.
        </p>
      </section>
      <div class="game-grid">
        <!--===================task 83==================-->
        <!--use computed sortGames-->
        <!--<GameLink v-for="game in sortGames" :key="game.id" :game="game" :isDark="isDark" class="platform"-->
        <GameLink v-for="game in displayGames" :key="game.id" :game="game" :isDark="isDark"
                  :textColor="lightColor"/>
      </div>
    </section>
  </div>
</template>

<style scoped>
/* main container styling */
.main {
  min-height: 100vh;
  position: relative;
  font-family: Arial, Helvetica, sans-serif;
  transition: background-color 0.3s ease, color 0.3s ease;
  padding-bottom: 20px;
  overflow-x: hidden;
}

.browse-bar,
.platforms,
.games-placeholder,
.games-heading {
  position: relative;
  z-index: 10;
}

/* theme controls panel */
.theme-controls {
  flex: 0 0 auto;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 16px;
  padding: 0;
}

/* color pickers */
.pickers {
  display: flex;
  flex-direction: row;
  gap: 12px;
}

.pickers label {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  font-size: 0.95rem;
}

.picker,
input[type='color'] {
  width: 20px;
  height: 20px;
  padding: 0;
  border: 1px solid currentColor;
  border-radius: 4px;
  background: transparent;
  cursor: pointer;
}

/* toggle button styling */
.toggle {
  padding: 10px 16px;
  border: 1px solid currentColor;
  background: transparent;
  color: inherit;
  cursor: pointer;
  border-radius: 4px;
}

.toggle:hover {
  background: #111111;
  color: #ffffff;
  border-color: #111111;
}

.quiz-container {
  display: contents;
}

.quiz-button {
  padding: 12px 22px;
  border: 1px solid currentColor;
  border-radius: 0;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
}

.dark-mode .quiz-button {
  background: #ffffff;
  color: #111111;
  border-color: #ffffff;
}

.light-mode .quiz-button {
  background: #111111;
  color: #ffffff;
  border-color: #111111;
}

.quiz-button:hover {
  opacity: 0.88;
}

.games-heading {
  position: relative;
  z-index: 10;
  margin: 8px 48px 12px;
  font-size: 1.5rem;
}

.browse-bar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  margin: 0 48px 16px;
}

.search {
  padding: 0;
  flex: 1 1 220px;
  min-width: 200px;
}

.filter-section {
  z-index: 20;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
}

/* platform cards styling */
.platforms {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  padding: 0 48px;
  margin-bottom: 16px;
  border-bottom: 1px solid #444444;
}

.platform {
  padding: 8px 14px;
  text-align: center;
  border: none;
  border-bottom: 2px solid transparent;
  background: transparent;
  cursor: pointer;
  margin-bottom: -1px;
}

.platform h3 {
  margin: 0;
  font-size: 0.95rem;
  font-weight: 600;
}

.platform:hover {
  background: rgba(128, 128, 128, 0.2);
}

.active-platform {
  background: transparent;
  border-bottom-color: currentColor;
}

.favorites-toggle-section {
  display: flex;
  justify-content: center; /* centers horizontally */
  align-items: center;
  margin: 12px 0;
}

/* game section placeholder styling */
.games-placeholder {
  margin: 0 48px 24px;
  padding: 0;
  text-align: center;
}

.games-placeholder h2 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 1.5rem;
}

.empty-favorites-message {
  margin: 0 auto 20px;
  opacity: 0.85;
}

/* grid styling for game links */
.game-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4 columns equal width */
  gap: 20px;
  padding-top: 10px;
}

.favorites-row-section {
  width: 100%;
  margin-bottom: 24px;
}

.favorites-row-title {
  text-align: center;
  margin-bottom: 12px;
}

.favorites-scroll-row {
  display: flex;
  flex-wrap: nowrap;
  gap: 16px;
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  padding: 8px 4px 12px;
}

.favorites-scroll-row > * {
  flex: 0 0 auto;
}

.star-div {
  position: absolute;
  top: 0;
  left: 0;
  width: 200%;
  height: 200%;
  pointer-events: none;
  z-index: 0;
}

/* Create stars and make them move across the screen */
.stars-small {
  background-image:
    radial-gradient(2px 2px at 20px 30px, #fff, transparent),
    radial-gradient(2px 2px at 50px 70px, #fff, transparent),
    radial-gradient(2px 2px at 150px 50px, #fff, transparent),
    radial-gradient(2px 2px at 300px 250px, #fff, transparent),
    radial-gradient(2px 2px at 410px 310px, #fff, transparent),
    radial-gradient(2px 2px at 500px 100px, #fff, transparent),
    radial-gradient(2px 2px at 700px 400px, #fff, transparent),
    radial-gradient(2px 2px at 750px 500px, #fff, transparent);
  background-size: 800px 800px;
  animation: moveStars 100s linear infinite;
  opacity: 0.45;
}

.stars-large {
  background-image:
    radial-gradient(5px 5px at 100px 150px, #fff, transparent),
    radial-gradient(5px 5px at 250px 170px, #fff, transparent),
    radial-gradient(5px 5px at 400px 350px, #fff, transparent),
    radial-gradient(5px 5px at 510px 440px, #fff, transparent),
    radial-gradient(5px 5px at 600px 100px, #fff, transparent);
  background-size: 1000px 1000px;
  animation: moveStars 60s linear infinite;
  opacity: 0.5;
}

.stars-inverted {
  filter: invert(1);
}

@keyframes moveStars {
  from { transform: translate(0, 0); }
  to { transform: translate(-400px, -400px); }
}

/* Responsive design */
@media (max-width: 1100px) {
  .game-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .browse-bar,
  .games-placeholder,
  .games-heading {
    margin-left: 20px;
    margin-right: 20px;
  }

  .platforms {
    padding: 0 14px;
  }

  .game-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }
}
</style>