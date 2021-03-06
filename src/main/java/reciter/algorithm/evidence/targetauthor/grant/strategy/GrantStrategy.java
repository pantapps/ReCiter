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
package reciter.algorithm.evidence.targetauthor.grant.strategy;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reciter.algorithm.evidence.targetauthor.AbstractTargetAuthorStrategy;
import reciter.engine.Feature;
import reciter.engine.analysis.evidence.Grant;
import reciter.engine.analysis.evidence.GrantEvidence;
import reciter.model.article.ReCiterArticle;
import reciter.model.article.ReCiterArticleGrant;
import reciter.model.identity.Identity;

public class GrantStrategy extends AbstractTargetAuthorStrategy {

	private static final Logger log = LoggerFactory.getLogger(GrantStrategy.class);

	@Override
	public double executeStrategy(ReCiterArticle reCiterArticle, Identity identity) {
		log.info("Executing grant strategy for article id {} and identity id {}",
				reCiterArticle.getArticleId(),
				identity.getUid());
		double score = 0;
		GrantEvidence grantEvidence = new GrantEvidence();
		List<Grant> grants = new ArrayList<>();
		grantEvidence.setGrants(grants);
		for (ReCiterArticleGrant grant : reCiterArticle.getGrantList()) {
			for (String identityGrantId : identity.getGrants()) {
				log.info("identity grant {}", identityGrantId);
				if (grant.getGrantID() != null && grant.getGrantID().contains(identityGrantId)) {
					log.info("[known grant ids match=" + identityGrantId + "]");
					reCiterArticle.setClusterInfo(reCiterArticle.getClusterInfo() + " [known grant ids match=" + identityGrantId + "], ");
					Grant analysisGrant = new Grant();
					analysisGrant.setArticleGrant(grant.getGrantID());
					analysisGrant.setInstitutionGrant(identityGrantId);
					score += 1;
					reCiterArticle.getMatchingGrantList().add(grant);
					grants.add(analysisGrant);
				}
			}
		}
		reCiterArticle.setGrantEvidence(grantEvidence);
		return score;
	}
	
	@Override
	public double executeStrategy(List<ReCiterArticle> reCiterArticles, Identity identity) {
		double score = 0;
		for (ReCiterArticle reCiterArticle : reCiterArticles) {
			score += executeStrategy(reCiterArticle, identity);
		}
		return score;
	}

	@Override
	public void populateFeature(ReCiterArticle reCiterArticle, Identity identity, Feature feature) {
		// TODO Auto-generated method stub
		
	}
}
