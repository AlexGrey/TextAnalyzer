import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Zver on 06.10.2016.
 */
public class Main {

    public static void main(String[] args) {

        String inPath = args[0];
        String outPath = args[1];
        String stopWordsPath = args[2];

       /* String inPath = "C:\\in.txt";
        String outPath ="C:\\out.txt";
        String stopWordsPath = "C:\\stopWords.txt";*/

        Analyzer analyzer = new Analyzer(inPath, outPath, stopWordsPath);
        Set<String> stopWords = analyzer.fileToSet(stopWordsPath);
        Map<String, Integer> result = analyzer.sortByValues(analyzer.fileToMap(inPath, stopWords));
        analyzer.mapToFile(outPath, result);
    }
}
