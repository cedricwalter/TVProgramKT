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
package com.waltercedric.tvprogram.guide

import com.waltercedric.tvprogram.Config
import com.waltercedric.tvprogram.TVProgram
import com.waltercedric.tvprogram.utils.CircularList

import java.time.LocalTime
import java.util.ArrayList

class InteractiveTVGuide(private val config: Config, private val programs: List<TVProgram>, now: LocalTime, private var channelCursor: Int) : TVGuide {
    private val channels: CircularList<String>

    var timeCursor: LocalTime
        private set

    init {

        this.timeCursor = now

        this.channels = CircularList()
        channels.addAll(config.channels)
    }

    fun timeUp(): LocalTime {
        this.timeCursor = this.timeCursor.plusMinutes(config.timeIncrement.toLong())

        return this.timeCursor
    }

    fun timeDown(): LocalTime {
        this.timeCursor = this.timeCursor.minusMinutes(config.timeIncrement.toLong())

        return this.timeCursor
    }

    fun channelUp(): Int {
        this.channelCursor += 1

        return this.channelCursor
    }

    fun channelDown(): Int {
        this.channelCursor -= 1

        return this.channelCursor
    }

    // filter by time cursor
    // filter by selected channel
    override val program: List<TVProgram>
        get() {
            val tvGuideNow = TVGuideNow(config, programs, timeCursor)
            val programOnChannelAtThatTime = ArrayList<TVProgram>()
            val desiredChannel = channels.get(channelCursor)

            for (program in tvGuideNow.program) {
                if (program.channel == desiredChannel) {
                    programOnChannelAtThatTime.add(program)
                }
            }

            return programOnChannelAtThatTime
        }

    override val introduction: String
        get() = config.interactiveTVGuide_introduction

    override val forEachProgram: String
        get() = config.interactiveTVGuide_each
}
