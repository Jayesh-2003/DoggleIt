package com.example.doggleit;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    public static void main(String[] args) {
        String[] dogBreeds = {
                "affenpinscher", "afghan_hound", "african_hunting_dog", "airedale",
                "american_staffordshire_terrier", "appenzeller", "australian_terrier",
                "basenji", "basset", "beagle", "bedlington_terrier", "bernese_mountain_dog",
                "black-and-tan_coonhound", "blenheim_spaniel", "bloodhound", "bluetick",
                "border_collie", "border_terrier", "borzoi", "boston_bull",
                "bouvier_des_flandres", "boxer", "brabancon_griffon", "briard",
                "brittany_spaniel", "bull_mastiff", "cairn", "cardigan",
                "chesapeake_bay_retriever", "chihuahua", "chow", "clumber",
                "cocker_spaniel", "collie", "curly-coated_retriever", "dandie_dinmont",
                "dhole", "dingo", "doberman", "english_foxhound", "english_setter",
                "english_springer", "entlebucher", "eskimo_dog", "flat-coated_retriever",
                "french_bulldog", "german_shepherd", "german_short-haired_pointer",
                "giant_schnauzer", "golden_retriever", "gordon_setter", "great_dane",
                "great_pyrenees", "greater_swiss_mountain_dog", "groenendael", "ibizan_hound",
                "irish_setter", "irish_terrier", "irish_water_spaniel", "irish_wolfhound",
                "italian_greyhound", "japanese_spaniel", "keeshond", "kelpie",
                "kerry_blue_terrier", "komondor", "kuvasz", "labrador_retriever",
                "lakeland_terrier", "leonberg", "lhasa", "malamute", "malinois",
                "maltese_dog", "mexican_hairless", "miniature_pinscher", "miniature_poodle",
                "miniature_schnauzer", "newfoundland", "norfolk_terrier", "norwegian_elkhound",
                "norwich_terrier", "old_english_sheepdog", "otterhound", "papillon",
                "pekinese", "pembroke", "pomeranian", "pug", "redbone", "rhodesian_ridgeback",
                "rottweiler", "saint_bernard", "saluki", "samoyed", "schipperke",
                "scotch_terrier", "scottish_deerhound", "sealyham_terrier", "shetland_sheepdog",
                "shih-tzu", "siberian_husky", "silky_terrier", "soft-coated_wheaten_terrier",
                "staffordshire_bullterrier", "standard_poodle", "standard_schnauzer",
                "sussex_spaniel", "tibetan_mastiff", "tibetan_terrier", "toy_poodle",
                "toy_terrier", "vizsla", "walker_hound", "weimaraner", "welsh_springer_spaniel",
                "west_highland_white_terrier", "whippet", "wire-haired_fox_terrier",
                "yorkshire_terrier"
        };

        for (int i = 0; i < dogBreeds.length; i++) {
            dogBreeds[i] = capitalizeEachWord(dogBreeds[i]);
        }

        // Print the updated dog breeds
        for (String breed : dogBreeds) {
            System.out.println(breed);
        }
    }

    // Method to capitalize the first letter of each word in a string
    @Test
    public static String capitalizeEachWord(String str) {
        String[] words = str.split("_");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                sb.append(word.substring(1));
                sb.append(" ");
            }
        }

        return sb.toString().trim();
    }



    }


