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
package reciter.engine.erroranalysis;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import reciter.model.article.ReCiterAuthor;
import reciter.model.identity.AuthorName;

public class AnalysisObject {

	// Information regarding the article and target author.
	private StatusEnum status;
	private String uid;
	private String targetName;
	private String pubmedSearchQuery;
	private long pmid;
	private String articleTitle;
	private String fullJournalTitle;
	private String publicationYear;
	private String scopusTargetAuthorAffiliation;
	private String scopusCoAuthorAffiliation;
	private String pubmedTargetAuthorAffiliation;
	private String pubmedCoAuthorAffiliation;
	private String articleKeywords;
	
	private boolean isClusterOriginator;
	private long clusterArticleAssignedTo;
	private int countArticlesInAssignedCluster;
	private boolean isClusterSelectedInPhaseTwoMatching;
	
	private int targetAuthorYearTerminalDegree;
	private int targetAuthorYearBachelorsDegree;
	
	private List<String> targetAuthorKnownEmails;
	private List<AuthorName> targetAuthorKnownNameAliases;
	private List<String> targetAuthorKnownAffiliations;
	
	// Scores.
	private double emailStrategyScore;
	private double departmentStrategyScore;
	private double knownCoinvestigatorScore;
	private double affiliationScore;
	private double scopusStrategyScore;
	private double coauthorStrategyScore;
	private double journalStrategyScore;
	private double citizenshipStrategyScore;
	private double bachelorsYearDiscrepancyScore;
	private double doctoralYearDiscrepancyScore;
	private boolean isArticleTitleStartWithBracket;
	private double educationScore;
	
	private double bachelorsYearDiscrepancy;
	private double doctoralYearDiscrepancy;
	
	// Date of retrieval
	private Date dateInitialRun; // the date of the first time that ReCiter perform the retrieval
	private Date dateLastRun; // the date of the most recent retrieval
	private List<String> departments;
	private List<AnalysisObjectAuthor> analysisObjectAuthors;
	private List<String> frequentInstitutionalCollaborators;
	private List<ReCiterAuthor> knownRelationships;
	
	private double meshMajorStrategyScore;
	private Set<String> overlappingMeSHMajorNegativeArticles;
	private long clusterId;
	
	public double getMeshMajorStrategyScore() {
		return meshMajorStrategyScore;
	}
	public void setMeshMajorStrategyScore(double meshMajorStrategyScore) {
		this.meshMajorStrategyScore = meshMajorStrategyScore;
	}
	public Set<String> getOverlappingMeSHMajorNegativeArticles() {
		return overlappingMeSHMajorNegativeArticles;
	}
	public void setOverlappingMeSHMajorNegativeArticles(Set<String> overlappingMeSHMajorNegativeArticles) {
		this.overlappingMeSHMajorNegativeArticles = overlappingMeSHMajorNegativeArticles;
	}
	public long getClusterId() {
		return clusterId;
	}
	public void setClusterId(long clusterId) {
		this.clusterId = clusterId;
	}
	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getPubmedSearchQuery() {
		return pubmedSearchQuery;
	}
	public void setPubmedSearchQuery(String pubmedSearchQuery) {
		this.pubmedSearchQuery = pubmedSearchQuery;
	}
	public long getPmid() {
		return pmid;
	}
	public void setPmid(long pmid) {
		this.pmid = pmid;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getFullJournalTitle() {
		return fullJournalTitle;
	}
	public void setFullJournalTitle(String fullJournalTitle) {
		this.fullJournalTitle = fullJournalTitle;
	}
	public String getPublicationYear() {
		return publicationYear;
	}
	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}
	public String getScopusTargetAuthorAffiliation() {
		return scopusTargetAuthorAffiliation;
	}
	public void setScopusTargetAuthorAffiliation(
			String scopusTargetAuthorAffiliation) {
		this.scopusTargetAuthorAffiliation = scopusTargetAuthorAffiliation;
	}
	public String getScopusCoAuthorAffiliation() {
		return scopusCoAuthorAffiliation;
	}
	public void setScopusCoAuthorAffiliation(String scopusCoAuthorAffiliation) {
		this.scopusCoAuthorAffiliation = scopusCoAuthorAffiliation;
	}
	public String getPubmedTargetAuthorAffiliation() {
		return pubmedTargetAuthorAffiliation;
	}
	public void setPubmedTargetAuthorAffiliation(
			String pubmedTargetAuthorAffiliation) {
		this.pubmedTargetAuthorAffiliation = pubmedTargetAuthorAffiliation;
	}
	public String getPubmedCoAuthorAffiliation() {
		return pubmedCoAuthorAffiliation;
	}
	public void setPubmedCoAuthorAffiliation(String pubmedCoAuthorAffiliation) {
		this.pubmedCoAuthorAffiliation = pubmedCoAuthorAffiliation;
	}
	public String getArticleKeywords() {
		return articleKeywords;
	}
	public void setArticleKeywords(String articleKeywords) {
		this.articleKeywords = articleKeywords;
	}
	public boolean isClusterOriginator() {
		return isClusterOriginator;
	}
	public void setClusterOriginator(boolean isClusterOriginator) {
		this.isClusterOriginator = isClusterOriginator;
	}
	public long getClusterArticleAssignedTo() {
		return clusterArticleAssignedTo;
	}
	public void setClusterArticleAssignedTo(long clusterArticleAssignedTo) {
		this.clusterArticleAssignedTo = clusterArticleAssignedTo;
	}
	public int getCountArticlesInAssignedCluster() {
		return countArticlesInAssignedCluster;
	}
	public void setCountArticlesInAssignedCluster(int countArticlesInAssignedCluster) {
		this.countArticlesInAssignedCluster = countArticlesInAssignedCluster;
	}
	public boolean isClusterSelectedInPhaseTwoMatching() {
		return isClusterSelectedInPhaseTwoMatching;
	}
	public void setClusterSelectedInPhaseTwoMatching(
			boolean isClusterSelectedInPhaseTwoMatching) {
		this.isClusterSelectedInPhaseTwoMatching = isClusterSelectedInPhaseTwoMatching;
	}
	public double getEmailStrategyScore() {
		return emailStrategyScore;
	}
	public void setEmailStrategyScore(double emailStrategyScore) {
		this.emailStrategyScore = emailStrategyScore;
	}
	public double getDepartmentStrategyScore() {
		return departmentStrategyScore;
	}
	public void setDepartmentStrategyScore(double departmentStrategyScore) {
		this.departmentStrategyScore = departmentStrategyScore;
	}
	public double getKnownCoinvestigatorScore() {
		return knownCoinvestigatorScore;
	}
	public void setKnownCoinvestigatorScore(double knownCoinvestigatorScore) {
		this.knownCoinvestigatorScore = knownCoinvestigatorScore;
	}
	public double getAffiliationScore() {
		return affiliationScore;
	}
	public void setAffiliationScore(double affiliationScore) {
		this.affiliationScore = affiliationScore;
	}
	public double getScopusStrategyScore() {
		return scopusStrategyScore;
	}
	public void setScopusStrategyScore(double scopusStrategyScore) {
		this.scopusStrategyScore = scopusStrategyScore;
	}
	public double getCoauthorStrategyScore() {
		return coauthorStrategyScore;
	}
	public void setCoauthorStrategyScore(double coauthorStrategyScore) {
		this.coauthorStrategyScore = coauthorStrategyScore;
	}
	public double getJournalStrategyScore() {
		return journalStrategyScore;
	}
	public void setJournalStrategyScore(double journalStrategyScore) {
		this.journalStrategyScore = journalStrategyScore;
	}
	public double getCitizenshipStrategyScore() {
		return citizenshipStrategyScore;
	}
	public void setCitizenshipStrategyScore(double citizenshipStrategyScore) {
		this.citizenshipStrategyScore = citizenshipStrategyScore;
	}
	public double getBachelorsYearDiscrepancyScore() {
		return bachelorsYearDiscrepancyScore;
	}
	public void setBachelorsYearDiscrepancyScore(double bachelorsYearDiscrepancyScore) {
		this.bachelorsYearDiscrepancyScore = bachelorsYearDiscrepancyScore;
	}
	public double getDoctoralYearDiscrepancyScore() {
		return doctoralYearDiscrepancyScore;
	}
	public void setDoctoralYearDiscrepancyScore(double doctoralYearDiscrepancyScore) {
		this.doctoralYearDiscrepancyScore = doctoralYearDiscrepancyScore;
	}
	public boolean isArticleTitleStartWithBracket() {
		return isArticleTitleStartWithBracket;
	}
	public void setArticleTitleStartWithBracket(boolean isArticleTitleStartWithBracket) {
		this.isArticleTitleStartWithBracket = isArticleTitleStartWithBracket;
	}
	public double getEducationScore() {
		return educationScore;
	}
	public void setEducationScore(double educationScore) {
		this.educationScore = educationScore;
	}
	public Date getDateInitialRun() {
		return dateInitialRun;
	}
	public void setDateInitialRun(Date dateInitialRun) {
		this.dateInitialRun = dateInitialRun;
	}
	public Date getDateLastRun() {
		return dateLastRun;
	}
	public void setDateLastRun(Date dateLastRun) {
		this.dateLastRun = dateLastRun;
	}
	public int getTargetAuthorYearTerminalDegree() {
		return targetAuthorYearTerminalDegree;
	}
	public void setTargetAuthorYearTerminalDegree(int targetAuthorYearTerminalDegree) {
		this.targetAuthorYearTerminalDegree = targetAuthorYearTerminalDegree;
	}
	public int getTargetAuthorYearBachelorsDegree() {
		return targetAuthorYearBachelorsDegree;
	}
	public void setTargetAuthorYearBachelorsDegree(int targetAuthorYearBachelorsDegree) {
		this.targetAuthorYearBachelorsDegree = targetAuthorYearBachelorsDegree;
	}
	public List<String> getDepartments() {
		return departments;
	}
	public void setDepartments(List<String> departments) {
		this.departments = departments;
	}
	public List<String> getTargetAuthorKnownEmails() {
		return targetAuthorKnownEmails;
	}
	public void setTargetAuthorKnownEmails(List<String> targetAuthorKnownEmails) {
		this.targetAuthorKnownEmails = targetAuthorKnownEmails;
	}
	public List<AuthorName> getTargetAuthorKnownNameAliases() {
		return targetAuthorKnownNameAliases;
	}
	public void setTargetAuthorKnownNameAliases(List<AuthorName> targetAuthorKnownNameAliases) {
		this.targetAuthorKnownNameAliases = targetAuthorKnownNameAliases;
	}
	public List<String> getTargetAuthorKnownAffiliations() {
		return targetAuthorKnownAffiliations;
	}
	public void setTargetAuthorKnownAffiliations(List<String> targetAuthorKnownAffiliations) {
		this.targetAuthorKnownAffiliations = targetAuthorKnownAffiliations;
	}
	public double getBachelorsYearDiscrepancy() {
		return bachelorsYearDiscrepancy;
	}
	public void setBachelorsYearDiscrepancy(double bachelorsYearDiscrepancy) {
		this.bachelorsYearDiscrepancy = bachelorsYearDiscrepancy;
	}
	public double getDoctoralYearDiscrepancy() {
		return doctoralYearDiscrepancy;
	}
	public void setDoctoralYearDiscrepancy(double doctoralYearDiscrepancy) {
		this.doctoralYearDiscrepancy = doctoralYearDiscrepancy;
	}
	public List<AnalysisObjectAuthor> getAnalysisObjectAuthors() {
		return analysisObjectAuthors;
	}
	public void setAnalysisObjectAuthors(List<AnalysisObjectAuthor> analysisObjectAuthors) {
		this.analysisObjectAuthors = analysisObjectAuthors;
	}
	public List<String> getFrequentInstitutionalCollaborators() {
		return frequentInstitutionalCollaborators;
	}
	public void setFrequentInstitutionalCollaborators(List<String> frequentInstitutionalCollaborators) {
		this.frequentInstitutionalCollaborators = frequentInstitutionalCollaborators;
	}
	public List<ReCiterAuthor> getKnownRelationships() {
		return knownRelationships;
	}
	public void setKnownRelationships(List<ReCiterAuthor> knownRelationships) {
		this.knownRelationships = knownRelationships;
	}
	
	@Override
	public String toString() {
		return "AnalysisObject [status=" + status + ", uid=" + uid + ", targetName=" + targetName
				+ ", pubmedSearchQuery=" + pubmedSearchQuery + ", pmid=" + pmid + ", articleTitle=" + articleTitle
				+ ", fullJournalTitle=" + fullJournalTitle + ", publicationYear=" + publicationYear
				+ ", scopusTargetAuthorAffiliation=" + scopusTargetAuthorAffiliation + ", scopusCoAuthorAffiliation="
				+ scopusCoAuthorAffiliation + ", pubmedTargetAuthorAffiliation=" + pubmedTargetAuthorAffiliation
				+ ", pubmedCoAuthorAffiliation=" + pubmedCoAuthorAffiliation + ", articleKeywords=" + articleKeywords
				+ ", isClusterOriginator=" + isClusterOriginator + ", clusterArticleAssignedTo="
				+ clusterArticleAssignedTo + ", countArticlesInAssignedCluster=" + countArticlesInAssignedCluster
				+ ", isClusterSelectedInPhaseTwoMatching=" + isClusterSelectedInPhaseTwoMatching
				+ ", targetAuthorYearTerminalDegree=" + targetAuthorYearTerminalDegree
				+ ", targetAuthorYearBachelorsDegree=" + targetAuthorYearBachelorsDegree + ", targetAuthorKnownEmails="
				+ targetAuthorKnownEmails + ", targetAuthorKnownNameAliases=" + targetAuthorKnownNameAliases
				+ ", targetAuthorKnownAffiliations=" + targetAuthorKnownAffiliations + ", emailStrategyScore="
				+ emailStrategyScore + ", departmentStrategyScore=" + departmentStrategyScore
				+ ", knownCoinvestigatorScore=" + knownCoinvestigatorScore + ", affiliationScore=" + affiliationScore
				+ ", scopusStrategyScore=" + scopusStrategyScore + ", coauthorStrategyScore=" + coauthorStrategyScore
				+ ", journalStrategyScore=" + journalStrategyScore + ", citizenshipStrategyScore="
				+ citizenshipStrategyScore + ", bachelorsYearDiscrepancyScore=" + bachelorsYearDiscrepancyScore
				+ ", doctoralYearDiscrepancyScore=" + doctoralYearDiscrepancyScore + ", isArticleTitleStartWithBracket="
				+ isArticleTitleStartWithBracket + ", educationScore=" + educationScore + ", bachelorsYearDiscrepancy="
				+ bachelorsYearDiscrepancy + ", doctoralYearDiscrepancy=" + doctoralYearDiscrepancy
				+ ", dateInitialRun=" + dateInitialRun + ", dateLastRun=" + dateLastRun + ", departments=" + departments
				+ ", analysisObjectAuthors=" + analysisObjectAuthors + ", frequentInstitutionalCollaborators="
				+ frequentInstitutionalCollaborators + ", knownRelationships=" + knownRelationships
				+ ", meshMajorStrategyScore=" + meshMajorStrategyScore + ", overlappingMeSHMajorNegativeArticles="
				+ overlappingMeSHMajorNegativeArticles + ", clusterId=" + clusterId + "]";
	}
}
