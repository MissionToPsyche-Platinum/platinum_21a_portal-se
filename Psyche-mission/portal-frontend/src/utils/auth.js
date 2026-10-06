const SESSION_KEY = "userSession";

const isValidRole = (role) => role === "user" || role === "admin";

export const getSession = () => {
    const savedSession = localStorage.getItem(SESSION_KEY);

    if (!savedSession) {
        return null;
    }

    try {
        const session = JSON.parse(savedSession);

        if (!session.username || !isValidRole(session.role)) {
            return null;
        }

        return {
            username: session.username,
            role: session.role
        };
    } catch {
        return null;
    }
};

export const setSession = (session) => {
    if (!session || !session.username || !isValidRole(session.role)) {
        return;
    }

    localStorage.setItem(SESSION_KEY, JSON.stringify({
        username: session.username,
        role: session.role
    }));
    window.dispatchEvent(new Event("session-changed"));
};

export const clearSession = () => {
    localStorage.removeItem(SESSION_KEY);
    window.dispatchEvent(new Event("session-changed"));
};

export const isLoggedIn = () => {
    return getSession() !== null;
};

export const isAdmin = () => {
    const session = getSession();
    return session !== null && session.role === "admin";
};
