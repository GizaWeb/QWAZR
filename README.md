Welcome to QWAZR
----------------

[![Join the chat at https://gitter.im/qwazr/QWAZR](https://badges.gitter.im/qwazr/QWAZR.svg)](https://gitter.im/qwazr/QWAZR?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Build Status](https://travis-ci.org/qwazr/QWAZR.svg?branch=master)](https://travis-ci.org/qwazr/QWAZR)

QWAZR is a an **application server** dedicated to scalable, failover, distributed projects.
It can be used to create a web application or a **microservice** written in Java and/or Javascript.

By building QWAZR, our goal is to provide an easy and efficient way to build **scalable applications**
that require both distributed processes and fail-over abilities.

What do you get (production ready) ?
------------------------------------

This project is a work in progress. The current version of QWAZR (1.0.1) comes with the following services:

- [Webapps](qwazr-webapps) : A model-view-controller application server,
- [Scheduler](qwazr-scheduler): A multi-master scheduler,
- [Scripts](qwazr-scripts): A distributed JAVA/Javascript execution service,
- [Connectors](qwazr-connectors) : A set of connectors to link your application to the outside world
(Cassandra, MongoDB, MySQL, HDFS, LDAP, FTP, etc.),
- [Tools](qwazr-tools) : A set of tools for data management (language tools, markdown to HTML converter,
Freemarker, XML parser and writer, etc.).
- [Crawlers](qwazr-crawlers) : A distributed (web) crawler,
- [Extractor](qwazr-extractor) : Text extraction from various kinds of binaries files,
- [Search](qwazr-search): Full-text indexation and search based on Lucene,
- [Database](qwazr-database): A NoSQL key/value database based on LevelDB.
- [Cluster](qwazr-cluster) : Manages the cluster's nodes.

Getting started
---------------

Here is two examples of QWAZR applications which can be used as tutorials.
By running one of this 5 minutes tutorials you will create a small web site,
a REST/JSON API connected to a MongoDB database.

- [Javascript example](../qwazr-javascript-example)
- [Java example](../qwazr-java-example)
- Instructions how to [install QWAZR on Linux](qwazr-server/src/doc/linux-package-installation.md)

Our roadmap
-----------

#### Multi-master clustering

Deployed on several servers, the **(multi) masters** and the nodes work together,
sharing the required information to provide a **fault-tolerant** distributed environment.

#### Full set of JSON API for each module

The server also provides a set of **JSON REST web services** that manages distributed jobs,
web applications and data persistence.

#### Fast Javascript engine and automatic JAVA compilation

The Javascript and The JAVA source code can be updated without having to restart the server.
The source code is automatically compiled without having to restart the application server.

#### A comprehensive documentation

The purpose of this Wiki.

#### Releasing the last modules:
- [Graph](qwazr-graph) : A graph engine,
- [Store](qwazr-store): A file management service with distribution and replication support,

Open Source & contributions
---------------------------

We believe that **open source** is a smart way to build amazing software.
QWAZR is provided under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).

There are many ways to contribute to the project:
- The source code is [here](https://github.com/qwazr/QWAZR), you may fork, compile,
develop and provide patches.
- [API Javadocs](https://www.qwazr.com/apidocs/)
- We are currently writing the documentation in the source pages.
You may suggest subjects, tutorials, etc.

Question & Contact
------------------

We support QWAZR on StackOverflow. We monitor and answer questions with the tag "qwazr":
[Ask a question](http://stackoverflow.com/questions/ask?tags=qwazr).

You can use the software and provide feedback,
[bug reports and/or feature requests](https://github.com/qwazr/QWAZR/issues).

Anyway, thanks to support the QWAZR project !


Issues and change Log
---------------------

Issues and milestones are tracked on GitHub:

- [Open issues](https://github.com/qwazr/QWAZR/issues?q=is%3Aopen+is%3Aissue)
- [Closed issues](https://github.com/qwazr/QWAZR/issues?q=is%3Aissue+is%3Aclosed)

License
-------

Copyright 2015 [Emmanuel Keller / QWAZR S.A.S.](http://www.qwazr.com)


Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.