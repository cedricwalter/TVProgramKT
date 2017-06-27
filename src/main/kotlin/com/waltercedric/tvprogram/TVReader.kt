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

import com.waltercedric.tvprogram.guide.TVGuide
import com.waltercedric.tvprogram.plugins.mapping.TimeToTextConverter
import com.waltercedric.tvprogram.plugins.reader.TTSReader
import freemarker.template.Configuration
import freemarker.template.Template

import java.io.StringReader
import java.io.StringWriter
import java.time.LocalTime
import java.util.HashMap

class TVReader {
    private val timeToTextConverter: TimeToTextConverter
    private val TTSReader: TTSReader

    init {
        this.timeToTextConverter = config.timeToTextConverter
        this.TTSReader = config.ttsReader
    }

    @Throws(Exception::class)
    fun read(time: LocalTime, guide: TVGuide) {
        val programs = guide.program

        TTSReader.play(guide.introduction)
        for (tvProgram in programs) {
            play(time, tvProgram, guide.forEachProgram)
        }
    }

    @Throws(Exception::class)
    private fun play(time: LocalTime, tvProgram: TVProgram, sentence: String) {
        val template = Template("templateName", StringReader(sentence), Configuration())

        val parameters = HashMap<String, String>()
        parameters.put("channel", tvProgram.channel)
        parameters.put("title", tvProgram.title)
        parameters.put("description", tvProgram.description)
        parameters.put("category", tvProgram.category)
        parameters.put("start", timeToTextConverter.convertTimeToText(tvProgram.startTime.toString()))
        parameters.put("end", timeToTextConverter.convertTimeToText(tvProgram.endTime.toString()))
        parameters.put("duration", tvProgram.duration!!.toString())
        parameters.put("left", tvProgram.restTime.toString())
        parameters.put("time", time.hour.toString() + ":" + time.minute)

        val output = StringWriter()
        template.process(parameters, output)

        val sentenceToPlay = output.toString()

        TTSReader.play(sentenceToPlay)
    }

    fun stop() {
        TTSReader.stop()
    }

    companion object {

        private val config = Config()
    }

}
