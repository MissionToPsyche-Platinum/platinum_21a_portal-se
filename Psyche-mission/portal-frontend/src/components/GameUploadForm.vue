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
            video: "",
            credits: "",
            src: "",
            dsecription: ""
        }
    },
    methods: {
        submitGameUploadRequest() {
            const request = {
                title: this.title,
                thumbnail: this.thumbnail,
                description: this.description,
                difficulty: this.difficulty,
                genre: this.genre,
                age: this.age,
                className: this.className,
                video: this.video,
                credits: this.credits,
                src: this.src
            }

            console.log(request);
        }
    }
}
</script>

<template>
    <div class="game-upload-form">
        <h1>Submit a Request to Upload a New Game</h1>
        <form @submit.prevent="submitGameUploadRequest" />
        <div>
            <label for="title">Title</label>
            <input id="title" v-model="title" type="text" required />
        </div>
        <div>
            <label for="thumbnail">Thumbnail URL</label>
            <input id="thumbnail" v-model="thumbnail" type="text" />
        </div>
        <div>
            <label for="difficulty">Difficulty [Easy, Median, Hard]</label>
            <input id="difficulty" v-model="difficulty" type="text" required />
        </div>
        <div>
            <label for="genre">Genre [Interactive Simulation, VR Experience, AR Experience, Trivia, Adventure, Arcade, Simulation]</label>
            <input id="genre" v-model="genre" type="text" required/>
        </div>
        <div>
            <label for="age">Age [Elementary, Middle High]</label>
            <input id="age" v-model="age" type="text" required />
        </div>
        <div>
            <label for="className">Class Name</label>
            <input id="className" v-model="className" type="text" />
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

    </div>
</template>