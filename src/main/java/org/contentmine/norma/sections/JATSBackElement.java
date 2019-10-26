package org.contentmine.norma.sections;

import java.util.Arrays;
import java.util.List;

import nu.xom.Element;

public class JATSBackElement extends JATSElement implements IsBlock, HasDirectory {

	static String TAG = "back";
	
	public final static List<String> ALLOWED_CHILD_NAMES = Arrays.asList(new String[] {
			JATSDivFactory.FN_GROUP,
			JATSDivFactory.REF_LIST,
			JATSDivFactory.ACK,
			JATSDivFactory.SEC,
			JATSDivFactory.BIO,
			JATSDivFactory.APP_GROUP,
			JATSDivFactory.GLOSSARY,
			JATSDivFactory.NOTES,
			
	});
	
	@Override
	protected List<String> getAllowedChildNames() {
		return ALLOWED_CHILD_NAMES;
	}
	
	private JATSReflistElement reflist;
	private JATSFnGroupElement fnGroup;

	public JATSBackElement(Element element) {
		super(element);
	}

	public JATSReflistElement getReflist() {
		return reflist;
	}

	public JATSFnGroupElement getFnGroup() {
		return fnGroup;
	}

	protected void applyNonXMLSemantics() {
		fnGroup = (JATSFnGroupElement) getSingleChild(JATSFnGroupElement.TAG);
		reflist = (JATSReflistElement) getSingleChild(JATSReflistElement.TAG);
	}

	public String directoryName() {
		return this.TAG;
	}



}
