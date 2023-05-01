package com.redhat.parser;

import com.redhat.util.ParserUtil;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class TextParser {

    public void parse(URL fileURL, boolean includeComponent, boolean addStyle) {

        boolean beginningSet = false;
        int processed = 0;
        String component = "";

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(fileURL.toURI())));

            String line;
            StringBuffer sb = new StringBuffer();

            while ((line = br.readLine()) != null) {
                // validate data contains expected input
                if (line.startsWith("Component:") ||
                        line.startsWith("https:")) {
                    if (!beginningSet) {
                        System.out.println("Gathering list of resolved issues...");

                        if(addStyle) {
                            sb.append(ParserUtil.addStyle());
                        }
                        /* There might be some issues that include a component and others that do not
                        / because of this, we cannot rely on auto-determining this based on the data
                        / as the table headed has been loaded into the buffer before all issues are
                        / processed
                         */
                        sb.append(ParserUtil.addTable(includeComponent));
                        beginningSet = true;
                    }
                }

                // gather the list
                if (beginningSet) {

                    if (line.contains("Component:")) {
                        // check if a component name was provided
                        int componentIndex = line.indexOf(":") + 2;
                        if (componentIndex < line.length()) {
                            // get the affected component
                            component = line.substring(componentIndex);
                        }
                    }

                    if (line.contains("https:")) {
                        // get the issue
                        sb.append(ParserUtil.processLine(line, component.trim(), includeComponent));
                        processed++;
                    }
                }
            }
            // close the buffered reader
            br.close();

            if (processed > 0) {
                ParserUtil.write(fileURL, sb, processed);
            } else {
                System.out.println("No issues were found");
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
