package com.javarush.cryptoanalyser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FrequencyAnalysis {

    public static void main(String[] args) {
        String command = args[0];
        String filePath = args[1];
        String keyFilePath = args[2];

        String text = readFile(filePath);

        Map<Character, Integer> frequencyMap = calculateFrequencyMap(text);
        if (command.equals("BRUTE_FORCE")) {
            String key = findKey(frequencyMap);
            writeKeyToFile(keyFilePath, key);
            System.out.println("Key found: " + key);
        } else if (command.equals("STATIC_ANALYSIS")) {
            System.out.println("Frequency map:");
            printFrequencyMap(frequencyMap);
        } else {
            System.out.println("Invalid command: " + command);
        }
    }
    public static String readFile(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static Map<Character, Integer> calculateFrequencyMap(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<Character, Integer>();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (frequencyMap.containsKey(c)) {
                int frequency = frequencyMap.get(c);
                frequencyMap.put(c, frequency + 1);
            } else {
                frequencyMap.put(c, 1);
            }
        }
        return frequencyMap;
    }

    public static String findKey(Map<Character, Integer> frequencyMap) {
        // TODO: Implement key finding algorithm
        return "TEST_KEY";
    }

    public static void writeKeyToFile(String keyFilePath, String key) {
        // TODO: Implement writing key to file
    }
    public static void printFrequencyMap(Map<Character, Integer> frequencyMap) {
        Set<Character> characters = frequencyMap.keySet();
        for (Character c : characters) {
            int frequency = frequencyMap.get(c);
            System.out.println(c + ": " + frequency);
        }
    }

}
