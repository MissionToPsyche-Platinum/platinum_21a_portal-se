<script>
export default {
  name: 'GamePage',
  data() {
    return {
      gameIsDark: false,// variable to track if dark mode is active
    }
  },
  mounted() {
    // get the previously saved mode from the browser local storage
    const savedMode = localStorage.getItem("gameSavedMode");
    //check if previous mode is a dark mode, enable dark mode on page load
    if(savedMode==="Dark"){
      this.isDark=true;// will update automatically

    }


  },
  methods:{

    //this function toggle between dark mode and light mode
    toggleMode(){
      this.gameIsDark=!this.gameIsDark;//if dark mode is active change to light mode, if light mode is active change to dark mode
      if(this.gameIsDark){
        localStorage.setItem("gameSavedMode","Dark");// save dark mode in local storage
      }else{
        localStorage.setItem("gameSavedMode","Light");// save light mode in local storage
      }
    }

  }
}
</script>

<template>
  <div :class="['main-container',gameIsDark ? 'main-container-dark': 'main-container-light']">
    <nav :class="['navbar', gameIsDark ? 'navbar-dark ' : 'navbar-light']">
      <h1>navbar component goes here</h1>
    </nav>
    <!--toggle button that change the theme based on the mode, calls th toggleMode() function-->
    <button :class="['toggle', gameIsDark ? 'main-container-dark': 'main-container-light']" @click="toggleMode">
      <!--dynamically change thee text based on the mode-->
      {{isDark? "Switch to Light": "Switch to Dark" }}
    </button>

    <div class="game">
      <div class="placeholder-content">Game component goes here</div>
    </div>

    <div class="details">
      <div class="description">
        <h3>Game Description</h3>
        <p>Game description component goes here.</p>
      </div>

      <div class="vertical-divider"></div>

      <div class="metadata">
        <h3>Metadata</h3>
        <p>Metadata component goes here</p>
      </div>
    </div>

    <div class="suggested-games">
      <div v-for="i in 3" :key="i" :class="['suggestion',gameIsDark ? 'suggestion-dark': 'suggestion-light']">
        Suggested Game components go here
      </div>
    </div>

    <footer :class="['footer',gameIsDark ? 'footer-dark' : 'footer']">
      <p>footer component goes here</p>
    </footer>
  </div>
</template>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  color: #333;
}

.navbar {
  background: #2c3e50;
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: center;
  text-align: center;
}

.game {
  width: 100%;
  height: 50vh;
  background: #120e31;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
}

.details {
  display: flex;
  padding: 2rem;
  gap: 2rem;
  border-bottom: 1px solid #ccc;
}

.description {
  flex: 1;
}

.metadata {
  flex: 1;
}

.vertical-divider {
  width: 1px;
  background-color: #ccc;
}

.suggested-games {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  margin-top: 3rem;
  gap: 1rem;
  padding: 2rem;
}

.suggestion {
  background: #f4f4f4;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer {
  margin-top: auto;
  padding: 1rem;
  background: #eee;
  text-align: center;
}

/* light mode styling*/
.main-container-light{
  color: #000309;
  background-color: #ffffff;
}

/*dark mode styling*/
.main-container-dark{
  color: #ffffff;
  background-color: #000309;
}
.suggestion-dark{
  background: #000309;
  color: #ffffff;
  border: 1px solid #ccc;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.suggestion-light {
  background: #f4f4f4;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer-dark {
  color: #ffffff;
  border: 1px solid #ccc;
  margin-top: auto;
  padding: 1rem;
  background: #000309;
  text-align: center;
}

.navbar-dark {
  background: #000309;
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: center;
  text-align: center;
}

.navbar-light{
  background: #2c3e50;
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: center;
  text-align: center;
}

</style>