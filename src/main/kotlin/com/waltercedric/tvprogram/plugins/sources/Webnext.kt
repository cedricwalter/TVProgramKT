/**
 * Copyright (c) 2017-2017 by Cédric Walter - www.cedricwalter.com

 * TVProgram is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * TVProgram is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.waltercedric.tvprogram.plugins.sources

import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import com.waltercedric.tvprogram.TVProgram
import org.jsoup.Jsoup
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


/**
 * use https://webnext.fr/programme-tv-rss
 */
class Webnext : TVProgramBuilder {

    override val todayProgram: List<TVProgram>
        @Synchronized get() {
            if (cache == null) {
                println("Initialize Today TV program cache")
                cacheDate = date
                cache = programList
            }

            if (date != cacheDate) {
                println("Initialize Today TV program cache")
                cacheDate = date
                cache = programList
            }

            println("Get Today TV program from cache")
            return cache!!
        }

    private // use previous program on same channel to calculate end time
    val programList: List<TVProgram>
        get() {
            val programs = ArrayList<TVProgram>()

            try {
                println("Starting to build today's program")
                val feedUrl = todayFeedURL

                val input = SyndFeedInput()
                val feed = input.build(XmlReader(feedUrl))

                val entries = feed.getEntries()
                for (entry in entries) {
                    val tvProgram = getTvProgramFromEntry(entry)
                    if (programs.size > 0) {
                        val startTime = tvProgram.startTime

                        val previous = programs[programs.size - 1]
                        if (previous.channel == tvProgram.channel) {
                            previous.endTime = startTime
                        }
                    }

                    programs.add(tvProgram)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException(e)
            }

            println("Finished building today's program")
            return programs
        }


    private fun getTvProgramFromEntry(entry: SyndEntry): TVProgram {
        val o = entry.getCategories().get(0)

        val category = Jsoup.parse(o.getName()).text()
        var description = Jsoup.parse(entry.getDescription().getValue()).text()
        description = description.replace("\\>".toRegex(), " et")

        val text = Jsoup.parse(entry.getTitle()).text()
        val split = text.split("\\|".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val title = split[2].trim({ it <= ' ' })
        val startTime = LocalTime.parse(split[1].trim({ it <= ' ' }) + ":00")
        val channel = translateChannelForMoreClarity(split[0].trim({ it <= ' ' }))

        //endTime do not exist in feed, will be later updated when we have the whole list, so use startTime for now
        return TVProgram(channel, title, category, description, startTime, startTime)
    }

    private fun translateChannelForMoreClarity(channel: String): String {
        if (channels.containsKey(channel)) {
            return channels[channel]!!
        }
        return channel
    }

    private val todayFeedURL: URL
        @Throws(MalformedURLException::class)
        get() {
            val date = date

            val spec = "https://webnext.fr/epg_cache/programme-tv-rss_$date.xml"
            println("Fetching RSS from " + spec)

            return URL(spec)
        }

    private val date: String
        get() {
            val now = LocalDateTime.now()

            var monthValue = now.monthValue.toString()
            if (now.monthValue < 10) {
                monthValue = "0" + now.monthValue
            }

            var dayOfMonth = now.dayOfMonth.toString()
            if (now.dayOfMonth < 10) {
                dayOfMonth = "0" + now.dayOfMonth
            }

            return now.year.toString() + "-" + monthValue + "-" + dayOfMonth
        }

    companion object {

        private var cache: List<TVProgram>? = null
        private var cacheDate: String? = null

        private val channels = HashMap<String, String>()

        init {
            channels.put("TF1", "TF1")
            channels.put("M6", "M6")
            channels.put("C8", "C8")
            channels.put("W9", "W9")
            channels.put("ter", "6ter")
            channels.put("TMC", "TMC")
            channels.put("HD1", "HD1")
            channels.put("Gulli", "Gulli")
        }
    }

}
