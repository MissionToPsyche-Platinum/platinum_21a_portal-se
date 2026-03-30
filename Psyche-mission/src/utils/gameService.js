import gameData from "../assets/games.json"

export const getSuggestions = (gameId) => {
    const game = gameData.games.find(game => game.id == gameId);
    let categories = ["difficulty", "age", "genre", "class"];

    const suggestionCount = 3;
    let suggestions = [];
    for (let i = 0; i < suggestionCount; i++) {
        let category = categories[Math.floor(Math.random() * categories.length)]; // Get random category
        let matches = gameData.games.filter(g => (g[category] === game[category] && g.id !== game.id)); // Find matches for that category
        if (matches.length != 0) {
            // TODO: Need to test if randomly chosen game is already in suggestions array before pushing to suggestions.
            suggestions.push(matches[Math.floor(Math.random() * matches.length)]); // Add random game with matching category
        } else {
            // remove category if there are no matches in that category
            categories.splice(categories.indexOf(category), 1);
            // exit loop if there are no matching categories
            if (categories.length === 0) {
                break;
            }
            // add an extra loop iteration if previous category had no matches
            i--;
        }
    }

    return suggestions;
}