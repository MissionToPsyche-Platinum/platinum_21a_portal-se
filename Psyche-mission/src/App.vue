<script>
export default {
  name: "HomePage",
  data() {
    return {};
  }
};
</script>

<template>
  <router-view></router-view>

  <h1>Welcome to the Psyche mission's web-based game portal!</h1>
>>>>>>> dev


  <div class="main">  <!--root container-->
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
    </section>

    <!-- footer -->
    <footer class="footer">
      <p>Psyche Mission Portal – Capstone Project</p>
    </footer>

  </div>

</template>

<style>

.main {
  border: 1px solid ;
  font-family: Arial, sans-serif;
}


.header {
  border: 1px solid ;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
}



.intro {
  border: 1px solid ;
  padding: 10px;
  text-align: center;
}


.platforms {
  display: grid;
  grid-template-columns:  1fr 1fr 1fr 1fr;  /* 4 columns equal width*/
  gap: 20px;
  padding: 10px;
}

.platform {
  border: 1px solid ;
  padding: 10px;
  text-align: center;
}

.games-placeholder {
  border: 1px solid ;
  padding: 40px;
  text-align: center;
}

.footer {
  border: 1px solid ;
  text-align: center;
  padding: 15px;

}

/* Responsiveness */

@media (max-width: 900px) {
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