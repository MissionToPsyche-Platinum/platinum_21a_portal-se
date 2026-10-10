<script>
import { isAdmin } from "@/utils/auth";

export default {
    name: "PendingUploadRequests",
    data() {
        return {
            requests: [],
            message: ""
        };
    },
    mounted() {
        this.loadPending();
    },
    methods: {
        adminHeaders() {
            return {
                "X-Role": isAdmin() ? "admin" : "user"
            };
        },
        async loadPending() {
            this.message = "";

            try {
                const response = await fetch("http://localhost:8080/api/game-upload-requests/pending", {
                    headers: this.adminHeaders()
                });

                if (!response.ok) {
                    this.message = "Could not load pending requests.";
                    return;
                }

                this.requests = await response.json();
            } catch {
                this.message = "Could not reach the server.";
            }
        },
        async updateRequest(id, action) {
            try {
                const response = await fetch(
                    `http://localhost:8080/api/game-upload-requests/${id}/${action}`,
                    {
                        method: "PUT",
                        headers: this.adminHeaders()
                    }
                );

                if (!response.ok) {
                    this.message = `Could not ${action} request.`;
                    return;
                }

                await this.loadPending();
            } catch {
                this.message = "Could not reach the server.";
            }
        }
    }
};
</script>

<template>
    <div>
        <h3>Pending Upload Requests</h3>
        <p v-if="message">{{ message }}</p>
        <p v-else-if="requests.length === 0">No pending requests.</p>
        <div v-for="request in requests" :key="request.requestId" class="pending-request">
            <p>
                {{ request.game ? request.game.title : "Untitled game" }}
                <span v-if="request.submittedBy">
                    — {{ request.submittedBy.username }}
                </span>
            </p>
            <button type="button" @click="updateRequest(request.requestId, 'approve')">Approve</button>
            <button type="button" @click="updateRequest(request.requestId, 'deny')">Deny</button>
        </div>
    </div>
</template>

<style scoped>
.pending-request {
    margin-top: 12px;
}
.pending-request button {
    margin-right: 8px;
    cursor: pointer;
}
</style>
