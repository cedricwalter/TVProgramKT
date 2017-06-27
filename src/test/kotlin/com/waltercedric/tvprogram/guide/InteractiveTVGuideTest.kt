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
import java.util.Arrays

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mockito

internal class InteractiveTVGuideTest {

    private val tvProgram1 = TVProgram("channel1", "anyTitle", "anyCategory", "anyDescription", LocalTime.of(9, 5), LocalTime.of(9, 20))
    private val tvProgram2 = TVProgram("channel2", "anyTitle", "anyCategory", "anyDescription", LocalTime.of(9, 5), LocalTime.of(9, 25))
    private val tvProgram3 = TVProgram("channel3", "anyTitle", "anyCategory", "anyDescription", LocalTime.of(9, 5), LocalTime.of(9, 35))

    @Test
    fun withTVProgram_channelUp_expectTvProgram2() {
        // arrange
        val config = mockConfigWith3Channel()

        val list = tvProgram

        val interactiveTVGuide = InteractiveTVGuide(config, list, LocalTime.of(9, 10), 0)

        // act
        interactiveTVGuide.channelUp()

        //assert
        val program = interactiveTVGuide.program
        assertThat(program.size, `is`(1))
        assertThat(program[0], `is`(tvProgram2))
    }

    @Test
    fun withTVProgram_channelUpRepetition_expectTvProgram3() {
        // arrange
        val config = mockConfigWith3Channel()

        val list = tvProgram

        val interactiveTVGuide = InteractiveTVGuide(config, list, LocalTime.of(9, 10), 0)

        // act
        interactiveTVGuide.channelUp()
        interactiveTVGuide.channelUp()


        //assert
        val program = interactiveTVGuide.program
        assertThat(program.size, `is`(1))
        assertThat(program[0], `is`(tvProgram3))
    }

    @Test
    fun withTVProgram_channelUpRepetitionChannelDown_expectTvProgram2() {
        // arrange
        val config = mockConfigWith3Channel()

        val list = tvProgram

        val interactiveTVGuide = InteractiveTVGuide(config, list, LocalTime.of(9, 10), 0)

        // act
        interactiveTVGuide.channelUp()  //bad to use up to test down
        interactiveTVGuide.channelUp()
        interactiveTVGuide.channelDown()

        //assert
        val program = interactiveTVGuide.program
        assertThat(program.size, `is`(1))
        assertThat(program[0], `is`(tvProgram2))
    }

    @Test
    fun withTVProgram_nowIsLate_expectNoTVProgram() {
        // arrange
        val config = mockConfigWith3Channel()

        val list = tvProgram

        val interactiveTVGuide = InteractiveTVGuide(config, list, LocalTime.of(10, 0), 0)

        // act
        val program = interactiveTVGuide.program

        //assert
        assertThat(program.size, `is`(0))
    }

    @Test
    fun withTVProgram_nowIsLateButTimeDown_expectTVProgram1OnChannel1() {
        // arrange
        val config = mockConfigWith3Channel()

        val list = tvProgram

        val interactiveTVGuide = InteractiveTVGuide(config, list, LocalTime.of(9, 30), 0)

        // act
        interactiveTVGuide.timeDown() // channel1 is still set

        //assert
        val program = interactiveTVGuide.program
        assertThat(program.size, `is`(1))
        assertThat(program[0], `is`(tvProgram1))
    }

    private val tvProgram: List<TVProgram>
        get() {
            val list = ArrayList<TVProgram>()
            list.add(tvProgram1)
            list.add(tvProgram2)
            list.add(tvProgram3)

            return list
        }

    private fun mockConfigWith3Channel(): Config {
        val config = Mockito.mock(Config::class.java)
        Mockito.`when`(config.free).thenReturn(Arrays.asList<String>("channel1", "channel2", "channel3"))
        Mockito.`when`(config.channels).thenReturn(Arrays.asList<String>("channel1", "channel2", "channel3"))

        return config
    }

}