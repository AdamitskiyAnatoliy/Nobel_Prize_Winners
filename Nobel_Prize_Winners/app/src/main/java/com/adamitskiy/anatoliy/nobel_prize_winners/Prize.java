package com.adamitskiy.anatoliy.nobel_prize_winners;

import java.io.Serializable;

/**
 * Created by Anatoliy on 6/19/15.
 */
public class Prize implements Serializable {

    private String year, category, share, motivation;

    public Prize (String _year, String _cat, String _share, String _motivation) {
        year = _year; category = _cat; share = _share; motivation = _motivation;
    }

    public String getYear() {
        return year;
    }

    public String getCategory() {
        return category;
    }

    public String getShare() {
        return share;
    }

    public String getMotivation() {
        return motivation;
    }
}
