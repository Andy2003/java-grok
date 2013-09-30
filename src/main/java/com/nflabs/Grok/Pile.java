package com.nflabs.Grok;

import java.io.File;
import java.util.*;

@SuppressWarnings("UnusedDeclaration")
public class Pile {

    //Private
    private List<Grok> groks;
    private Map<String, String> patterns;
    private List<String> patternFiles;

    static final String defaultPatternDirectory = "patterns/";

    /**
     * * Constructor
     */
    public Pile() {
        patterns = new TreeMap<String, String>();
        groks = new ArrayList<Grok>();
        patternFiles = new ArrayList<String>();
    }

    /**
     * @param name of the pattern
     * @param file path
     * @return the Status
     */
    public GrokStatus addPattern(String name, String file) {
        if (name.isEmpty() || file.isEmpty()) {
            return GrokStatus.UNINITIALIZED;
        }
        patterns.put(name, file);
        return GrokStatus.OK;
    }

    /**
     * Load patterns file from a directory
     *
     * @param directory the directory
     */
    public GrokStatus addFromDirectory(String directory) {

        if (directory == null || directory.isEmpty()) {
            directory = defaultPatternDirectory;
        }

        File dir = new File(directory);
        File lst[] = dir.listFiles();

        if (lst == null) {
            return GrokStatus.OK;
        }
        for (File aLst : lst) {
            if (aLst.isFile()) {
                addPatternFromFile(aLst.getAbsolutePath());
            }
        }

        return GrokStatus.OK;
    }


    /**
     * Add pattern to grok from a file
     *
     * @param file the file
     * @return the Status
     */
    public GrokStatus addPatternFromFile(String file) {

        File f = new File(file);
        if (!f.exists()) {
            return GrokStatus.FILE_NOT_ACCESSIBLE;
        }
        patternFiles.add(file);
        return GrokStatus.OK;
    }

    /**
     * Compile the pattern with a corresponding grok
     *
     * @param pattern the pattern
     * @throws Throwable
     */
    public void compile(String pattern) throws Throwable {

        Grok grok = new Grok();

        Set<String> added = new HashSet<String>();

        for (Map.Entry<String, String> entry : patterns.entrySet()) {
            if (!added.add(entry.getValue())) {
                grok.addPattern(entry.getKey(), entry.getValue());
            }
        }

        for (String file : patternFiles) {
            grok.addPatternFromFile(file);
        }

        grok.compile(pattern);
        groks.add(grok);
    }

    /**
     * @param line to match
     * @return Grok Match
     */
    public Match match(String line) {
        for (Grok grok : groks) {
            Match gm = grok.match(line);
            if (gm != null) {
                return gm;
            }
        }

        return null;
    }

}
