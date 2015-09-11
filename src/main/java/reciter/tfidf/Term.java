package reciter.tfidf;

import java.util.HashSet;
import java.util.Set;

public class Term {

	private Set<Long> documentIds; // Set of documents which contains this term.
	private String term;
	private double tfidfScore;

	public Term(String term) {
		this.term = term;
		documentIds = new HashSet<Long>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((term == null) ? 0 : term.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}

	public Set<Long> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(Set<Long> documentIds) {
		this.documentIds = documentIds;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public double getTfidfScore() {
		return tfidfScore;
	}

	public void setTfidfScore(double tfidfScore) {
		this.tfidfScore = tfidfScore;
	}

}
