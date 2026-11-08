<script>
import { getSession } from "@/utils/auth";
import UserPortalLayout from "@/components/UserPortalLayout.vue"


export default {
    components: {
        UserPortalLayout
    },
    data() {
        return {
            username: getSession()?.username || "User",
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
    <UserPortalLayout :username="username">
        <main class="requests-page">
            <div class="page-header">
                <h1>My Upload Requests</h1>
                <p>View the status and details of your game submissions.</p>
            </div>


            <section class="requests-section">
                <div class="section-heading">
                    <h2>Your Submissions</h2>
                    <span class="request-count">
                   {{requests.length}}
                   {{requests.length === 1 ? "request" : "requests"}}
               </span>
                </div>


                <div v-if="loading" class="message">Loading your requests...</div>
                <div v-else-if="error" class="error message">{{ error }}</div>
                <div v-else-if="requests.length === 0" class="empty">
                    <h3>No upload requests.</h3>
                    <p>You haven't submitted any games for approval.</p>
                    <router-link to="/profile/upload-request" class="submit-link">
                        Submit a Game
                    </router-link>
                </div>


                <div v-else class="request-list">
                    <article v-for="request in requests" :key="request.requestId" class="request-card">
                        <div class="request-header">
                            <h3>{{request.game?.title || "Untitled Game"}}</h3>
                            <span class="status-badge" :class="'status-' + request.requestStatus?.toUpperCase()">
                           {{request.requestStatus}}
                       </span>
                        </div>


                        <div class="request-details">
                            <p v-if="request.game?.genre">
                                <strong>Genre:</strong>
                                {{request.game.genre}}
                            </p>


                            <p v-if="request.game?.description">
                                <strong>Description:</strong>
                                {{request.game?.description}}
                            </p>


                            <p>
                                <strong>Request ID:</strong>
                                {{request.requestId}}
                            </p>
                        </div>
                    </article>
                </div>
            </section>
        </main>
    </UserPortalLayout>
</template>


<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.requests-page {
    max-width: 900px;
    margin: 2rem auto;
    padding: 1.5rem;
    text-align: center;
    font-family: "Inter", sans-serif;
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
    text-align: center;
}
.request-count {
    padding: 6px 14px;
    color: #fff;
    background: #365f91;
    border-radius: 20px;
    font-size: 0.85rem;
    font-weight: 600;
}
.status-badge {
    flex-shrink: 0;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 100px;
    padding: 7px 15px;
    border-radius: 8px;
    font-size: 0.8rem;
    font-weight: 700;
    letter-spacing: 0.3px;
    text-transform: uppercase;
    border: 1px solid;
}
.page-header {
    margin-bottom: 30px;
}
.page-header h1 {
    margin: 0 0 8px;
    font-size: 1.8rem;
    font-weight: 700;
    color: #252b33;
}
.page-header p {
    margin: 0;
    color: #667085;
    font-size: 0.95rem;
    line-height: 1.5;
}
.requests-section {
    padding: 25px;
    background: #fff;
    border: 1px solid #e2e5e9;
    border-radius: 10px;
}
.section-heading {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 15px;
    margin-bottom: 20px;
}
.section-heading h2 {
    margin: 0;
    font-size: 1.2rem;
    font-weight: 600;
    color: #303640;
}
.message {
    padding: 25px;
    text-align: center;
    background: #f5f7fa;
    border-radius: 8px;
    color: #667085;
}
.error.message {
    color: #a52834;
    background: #fce5e7;
    border: 1px solid #e8aab0;
}
.empty {
    padding: 40px 20px;
    text-align: center;
    background: #f5f7fa;
    border-radius: 8px;
}
.empty h3 {
    margin: 0 0 10px;
    color: #303640;
    font-size: 1.1rem;
}
.empty p {
    margin: 0 0 20px;
    color: #667085;
    line-height: 1.5;
}
.submit-link {
    display: inline-block;
    padding: 10px 20px;
    color: #fff;
    background: #365f91;
    border-radius: 6px;
    text-decoration: none;
    font-size: 0.9rem;
    font-weight: 600;
    transition: background 0.2s ease;
}
.submit-link:hover {
    background: #294b75;
}
.status-APPROVED {
    color: #218838;
    background: #d4edda;
}
.status-DENIED {
    color: #c82333;
    background: #f8d7da;
}
.status-PENDING {
    color: #856404;
    background: #fff3cd;
}
</style>
