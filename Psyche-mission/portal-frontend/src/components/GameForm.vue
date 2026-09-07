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
  },
  watch: {

    game: {
      immediate: true,

      handler(newGame) {

        if (newGame) {

          this.form = {
            id: newGame.id || "",
            title: newGame.title || "",
            description: newGame.description || "",
            difficulty: newGame.difficulty || "",
            genre: newGame.genre || "",
            age: newGame.age || "",
            class: newGame.class || "",
            credits: newGame.credits || "",
            src: newGame.src || "",
            thumbnail: newGame.thumbnail || "",
            engine: newGame.engine || ""
          };

        } else {

          this.resetForm();

        }

      }
    }

  },

  methods: {

    resetForm() {

      this.form = {
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
      };

      this.errors = {};
    },

    validateForm() {

      const errors = {};

      if (!this.form.id.trim()) {
        errors.id = "Game ID is required.";
      }

      if (!this.form.title.trim()) {
        errors.title = "Game title is required.";
      }

      if (!this.form.description.trim()) {
        errors.description = "Description is required.";
      }

      if (!this.form.difficulty) {
        errors.difficulty = "Difficulty is required.";
      }

      if (!this.form.genre) {
        errors.genre = "Genre is required.";
      }

      if (!this.form.age) {
        errors.age = "Age group is required.";
      }

      if (!this.form.class) {
        errors.class = "Class is required.";
      }

      if (!this.form.src.trim()) {
        errors.src = "Game URL is required.";
      }

      this.errors = errors;

      return Object.keys(errors).length === 0;
    },

    submitForm() {

      if (!this.validateForm()) {
        return;
      }

      this.$emit(
          "submit-game",
          { ...this.form }
      );
    },

    cancelForm() {
      this.$emit("cancel");
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

  box-shadow:
      0 6px 18px rgba(0, 0, 0, 0.08);

  background:
      linear-gradient(
          145deg,
          rgba(255, 255, 255, 0.07),
          rgba(0, 0, 0, 0.08)
      );
}

.game-form-container h2 {
  margin-top: 0;
  margin-bottom: 25px;
}

.game-form {
  display: grid;
  grid-template-columns:
    repeat(2, minmax(0, 1fr));
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.form-group-full {
  grid-column: 1 / -1;
}

.form-group label {
  font-weight: 600;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  box-sizing: border-box;
  padding: 12px 14px;
  border: 1px solid #aaaaaa;
  border-radius: 10px;
  background-color: #ffffff;
  color: #000000;
  font-family: Arial, sans-serif;
  font-size: 1rem;
}

.form-group textarea {
  resize: vertical;
  min-height: 120px;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: 2px solid #7551c9;
  outline-offset: 1px;
}

.form-group input:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-message {
  color: #ff5a5a;
  font-size: 0.85rem;
  font-weight: 600;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 10px;
}

.form-button {
  padding: 10px 18px;
  border: 1px solid currentColor;
  border-radius: 999px;
  background: transparent;
  cursor: pointer;
  font-weight: 600;

  transition:
      transform 0.2s ease,
      box-shadow 0.2s ease;
}

.form-button:hover {
  transform: translateY(-3px);

  box-shadow:
      0 6px 14px rgba(0, 0, 0, 0.12);
}

.submit-button {
  background:
      rgba(97, 64, 196, 0.35);
}

@media (max-width: 700px) {

  .game-form {
    grid-template-columns: 1fr;
  }

  .form-group-full {
    grid-column: auto;
  }

  .form-actions {
    flex-direction: column-reverse;
  }

  .form-button {
    width: 100%;
  }

}

</style>
