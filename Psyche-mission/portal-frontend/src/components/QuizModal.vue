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
            window.setTimeout(() => {
                currentQuestionIdx.value++
            }, 280)
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
            <p class="quiz-kicker">Find your game</p>
            <h2>Quiz</h2>
            <div class="progress-container">
                <div class="progress-bar" :style="{ width: progress + '%' }"></div>
            </div>

            <ol class="quiz-steps">
                <li
                    v-for="(question, index) in questions"
                    :key="index"
                    class="quiz-step"
                    :class="{
                        current: index === currentQuestionIdx,
                        done: [answers.device, answers.difficulty, answers.fun, answers.interest][index]
                    }"
                >
                    <span class="quiz-step-num">{{ index + 1 }}</span>
                </li>
            </ol>

            <p class="progres-text">Question {{ currentQuestionIdx + 1 }} of {{ questions.length }}</p>

            <div v-if="!isFinished" class="quiz-body">
                <h3>
                    {{ questions[currentQuestionIdx].question }}
                </h3>

                <div class="options">
                    <button v-for="(option, index) in questions[currentQuestionIdx].options"
                    :key="index"
                    @click="selectAnswer(option)"
                    class="option-button"
                    :class="{ selected: [answers.device, answers.difficulty, answers.fun, answers.interest][currentQuestionIdx] === option }">
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
                <button class="btn btn-primary" :disabled="!isQuizComplete()" @click="submitAndViewResults">View Results</button>
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
    background: rgba(0, 0, 0, 0.78);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1100;
    padding: 20px;
    box-sizing: border-box;
}

.modal-content {
    background: #000000;
    color: #ffffff;
    padding: 24px;
    border: 1px solid #666666;
    border-radius: 4px;
    text-align: left;
    width: 480px;
    max-width: 100%;
    max-height: min(90vh, 720px);
    overflow-y: auto;
    box-shadow: 0 12px 28px rgba(0, 0, 0, 0.45);
    scrollbar-width: thin;
    scrollbar-color: color-mix(in srgb, currentColor 55%, transparent) transparent;
}

.modal-content::-webkit-scrollbar {
    width: 8px;
}

.modal-content::-webkit-scrollbar-track {
    background: transparent;
}

.modal-content::-webkit-scrollbar-thumb {
    background: #666666;
    border-radius: 999px;
}

.modal-content::-webkit-scrollbar-button {
    display: none;
}

.quiz-kicker {
    margin: 0 0 4px;
    font-size: 0.72rem;
    font-weight: 600;
    letter-spacing: 0.12em;
    text-transform: uppercase;
    opacity: 0.55;
}

.modal-content h2 {
    margin: 0 0 16px;
    font-size: 1.5rem;
}

.quiz-steps {
    display: flex;
    gap: 8px;
    margin: 0 0 12px;
    padding: 0;
    list-style: none;
}

.quiz-step {
    flex: 1;
    display: flex;
    justify-content: center;
}

.quiz-step-num {
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #444444;
    border-radius: 4px;
    font-size: 0.85rem;
    font-weight: 600;
    opacity: 0.55;
}

.quiz-step.current .quiz-step-num {
    border-color: currentColor;
    opacity: 1;
}

.quiz-step.done .quiz-step-num {
    border-color: #ffd60a;
    color: #ffd60a;
    opacity: 1;
}

.modal-actions {
    display: flex;
    justify-content: space-between;
    gap: 12px;
    margin-top: 24px;
}

.btn {
    padding: 10px 16px;
    border-radius: 4px;
    border: 1px solid currentColor;
    background: transparent;
    color: inherit;
    cursor: pointer;
    font-weight: 600;
}

.btn:hover {
    background: #111111;
    color: #ffffff;
    border-color: #111111;
}

.btn-primary {
    background: #ffffff;
    color: #111111;
    border-color: #ffffff;
}

.btn-primary:hover {
    opacity: 0.88;
    background: #ffffff;
    color: #111111;
    border-color: #ffffff;
}

.btn:disabled {
    opacity: 0.45;
    cursor: not-allowed;
}

.btn:disabled:hover {
    background: #ffffff;
    color: #111111;
    border-color: #ffffff;
    opacity: 0.45;
}

.progress-container {
    background-color: #222222;
    width: 100%;
    height: 4px;
    border-radius: 999px;
    overflow: hidden;
    margin: 0 0 14px;
}

.progress-bar {
    height: 100%;
    background: #e8c547;
    transition: width 0.3s ease;
}

.progres-text {
    font-size: 12px;
    opacity: 0.7;
    margin: 0 0 16px;
}

.quiz-body {
    margin-top: 4px;
}

.quiz-body h3 {
    margin: 0;
    font-size: 1.15rem;
    line-height: 1.35;
}

.options {
    display: flex;
    flex-direction: column;
    gap: 8px;
    margin-top: 16px;
}

.option-button {
    padding: 12px 14px;
    border-radius: 4px;
    border: 1px solid #666666;
    cursor: pointer;
    background: transparent;
    color: inherit;
    text-align: left;
    font: inherit;
}

.option-button:hover {
    border-color: #ffd60a;
    background: rgba(255, 214, 10, 0.08);
}

.option-button:active,
.option-button.selected {
    border-color: #ffd60a;
    background: rgba(255, 214, 10, 0.2);
    color: #ffd60a;
}
</style>
