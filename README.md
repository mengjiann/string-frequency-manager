# String Frequency Manager
- A Spring Boot application that monitors log files for past 24 hours in a predefined local directory.
- Return true if the string appears for more than 5 times in the database.

# Instruction
- Clone the project to your local machine.
- Open Terminal and navigate to the root directory of the project: `mvn clean package docker:build`
- When the image is built successfully, run the following in your Terminal

`docker run -v <LOCAL DIRECTORY CONTAINING THE LOG FILES>:/data -it -d -p 8085:8080 -e SPRING_APPLICATION_JSON='{"spring": {"profiles": {"active": "docker"}}}' --name string-frequency-manager string-frequency-manager`

- After that, access the site at `http://localhost:8085/isStringValid?string=<STRING_ID> `

# Requirements
- Upon the zero-th minute of every hour, a new log file that contains the data in the last hour will be generated and be placed into the same folder, the file name format is string-generation-{yyyymmddhh}.log​.
- A periodic logic flow that process new log files as stated above
- Available log should be loaded for the past 24 hours
- API endpoint is provided to check if the string appears for more than 5 times.
- No limit on the log file size.

# Assumptions
- The log file will be placed in the zero-th minute of every hour. It will be available for scanning at the next second and will not change after that.
- The log file might contain same string that appears more than once for the same timestamp
- If the log file contains record that have timestamp not in the previous hour, the record will be discarded.
- For demo purpose, the past 24 hours data in the database will be discarded when the app is shut down. It will load the log files for the past 24 hours from the specified directory when the app boots up.
- Design for high availability and scalability is not required.
 
# Prerequisite
- Docker 
- Apache Maven
