/**
 * Author: Jason Sherman
 */
package com.redhat;

import com.redhat.parser.TextParser;
import com.redhat.parser.XMLParser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * IssueParser: Used to parse resolved Jira issues and format output for a KCS article
 *
 */
public class IssueParser
{
    public static void main( String[] args ) throws MalformedURLException{
        String ISSUES_FILE = System.getProperty("fileName", "resolved-issues.xml");
        boolean includeComponent = Boolean.parseBoolean(System.getProperty("includeComponent", "true"));
        boolean addStyle = Boolean.parseBoolean(System.getProperty("addStyle", "false"));

        System.out.println("Using file: " + ISSUES_FILE);
        System.out.println("Include Component: " + includeComponent);
        System.out.println("Add Style: " + addStyle);

        IssueParser ip = new IssueParser();
        URL fileURL = ip.getClass().getResource("/" + ISSUES_FILE);
        if (fileURL == null) {
            //attempt to read the file from the file system
            Path path = Paths.get(ISSUES_FILE);
            if(Files.exists(path)) {
                fileURL = path.toUri().toURL();
            } else {
                System.out.println("Unable to locate: " + path.toUri());
                System.exit(-1);
            }
        }

        if(ISSUES_FILE.endsWith(".txt")){
            //process text file
            TextParser tp = new TextParser();
            tp.parse(fileURL, includeComponent, addStyle);
        } else if (ISSUES_FILE.endsWith(".xml")) {
            //process xml file
            XMLParser xmlp = new XMLParser();
            xmlp.parse(fileURL, includeComponent, addStyle);
        } else {
            //unknown file format
            System.out.println("Unknown file format detected, unable to process file");
            System.exit(-1);
        }
    }
}

