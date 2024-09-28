package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    public static final String CODE_NAME = "alpha3";
    private final JSONArray jsonArray;
    //  pick appropriate instance variables for this class

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            this.jsonArray = new JSONArray(jsonString);

            // use the data in the jsonArray to populate your instance variables
            //            Note: this will likely be one of the most substantial pieces of code you write in this lab.

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        ArrayList<String> languages = new ArrayList<>();
        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject jsonObject = this.jsonArray.getJSONObject(i);
            if (jsonObject.getString(CODE_NAME).equals(country.toLowerCase())) {
                for (String key : jsonObject.keySet()) {
                    if (!Objects.equals(key, "id") && !Objects.equals(key, CODE_NAME) && !Objects
                            .equals(key, "alpha2")) {
                        languages.add(key);
                    }
                }
            }
        }
        // return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        return languages;
    }

    @Override
    public List<String> getCountries() {
        ArrayList<String> countries = new ArrayList<>();
        for (int i = 0; i < this.jsonArray.length(); i++) {
            countries.add(this.jsonArray.getJSONObject(i).getString(CODE_NAME));
        }
        // return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        return countries;
    }

    @Override
    public String translate(String country, String language) {
        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject jsonObject = this.jsonArray.getJSONObject(i);
            if (jsonObject.getString(CODE_NAME).equals(country.toLowerCase())) {
                return jsonObject.optString(language.toLowerCase(), null);
            }
        }
        // complete this method using your instance variables as needed
        return "language not found";
    }
}
