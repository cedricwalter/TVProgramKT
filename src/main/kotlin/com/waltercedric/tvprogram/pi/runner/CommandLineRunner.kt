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
package com.waltercedric.tvprogram.pi.runner

import com.waltercedric.tvprogram.Config
import com.waltercedric.tvprogram.TVReader
import com.waltercedric.tvprogram.guide.InteractiveTVGuide
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder

import java.time.LocalTime
import java.util.Scanner


class CommandLineRunner : Runner {

    private val config: Config
    private val tvReader: TVReader
    private var channelCursor: Int = 0
    private var now: LocalTime? = null

    init {

        config = Config()
        tvReader = TVReader()
        now = LocalTime.now()
        channelCursor = 0
    }

    @Throws(InterruptedException::class)
    override fun execute() {
        val builder = config.tvProgramBuilder

        println("Waiting for key +/- for time    d/f for channel")
        while (true) {
            val keyboard = Scanner(System.`in`)
            val input = keyboard.nextLine()
            println(input)

            if (input == "d") {
                println("Channel UP pressed")
                val tvGuide = InteractiveTVGuide(config, builder.todayProgram, now!!, channelCursor)
                channelCursor = tvGuide.channelUp()
                try {
                    tvReader.stop()
                    tvReader.read(tvGuide.timeCursor!!, tvGuide)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            if (input == "f") {
                println("Channel Down pressed")
                val tvGuide = InteractiveTVGuide(config, builder.todayProgram, now!!, channelCursor)
                channelCursor = tvGuide.channelDown()
                try {
                    tvReader.stop()
                    tvReader.read(tvGuide.timeCursor!!, tvGuide)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            if (input == "+") {
                println("Time UP pressed")
                val tvGuide = InteractiveTVGuide(config, builder.todayProgram, now!!, channelCursor)
                now = tvGuide.timeUp()
                try {
                    tvReader.stop()
                    tvReader.read(tvGuide.timeCursor!!, tvGuide)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            if (input == "-") {
                println("Time Down pressed")
                val tvGuide = InteractiveTVGuide(config, builder.todayProgram, now!!, channelCursor)
                now = tvGuide.timeDown()
                try {
                    tvReader.stop()
                    tvReader.read(tvGuide.timeCursor!!, tvGuide)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            Thread.sleep(200)
        }
    }


}
