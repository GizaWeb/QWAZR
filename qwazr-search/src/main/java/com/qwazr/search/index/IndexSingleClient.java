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
package com.qwazr.search.index;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qwazr.search.analysis.AnalyzerDefinition;
import com.qwazr.search.field.FieldDefinition;
import com.qwazr.utils.UBuilder;
import com.qwazr.utils.http.HttpRequest;
import com.qwazr.utils.json.CloseableStreamingOutput;
import com.qwazr.utils.json.client.JsonClientAbstract;
import com.qwazr.utils.server.RemoteService;

import javax.ws.rs.core.Response;
import java.util.*;

public class IndexSingleClient extends JsonClientAbstract implements IndexServiceInterface {

	public IndexSingleClient(final RemoteService remote) {
		super(remote);
	}

	private final static String PATH = "/" + IndexServiceInterface.PATH;
	private final static String PATH_SLASH = PATH + "/";

	public final static TypeReference<Set<String>> SetStringTypeRef = new TypeReference<Set<String>>() {
	};

	@Override
	public SchemaSettingsDefinition createUpdateSchema(final String schema_name) {
		final UBuilder uriBuilder = RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name);
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, null, null, SchemaSettingsDefinition.class, valid200Json);
	}

	@Override
	public SchemaSettingsDefinition createUpdateSchema(final String schema_name,
			final SchemaSettingsDefinition settings) {
		final UBuilder uriBuilder = RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name);
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, settings, null, SchemaSettingsDefinition.class, valid200Json);
	}

	@Override
	public Set<String> getSchemas() {
		final UBuilder uriBuilder = RemoteService.getNewUBuilder(remote, PATH);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, SetStringTypeRef, valid200Json);
	}

	@Override
	public Response deleteSchema(final String schema_name) {
		final UBuilder uriBuilder = RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name);
		final HttpRequest request = HttpRequest.Delete(uriBuilder.buildNoEx());
		return Response.status(executeStatusCode(request, null, null, valid200)).build();
	}

	@Override
	public Set<String> getIndexes(final String schema_name) {
		final UBuilder uriBuilder = RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, SetStringTypeRef, valid200Json);
	}

	@Override
	public IndexStatus createUpdateIndex(final String schema_name, final String index_name) {
		final UBuilder uriBuilder = RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, " / ", index_name);
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, null, null, IndexStatus.class, valid200Json);
	}

	@Override
	public IndexStatus createUpdateIndex(final String schema_name, final String index_name,
			final IndexSettingsDefinition settings) {
		final UBuilder uriBuilder = RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name);
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, settings, null, IndexStatus.class, valid200Json);
	}

	@Override
	public LinkedHashMap<String, FieldDefinition> getFields(final String schema_name, final String index_name) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/fields");
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, FieldDefinition.MapStringFieldTypeRef, valid200Json);
	}

	@Override
	public LinkedHashMap<String, FieldDefinition> setFields(final String schema_name, final String index_name,
			final LinkedHashMap<String, FieldDefinition> fields) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/fields");
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, fields, null, FieldDefinition.MapStringFieldTypeRef, valid200Json);
	}

	@Override
	public List<TermDefinition> doAnalyzeQuery(final String schema_name, final String index_name,
			final String field_name, final String text) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/fields/", field_name,
						"/analyzer/query");
		uriBuilder.setParameter("text", text);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, TermDefinition.ListTermDefinitionRef, valid200Json);
	}

	@Override
	public List<TermDefinition> doAnalyzeIndex(final String schema_name, final String index_name,
			final String field_name, final String text) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/fields/", field_name,
						"/analyzer/index");
		uriBuilder.setParameter("text", text);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, TermDefinition.ListTermDefinitionRef, valid200Json);
	}

	@Override
	public List<TermEnumDefinition> doExtractTerms(final String schema_name, final String index_name,
			final String field_name, final Integer start, final Integer rows) {
		return this.doExtractTerms(schema_name, index_name, field_name, null, start, rows);
	}

	@Override
	public List<TermEnumDefinition> doExtractTerms(final String schema_name, final String index_name,
			final String field_name, final String prefix, final Integer start, final Integer rows) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/fields/", field_name, "/terms/",
						prefix);
		uriBuilder.setParameter("start", start).setParameter("rows", rows);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, TermEnumDefinition.ListTermEnumDefinitionRef, valid200Json);
	}

	@Override
	public FieldDefinition getField(final String schema_name, final String index_name, final String field_name) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/fields/", field_name);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, FieldDefinition.class, valid200Json);
	}

	@Override
	public FieldDefinition setField(final String schema_name, final String index_name, final String field_name,
			final FieldDefinition field) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/fields/", field_name);
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, field, null, FieldDefinition.class, valid200Json);
	}

	@Override
	public Response deleteField(final String schema_name, final String index_name, final String field_name) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/fields/", field_name);
		final HttpRequest request = HttpRequest.Delete(uriBuilder.buildNoEx());
		return Response.status(executeStatusCode(request, null, null, valid200)).build();
	}

	@Override
	public LinkedHashMap<String, AnalyzerDefinition> getAnalyzers(final String schema_name, final String index_name) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/analyzers");
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, AnalyzerDefinition.MapStringAnalyzerTypeRef, valid200Json);
	}

	@Override
	public AnalyzerDefinition getAnalyzer(final String schema_name, final String index_name,
			final String analyzer_name) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/analyzers/", analyzer_name);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, AnalyzerDefinition.class, valid200Json);
	}

	@Override
	public AnalyzerDefinition setAnalyzer(final String schema_name, final String index_name, final String analyzer_name,
			final AnalyzerDefinition analyzer) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/analyzers/", analyzer_name);
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, analyzer, null, AnalyzerDefinition.class, valid200Json);
	}

	@Override
	public LinkedHashMap<String, AnalyzerDefinition> setAnalyzers(final String schema_name, final String index_name,
			final LinkedHashMap<String, AnalyzerDefinition> analyzers) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/analyzers");
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, analyzers, null, AnalyzerDefinition.MapStringAnalyzerTypeRef, valid200Json);
	}

	@Override
	public Response deleteAnalyzer(final String schema_name, final String index_name, final String analyzer_name) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/analyzers/", analyzer_name);
		final HttpRequest request = HttpRequest.Delete(uriBuilder.buildNoEx());
		return Response.status(executeStatusCode(request, null, null, valid200)).build();
	}

	@Override
	public List<TermDefinition> testAnalyzer(final String schema_name, final String index_name,
			final String analyzer_name, final String text) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/analyzers/", analyzer_name);
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, text, null, TermDefinition.ListTermDefinitionRef, valid200Json);
	}

	@Override
	public IndexStatus getIndex(final String schema_name, final String index_name) {
		final UBuilder uriBuilder = RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, IndexStatus.class, valid200Json);
	}

	@Override
	public Response deleteIndex(final String schema_name, final String index_name) {

		final UBuilder uriBuilder = RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name);
		final HttpRequest request = HttpRequest.Delete(uriBuilder.buildNoEx());
		return Response.status(executeStatusCode(request, null, null, valid200)).build();
	}

	@Override
	public BackupStatus doBackup(final String schema_name, final String index_name, final Integer keep_last_count) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/backup")
						.setParameterObject("keep_last", keep_last_count);
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, null, null, BackupStatus.class, valid200Json);
	}

	public final static TypeReference<List<BackupStatus>> ListBackupStatusTypeRef =
			new TypeReference<List<BackupStatus>>() {
			};

	@Override
	public List<BackupStatus> getBackups(final String schema_name, final String index_name) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/backup");
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, ListBackupStatusTypeRef, valid200);
	}

	@Override
	public CloseableStreamingOutput replicationObtain(final String schema_name, final String index_name,
			final String sessionID,
			final String source, final String fileName) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/replication/", sessionID, "/",
						source, "/", fileName);
		return executeStream(HttpRequest.Get(uriBuilder.buildNoEx()), null, null, valid200Stream);
	}

	@Override
	public Response replicationRelease(final String schema_name, final String index_name, final String sessionID) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/replication/", sessionID);
		final HttpRequest request = HttpRequest.Delete(uriBuilder.buildNoEx());
		return Response.status(executeStatusCode(request, null, null, valid200)).build();
	}

	@Override
	public CloseableStreamingOutput replicationUpdate(final String schema_name, final String index_name,
			final String current_version) {
		final UBuilder uriBuilder = RemoteService
				.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/replication/", current_version);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeStream(request, null, null, valid200);
	}

	@Override
	public Response replicationCheck(final String schema_name, final String index_name) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/replication");
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return Response.status(executeStatusCode(request, null, null, valid200)).build();
	}

	public final static TypeReference<Collection<Map<String, Object>>> CollectionMapStringObjectTypeRef =
			new TypeReference<Collection<Map<String, Object>>>() {
			};

	@Override
	public Integer postMappedDocument(final String schema_name, final String index_name,
			final Map<String, Object> document) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/doc");
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, document, null, Integer.class, valid200Json);
	}

	@Override
	public Integer postMappedDocuments(final String schema_name, final String index_name,
			final Collection<Map<String, Object>> documents) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/docs");
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, documents, null, Integer.class, valid200Json);
	}

	@Override
	public Integer updateMappedDocValues(final String schema_name, final String index_name,
			final Map<String, Object> document) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/doc/values");
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, document, null, Integer.class, valid200Json);
	}

	@Override
	public Integer updateMappedDocsValues(final String schema_name, final String index_name,
			final Collection<Map<String, Object>> documents) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/docs/values");
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, documents, null, Integer.class, valid200Json);
	}

	@Override
	public Response deleteAll(final String schema_name, final String index_name) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/docs");
		final HttpRequest request = HttpRequest.Delete(uriBuilder.buildNoEx());
		return Response.status(executeStatusCode(request, null, null, valid200)).build();
	}

	public final static TypeReference<LinkedHashMap<String, Object>> MapStringObjectTypeRef =
			new TypeReference<LinkedHashMap<String, Object>>() {
			};

	@Override
	public LinkedHashMap<String, Object> getDocument(final String schema_name, final String index_name,
			final String doc_id) {
		Objects.requireNonNull(doc_id, "The document must not be empty");
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/doc/", doc_id);
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, MapStringObjectTypeRef, valid200Json);
	}

	public final static TypeReference<ArrayList<LinkedHashMap<String, Object>>> ListMapStringObjectTypeRef =
			new TypeReference<ArrayList<LinkedHashMap<String, Object>>>() {
			};

	@Override
	public List<LinkedHashMap<String, Object>> getDocuments(final String schema_name, final String index_name,
			final Integer start, final Integer rows) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/doc");
		uriBuilder.addParameter("start", start == null ? null : start.toString())
				.addParameter("rows", rows == null ? null : rows.toString());
		final HttpRequest request = HttpRequest.Get(uriBuilder.buildNoEx());
		return executeJson(request, null, null, ListMapStringObjectTypeRef, valid200Json);
	}

	@Override
	public ResultDefinition.WithMap searchQuery(final String schema_name, final String index_name,
			final QueryDefinition query, final Boolean delete) {
		final UBuilder uriBuilder =
				RemoteService.getNewUBuilder(remote, PATH_SLASH, schema_name, "/", index_name, "/search")
						.setParameterObject("delete", delete);
		final HttpRequest request = HttpRequest.Post(uriBuilder.buildNoEx());
		return executeJson(request, query, null, ResultDefinition.WithMap.class, valid200Json);
	}

}
