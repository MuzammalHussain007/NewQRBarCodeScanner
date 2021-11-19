package com.be.solo.qr.code.scanner.SoloUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.be.solo.qr.code.scanner.SoloModels.Scanned;
import com.be.solo.qr.code.scanner.SoloModels.SoloFavoriteModel;
import com.be.solo.qr.code.scanner.SoloModels.SoloHistoryModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class SoloSharedPreference {

    public static final String HISTORY = "HISTORY";
    public static final String SCANNED_HISTORY = "ScannedHistory";
    public static final String PREFS_SCANNED = "ScannedHistory";
    public static final String PREFS_HISTORY = "HISTORY";
    public static final String PREFS_NAME = "QRCODE";
    public static final String FAVORITES = "FAVORITES";
    public static final String SOLO_MODE = "SoloMode";
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
    String currentDateandTime = sdf.format(new Date());

    public static int CLICK_COUNTER = 1;

    public void addTofavourite(Context context, String result,int color,int icon) {

        List<SoloFavoriteModel> favorites = getFavorites(context);
        if (favorites == null) {
            favorites = new ArrayList<>();
        }
        if (URLUtil.isValidUrl(result)) {
            favorites.add(new SoloFavoriteModel(result, currentDateandTime, "URL",color,icon));
        } else {
            favorites.add(new SoloFavoriteModel(result, currentDateandTime, "TEXT",color,icon));
        }
        Toast.makeText(context, "Save Favourities", Toast.LENGTH_SHORT).show();
        saveFavorites(context, favorites);

    }

    public void addToHistory(Context context, String result,int color,int icon) {

        List<SoloHistoryModel> history = getHistory(context);
        if (history == null) {
            history = new ArrayList<>();
        }
        if (URLUtil.isValidUrl(result)) {
            history.add(new SoloHistoryModel(result, currentDateandTime, "URL",color,icon));
        } else {
            history.add(new SoloHistoryModel(result, currentDateandTime, "TEXT",color,icon));
        }
        saveHistory(context, history);

    }

    public void saveFavorites(Context context, List<SoloFavoriteModel> favorites) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FAVORITES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES, jsonFavorites);
        editor.apply();
    }
    public void saveSoloMode(Context context, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SOLO_MODE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("SoloValue",value);
        editor.apply();
    }
    public int  getsaveSoloMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SOLO_MODE, MODE_PRIVATE);
        return sharedPreferences.getInt("SoloValue",-1);

    }
     //TODO History ka text sai ni aa raha hy Aor sath mein scannedHistory ko dobara check kr lena 
    public void saveHistory(Context context, List<SoloHistoryModel> history) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_HISTORY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonHistory = gson.toJson(history);
        editor.putString(HISTORY, jsonHistory);
        editor.apply();

    }

    public void saveScannedHistory(Context context, List<Scanned> history) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_SCANNED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonHistory = gson.toJson(history);
        editor.putString(SCANNED_HISTORY, jsonHistory);
        editor.apply();

    }
    public List<Scanned> getScannedHistory(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_SCANNED, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SCANNED_HISTORY, null);
        Type type = new TypeToken<ArrayList<Scanned>>() {
        }.getType();
        List<Scanned> history = gson.fromJson(json, type);
        return history;

    }

    public ArrayList<SoloHistoryModel> getHistory(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_HISTORY, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(HISTORY, null);
        Type type = new TypeToken<ArrayList<SoloHistoryModel>>() {
        }.getType();
        List<SoloHistoryModel> history = gson.fromJson(json, type);

        return (ArrayList<SoloHistoryModel>) history;
    }

    public ArrayList<SoloFavoriteModel> getFavorites(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(FAVORITES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(FAVORITES, null);
        Type result_type = new TypeToken<ArrayList<SoloFavoriteModel>>() {
        }.getType();
        List<SoloFavoriteModel> favorites = gson.fromJson(json, result_type);

        return (ArrayList<SoloFavoriteModel>) favorites;
    }

    public void removeFavourite(Context context, int favouriteRemove) {

        List<SoloFavoriteModel> favorites = getFavorites(context);

        if (favorites != null) {
            favorites.remove(favouriteRemove);
            Toast.makeText(context, "Removed from Favourities!", Toast.LENGTH_SHORT).show();
            saveFavorites(context, favorites);
        }
    }

    public void removeHistory(Context context, int historyRemove) {

        List<SoloHistoryModel> history = getHistory(context);

        if (history != null) {
            history.remove(historyRemove);

            Toast.makeText(context, "History Removed!", Toast.LENGTH_SHORT).show();
            saveHistory(context, history);
        }
    }
    public void removeScannedHistory(Context context, int historyRemove) {

        List<Scanned> history = getScannedHistory(context);

        if (history != null) {
            history.remove(historyRemove);

            Toast.makeText(context, "History Removed!", Toast.LENGTH_SHORT).show();
            saveScannedHistory(context, history);
        }
    }

    public void clearAllFavourities(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(FAVORITES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
        editor.commit();

    }

    public void clearAllHistory(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_HISTORY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }
}
