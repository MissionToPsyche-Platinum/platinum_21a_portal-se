import { createRouter, createWebHistory } from 'vue-router'
import Welcome from "../views/Welcome.vue"
import GamePage from "../views/GamePage.vue"

const routes = [
    {path: "/", component: Welcome},
    {path: "/gamepage", component: GamePage}
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;