<script>
export default {
  name: "GameForm",

  props: {
    game: {
      type: Object,
      default: null
    },

    textColor: {
      type: String,
      default: "#ffffff"
    }
  },

  emits: [
    "submit-game",
    "cancel"
  ],

  data() {
    return {
      form: {
        id: "",
        title: "",
        description: "",
        difficulty: "",
        genre: "",
        age: "",
        class: "",
        credits: "",
        src: "",
        thumbnail: "",
        engine: ""
      },

      errors: {}
    };
  },

  computed: {
    isEditMode() {
      return this.game !== null;
    },

    formTitle() {
      return this.isEditMode ? "Edit Game" : "Add New Game";
    },

    submitButtonText() {
      return this.isEditMode ? "Save Changes" : "Submit Game";
    }
  }
};
</script>

<template>
  <div
      class="game-form-container"
      :style="{color: textColor, borderColor: textColor}"
  >
    <h2>
      {{ formTitle }}
    </h2>
  </div>
</template>

<style scoped>
.game-form-container {
  margin-top: 25px;
  padding: 28px 24px;
  border: 1px solid rgba(128, 128, 128, 0.22);
  border-radius: 20px;
}
</style>