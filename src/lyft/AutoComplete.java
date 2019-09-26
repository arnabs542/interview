package lyft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class AutoComplete {

    class TrieNode {
        Map<Character, TrieNode> childMap;
        Map<String, Integer> countMap;

        TrieNode() {
            childMap = new HashMap<>();
            countMap = new HashMap<>();
        }
    }

    private TrieNode root;
    private String prefix;

    public AutoComplete(String[] sentences, Integer[] times) {
        this.root = new TrieNode();
        this.prefix = "";
        for (int i = 0; i < sentences.length; i++) {
            insert(sentences[i], times[i]);
        }
    }

    private void insert(String word, int count) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            TrieNode next = current.childMap.get(c);
            if (next == null) {
                next = new TrieNode();
                current.childMap.put(c, next);
            }
            current = next;
            current.countMap.put(word, current.countMap.getOrDefault(word, 0) + count);
        }
    }

    public List<String> input(char c) {
        prefix = "";
        if (c == '#') {
            insert(prefix, 1);
            prefix = "";
            return new ArrayList<>();
        }

        prefix = prefix + c;
        TrieNode current = root;
        for (char cp : prefix.toCharArray()) {
            TrieNode next = current.childMap.get(cp);
            if (next == null) {
                return new ArrayList<>();
            }
            current = next;
        }

        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a, b) -> (a.getValue() == b.getValue()) ?
                a.getKey().compareTo(b.getKey()) : b.getValue() - a.getValue());
        pq.addAll(current.countMap.entrySet());

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll().getKey());
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\Dropbox\\interview\\src\\lyft\\autoinput.txt");
        Scanner sc = new Scanner(file);
        Map<String, Integer> map = new HashMap<>();
        while (sc.hasNextLine()) {
            String word = sc.nextLine();
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        sc.close();

        AutoComplete ac = new AutoComplete(map.keySet().toArray(new String[0]), map.values().toArray(new Integer[0]));

        System.out.println(ac.input('i'));
        System.out.println(ac.input('t'));
        System.out.println(ac.input('a'));
        System.out.println(ac.input('l'));

        BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\Dropbox\\interview\\src\\lyft\\autooutput.txt"));
        writer.write(ac.input('i').toString());
        writer.newLine();
        writer.write(ac.input('t').toString());
        writer.newLine();
        writer.write(ac.input('a').toString());
        writer.newLine();
        writer.write(ac.input('l').toString());
        writer.close();
    }
}
