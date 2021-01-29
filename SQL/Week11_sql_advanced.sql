--add set echo and spool command here
SET ECHO ON

SPOOL week11_sql_advanced_output.txt


/*
Databases Week 11 Tutorial
week11_sql_advanced.sql

student id: 28796527
student name: Joleen Tang
last modified date: 1-11-2020

*/

/* 1. Find the number of scheduled classes assigned to each staff member 
for each semester in 2019. If the number of classes is 2 
then this should be labelled as a correct load, 
more than 2 as an overload and less than 2 as an underload. 
Order the list by decreasing order of the number of scheduled classes 
and when the number of classes is the same order by increasing order of staff id. */ 
SELECT
    staffid,
    semester,
    CASE
        WHEN COUNT(classno) > 2     THEN
            'Overload'
        WHEN COUNT(classno) < 2     THEN
            'Underload'
        ELSE
            'Correct Load'
    END load
FROM
    uni.schedclass
WHERE
    to_char(ofyear, 'yyyy') = '2019'
GROUP BY
    staffid,
    semester
ORDER BY
    COUNT(classno) DESC,
    staffid;

/* 2. Display the unit code and unit name for units that do not have a prerequisite. 
Order the list in increasing order of unit code. 

There are many approaches that you can take in writing an SQL statement to answer this query. 
You can use the SET OPERATORS, OUTER JOIN and a SUBQUERY. 
Write SQL statements based on all three approaches.*/

/* Using outer join */
SELECT
    u.unitcode,
    u.unitname
FROM
    uni.unit      u
    LEFT OUTER JOIN uni.prereq    p ON u.unitcode = p.unitcode
WHERE
    p.has_prereq_of IS NULL
ORDER BY
    u.unitcode;

/* Using set operator MINUS */
--MINUS first and then select name?
SELECT
    u1.unitcode,
    u1.unitname
FROM
    uni.unit      u1
    LEFT OUTER JOIN uni.prereq    p1 ON u1.unitcode = p1.unitcode
MINUS
SELECT
    p2.unitcode,
    u2.unitname
FROM
         uni.prereq p2
    JOIN uni.unit u2 ON p2.unitcode = u2.unitcode
ORDER BY
    unitcode;


/* Using subquery */
SELECT
    unitcode,
    unitname
FROM
    uni.unit
WHERE
    unitcode NOT IN (
        SELECT
            unitcode
        FROM
            uni.prereq
    )
ORDER BY
    unitcode;


/* 3. List all units offered in semester 2 2019 which do not have any enrolment. 
Include the unit code, unit name, and the chief examiner's name in the list. 
Order the list based on the unit code. */

--It says "SQL command not properly ended", not sure what's wrong
SELECT
    o.unitcode,
    o.unitname,
    s.stafffname
    || ' '
    || s.stafflname AS staffname
FROM
    (
             uni.offering
        NATURAL JOIN uni.unit
    ) o
    JOIN uni.staff s ON o.chiefexam = s.staffid
WHERE
        semester = 2
    AND to_char(ofyear, 'yyyy') = '2019'
    AND o.unitcode NOT IN (
        SELECT DISTINCT
            unitcode
        FROM
            uni.enrolment
    )
ORDER BY
    unitcode;


/* 4. List the full names of students who are enrolled in both ‘Introduction to databases’ 
and ‘Introduction to computer architecture and networks’ (note: both unit names are unique)
in semester 1 2020. Order the list by the students’ full name.*/
SELECT
    studfname
    || ' '
    || studlname AS fullname
FROM
    uni.student
WHERE
    studid IN (
        SELECT
            studid
        FROM
            uni.enrolment
        WHERE
                unitcode = (
                    SELECT
                        unitcode
                    FROM
                        uni.unit
                    WHERE
                        unitname = 'Introduction to databases'
                )
            AND semester = 1
            AND to_char(ofyear, 'yyyy') = '2020'
    )
    AND studid IN (
        SELECT
            studid
        FROM
            uni.enrolment
        WHERE
                unitcode = (
                    SELECT
                        unitcode
                    FROM
                        uni.unit
                    WHERE
                        unitname = 'Introduction to computer architecture and networks'
                )
            AND semester = 1
            AND to_char(ofyear, 'yyyy') = '2020'
    )
ORDER BY
    fullname;

/* 5. Given that the payment rate for a tutorial is $42.85 per hour 
and the payment rate for a lecture is $75.60 per hour, 
calculate the weekly payment per type of class for each staff member in semester 1 2020. 
In the display, include staff id, staff name, type of class (lecture or tutorial), 
number of classes, number of hours (total duration), 
and weekly payment (number of hours * payment rate). 
Order the list by increasing order of staff id and for a given staff id by type of class.*/
SELECT
    staffid,
    stafffname
    || ' '
    || stafflname        AS staffname,
    (
        CASE cltype
            WHEN 'L'  THEN
                'Lecture'
            WHEN 'T'  THEN
                'Tutorial'
        END
    )                    AS type,
    COUNT(classno)       AS no_of_classes,
    SUM(clduration)      AS total_hours,
    to_char(SUM((
        CASE cltype
            WHEN 'L'  THEN
                75.60
            WHEN 'T'  THEN
                42.85
        END
    ) * clduration),
            '$990.00')   AS "Weekly Payment"
FROM
         uni.staff
    NATURAL JOIN uni.schedclass
WHERE
        semester = 1
    AND to_char(ofyear, 'yyyy') = '2020'
GROUP BY
    staffid,
    stafffname
    || ' '
    || stafflname,
    cltype
ORDER BY
    staffid,
    cltype;
    
/* 6. Given that the payment rate for a tutorial is $42.85 per hour 
and the payment rate for a lecture is $75.60 per hour, 
calculate the total weekly payment (the sum of both tutorial and lecture payments) 
for each staff member in semester 1 2020. 
In the display, include staff id, staff name, total weekly payment for tutorials, 
total weekly payment for lectures and the total weekly payment. 
If the payment is null, show it as $0.00. 
Order the list by increasing order of staff id.*/
SELECT
    staffid,
    stafffname
    || ' '
    || stafflname        AS staffname,
    to_char(SUM(nvl(
        CASE cltype
            WHEN 'T' THEN
                clduration
        END, 0) * 42.85),
            '$990.00')   AS tutorial_payment,
    to_char(SUM(nvl(
        CASE cltype
            WHEN 'L' THEN
                clduration
        END, 0) * 75.60),
            '$990.00')   AS lecture_payment,
    to_char(SUM((
        CASE cltype
            WHEN 'L'  THEN
                75.60
            WHEN 'T'  THEN
                42.85
        END
    ) * clduration),
            '$990.00')   AS total_weekly_payment
FROM
         uni.staff
    NATURAL JOIN uni.schedclass
WHERE
        semester = 1
    AND to_char(ofyear, 'yyyy') = '2020'
GROUP BY
    staffid,
    stafffname
    || ' '
    || stafflname
ORDER BY
    staffid;

/* 7. Assume that all units are worth 6 credit points each, 
calculate each student’s Weighted Average Mark (WAM) and GPA. 
Please refer to these Monash websites: https://www.monash.edu/exams/results/wam 
and https://www.monash.edu/exams/results/gpa for more information about WAM and GPA respectively. 
Do not include WH or DEF grade in the calculation.


Include student id, student full name (in a 40 characters wide column headed “Student Full Name�?), 
WAM and GPA in the display. Order the list by descending order of WAM then descending order of GPA.
*/ 

--it says "not a single-group group function" but I included the attributes in the GROUP BY clause?
SELECT
    studid,
    RPAD(studfname
         || ' '
         || studlname, 40)     AS "Student Full Name",
    to_char(SUM((
        CASE
            WHEN substr(unitcode, 4, 1) = '1' THEN
                3
            ELSE
                6
        END
    ) * mark) /(SUM(
        CASE
            WHEN substr(unitcode, 4, 1) = '1' THEN
                3
            ELSE
                6
        END
    )),
            '90.00')      AS wam,
    to_char(SUM(AVG((
        CASE grade
            WHEN 'HD'  THEN
                4
            WHEN 'D'   THEN
                3
            WHEN 'C'   THEN
                2
            WHEN 'P'   THEN
                1
        END
    ))),
            '0.00')       AS gpa
FROM
         uni.student
    NATURAL JOIN uni.enrolment
GROUP BY
    studid,
    rpad(studfname
         || ' '
         || studlname, 40)
ORDER BY
    wam DESC,
    gpa;

SPOOL OFF

SET ECHO OFF