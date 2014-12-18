package com.rabidgremlin.legalbeagle;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.rabidgremlin.legalbeagle.xmlbindings.License;
import com.rabidgremlin.legalbeagle.xmlbindings.Model;
import com.rabidgremlin.legalbeagle.xmlbindings.Model.Licenses;

//-Dhttp.proxyHost=10.0.0.100 -Dhttp.proxyPort=8800

public class Main
{

  private static List<License> getLicense(HttpHelper httpHelper, Model pom) throws Exception
  {
	if (pom == null)
	{
	  return null;
	}

	Licenses licenses = pom.getLicenses();

	if (licenses != null)
	{
	  return licenses.getLicense();
	}

	if (pom.getParent() != null)
	{
	  return getLicense(httpHelper, httpHelper.getPom(new MavenArtifact(pom.getParent().getGroupId(), pom.getParent()
		  .getArtifactId(), pom.getParent().getVersion())));
	}

	return null;
  }

  private static String dumpLicenses(List<License> licenses)
  {
	StringBuffer buffer = new StringBuffer();

	for (License license : licenses)
	{
	  buffer.append("\t");
	  buffer.append(license.getName());

	}

	return buffer.toString();
  }

  public static void main(String[] args)
  {

	CommandLineOptions cmdOptions = new CommandLineOptions();
	JCommander jCommander = new JCommander(cmdOptions);
	jCommander.setProgramName("java -jar legalbeagle.jar");
	
	try
	{
	  jCommander.parse(args);
	}
	catch (ParameterException e)
	{
	  jCommander.usage();
	  System.exit(1);
	}

	try
	{
	  // System.out.println(args[0]);

	  HttpHelper httpHelper = new HttpHelper();

	  // Model pom = httpHelper.getPom(new MavenArtifact("com.jolira", "guice",
	  // "3.0.0"));

	  FileCollector fc = new FileCollector();
	  List<File> files = fc.getJars(new File(cmdOptions.dir));

	  for (File f : files)
	  {
		String fileHash = DigestUtils.sha1Hex(new FileInputStream(f));

		try
		{

		  Model mod = httpHelper.getPom(fileHash);

		  if (mod != null)
		  {
			System.out.print(f.getAbsolutePath() + "\t" + fileHash + "\t" + mod.getName());

			List<License> licenses = getLicense(httpHelper, mod);
			if (licenses != null)
			{
			  System.out.println(dumpLicenses(licenses));
			}
			else
			{
			  System.out.println("\tNo licenses found");
			}

		  }
		  else
		  {
			System.out.println(f.getAbsolutePath() + "\t" + fileHash + "\tNot Identified");
		  }
		}
		catch (Exception e)
		{
		  System.out.println(f.getAbsolutePath() + "\t" + fileHash + "\tERR: " + e.getMessage());
		}

	  }
	}
	catch (Exception e)
	{
	  e.printStackTrace();
	}

  }
}
