import { createRouter, createWebHistory } from 'vue-router'
import GamePage from "../views/GamePage.vue"
import HomePage from "../views/HomePage.vue"
import NotFound from "../views/NotFound.vue"

import Login from "@/views/Login.vue";

import AdminPage from "../views/AdminPage.vue"
import SignUpPage from "@/views/SignUpPage.vue";
import UserProfilePage from "@/views/UserProfilePage.vue";
import { isLoggedIn, isAdmin } from "@/utils/auth";

const routes = [
    {path: "/", component: HomePage},
    {path: "/gamepage", component: GamePage},
    {path: "/game/:id", name: "GamePage", component: GamePage, props: true},

    {path: "/login", name: "Login", component: Login},

    {path: "/admin", name: "Admin", component: AdminPage, meta: { requiresAdmin: true }},

    {path: "/signup", name:SignUpPage, component: SignUpPage},

    {path: "/profile", name:UserProfilePage, component: UserProfilePage, meta: { requiresAuth: true }},

    {
        path: "/:path(.*)*", // Must be at end of routes. Checks for any url path that has not been defined above
        name: "NotFound",
        component: NotFound  // load NotFound page for any url not specified above
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        return {top: 0, left: 0};
    }
});

router.beforeEach((to) => {
    if (to.meta.requiresAuth && !isLoggedIn()) {
        return "/login";
    }

    if (to.meta.requiresAdmin && !isAdmin()) {
        return isLoggedIn() ? "/" : "/login";
    }
});

export default router;