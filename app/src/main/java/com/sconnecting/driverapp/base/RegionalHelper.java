package com.sconnecting.driverapp.base;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by TrungDao on 8/4/16.
 */


public class RegionalHelper {

/*
    public static String getCountryNameFromCode(String code) {

        String[] locales = Locale.getISOCountries();

        for (String countryCode : locales) {

            if (countryCode.equals(code)) {
                Locale obj = new Locale("", countryCode);
                return obj.getDisplayCountry();
            }

        }

        return "";

    }*/

    public static String getCountryCodeFromName(String name) {

        Locale curent = new Locale("vi","VN");

        for (Locale obj : Locale.getAvailableLocales()) {

            if (obj.getDisplayCountry(curent).equals(name)) {
                return obj.getCountry();
            }

        }

        return null;
    }

    public static String getLocaleIdentifier(String currency, String country) {

        if ((country != null && country.equals("VN")) || (currency != null && currency .equals("VND"))) {
            return "vi_VN";
        }
        return null;
    }

    public static String getCurrencySymbol(String currency, String country) {

        if (currency.equals("VND") || country .equals("VN")) {
            return "đ";
        }

        return null;
    }

    public static String toCurrencyOfCountry(Double amount, String countryCode) {

        NumberFormat format;

        if(countryCode .equals("VN")){

            Locale objLocale = new Locale("vi", "VN");

            format = NumberFormat.getCurrencyInstance(objLocale);

            format.setCurrency(Currency.getInstance(objLocale));
            DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) format).getDecimalFormatSymbols();

            decimalFormatSymbols.setDecimalSeparator(',');
            decimalFormatSymbols.setGroupingSeparator('.');
            decimalFormatSymbols.setCurrencySymbol("đ");
            ((DecimalFormat) format).setDecimalFormatSymbols(decimalFormatSymbols);
            ((DecimalFormat) format).applyPattern("#,##0 ¤");

        }else{

            Locale objLocale = new Locale("", countryCode);

            format = NumberFormat.getCurrencyInstance(objLocale);

            format.setCurrency(Currency.getInstance(objLocale));
        }

        Double newValue = new Double( Math.round(amount / 1000) * 1000);

        return format.format(newValue);
    }


    public static String toCurrency(Double amount, String currency) {

        NumberFormat format;

        Double newValue = new Double( Math.round(amount / 1000) * 1000);

        if(currency .equals("VND")){

            Locale objLocale = new Locale("vi", "VN");

            format = NumberFormat.getCurrencyInstance(objLocale);

            format.setCurrency(Currency.getInstance(objLocale));
            DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) format).getDecimalFormatSymbols();

            decimalFormatSymbols.setDecimalSeparator(',');
            decimalFormatSymbols.setGroupingSeparator('.');
            decimalFormatSymbols.setCurrencySymbol("đ");
            ((DecimalFormat) format).setDecimalFormatSymbols(decimalFormatSymbols);
            ((DecimalFormat) format).applyPattern("#,##0 ¤");

            return format.format(newValue);

        }else if(currency != null && currency.isEmpty() ==false){

            Locale objLocale = new Locale(currency, "");

            format = NumberFormat.getCurrencyInstance(objLocale);

            format.setCurrency(Currency.getInstance(objLocale));

            return format.format(newValue);
        }


        return newValue.toString();
    }

}