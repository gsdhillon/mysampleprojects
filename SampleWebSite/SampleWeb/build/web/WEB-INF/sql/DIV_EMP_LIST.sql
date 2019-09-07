-- On 31/08/2013
-- By Gurmeet Singh
SELECT 
    USER_ID,            -- NUMBER
    NAME,               -- VARCHAR(45)
    DESIG               -- VARCHAR(45)
FROM  
    USERS
WHERE  
  DIVISION = #DIVISION#   -- VARCHAR(45)