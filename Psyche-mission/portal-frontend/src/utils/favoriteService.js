import { getSession } from "./auth";

const FAVORITES_URL = "http://localhost:8080/api/favorites";

export const saveFavoritesToServer = (favorites) => {
    const session = getSession();

    if (!session) {
        return;
    }

    fetch(`${FAVORITES_URL}/${encodeURIComponent(session.username)}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(favorites)
    }).catch(() => {});
};

export const loadFavoritesFromServer = async () => {
    const session = getSession();

    if (!session) {
        return null;
    }

    try {
        const response = await fetch(`${FAVORITES_URL}/${encodeURIComponent(session.username)}`);

        if (!response.ok) {
            return null;
        }

        const gameIds = await response.json();
        localStorage.setItem("favoriteGames", JSON.stringify(gameIds));
        window.dispatchEvent(new Event("favorites-updated"));
        return gameIds;
    } catch {
        return null;
    }
};
