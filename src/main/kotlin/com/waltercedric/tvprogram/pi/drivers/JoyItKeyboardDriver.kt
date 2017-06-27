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
package com.waltercedric.tvprogram.pi.drivers

import com.pi4j.io.gpio.*
import com.pi4j.io.gpio.event.GpioPinListenerDigital

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.util.ArrayList

class JoyItKeyboardDriver {

    private val listener = ArrayList<PropertyChangeListener>()
    var keyPressed: Int = 0
        private set

    private val gpio = GpioFactory.getInstance()

    // 1,2,3,4 = columns = input
    private val thePin1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, PinPullResistance.PULL_UP)
    private val thePin2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, PinPullResistance.PULL_UP)
    private val thePin3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_17, PinPullResistance.PULL_UP)
    private val thePin4 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP)

    // 5,6,7,8 = lines = output
    private val outputPins = arrayOf<GpioPinDigitalOutput>(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18), gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23), gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24), gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25))

    private var theInput: GpioPinDigitalInput? = null

    private var lineSelected: Int = 0
    private var columnSelected: Int = 0

    init {
        thePin1.addListener({ event: GpioPinDigital ->
            if (event.getState() === PinState.LOW) {
                theInput = thePin1
                lineSelected = 1
                findOutput()
            }
        } as GpioPinListenerDigital)
        thePin2.addListener({ event: GpioPinDigital ->
            if (event.getState() === PinState.LOW) {
                theInput = thePin2
                lineSelected = 2
                findOutput()
            }
        } as GpioPinListenerDigital)
        thePin3.addListener({ event: GpioPinDigital ->
            if (event.getState() === PinState.LOW) {
                theInput = thePin3
                lineSelected = 3
                findOutput()
            }
        } as GpioPinListenerDigital)
        thePin4.addListener({ event: GpioPinDigital ->
            if (event.getState() === PinState.LOW) {
                theInput = thePin4
                lineSelected = 4
                findOutput()
            }
        } as GpioPinListenerDigital)
    }

    /**
     * Find output.
     *
     *
     * Sets output lines to high and then to low one by one. Then the input line
     * is tested. If its state is low, we have the right output line and
     * therefore a mapping to a key on the keypad.
     */
    private fun findOutput() {
        // now test the inputs by setting the outputs from high to low
        // one by one
        for (outputPinCounter in outputPins.indices) {
            for (outputPin in outputPins) {
                outputPin.high()
            }

            outputPins[outputPinCounter].low()

            // input found?
            if (theInput!!.isLow()) {
                columnSelected = outputPinCounter
                checkPins()
                try {
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    //do nothing
                }

                break
            }
        }

        for (myTheOutput in outputPins) {
            myTheOutput.low()
        }
    }

    /**
     * Check pins.
     * Determines the pressed key based on the activated GPIO pins.
     */
    @Synchronized private fun checkPins() {
        val oldValue = this.keyPressed
        this.keyPressed = keypad[lineSelected - 1][columnSelected]
        val newValue = this.keyPressed

        for (name in listener) {
            name.propertyChange(PropertyChangeEvent(this, KEY,
                    oldValue, newValue))
        }
        println(keypad[lineSelected - 1][columnSelected])
    }

    fun shutdown() {
        gpio.shutdown()
    }

    companion object {

        private val KEY = "key"

        private val keypad = arrayOf(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 14, 15, 16))
    }
}