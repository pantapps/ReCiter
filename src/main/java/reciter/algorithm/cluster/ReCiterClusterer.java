package reciter.algorithm.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reciter.algorithm.cluster.model.ReCiterCluster;
import reciter.erroranalysis.Analysis;
import reciter.erroranalysis.AnalysisObject;
import reciter.erroranalysis.YearDiscrepacyReader;
import reciter.lucene.docsimilarity.AffiliationCosineSimilarity;
import reciter.lucene.docsimilarity.DocumentSimilarity;
import reciter.lucene.docsimilarity.KeywordCosineSimilarity;
import reciter.model.article.ReCiterArticle;
import reciter.model.author.ReCiterAuthor;
import reciter.model.author.TargetAuthor;
import reciter.model.author.TargetAuthor.TypeScore;
import database.dao.JournalDao;

public class ReCiterClusterer implements Clusterer {
	
	private static final Logger slf4jLogger = LoggerFactory.getLogger(ReCiterClusterer.class);	
	private Map<Integer, ReCiterCluster> finalCluster;
	private boolean selectingTarget = false;
	private double similarityThreshold = 0.3;
	private double targetAuthorSimilarityThreshold = 0.001;

	public ReCiterClusterer() {
		finalCluster = new HashMap<Integer, ReCiterCluster>();
	}

	public void cluster(List<ReCiterArticle> reciterArticleList, Analysis analysis) {

		finalCluster.clear(); // forgot to clear this: now similarity threshold should work.
		ReCiterCluster.getClusterIDCounter().set(0); // reset counter.

		cluster(reciterArticleList);

		DocumentSimilarity affiliationSimilarity = new AffiliationCosineSimilarity();
		DocumentSimilarity keywordSimilarity = new KeywordCosineSimilarity();

		ReCiterArticle targetAuthorArticle = TargetAuthor.getInstance().getTargetAuthorArticleIndexed();
		// Compute the affiliation similarity and keyword similarity of target author to all the clusters.
		for (Entry<Integer, ReCiterCluster> entry : finalCluster.entrySet()) {
			TargetAuthor.getInstance().getMap().put(entry.getKey(), new ArrayList<TypeScore>());
			double affiliationMax = -1;
			double keywordMax = -1;
			for (ReCiterArticle reCiterArticle : entry.getValue().getArticleCluster()) {
				double currentAffiliationSimScore = affiliationSimilarity.documentSimilarity(reCiterArticle, targetAuthorArticle);
				double currentKeywordSimScore = keywordSimilarity.documentSimilarity(reCiterArticle, targetAuthorArticle);

				if (currentAffiliationSimScore > affiliationMax) {
					affiliationMax = currentAffiliationSimScore;
				}

				if (currentKeywordSimScore > keywordMax) {
					keywordMax = currentKeywordSimScore;
				}
			}
			TargetAuthor.getInstance().getMap().get(entry.getKey()).add(new TypeScore("affiliation", affiliationMax));
			TargetAuthor.getInstance().getMap().get(entry.getKey()).add(new TypeScore("keyword", keywordMax));
		}

		// Assign target author to a cluster in finalCluster.
		int assignedClusterId = assignTargetToCluster(TargetAuthor.getInstance().getTargetAuthorArticleIndexed());

		ReCiterCluster reCiterCluster = finalCluster.get(assignedClusterId);

		if (reCiterCluster != null) {
			Set<Integer> pmidSet = reCiterCluster.getPmidSet();
			analysis.setTruePositiveList(pmidSet);
			analysis.setSizeOfSelected(pmidSet.size());
			double precision = analysis.getPrecision();
			double recall = analysis.getRecall();
			//		double avgPrecRecall = (precision + recall) / 2;

			// Analysis:
			for (Entry<Integer, ReCiterCluster> entry : finalCluster.entrySet()) {
				for (ReCiterArticle reCiterArticle : entry.getValue().getArticleCluster()) {
					AnalysisObject analysisObject = new AnalysisObject();
					analysisObject.setSimilarityMeasure(similarityThreshold);
					analysisObject.setReCiterArticle(reCiterArticle);
					analysisObject.setClusterId(entry.getValue().getClusterID());
					analysisObject.setNumArticlesInCluster(entry.getValue().getArticleCluster().size());

					if (reCiterArticle.getJournal() != null) {
						analysisObject.setYearOfPublication(reCiterArticle.getJournal().getJournalIssuePubDateYear());
					} else {
						analysisObject.setYearOfPublication(-1);
					}
					// If the cluster id is the same, then cluster is selected as the assigned matching cluster.
					if (entry.getKey() == reCiterCluster.getClusterID()) {
						analysisObject.setSelected(true);
						// True Positive.
						if (analysis.getGoldStandard().contains(reCiterArticle.getArticleID())) {
							analysisObject.setStatus("True Positive");
						}
						// False Positive.
						else {
							analysisObject.setStatus("False Positive");
						}
					} else {
						analysisObject.setSelected(false);
						// True Negative.
						if (!analysis.getGoldStandard().contains(reCiterArticle.getArticleID())) {
							analysisObject.setStatus("True Negative");
						}
						// False Negative.
						else {
							analysisObject.setStatus("False Negative");
						}
					}

					AnalysisObject.getAnalysisObjectList().add(analysisObject);	
					AnalysisObject.getAllAnalysisObjectList().add(analysisObject);
				}
			}

			slf4jLogger.info("Precision = " + precision);
			slf4jLogger.info("Recall = " + recall);

			ReCiterExample.totalPrecision += precision;
			ReCiterExample.totalRecall += recall;

		} else {
			slf4jLogger.info("No cluster match found.");
		}
	}

	/**
	 * Getter method to retrieve the finalCluster map data structure.
	 * @return
	 */
	public Map<Integer, ReCiterCluster> getFinalCluster() {
		return finalCluster;
	}

	/**
	 * 
	 * @param article
	 * @param targetAuthor
	 * @return
	 */
	public int assignTargetToCluster(ReCiterArticle article) {
		selectingTarget = true;
		return selectCandidateCluster(article);
	}

	/**
	 * 
	 * @param articleList
	 * @param targetAuthor
	 */
	@Override
	public void cluster(List<ReCiterArticle> reciterArticleList) {

		slf4jLogger.info("Number of articles to be clustered: " + reciterArticleList.size());
		ReCiterCluster firstCluster = new ReCiterCluster();
		ReCiterArticle first = reciterArticleList.get(0);

		first.setClusterStarter(true); // first article is the cluster starter.
		firstCluster.add(first);

		finalCluster.put(firstCluster.getClusterID(), firstCluster);
		for (int i = 1; i < reciterArticleList.size(); i++) {
			
			ReCiterArticle article = reciterArticleList.get(i);
			slf4jLogger.info("Assigning " + i + ": " + article.getArticleID());
			int selection = selectCandidateCluster(article);
			if (selection == -1) {
				article.setClusterStarter(true);
				// create its own cluster.
				ReCiterCluster newCluster = new ReCiterCluster();
				newCluster.add(article);
				finalCluster.put(newCluster.getClusterID(), newCluster);
			} else {
				finalCluster.get(selection).add(article);
			}
		}
	}

	/**
	 * Select the candidate cluster.
	 * @param currentArticle
	 * @param targetAuthor
	 * @return
	 */
	public int selectCandidateCluster(ReCiterArticle currentArticle) {

		// Get cluster ids with max number of coauthor matches.
		Set<Integer> clusterIdSet = getKeysWithMaxVal(computeCoauthorMatch(currentArticle));
		//		slf4jLogger.info("PMID: " + currentArticle.getArticleID() + " " + clusterIdSet);

		// If groups have matching co-authors, the program selects the group that has the most matching names.
		if (clusterIdSet.size() == 1) {
			for (int id : clusterIdSet) {
				return id;
			}
		}

		// If two or more of these have the same number of coauthors, the one with the highest matching score is selected.
		if (clusterIdSet.size() > 1) {
			return getIdWithMostContentSimilarity(clusterIdSet, currentArticle);
		}

		// If groups have no matching co-authors, the group with the highest 
		// matching (cosine) score is selected, provided that the score
		// exceeds a given threshold.
		return getIdWithMostContentSimilarity(finalCluster.keySet(), currentArticle);
	}

	/**
	 * 
	 * @param clusterIdList
	 * @param currentArticle
	 * @return
	 */
	private int getIdWithMostContentSimilarity(Set<Integer> clusterIdList, ReCiterArticle currentArticle) {
		double currentMax = -1;
		int currentMaxId = -1;
		for (int id : clusterIdList) {

			double sim = finalCluster.get(id).contentSimilarity(currentArticle); // cosine similarity score.

			// Github issue: https://github.com/wcmc-its/ReCiter/issues/60
			// For individuals with no/few papers, use default departmental-journal similarity score.
//			if (selectingTarget) {
//				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
//					// TODO 
//					// String departmentName = TargetAuthor.getInstance().getDepartmentName();
//					// String journalIsoAbbr = article.getJournal().getIsoAbbreviation();
//					// sample SQL code:
//					// SELECT score FROM wcmc_matching_journals_department
//					// WHERE department_id = ...
//					// AND journal_id = ...
//					// Now: increase similarity score:
//					// sim *= (1 + score) from database table.
//				}
//			}
//			
//			// Github issue: https://github.com/wcmc-its/ReCiter/issues/45
//			// Leverage data on board certifications to improve phase two matching.
//			if (selectingTarget) {
//				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
//					// TODO if TargetAuthor.getInstance()'s board certification matches the `article` object. Increase `sim`.
//				}
//			}
//
//			// Github issue: https://github.com/wcmc-its/ReCiter/issues/49
//			// Leverage known co-investigators on grants to improve phase two matching.
//			if (selectingTarget) {
//				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
//					// TODO if TargetAuthor.getInstance()'s co-investigators matches the `article`'s authors. Increase `sim`.
//				}
//			}
//
//			// Github issue: https://github.com/wcmc-its/ReCiter/issues/83
//			// If a candidate article is published in a journal and the cluster contains that journal, increase the score for a match.
//			if (!selectingTarget) {
//				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
//					// TODO If `article`'s journal title matches (by direct string matching or journal similarity) `currentArticle`'s
//					// journal title, increase `sim` score.
//				}
//			}

			// Grab CWID from rc_identity table. Combine with "@med.cornell.edu" and match against candidate records. 
			// When email is found in affiliation string, during phase two clustering, automatically assign the matching identity.
			if (selectingTarget) {
				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
					if (article.getAffiliationConcatenated() != null) {
						if (article.getAffiliationConcatenated().contains(TargetAuthor.getInstance().getCwid() + "@med.cornell.edu")) {
							//							sim *= 1.3; // a matching email should dramatically increase the score of some results but not decrease the score of others
							return id;
						}
					}
				}
			}


			// Increase similarity if the affiliation information "Weill Cornell Medical College" appears in affiliation.
			if (!selectingTarget) {
				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
					if (StringUtils.contains(StringUtils.lowerCase(article.getAffiliationConcatenated()), "weill cornell") &&
							StringUtils.contains(StringUtils.lowerCase(currentArticle.getAffiliationConcatenated()), "weill cornell")) {
						sim *= 3;
					}
				}
			}

			// Adjust cosine similarity score with year discrepancy.
			if (!selectingTarget) {
				// Update the similarity score with year discrepancy.
				int yearDiff = Integer.MAX_VALUE; // Compute difference in year between candidate article and closest year in article cluster.
				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
					int currentYearDiff = Math.abs(currentArticle.getJournal().getJournalIssuePubDateYear() - article.getJournal().getJournalIssuePubDateYear());
					if (currentYearDiff < yearDiff) {
						yearDiff = currentYearDiff;
					}
				}
				if (yearDiff > 40) {
					sim *= 0.001526;
				} else {
					sim = sim * YearDiscrepacyReader.getYearDiscrepancyMap().get(yearDiff);
				}
			}

			// https://github.com/wcmc-its/ReCiter/issues/59
			// Context: first name is a valuable indication if a person is author for an article. 
			// It is always tracked in the rc_identity table. And it is sometimes, though not always available in the 
			// article. First names tend to change especially in cases where it becomes Westernized.

			// https://github.com/wcmc-its/ReCiter/issues/58
			// middle initial is a valuable indication if a person has the identity of author for an article. 
			// It is, sometimes, though not always, tracked in the rc_identity table. 
			// And it is sometimes, though not always available in the article.
			if (!selectingTarget) {
				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
					String targetAuthorMiddleInitial = TargetAuthor.getInstance().getAuthorName().getMiddleInitial();
					String firstName = TargetAuthor.getInstance().getAuthorName().getFirstName();

					// First Name from rc_identity.
					if (firstName != null) {
						// For cases where first name is present in rc_identity.
						for (ReCiterAuthor author : article.getArticleCoAuthors().getCoAuthors()) {
							if (author.getAuthorName().firstInitialLastNameMatch(TargetAuthor.getInstance().getAuthorName())) {
								if (firstName.equalsIgnoreCase((author.getAuthorName().getFirstName()))) {
									for (ReCiterAuthor currentArticleAuthor : currentArticle.getArticleCoAuthors().getCoAuthors()) {
										if (currentArticleAuthor.getAuthorName().firstInitialLastNameMatch(TargetAuthor.getInstance().getAuthorName())) {
											if (firstName.equalsIgnoreCase(currentArticleAuthor.getAuthorName().getFirstName())) {
												sim *= 1.3; // (rc_idenity = YES, present in cluster = YES, match = YES.
											} else {
												sim *= 0.4; // (rc_idenity = YES, present in cluster = YES, match = NO.
											}
										}
									}
								}
							}
						}
					}

					// Middle initial from rc_identity.
					if (targetAuthorMiddleInitial != null) {
						// For cases where middle initial is present in rc_identity.
						for (ReCiterAuthor author : article.getArticleCoAuthors().getCoAuthors()) {
							if (author.getAuthorName().firstInitialLastNameMatch(TargetAuthor.getInstance().getAuthorName())) {
								if (targetAuthorMiddleInitial.equalsIgnoreCase((author.getAuthorName().getMiddleInitial()))) {
									for (ReCiterAuthor currentArticleAuthor : currentArticle.getArticleCoAuthors().getCoAuthors()) {
										if (currentArticleAuthor.getAuthorName().firstInitialLastNameMatch(TargetAuthor.getInstance().getAuthorName())) {
											if (targetAuthorMiddleInitial.equalsIgnoreCase(currentArticleAuthor.getAuthorName().getMiddleInitial())) {
												sim *= 1.3;
											} else {
												sim *= 0.3; // the likelihood that someone wrote an article should plummet when this is the case
											}
										}
									}
								}
							}
						}
					} else {
						// For cases where middle initial is not present in rc_identity.
						for (ReCiterAuthor author : article.getArticleCoAuthors().getCoAuthors()) {
							if (author.getAuthorName().firstInitialLastNameMatch(TargetAuthor.getInstance().getAuthorName())) {
								for (ReCiterAuthor currentArticleAuthor : currentArticle.getArticleCoAuthors().getCoAuthors()) {
									if (currentArticleAuthor.getAuthorName().firstInitialLastNameMatch(TargetAuthor.getInstance().getAuthorName())) {
										if (currentArticleAuthor.getAuthorName().getMiddleInitial() == null &&
												author.getAuthorName().getMiddleInitial() != null) {
											// it's rare but not completely improbable that an author would share their 
											// middle initial on a paper but won't supply it on an official CV, or so I would argue
											sim *= 0.7;
										}
									}
								}
							}
						}
					}
				}
			}

//			if (!selectingTarget) {
//				JournalDao journalDao = new JournalDao();
//				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
//					// Use the cluster starter to compare journal similarity.
//					if (article.isClusterStarter()) {
//						if (article.getJournal() != null && currentArticle.getJournal() != null) {
//							double journalSimScore = journalDao.getJournalSimilarity(
//									article.getJournal().getIsoAbbreviation(),
//									currentArticle.getJournal().getIsoAbbreviation());
//							// Check similarity both ways.
//							if (journalSimScore == -1.0) {
//								journalSimScore = journalDao.getJournalSimilarity(
//										currentArticle.getJournal().getIsoAbbreviation(),
//										article.getJournal().getIsoAbbreviation());
//							}
//							if (journalSimScore != -1.0) {
//								if (journalSimScore > 0.8) {
//									sim *= (1 + journalSimScore); // Journal similarity on a sliding scale.
//								}
//							}
//						}
//					}
//				}
//			}

			if (selectingTarget) {

				// Update the similarity score with year discrepancy.
				int yearDiff = Integer.MAX_VALUE; // Compute difference in year between candidate article and closest year in article cluster.
				for (ReCiterArticle article : finalCluster.get(id).getArticleCluster()) {
					int currentYearDiff = Math.abs(TargetAuthor.getInstance().getTerminalDegreeYear() - article.getJournal().getJournalIssuePubDateYear());
					if (currentYearDiff < yearDiff) {
						yearDiff = currentYearDiff;
					}
				}
				// Moderately penalize articles that were published slightly before (0 - 7 years) a person's terminal 
				// degree, and strongly penalize articles that were published well before (>7 years) a person's terminal degree.
				if (yearDiff > 40) {
					sim *= 0.001526;
				} else {
					sim = sim * YearDiscrepacyReader.getYearDiscrepancyMap().get(yearDiff);
				}
				if (sim > targetAuthorSimilarityThreshold && sim > currentMax) {
					currentMaxId = id;
					currentMax = sim;
				}
			} else if (sim > similarityThreshold && sim > currentMax) {
				currentArticle.setInfo("Max Id: + " + currentMaxId + " sim: " + sim);
				currentMaxId = id;
				currentMax = sim;
				// TODO: what happens if cosine similarity is tied?
			}
		}
		return currentMaxId; // found a cluster.
	}

	/**
	 * 
	 * @param currentArticle
	 * @param targetAuthor
	 * @return
	 */
	// computes coauthor matches of this article with all current clusters.
	private Map<Integer, Integer> computeCoauthorMatch(ReCiterArticle currentArticle) {
		Map<Integer, Integer> coauthorsCount = new HashMap<Integer, Integer>(); // ClusterId to number of coauthors.
		for (Entry<Integer, ReCiterCluster> entry : finalCluster.entrySet()) {
			int clusterId = entry.getKey();
			ReCiterCluster reCiterCluster = entry.getValue();
			int matchingCoauthors = reCiterCluster.getMatchingCoauthorCount(currentArticle);
			coauthorsCount.put(clusterId, matchingCoauthors);
		}
		return coauthorsCount;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	// helper function to find keys in map data structure with max values.
	private Set<Integer> getKeysWithMaxVal(Map<Integer, Integer> map) {
		Set<Integer> keyList = new HashSet<Integer>();
		int maxValueInMap=(Collections.max(map.values()));  // This will return max value in the Hashmap
		//		System.out.println("Max value: " + maxValueInMap);
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			if (entry.getValue() == maxValueInMap && maxValueInMap != 0) {
				keyList.add(entry.getKey());
			}
		}
		return keyList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of clusters formed: " + getFinalCluster().size() + "\n");

		for (ReCiterCluster r : getFinalCluster().values()) {
			sb.append("{");
			sb.append(r.getClusterID());
			sb.append(" (size of cluster=");
			sb.append(r.getArticleCluster().size());
			sb.append("): ");
			for (ReCiterArticle a : r.getArticleCluster()) {
				sb.append(a.getArticleID());
				sb.append(", ");
			}
			sb.append("}\n");
		}
		return sb.toString();
	}

	@Override
	public double getArticleToArticleSimilarityThresholdValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
