package com.javarush.cryptoanalyser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;


import static com.javarush.cryptoanalyser.CharacterData.ALPHABET_ENGLISH_FULL;
import static com.javarush.cryptoanalyser.CharacterData.ENGLISH_ALPHABET_FULL_SIZE;
import static com.javarush.cryptoanalyser.CharacterData.ALPHABET_UKRAINIAN_FULL;
import static com.javarush.cryptoanalyser.CharacterData.UKRAINIAN_ALPHABET_FULL_SIZE;
import static com.javarush.cryptoanalyser.Main.Alphabet.detectAlphabet;


public class Main {
    public class Alphabet {
        public static String detectAlphabet(String text) {
            if (ALPHABET_ENGLISH_FULL.equals(text)) {
                return "eng";
            } else if (ALPHABET_UKRAINIAN_FULL.equals(text)) {
                return "ukr";
            } else {
                return "unknown";
            }
        }
    public static void main(String[] args) {
//            for (String arg : args) {
//                System.out.println(arg);
//            }
        String text1 = "This is English text";
        String text2 = "Це український текст";
        String text3 = "Не відомо, який алфавіт";
        System.out.println(detectAlphabet(text1));
        System.out.println(detectAlphabet(text2));
        System.out.println(detectAlphabet(text3));
    }
}

private static String encryption(String encryptTextBefore, int encryptKey){
        char[] encryptCharBefore = encryptTextBefore.toCharArray();
        char[] encryptCharAfter = new char[encryptCharBefore.length];
        boolean isPresence = false;
        for (int i = 0; i < encryptCharBefore.length; i++) {
            for (int k = 0; k < ENGLISH_ALPHABET_FULL_SIZE || k < UKRAINIAN_ALPHABET_FULL_SIZE; k++) {
                if (encryptCharBefore[i] == ALPHABET_ENGLISH_FULL[k]) {
                    isPresence = true;
                }
                if (isPresence){
                    encryptCharAfter[i] = ALPHABET_ENGLISH_FULL[(k + encryptKey)%ENGLISH_ALPHABET_FULL_SIZE];
                    isPresence = false;
                }
                if (encryptCharBefore[i] == ALPHABET_UKRAINIAN_FULL[k]) {
                    isPresence = true;
                }
                if (isPresence){
                    encryptCharAfter[i] = ALPHABET_UKRAINIAN_FULL[(k + encryptKey)%UKRAINIAN_ALPHABET_FULL_SIZE];
                    isPresence = false;
                }
            }
        }
        return new String(encryptCharAfter);
    }

    private static String decryption(String decryptTextBefore, int decryptKey){
        char[] decryptCharBefore = decryptTextBefore.toCharArray();
        char[] decryptCharAfter = new char[decryptCharBefore.length];
        boolean isPresence = false;
        for (int i = 0; i < decryptCharBefore.length; i++) {
            for (int k = 0; k < ENGLISH_ALPHABET_FULL_SIZE; k++) {
                if (decryptCharBefore[i] == ALPHABET_ENGLISH_FULL[k]) {
                    isPresence = true;
                }
                if (isPresence){
                    if ((k-decryptKey) > 0) {
                        decryptCharAfter[i] = ALPHABET_ENGLISH_FULL[(k - decryptKey) % ENGLISH_ALPHABET_FULL_SIZE];
                    } else {
                        decryptCharAfter[i] = ALPHABET_ENGLISH_FULL[(ENGLISH_ALPHABET_FULL_SIZE + (k - decryptKey)) % ENGLISH_ALPHABET_FULL_SIZE];
                    }
                    isPresence = false;
                }
            }
        }
        return new String(decryptCharAfter);
    }

    public static void main(String[] args) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Введите путь файла для шифрования текста:");
            byte[] buffer = Files.readAllBytes(Path.of(reader.readLine()));
            String encryptTextBefore = new String(buffer, StandardCharsets.UTF_8);

            System.out.println("Введите ключ для шифрования ввиде цифры:");
            int encryptKey = Integer.parseInt(reader.readLine());

            System.out.println("Введите путь для создания файла и записи зашифрованного текста:");
            Path encryptFile = Files.createFile(Path.of(reader.readLine()));
            Files.writeString(encryptFile,encryption(encryptTextBefore, encryptKey));

            String decryptTextBefore = encryption(encryptTextBefore, encryptKey);

            System.out.println("Введите путь для создания файла и записи расшифрованного текста:");
            Path decryptFile = Files.createFile(Path.of(reader.readLine()));

            System.out.println("Введите ключ для расшифрования ввиде цифры:");
            int decryptKey = Integer.parseInt(reader.readLine());
            Files.writeString(decryptFile,decryption(decryptTextBefore, decryptKey));

        } catch (IOException e) {
            System.out.println("The file does not exist " + e.getMessage());
        }
    }
}