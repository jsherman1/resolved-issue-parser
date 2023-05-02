# Setup

Create/copy the resolved-issues.xml file to the resources directory of this project.
The resolved-issues.xml file can also be located on the file system and passed in as an option.

# Building the project:
`
mvn clean install
`

# Options

fileName: name of file to be processed (default=resolved-issues.xml).  The full path to the file can be provided.  If only the file name is provided it will attempt to locate the file in the resources directory or the working directory.

includeComponent: Option to include/exclude component column (default: true)

addStyle: Option to include/exclude markup style (default: false)

# Data File Options
The parser can handle text or XML data.  

Text format:
See the resolved-issues-example.xml provided in the resources directory for the expected data format. This file needs to be curated from a data export. The Component headers are optional and can be used to identify what component the fix targets.

XML format: XML can be obtained from exporting data from Jira using the Export XML option.  The data can be further filtered by setting the following parameters on the URL: field=link&field=summary.
See the resolved-issues-example.xml file provided in the resources directory.

# Running the project:
```
mvn exec:java -Dexec.mainClass="com.redhat.IssueParser" -DfileName=resolved-issues.txt -DincludeComponent=true -DaddStyle=true
