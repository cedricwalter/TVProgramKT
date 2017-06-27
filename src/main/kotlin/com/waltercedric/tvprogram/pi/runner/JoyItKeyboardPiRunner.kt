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
import com.waltercedric.tvprogram.pi.drivers.JoyItKeyboardDriver
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder

import java.time.LocalTime


class JoyItKeyboardPiRunner : Runner {
    private val tvReader: TVReader
    private val config: Config
    private val keyboard: JoyItKeyboardDriver
    private var now: LocalTime? = null
    private var channelCursor: Int = 0

    init {
        tvReader = TVReader()
        config = Config()
        keyboard = JoyItKeyboardDriver()
        channelCursor = 0
        now = LocalTime.now()
    }

    @Throws(InterruptedException::class)
    override fun execute() {
        val builder = config.tvProgramBuilder

        synchronized(`object`) {
            val keyPressed = keyboard.keyPressed
            if (keyPressed == config.joyItChannelUp) {
                println("Channel UP pressed")
                val tvGuide = InteractiveTVGuide(config, builder.todayProgram, now!!, channelCursor)
                channelCursor = tvGuide.channelUp()
                try {
                    tvReader.stop()
                    tvReader.read(tvGuide.timeCursor, tvGuide)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            if (keyPressed == config.joyItChannelDown) {
                println("Channel Down pressed")
                val tvGuide = InteractiveTVGuide(config, builder.todayProgram, now!!, channelCursor)
                channelCursor = tvGuide.channelDown()
                try {
                    tvReader.stop()
                    tvReader.read(tvGuide.timeCursor, tvGuide)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            if (keyPressed == config.joyItTimeUp) {
                println("Time UP pressed")
                val tvGuide = InteractiveTVGuide(config, builder.todayProgram, now!!, channelCursor)
                now = tvGuide.timeUp()
                try {
                    tvReader.stop()
                    tvReader.read(tvGuide.timeCursor, tvGuide)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            if (keyPressed == config.joyItTimeDown) {
                println("Time Down pressed")
                val tvGuide = InteractiveTVGuide(config, builder.todayProgram, now!!, channelCursor)
                now = tvGuide.timeDown()
                try {
                    tvReader.stop()
                    tvReader.read(tvGuide.timeCursor, tvGuide)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        println("Keep program running until user aborts (CTRL-C)")
        try {
            while (true) {
                Thread.sleep(1000)
            }
        } finally {
            keyboard.shutdown()
        }
    }

    companion object {

        private val `object` = Any()
    }


}
