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

import java.util.ArrayList

// do not work for mutable list, only support ArrayList, used only for channels
class CircularList<E> : ArrayList<E>() {

    override fun get(index: Int): E {

        val listSize = super.size
        var indexToGet = index % listSize

        //might happen to be negative
        indexToGet = if (indexToGet < 0) indexToGet + listSize else indexToGet

        return super.get(indexToGet)
    }
}