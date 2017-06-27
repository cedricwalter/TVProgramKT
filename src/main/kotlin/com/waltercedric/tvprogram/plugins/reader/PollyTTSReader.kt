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

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.polly.AmazonPollyAsync
import com.amazonaws.services.polly.AmazonPollyAsyncClient
import com.amazonaws.services.polly.model.OutputFormat
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest
import com.waltercedric.tvprogram.Config
import com.waltercedric.tvprogram.plugins.player.Player

class PollyTTSReader : TTSReader {
    private val polly: AmazonPollyAsync
    private val myplayer: Player
    private val config = Config()

    init {
        val awsCreds = BasicAWSCredentials(config.iam_access, config.iam_secret)

        polly = AmazonPollyAsyncClient.asyncBuilder()
                .withCredentials(AWSStaticCredentialsProvider(awsCreds))
                .withRegion(config.aws_region).build()

        myplayer = config.player
    }

    override fun play(sentenceToPlay: String) {
        var sentenceToPlay = sentenceToPlay
        synchronized(`object`) {
            val tssRequest = newRequest()

            // TextLengthExceededException
            // The value of the "Text" parameter is longer than the accepted limits. The limit for input text is a
            // maximum of 3000 characters total, of which no more than 1500 can be billed characters.
            // SSML tags are not counted as billed characters.
            // HTTP Status Code: 400
            if (sentenceToPlay.length > 3000) {
                sentenceToPlay = sentenceToPlay.substring(0, 2999)
            }

            tssRequest.setText(sentenceToPlay)

            val synthesizeSpeechResultFuture = polly.synthesizeSpeechAsync(tssRequest)
            try {
                val audioStream = synthesizeSpeechResultFuture.get().getAudioStream()
                myplayer.play(audioStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun stop() {
        myplayer.stopPlayer()
    }

    private fun newRequest(): SynthesizeSpeechRequest {
        val tssRequest = SynthesizeSpeechRequest()
        tssRequest.setVoiceId(config.voiceid)
        tssRequest.setOutputFormat(OutputFormat.Mp3)

        return tssRequest
    }

    companion object {

        private val `object` = Any()
    }

}
