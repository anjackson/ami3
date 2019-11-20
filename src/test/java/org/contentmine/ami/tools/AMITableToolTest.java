package org.contentmine.ami.tools;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

public class AMITableToolTest extends AbstractAMITest {
	private static final Logger LOG = Logger.getLogger(AMITableToolTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testSummaryTableOil() {
		File dir = OIL5;
//		File dir = OIL186;
		new AMISectionTool().runCommands("-p " + dir );
		String args = ""
				+ "-p " + dir + ""
				+ " --summarytable __table/summary.html"
				+ " --tabledir sections/tables"
				
				+ " --templatefile "+CEV_SEARCH+"/../templates/phytomedchem.xml"
				+ " --template composition"
				
			;
		new AMITableTool().runCommands(args);
	}

	@Test
	public void testColumnTypesOil() {
		File dir = OIL5;
//		File dir = OIL186;
//		new AMISectionTool().runCommands("-p " + dir +" --forcemake");
		String args = ""
				+ "-p " + dir + ""
				+ " --tabledir sections/tables"
				+ " --columntypes"
				+ " --templatefile "+CEV_SEARCH+"/../templates/phytomedchem.xml"
//				+ " --templatefile "+CEV_SEARCH+"/../templates/phytomedchem0.xml"
				+ " --template composition"
				+ " --multiset compound"
				
			;
		new AMITableTool().runCommands(args);
	}


	@Test
	public void testSummaryTableClimate() {
//		System.out.println("DIR "+OIL186+" X "+OIL186.exists());
		String args = ""
				+ "-p " + CMIP200 + ""
				+ " --summarytable __table/summary.html"
				+ " --tabledir sections/tables"
				
				+ " --templatefile "+CLIM_SEARCH+"/../templates/climate.xml"
				+ " --template composition"
			;
		new AMITableTool().runCommands(args);
	}

}
