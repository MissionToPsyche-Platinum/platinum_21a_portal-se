import { createRouter, createWebHistory } from 'vue-router'
import HomePage from "../views/HomePage.vue"
import GamePage from "../views/GamePage.vue"

const routes = [
    {path: "/", component: HomePage},
    {path: "/gamepage", component: GamePage},
    {path: "/:id", name: "GamePage", component: GamePage, props: true}
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;