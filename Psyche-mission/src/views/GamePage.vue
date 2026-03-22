<script>
import gameData from "../assets/games.json"

export default {
  name: 'GamePage',
  props: ['id'], // ID of game passed to this component
  data() {
    return {
      gameIsDark: false, // track whether dark mode is active
      backGround: "#ffffff", // whole page background color
      foreGround: "#000000", // whole page text color
      game: null,
      src: null,
    }
  },
  mounted() {
    // restore saved mode
    const savedMode = localStorage.getItem("gameSavedMode");
    if (savedMode === "Dark") {
      this.gameIsDark = true;
    }

    // restore saved colors
    const savedBackGround = localStorage.getItem("gameSavedBackGround");
    const savedForeGround = localStorage.getItem("gameSavedForeGround");

    if (savedBackGround) {
      this.backGround = savedBackGround;
    } else {
      this.backGround = this.gameIsDark ? "#000000" : "#ffffff";
    }

    if (savedForeGround) {
      this.foreGround = savedForeGround;
    } else {
      this.foreGround = this.gameIsDark ? "#ffffff" : "#000000";
    }
  },
  methods: {
    // toggle between black/white dark-light defaults
    toggleMode() {
      this.gameIsDark = !this.gameIsDark;

      if (this.gameIsDark) {
        this.backGround = "#000000";
        this.foreGround = "#ffffff";
        localStorage.setItem("gameSavedMode", "Dark");
      } else {
        this.backGround = "#ffffff";
        this.foreGround = "#000000";
        localStorage.setItem("gameSavedMode", "Light");
      }

      localStorage.setItem("gameSavedBackGround", this.backGround);
      localStorage.setItem("gameSavedForeGround", this.foreGround);
    },

    // save selected background color
    updateBackGround() {
      localStorage.setItem("gameSavedBackGround", this.backGround);
    },

    // save selected foreground/text color
    updateForeGround() {
      localStorage.setItem("gameSavedForeGround", this.foreGround);
    },

    // find game data by id
    findGame() {
      const gameFound = gameData.games.find(game => game.id == this.id);

      if (!gameFound) {
        this.$router.replace({ name: "NotFound" });
      } else {
        this.game = gameFound;
      }
    },

    // toggle fullscreen for game iframe
    toggleFullscreen() {
      const gameContainer = this.$refs.gameContainer;

      if (!document.fullscreenElement) {
        if (gameContainer.requestFullscreen) {
          gameContainer.requestFullscreen();
        } else if (gameContainer.webkitRequestFullscreen) { // Safari
          gameContainer.webkitRequestFullscreen();
        }
      } else {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        }
      }
    },
  },
  created() {
    this.findGame();
  },
  watch: {
    id() {
      this.findGame();
    }
  }
}
</script>

<template>
  <div
      class="main-container"
      :style="[
      { backgroundColor: backGround },
      { color: foreGround }
    ]"
  >
    <div class="top">
      <div class="theme-controls">
        <button
            class="toggle"
            :style="{ color: foreGround, borderColor: foreGround }"
            @click="toggleMode"
        >
          {{ gameIsDark ? "Switch to Light" : "Switch to Dark" }}
        </button>


        <div class="pickers">
          <label>
            Background
            <input
                type="color"
                v-model="backGround"
                @input="updateBackGround"
                class="picker"
                :style="[{backgroundColor: gameIsDark? backGround : foreGround},{color: gameIsDark? foreGround: backGround}]"
            />
          </label>
          <label>
            Text
            <input
                type="color"
                v-model="foreGround"
                @input="updateForeGround"
                class="picker"
                :style="[{backgroundColor: gameIsDark? backGround : foreGround},{color: gameIsDark? backGround: foreGround }]"
            />
          </label>
        </div>

      </div>

      <h1>{{ game ? game.title : "Loading..." }}</h1>
    </div>

    <nav class="navbar">
      <h1>navbar component goes here</h1>
    </nav>

    <div class="game">
      <div v-if="game && game.src" class="game-iframe">
          <a :href="game.src" v-if="game.genre === 'VR Experience'"><h1>{{ game.src }}</h1></a>
          <iframe v-else
            ref="gameContainer"
            :src="game.src"
            frameborder="0"
            allowfullscreen
          ></iframe>

          <button class="fullscreen-btn" @click="toggleFullscreen">
            ⛶ Fullscreen
          </button>
      </div>
      <div v-else>
        <h1>Unable to Load</h1>
      </div>
    </div>

    <div class="details">
      <div class="description">
        <h3>Game Description</h3>
        <p>{{ game ? game.description : "--"}} </p>
      </div>

      <div class="vertical-divider"></div>

      <!-- id: psyche-scraper
      <!-- title: Psyche Scraper
           				"thumbnail": "None",
           				"difficulty": "Easy",
           				"genre": "VR Experience",
           				"age": "Elementary",
           				"class": "Iridium - 2025",
           				"gtype": "iridium",
           				"video": "None",
           				"credits": "Kevin Nangcas, Shannon Ray, Erik Sousa, Chad Valente",
           				"src": "https://github.com/MissionToPsyche-Iridium/iridium_20b_vr_game-se",
           				"description":"Psyche Scraper is a Virtual Reality space game that is based off of an existing 2D web game, “Return to Psyche.” These games are inspired by NASA’s Psyche mission, which aims to study the metal-rich asteroid, Psyche 16. " -->

      <div class="metadata">
        <h3>Details</h3>
        <p><strong>Difficulty</strong>: {{game.difficulty}}</p>
        <p><strong>Genre</strong>: {{game.genre}}</p>
        <p><strong>Age</strong>: {{game.age}}</p>
        <p><strong>Class</strong>: {{game.class}}</p>
        <p><strong>Credits</strong>: {{game.credits}}</p>
      </div>
    </div>

    <div class="suggested-games">
      <div
          v-for="i in 3"
          :key="i"
          class="suggestion"
      >
        Suggested Game components go here
      </div>
    </div>

    <footer class="footer">
      <p>footer component goes here</p>
    </footer>
  </div>
</template>

<style scoped>
/* main page container */
.main-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  font-family: Arial, sans-serif;
  transition: background-color 0.3s ease, color 0.3s ease;
  padding-bottom: 20px;
}

/* top section styled like homepage */
.top {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 34px 20px 24px;
  margin: 20px;
  position: relative;
  text-align: center;
  border: 1px solid rgba(128, 128, 128, 0.25);
  border-radius: 24px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.top h1 {
  margin: 0;
  max-width: 900px;
  font-size: 2.2rem;
  line-height: 1.3;
  padding: 0 120px;
}

/* homepage-style theme controls panel */
.theme-controls {
  position: absolute;
  top: 18px;
  right: 18px;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 10px;
  margin: 0;
  padding: 14px;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 16px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(4px);
}

/* toggle button */
.toggle {
  padding: 10px 16px;
  border: 1px solid currentColor;
  background: transparent;
  cursor: pointer;
  border-radius: 50px;
  font-weight: 600;
  transition: transform 0.2s ease, opacity 0.2s ease, box-shadow 0.2s ease;
}

.toggle:hover {
  transform: translateY(-10px);
  opacity: 0.95;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.12);
}


/* color pickers layout */
.pickers {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 0;
}

label {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  font-size: 0.95rem;
}

.picker {
  padding: 1px ;
  border: 1px solid currentColor;
  background: transparent;
  cursor: pointer;

  font-weight: 100;
}

input[type="color"] {
  width: 20px;
  height: 20px;
  border: none;
  background: transparent;
  cursor: pointer;
  padding: 0;
  border-radius: 100px;
}

/* navbar */
.navbar {
  margin: 20px;
  padding: 18px 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  border: 1px solid rgba(128, 128, 128, 0.25);
  border-radius: 20px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}

.navbar h1 {
  margin: 0;
  font-size: 1.4rem;
  letter-spacing: 0.3px;
}

/* hero/game display area */
.game {
  margin: 0 20px 20px;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 24px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.game h1 {
  margin: 0;
  line-height: 1.2;
  text-align: center;
  padding: 40px;
}

.game-iframe {
  position: relative;
  width: 100%;
  aspect-ratio: 16 / 9;
}

.game-iframe iframe {
  width: 100%;
  height: 100%;
}

.fullscreen-btn {
  position: absolute;
  bottom: 15px;
  right: 15px;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.8rem;
  transition: background 0.2s;
  z-index: 10;
}

.fullscreen-btn:hover {
  background: rgba(0, 0, 0, 0.8);
}

:fullscreen .fullscreen-btn{
  opacity: 0.5;
}

/* details section */
.details {
  display: flex;
  gap: 24px;
  margin: 0 20px 20px;
  padding: 28px 24px;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 20px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}

.description,
.metadata {
  flex: 1;
}

.description h3,
.metadata h3 {
  margin-top: 0;
  margin-bottom: 12px;
  font-size: 1.2rem;
}

.description p,
.metadata p {
  margin: 0;
  opacity: 0.85;
  line-height: 1.6;
}

.vertical-divider {
  width: 1px;
  background-color: rgba(128, 128, 128, 0.35);
  border-radius: 2px;
}

/* suggested cards */
.suggested-games {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin: 0 20px 20px;
}

.suggestion {
  min-height: 220px;
  padding: 24px 18px;
  border-radius: 18px;
  border: 1px solid rgba(128, 128, 128, 0.22);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  background: transparent;
}

.suggestion:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 22px rgba(0, 0, 0, 0.14);
}

/* footer */
.footer {
  margin: 0 20px 20px;
  margin-top: auto;
  padding: 18px;
  text-align: center;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 18px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}

.footer p {
  margin: 0;
}

/* responsive design */
@media (max-width: 1100px) {
  .suggested-games {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 800px) {
  .details {
    flex-direction: column;
  }

  .vertical-divider {
    width: 100%;
    height: 1px;
  }

  .placeholder-content h1 {
    font-size: 1.7rem;
  }

  .top {
    padding-top: 110px;
  }

  .top h1 {
    padding: 0 20px;
    font-size: 1.9rem;
  }

  .theme-controls {
    top: 16px;
    right: 16px;
  }
}

@media (max-width: 600px) {
  .suggested-games {
    grid-template-columns: 1fr;
  }

  .navbar,
  .game,
  .details,
  .suggested-games,
  .footer,
  .top {
    margin-left: 14px;
    margin-right: 14px;
  }

  .top {
    flex-direction: column;
    padding: 22px 16px;
  }

  .top h1 {
    font-size: 1.55rem;
    padding: 0;
  }

  .toggle {
    align-self: stretch;
  }

  .theme-controls {
    position: static;
    width: 100%;
    max-width: 320px;
    margin-bottom: 16px;
  }

  .navbar h1 {
    font-size: 1.15rem;
  }

  .placeholder-content h1 {
    font-size: 1.45rem;
  }

  .details {
    padding: 22px 16px;
  }
}
</style>