package com.agungsantoso.udacity.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.agungsantoso.udacity.popularmovies.R;

/**
 * Created by agung.santoso on 09/08/2017.
 */

public final class PopularMoviePreferences {

    public static final String PREF_SORT_BY = "sort_by";

    public static void setSortBy(
            Context context,
            String sortBy) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREF_SORT_BY, sortBy);
        editor.apply();
    }

    public static void resetSortBy(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(PREF_SORT_BY);
    }

    public static String getPreferredSortBy(
            Context context
    ) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String defaultSortBy = context.getString(R.string.sort_by_default);

        return sp.getString(PREF_SORT_BY, defaultSortBy);
    }
}
