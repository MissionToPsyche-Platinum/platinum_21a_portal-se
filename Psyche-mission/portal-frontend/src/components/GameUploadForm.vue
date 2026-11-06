<script>
import { getSession } from "@/utils/auth";

export default {
    data() {
        return {
            //Game Information
            title: "",
            difficulty: "",
            genre: "",
            age: "",
            description: "",

            //Additional Details
            className: "",
            engine: "",
            credits: "",
            gtype: "",

            //Media and Resources
            thumbnail: "",
            src: "",
            video: "",

            //Status
            submitted: false,
            submitting: false,
            error: ""
        }
    },
    methods: {
     async   submitGameUploadRequest() {

          if (this.submitting) return;

          const session = getSession();
          this.submitted = false;
          this.error = "";

          if (!session?.username) {
            this.error = "Please log in before submitting a game upload request.";
            return;
          }

          // validate required fields
          if (![this.title, this.difficulty, this.genre, this.age,
            this.credits, this.src, this.description]
              .every(value => typeof value === "string" && value.trim())) {
            this.error = "Please complete all required fields.";
            return;
          }
            const request = {
                title: this.title,
                thumbnail: this.thumbnail,
                description: this.description,
                difficulty: this.difficulty,
                genre: this.genre,
                age: this.age,
                gtype: this.gtype,
                engine: this.engine,
                className: this.className,
                video: this.video,
                credits: this.credits,
                src: this.src
            }
       this.submitting = true;

       let csrf;

       try {
         const csrfResponse = await fetch(
             "http://localhost:8080/api/auth/csrf",
             { credentials: "include" }
         );

         if (!csrfResponse.ok) {
           throw new Error(`CSRF request failed: ${csrfResponse.status}`);
         }

         csrf = await csrfResponse.json();
       } catch (error) {
         console.error("CSRF initialization failed:", error);
         this.error = "Could not initialize upload security.";
         this.submitting = false;
         return;
       }

       fetch("http://localhost:8080/api/game-upload-requests", {
         method: "POST",
         credentials: "include",
         headers: {
           "Content-Type": "application/json",
           "X-username": session.username,
           [csrf.headerName]: csrf.token
         },
         body: JSON.stringify(request)
            }).then(response => {
                if (!response.ok) {
                    throw new Error(`Server returned ${response.status}`);
                }

                return response.json();
            }).then(data => {
                console.log("Request submitted: ", data);
                this.submitted = true;
            }).catch(error => {
                console.error("Error submitting request: ", error);
                this.error = "Failed to submit game upload request.";
            }).finally(() => {
              this.submitting = false;
            });
        }
    }
}
</script>

<template>
    <div class="game-upload-form">
        <div class="form-heading">
            <h2>Submit a Request to Upload a New Game</h2>
            <p>
                Provide the details below to request that your game be added to the Psyche Portal.
            </p>
            <p class="required-note">* Required fields</p>
        </div>

        <p v-if="submitted">Game upload request submitted successfully!</p>
        <p v-if="error">{{ error }}</p>

        <form @submit.prevent="submitGameUploadRequest">
            <section class="form-section">
                <div class="section-heading">
                    <h3>1. Game Information</h3>
                    <p>Tell us about your game.</p>
                </div>

                <div class="form-grid">
                    <div class="form-row">
                        <label for="title">Game Title *</label>
                        <input id="title" v-model="title" type="text" required />
                    </div>

                    <div class="form-row">
                        <label for="difficulty">Difficulty *</label>
                        <select id="difficulty" v-model="difficulty" required>
                            <option value="" disabled>Select Difficulty</option>
                            <option value="Easy">Easy</option>
                            <option value="Medium">Medium</option>
                            <option value="Hard">Hard</option>
                        </select>
                    </div>

                    <div class="form-row">
                        <label class="long-label" for="genre">Genre *</label>
                        <select id="genre" v-model="genre" required>
                            <option value="" disabled>Select Genre</option>
                            <option value="Arcade">Arcade</option>
                            <option value="Trivia">Trivia</option>
                            <option value="Adventure">Adventure</option>
                            <option value="Simulation">Simulation</option>
                            <option value="VR Experience">VR Experience</option>
                            <option value="AR Experience">AR Experience</option>
                            <option value="Interactive Simulation">Interactive Simulation</option>
                        </select>
                    </div>

                    <div class="form-row">
                        <label for="age">Age *</label>
                        <select id="age" v-model="age" required>
                            <option value="" disabled>Select Age</option>
                            <option value="Elementary">Elementary</option>
                            <option value="Middle">Middle</option>
                            <option value="High">High</option>
                        </select>
                    </div>

                    <div class="form-row">
                        <label for="description">Description *</label>
                        <textarea id="description" v-model="description" required></textarea>
                    </div>
                </div>
            </section>

            <section class="form-section">
                <div class="section-heading">
                    <h3>2. Media and Resources</h3>
                    <p>Provide links to the game's media.</p>
                </div>

                <div class="form-grid">
                    <div class="form-row">
                        <label for="thumbnail">Thumbnail URL *</label>
                        <input id="thumbnail" v-model="thumbnail" type="text" />
                    </div>

                    <div class="form-row">
                        <label for="video">Video URL *</label>
                        <input id="video" v-model="video" type="text" />
                    </div>

                    <div class="form-row">
                        <label for="src">Game URL *</label>
                        <input id="src" v-model="src" type="text" required />
                    </div>
                </div>
            </section>

            <section class="form-section">
                <div class="section-heading">
                    <h3>3. Additional Details</h3>
                </div>

                <div class="form-grid">
                    <div class="form-row">
                        <label for="className">Class Name *</label>
                        <select id="className" v-model="className" required>
                            <option value="" disabled>Select Class</option>
                            <option value="Platinum - 2026">Platinum - 2026</option>
                            <option value="Iridium - 2025">Iridium - 2025</option>
                            <option value="Tungsten - 2024">Tungsten - 2024</option>
                            <option value="Silver - 2023">Silver - 2023</option>
                            <option value="Copper - 2022">Copper - 2022</option>
                            <option value="Nickel - 2021">Nickel - 2021</option>
                        </select>
                    </div>

                    <div class="form-row">
                        <label for="gtype">gtype *</label>
                        <input id="gtype" v-model="gtype" type="text" />
                    </div>

                    <div class="form-row">
                        <label for="engine">Engine *</label>
                        <input id="engine" v-model="engine" type="text"  />
                    </div>

                    <div class="form-row">
                        <label for="credits">Credits *</label>
                        <input id="credits" v-model="credits" type="text" required />
                    </div>
                </div>
            </section>

        <button class="button" type="submit" :disabled="submitting">
            {{ submitting ? "Submitting..." : "Submit Request" }}
        </button>
        </form>
    </div>
</template>

<style>
.game-upload-form {
    width: 100%;
    max-width: 700px;
    margin: 0 auto;
    padding: 30px;
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 10px;
    box-sizing: border-box;
}
.game-upload-form form {
    display: flex;
    flex-direction: column;
    gap: 16px;
}
.form-row {
    display: grid;
    grid-template-columns: 160px minmax(0, 1fr);
    align-items: center;
    gap: 16px;
}
.form-row input {
    width: 100%;
    box-sizing: border-box;
    padding: 8px;
}
.form-row select {
    width: 100%;
    box-sizing: border-box;
    padding: 8px;
}
.form-row textarea {
    width: 100%;
    box-sizing: border-box;
    padding: 8px;
}
.form-row .long-label {
    display: block;
    width: 160px;
    white-space: normal;
    overflow-wrap: break-word;
    line-height: 1.3;
    text-align: left;
}
.form-heading {
    margin-bottom: 30px;
}
.form-heading h2 {
    margin: 0 0 8px;
    font-size: 24px;
}
.form-heading p {
    margin: 0;
    color: #64748b;
    font-size: 14px;
}
.form-heading .required-note {
    margin-top: 12px;
    font-size: 12px;
}
.form-section {
    padding: 24px 0;
    border-top: 1px solid #e2e8f0;
}
.section-heading {
    margin-bottom: 20px;
}
.section-heading h3 {
    margin: 0 0 6px;
    font-size: 17px;
}
.section-heading p {
    margin: 0;
    color: #64748b;
    font-size: 14px;
}
.form-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 20px;
}
.form-row {
    display: flex;
    flex-direction: column;
    gap: 8px;
    min-width: 0;
}
.form-row label {
    font-size: 13px;
    font-weight: 600;
    color: #334155;
}
.form-row input, .form-row select, .form-row textarea {
    width: 100%;
    box-sizing: border-box;
    padding: 11px 12px;
    border: 1px solid #cbd5e1;
    border-radius: 6px;
    background: #ffffff;
    font: inherit;
    font-size: 14px;
    color: #1e293b;
    transition: border-color 0.2s, box-shadow 0.2s;
}
.form-row textarea {
    min-height: 120px;
    resize: vertical;
}
.form-row input:focus, .form-row select:focus, .form-row textarea:focus {
    outline: none;
    border-color: #2563eb;
    box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
}
.form-row input::placeholder, .form-row textarea::placeholder {
    color: #94a3b8;
}
.button {
    padding: 12px 24px;
    border-radius: 5px;
    border: none;
    background: lightgray;

    cursor: pointer;
    transition: background 0.2s, transform 0.2s;
}
.button:hover:not(:disabled) {
    background: gray;
    transform: translateY(-1px);
}
.button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}
@media (max-width: 600px) {
    .form-row {
        grid-template-columns: 1fr;
        gap: 6px;
    }
    .form-row label {
        text-align: left;
    }
}
</style>