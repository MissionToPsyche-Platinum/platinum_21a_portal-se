<script setup>
import { ref } from 'vue'

    const emit = defineEmits(['close', 'quiz-complete'])
    const closeModal = () => emit('close')
    const currentQuestionIdx = ref(0)
    const answers = ref([])
    const questions = [
        {
            question:"What device are you playing on?",
            options:["VR Headset", "Laptop/Desktop", "Phone/Tablet"]
        },
        {
            question:"How challenging would you like it?",
            options:["Easy", "Medium", "Hard"]
        },
        {
            question:"What sounds like the most fun?",
            options:["Fast-paced action", "Exploring space", "Solving puzzles", "Trivia and quizzes", "Doing science or collecting data", "Building or managing things"]
        },
        {
            question:"What are you the most interested in?",
            options:["Psyche asteroid", "Planets and moons", "Space missions and spacecraft", "Learning science concepts", "Just playing for fun"]
        }
    ]
    const selectAnswer = (option) => {
        answers.value[currentQuestionIdx.value] = option

        if (currentQuestionIdx.value < questions.length - 1) {
            currentQuestionIdx.value++
        }
        else {
            showResults()
        }

    }
    const isFinished = ref(false)
    const showResults = () => {
        console.log("User answers:", answers.value)
        isFinished.value = true
        emit('quiz-complete', answers.value)
        setTimeout(() => {
            closeModal()
        }, 10000
        )
    }

</script>

<template>
    <div class="modal" @click="closeModal">
        <div class="modal-content" @click.stop>
            <h2>Quiz</h2>

            <div v-if="!isFinished">
                <h3>
                    {{ questions[currentQuestionIdx].question }}
                </h3>

                <div class="options">
                    <button v-for="(option, index) in questions[currentQuestionIdx].options"
                    :key="index"
                    @click="selectAnswer(option)"
                    class="option-button">
                        {{ option }}
                    </button>
                </div>
            </div>

            <div v-else>
                <h3>
                    Quiz Completed!
                </h3>
                <p>
                    Your answers: {{ answers }}
                </p>
            </div>

            <div class="modal-actions">
                <button class="btn" @click="closeModal">Close</button>
                <button>Submit</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}
.modal-content {
    background: whitesmoke;
    color: black;
    padding: 24px;
    border-radius: 10px;
    text-align: center;
    width: 400px;
    max-width: 90%;
}
.modal-actions {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}
</style>
