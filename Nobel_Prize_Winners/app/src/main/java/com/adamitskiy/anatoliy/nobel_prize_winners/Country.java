package com.adamitskiy.anatoliy.nobel_prize_winners;

import java.io.Serializable;

/**
 * Created by Anatoliy on 6/18/15.
 */
public class Country implements Serializable {

    private String countryCode, countryName;

    public Country (String _code, String _name) {
        countryCode = _code; countryName = _name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }
}
