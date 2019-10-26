package org.contentmine.norma.sections;

import java.util.Arrays;
import java.util.List;

import org.contentmine.eucl.euclid.Util;

import nu.xom.Element;

public class JATSLabelElement extends JATSElement implements IsInline {

	/**
		
	 */
	public static String TAG = "label";

	public JATSLabelElement(Element element) {
		super(element);
	}

	@Override
	public String debugString() {
		return "["+this.getValue()+"]";
	}

	@Override
	public String debugString(int level) {
		StringBuilder sb = new StringBuilder();
		sb.append("["+this.getValue()+"]");
		return /*Util.spaces(level)+TAG+*/":"+sb.toString()+"\n";
	}

}