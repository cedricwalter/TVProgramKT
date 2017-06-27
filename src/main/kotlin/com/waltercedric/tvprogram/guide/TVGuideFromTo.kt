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

import java.time.LocalTime
import java.util.ArrayList


class TVGuideFromTo(private val config: Config, private val programs: List<TVProgram>, private val from: LocalTime, private val to: LocalTime) : TVGuide {

    override val program: List<TVProgram>
        get() {
            val programs = ArrayList<TVProgram>()
            for (program in this.programs) {
                val startingAtTime = program.startTime.isAfter(from)
                val startingBeforeEnd = program.startTime.isBefore(to)
                if (startingBeforeEnd && startingAtTime) {

                    if (config.free.contains(program.channel)) {
                        programs.add(program)
                    }
                    if (config.usePremium) {
                        if (config.premium.contains(program.channel)) {
                            programs.add(program)
                        }
                    }
                }
            }

            return programs
        }

    override val introduction: String
        get() = config.fromto_introduction

    override val forEachProgram: String
        get() = config.fromto_each

    override fun toString(): String {
        return "TVGuideFromTo{" +
                "programs=" + programs +
                ", from=" + from +
                ", to=" + to +
                ", config=" + config +
                "} \n"
    }
}
