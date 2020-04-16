package org.contentmine.ami.tools;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.contentmine.ami.tools.AMI.ShortErrorMessageHandler;
import org.contentmine.cproject.files.CProject;
import org.contentmine.cproject.files.DirectoryDeleter;
import org.contentmine.cproject.files.Unzipper;
import org.contentmine.cproject.util.CMineGlobber;
import org.contentmine.cproject.util.CMineTestFixtures;
import org.contentmine.eucl.euclid.util.CMFileUtil;
import org.junit.Assert;
import org.junit.Test;

import picocli.CommandLine;

/** test cleaning.
 * 
 * @author pm286
 *
 */
public class AMICleanTest {
	private static final Logger LOG = Logger.getLogger(AMICleanTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	@Test
	public void testHelp() {
		new AMICleanTool().runCommands(new String[]{});
	}

	/**
	 * 
	 */
	@Test
	public void testCleanForestPlotsSmall() throws Exception {
		Path temp = Files.createTempDirectory("ami-forestplotssmall");
		//System.out.println(temp);
		try {
			new Unzipper().extract(getClass().getResourceAsStream("/uclforest/forestplotssmall.zip"), temp.toFile());

			// gather all project files
			List<Path> before = listFully(temp);
			//System.out.println("BEFORE");
			//before.forEach(System.out::println);
			long svgCount = before.stream()
					.filter(f -> f.toString().contains(File.separator + "svg" + File.separator))
					.count();
			long pdfimagesCount = before.stream()
					.filter(f -> f.toString().contains(File.separator + "pdfimages" + File.separator))
					.count();
			long scholarlyCount = before.stream()
					.filter(f -> f.toString().contains(File.separator + "scholarly.html"))
					.count();
			assertNotEquals(0, svgCount);
			assertNotEquals(0, pdfimagesCount);
			assertNotEquals(0, scholarlyCount);

			String args =
					("-p " + temp.toFile().getAbsolutePath()
							+ " -vv clean"
							+ " **/svg/* **/pdfimages/*  **/scholarly.html");
			AMICleanTool amiCleaner = AMI.execute(AMICleanTool.class, args);

			CProject cProject = amiCleaner.getCProject();
			Assert.assertNotNull("CProject not null", cProject);

			// count all remaining files and assert the targers were deleted
			System.out.println("AFTER");
			List<Path> after = listFully(temp);
			//after.forEach(System.out::println);

			long afterSvgCount = after.stream()
					.filter(f -> f.toString().contains(File.separator + "svg" + File.separator))
					.count();
			long afterPdfimagesCount = after.stream()
					.filter(f -> f.toString().contains(File.separator + "pdfimages" + File.separator))
					.count();
			long afterScholarlyCount = after.stream()
					.filter(f -> f.toString().contains(File.separator + "scholarly.html"))
					.count();
			assertEquals(0, afterSvgCount);
			assertEquals(0, afterPdfimagesCount);
			assertEquals(0, afterScholarlyCount);

			long removedCount = svgCount + pdfimagesCount + scholarlyCount;
			assertEquals("Nothing else was deleted", after.size(), before.size() - removedCount);

		} finally {
			Files.walkFileTree(temp, new DirectoryDeleter());
		}
	}

	private List<Path> listFully(Path temp) throws IOException {
		List<Path> after = new ArrayList<>();
		Files.walkFileTree(temp, new SimpleFileVisitor<Path>() {
			// Invoke the pattern matching method on each file.
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				after.add(file);
				return CONTINUE;
			}

			// Invoke the pattern matching method on each directory.
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				after.add(dir);
				return CONTINUE;
			}
		});
		return after;
	}


	@Test
	/**
	 * tests cleaning directories in a project for ami-search
	 */
	public void testCleanResultsGlob() throws IOException {
		File sourceDir = new File("src/test/resources/org/contentmine/ami/oil5");
		CMFileUtil.assertExistingDirectory(sourceDir);
		File targetDir = new File("target/oil5");
		CMFileUtil.forceDelete(targetDir);
		CMineTestFixtures.cleanAndCopyDir(sourceDir, targetDir);
		CMFileUtil.assertExistingDirectory(targetDir);
		// old style. we'll replace
		List<File> files = new CMineGlobber().setGlob("**/*.xml").setLocation(targetDir).setRecurse(true).listFiles();
		Assert.assertEquals("xml "+files.size(), 792, files.size());

		String args;
		// delete children of ctrees
		args = ""
			+ "-p " + targetDir + " clean"
			//+ " --fileglob "
			+ " **/*.xml"
 			+ " gene.**.xml"
		    + " **/species.*"
		    + " search.*"
		    + " xml";
		AMI.execute(args);
		
		files = new CMineGlobber().setGlob("**/*.xml").setLocation(targetDir).setRecurse(true).listFiles();
		Assert.assertEquals("xml "+files.size(), 0, files.size());
		System.out.println("files: "+files.size());
	}

//	@Test
//	/**
//	 * tests cleaning directories in a project for ami-search
//	 */
//	public void testCleanResultsRegex() throws IOException {
//		File sourceDir = new File("src/test/resources/org/contentmine/ami/oil5");
//		CMFileUtil.assertExistingDirectory(sourceDir);
//		File targetDir = new File("target/oil5");
//		CMFileUtil.forceDelete(targetDir);
//		CMineTestFixtures.cleanAndCopyDir(sourceDir, targetDir);
//		CMFileUtil.assertExistingDirectory(targetDir);
//		// old style. we'll replace
//		List<File> files = new CMineGlobber().setRegex(".*\\.xml").setLocation(targetDir).setRecurse(true).listFiles();
//		Assert.assertEquals("xml "+files.size(), 792, files.size());
//
//		String args;
//		// delete children of ctrees
//		args = ""
//			+ "-p " + targetDir + " clean"
//			+ " --fileregex "
//			+ " .*\\.xml"
//		    + "";
//		AMI.execute(args);
//
//		files = new CMineGlobber().setRegex(".*\\.xml").setLocation(targetDir).setRecurse(true).listFiles();
//		Assert.assertEquals("xml "+files.size(), 0, files.size());
//		System.out.println("files: "+files.size());
//	}


}
