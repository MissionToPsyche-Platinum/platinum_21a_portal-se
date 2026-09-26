/* Psyche Portal PostgreSQL Schema Validation Test*/

/*Verify that the games table exists.*/
SELECT EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_name = 'games'
) AS games_table_exists;

 /*Verify the games table columns and data types.*/
SELECT
    column_name,
    data_type,
    is_nullable
FROM information_schema.columnsq
WHERE table_schema = 'public'
  AND table_name = 'games'
ORDER BY ordinal_position;