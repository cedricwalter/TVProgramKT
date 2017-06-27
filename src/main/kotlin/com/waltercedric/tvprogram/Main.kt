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
package com.waltercedric.tvprogram

import com.pi4j.io.gpio.*
import com.pi4j.io.gpio.event.GpioPinListenerDigital
import com.waltercedric.tvprogram.guide.TVGuideFromTo
import com.waltercedric.tvprogram.guide.TVGuideNow
import java.time.LocalTime

internal object Main {

    private val `object` = Any()
    private val config = Config()

    @Throws(Exception::class)
    @JvmStatic fun main(args: Array<String>) {
        if ("pi" == args[0]) {
            executeForPI()
        } else if ("now" == args[0]) {
            executeNowOnTV()
        } else if ("program" == args[0]) {
            executeTVProgram(args)
        } else if ("interactive" == args[0]) {
            config.runner.execute()
        }
    }

    /**
     * read GPIO pin, need to run as root to be able to read pin

     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    private fun executeForPI() {
        // create gpio controller
        val gpio = GpioFactory.getInstance()

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        val digitalButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN)
        digitalButton.setShutdownOptions(true)
        digitalButton.addListener({ event: GpioPinDigital ->
            System.out.println(" --> Digital: " + event.getPin() + " = " + event.getState())

            if (event.getState().equals(PinState.HIGH)) {
                synchronized(`object`) {
                    try {
                        executeNowOnTV()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        } as GpioPinListenerDigital)

        println("Keep program running until user aborts (CTRL-C)")
        while (true) {
            Thread.sleep(1000)
        }
    }

    /**
     * You may want to call this after user press on a button

     * @throws Exception
     */
    @Throws(Exception::class)
    private fun executeNowOnTV() {
        val config = Config()
        val builder = config.tvProgramBuilder
        val now = LocalTime.now()
        val guideNow = TVGuideNow(config, builder.todayProgram, now)
        TVReader().read(now, guideNow)
    }

    /**
     * You may want to call this in a crontab

     * @param args
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun executeTVProgram(args: Array<String>) {
        val config = Config()
        val builder = config.tvProgramBuilder
        val now = LocalTime.now()
        val guideFromTo = TVGuideFromTo(config, builder.todayProgram, LocalTime.parse(args[1]), LocalTime.parse(args[2]))
        TVReader().read(now, guideFromTo)
    }

}