-- On 31/08/2013
-- By Gurmeet Singh
SELECT 
    DATE_FORMAT(date_of_attendance, '%d') DAY,  -- VARCHAR
    DATE_FORMAT(in_time, '%y-%m-%d#%H:%i:%S') TIME_IN, -- VARCHAR
    DATE_FORMAT(out_time, '%y-%m-%d#%H:%i:%S') TIME_OUT, -- VARCHAR
    late_arrival LATE, -- VARCHAR
    early_departure EALRY, -- VARCHAR
    STATUS  -- VARCHAR
FROM 
        attendance 
WHERE 
        user_id = #USERID# AND  -- NUMBBER
        DATE_FORMAT(date_of_attendance, '%m%y') = #MMYY# -- VARCHAR
ORDER BY 
        DAY 