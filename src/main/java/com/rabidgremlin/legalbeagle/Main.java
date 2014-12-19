package com.rabidgremlin.legalbeagle;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.rabidgremlin.legalbeagle.maven.MavenArtifact;
import com.rabidgremlin.legalbeagle.maven.MavenJarIdentifier;
import com.rabidgremlin.legalbeagle.report.DebugReportWriter;
import com.rabidgremlin.legalbeagle.report.Report;
import com.rabidgremlin.legalbeagle.util.FileCollector;
import com.rabidgremlin.legalbeagle.util.HttpHelper;
import com.rabidgremlin.legalbeagle.xmlbindings.License;
import com.rabidgremlin.legalbeagle.xmlbindings.Model;
import com.rabidgremlin.legalbeagle.xmlbindings.Model.Licenses;

//-Dhttp.proxyHost=10.0.0.100 -Dhttp.proxyPort=8800

public class Main
{
  private final static Logger log = LoggerFactory.getLogger(Main.class);

  

  public static void main(String[] args)
  {

	// set up command line parsing
	CommandLineOptions cmdOptions = new CommandLineOptions();
	JCommander jCommander = new JCommander(cmdOptions);
	jCommander.setProgramName("java -jar legalbeagle.jar");

	// parse and validate command line opts
	try
	{
	  jCommander.parse(args);
	  
	  log.info("Scanning {}",cmdOptions.dir);
	  log.info("Writring output to {}",cmdOptions.outputFile);
	}
	catch (ParameterException e)
	{
	  jCommander.usage();
	  System.exit(1);
	}

	// find and identify licenses
	try
	{

	  // create http helper
	  HttpHelper httpHelper = new HttpHelper();

	  // create report object to hold results
	  Report report = new Report();

	  // scan supplied dir (and sub dirs) for .jar files
	  FileCollector fc = new FileCollector();
	  List<File> files = fc.getJars(new File(cmdOptions.dir));
	    

	  // add files to report
	  report.addFiles(files);	  
	  
	  log.info("Found {} files to process",report.getReportItems().size());
	  
	  // try identify file using maven and file signatures
	  MavenJarIdentifier mavenJarIdentifier = new MavenJarIdentifier(httpHelper);
	  mavenJarIdentifier.identifyFiles(report);
	  
	  // write the report
	  DebugReportWriter reportWriter = new DebugReportWriter();
	  reportWriter.generateReport(cmdOptions.outputFile, report);
	  
	  log.info("Done!");
	}
	catch (Exception e)
	{
	  log.error("Oops!",e);
	}

  }
}
