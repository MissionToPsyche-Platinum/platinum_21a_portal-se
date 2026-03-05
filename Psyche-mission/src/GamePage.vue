<script>
export default {
    data() {
        return {
        }
    },
    methods: {

    }
}
</script>

<template>
    <div class="main-container">
        <nav class="navbar">
            <h1>navbar component goes here</h1>
        </nav>

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
            <div v-for="i in 3" :key="i" class="suggestion">
                Suggested Game components go here
            </div>
        </div>

        <footer class="footer">
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
  justify-content: space-between;
  align-items: center;
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
</style>