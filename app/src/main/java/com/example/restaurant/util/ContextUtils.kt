package com.example.restaurant.util

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.preference.PreferenceManager
import java.util.*


class ContextUtils(base: Context) : ContextWrapper(base) {

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Selected.Language"

        fun onAttach(context: Context): Context? {
            val lang: String = getSavedLanguage(context)
            return setLocale(context!!, lang)
        }

        fun setLocale(
            context: Context,
            language: String
        ): Context? {
            persist(context, language)

            // updating the language for devices above android nougat
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, language)
            } else updateResourcesLegacy(context, language)
            // for devices having lower version of android os
        }

         fun persist(
             context: Context,
             language: String
         ) {
             val preferences: SharedPreferences =
                 PreferenceManager.getDefaultSharedPreferences(context)
             val editor = preferences.edit()
             editor.putString(SELECTED_LANGUAGE, language)
             editor.apply()
         }

        fun getSavedLanguage(context: Context): String =
            PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SELECTED_LANGUAGE, "en") ?: "en"


        private fun updateResources(
            context: Context,
            language: String
        ): Context? {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration: Configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        }

        private fun updateResourcesLegacy(
            context: Context,
            language: String
        ): Context? {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            configuration.locale = locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLayoutDirection(locale)
            }
            resources.updateConfiguration(configuration, resources.getDisplayMetrics())
            return context
        }

    }

}
