/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package reciter.scopus.querybuilder;

public class ScopusXmlQuery {
	
	private final String query;
	private final int count;
	private final String field;
	private final int start;
	private final String view;
	private final String httpAccept;
	private final String queryUrl;
	
	private static final String SCOPUS_URL_PREFIX = "https://api.elsevier.com/content/search/index:SCOPUS?query=";
	
	private ScopusXmlQuery(ScopusXmlQueryBuilder scopusXmlQueryBuilder) {
		query = scopusXmlQueryBuilder.query;
		count = scopusXmlQueryBuilder.count;
		field = scopusXmlQueryBuilder.field;
		start = scopusXmlQueryBuilder.start;
		view = scopusXmlQueryBuilder.view;
		httpAccept = scopusXmlQueryBuilder.httpAccept;
		queryUrl = scopusXmlQueryBuilder.queryUrl;
	}
	
	/**
	 * Scopus XML Query builder class for constructing a Scopus XML query.
	 * @author jil3004
	 *
	 */
	public static class ScopusXmlQueryBuilder {
		// Required parameters.
		private final String query;
		
		// Optional parameters - initialized to default values.
		private int count;
		private String field = "pubmed-id,doi,affiliation,author,afid";
		private int start = 0;
		private String view = "COMPLETE";
		private String httpAccept = "application/xml";
		private String queryUrl;
		
		public ScopusXmlQueryBuilder(String query) {
			this.query = query;
			count = 1; // Assuming that it's fetching a single Scopus article.
		}
		
		public ScopusXmlQueryBuilder(String query, int count) {
			this.query = query;
			this.count = count;
		}
		public ScopusXmlQueryBuilder count(int count) {
			this.count = count;
			return this;
		}
		public ScopusXmlQueryBuilder field(String field) {
			this.field = field;
			return this;
		}
		public ScopusXmlQueryBuilder start(int start) {
			this.start = start;
			return this;
		}
		public ScopusXmlQueryBuilder view(String view) {
			this.view = view;
			return this;
		}
		public ScopusXmlQueryBuilder httpAccept(String httpAccept) {
			this.httpAccept = httpAccept;
			return this;
		}
		public ScopusXmlQuery build() {
			StringBuilder sb = new StringBuilder();
			sb.append(ScopusXmlQuery.SCOPUS_URL_PREFIX);
			sb.append(query);
			sb.append("&count=");
			sb.append(count);
			sb.append("&field=");
			sb.append(field);
			sb.append("&start=");
			sb.append(start);
			sb.append("&view=");
			sb.append(view);
			sb.append("&httpAccept=");
			sb.append(httpAccept);
			queryUrl = sb.toString();
			return new ScopusXmlQuery(this);
		}
		
		public ScopusXmlQuery buildSingle() {
			StringBuilder sb = new StringBuilder();
			sb.append(ScopusXmlQuery.SCOPUS_URL_PREFIX);
			sb.append("pmid(");
			sb.append(query);
			sb.append(")");
			sb.append("&count=");
			sb.append(count);
			sb.append("&field=");
			sb.append(field);
			sb.append("&start=");
			sb.append(start);
			sb.append("&view=");
			sb.append(view);
			sb.append("&httpAccept=");
			sb.append(httpAccept);
			queryUrl = sb.toString();
			return new ScopusXmlQuery(this);
		}
	}

	public String getQuery() {
		return query;
	}

	public int getCount() {
		return count;
	}

	public String getField() {
		return field;
	}

	public int getStart() {
		return start;
	}

	public String getView() {
		return view;
	}

	public String getHttpAccept() {
		return httpAccept;
	}

	public static String getScopusUrlPrefix() {
		return SCOPUS_URL_PREFIX;
	}

	public String getQueryUrl() {
		return queryUrl;
	}
}
