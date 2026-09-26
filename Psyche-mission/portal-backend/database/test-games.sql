/*Psyche Portal PostgreSQL Game Data Validation Test*/

/*Total number of games.*/
SELECT COUNT(*) AS total_games
FROM games;

/*Find games with missing titles.*/
SELECT id, title
FROM games
WHERE title IS NULL
   OR TRIM(title) = '';

/*Find duplicate game titles.*/
SELECT
    title,
    COUNT(*) AS duplicate_count
FROM games
WHERE title IS NOT NULL
GROUP BY title
HAVING COUNT(*) > 1
ORDER BY duplicate_count DESC;

/*Find games with missing source URLs.*/
SELECT id, title, src
FROM games
WHERE src IS NULL
   OR TRIM(src) = '';