import { createRouter, createWebHistory } from 'vue-router'
import HomePage from "../views/HomePage.vue"
import GamePage from "../views/GamePage.vue"

const routes = [
    {path: "/", component: HomePage},
    {path: "/gamepage", component: GamePage}
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;