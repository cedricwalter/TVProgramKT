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
package com.waltercedric.tvprogram.utils


import java.util.Arrays

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

internal class CircularListTest {

    @Test
    fun withCircularList_get_expectValues() {
        // arrange
        val strings = CircularList<String>()
        strings.addAll(Arrays.asList("A", "B", "C", "D"))

        // act + assert
        assertThat(strings[0], `is`("A"))
        assertThat(strings[1], `is`("B"))
        assertThat(strings[2], `is`("C"))
        assertThat(strings[3], `is`("D"))
        assertThat(strings[4], `is`("A"))
        assertThat(strings[5], `is`("B"))
        assertThat(strings[-2], `is`("C"))
    }

}