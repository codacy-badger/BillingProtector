package com.andreacioccarelli.billingprotector

import android.content.Context
import android.content.pm.PackageManager
import com.andreacioccarelli.billingprotector.data.PirateApp
import com.andreacioccarelli.billingprotector.data.SelectionCriteria
import com.andreacioccarelli.billingprotector.data.createPirateAppsList
import com.andreacioccarelli.billingprotector.utils.RootUtils


/**
 * Created by andrea on 2018/Jul.
 * Part of the package com.andreacioccarelli.billingprotector
 */
class BillingProtector(private val context: Context) {

    /**
     * Returns a boolean that represents the device root state.
     * */
    fun isRootInstalled() = RootUtils.hasRootAccess()

    /**
     * Returns a String, representing the root binary path, if present.
     * */
    fun getRootBinatyPath() = RootUtils.extractPath()

    /**
     * Returns a boolean that indicates the presence of pirate apps in the host system
     * */
    fun arePirateAppsInstalled(): Boolean {
        val appList = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (app in appList) {
            createPirateAppsList().map {
                when (it.criteria) {
                    SelectionCriteria.SLICE -> {
                        if (it.packageName.contains(app.packageName)) return true
                    }

                    SelectionCriteria.MATCH -> {
                        if (it.packageName == app.packageName) return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Returns a list of the installed apps detected as pirate software
     * */
    fun getPirateAppsList(): List<PirateApp> {
        val foundThreats = mutableListOf<PirateApp>()
        val appList = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (app in appList) {
            createPirateAppsList().map {
                when (it.criteria) {
                    SelectionCriteria.SLICE -> {
                        if (it.packageName.contains(app.packageName)) foundThreats.add(it)
                    }

                    SelectionCriteria.MATCH -> {
                        if (it.packageName == app.packageName) foundThreats.add(it)
                    }
                }
            }
        }
        return foundThreats.toList()
    }
}