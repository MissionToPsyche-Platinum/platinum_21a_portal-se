<script>
import { isAdmin } from "@/utils/auth";

export default {
    name: "PendingUploadRequests",
    data() {
      return {
        requests: [],
        message: "",
        loading: false,
        processingId: null
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
          this.loading = true;

          try {
                const response = await fetch("http://localhost:8080/api/game-upload-requests/pending", {
                    headers: this.adminHeaders()
                });

                if (!response.ok) {
                  this.requests = [];
                  this.message = "Could not load pending requests.";
                    return;
                }

                this.requests = await response.json();
            } catch {
                this.requests = [];
                this.message = "Could not reach the server.";
            }finally {
            this.loading = false;
          }
        },
        async updateRequest(id, action) {
          if (this.processingId !== null) return;

          this.processingId = id;
          this.message = "";

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
            } finally {
              this.processingId = null;
            }
        }
    }
};
</script>


<template>
  <section class="requests-panel" aria-labelledby="requests-title">
    <div class="requests-header">
      <div>
        <span class="eyebrow">GAME MANAGEMENT</span>
        <h3 id="requests-title">Pending Upload Requests</h3>
        <p class="section-description">
          Review submitted games before they are added
          to the Psyche Portal.
        </p>
      </div>

      <span class="request-count">
        {{ requests.length }} Pending
      </span>
    </div>

    <p v-if="message" class="feedback error" role="alert">
      {{ message }}
    </p>

    <div v-if="loading" class="feedback" role="status">
      Loading pending requests...
    </div>

    <div
        v-else-if="requests.length === 0 && !message"
        class="empty-state"
    >
      <span class="empty-icon" aria-hidden="true">✓</span>
      <h4>All caught up!</h4>
      <p>There are no pending game upload requests.</p>
    </div>

    <div v-else class="requests-list">
      <article
          v-for="request in requests"
          :key="request.requestId"
          class="request-card"
      >
        <div class="request-information">
          <div class="request-title-row">
            <h4>
              {{ request.game?.title || "Untitled game" }}
            </h4>

            <span class="status-badge">
              Pending
            </span>
          </div>

          <p class="request-meta">
            Submitted by
            <strong>
              {{ request.submittedBy?.username || "Unknown user" }}
            </strong>
          </p>

          <p class="request-id">
            Request #{{ request.requestId }}
          </p>
        </div>

        <div class="request-actions">
          <button
              type="button"
              class="action-button approve-button"
              :disabled="processingId !== null || loading"
              @click="updateRequest(request.requestId, 'approve')"
          >
            {{
              processingId === request.requestId
                  ? "Processing..."
                  : "Approve"
            }}
          </button>

          <button
              type="button"
              class="action-button deny-button"
              :disabled="processingId !== null || loading"
              @click="updateRequest(request.requestId, 'deny')"
          >
            {{
              processingId === request.requestId
                  ? "Processing..."
                  : "Deny"
            }}
          </button>
        </div>
      </article>
    </div>
  </section>
</template>



<style scoped>
.requests-panel {
  width: 100%;
  min-width: 0;
  color: inherit;
}

.requests-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 26px;
}

.eyebrow {
  display: block;
  margin-bottom: 10px;
  font-size: 0.7rem;
  font-weight: 800;
  letter-spacing: 0.14em;
  opacity: 0.7;
}

.requests-header h3 {
  margin: 0;
  font-size: 1.5rem;
}

.section-description {
  margin: 10px 0 0;
  font-size: 0.92rem;
  line-height: 1.6;
  opacity: 0.75;
}

.request-count {
  padding: 8px 14px;
  border-radius: 999px;
  font-size: 0.8rem;
  font-weight: 700;
  white-space: nowrap;
  background: rgba(139, 116, 235, 0.15);
  border: 1px solid rgba(139, 116, 235, 0.4);
}

.requests-list {
  display: grid;
  gap: 16px;
}

.request-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
  min-width: 0;
  padding: 24px;
  border-radius: 16px;
  background: rgba(127, 127, 127, 0.075);
  border: 1px solid rgba(128, 128, 128, 0.3);
  backdrop-filter: blur(8px);
  transition:
      border-color 0.2s ease,
      box-shadow 0.2s ease,
      transform 0.2s ease;
}

.request-card:hover {
  transform: translateY(-2px);
  border-color: rgba(139, 116, 235, 0.6);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.request-information {
  flex: 1;
  min-width: 0;
}

.request-title-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.request-title-row h4 {
  margin: 0;
  font-size: 1.12rem;
  overflow-wrap: anywhere;
}

.status-badge {
  padding: 5px 11px;
  border-radius: 999px;
  background: rgba(245, 158, 11, 0.13);
  border: 1px solid rgba(245, 158, 11, 0.5);
  color: #d97706;
  font-size: 0.72rem;
  font-weight: 700;
}

.request-meta {
  margin: 12px 0 0;
  font-size: 0.88rem;
  opacity: 0.85;
  overflow-wrap: anywhere;
}

.request-id {
  margin: 7px 0 0;
  font-size: 0.76rem;
  opacity: 0.6;
}

.request-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.action-button {
  min-width: 100px;
  min-height: 44px;
  padding: 11px 18px;
  border-radius: 10px;
  border: 1px solid transparent;
  font-size: 0.88rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s ease;
}

.approve-button {
  color: #fff;
  background: #237a4b;
}

.approve-button:hover:not(:disabled) {
  background: #1a603a;
}

.deny-button {
  color: #fff;
  background: #a83232;
}

.deny-button:hover:not(:disabled) {
  background: #862626;
}

.action-button:focus-visible {
  outline: 3px solid #8f7aff;
  outline-offset: 3px;
}

.action-button:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.feedback {
  padding: 18px;
  border-radius: 12px;
  background: rgba(127, 127, 127, 0.08);
}

.error {
  border: 1px solid rgba(220, 60, 60, 0.5);
  color: #e57373;
}

.empty-state {
  padding: 48px 24px;
  text-align: center;
  border-radius: 16px;
  background: rgba(127, 127, 127, 0.06);
  border: 1px dashed rgba(128, 128, 128, 0.4);
}

.empty-icon {
  display: inline-grid;
  place-items: center;
  width: 48px;
  height: 48px;
  margin-bottom: 12px;
  border-radius: 50%;
  background: rgba(35, 122, 75, 0.15);
  font-size: 1.5rem;
}

.empty-state h4 {
  margin: 0 0 8px;
}

.empty-state p {
  margin: 0;
  opacity: 0.7;
}

@media (max-width: 720px) {
  .request-card {
    flex-direction: column;
    align-items: stretch;
    padding: 18px;
  }

  .request-actions {
    width: 100%;
  }

  .action-button {
    flex: 1;
  }
}

@media (prefers-reduced-motion: reduce) {
  .request-card,
  .action-button {
    transition: none;
  }

  .request-card:hover {
    transform: none;
  }
}
</style>

