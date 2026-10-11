<script>
import { getSession } from "@/utils/auth";

export default {
    data() {
        return {
            requests: [],
            loading: true,
            error: ""
        }
    },
    async mounted() {
        const session = getSession();

        if (!session || !session.username) {
            this.error = "Please log in to view your request history.";
            this.loading = false;

            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/game-upload-requests/requests", {
                headers: { "X-Username": session.username }
            });

            if (!response.ok) {
                throw new Error("Unable to load request history.");
            }

            this.requests = await response.json();
            console.log("username:", session.username);
            console.log("requests:", this.requests);
        }
        catch (err) {
            this.error = err.message;
        }
        finally {
            this.loading = false;
        }
    }
}
</script>

<template>
    <main class="requests-page">
        <router-link to="/profile">Back to Profile</router-link>
        <h1>Upload Request History</h1>
        <p v-if="loading">Loading your requests...</p>
        <p v-else-if="error">{{ error }}</p>
        <p v-else-if="requests.length === 0">No requests uploaded yet.</p>
        <div v-else class="request-list">
            <article v-for="request in requests" :key="request.id" class="request-card">
                <h2>{{ request.game?.title || "Untitled Game" }}</h2>
                <p>
                    <strong>Status: </strong>
                    <span :class="'status-' + request.requestStatus.toUpperCase()">{{ request.requestStatus }}</span>
                </p>
                <p v-if="request.game?.genre">
                    <strong>Genre:</strong> {{ request.game?.genre }}
                </p>
                <p v-if="request.game?.description">
                    <strong>Description:</strong> {{ request.game.description }}</p>
                <p>
                    <strong>Request ID:</strong> {{ request.requestId }}
                </p>
            </article>
        </div>
    </main>
</template>

<style scoped>
.requests-page {
    max-width: 900px;
    margin: 2rem auto;
    padding: 1.5rem;
}
.request-list {
    display: grid;
    gap: 1rem;
    margin: 1.5rem 0;
}
.request-card {
    padding: 1.25rem;
    border: 1px solid #ddd;
    border-radius: 8px;
}
.status-APPROVED {
    color: #218838;
}
.status-DENIED {
    color: #c82333;
}
status-PENDING {
    color: #856404
}
</style>