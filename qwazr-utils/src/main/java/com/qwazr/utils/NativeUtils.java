/**
 * Copyright 2014-2015 OpenSearchServer Inc.
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeUtils {

	private final static Logger logger = LoggerFactory
			.getLogger(NativeUtils.class);

	public final static String NATIVE_OSS_LIBNAME = "oss-native-utils";
	public final static String NATIVE_OSS_MAPPED_LIBNAME = System
			.mapLibraryName(NATIVE_OSS_LIBNAME);

	private static Boolean LOADED = null;

	public synchronized final static boolean loaded() {
		if (LOADED != null)
			return LOADED;
		try {
			loadLibrary(NATIVE_OSS_LIBNAME);
			LOADED = true;
		} catch (Throwable t) {
			LOADED = false;
		}
		return LOADED;
	}

	public static void loadLibrary(String libraryName)
			throws UnsatisfiedLinkError, UnsupportedOperationException {

		try {
			System.loadLibrary(libraryName);
			logger.info("Native OSS loaded from classpath:" + libraryName);
		} catch (UnsatisfiedLinkError e1) {

			String libraryResourcePath = '/' + System
					.mapLibraryName(libraryName);

			InputStream inputStream = NativeUtils.class
					.getResourceAsStream(libraryResourcePath);
			if (inputStream == null)
				throw new UnsatisfiedLinkError("Can't find " + libraryName
						+ " in the filesystem nor in the classpath: "
						+ e1.getMessage());

			try {
				File libraryFile = File.createTempFile(libraryName, null);
				IOUtils.copy(inputStream, libraryFile, true);
				System.load(libraryFile.getAbsolutePath());
				logger.info("Native OSS loaded from temp file:" + libraryName);

			} catch (IOException e) {
				throw new Error("Failed to create temporary file for "
						+ libraryName, e);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		}
	}

}
