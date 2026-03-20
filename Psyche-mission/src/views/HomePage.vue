<script>
import gameData from '../assets/games.json'
import GameLink from '../components/GameLink.vue'

export default {
  name: "HomePage",
  components: {
    GameLink,
  },
  data() {
    return {
      isDark: false,// variable to track if dark mode is active
      games: gameData.games, // creates an array of game objects from JSON file
      darkColor: "#000000",// variable for selected dark mode color with a default value
      lightColor: "#ffffff"// variable for selected light mode color with a default value
    };
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

    //this function toggle between dark mode and light mode
    toggleMode() {
      this.isDark = !this.isDark;//if dark mode is active change to light mode, if light mode is active change to dark mode
      if (this.isDark) {
        localStorage.setItem("savedMode", "Dark");// save dark mode in local storage
      } else {
        localStorage.setItem("savedMode", "Light");// save light mode in local storage
      }
    },

    // these function update the localstorage with the current colors selection
    updateDarkColor() {
      localStorage.setItem("savedDarkColor", this.darkColor);
    },

    updateLightColor() {
      localStorage.setItem("savedLightColor", this.lightColor);
    }

  }
};
</script>

<template>


  <!--dynamically adds either the "light" or "dark" class-->
  <div :class="['main', isDark ? 'dark' : 'light']" :style="[{backgroundColor: isDark? darkColor: lightColor},{color: isDark? lightColor: darkColor}]">
    <!--root container-->

    <!-- Top section containing the toggle button and title -->
    <div class="top">
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
            Dark Mode
            <input type="color" v-model="darkColor" @input="updateDarkColor"/>
          </label>
          <label>
            Light Mode
            <input type="color" v-model="lightColor" @input="updateLightColor"/>
          </label>
        </div>
      </div>
      <h1>Welcome to the Psyche mission's web-based game portal!</h1>
    </div>
    <!-- page header -->
    <header class="header">
      <h1>Psyche Mission Game Portal AR, VR, and XR</h1>

    </header>

    <!-- place holder for an introduction -->
    <section class="intro">
      <h2>Welcome to the Psyche Mission's Web-Based Portal By Team 1 platinum_21a_portal-se</h2>
    </section>

    <!--  main platforms section play as place holder for different platforms -->
    <section class="platforms">
      <!--card for web baseed games-->
      <div class="platform">
        <h3>Web Games</h3>
        <p>Browser-based games.</p>
      </div>
      <!--card for AR games-->
      <div class="platform">
        <h3>AR Experiences</h3>
        <p>Augmented reality.</p>
      </div>
      <!--card for VR games-->
      <div class="platform">
        <h3>VR Experiences</h3>
        <p>Virtual reality.</p>
      </div>

      <!--card for XR games-->
      <div class="platform">
        <h3>XR Experiences</h3>
        <p>Mixed reality.</p>
      </div>

    </section>

    <!-- placeholder for Game Links -->
    <section class="games-placeholder">
      <h2>Available Experiences</h2>
      <p>Place holder for games links and previews in upcoming sprints.</p>
      <div class="game-grid">
        <GameLink v-for="game in games" :key="game.id" :game="game" :is-dark="isDark"/>
      </div>
    </section>

    <!-- footer -->
    <footer class="footer">
      <p>Psyche Mission Portal – Capstone Project</p>
    </footer>

  </div>

</template>

<style>

/*main container styling*/
.main {
  border: 1px solid;
  font-family: Arial, sans-serif;
  position: relative;
}

/*header styling*/
.header {
  border: 1px solid;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  position: relative;
}

/*tob section styling that hold the toggle button*/
.top {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  position: relative;
}



/*toggle button styling*/
.toggle {

  top: 10px;
  right: 10px;
  padding: 8px 12px;
  border: 1px solid;
  background: transparent;
  cursor: pointer;
  border-radius: 15px;
}

/*intro section styling*/
.intro {
  border: 1px solid;
  padding: 10px;
  text-align: center;
}

/*platforms section styling*/
.platforms {
  display: grid;
  grid-template-columns:  1fr 1fr 1fr 1fr; /* 4 columns equal width*/
  gap: 20px;
  padding: 10px;
}

/*platform cards styling*/
.platform {
  border: 1px solid;
  padding: 10px;
  text-align: center;
}

/*game section place holder styling*/
.games-placeholder {
  border: 1px solid;
  padding: 40px;
  text-align: center;
}

/*grid styling for game links*/
.game-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 50px;
  padding: 20px;
}

/*footer styling*/
.footer {
  border: 1px solid;
  text-align: center;
  padding: 15px;

}

.theme-controls {

  position: absolute;
  top: 10px;
  right: 20px;
  display: flex;
  flex-direction: column;
  align-items: normal;
  margin: 5px 0px 5px 0px;
}

.pickers {
  display: flex;
  flex-direction: column;
  gap: 1px;
  margin: 5px 0px 5px 0px;

}

label {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* Responsiveness */

@media (max-width: 1100px) {
  .platforms {
    grid-template-columns: 1fr 1fr 1fr 1fr; /* 2 columns equal width*/
  }
}

@media (max-width: 800px) {
  .platforms {
    grid-template-columns: 1fr 1fr; /* 2 columns equal width*/
  }
}

@media (max-width: 600px) {
  .platforms {
    grid-template-columns: 1fr;
  }
}

</style>