--add set echo and spool command here
SET ECHO ON

SPOOL week10_sql_intermediate_output.txt

/*
Databases Week 10 Tutorial
week10_sql_intermediate.sql

student id: 28796527
student name: Joleen Tang
last modified date: 28-10-2020

*/

/* 1. Find the average mark of FIT2094 in semester 2, 2019. 
Show the average mark with two decimal places. 
Name the output column as â€œAverage Markâ€?. */
SELECT
    to_char(AVG(mark), '990.99') AS "Average Mark"
FROM
    uni.enrolment
WHERE
        semester = 2
    AND to_char(ofyear, 'yyyy') = '2019'
    AND unitcode = 'FIT2094';


/* 2. List the average mark for each offering of FIT9136. 
In the listing, include the year and semester number. 
Sort the result according to the year then the semester.*/
SELECT
    AVG(mark) AS "Average Mark",
    ofyear,
    semester
FROM
    uni.enrolment
WHERE
    unitcode = 'FIT9136'
GROUP BY
    ofyear,
    semester
ORDER BY
    ofyear,
    semester;


/* 3. Find the number of students enrolled in FIT1045 in the year 2019, 
under the following conditions:
      a. Repeat students are counted each time
      b. Repeat students are only counted once
*/

-- a. Repeat students are counted each time
SELECT
    COUNT(studid) AS "Number of Students Enrolled"
FROM
    uni.enrolment
WHERE
        to_char(ofyear, 'yyyy') = '2019'
    AND unitcode = 'FIT1045';

  
-- b. Repeat students are only counted once
SELECT
    COUNT(DISTINCT studid) AS "Number of Students Enrolled"
FROM
    uni.enrolment
WHERE
        to_char(ofyear, 'yyyy') = '2019'
    AND unitcode = 'FIT1045';


/* 4. Find the total number of prerequisite units for FIT5145. */
SELECT
    COUNT(has_prereq_of)
FROM
    uni.prereq
WHERE
    unitcode = 'FIT5145';  
  
/* 5. Find the total number of prerequisite units for each unit. 
In the list, include the unitcode for which the count is applicable. 
Order the list by unit code.*/
SELECT
    unitcode,
    COUNT(has_prereq_of)
FROM
    uni.prereq
GROUP BY
    unitcode
ORDER BY
    unitcode;


/*6. Find the total number of students 
whose marks are being withheld (grade is recorded as 'WH') 
for each unit offered in semester 1 2020. 
In the listing include the unit code for which the count is applicable. 
Sort the list by descending order of the total number of students 
whose marks are being withheld.*/
SELECT
    COUNT(grade) AS "Number of Students with Withheld Marks",
    unitcode
FROM
    uni.enrolment
WHERE
        grade = 'WH'
    AND semester = 1
    AND to_char(ofyear, 'yyyy') = '2020'
GROUP BY
    unitcode
ORDER BY
    "Number of Students with Withheld Marks" DESC;

/* 7. For each prerequisite unit, calculate how many times 
it has been used as a prerequisite (number of times used). 
In the listing include the prerequisite unit code, 
the prerequisite unit name and the number of times used. 
Sort the output by unit name. */
SELECT
    p.has_prereq_of,
    u.unitname,
    COUNT(*) AS "Number of Times Used"
FROM
         uni.prereq p
    JOIN uni.unit u ON p.has_prereq_of = u.unitcode
GROUP BY
    p.has_prereq_of,
    u.unitname
ORDER BY
    u.unitname;

/*8. Display unit code and unit name of units 
which had at least 1 student who was granted deferred exam 
(grade is recorded as 'DEF') in semester 1 2020. 
Order the list by unit code.*/
SELECT
    e.unitcode,
    u.unitname
FROM
         uni.enrolment e
    JOIN uni.unit u ON e.unitcode = u.unitcode
WHERE
        grade = 'DEF'
    AND semester = 1
    AND to_char(ofyear, 'yyyy') = '2020'
GROUP BY
    e.unitcode,
    u.unitname
HAVING
    COUNT(studid) >= 2
ORDER BY
    unitcode;

/* 9. Find the unit/s with the highest number of enrolments 
for each offering in the year 2019. 
Sort the list by semester then by unit code. */
SELECT
    unitcode,
    semester
FROM
    uni.enrolment
WHERE
    to_char(ofyear, 'yyyy') = '2019'
GROUP BY
    semester,
    unitcode
HAVING
    COUNT(studid) = (
        SELECT
            MAX(COUNT(studid))
        FROM
            uni.enrolment
        WHERE
            to_char(ofyear, 'yyyy') = '2019'
        GROUP BY
            semester,
            unitcode
    )
ORDER BY
    semester,
    unitcode;

/* 10. Find all students enrolled in FIT3157 in semester 1, 2020 
who have scored more than the average mark for FIT3157 in the same offering? 
Display the students' name and the mark. 
Sort the list in the order of the mark from the highest to the lowest 
then in increasing order of student name.*/
SELECT
    s.studfname
    || ' '
    || s.studlname AS studname,
    e.mark
FROM
         uni.enrolment e
    NATURAL JOIN uni.student s
WHERE
        semester = 1
    AND to_char(ofyear, 'yyyy') = '2020'
    AND e.mark >= (
        SELECT
            AVG(mark)
        FROM
            uni.enrolment
        WHERE
                semester = 1
            AND to_char(ofyear, 'yyyy') = '2020'
            AND unitcode = e.unitcode
    )
    ORDER BY e.mark DESC, studname;

SPOOL off set echo off