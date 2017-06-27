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

import com.waltercedric.tvprogram.pi.runner.Runner
import com.waltercedric.tvprogram.plugins.mapping.TimeToTextConverter
import com.waltercedric.tvprogram.plugins.player.Player
import com.waltercedric.tvprogram.plugins.reader.TTSReader
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder

import java.io.FileReader
import java.io.InputStream
import java.util.Arrays
import java.util.Properties
import java.util.Collections.addAll
import com.sun.org.apache.xml.internal.utils.StringBufferPool.free


class Config {

    val fromto_introduction: String
    val fromto_each: String

    val iam_access: String
    val iam_secret: String
    val aws_region: String
    val voiceid: String

    val timeIncrement: Int

    val joyItChannelUp: Int
    val joyItChannelDown: Int
    val joyItTimeUp: Int
    val joyItTimeDown: Int

    val sentenceNow_each: String
    val sentenceNow_introduction: String
    val voice: String

    val free: MutableList<String>
    val channels: MutableList<String>

    val premium: List<String>
    val usePremium: Boolean
    val channelUpPin: String
    val channelDownPin: String
    val timeUpPin: String
    val timeDownPin: String
    val interactiveTVGuide_introduction: String
    val interactiveTVGuide_each: String

    val runnerValue: String
    val timeToTextValue: String
    val tvGuideRunnerValue: String
    val audioPlayerRunnerValue: String

    val ttsReader: TTSReader
        get() {
            var aTTSReader: TTSReader
            try {
                aTTSReader = Class.forName(ttsReaderValue).newInstance() as TTSReader
            } catch (e: Exception) {
                throw RuntimeException("TTSReader class is wrongly set", e)
            }


            if (aTTSReader.javaClass.simpleName.equals("PollyTTSReader")) {
                if ("" == iam_access || "" == iam_secret) {
                    throw Exception("if you want to use Amazon Polly you have to provide 'TVReader.PollyTTSReader.IAM-access' and 'TVReader.PollyTTSReader.IAM-secret' value")
                }
            }

            return aTTSReader
        }

    val tvProgramBuilder: TVProgramBuilder
        get() {
            try {
                return Class.forName(runnerValue).newInstance() as TVProgramBuilder
            } catch (e: Exception) {
                throw RuntimeException("TVProgramBuilder class is wrongly set", e)
            }
        }

    val timeToTextConverter: TimeToTextConverter
        get() {
            try {
                return Class.forName(timeToTextValue).newInstance() as TimeToTextConverter
            } catch (e: Exception) {
                throw RuntimeException("TimeToTextConverter key class is wrongly set", e)
            }
        }

    val runner: Runner
        get() {
            try {
                return Class.forName(runnerValue).newInstance() as Runner
            } catch (e: Exception) {
                throw RuntimeException("InteractiveTVGuide.runner class is wrongly set", e)
            }
        }

    val player: Player
        get() {
            try {
                return Class.forName(audioPlayerRunnerValue).newInstance() as Player
            } catch (e: Exception) {
                throw RuntimeException("Player class is wrongly set", e)
            }
        }

    val ttsReaderValue: String

    init {
        val resourceAsStream = javaClass.getResourceAsStream("/config.properties")
        val props = Properties()
        try {
            if (resourceAsStream != null) {
                props.load(resourceAsStream)
            } else {
                props.load(FileReader("config.properties"))
            }

            premium = Arrays.asList(*props.getProperty("premium").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            free = Arrays.asList(*props.getProperty("free").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            usePremium = java.lang.Boolean.valueOf(props.getProperty("use.premium"))!!
            voice = props.getProperty("voice")

            sentenceNow_introduction = props.getProperty("TVGuideNow.introduction")
            sentenceNow_each = props.getProperty("TVGuideNow.each")

            interactiveTVGuide_introduction = props.getProperty("InteractiveTVGuide.introduction")
            interactiveTVGuide_each = props.getProperty("InteractiveTVGuide.each")

            fromto_introduction = props.getProperty("TVGuideFromTo.introduction")
            fromto_each = props.getProperty("TVGuideFromTo.each")

            iam_access = props.getProperty("TVReader.PollyTTSReader.IAM-access")
            iam_secret = props.getProperty("TVReader.PollyTTSReader.IAM-secret")


            aws_region = props.getProperty("TVReader.PollyTTSReader.region")
            voiceid = props.getProperty("TVReader.PollyTTSReader.voiceid")

            channelUpPin = props.getProperty("CustomPiRunner.gpio.channel.up")
            channelDownPin = props.getProperty("CustomPiRunner.gpio.channel.down")
            timeUpPin = props.getProperty("CustomPiRunner.gpio.time.up")
            timeDownPin = props.getProperty("CustomPiRunner.gpio.time.down")

            joyItChannelUp = Integer.parseInt(props.getProperty("JoyItKeyboardPiRunner.channel.up"))
            joyItChannelDown = Integer.parseInt(props.getProperty("JoyItKeyboardPiRunner.channel.down"))
            joyItTimeUp = Integer.parseInt(props.getProperty("JoyItKeyboardPiRunner.time.up"))
            joyItTimeDown = Integer.parseInt(props.getProperty("JoyItKeyboardPiRunner.time.down"))

            timeIncrement = Integer.valueOf(props.getProperty("InteractiveTVGuide.time.increment"))

            ttsReaderValue = props.getProperty("TTSReader");
            runnerValue = props.getProperty("TVProgramBuilder")
            timeToTextValue = props.getProperty("TimeToTextConverter")
            tvGuideRunnerValue = props.getProperty("InteractiveTVGuide.runner")
            audioPlayerRunnerValue = props.getProperty("AudioPlayer")

            channels = free
            if (usePremium) {
                channels.addAll(premium)
            }

        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

}
