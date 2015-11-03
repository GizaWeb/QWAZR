/**
 * Copyright 2015 Emmanuel Keller / QWAZR
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
package com.qwazr.search.index;

import com.qwazr.utils.FileClassCompilerLoader;
import org.apache.lucene.analysis.Analyzer;

import java.io.File;
import java.io.IOException;

class AnalyzerUtils {

	final static String[] classPrefixes = { "", "com.qwazr.search.analysis.", "org.apache.lucene.analysis." };

	final static Class<Analyzer> findAnalyzerClass(String analyzer) throws ClassNotFoundException {
		ClassNotFoundException firstClassException = null;
		for (String prefix : classPrefixes) {
			try {
				return (Class<Analyzer>) Class.forName(prefix + analyzer);
			} catch (ClassNotFoundException e) {
				if (firstClassException == null)
					firstClassException = e;
			}
		}
		throw firstClassException;
	}

	final static Class<Analyzer> findAnalyzer(FileClassCompilerLoader compilerLoader, String analyzer)
			throws ReflectiveOperationException, InterruptedException, IOException {
		if (compilerLoader != null && analyzer.endsWith(".java"))
			return compilerLoader.loadClass(new File(analyzer));
		return findAnalyzerClass(analyzer);
	}
}
