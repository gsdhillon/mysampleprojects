-- On 31/08/2013
-- By Gurmeet Singh
SELECT 
    NAME,               -- VARCHAR(45)
    DESIG,              -- VARCHAR(45)
    DIVISION,           -- VARCHAR(45)
    PASSWORD            -- VARCHAR(45)
FROM  
    USERS
WHERE  
    USER_ID = #USER_ID# -- NUMBER(10)