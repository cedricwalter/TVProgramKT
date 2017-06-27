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

class TimeToFrenchTest {

    @Test
    fun with() {
        val timeToFrench = TimeToFrench()

        assertThat(timeToFrench.convertTimeToText("12:00"), `is`("midi"))
        assertThat(timeToFrench.convertTimeToText("12:12"), `is`("douze heures douze"))
        assertThat(timeToFrench.convertTimeToText("12:30"), `is`("douze heures et demie"))
        assertThat(timeToFrench.convertTimeToText("12:15"), `is`("douze heures et quart"))
        assertThat(timeToFrench.convertTimeToText("12:45"), `is`("treize heures moins le quart"))
        assertThat(timeToFrench.convertTimeToText("13:00"), `is`("treize heures pile"))
        assertThat(timeToFrench.convertTimeToText("23:45"), `is`("minuit moins le quart"))
        assertThat(timeToFrench.convertTimeToText("17:45"), `is`("dix huit heures moins le quart"))
        assertThat(timeToFrench.convertTimeToText("9:45"), `is`("dix heures moins le quart"))
        assertThat(timeToFrench.convertTimeToText("18:20"), `is`("dix huit heures vingt"))
        assertThat(timeToFrench.convertTimeToText("00:00"), `is`("minuit"))
        assertThat(timeToFrench.convertTimeToText("00:15"), `is`("minuit et quart"))
        assertThat(timeToFrench.convertTimeToText("00:30"), `is`("minuit et demie"))
        assertThat(timeToFrench.convertTimeToText("00:45"), `is`("une heures moins le quart"))
        assertThat(timeToFrench.convertTimeToText("00:58"), `is`("minuit cinquante huit"))
    }

}