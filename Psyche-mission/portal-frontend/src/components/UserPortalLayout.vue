<script>
export default {
    props: {
        username: {
            type: String,
            default: "User"
        }
    }
}
</script>

<template>
    <div class="user-portal">
        <header class="portal-header">
            <div>
                <p class="eyebrow">PSYCHE PORTAL / USER</p>
                <h1>Welcome, {{ username }}</h1>
                <p class="subtitle">Manage your account and game submissions.</p>
            </div>
        </header>

        <div class="portal-layout">
            <aside class="portal-sidebar">
                <h3>Actions</h3>
                <nav>
                    <router-link to="/profile">Dashboard</router-link>
                    <router-link to="/profile/upload-request">Submit a Game</router-link>
                    <router-link to="/profile/requests">My Requests</router-link>
                </nav>
            </aside>

            <main class="portal-content">
                <slot />
            </main>
        </div>
    </div>
</template>

<style scoped>
.user-portal {
    width: 90%;
    max-width: 1200px;
    margin: 30px auto;
}
.portal-header {
    padding: 30px;
    margin-bottom: 24px;
    border: 1px solid #ddd;
    border-radius: 12px;
}
.eyebrow {
    font-size: 12px;
    letter-spacing: 2px;
    font-weight: bold;
    color: #777;
}
.subtitle {
    text-align: center;
}
.portal-header h1 {
    margin: 8px 0;
}
.portal-header p {
    margin-bottom: 0;
}
.portal-layout {
    display: flex;
    align-items: flex-start;
    gap: 24px;
}
.portal-sidebar {
    width: 230px;
    flex-shrink: 0;
    padding: 24px;
    border: 1px solid #ddd;
    border-radius: 12px;
}
.portal-sidebar h3 {
    margin-top: 0;
}
.portal-sidebar nav {
    display: flex;
    flex-direction: column;
    gap: 8px;
}
.portal-sidebar a {
    padding: 12px;
    border-radius: 8px;
    text-decoration: none;
    color: inherit;
    transition: background 0.2s;
}
.portal-sidebar a:hover {
    background: #eee;
}
.portal-sidebar a.router-link-exact-active {
    background: #e2e8f0;
    font-weight: bold;
}
.portal-content {
    flex: 1;
    min-width: 0;
}
@media (max-width: 800px) {
    .user-portal {
        width: calc(100% - 24px);
        margin: 16px auto;
    }
    .portal-header {
        padding: 20px 16px;
        margin-bottom: 16px;
    }
    .portal-header h1 {
        font-size: 24px;
        text-align: center;
        overflow-wrap: anywhere;
    }
    .eyebrow {
        text-align: center;
        font-size: 10px;
    }
    .subtitle {
        font-size: 14px;
    }
    .portal-layout {
        flex-direction: column;
        gap: 16px;
    }
    .portal-sidebar {
        padding: 16px;
        width: 100%;
        box-sizing: border-box;
    }
    .portal-sidebar h3 {
        margin-bottom: 12px;
    }
    .portal-sidebar nav {
        flex-wrap: nowrap;
        overflow-x: auto;
        gap: 6px;
        padding-bottom: 4px;
    }
    .portal-sidebar a {
        flex: 0 0 auto;
        padding: 10px 12px;
        font-size: 13px;
        white-space: nowrap;
    }
}
</style>