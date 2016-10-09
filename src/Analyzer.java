import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Zver on 09.10.2016.
 */
public class Analyzer {
    private final String inPath;
    private final String outPath;
    private final String stopWordsPath;
    private String trashSymbols;
    private String charset;

    public Analyzer(String inPath, String outPath, String stopWordsPath) {
        this.inPath = inPath;
        this.outPath = outPath;
        this.stopWordsPath = stopWordsPath;
        trashSymbols = "[.?!=&^$:;,-><+@\"%]";
        charset = "windows-1251";
    }

    public void setTrashSymbols(String symbols) {
        trashSymbols = symbols;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void mapToFile(String path, Map<String, Integer> map) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(path, this.charset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            writer.println(entry.getKey() + " = " + entry.getValue());
        }
        writer.close();
    }

    public Map<String, Integer> fileToMap(String path, Set<String> stopWords) {
        Map<String, Integer> map = new HashMap<>();
        InputStreamReader inputStreamReader = inReader(path);

        try (BufferedReader br = new BufferedReader(inputStreamReader)){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null && !sCurrentLine.equals("")) {
                String[] s = modify(sCurrentLine);
                for (int i = 0; i < s.length; i++) {
                    if (stopWords.contains(s[i])) continue;
                    if (!map.containsKey(s[i])) {
                        map.put(s[i], 1);
                    } else {
                        map.put(s[i], map.get(s[i]) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    public Set<String> fileToSet(String path) {
        Set<String> set = new HashSet<>();
        InputStreamReader inputStreamReader = inReader(path);

        try (BufferedReader br = new BufferedReader(inputStreamReader)) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] s = modify(sCurrentLine);
                for (int i = 0; i < s.length; i++) {
                    set.add(s[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return set;
    }

    /**
     * сортировка по убыванию
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =  new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare = map.get(k2).compareTo(map.get(k1));
                if (compare == 0) return 1;
                else return compare;
            }
        };
        Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }

    private InputStreamReader inReader(String path) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader  = new InputStreamReader(new
                    FileInputStream(path), Charset.forName(this.charset));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStreamReader;
    }

    private String[] modify(String s) {
        s = s.toLowerCase().replaceAll(this.trashSymbols, "");
        return s.split(" ");
    }
}
