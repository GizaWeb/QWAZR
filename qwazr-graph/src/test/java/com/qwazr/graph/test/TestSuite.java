/**
 * Copyright 2015-2016 Emmanuel Keller / QWAZR
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qwazr.graph.test;

import com.qwazr.database.store.KeyStore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestSuite.LevelDbTest.class/*, TestSuite.LmdbTest.class*/ })
public class TestSuite {

	public static class LevelDbTest extends FullTest {

		@Override
		protected KeyStore.Impl getStoreImplementation() {
			return KeyStore.Impl.leveldb;
		}
	}

	public static class LmdbTest extends FullTest {

		@Override
		protected KeyStore.Impl getStoreImplementation() {
			return KeyStore.Impl.lmdb;
		}
	}

}
