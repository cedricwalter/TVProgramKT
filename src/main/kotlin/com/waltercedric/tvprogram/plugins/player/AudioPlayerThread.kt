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
package com.waltercedric.tvprogram.plugins.player

import marytts.util.data.audio.AudioPlayer

import javax.sound.sampled.AudioInputStream

class AudioPlayerThread : Runnable {
    private var player: AudioPlayer? = null
    private var playerThread: Thread? = null

    override fun run() {
        if (player != null) {
            player!!.run()
        }
    }

    fun play(audioStream: AudioInputStream) {
        stopPlayer()

        player = AudioPlayer()
        player!!.setAudio(audioStream)
        player!!.setPriority(10)
        playerThread = Thread(this, "Audio player thread")
        playerThread!!.start()
    }

    fun stopPlayer() {
        if (player != null) {
            player!!.cancel()
            player = null
            playerThread = null
        }
    }
}
