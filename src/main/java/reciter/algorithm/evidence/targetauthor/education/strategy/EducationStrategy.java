package reciter.algorithm.evidence.targetauthor.education.strategy;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import reciter.algorithm.evidence.targetauthor.AbstractTargetAuthorStrategy;
import reciter.model.article.ReCiterArticle;
import reciter.model.author.AuthorEducation;
import reciter.model.author.ReCiterAuthor;
import reciter.model.author.TargetAuthor;
import reciter.tfidf.ReCiterStringUtil;
import xmlparser.scopus.model.Author;
import xmlparser.scopus.model.ScopusArticle;

public class EducationStrategy extends AbstractTargetAuthorStrategy {

	@Override
	public double executeStrategy(ReCiterArticle reCiterArticle, TargetAuthor targetAuthor) {
		int numWordsOverlap = 0;
		ScopusArticle scopusArticle = reCiterArticle.getScopusArticle();
		if (scopusArticle != null) {
			numWordsOverlap += numWordsOverlapByEducationFromScopus(scopusArticle, targetAuthor);
		}
		if (reCiterArticle.getArticleCoAuthors() != null) {
			numWordsOverlap += numWordsOverlapByEducationFromPubmed(reCiterArticle.getArticleCoAuthors().getAuthors(), targetAuthor);
		}
		reCiterArticle.setClusterInfo(reCiterArticle.getClusterInfo() + "[Education number of overlapping words=" + numWordsOverlap + "]");
		reCiterArticle.setEducationScore(numWordsOverlap);
		return numWordsOverlap;
	}

	@Override
	public double executeStrategy(List<ReCiterArticle> reCiterArticles, TargetAuthor targetAuthor) {
		int numWordsOverlap = 0;
		for (ReCiterArticle reCiterArticle : reCiterArticles)
			numWordsOverlap += executeStrategy(reCiterArticle, targetAuthor);
		return numWordsOverlap;
	}
	
	private int numWordsOverlapByEducationFromPubmed(List<ReCiterAuthor> authors, TargetAuthor targetAuthor) {
		int numWordsOverlap = 0;
		for (ReCiterAuthor author : authors) {
			String lastName = author.getAuthorName().getLastName();
			if (lastName != null) {
				if (StringUtils.equalsIgnoreCase(targetAuthor.getAuthorName().getLastName(), lastName)) {
					String authorEducationConcatenation = null;
					if (targetAuthor.getEducations() != null) {
						authorEducationConcatenation = concatenateAuthorEducationFields(targetAuthor.getEducations());
					}
					String affiliation = author.getAffiliation().getAffiliationName();
					if (affiliation != null && authorEducationConcatenation != null) {
						numWordsOverlap += ReCiterStringUtil.computeNumberOfOverlapTokens(authorEducationConcatenation, affiliation);
					}
				}
			}
		}
		return numWordsOverlap;
	}
	
	private int numWordsOverlapByEducationFromScopus(ScopusArticle scopusArticle, TargetAuthor targetAuthor) {
		int numWordsOverlap = 0;
		if (scopusArticle != null) {
			for (Entry<Long, Author> entry : scopusArticle.getAuthors().entrySet()) {
				boolean isNameMatch = entry.getValue().getSurname().equals(targetAuthor.getAuthorName().getLastName());
				if (isNameMatch) {
					if (scopusArticle.getAffiliationMap() != null && 
						scopusArticle.getAffiliationMap().get(entry.getKey()) != null) {
						
						String scopusAffiliation = scopusArticle.getAffiliationMap().get(entry.getKey()).getAffilname();
						String scopusAffiliationNameVariant = scopusArticle.getAffiliationMap().get(entry.getKey()).getNameVariant();
						String affiliationCity = scopusArticle.getAffiliationMap().get(entry.getKey()).getAffiliationCity();
						String affiliationCountry = scopusArticle.getAffiliationMap().get(entry.getKey()).getAffiliationCountry();
						
						StringBuilder scopusSb = new StringBuilder();
						if (scopusAffiliation != null) {
							scopusSb.append(scopusAffiliation);
							scopusSb.append(" ");
						}
						
						if (scopusAffiliationNameVariant != null) {
							scopusSb.append(scopusAffiliationNameVariant);
							scopusSb.append(" ");
						}
						
						if (affiliationCity != null) {
							scopusSb.append(affiliationCity);
							scopusSb.append(" ");
						}
						
						if (affiliationCountry != null) {
							scopusSb.append(affiliationCountry);
							scopusSb.append(" ");
						}
						
						String scopusString = scopusSb.toString();
						
						String authorEducationConcatenation = null;
						if (targetAuthor.getEducations() != null) {
							authorEducationConcatenation = concatenateAuthorEducationFields(targetAuthor.getEducations());
						}
						
						if (scopusString != null && authorEducationConcatenation != null) {
							numWordsOverlap += ReCiterStringUtil.computeNumberOfOverlapTokens(scopusString, authorEducationConcatenation);
						}
					}
				}
			}
		}
		return numWordsOverlap;
	}
	
	private String concatenateAuthorEducationFields(List<AuthorEducation> authorEducations) {
		String authorEducationConcatenation = null;
		if (authorEducations != null) {
			for (AuthorEducation authorEducation : authorEducations) {
				String institution = authorEducation.getInstitution();
				String degreeYear = Integer.toString(authorEducation.getDegreeYear());
				String degreeField = authorEducation.getDegreeField();
				String instLoc = authorEducation.getInstLoc();
				String instAbbr = authorEducation.getInstAbbr();
				
				
				StringBuilder sb = new StringBuilder();
				if (institution != null) {
					sb.append(institution);
					sb.append(" ");
				}
				
				if (degreeYear != null && !"0".equals(degreeYear)) {
					sb.append(degreeYear);
					sb.append(" ");
				}
				
				if (degreeField != null) {
					sb.append(degreeField);
					sb.append(" ");
				}
				
				if (instLoc != null) {
					sb.append(instLoc);
					sb.append(" ");
				}
				
				if (instAbbr != null) {
					sb.append(instAbbr);
				}
				authorEducationConcatenation = sb.toString();
			}
		}
		return authorEducationConcatenation;
	}
}