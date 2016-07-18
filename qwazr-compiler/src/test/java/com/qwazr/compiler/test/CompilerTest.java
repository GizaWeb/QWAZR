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
package com.qwazr.compiler.test;

import com.qwazr.classloader.ClassLoaderManager;
import com.qwazr.compiler.CompilerManager;
import com.qwazr.compiler.CompilerServiceImpl;
import com.qwazr.utils.server.ServerBuilder;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompilerTest {

	@Test
	public void test000loadManager() throws IOException {
		final File dataDir = Files.createTempDirectory("compiler_test").toFile();
		System.setProperty("QWAZR_DATA", dataDir.getAbsolutePath());
		ClassLoaderManager.load(dataDir, Thread.currentThread());
		Assert.assertNotNull(ClassLoaderManager.getInstance());
		CompilerManager.load(new ServerBuilder());
		Assert.assertNotNull(CompilerManager.getInstance());
	}

	@Test
	public void test500getStatus() {
		Assert.assertNotNull(new CompilerServiceImpl().get());
	}
}