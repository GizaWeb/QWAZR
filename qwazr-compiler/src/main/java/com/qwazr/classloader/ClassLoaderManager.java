/**
 * Copyright 2016 Emmanuel Keller / QWAZR
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
 **/
package com.qwazr.classloader;

import com.qwazr.utils.ClassLoaderUtils;
import com.qwazr.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public class ClassLoaderManager {

	private static volatile ClassLoaderManager INSTANCE = null;

	final public static ClassLoaderManager getInstance() {
		Objects.requireNonNull(INSTANCE, "The ClassloaderManager has not been initialized");
		return INSTANCE;
	}

	public static void load(final File dataDirectory, final Thread mainThread) throws IOException {
		if (INSTANCE != null)
			throw new IOException("Already loaded");
		INSTANCE = new ClassLoaderManager(dataDirectory, mainThread == null ? Thread.currentThread() : mainThread);
	}

	public static volatile URLClassLoader classLoader = null;

	final public static <T> Class<T> findClass(final String className) throws ClassNotFoundException {
		return ClassLoaderUtils.findClass(classLoader, className);
	}

	final public static InputStream getResourceAsStream(final String name) {
		return ClassLoaderUtils.getResourceAsStream(classLoader, name);
	}

	public final File javaResourceDirectory;
	public final File javaClassesDirectory;
	public final File javaLibrariesDirectory;

	private final ClassLoader parentClassLoader;
	private final Thread mainThread;

	private final URL[] urls;

	private volatile ClassFactory classFactory;

	private ClassLoaderManager(final File dataDirectory, final Thread mainThread) throws MalformedURLException {
		this.mainThread = mainThread;
		this.classFactory = ClassFactory.DefaultFactory.INSTANCE;
		this.parentClassLoader = mainThread.getContextClassLoader();
		this.javaResourceDirectory = new File(dataDirectory, "src/main/resources");
		this.javaClassesDirectory = new File(dataDirectory, "target/classes");
		this.javaLibrariesDirectory = new File(dataDirectory, "lib");
		urls = new URL[] { javaResourceDirectory.toURI().toURL(),
				javaClassesDirectory.toURI().toURL(),
				javaLibrariesDirectory.toURI().toURL() };
		reload();
	}

	public synchronized void reload() {
		final URLClassLoader oldClassLoader = classLoader;
		final URLClassLoader newClassLoader = new URLClassLoader(urls, parentClassLoader);
		mainThread.setContextClassLoader(newClassLoader);
		classLoader = newClassLoader;
		if (oldClassLoader != null)
			IOUtils.close(oldClassLoader);
	}

	final public void register(final ClassFactory classFactory) {
		this.classFactory = classFactory == null ? ClassFactory.DefaultFactory.INSTANCE : classFactory;
	}

	final public <T> T newInstance(final Class<T> clazz) throws ReflectiveOperationException {
		return clazz == null ? null : classFactory.newInstance(clazz);
	}

	final public <T> T newInstance(final String className, final String[] classPrefixes)
			throws IOException, ReflectiveOperationException {
		return newInstance(ClassLoaderUtils.findClass(classLoader, className, classPrefixes));
	}

	final public <T> T newInstance(final String className) throws IOException, ReflectiveOperationException {
		return newInstance(ClassLoaderUtils.findClass(ClassLoaderManager.classLoader, className));
	}

}
