package reciter.algorithm.evidence.targetauthor.affiliation;

import java.util.List;

import reciter.algorithm.evidence.targetauthor.TargetAuthorStrategy;
import reciter.algorithm.evidence.targetauthor.TargetAuthorStrategyContext;
import reciter.model.article.ReCiterArticle;
import reciter.model.author.TargetAuthor;

public class AffiliationStrategyContext implements TargetAuthorStrategyContext {
	private final TargetAuthorStrategy strategy;
	private double strategyScore;
	
	public AffiliationStrategyContext(TargetAuthorStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public double executeStrategy(List<ReCiterArticle> reCiterArticles, TargetAuthor targetAuthor) {
		strategyScore = strategy.executeStrategy(reCiterArticles, targetAuthor);
		return strategyScore;
	}

	@Override
	public double executeStrategy(ReCiterArticle reCiterArticle, TargetAuthor targetAuthor) {
		return strategy.executeStrategy(reCiterArticle, targetAuthor);
	}
}
