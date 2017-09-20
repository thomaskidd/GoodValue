package com.example.thomaskidd.goodvalue;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas Kidd on 2017-08-05.
 */

public class Forex {

    private static List<String> volumeArray = Arrays.asList("Litres", "Millilitres", "US gallons", "US fl. oz.", "Imp. gallons", "Imp. fl. oz.");
    private static List<String> massArray = Arrays.asList("Grams", "Kilograms", "Ounces", "Pounds");
    
    public static String convert(double price, String currencyName, double quantity, String unitName, String targetCurrencyName, String targetUnitName) {
        double currency, unit, targetCurrency, targetUnit;

        //Create an exchange
        Exchange exchange = create("http://api.fixer.io/latest?base=USD");

        //Get value of 1.00 USD in currencies
        currency = getValue(exchange, currencyName);
        targetCurrency = getValue(exchange, targetCurrencyName);

        //Get value of 1.00 litres/grams in units
        unit = getUnit(unitName);
        targetUnit = getUnit(targetUnitName);


        //Step One - determine the value of x currency in y currency
        double tempPrice;

        tempPrice = price*equivalence(currency, targetCurrency); //price in new currency per old unit

        //Step Two - determine the number of x units in y units
        double tempQuantity;

        tempQuantity = quantity*equivalence(unit, targetUnit);

        //Step Three - determine the price in y currency per y unit
        double newPrice;

        newPrice = tempPrice/tempQuantity;

       return String.valueOf(newPrice)+"\n"+targetCurrency+"/"+targetUnit;
    }

    public static Exchange create(String test) {
        URL url = null;

        try {
            url = new URL(test);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        InputStreamReader reader = null;

        try {
            reader = new InputStreamReader(url.openStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Exchange exchange = new Gson().fromJson(reader, Exchange.class);

        return exchange;
    }

    public static double equivalence(double one, double two) {

        //denomination unit (two) over price point unit (one) gives the value of currency one in currency two (or the amount of unit one in unit two)
        return two/one;
    }

    //Returns the value of 1 USD in the selected currency
    public static double getValue(Exchange exchange, String name) {

        double rate = -1.0;

        switch (name) {
            case "AUD":
                rate = exchange.getRates().getAUD();
                break;
            case "BGN":
                rate = exchange.getRates().getBGN();
                break;
            case "BRL":
                rate = exchange.getRates().getBRL();
                break;
            case "CAD":
                rate = exchange.getRates().getCAD();
                break;
            case "CHF":
                rate = exchange.getRates().getCHF();
                break;
            case "CNY":
                rate = exchange.getRates().getCNY();
                break;
            case "CZK":
                rate = exchange.getRates().getCZK();
                break;
            case "DKK":
                rate = exchange.getRates().getDKK();
                break;
            case "GBP":
                rate = exchange.getRates().getGBP();
                break;
            case "HKD":
                rate = exchange.getRates().getHKD();
                break;
            case "HRK":
                rate = exchange.getRates().getHRK();
                break;
            case "HUF":
                rate = exchange.getRates().getHUF();
                break;
            case "IDR":
                rate = exchange.getRates().getIDR();
                break;
            case "ILS":
                rate = exchange.getRates().getILS();
                break;
            case "INR":
                rate = exchange.getRates().getINR();
                break;
            case "JPY":
                rate = exchange.getRates().getJPY();
                break;
            case "KRW":
                rate = exchange.getRates().getKRW();
                break;
            case "MXN":
                rate = exchange.getRates().getMXN();
                break;
            case "MYR":
                rate = exchange.getRates().getMYR();
                break;
            case "NOK":
                rate = exchange.getRates().getNOK();
                break;
            case "NZD":
                rate = exchange.getRates().getNZD();
                break;
            case "PHP":
                rate = exchange.getRates().getPHP();
                break;
            case "PLN":
                rate = exchange.getRates().getPLN();
                break;
            case "RON":
                rate = exchange.getRates().getRON();
                break;
            case "RUB":
                rate = exchange.getRates().getRUB();
                break;
            case "SEK":
                rate = exchange.getRates().getSEK();
                break;
            case "SGD":
                rate = exchange.getRates().getSGD();
                break;
            case "THB":
                rate = exchange.getRates().getTHB();
                break;
            case "TRY":
                rate = exchange.getRates().getTRY();
                break;
            case "ZAR":
                rate = exchange.getRates().getZAR();
                break;
            case "EUR":
                rate = exchange.getRates().getEUR();
                break;
            case "USD":
                rate = exchange.getRates().getUSD();
                break;
            default:
                break;
        }

        return rate;
    }

    public static double getUnit(String name) {
        double unitValue = -1.0;
        Volume vol = new Volume();
        Mass mass = new Mass();

        switch(name) {
            //Volume Values
            case "Litres":
                unitValue = vol.getLitre();
                break;
            case "Millilitres":
                unitValue = vol.getMillilitre();
                break;
            case "US gallons":
                unitValue = vol.getGallon();
                break;
            case "Imp. gallons":
                unitValue = vol.getImpGallon();
                break;
            case "US fl. oz.":
                unitValue = vol.getFlOz();
                break;
            case "Imp. fl. oz.":
                unitValue = vol.getImpFlOz();
                break;

            //Mass Values
            case "Grams":
                unitValue = mass.getGram();
                break;
            case "Kilograms":
                unitValue = mass.getKilogram();
                break;
            case "Ounces":
                unitValue = mass.getOunce();
                break;
            case "Pounds":
                unitValue = mass.getPound();
                break;
        }

        return unitValue;
    }
    
    public static String getCurrencySymbol(String name) {
        
        String symbol = "$";
        
        switch (name) {
            case "AUD":
                symbol = "$";
                break;
            case "BGN":
                symbol = "лв";
                break;
            case "BRL":
                symbol = "R$";
                break;
            case "CAD":
                symbol = "$";
                break;
            case "CHF":
                symbol = "CHF ";
                break;
            case "CNY":
                symbol = "¥";
                break;
            case "CZK":
                symbol = "Kč ";
                break;
            case "DKK":
                symbol = "kr ";
                break;
            case "GBP":
                symbol = "£";
                break;
            case "HKD":
                symbol = "$";
                break;
            case "HRK":
                symbol = "kn ";
                break;
            case "HUF":
                symbol = "Ft ";
                break;
            case "IDR":
                symbol = "Rp ";
                break;
            case "ILS":
                symbol = "₪ ";
                break;
            case "INR":
                symbol = "INR ";
                break;
            case "JPY":
                symbol = "¥";
                break;
            case "KRW":
                symbol = "₩";
                break;
            case "MXN":
                symbol = "$";
                break;
            case "MYR":
                symbol = "RM ";
                break;
            case "NOK":
                symbol = "kr ";
                break;
            case "NZD":
                symbol = "$";
                break;
            case "PHP":
                symbol = "₱";
                break;
            case "PLN":
                symbol = "zł ";
                break;
            case "RON":
                symbol = "lei ";
                break;
            case "RUB":
                symbol = "₽";
                break;
            case "SEK":
                symbol = "kr ";
                break;
            case "SGD":
                symbol = "$";
                break;
            case "THB":
                symbol = "฿";
                break;
            case "TRY":
                symbol = "TRY ";
                break;
            case "ZAR":
                symbol = "R";
                break;
            case "EUR":
                symbol = "€";
                break;
            case "USD":
                symbol = "$";
                break;
            default:
                break;
        }

        return symbol;
        
    }

    public static String getUnitSymbol(String name) {

        String symbol = "";

        switch(name) {
            //Volume Values
            case "Litres":
                symbol = "L";
                break;
            case "Millilitres":
                symbol = "mL";
                break;
            case "US gallons":
                symbol = "gal";
                break;
            case "Imp. gallons":
                symbol = "gal";
                break;
            case "US fl. oz.":
                symbol = "fl. oz.";
                break;
            case "Imp. fl. oz.":
                symbol = "fl. oz.";
                break;

            //Mass Values
            case "Grams":
                symbol = "g";
                break;
            case "Kilograms":
                symbol = "kg";
                break;
            case "Ounces":
                symbol = "oz.";
                break;
            case "Pounds":
                symbol = "lb.";
                break;
        }

        return symbol;
    }

    public static boolean volume(String s) {
        if (volumeArray.contains(s)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean mass(String s) {
        if (massArray.contains(s)) {
            return true;
        }
        else {
            return false;
        }
    }
}
