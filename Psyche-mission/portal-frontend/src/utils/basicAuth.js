let basicAuthHeader = null

export const setBasicAuth = (username, password) => {
    const credentials = btoa(`${username}:${password}`);

    basicAuthHeader = `Basic ${credentials}`;
}
export const getBasicAuthHeader = () => basicAuthHeader;
export const clearAuthHeader = () => basicAuthHeader = null;