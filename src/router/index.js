import { createRouter, createWebHashHistory } from 'vue-router'
import GamePage from "../views/GamePage.vue"
import HomePage from "../views/HomePage.vue"
import NotFound from "../views/NotFound.vue"

const routes = [
    {path: "/", component: HomePage},
    {path: "/gamepage", component: GamePage},
    {path: "/game/:id", name: "GamePage", component: GamePage, props: true},
    {
        path: "/:path(.*)*", // Must be at end of routes. Checks for any url path that has not been defined above
        name: "NotFound",
        component: NotFound  // load NotFound page for any url not specified above
    },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        return {top: 0, left: 0};
    }
});

export default router;