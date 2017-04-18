APP FOR SEARCHING PATIENT DOCUMENTS

Java Files Directory: src/main/java/com/health/app/search/service/ 

-PatientService.java
-searchAppFunctions.java

Code Summary:

-Created a searchAppFunctions.java class that is inherited by the PatientService.java class. 
-The searchAppFunctions.java class encapsulates methods for instantiating new people, retrieving patient documents, as well as a "replace" method for case-insensitivity of strings within a array.
-Added additional methods for tokenizing documents (removing punctuation, normalization to lower case), processing the information contained within the patient documents, and creating an inverted index (token -> List<patient>) HashMap for performing boolean searches.

Testing:

-joe_testperson -> retrieves all information related to Joe (same for Mary and Sam).
-pregnancy -> retrieves all information related to Mary.
-illness -> retrieves all information related to Joe and Sam.

*The javascript ui search truncates the last character of the query string (checked the main.js javascript file to see if I could locate the error). For example, typing “pregnancy” would result in a query for “pregnanc.” As I implemented an inverted index, an exact key match is required. Hack fix was querying for “pregnancyy.”




