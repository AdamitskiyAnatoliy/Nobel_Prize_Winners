package com.adamitskiy.anatoliy.nobel_prize_winners;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataStorage {

    // Retrieving Countries from Device Storage
    public static ArrayList<Country> getCountries (Context _context) {

        File external = _context.getExternalFilesDir(null);
        File file = new File(external, "Countries");
        ArrayList<Country> countryList = new ArrayList<Country>();;

        try {

            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            countryList = (ArrayList<Country>) objectInputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            countryList = null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            countryList = null;
        }

        return countryList;
    }

    // Saving Countries to Device Storage
    public static void saveCountries (Context _context, ArrayList<Country> countries) {

        File external = _context.getExternalFilesDir(null);
        File file = new File(external, "Countries");

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(countries);
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Retrieving Laureates from Device Storage
    public static ArrayList<Laureate> getLaureates (Context _context, String countryCode) {

        File external = _context.getExternalFilesDir(null);
        File file = new File(external, countryCode);
        ArrayList<Laureate> laureateList = new ArrayList<Laureate>();;

        try {

            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            laureateList = (ArrayList<Laureate>) objectInputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            laureateList = null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            laureateList = null;
        }

        return laureateList;
    }

    // Saving Laureates to Device Storage
    public static void saveLaureates (Context _context, ArrayList<Laureate> laureates, String countryCode) {

        File external = _context.getExternalFilesDir(null);
        File file = new File(external, countryCode);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(laureates);
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
