<script>
import gameData from '../assets/games.json'
import GameLink from '../components/GameLink.vue'
import Filter from '../components/Filter.vue'
import SearchBar from '../components/SearchBar.vue'
import QuizModal from '../components/QuizModal.vue'

export default {
  name: "HomePage",
  components: {
    GameLink,
    Filter,
    SearchBar,
    QuizModal
  },
  data() {
    return {
      isDark: false,// variable to track if dark mode is active
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

    displayGames() {
        return this.showQuizResults ? this.quizResults :this.sortGames;
    }
  },
  mounted() {
    // get the previously saved mode from the browser local storage
    const savedMode = localStorage.getItem("savedMode");
    //check if previous mode is a dark mode, enable dark mode on page load
    if (savedMode === "Dark") {
      this.isDark = true;// will update automatically
    }
    /*check for saved colors in browser*/
    const savedDarkColor = localStorage.getItem("savedDarkColor");
    const savedLightColor = localStorage.getItem("savedLightColor");
    if (savedDarkColor) {
      this.darkColor = savedDarkColor;// retrieve the dark color prefrence from the browser
    }
    if (savedLightColor) {
      this.lightColor = savedLightColor;// retrieve the light color prefrence from the browser
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
  <div class="main" :style="[{backgroundColor: darkColor},{ color: lightColor}]">
    <!--root container-->

    <!-- Top section containing the toggle button and title -->
    <div class="top">
      <h1>Welcome to the Psyche mission's web-based game portal!</h1>
      <div class="spacer"></div>

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

    </div>

    <!-- page header -->
    <!-- <header class="header">
      <h1>Psyche Mission Game Portal AR, VR, and XR</h1>
    </header> -->

    <!-- place holder for an introduction -->
    <!-- <section class="intro">
      <h2>Welcome to the Psyche Mission's Web-Based Portal By Team 1 platinum_21a_portal-se</h2>
      <p>Discover the latest web-based experiences from the Psyche Mission team.</p>
    </section> -->

    <section class="quiz">
      <div id="quiz-container" class="quiz-container">
        <label id="quiz-label" class="quiz-label">
          Take a quiz to find your favorite game!
        </label>
        <button id="quiz-button" class="quiz-button" @click="openQuiz">
          Take Quiz
        </button>
      </div>
    </section>

    <QuizModal v-if="isQuizOpen" @close="closeQuiz" @quiz-complete="handleQuizResults"/>

    <section class="search">
      <SearchBar :isDark="isDark" @search="searchRequest = $event"/>
    </section>

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
        <p>{{ platform.desc }}</p>
      </div>

    </section>

    <section class="filter">
      <Filter :isDark="isDark" @update-filter="handleFilters" @sort-games="handleSort"/>
    </section>
    <!-- show only favorites games' section-->
    <section class="favorites-toggle-section">
      <button
          class="toggle"
          :style="[
      { backgroundColor: isDark ? darkColor : lightColor },
      { color: isDark ? lightColor : darkColor }
    ]"
          @click="toggleFavoritesView"
      >
        {{ favoritesOnly ? "Show All Games" : "View Saved Games" }}
      </button>
    </section>

    <!-- placeholder for Game Links -->
    <section class="games-placeholder">
      <!-- dynamic title -->
      <h2>{{ favoritesOnly ? "Favorite Games" : "Available Experiences" }}</h2>
      <div class="game-grid">
        <!--===================task 83==================-->
        <!--use computed sortGames-->
        <!--<GameLink v-for="game in sortGames" :key="game.id" :game="game" :isDark="isDark" class="platform"-->
        <GameLink v-for="game in displayGames" :key="game.id" :game="game" :isDark="isDark" class="platform"
                  :textColor="lightColor"/>
      </div>
    </section>
  </div>
</template>

<style>
/* main container styling */
.main {
  min-height: 100vh;
  font-family: Arial, sans-serif;
  position: relative;
  transition: background-color 0.3s ease, color 0.3s ease;
  padding-bottom: 20px;
}

/* header styling */
.header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 18px 20px;
  margin: 20px;
  border: 1px solid rgba(128, 128, 128, 0.25);
  border-radius: 20px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  position: relative;
  text-align: center;
}


/* top section styling (toggle button/color pickers) */
.top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 34px 20px 24px;
  margin: 20px;
  border: 1px solid rgba(128, 128, 128, 0.25);
  border-radius: 24px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  gap: 20px;
}

.top h1 {
  flex: 2;
  text-align: center;
  margin: 0;
  font-size: 1.6rem;
}

.spacer {
  flex: 0 1 auto;
  width: 100px;
  visibility: hidden;
}

.theme-controls {
  flex: 0 1 auto;
  width: fit-content;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  gap: 10px;
  margin: 0;
  padding: 14px;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 16px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(4px);
}

/* color pickers */
.pickers {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 0;


}

.picker {
  padding: 1px;
  border: 1px solid currentColor;
  background: transparent;
  cursor: pointer;

  font-weight: 100;

}

/* toggle button styling */
.toggle {
  padding: 10px 15px;
  border: 1px solid currentColor;
  background: transparent;
  cursor: pointer;
  border-radius: 999px;
  font-weight: 600;
  transition: transform 0.2s ease, opacity 0.2s ease, box-shadow 0.2s ease;
}

.toggle:hover {
  transform: translateY(-10px);

  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.12);
}

.favorites-toggle-section {
  display: flex;
  justify-content: center; /* centers horizontally */
  align-items: center;
  margin: 20px 0;
}

/* intro section styling */
.intro {
  margin: 20px;
  padding: 28px 22px;
  text-align: center;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 20px;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.12);
}

.search {
  padding-bottom: 10px;
}

/* platform cards styling */
.platforms {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  padding: 0 20px;
  margin-bottom: 20px;
}

.platform {
  border: 1px solid;
  padding: 10px;
  text-align: center;
  border-radius: 10px;
  background: linear-gradient(145deg, rgba(255, 255, 255, 0.1), rgba(0, 0, 0, 0.2));
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
}

.active-platform {
  border: 3px solid currentColor;
  background: rgba(128, 128, 128, 0.15);
  box-shadow: 0 0 20px currentColor;
  transform: scale(1.05);
}

.platform:active {
  transform: scale(0.95) translateY(0);
}

/* game section placeholder styling */
.games-placeholder {
  margin: 20px;
  padding: 30px 24px;
  text-align: center;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 20px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}

.games-placeholder h2 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 1.5rem;
}

.games-placeholder p {
  margin: 0 auto 24px;
  max-width: 750px;
  opacity: 0.85;
  line-height: 1.6;
}

/* grid styling for game links */
.game-grid {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr; /* 4 columns equal width */
  gap: 24px;
  padding: 10px 0 0;

}

/* footer styling */
.footer {
  margin: 20px;
  text-align: center;
  padding: 18px;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 18px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  font-size: 0.95rem;
}

/* theme controls panel */

label {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  font-size: 0.95rem;
}

input[type="color"] {
  width: 20px;
  height: 20px;
  border: none;
  background: transparent;
  cursor: pointer;
  padding: 0;
  border-radius: 50px;

}

/* Responsive design */
@media (max-width: 1100px) {
  .platforms,
  .game-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 800px) {
  .top {
    padding-top: 110px;
  }


}

@media (max-width: 600px) {
  .platforms,
  .game-grid {
    grid-template-columns: 1fr;
  }

  .top,
  .header,
  .intro,
  .games-placeholder,
  .footer {
    margin: 14px;
  }

  .top {
    flex-direction: column;
    padding: 22px 16px;
  }

  .top h1 {
    font-size: 1.55rem;
    padding: 0;
  }

  .header h1 {
    font-size: 1.2rem;
  }

  .theme-controls {
    position: static;
    width: 100%;
    max-width: 320px;
    margin-bottom: 16px;
  }
}

.quiz-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 12px;
  padding: 20px;
  border: 1px solid rgba(128);
  border-radius: 10px;
}

.quiz-label {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  text-align: center;
}
</style>

