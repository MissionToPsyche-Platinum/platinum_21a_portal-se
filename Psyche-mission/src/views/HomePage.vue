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
      sortBy:"" /*===task 83===*/,
      searchRequest: "",
      isQuizOpen: false
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

          return classMatch && genreMatch && ageMatch && difficultyMatch && searchMatch;
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
        const order = { Easy: 1, Medium: 2, Hard: 3 };
        sorted.sort((a, b) => order[a.difficulty] - order[b.difficulty]);

        //Sort by difficulty (hard to easy)
      } else if (this.sortBy === "difficulty-hard") {
        const order = { Easy: 1, Medium: 2, Hard: 3 };
        sorted.sort((a, b) => order[b.difficulty] - order[a.difficulty]);
      }

      return sorted;
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
    }
  }
};
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

     <QuizModal v-if="isQuizOpen" @close="closeQuiz" />

    <section class="search">
      <SearchBar :isDark="isDark" @search="searchRequest = $event"/>
    </section>

    <section class="filter">
      <Filter :isDark="isDark" @update-filter="handleFilters" @sort-games="handleSort" />
    </section>

    <!--  main platforms section can be used to filter the displayed games -->
    <section class="platforms">
      <!--card for web baseed games-->
      <div class="platform" @click="setGenreFilter('Web Based')">
        <h3>Web Games</h3>
        <p>Browser-based games.</p>
      </div>
      <!--card for AR games-->
      <div class="platform" @click="setGenreFilter('AR Experience')">
        <h3>AR Experiences</h3>
        <p>Augmented reality.</p>
      </div>
      <!--card for VR games-->
      <div class="platform" @click="setGenreFilter('VR Experience')">
        <h3>VR Experiences</h3>
        <p>Virtual reality.</p>
      </div>

      <!--card for All games-->
      <div class="platform" @click="setGenreFilter('')">
        <h3>All Experiences</h3>
        <p>Show Everything.</p>
      </div>

    </section>

    <!-- placeholder for Game Links -->
    <section class="games-placeholder">
      <h2>Available Experiences</h2>
      <div class="game-grid">
        <!--===================task 83==================-->
        <!--use computed sortGames-->
        <GameLink v-for="game in sortGames" :key="game.id" :game="game" :isDark="isDark" class="platform" :textColor="lightColor"/>
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

/* intro section styling */
.intro {
  margin: 20px;
  padding: 28px 22px;
  text-align: center;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 20px;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.12);
}


/* platforms section styling */
.platforms {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr; /* 4 columns equal width */
  gap: 20px;
  padding: 0 20px;
  margin-bottom: 20px;

}

/* platform cards styling */
.platform {
  border: 1px solid rgba(128, 128, 128, 0.22);
  padding: 24px 18px;
  text-align: center;
  border-radius: 18px;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.12);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
}

.platform:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 22px rgba(0, 0, 0, 0.14);
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