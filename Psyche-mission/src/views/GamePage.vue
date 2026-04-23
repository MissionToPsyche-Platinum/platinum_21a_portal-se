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
      gameIsDark: false, // track whether dark mode is active
      backGround: "#ffffff", // whole page background color
      foreGround: "#000000", // whole page text color
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
      :style="[
      { backgroundColor: backGround },
      { color: foreGround }
    ]"
  >
    <div class="top">
      <h1>{{ game ? game.title : "Loading..." }}</h1>
      <div class="spacer"></div>
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

    </div>

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
          <p>{{ isFavorite ? "Saved" : "" }} </p>

          <button
              class="favorite-btn"
              :style="{ color: foreGround }"
              :class="{ saved: isFavorite }"
              @click.prevent.stop="toggleFavorite"
              :aria-label="isFavorite ? 'Remove from favorites' : 'Save to favorites'"
              :title="isFavorite ? 'Unsave game' : 'Save game'"
          >
            {{ isFavorite ? "\u2665" : "\u2661" }} <!--heart icon changes based on the state-->


          </button>
        </div>


      </div>
    </div>

    <h2 class="more-games-title">More games you might like:</h2>
    <div class="suggested-games">
      <div v-for="sGame in suggestedGames" :key="sGame.id" class="suggestion">
        <GameLink
            :game="sGame"
            :isDark="gameIsDark"
            :textColor="foreGround"
        />
      </div>
    </div>
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

.qr-code-div {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.load-game-btn {
  border-radius: 30px;
  font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: darkcyan;
  color: white
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

/* favorite button styling */

.favorite-container {
  display: flex;
  flex-direction: row;
  justify-content: right;
  align-items: center;
  margin-top: 12px;
}

.favorite-btn {
  border: none;
  background: transparent;
  font-size: 24px;
  cursor: pointer;
  transition: transform 0.2s ease, opacity 0.2s ease;
  opacity: 1;
}

.favorite-btn:hover {
  transform: scale(1.1);
  opacity: 0.85;
}

.favorite-btn.saved {
  opacity: 1;
}

.more-games-title {
  text-align: center;
  padding-top: 20px;
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
  .navbar,
  .game,
  .details,
  .footer {
    margin-left: 14px;
    margin-right: 14px;
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

  .top {
    position: relative;
    padding: 15px;
    min-height: 80px;
  }

  .top h1 {
    font-size: 1.05rem;
    text-align: left;
    margin: 0;
    width: auto;
    padding-right: 125px;
    line-height: 1.3;
    display: block;
  }

  .theme-controls {
    position: absolute;
    top: 10px;
    right: 10px;
    margin: 0;
    transform: scale(0.65);
    transform-origin: top right;
  }

  .pickers {
    /* flex-direction: row; */
    gap: 10px;
  }

  .pickers label {
    font-size: 0.75rem;
  }

  .toggle {
    padding: 6px 12px;
    font-size: 0.8rem;
  }

  .suggested-games {
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 8px;
    margin: 10px;
  }

  .game-iframe {
    aspect-ratio: 4 / 5;
  }

  .suggestion {
    min-height: auto;
    padding: 5px;
    border-radius: 8px;
    overflow: hidden;
  }

  .suggestion :deep(h2),
  .suggestion :deep(h3) {
    font-size: 0.65rem !important;
    margin: 4px 0;
  }
}
</style>