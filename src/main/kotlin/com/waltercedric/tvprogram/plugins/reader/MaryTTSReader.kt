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
package com.waltercedric.tvprogram.plugins.reader

import com.waltercedric.tvprogram.Config
import com.waltercedric.tvprogram.plugins.player.AudioPlayerThread
import marytts.LocalMaryInterface
import marytts.MaryInterface
import marytts.exceptions.MaryConfigurationException

class MaryTTSReader : TTSReader {
    private val maryTTS: MaryInterface
    private val myplayer: AudioPlayerThread?

    init {
        try {
            maryTTS = LocalMaryInterface()
        } catch (e: MaryConfigurationException) {
            throw RuntimeException(e)
        }

        maryTTS.setVoice(config.voice)

        myplayer = AudioPlayerThread()
    }

    override fun play(sentenceToPlay: String) {
        synchronized(`object`) {
            try {
                if ("" != sentenceToPlay) {
                    val audioInputStream = maryTTS.generateAudio(sentenceToPlay)
                    myplayer!!.play(audioInputStream)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun stop() {
        myplayer?.stopPlayer()
    }

    companion object {
        private val config = Config()
        private val `object` = Any()
    }

}
