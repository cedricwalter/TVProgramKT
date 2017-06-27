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

import com.pi4j.io.gpio.*
import com.pi4j.io.gpio.event.GpioPinListenerDigital
import com.waltercedric.tvprogram.Config
import com.waltercedric.tvprogram.TVReader
import com.waltercedric.tvprogram.guide.InteractiveTVGuide
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder

import java.time.LocalTime


class CustomPiRunner : Runner {
    private val tvReader: TVReader
    private val config: Config
    private var now: LocalTime? = null
    private var channelCursor: Int = 0
    private val `object` = Any()
    private var gpio: GpioController

    init {
        gpio = GpioFactory.getInstance()
        tvReader = TVReader()
        config = Config()
        channelCursor = 0
        now = LocalTime.now()
    }

    @Throws(InterruptedException::class)
    override fun execute() {
        val builder = config.tvProgramBuilder

        println("Starting listener for channel up on pin " + config.channelUpPin)
        val channelUp = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.channelUpPin), PinPullResistance.PULL_DOWN)
        channelUp.setShutdownOptions(true)
        channelUp.addListener({ event: GpioPinDigital ->
            if (event.getState().equals(PinState.HIGH)) {
                synchronized(`object`) {
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
            }
        } as GpioPinListenerDigital)

        println("Starting listener for channel down on pin " + config.channelDownPin)
        val channelDown = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.channelDownPin), PinPullResistance.PULL_DOWN)
        channelDown.setShutdownOptions(true)
        channelDown.addListener({ event: GpioPinDigital ->
            if (event.getState().equals(PinState.HIGH)) {
                synchronized(`object`) {
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
            }
        } as GpioPinListenerDigital)

        println("Starting listener for time up on pin " + config.timeUpPin)
        val timeUp = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.timeUpPin), PinPullResistance.PULL_DOWN)
        timeUp.setShutdownOptions(true)
        timeUp.addListener({ event: GpioPinDigital ->
            if (event.getState().equals(PinState.HIGH)) {
                synchronized(`object`) {
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
            }
        } as GpioPinListenerDigital)

        println("Starting listener for time down on pin " + config.timeDownPin)
        val timeDown = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.timeDownPin), PinPullResistance.PULL_DOWN)
        timeDown.setShutdownOptions(true)
        timeDown.addListener({ event: GpioPinDigital ->
            if (event.getState().equals(PinState.HIGH)) {
                synchronized(`object`) {
                    println("Time down pressed")
                    val tvGuide = InteractiveTVGuide(config, builder.todayProgram, now!!, channelCursor)
                    now = tvGuide.timeDown()
                    try {
                        tvReader.stop()
                        tvReader.read(tvGuide.timeCursor!!, tvGuide)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        } as GpioPinListenerDigital)

        println("Now listening to PIN changes, keep program running until user aborts (CTRL-C)")

        try {
            while (true) {
                Thread.sleep(200)
            }
        } finally {
            gpio.shutdown()
        }
    }


}
