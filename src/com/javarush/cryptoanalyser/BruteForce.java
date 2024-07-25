package com.javarush.cryptoanalyser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;



import static com.javarush.cryptoanalyser.CharacterData.*;

public class BruteForce {

    private static final int NUMBER_SMALL_SIZE = 0;
    private static final int NUMBER_MEDIUM_SIZE = 4;
    private static final int NUMBER_LARGE_SIZE = 8;
    private static final String SMALL_TEXT = "small";
    private static final String MEDIUM_TEXT = "medium";
    private static final String LARGE_TEXT = "large";

    private static String decryptionEnglish(String encryptText, int decryptKey){
        char[] decryptCharBefore = encryptText.toCharArray();
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

    private static String decryptionUkrainian(String encryptText, int decryptKey){
        char[] decryptCharBefore = encryptText.toCharArray();
        char[] decryptCharAfter = new char[decryptCharBefore.length];
        boolean isPresence = false;
        for (int i = 0; i < decryptCharBefore.length; i++) {
            for (int k = 0; k < UKRAINIAN_ALPHABET_FULL_SIZE; k++) {
                if (decryptCharBefore[i] == ALPHABET_UKRAINIAN_FULL[k]) {
                    isPresence = true;
                }
                if (isPresence){
                    if ((k-decryptKey) > 0) {
                        decryptCharAfter[i] = ALPHABET_UKRAINIAN_FULL[(k - decryptKey) % UKRAINIAN_ALPHABET_FULL_SIZE];
                    } else {
                        decryptCharAfter[i] = ALPHABET_UKRAINIAN_FULL[(UKRAINIAN_ALPHABET_FULL_SIZE + (k - decryptKey)) % UKRAINIAN_ALPHABET_FULL_SIZE];
                    }
                    isPresence = false;
                }
            }
        }
        return new String(decryptCharAfter);
    }

    private static int getKeyCharactersCount(String force){
        int keyCharactersCount = 0;

        for (String keyCh : KEY_CHARACTERS) {
            if (force.contains(keyCh)) {
                keyCharactersCount++;
            }
        }
        return keyCharactersCount;
    }

    public static void main(String[] args) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Введите путь файла для взлома текста:");
            byte[] buffer = Files.readAllBytes(Path.of(reader.readLine()));
            String encryptText = new String(buffer, StandardCharsets.UTF_8);
            System.out.println("Введите размер зашифрованного текста\n" +
                    "(маленький текст - введите " + SMALL_TEXT + "\n" +
                    "средний текст - введите " + MEDIUM_TEXT + "\n" +
                    "большой текст - введите " + LARGE_TEXT + "):");

            String sizeText = reader.readLine();
            int sizeNumber = NUMBER_SMALL_SIZE;
            switch (sizeText) {
                case SMALL_TEXT:
                    break;
                case MEDIUM_TEXT:
                    sizeNumber = NUMBER_MEDIUM_SIZE;
                    break;
                case LARGE_TEXT:
                    sizeNumber = NUMBER_LARGE_SIZE;
                    break;
                default:
                    System.out.println("Неправильно введён размер (по умолчанию выбран размер " + SMALL_TEXT + ")");
                    break;
            }

            System.out.println("Введите путь для создания файла и записи взломанного текста:");
            Path forceTextFile = Files.createFile(Path.of(reader.readLine()));
            List<String> listVariants = new ArrayList<>();
            List<String> forceText = new ArrayList<>();
            for (int i = 0; i < ENGLISH_ALPHABET_FULL_SIZE; i++) {
                listVariants.add(decryptionEnglish(encryptText,i));
                char variantCharacter = listVariants.get(i).charAt(listVariants.get(i).length()-1);
                boolean endText = (variantCharacter == '!') || (variantCharacter == '?') || (variantCharacter == '.');

                if ((getKeyCharactersCount(listVariants.get(i)) > sizeNumber) && (endText)){
                    forceText.add(listVariants.get(i));
                }
            }
            for (int i = 0; i < UKRAINIAN_ALPHABET_FULL_SIZE; i++) {
                listVariants.add(decryptionUkrainian(encryptText,i));
                char variantCharacter = listVariants.get(i).charAt(listVariants.get(i).length()-1);
                boolean endText = (variantCharacter == '!') || (variantCharacter == '?') || (variantCharacter == '.');

                if ((getKeyCharactersCount(listVariants.get(i)) > sizeNumber) && (endText)){
                    forceText.add(listVariants.get(i));
                }
            }

            Files.write(forceTextFile, forceText);

        } catch (IOException e) {
            System.out.println("The file does not exist " + e.getMessage());
        }
    }
}