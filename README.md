
##**PII REPORT**

This repository contains a report on the fields used in com.citi.cpb and annotations implemented on them.

---

## Description

All the classes from com.citi.cpb package were extracted and by using reflections all the declared fields were extracted from each class.
Using OpenCSV the output was pushed to a .csv file (excel).

---

## Dependencies Added to pom.xml

1. Reflections
2. OpenCSV

---

## Output Format

The data is present under the column names as Class, Field and Annotation(if applied to that field).
