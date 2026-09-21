<script>
import gameData from "../assets/games.json"
import GameLink from '../components/GameLink.vue'
import { getSuggestions } from "../utils/gameService.js"
import QRCode from 'qrcode'
import GameRating from "../components/GameRating.vue";

export default {
  name: 'GamePage',
  components: { 
    GameRating,
    GameLink,
  },
  props: ['id'], // ID of game passed to this component
  data() {
    return {
      gameIsDark: true, // track whether dark mode is active
      backGround: "#000000", // whole page background color
      foreGround: "#ffffff", // whole page text color
      game: null,
      qrCode: null,
      displayQr: true,
      suggestedGames: null,
      isFavorite: false, // true if the game is saved in favorite
    }
  },
  mounted() {
    // restore saved mode
    const savedMode = localStorage.getItem("gameSavedMode");
    if (savedMode === "Light") {
      this.gameIsDark = false;
    } else {
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
    this.loadFavoriteState();
    window.addEventListener("favorites-updated", this.loadFavoriteState);

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
        this.suggestedGames = getSuggestions(this.game.id);
        this.generateQR();
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

    // generate a qr code for the game
    async generateQR() {
      if (this.isMobileDevice()) {
        this.displayQr = false;
        return;
      }

      try {
        this.qrCode = await QRCode.toDataURL(this.game.src, {
          width: 200,
          margin: 2,
          color: {
            dark: '#000000',
            light: '#ffffff',
          },
          errorCorrectionLevel: 'H'
        });
      } catch (err) {
        console.error(err);
      }

    },
    
    toggleGame() {
      this.displayQr = false;
    },

    isMobileDevice() {
      // Use Regex to test the userAgent string to see if the user is using a mobile device
      return (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) 
      || (/Macintosh/i.test(navigator.userAgent) && navigator.maxTouchPoints && navigator.maxTouchPoints > 1); // Additional check for modern Ipads with Macintosh in userAgent string
    },

    // read saved favorite list from localStorage
    getFavoriteGames() {
      //if favorite list exists, convert it to an array, otherwise return null
      const savedFavorites = localStorage.getItem("favoriteGames");
      return savedFavorites ? JSON.parse(savedFavorites) : [];
    },
    // check if the current game is saved
    loadFavoriteState() {
      const favorites = this.getFavoriteGames();
      this.isFavorite = favorites.includes(this.game.id);
    },
// switches between favorite/not favorite, if the current game is saved remove it
// if not, save it
    toggleFavorite() {
      let favorites = this.getFavoriteGames();

      if (favorites.includes(this.game.id)) {
        favorites = favorites.filter(id => id !== this.game.id); // remove from favorites
        this.isFavorite = false;
      } else {
        favorites.push(this.game.id);  // add to favorites
        this.isFavorite = true;
      }

      localStorage.setItem("favoriteGames", JSON.stringify(favorites));

      // notify other components that favorites changed
      window.dispatchEvent(new Event("favorites-updated"));
    },


  },

  created() {
    this.findGame();
  },
  watch: {
    id() {
      this.displayQr = true;
      this.findGame();
    }
  }
}
</script>

<template>
  <div
      class="main-container"
      :class="{ 'dark-mode': gameIsDark, 'light-mode': !gameIsDark }"
      :style="[
      { backgroundColor: backGround },
      { color: foreGround }
    ]"
  >
    <div class="star-div stars-small" :class="{ 'stars-inverted': !gameIsDark }"></div>
    <div class="star-div stars-large" :class="{ 'stars-inverted': !gameIsDark }"></div>

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

    <header class="game-header">
      <p class="hero-kicker">Psyche Mission Game Portal</p>
      <h1>{{ game ? game.title : "Loading..." }}</h1>
      <p v-if="game" class="hero-meta">
        {{ game.difficulty || "Unknown" }} · {{ game.genre }} · {{ game.age }}
      </p>
    </header>

    <div class="game">
      <div v-if="game && game.src" class="game-iframe">
          <a :href="game.src" v-if="game.genre === 'VR Experience'"><h1>{{ game.src }}</h1></a>
          <div v-else-if="game.genre === 'AR Experience' && displayQr" class="qr-code-div">
            <img :src="qrCode" />
            <h3>The AR Experiences are optimized for use with a mobile device.</h3>
            <h3>Please Scan the QR code with your mobile device to play!</h3>
            <h3>Or:</h3>
            <button class="load-game-btn" @click="toggleGame">Load Game In The Browser</button>
          </div>
          <div v-else class="iframe-div">
            <iframe
              ref="gameContainer"
              :src="game.src"
              frameborder="0"
              allowfullscreen
            ></iframe>

            <button v-if="game.engine != 'unity'" class="fullscreen-btn" @click="toggleFullscreen">
              ⛶ Fullscreen
            </button>
          </div>
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

      <div class="metadata">
        <h3>Details</h3>
        <p><strong>Difficulty</strong>: {{game.difficulty}}</p>
        <p><strong>Genre</strong>: {{game.genre}}</p>
        <p><strong>Age</strong>: {{game.age}}</p>
        <p><strong>Class</strong>: {{game.class}}</p>
        <p><strong>Credits</strong>: {{game.credits}}</p>

<!--game rating component goes here-->
        <GameRating
            v-if="game"
            :gameId="game.id"
            :isDark="gameIsDark"
            :foreGround="foreGround"
            :backGround="backGround"
        />

        <!--favorite button that change color dynamically and prevent navigation when clicked.-->
        <div class="favorite-container">
          <button
              class="favorite-btn"
              :class="{ saved: isFavorite }"
              @click.prevent.stop="toggleFavorite"
              :aria-label="isFavorite ? 'Remove from favorites' : 'Save to favorites'"
              :title="isFavorite ? 'Remove from favorites' : 'Save to favorites'"
          >
            <span class="favorite-icon">{{ isFavorite ? "\u2665" : "\u2661" }}</span>
            <span class="favorite-label">{{ isFavorite ? "Saved to favorites" : "Add to favorites" }}</span>
          </button>
        </div>


      </div>
    </div>

    <h2 class="more-games-title">More games you might like</h2>
    <div class="suggested-games">
      <GameLink
          v-for="sGame in suggestedGames"
          :key="sGame.id"
          :game="sGame"
          :isDark="gameIsDark"
          :textColor="foreGround"
      />
    </div>
  </div>
</template>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  position: relative;
  font-family: Arial, Helvetica, sans-serif;
  transition: background-color 0.3s ease, color 0.3s ease;
  padding-bottom: 40px;
  overflow-x: hidden;
}

.main-container.dark-mode {
  color-scheme: dark;
}

.main-container.light-mode {
  color-scheme: light;
}

.main-container,
.game-iframe iframe {
  scrollbar-width: thin;
  scrollbar-color: color-mix(in srgb, currentColor 55%, transparent) transparent;
}

.main-container::-webkit-scrollbar,
.game-iframe iframe::-webkit-scrollbar {
  width: 10px;
  height: 8px;
}

.main-container::-webkit-scrollbar-track,
.game-iframe iframe::-webkit-scrollbar-track {
  background: transparent;
}

.main-container::-webkit-scrollbar-thumb,
.game-iframe iframe::-webkit-scrollbar-thumb {
  background: color-mix(in srgb, currentColor 45%, transparent);
  border-radius: 999px;
}

.main-container::-webkit-scrollbar-thumb:hover,
.game-iframe iframe::-webkit-scrollbar-thumb:hover {
  background: currentColor;
}

.main-container::-webkit-scrollbar-button,
.game-iframe iframe::-webkit-scrollbar-button {
  display: none;
  width: 0;
  height: 0;
}

.main-container::-webkit-scrollbar-corner,
.game-iframe iframe::-webkit-scrollbar-corner {
  background: transparent;
}

.theme-controls {
  position: absolute;
  top: 20px;
  right: 48px;
  z-index: 20;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 16px;
  padding: 0;
}

.toggle {
  padding: 10px 16px;
  border: 1px solid currentColor;
  background: transparent;
  color: inherit;
  cursor: pointer;
  border-radius: 4px;
  font-weight: 600;
}

.toggle:hover {
  background: #111111;
  color: #ffffff;
  border-color: #111111;
}

.pickers {
  display: flex;
  flex-direction: row;
  gap: 12px;
}

.pickers label,
label {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  font-size: 0.95rem;
}

.picker,
input[type="color"] {
  width: 20px;
  height: 20px;
  padding: 0;
  border: 1px solid currentColor;
  border-radius: 4px;
  background: transparent;
  cursor: pointer;
}

.game-header,
.game,
.details,
.more-games-title,
.suggested-games {
  position: relative;
  z-index: 10;
}

.game-header {
  padding: 88px 48px 28px;
  max-width: 1080px;
}

.hero-kicker {
  margin: 0 0 10px;
  font-size: 0.95rem;
  opacity: 0.7;
}

.game-header h1 {
  margin: 0 0 12px;
  font-size: clamp(2.1rem, 5vw, 3.5rem);
  line-height: 1.1;
  font-weight: bold;
}

.hero-meta {
  margin: 0;
  opacity: 0.85;
}

.game {
  margin: 0 48px 32px;
  border: 1px solid currentColor;
  overflow: hidden;
}

.game h1 {
  margin: 0;
  line-height: 1.2;
  text-align: center;
  padding: 40px;
  font-size: 1.1rem;
  word-break: break-all;
}

.game a {
  color: inherit;
}

.game-iframe {
  aspect-ratio: 16 / 9;
}

.iframe-div {
  position: relative;
  width: 100%;
  height: 100%;
}

.game-iframe iframe {
  width: 100%;
  height: 100%;
  border: 0;
  display: block;
}

.fullscreen-btn {
  position: absolute;
  bottom: 15px;
  right: 15px;
  padding: 8px 12px;
  background: transparent;
  color: inherit;
  border: 1px solid currentColor;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  font-weight: 600;
  z-index: 10;
}

.fullscreen-btn:hover {
  background: #111111;
  color: #ffffff;
  border-color: #111111;
}

:fullscreen .fullscreen-btn {
  opacity: 0.5;
}

.qr-code-div {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  gap: 8px;
  padding: 24px;
  text-align: center;
}

.qr-code-div h3 {
  margin: 0;
  font-weight: 500;
  opacity: 0.85;
}

.load-game-btn {
  margin-top: 8px;
  padding: 12px 22px;
  border: 1px solid currentColor;
  border-radius: 0;
  background: #ffffff;
  color: #111111;
  font: inherit;
  font-weight: 600;
  cursor: pointer;
}

.light-mode .load-game-btn {
  background: #111111;
  color: #ffffff;
}

.load-game-btn:hover {
  opacity: 0.88;
}

.details {
  display: flex;
  gap: 40px;
  margin: 0 48px 32px;
  padding: 0 0 8px;
}

.description,
.metadata {
  flex: 1;
}

.description h3,
.metadata h3 {
  margin-top: 0;
  margin-bottom: 12px;
  font-size: 1.25rem;
}

.description p,
.metadata p {
  margin: 0 0 8px;
  opacity: 0.85;
  line-height: 1.6;
}

.vertical-divider {
  width: 1px;
  background-color: #444444;
  flex-shrink: 0;
}

.suggested-games {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 20px;
  margin: 0 48px 40px;
}

.favorite-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-top: 16px;
}

.favorite-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border: 1px solid currentColor;
  background: transparent;
  color: inherit;
  cursor: pointer;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 600;
}

.favorite-icon {
  font-size: 1.15rem;
  line-height: 1;
}

.favorite-btn:hover {
  background: #111111;
  color: #ffffff;
  border-color: #111111;
}

.favorite-btn.saved {
  border-color: #e8c547;
  color: #e8c547;
}

.favorite-btn.saved:hover {
  background: rgba(232, 197, 71, 0.12);
  color: #e8c547;
  border-color: #e8c547;
}

.more-games-title {
  margin: 8px 48px 16px;
  font-size: 1.5rem;
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

  .theme-controls {
    right: 20px;
    top: 16px;
  }
}

@media (max-width: 600px) {
  .game-header,
  .game,
  .details,
  .more-games-title,
  .suggested-games {
    margin-left: 20px;
    margin-right: 20px;
  }

  .game-header {
    padding: 96px 20px 20px;
  }

  .theme-controls {
    right: 16px;
    top: 12px;
    transform: scale(0.85);
    transform-origin: top right;
  }

  .pickers label {
    font-size: 0.75rem;
  }

  .toggle {
    padding: 6px 12px;
    font-size: 0.8rem;
  }

  .suggested-games {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .game-iframe {
    aspect-ratio: 4 / 5;
  }
}
</style>
