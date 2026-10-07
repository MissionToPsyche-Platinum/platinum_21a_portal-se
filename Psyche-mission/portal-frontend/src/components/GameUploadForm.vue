<script>
export default {
    data() {
        return {
            title: "",
            thumbnail: "",
            difficulty: "",
            genre: "",
            age: "",
            className: "",
            engine: "",
            video: "",
            credits: "",
            src: "",
            gtype: "",
            description: "",
            submitted: false,
            error: ""
        }
    },
    methods: {
        submitGameUploadRequest() {
            console.log("Caleld");

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

            fetch("http://localhost:8080/api/game-upload-requests", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(request)
            }).then(response => {
                if (!response.ok) {
                    throw new Error(`Server returned \`${response.status}`);
                }

                return response.json();
            }).then(data => {
                console.log("Request submitted: ", data);
                this.submitted = true;
            }).catch(error => {
                console.error("Error submitting request: ", error);
                this.error = "Failed to submit game upload request.";
            })
        }
    }
}
</script>

<template>
    <p v-if="submitted">Game upload request submitted successfully!</p>
    <p v-if="error">{{ error }}</p>
    <div class="game-upload-form">
        <h1>Submit a Request to Upload a New Game</h1>
        <form @submit.prevent="submitGameUploadRequest" >
        <div>
            <label for="title">Title</label>
            <input id="title" v-model="title" type="text" required />
        </div>
        <div>
            <label for="thumbnail">Thumbnail URL</label>
            <input id="thumbnail" v-model="thumbnail" type="text" />
        </div>
        <div>
            <label for="difficulty">Difficulty [Easy, Medium, Hard]</label>
            <input id="difficulty" v-model="difficulty" type="text" required />
        </div>
        <div>
            <label for="genre">Genre [Interactive Simulation, VR Experience, AR Experience, Trivia, Adventure, Arcade, Simulation]</label>
            <input id="genre" v-model="genre" type="text" required/>
        </div>
        <div>
            <label for="age">Age [Elementary, Middle, High]</label>
            <input id="age" v-model="age" type="text" required />
        </div>
        <div>
            <label for="className">Class Name</label>
            <input id="className" v-model="className" type="text" />
        </div>
            <div>
                <label for="gtype">gtype</label>
                <input id="gtype" v-model="gtype" type="text" />
            </div>
        <div>
            <label for="video">Video URL</label>
            <input id="video" v-model="video" type="text" />
        </div>
        <div>
            <label for="engine">Engine</label>
            <input id="engine" v-model="engine" type="text"  />
        </div>
        <div>
            <label for="credits">Credits</label>
            <input id="credits" v-model="credits" type="text" required />
        </div>
        <div>
            <label for="src">Game URL</label>
            <input id="src" v-model="src" type="text" required />
        </div>
        <div>
            <label for="description">Description</label>
            <input id="description" v-model="description" type="text" required/>
        </div>
            <button type="submit">Submit Request</button>
        </form>
    </div>
</template>