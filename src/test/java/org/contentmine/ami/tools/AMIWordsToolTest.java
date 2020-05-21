package org.contentmine.ami.tools;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.contentmine.ami.AMIFixtures;
import org.contentmine.ami.tools.AMITransformTool;
import org.contentmine.ami.tools.AMIWordsTool;
import org.contentmine.cproject.util.CMineTestFixtures;
import org.junit.Test;

public class AMIWordsToolTest extends AbstractAMITest {
	private static final Logger LOG = Logger.getLogger(AMIWordsToolTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	/** a regression test. Uses conventional Old search */
	public void testOilSearch() {
//		File testFile = OIL5;
//		String name = testFile.getName();
//		File targetDir = new File("target/cooccurrence/", name);
//		CMineTestFixtures.cleanAndCopyDir(testFile, targetDir);
//		/** need HTML */
//		String args = 
//				"-p /Users/pm286/workspace/cmdev/normami/target/cooccurrence/"+name+"/";
//			;
//		new AMITransformTool().runCommands(args);
//		// transformed norma
		System.out.println("output "+OIL5);
		String cmd = ""
				+ " -p " + OIL5
			;
//		AMISearchToolTest. cmd);
	}
	

	@Test
	public void testOil5() {
//		File testFile = OIL5;
//		String name = testFile.getName();
//		File targetDir = new File("target/cooccurrence/", name);
//		CMineTestFixtures.cleanAndCopyDir(testFile, targetDir);
//		/** need HTML */
//		String args = 
//				"-p /Users/pm286/workspace/cmdev/normami/target/cooccurrence/"+name+"/";
//			;
//		new AMITransformTool().runCommands(args);
//		// transformed norma
		System.out.println("output "+OIL5);
		String cmd = ""
//				"-h"
//				+ " -p /Users/pm286/workspace/cmdev/normami/target/cooccurrence/"+name+"/"
				+ " -p " + OIL5
				+ " -vv"
				+ " words"
				+ " --stopwords pmcstop.txt stopwords.txt"
				+ " --methods frequencies "  //wordLengths"
				+ " --stripNumbers" // works
				+ " --stemming" // works
				+ " --wordCount 20,40"
			;
		AMI.execute(AMIWordsTool.class, cmd);
//		new AMIWordsTool().runCommands(args);
	}
	
	
	@Test
	public void testZikaWords() {
		File targetDir = new File("target/words/zika10");
		CMineTestFixtures.cleanAndCopyDir(AMIFixtures.TEST_ZIKA10_DIR, targetDir);
		System.out.println("target> "+targetDir);
		System.out.println(AMIFixtures.TEST_ZIKA10_DIR);
		String cmd = 
//				"-p /Users/pm286/workspace/cmdev/normami/target/cooccurrence/zika10"
				"-p " + targetDir.getAbsolutePath()
				+ " words" 
				+ " --xpath table=//table-wrap table-td=//table-wrap//td p=//p"
				+ " --methods frequencies wordLengths"
//				+ " --dictionary country disease funders"
			;
		AMI.execute(AMIWordsTool.class, cmd);
		
	}

//	+ "-p " + AMIFixtures.TEST_ZIKA10_DIR
//	+ " --forcemake"
//	+ " section"
//	+ " --sections ALL"
//	+ " --sectiontype XML"
//;
//	+ "-p " + AMIFixtures.TEST_ZIKA10_DIR
//	+ " --forcemake"
//	+ " section"
//	+ " --sections ALL"
//	+ " --sectiontype XML"
//;

}
