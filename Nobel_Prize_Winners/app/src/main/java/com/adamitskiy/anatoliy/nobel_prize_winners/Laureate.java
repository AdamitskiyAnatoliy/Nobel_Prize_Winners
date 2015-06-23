package com.adamitskiy.anatoliy.nobel_prize_winners;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anatoliy on 6/19/15.
 */
public class Laureate implements Serializable {

    private String firstName, lastName, born, died, bornCountry, bornCountryCode, bornCity;
    private ArrayList<Prize> prizes;

    public Laureate (String _firstName, String _lastName, String _born, String _died,
                     String _bornCountry, String _bornCountryCode, String _bornCity,
                     ArrayList<Prize> _prizes) {
        firstName = _firstName; lastName = _lastName; born = _born; died = _died;
        bornCountry = _bornCountry; bornCountryCode = _bornCountryCode; bornCity = _bornCity;
        prizes = _prizes;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBorn() {
        return born;
    }

    public String getDied() {
        return died;
    }

    public String getBornCountry() {
        return bornCountry;
    }

    public String getBornCountryCode() {
        return bornCountryCode;
    }

    public String getBornCity() {
        return bornCity;
    }

    public ArrayList<Prize> getPrizes() {
        return prizes;
    }
}
