import gameData from "../assets/games.json"

export const getSuggestions = (gameId) => {
    const game = gameData.games.find(g => g.id === gameId);
    let categories = ["difficulty", "age", "genre", "class"];

    const suggestionCount = 3;
    let suggestions = [];
    for (let i = 0; i < suggestionCount; i++) {
        let category = categories[Math.floor(Math.random() * categories.length)]; // Get random category
        let matches = gameData.games.filter(g => (g[category] === game[category] && g.id !== game.id)); // Find matches for that category
        let suggestedGame = null;
        let gameFound = false;

        if (matches.length != 0) {
            gameFound = true;
            suggestedGame = matches[Math.floor(Math.random() * matches.length)];

            // continue searching for a game if suggestedGame is already in the list of suggestions
            while (suggestions.includes(suggestedGame)) {
                // Remove the already existing game from the potential matches
                matches.splice(matches.indexOf(suggestedGame), 1);
                // If there are no more potential matches, exit loop and remove category from potential matches
                if (matches.length === 0) {
                    gameFound = false;
                    break;
                }
                suggestedGame = matches[Math.floor(Math.random() * matches.length)];
            }
        }
        
        if (gameFound) {
            suggestions.push(suggestedGame); // Add random game with matching category
        } else {
            // remove category if there are no matches in that category
            categories.splice(categories.indexOf(category), 1);
            // exit loop if there are no categories left to check
            if (categories.length === 0) {
                break;
            }
            // add an extra loop iteration if category had no matches
            i--;
        }
    }

    return suggestions;
}