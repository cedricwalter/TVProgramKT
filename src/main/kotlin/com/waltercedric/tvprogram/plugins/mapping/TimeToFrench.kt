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
package com.waltercedric.tvprogram.plugins.mapping

class TimeToFrench : TimeToTextConverter {


    override fun convertTimeToText(value: String): String {
        val split = value.split("\\:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        return getTimeName(Integer.valueOf(split[0])!!, Integer.valueOf(split[1])!!)
    }

    private fun getTimeName(hours: Int, minutes: Int): String {
        val timeName: String

        if (minutes == 0) {
            if (hours == 0) {
                timeName = "minuit"
            } else if (hours == 12) {
                timeName = "midi"
            } else {
                timeName = translation[hours] + " heures pile"
            }
        } else if (minutes == 15) {
            if (hours == 0) {
                timeName = "minuit et quart"
            } else {
                timeName = translation[hours] + " heures et quart"
            }
        } else if (minutes == 30) {
            if (hours == 0) {
                timeName = "minuit et demie"
            } else {
                timeName = translation[hours] + " heures et demie"
            }
        } else if (minutes == 45) {
            if (hours == 23) {
                timeName = "minuit moins le quart"
            } else {

                timeName = translation[hours + 1] + " heures moins le quart"
            }
        } else {
            if (hours == 0) {
                timeName = "minuit " + translation[minutes]
            } else {
                timeName = translation[hours] + " heures " + translation[minutes]
            }
        }

        return timeName
    }

    companion object {

        private val translation = arrayOf("minuit", "une", "deux", "trois", "quatre", "cinq", "Six", "sept", "huit", "neuf", "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix sept", "dix huit", "dix neuf", "vingt", "vingt un", "vingt deux", "vingt trois", "vingt quatre", "vingt cinq", "vingt six", "vingt sept", "vingt huit", "vingt neuf", "trente", "trente et un", "trente deux", "trente trois", "trente quatre", "trente cinq", "trente six", "trente sept", "trente huit", "trente neuf", "quarannte", "quarannte et un", "quarannte deux", "quarannte trois", "quarannte quatre", "quarannte cinq", "quarannte six", "quarannte sept", "quarannte huit", "quarannte neuf", "cinquante", "cinquante et un", "cinquante deux", "cinquante trois", "cinquante quatre", "cinquante cinq", "cinquante six", "cinquante sept", "cinquante huit", "cinquante neuf")
    }

}
