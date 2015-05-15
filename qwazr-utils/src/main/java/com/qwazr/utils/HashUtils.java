/**
 * Copyright 2015 Emmanuel Keller / QWAZR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qwazr.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HashUtils {

	public static final int getMurmur3Mod(final String hashString, final int mod) {
		HashFunction m3 = Hashing.murmur3_128();
		return (Math.abs(m3.hashString(hashString).asInt()) % mod);
	}
}
