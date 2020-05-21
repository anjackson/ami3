package org.contentmine.ami.tools;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.contentmine.cproject.files.CProject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/** test cleaning.
 * 
 * @author pm286
 *
 */
public class AMIImageTest extends AbstractAMITest {
	private static final File OLD_DEVTEST = new File(SRC_TEST_AMI, "uclforest/devtest/");
	private static final File FOREST_PLOT_SMALL = new File(SRC_TEST_AMI, "uclforest/forestplotssmall/");
	private static final File OLD_SPSS = new File(SRC_TEST_AMI, "uclforest/spss/");
	
	
	private static final Logger LOG = Logger.getLogger(AMIImageTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	boolean started = false;

	@Before
	public void startUp() {
		if (!started) {
			ensurePdfImages(FOREST_PLOT_SMALL);
			ensurePdfImages(OLD_DEVTEST);
			ensurePdfImages(OLD_SPSS); 
			ensurePdfImages(new File(SRC_TEST_AMI, "battery10")); 
		}
		started = true;
	}

	private void ensurePdfImages(File dir) {
		String cmd = "-p " + dir 
			+ " pdfbox";
		AMIPDFTool pdfTool = AMI.execute(AMIPDFTool.class, cmd);
	}
	
	@Test
	public void testHelp() {
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, "image --help");
	}
	
	@Test
	public void testParseSpecifics() {
		String cmd = "-v image --minwidth 20";
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
	}
	
	@Test
	/** 
	 */
	public void testFilterTrees() throws Exception {
		System.out.println("TREE " + new File(OLD_DEVTEST , "mcarthur_etal2012_cochran"));
		String cmd = 
//				"-t "+OLD_DEVTEST+"/bowmann-perrottetal_2013"
//				"-t "+OLD_DEVTEST+"/buzick_stone_2014_readalo"
//				"-t "+OLD_DEVTEST+"/campbell_systematic_revie"
//				"-t "+OLD_DEVTEST+"/case_systematic_review_ar"
				"-t " + new File(OLD_DEVTEST, "mcarthur_etal2012_cochran")
//				"-t "+OLD_DEVTEST+"/puziocolby2013_co-operati"
//				"-t "+OLD_DEVTEST+"/torgersonetal_2011dferepo"
//				"-t "+OLD_DEVTEST+"/zhengetal_2016"
				+ " --inputname raw"
				+ " image"
				+ " --sharpen sharpen4"
				+ " --threshold 180"
				+ " --binarize GLOBAL_ENTROPY"
//				+ " --rotate 270"
				+ " --priority SCALE"
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
	}

	@Test
	/** 
	 * mainly for visual inspection of results
	 */
	public void testBinarize() throws Exception {
		String cmd = 
//				"-t "+OLD_DEVTEST+"/bowmann-perrottetal_2013"
//				"-t "+OLD_DEVTEST+"/buzick_stone_2014_readalo"
//				"-t "+OLD_DEVTEST+"/campbell_systematic_revie"
				"-t "+OLD_DEVTEST+"/case_systematic_review_ar"
//				"-t "+OLD_DEVTEST+"/mcarthur_etal2012_cochran"
//				"-t "+OLD_DEVTEST+"/puziocolby2013_co-operati"
//				"-t "+OLD_DEVTEST+"/torgersonetal_2011dferepo"
//				"-t "+OLD_DEVTEST+"/zhengetal_2016"
//				+ " --sharpen sharpen4"
				+ " --inputname raw"
                + " image"
				+ " --threshold 180"
				+ " --binarize BLOCK_OTSU"
//				+ " --rotate 270"
				+ " --priority SCALE"
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
	}
	
	@Test
	/** 
	 * mainly for visual inspection of results
	 */
	public void testBinarizeMethods() throws Exception {
		String cmd = "-t "+OLD_DEVTEST+"/case_systematic_review_ar"
				+ " --inputname raw"
				+ " image"
				+ " --binarize ";
		String[] methods = {
			"GLOBAL_MEAN",
			"GLOBAL_ENTROPY",
			"BLOCK_MIN_MAX",
			"BLOCK_OTSU",
//			"LOCAL_MEAN",    // large spurius blocks
//			"BLOCK_MEAN",    // many large spurious blocks
//			"LOCAL_GAUSSIAN", // spurious blocks
//			"LOCAL_SAUVOLA",  // stripes
//			"LOCAL_NICK",    // erosion
//			"GLOBAL_OTSU",   // spurious blocks
		};
		for (String method : methods) {
			AMIImageTool imageTool = AMI.execute(AMIImageTool.class,  cmd + method);
		}
	}

	@Test
	/** 
	 * mainly for visual inspection of results
	 */
	public void testBinarizeMethodsAll() throws Exception {
		String[] names = {
            "bowmann-perrottetal_2013",
            "buzick_stone_2014_readalo",
            "campbell_systematic_revie",
            "case_systematic_review_ar",
            "case-systematic-review-ju",
            "cole_2014",
            "davis2010_dissertation",
            "donkerdeboerkostons2014_l",
            "ergen_canagli_17_",
            "fanetal_2017_meta_science",
            "higginshallbaumfieldmosel",
            "kunkel_2015",
            "marulis_2010-300-35review",
            "mcarthur_etal2012_cochran",
            "puziocolby2013_co-operati",
            "rui2009_meta_detracking",
            "shenderovichetal_2016_pub",
            "torgersonetal_2011dferepo",
            "zhengetal_2016",
		};
		String[] methods = {
			"GLOBAL_MEAN",
			"GLOBAL_ENTROPY",
			"BLOCK_MIN_MAX",
			"BLOCK_OTSU",
		};
		for (String name : names) {
			String tree = "-t "+OLD_DEVTEST+"/";
			String treename = tree + name;
			String cmd = treename 
					+ " --inputname raw"
					+ " image --binarize ";
			for (String method : methods) {
//				AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd + method);
			}
			for (int threshold : new int[] {120, 140, 160, 180, 200, 220}) {
				cmd = treename + ""
						+ " -v  image"
						+ " --monochrome monochrome"
						+ " --small small"
						+ " --duplicate duplicate"
						+ " --sharpen sharpen4"
						+ " --threshold "+threshold;
				System.out.println("<"+cmd+">");
				AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
			}
		}
	}



	/** 
	imageProcessor.setThreshold(180);
	This is a poor subpixel image which need thresholding and sharpening
 */
	@Test
	public void testFilterProject() throws Exception {
		String cmd = 
				"-p "+OLD_DEVTEST+"/"
                + " image"
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
	}


	@Test
	/** 
	 */
	public void testBinarizeTrees() throws Exception {
		String cmd = 
//				"-t "+OLD_DEVTEST+"/bowmann-perrottetal_2013"
				"-t "+new File(OLD_DEVTEST, "buzick_stone_2014_readalo")
//				"-t "+OLD_DEVTEST+"/campbell_systematic_revie"
//				"-t "+OLD_DEVTEST+"/mcarthur_etal2012_cochran"
//				"-t "+OLD_DEVTEST+"/puziocolby2013_co-operati"
//				"-t "+OLD_DEVTEST+"/torgersonetal_2011dferepo"
//				"-t "+OLD_DEVTEST+"/zhengetal_2016"
//				+ " --binarize xLOCAL_MEAN"
				+ " image"

				+ " --threshold 180"
//				+ " --sharpen x"
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
	}


	@Test
	/** 
		imageProcessor.setThreshold(180);
		This is a poor subpixel image which need thresholding and sharpening
	 */
	public void testScale() throws Exception {
		String cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
				+ " --inputname raw"
				+ " image"
				+ " --scalefactor 0.5"
				+ " --maxwidth 100"
				+ " --maxheight 100"
				+ "";

		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
	}

	@Test
	/** 
	 * rotate by multiples of 90 degrees
	 */
	public void testRotate() throws Exception {
		String cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
				+ " --inputname raw"
				+ " image"
				+ " --rotate 90"
				+ "";
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --rotate 180"
				+ "";
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --rotate 270";
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --rotate 0"
//				+ " --inputname rot0"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
	}

	@Test
	/** 
		imageProcessor.setThreshold(180);
		This is a poor subpixel image which need thresholding and sharpening
	 */
	public void testThreshold() throws Exception {
		String cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ "";
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 160"
//				+ " --inputname thresh160";
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 20"
//				+ " --inputname thresh20"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 30"
//				+ " --inputname thresh30"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 35"
//				+ " --inputname thresh35"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 40"
//				+ " --inputname thresh40"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 200"
//				+ " --thinning none"
//				+ " --binarize min_max"
				+ " --inputname threshold200";
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 220"
//				+ " --thinning none"
//				+ " --binarize min_max"
//				+ " --inputname threshold220"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 225"
//				+ " --thinning none"
//				+ " --binarize min_max"
//				+ " --inputname threshold225"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 230"
//				+ " --thinning none"
//				+ " --binarize min_max"
//				+ " --inputname threshold230"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = 
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --threshold 240"
//				+ " --thinning none"
//				+ " --binarize min_max"
//				+ " --inputname threshold240"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
	}
	
	@Test
	/** 
	 */
	public void testBitmapForestPlotsSmall() throws Exception {
		String cmd = 
				"-p "+FOREST_PLOT_SMALL+""
						+ " --inputname raw"
				+ " image"
				+ " --threshold 180"
//				+ " --thinning none"
//				+ " --binarize entropy"
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
	}
	
	@Test
	/** 
	 */
	public void testSharpen() throws Exception {
		String cmd =
				"-p "+FOREST_PLOT_SMALL+""
						+ " --inputname raw"
				+ " image"
				+ " --sharpen sharpen4"
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd =
				"-p "+FOREST_PLOT_SMALL+""
						+ " --inputname raw"
				+ " image"
				+ " --sharpen sharpen8"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd =
				"-p "+FOREST_PLOT_SMALL+""
						+ " --inputname raw"
				+ " image"
				+ " --sharpen laplacian"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
	}

	@Test
	/** 
	 */
	public void testSharpenBoofcv() throws Exception {
		String cmd =
				"-t "+FOREST_PLOT_SMALL+"/cole"
						+ " --inputname raw"
				+ " image"
				+ " --sharpen sharpen4"
//				+ " --inputname sharpen4mean"
//				+ " --binarize local_mean"
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd =
				"-p "+FOREST_PLOT_SMALL+""
						+ " --inputname raw"
				+ " image"
				+ " --sharpen laplacian"
//				+ " --inputname laplacian"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
	}
	
	@Test
	/** 
	 */
	public void testSharpenThreshold1() throws Exception {
		String cmd =
				"-p "+FOREST_PLOT_SMALL+""
						+ " --inputname raw"
				+ " image"
				+ " --sharpen sharpen8"
				+ " --binarize block_otsu"

				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
		cmd = ""
				+ " --inputname raw"
				+ " -p "+FOREST_PLOT_SMALL+""
				+ " image"
				+ " --sharpen laplacian"
				+ " --threshold 180"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
	}

	@Test
	/** 
	 * 
	 */
	public void testImageForestPlotsSmall() throws Exception {
		String cmd = ""
				+ " -p "+FOREST_PLOT_SMALL
				+ " --inputname raw"
				+ " image"
				+ " --monochrome monochrome"
//				+ " --monochromedir monochrome"
				+ " --minwidth 100"
				+ " --minheight 100"
//				+ " --smalldir small"
				+ " --duplicate _delete"
//				+ " --duplicatedir duplicates"
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
	} 
	
	@Test
	/** 
	 * 
	 */
	public void testImagePanels() throws Exception {
		File projectDir = OLD_SPSS;
		String cmd = 
				"-p "+projectDir
				+ " --inputname raw"
				+ " image"
				+ " --minwidth 100"
				+ " --minheight 100"
				+ " --monochrome monochrome"
				+ " --small small"
				+ " --duplicate duplicate"
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
	}

	@Test
	public void testAddBorders() {
		File projectDir = OLD_SPSS;
		File treeDir = new File(projectDir, "PMC5502154");
		String cmd = 
				"-t "+treeDir
						+ " --inputname raw"
						+ " image"
				+ " --scalefactor 2.0"
				+ " --erodedilate" 
				+ " --borders 10 "
				;
		AMIImageTool imageTool = AMI.execute(AMIImageTool.class, cmd);
		
	}

	@Test
	public void testTreeList() {
		String cmd = null;
		File cProjectDir = new File(SRC_TEST_AMI, "battery10");
		CProject project = new CProject(cProjectDir);
		List<String> treeNames = Arrays.asList(new String[] {
				"PMC3776197",
				"PMC4062906",
				"PMC4709726",
				"PMC5082456",
				"PMC5082892",
				"PMC5115307",
				"PMC5241879",
				"PMC5604389",
				});
		String treeNamesString = String.join(" ", treeNames);
		cmd = "-p " + project
				+ " -v"
				+ " clean */svg/*"
				+ " clean */pdfimages/*"
				 ;
		AbstractAMITool imageTool = AMI.execute(AMICleanTool.class, cmd);

		cmd = "-p " + project
				+ " -v"
				+ " --inputname raw"
				+ " --includetree " + treeNamesString
				+ " pdfbox"
				+ " image"
//								+ " --includetree " + treeNamesString

//				+ " clean */svg/*"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		System.out.println("imageTool? " + imageTool);
//		Assert.assertEquals("class", imageTool.getClass(), AMIImageTool.class);
		cmd = "-p " + project
				+ " -v"
				+ " --inputname raw"
				+ " --includetree " + treeNamesString
				+ " image"
//				+ " pdfbox"
//				+ " --includetree " + treeNamesString
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		System.out.println("imageTool? " + imageTool);
		cmd = "-p " + project
				+ " -v"
				+ " --inputname raw"
				+ " --includetree " + treeNamesString
				+ " image"
				+ " --small=small --monochrome=monochrome --duplicate=duplicate"
				;
		imageTool = AMI.execute(AMIImageTool.class, cmd);
		System.out.println("imageTool? " + imageTool);
				
	}
	
	

}
