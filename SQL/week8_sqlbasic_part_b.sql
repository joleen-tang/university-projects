--add set echo and spool command here
SET ECHO ON

SPOOL week8_sqlbasic_part_b_output.txt

/*
Databases Week 8 Tutorial Sample Solution
week8_sqlbasic_part_b.sql

student id: 28796527
student name: Joleen Tang
last modified date: 5/10/2020
*/

/* B1. List all the unit codes, semester and name of the chief examiner 
for all the units that are offered in 2020. Order the output by unit code.*/
SELECT
    unitcode,
    semester,
    stafffname
    || ' '
    || stafflname AS name
FROM
         uni.offering o
    JOIN uni.staff s ON o.chiefexam = s.staffid
WHERE
    to_char(ofyear, 'yyyy') = '2020'
ORDER BY
    unitcode;


/* B2. List all the unit codes and the unit names and their year and semester offerings. 
Order the output by unit code then by offering year and semester. 
To display the date correctly in Oracle, you need to use the to_char function. 
For example, to_char(ofyear,'yyyy'). */

SELECT
    u.unitcode,
    unitname,
    to_char(ofyear, 'yyyy') AS year,
    semester
FROM
         uni.unit u
    JOIN uni.offering o ON u.unitcode = o.unitcode
ORDER BY
    u.unitcode,
    year,
    semester;
  

/* B3. List the unit code, semester, class type (lecture or tutorial), day and time 
for all units taught by Windham Ellard in 2020. 
Sort the list according to the unit code..*/

SELECT
    unitcode,
    semester,
    cltype,
    clday,
    cltime
FROM
         uni.schedclass c
    JOIN uni.staff s USING ( staffid )
WHERE
        stafffname = 'Windham'
    AND stafflname = 'Ellard'
    AND to_char(ofyear, 'yyyy') = '2020'
ORDER BY
    unitcode;

/* B4. Create a study statement for Friedrick Geist. 
A study statement contains unit code, unit name, semester and year study was attempted, 
the mark and grade. If the mark and grade is unknown, show the mark and grade as ‘N/A’. 
Sort the list by year, then by semester and unit code. */

SELECT
    unitcode,
    unitname,
    semester,
    to_char(ofyear, 'yyyy')           AS year,
    nvl(to_char(mark), 'N/A')         AS mark,
    nvl(grade, 'N/A')                 AS grade
FROM
    (
             uni.enrolment
        JOIN uni.unit USING ( unitcode )
    )
    JOIN uni.student USING ( studid )
WHERE
        studfname = 'Friedrick'
    AND studlname = 'Geist'
ORDER BY
    year,
    semester,
    unitcode;

/* B5. List the unit code and unit name of the prerequisite units 
of 'Introduction to data science' unit. 
Order the output by prerequisite unit code. */

SELECT
    u2.unitcode    AS prereq_code,
    u2.unitname    AS prereq_name
FROM
         uni.unit u1
    JOIN uni.prereq USING ( unitcode )
    JOIN uni.unit u2 ON has_prereq_of = u2.unitcode
WHERE
    u1.unitname = 'Introduction to data science'
ORDER BY
    prereq_code;


/* B6. Find all students (list their id, firstname and surname) 
who have received an HD for FIT2094 unit in semester 2 of 2019. 
Sort the list by student id. */
SELECT
    studid,
    studfname,
    studlname
FROM
         uni.student s
    JOIN uni.enrolment e USING ( studid )
WHERE
        grade = 'HD'
    AND unitcode = 'FIT2094'
    AND semester = 2
    AND to_char(ofyear, 'yyyy') = '2019'
ORDER BY
    studid;
  

/* B7.	List the student full name, and unit code for those students 
who have no mark in any unit in semester 1 of 2020. 
Sort the list by student full name. */

SELECT
    studfname
    || ' '
    || studlname AS fullname,
    unitcode
FROM
         uni.student s
    JOIN uni.enrolment e USING ( studid )
WHERE
    NOT ( to_char(ofyear, 'yyyy') = '2020'
          AND semester = 1
          AND mark IS NOT NULL )
ORDER BY
    fullname;

SPOOL OFF

SET ECHO OFF