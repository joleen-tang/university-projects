SET ECHO ON

SPOOL week9_dml_output.txt
--28796527 Joleen Tang
--FIT2094 Tutorial 10

--Update tasks
--Task 1
--Update the unit name of FIT9999 from 'FIT Last Unit' to 'place holder unit'.
UPDATE unit
SET
    unit_name = 'FIT Last Unit'
WHERE
    unit_code = 'FIT9999';

--Task 2
--Enter the mark and grade for the student with the student number of 11111113 for the unit
--code FIT5132 that the student enrolled in semester 2 of 2014. The mark is 75 and the grade
--is D.
UPDATE enrolment
SET
    enrol_mark = 75,
    enrol_grade = 'D'
WHERE
        stu_nbr = 11111113
    AND unit_code = 'FIT5132'
    AND enrol_semester = 2
    AND enrol_year = 2014;

--Task 3
--The university has introduced a new grade classification scale. The new classifications are:
--1. 45 - 54 is P1.
--2. 55 - 64 is P2.
--3. 65 - 74 is C.
--4. 75 - 84 is D.
--5. 85 - 100 is HD.
--Change the database to reflect the new grade classification scale.
UPDATE enrolment
SET
    enrol_grade = 'P1'
WHERE
    enrol_mark BETWEEN 45 AND 54;

UPDATE enrolment
SET
    enrol_grade = 'P2'
WHERE
    enrol_mark BETWEEN 55 AND 64;

UPDATE enrolment
SET
    enrol_grade = 'C'
WHERE
    enrol_mark BETWEEN 65 AND 74;

UPDATE enrolment
SET
    enrol_grade = 'D'
WHERE
    enrol_mark BETWEEN 75 AND 84;

UPDATE enrolment
SET
    enrol_mark = 'HD'
WHERE
    enrol_mark BETWEEN 85 AND 100;

--Delete tasks
--Task 1
--A student with student number 11111114 has taken intermission in semester 2 2014, hence
--all the enrolments of this student for semester 2 2014 should be removed. Change the
--database to reflect this situation.

DELETE FROM enrolment
WHERE
    stu_nbr = 11111114;

--Task 2 and 3
--Assume that Wendy Wheat (student number 11111113) has withdrawn from the university.
--Remove her details from the database.
--Add Wendy Wheat back to the database (use the INSERT statements you have created
--when completing module Tutorial 7 SQL Data Definition Language DDL).

ALTER TABLE enrolment DROP CONSTRAINT enrol_stu_fk;

ALTER TABLE enrolment
    ADD CONSTRAINT enrol_stu_fk FOREIGN KEY ( stu_nbr )
        REFERENCES student ( stu_nbr )
            ON DELETE CASCADE;

--not allowed to remove without specifying the ON DELETE CASCADE constraint
DELETE FROM student
WHERE
    stu_nbr = 11111113;
--after adding ON DELETE CASCADE constraint and deleting, all enrolments for Wendy are deleted
--when Wendy is deleted from student
SPOOL OFF

SET ECHO OFF