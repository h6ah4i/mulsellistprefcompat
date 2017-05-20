/*
 * Copyright (C) 2012 Haruki Hasegawa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.multiselectlistpreferencecompatexample;

import java.util.Arrays;
import java.util.Set;

public final class Utils {
    private Utils() {
        // hide constructor
    }

    public static String sortedToString(Set<String> values) {
        // sort items
        String[] sorted = new String[values.size()];
        values.toArray(sorted);
        Arrays.sort(sorted);

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < sorted.length; i++) {
            if (i > 0)
                builder.append(", ");

            builder.append(sorted[i]);
        }
        builder.append("]");

        return builder.toString();
    }
}
