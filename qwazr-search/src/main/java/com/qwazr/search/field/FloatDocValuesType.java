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
package com.qwazr.search.field;

import com.qwazr.search.index.QueryDefinition;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.SortField;

import java.io.IOException;
import java.util.Collection;

class FloatDocValuesType extends FieldTypeAbstract {

	FloatDocValuesType(final String fieldName, final FieldDefinition fieldDef) {
		super(fieldName, fieldDef);
	}

	@Override
	final public void fill(final Object value, final FieldConsumer consumer) {
		if (value instanceof Collection)
			fillCollection((Collection) value, consumer);
		else if (value instanceof Number)
			consumer.accept(new FloatDocValuesField(fieldName, ((Number) value).floatValue()));
		else
			consumer.accept(new FloatDocValuesField(fieldName, Float.parseFloat(value.toString())));
	}

	@Override
	final public SortField getSortField(final QueryDefinition.SortEnum sortEnum) {
		final SortField sortField = new SortField(fieldName, SortField.Type.FLOAT, SortUtils.sortReverse(sortEnum));
		SortUtils.sortFloatMissingValue(sortEnum, sortField);
		return sortField;
	}

	@Override
	final public ValueConverter getConverter(final LeafReader reader) throws IOException {
		NumericDocValues docValues = reader.getNumericDocValues(fieldName);
		if (docValues == null)
			return super.getConverter(reader);
		return new ValueConverter.FloatDVConverter(docValues);
	}
}
