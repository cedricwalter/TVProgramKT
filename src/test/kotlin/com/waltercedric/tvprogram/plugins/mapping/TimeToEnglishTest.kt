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

import org.junit.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

class TimeToEnglishTest {

    @Test
    fun with() {
        val timeToEnglish = TimeToEnglish()
        val text = timeToEnglish.convertTimeToText("1:15")

        assertThat(text, `is`("Quarter past One"))
    }

}