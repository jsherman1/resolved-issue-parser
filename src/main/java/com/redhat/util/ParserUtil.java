package com.redhat.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;

public class ParserUtil {

    public static String addTable(boolean includeComponent)
    {
        return "ID |" + (includeComponent ? " Component |" : "") + " Summary \n -- |" + (includeComponent ? " ------- |" : "") + " -------";
    }

    public static String addStyle()
    {
        return "<style>\n"
                + "table tr th, td {\n"
                + "font-size: 12px; font-family: monospace; text-align: left;\n"
                + "border-spacing: 2px;\n"
                + "padding-top:5px !important;\n"
                + "padding-bottom:5px !important;\n"
                + "padding-left:10px !important;\n"
                + "padding-right:10px !important;\n"
                + "line-height: normal;\n"
                + "}\n"
                + "\n"
                + "table tbody tr th, td {\n"
                + "font-size: 12px; font-family: monospace; text-align: left; border-spacing: 2px;\n"
                + "padding-top:5px !important;\n"
                + "padding-bottom:5px !important;\n"
                + "padding-left:10px !important;\n"
                + "padding-right:10px !important;\n"
                + "line-height: normal;\n"
                + "}\n"
                + "\n"
                + "td a *:first-child { white-space: nowrap !important; }\n"
                + "\n"
                + "</style>\n\n";
    }

    public static String processLine(String line, String component, boolean includeComponent)
    {
        String jira = "";
        String link = "";
        String description = "";

        link = line.substring(0, line.indexOf(" "));
        jira = link.substring(link.lastIndexOf("/") + 1, link.length());
        description = line.substring(line.indexOf("- ") + 2, line.length());

        //System.out.println("[" + link + "]");
        //System.out.println("[" + jira + "]");
        //System.out.println("[" + description + "]");

        return "\n[" + jira + "](" + link + ")" + (includeComponent ? " | " + component + " | " : " | ") + description;
    }

    public static void write(URL fileURL, StringBuffer sb, int processed)
    {
        String outputFile = "resolvedIssuesOutput.txt";

        // write sb to a file
        try {

            String path = fileURL.getPath();
            path = path.substring(0, path.lastIndexOf("/") + 1);
            System.out.println("Writing file to: " + path + outputFile);

            BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(path + outputFile)));
            //write contents of StringBuffer to a file
            bwr.write(sb.toString());

            //flush the stream
            bwr.flush();

            //close the stream
            bwr.close();

            System.out.println("Processed " + processed + " issue" + (processed > 1 ? "s" : ""));
        }
        catch(Exception e)
        {
            System.out.println("Exception writing file " + e);
        }
    }
}
