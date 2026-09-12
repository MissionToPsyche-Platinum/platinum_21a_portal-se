<script setup>
import { ref, computed } from 'vue'
    const emit = defineEmits(['close', 'quiz-complete'])
    const closeModal = () => emit('close')
    const currentQuestionIdx = ref(0)
    const answers = ref({device: null, difficulty: null, fun: null, interest: null})
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
        const keyMap = ['device', 'difficulty', 'fun', 'interest']

        answers.value[keyMap[currentQuestionIdx.value]] = option

        if (currentQuestionIdx.value < questions.length - 1) {
            currentQuestionIdx.value++
        }
    }
    const isFinished = ref(false)
    const isQuizComplete = () => {
        return answers.value.device && answers.value.difficulty && answers.value.fun && answers.value.interest
    }
    const showResults = () => {
        if (answers.value.device === null || answers.value.difficulty === null || answers.value.fun === null || answers.value.interest === null) {
            alert("Please answer all questions before submitting.")
            return
        }

        console.log("User answers:", answers.value)

        isFinished.value = true

        emit('quiz-complete', answers.value)

        closeModal()
    }
    const submitAndViewResults = () => {
        emit('quiz-complete', answers.value)
    }
    const progress = computed(() => {
        const answered = Object.values(answers.value).filter(answer => answer !== null).length
        return (answered / questions.length) * 100
    })
</script>

<template>
    <div class="modal" @click="closeModal">
        <div class="modal-content" @click.stop>
            <h2>Quiz</h2>
            <div class="progress-container">
                <div class="progress-bar" :style="{ width: progress + '%' }"></div>
            </div>

            <p class="progres-text">Question {{ currentQuestionIdx + 1 }} of {{ questions.length }}</p>

            <div v-if="!isFinished" class="quiz-body">
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
                <button class="btn" :disabled="!isQuizComplete()" @click="submitAndViewResults">View Results</button>
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
    background: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1100;
}
.modal-content {
    background: #ffffff;
    color: #000000;
    padding: 24px;
    border-radius: 8px;
    text-align: center;
    width: 400px;
    max-width: 90%;
}
.modal-content h2 {
    margin: 0 0 8px;
}
.modal-actions {
    display: flex;
    justify-content: space-between;
    gap: 12px;
    margin-top: 20px;
}
.btn {
    padding: 8px 16px;
    border-radius: 6px;
    border: 1px solid #111111;
    background: #111111;
    color: #ffffff;
    cursor: pointer;
    font-weight: 600;
}
.btn:last-child {
    background: #111111;
}
.btn:first-child {
    background: #ffffff;
    color: #111111;
}
.btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}
.progress-container {
    background-color: #dddddd;
    width: 100%;
    height: 8px;
    border-radius: 4px;
    overflow: hidden;
    margin: 10px 0 5px;
}
.progress-bar {
    height: 100%;
    background: #111111;
    transition: width 0.3s ease;
}
.progres-text {
    font-size: 12px;
    opacity: 0.7;
    margin-bottom: 10px;
}
.quiz-body {
    margin-top: 10px;
}
.options {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-top: 15px;
}
.option-button {
    padding: 10px;
    border-radius: 6px;
    border: 1px solid #cccccc;
    cursor: pointer;
    background: #f5f5f5;
}
.option-button:hover {
    background: #ececec;
    border-color: #111111;
}
</style>
