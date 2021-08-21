package com.example.s_tools.tools

import android.util.Log
import java.util.*

object ContentSplit {
    const val TAG = "mtag"
    @JvmStatic
    fun makeFirstLineBold(s: String?): String? {
        if (s == null || s.length == 0) {
            return s
        }
        val lines = s.substring(3).split("\n".toRegex()).toTypedArray()
        val fl = String.format(Locale.ENGLISH, "<b>%s</b><br>", lines[0])
        Log.i(TAG, "makeFirstLineBold: $fl")
        val sb = StringBuilder()
        sb.append(fl)
        for (i in 1 until lines.size) {
            if (i == lines.size - 1) {
                Log.i(TAG, "makeFirstLineBold: called")
                sb.append(lines[i])
            } else {
                sb.append(lines[i] + "<br>")
            }
        }
        return sb.toString().trim { it <= ' ' }
    }

    fun southmovieContent(s: String): String {
        s.replace("southfreak", "Buddy")
        try {
            if (s.contains("Released")) {
                Log.e(TAG, "movieContent: first")
                if (s.contains("IMDb")) {
                    Log.e(TAG, "movieContent: matched")
                    val qualities = s.split("IMDb".toRegex()).toTypedArray()
                    val q = qualities[1]
                    if (!s.contains("Stars")) {
                        return ""
                    }
                    val language = q.split("Stars".toRegex()).toTypedArray()
                    return language[0]
                }
                val qualities = s.split("IMDB".toRegex()).toTypedArray()
                val q = qualities[1]
                if (!s.contains("Languages")) {
                    return ""
                }
                val language = q.split("Languages".toRegex()).toTypedArray()
                return language[0]
            }
            if (s.contains("Title")) {
                Log.e(TAG, "movieContent: sec")
                val imDbs = s.split("Title:".toRegex()).toTypedArray()
                val a = imDbs[1]
                val genres = a.split("All Genres".toRegex()).toTypedArray()
                return if (!s.contains("All Genres")) {
                    ""
                } else genres[0]
            }
            if (s.contains("File Size:")) {
                if (s.contains("IMDb")) {
                    val imDbs = s.split("IMDb".toRegex()).toTypedArray()
                    val a = imDbs[1]
                    val imDbs2 = a.split("Stars".toRegex()).toTypedArray()
                    return if (!s.contains("Stars")) {
                        ""
                    } else imDbs2[0]
                }
                Log.e(TAG, "movieContent: third")
                val imDbs = s.split("IMDB".toRegex()).toTypedArray()
                val a = imDbs[1]
                val imDbs2 = a.split("Languages".toRegex()).toTypedArray()
                return if (!s.contains("Languages")) {
                    ""
                } else imDbs2[0]
            }
        } catch (e: Exception) {
            Log.e(TAG, "movieContent: exception")
            return ""
        }
        return ""
    }

    fun MovieRushSplit(s: String): String {
        val replace = s.replace("moviesrush", "Buddy")
        try {
            if (replace.contains("IMDB")) {
                val imbds = replace.split("IMBD".toRegex()).toTypedArray()
                if (imbds.size == 1) {
                    if (!replace.contains("Plot")) {
                        val plots = imbds[0].split("Size".toRegex()).toTypedArray()
                        return plots[0]
                    }
                    val plots = imbds[0].split("Plot".toRegex()).toTypedArray()
                    Log.e(TAG, "MovieRushSplit: content split else part")
                    return plots[0]
                }
                val plots = imbds[1].split("Plot".toRegex()).toTypedArray()
                return plots[0]
            }
        } catch (e: Exception) {
            return ""
        }
        return ""
    }
}