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
package com.qwazr.profiler.test.profiled;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfiledClass extends ProfiledAbstractClass {

	final public static AtomicInteger constructorCount = new AtomicInteger();
	final public static AtomicInteger testParamCount = new AtomicInteger();
	final public static AtomicLong testParamTime = new AtomicLong();
	final public static AtomicInteger testExCount = new AtomicInteger();
	final public static AtomicLong testExTime = new AtomicLong();

	private final double d1 = RandomUtils.nextDouble();
	private final double d2 = RandomUtils.nextDouble();
	private final double d3 = RandomUtils.nextDouble();

	public ProfiledClass() {
		constructorCount.incrementAndGet();
	}

	public void test(int waitBase) throws InterruptedException {
		wait(testParamCount, testParamTime, waitBase);
	}

	public void testEx() throws InterruptedException {
		wait(testExCount, testExTime, 250);
		throw new InterruptedException();
	}

	public double[] toArray() {
		return new double[]{d1, d2, d3};
	}

	public static class InnerClass {

		final public static AtomicInteger testCount = new AtomicInteger();

		public void test() {
			testCount.incrementAndGet();
		}
	}
}
