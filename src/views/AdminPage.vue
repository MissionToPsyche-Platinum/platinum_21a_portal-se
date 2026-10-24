<script>
export default {
  name: "AdminPage",

  data() {
    return {
      activeSection: "dashboard",

      // match HomePage theme behavior and default colors
      isDark: true,

      darkColor: "#000000",
      lightColor: "#ffffff"
    };
  },

  mounted() {
    // reuse the same saved mode used by HomePage
    const savedMode = localStorage.getItem("savedMode");

    if (savedMode === "Dark") {
      this.isDark = true;
    } else if (savedMode === "Light") {
      this.isDark = false;
    }

    // reuse HomePage saved colors
    const savedDarkColor = localStorage.getItem("savedDarkColor");
    const savedLightColor = localStorage.getItem("savedLightColor");

    if (savedDarkColor) {
      this.darkColor = savedDarkColor;
    }

    if (savedLightColor) {
      this.lightColor = savedLightColor;
    }
  },

  methods: {
    // match HomePage dark/light behavior
    toggleMode() {
      this.isDark = !this.isDark;

      if (this.isDark) {
        this.darkColor = "#000000";
        this.lightColor = "#ffffff";
        localStorage.setItem("savedMode", "Dark");
      } else {
        this.darkColor = "#ffffff";
        this.lightColor = "#000000";
        localStorage.setItem("savedMode", "Light");
      }

      localStorage.setItem("savedDarkColor", this.darkColor);
      localStorage.setItem("savedLightColor", this.lightColor);
    },

    // save selected background color
    updateDarkColor() {
      localStorage.setItem("savedDarkColor", this.darkColor);
    },

    // save selected text color
    updateLightColor() {
      localStorage.setItem("savedLightColor", this.lightColor);
    },

    changeSection(section) {
      this.activeSection = section;
    }
  }
};
</script>


<template>
  <div
      class="admin-main"
      :class="{
        'dark-mode': isDark,
        'light-mode': !isDark
      }"
      :style="[
        { backgroundColor: darkColor },
        { color: lightColor }
      ]"
  >

    <!-- Same visual background effect used on HomePage -->
    <div class="background-effect"></div>

    <div
        v-if="isDark"
        class="star-div stars-small"
    ></div>

    <div
        v-if="isDark"
        class="star-div stars-large"
    ></div>



    <div class="top">

      <h1>Admin Dashboard</h1>

      <div class="spacer"></div>


      <!-- same theme control concept as HomePage/GamePage -->
      <div class="theme-controls">

        <button
            class="toggle"
            :style="[
        {backgroundColor: isDark ? darkColor : lightColor},
        {color: isDark ? lightColor : darkColor}
      ]"
            @click="toggleMode"
        >
          {{ isDark ? "Switch to Light" : "Switch to Dark" }}
        </button>

        <div class="pickers">

          <label>
            Background

            <input
                type="color"
                v-model="darkColor"
                @input="updateDarkColor"
                class="picker"
                :style="[
            {backgroundColor: isDark ? lightColor : darkColor},
            {color: isDark ? lightColor : darkColor}
          ]"
            />
          </label>

          <label>
            Text

            <input
                type="color"
                v-model="lightColor"
                @input="updateLightColor"
                class="picker"
                :style="[
            {backgroundColor: isDark ? lightColor : darkColor},
            {color: isDark ? lightColor : darkColor}
          ]"
            />
          </label>

        </div>

      </div>

    </div>




    <div class="admin-layout">


<!--      Admin sidebar-->

      <aside
          class="admin-sidebar" :style="{borderColor: lightColor}">

        <h2>Admin</h2>


        <nav class="admin-navigation">

          <button
              class="admin-nav-button"
              :class="{
                active: activeSection === 'dashboard'
              }"
              :style="{color: lightColor, borderColor: lightColor}"
              @click="changeSection('dashboard')"
          >
            Dashboard
          </button>


          <button
              class="admin-nav-button"
              :class="{
                active: activeSection === 'games'
              }"
              :style="{color: lightColor, borderColor: lightColor}"
              @click="changeSection('games')"
          >
            Manage Games
          </button>


          <button
              class="admin-nav-button"
              :class="{
                active: activeSection === 'add-game'
              }"
              :style="{color: lightColor,  borderColor: lightColor}"
              @click="changeSection('add-game')"
          >
            Add Game
          </button>

        </nav>

      </aside>



      <main class="admin-content">

        <section v-if="activeSection === 'dashboard'" class="admin-section" :style="{borderColor: lightColor}">

          <h2>Dashboard</h2>

          <p>
            Select an admin option to manage
            Psyche Mission game content.
          </p>


          <div class="admin-cards">


            <button
                class="admin-card"
                :style="{
                  color: lightColor,
                  borderColor: lightColor
                }"
                @click="changeSection('games')"
            >

              <h3>
                Manage Games
              </h3>

              <p>
                View and manage games currently
                available on the Psyche Mission
                game portal.
              </p>

            </button>


            <button
                class="admin-card"
                :style="{color: lightColor, borderColor: lightColor}"
                @click="changeSection('add-game')"
            >

              <h3>
                Add Game
              </h3>

              <p>
                Add a new game or experience
                to the Psyche Mission portal.
              </p>

            </button>


          </div>

        </section>

<!--        manage games-->
        <section
            v-else-if="activeSection === 'games'"
            class="admin-section"
            :style="{borderColor: lightColor}"
        >

          <div class="section-header">

            <div>

              <h2>
                Manage Games
              </h2>

              <p>
                View and manage existing games.
              </p>

            </div>


            <button
                class="toggle"
                :style="{color: lightColor, borderColor: lightColor}"
                @click="changeSection('add-game')"
            >
              + Add Game
            </button>

          </div>



          <div
              class="admin-placeholder"
              :style="{
                borderColor: lightColor
              }"
          >

            <h3>
              Game Management
            </h3>

            <p>
              Game management functionality
              will be implemented here.
            </p>

          </div>

        </section>

<!--        add game-->

        <section
            v-else
            class="admin-section"
            :style="{
              borderColor: lightColor
            }"
        >

          <h2>
            Add Game
          </h2>

          <p>
            Add a new game to the portal.
          </p>


          <div
              class="admin-placeholder"
              :style="{
                borderColor: lightColor
              }"
          >

            <h3>
              Game Submission Form
            </h3>

            <p>
              The game submission form
              will be implemented here.
            </p>

          </div>

        </section>


      </main>

    </div>

  </div>
</template>


<style scoped>


.admin-main {
  min-height: 100vh;

  font-family: Arial, sans-serif;

  position: relative;

  transition:
      background-color 0.3s ease,
      color 0.3s ease;

  padding-bottom: 20px;

  overflow: hidden;
}


/* =========================
   header
   ========================= */

.top {
  display: flex;

  justify-content: space-between;

  align-items: center;

  padding: 34px 20px 24px;

  margin: 20px;

  border: 1px solid rgba(128, 128, 128, 0.25);

  border-radius: 24px;

  box-shadow:
      0 8px 20px rgba(0, 0, 0, 0.08);

  gap: 20px;

  position: relative;

  z-index: 10;
}


.top h1 {
  flex: 1;

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
  flex: 0 0 auto;
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


.toggle {
  padding: 10px 15px;
  border: 1px solid currentColor;
  background: transparent;
  cursor: pointer;
  border-radius: 999px;
  font-weight: 600;
  transition:
      transform 0.2s ease,
      opacity 0.2s ease,
      box-shadow 0.2s ease;
}


@media (pointer: fine) {

  .toggle:hover {
    transform: translateY(-10px);

    box-shadow:
        0 6px 14px rgba(0, 0, 0, 0.12);
  }

}

.admin-layout {
  display: flex;

  gap: 20px;

  margin: 0 20px 20px;

  position: relative;

  z-index: 10;
}


.admin-sidebar {
  width: 220px;

  flex-shrink: 0;

  padding: 24px 18px;

  border: 1px solid rgba(128, 128, 128, 0.22);

  border-radius: 20px;

  box-shadow:
      0 6px 18px rgba(0, 0, 0, 0.08);

  background: transparent;
}


.admin-sidebar h2 {
  margin-top: 0;

  text-align: center;
}


.admin-navigation {
  display: flex;

  flex-direction: column;

  gap: 12px;
}


.admin-nav-button {
  padding: 12px 15px;

  border: 1px solid currentColor;

  background: transparent;

  border-radius: 999px;

  cursor: pointer;

  font-weight: 600;

  text-align: left;

  transition:
      transform 0.2s ease,
      box-shadow 0.2s ease,
      background-color 0.2s ease;
}


.admin-nav-button:hover {
  transform: translateY(-3px);

  box-shadow:
      0 6px 14px rgba(0, 0, 0, 0.12);
}


.admin-nav-button.active {
  background:
      rgba(128, 128, 128, 0.25);

  outline: 2px solid currentColor;

  outline-offset: -2px;
}

.admin-content {
  flex: 1;
}


.admin-section {
  padding: 28px 24px;

  border: 1px solid rgba(128, 128, 128, 0.22);

  border-radius: 20px;

  box-shadow:
      0 6px 18px rgba(0, 0, 0, 0.08);

  min-height: 500px;
}


.admin-section h2 {
  margin-top: 0;
}


.admin-section p {
  line-height: 1.6;

  opacity: 0.85;
}


/* =========================
   dashboard
   ========================= */

.admin-cards {
  display: grid;

  grid-template-columns:
      repeat(2, 1fr);

  gap: 24px;

  margin-top: 30px;
}


.admin-card {
  padding: 28px 22px;

  border: 1px solid rgba(128, 128, 128, 0.22);

  border-radius: 20px;

  box-shadow:
      0 6px 18px rgba(0, 0, 0, 0.08);

  background:
      linear-gradient(
          145deg,
          rgba(255, 255, 255, 0.1),
          rgba(0, 0, 0, 0.2)
      );

  text-align: center;

  cursor: pointer;

  transition:
      transform 0.2s ease,
      box-shadow 0.2s ease;
}


.admin-card:hover {
  transform: translateY(-10px);

  box-shadow:
      0 10px 22px rgba(0, 0, 0, 0.14);
}


.admin-card h3 {
  margin-top: 0;
}

.section-header {
  display: flex;

  justify-content: space-between;

  align-items: center;

  gap: 20px;
}


.admin-placeholder {
  margin-top: 30px;

  padding: 35px;

  border: 1px solid rgba(128, 128, 128, 0.22);

  border-radius: 20px;

  box-shadow:
      0 6px 18px rgba(0, 0, 0, 0.08);

  text-align: center;
}

.background-effect {
  position: absolute;

  top: 0;

  width: 100%;

  height: 100%;

  background:
      radial-gradient(
          circle at 50% 50%,
          rgba(97, 64, 196, 0.5),
          transparent 80%
      );

  filter: blur(50px);

  z-index: 0;
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
      radial-gradient(
          2px 2px at 20px 30px,
          #fff,
          rgba(0, 0, 0, 0)
      ),

      radial-gradient(
          2px 2px at 50px 70px,
          #fff,
          rgba(0, 0, 0, 0)
      ),

      radial-gradient(
          2px 2px at 150px 50px,
          #fff,
          rgba(0, 0, 0, 0)
      ),

      radial-gradient(
          2px 2px at 300px 250px,
          #fff,
          rgba(0, 0, 0, 0)
      ),

      radial-gradient(
          2px 2px at 500px 100px,
          #fff,
          rgba(0, 0, 0, 0)
      );

  background-size: 800px 800px;

  animation:
      moveStars 100s linear infinite;

  opacity: 0.5;
}


.stars-large {
  background-image:
      radial-gradient(
          5px 5px at 100px 150px,
          #fff,
          rgba(0, 0, 0, 0)
      ),

      radial-gradient(
          5px 5px at 250px 170px,
          #fff,
          rgba(0, 0, 0, 0)
      ),

      radial-gradient(
          5px 5px at 400px 350px,
          #fff,
          rgba(0, 0, 0, 0)
      );

  background-size: 1000px 1000px;

  animation:
      moveStars 60s linear infinite;

  opacity: 0.8;
}


@keyframes moveStars {

  from {
    transform: translate(0, 0);
  }

  to {
    transform: translate(-400px, -400px);
  }

}


@media (max-width: 800px) {

  .admin-layout {
    flex-direction: column;
  }


  .admin-sidebar {
    width: auto;
  }


  .admin-navigation {
    flex-direction: row;

    flex-wrap: wrap;
  }


  .admin-cards {
    grid-template-columns: 1fr;
  }

}


@media (max-width: 600px) {

  .top {
    position: relative;

    padding: 15px;

    min-height: 80px;
  }


  .top h1 {
    font-size: 1.05rem;

    text-align: left;

    padding-right: 125px;
  }


  .theme-controls {
    position: absolute;

    top: 10px;

    right: 10px;

    transform: scale(0.65);

    transform-origin: top right;
  }


  .admin-layout {
    margin:
        0 14px 14px;
  }


  .admin-sidebar {
    padding: 16px;
  }


  .admin-content {
    width: 100%;
  }


  .admin-section {
    padding: 22px 16px;
  }


  .section-header {
    flex-direction: column;

    align-items: flex-start;
  }

}

</style>