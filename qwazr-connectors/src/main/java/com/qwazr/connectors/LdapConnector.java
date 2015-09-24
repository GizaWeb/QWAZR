/**
 * Copyright 2014-2015 Emmanuel Keller / QWAZR
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.qwazr.connectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qwazr.utils.IOUtils;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LdapConnector extends AbstractConnector {

    private static final Logger logger = LoggerFactory.getLogger(LdapConnector.class);

    public final String host = null;
    public final Integer port = null;
    public final String username = null;
    public final String password = null;
    public final String base_dn = null;
    public final Boolean use_pool = null;
    public final String auth_filter = null;

    private LdapConnectionPool connectionPool = null;
    private LdapConnectionConfig config = null;

    @Override
    public void load(File data_directory) {
	config = new LdapConnectionConfig();
	if (host != null)
	    config.setLdapHost(host);
	if (port != null)
	    config.setLdapPort(port);
	if (username != null)
	    config.setName(username);
	if (password != null)
	    config.setCredentials(password);
	if (use_pool != null && use_pool) {
	    ValidatingPoolableLdapConnectionFactory factory = new ValidatingPoolableLdapConnectionFactory(config);
	    connectionPool = new LdapConnectionPool(factory);
	    connectionPool.setTestOnBorrow(true);
	}
    }

    public LdapConnection getConnection(IOUtils.CloseableContext context, Long timeOut) throws LdapException {
	LdapConnection connection = null;
	if (connectionPool != null)
	    connection = connectionPool.getConnection();
	else
	    connection = new LdapNetworkConnection(config);
	context.add(connection);
	if (timeOut != null)
	    connection.setTimeOut(timeOut);
	return connection;
    }

    public Entry auth(LdapConnection connection, String username, String password, String user_filter)
		    throws LdapException, CursorException {
	if (username != null) {
	    if (password != null)
		connection.bind(username, password);
	    else
		connection.bind(username);
	} else
	    connection.bind();
	EntryCursor cursor = connection.search(base_dn, user_filter, SearchScope.SUBTREE);
	try {
	    if (!cursor.next())
		throw new LdapException("No entry found");
	    Entry entry = cursor.get();
	    if (entry == null)
		throw new LdapException("No entry found");
	    Dn userDN = entry.getDn();
	    connection.unBind();
	    connection.bind(userDN, password);
	    return entry;
	} finally {
	    if (!cursor.isClosed())
		cursor.close();
	}

    }

    public void unload() {
	if (connectionPool != null && !connectionPool.isClosed()) {
	    try {
		connectionPool.close();
	    } catch (Exception e) {
		logger.warn(e.getMessage(), e);
	    }
	}
    }

}
