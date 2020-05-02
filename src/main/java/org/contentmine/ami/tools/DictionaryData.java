package org.contentmine.ami.tools;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.contentmine.ami.tools.AMIDictionaryToolOLD.DictionaryFileFormat;
import org.contentmine.ami.tools.AMIDictionaryToolOLD.InputFormat;
import org.contentmine.ami.tools.AMIDictionaryToolOLD.Operation;
import org.contentmine.ami.tools.AMIDictionaryToolOLD.WikiLink;

class DictionaryData {
	static final Logger LOG = Logger.getLogger(DictionaryData.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

    String[]                dataCols;
    List<String>            dictionary;
    String                  dictionaryTopname;
	String                  href;
    String[]                hrefCols;
    InputFormat             informat;
    String                  input;
	String                  linkCol;
	String                  nameCol;
    Operation               operation;
    DictionaryFileFormat[]  outformats;
    String                  splitCol=",";
	String                  termCol;
    List<String>            terms;
	WikiLink[]              wikiLinks;

}
