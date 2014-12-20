/* 
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.rabidgremlin.legalbeagle;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.rabidgremlin.legalbeagle.maven.MavenJarIdentifier;
import com.rabidgremlin.legalbeagle.report.Report;
import com.rabidgremlin.legalbeagle.report.ReportWriter;
import com.rabidgremlin.legalbeagle.report.TsvReportWriter;
import com.rabidgremlin.legalbeagle.util.FileCollector;
import com.rabidgremlin.legalbeagle.util.HttpHelper;

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

	  log.info("Scanning {}", cmdOptions.dir);
	  log.info("Writring output to {}", cmdOptions.outputFile);
	}
	catch (ParameterException e)
	{
	  jCommander.usage();
	  System.exit(1);
	}

	// find and identify licenses
	try
	{
	  // determine report writer to used
	  ReportWriter reportWriter = null;
	  
	  if (cmdOptions.outputFile.endsWith(".tsv"))
	  {
		reportWriter = new TsvReportWriter();
	  }
	  else
	  {
		throw new Exception("Unsupported output file type. Only .tsv currently supported");
	  }
	  	  
	  
	  // create http helper
	  HttpHelper httpHelper = new HttpHelper();

	  // create report object to hold results
	  Report report = new Report();

	  // scan supplied dir (and sub dirs) for .jar files
	  FileCollector fc = new FileCollector();
	  List<File> files = fc.getJars(new File(cmdOptions.dir));

	  // add files to report
	  report.addFiles(files);

	  log.info("Found {} files to process", report.getReportItems().size());

	  // try identify file using maven and file signatures
	  MavenJarIdentifier mavenJarIdentifier = new MavenJarIdentifier(httpHelper);
	  mavenJarIdentifier.identifyFiles(report);

	  // write the report	  
	  reportWriter.generateReport(cmdOptions.outputFile, report);

	  log.info("Done!");
	}
	catch (Exception e)
	{
	  log.error("Oops!", e);
	}

  }
}
