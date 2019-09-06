package org.contentmine.norma.sections;

import java.util.Arrays;
import java.util.List;

import nu.xom.Element;

public class JATSFnElement extends JATSElement {

	/**
		<journal-meta>
		...
		</journal-meta>
		<article-meta>
		</article-meta>
		
	 */
	static String TAG = "fn";

	public final static List<String> ALLOWED_CHILD_NAMES = Arrays.asList(new String[] {
			JATSSpanFactory.P,
			JATSSpanFactory.LABEL,
	});
	
	@Override
	protected List<String> getAllowedChildNames() {
		return ALLOWED_CHILD_NAMES;
	}

//	private JATSJournalMetaElement journalMeta;
//	private JATSArticleMetaElement articleMeta;
	
	public JATSFnElement(Element element) {
		super(element);
	}

	protected void applyNonXMLSemantics() {
//		journalMeta = (JATSJournalMetaElement) this.getSingleChild(JATSJournalMetaElement.TAG);
//		articleMeta = (JATSArticleMetaElement) this.getSingleChild(JATSArticleMetaElement.TAG);
	}
}
