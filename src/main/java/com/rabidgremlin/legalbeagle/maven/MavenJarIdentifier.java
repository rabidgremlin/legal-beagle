package com.rabidgremlin.legalbeagle.maven;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.legalbeagle.report.Report;
import com.rabidgremlin.legalbeagle.report.ReportItem;
import com.rabidgremlin.legalbeagle.report.ReportItemStatus;
import com.rabidgremlin.legalbeagle.util.HttpHelper;
import com.rabidgremlin.legalbeagle.xmlbindings.License;
import com.rabidgremlin.legalbeagle.xmlbindings.Model;
import com.rabidgremlin.legalbeagle.xmlbindings.Model.Licenses;

public class MavenJarIdentifier
{
  private final Logger log = LoggerFactory.getLogger(MavenJarIdentifier.class);
  private HttpHelper httpHelper;

  public MavenJarIdentifier(HttpHelper httpHelper)
  {
	this.httpHelper = httpHelper;
  }

  private List<License> getLicense(HttpHelper httpHelper, Model pom) throws Exception
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

  private String dumpLicenses(List<License> licenses)
  {
	StringBuffer buffer = new StringBuffer();

	for (License license : licenses)
	{
	  buffer.append("\t");
	  buffer.append(license.getName());

	}

	return buffer.toString();
  }

  public void identifyFiles(Report report) throws Exception
  {

	for (ReportItem reportItem : report.getReportItems())
	{
	  File f = reportItem.getFile();

	  String fileHash = DigestUtils.sha1Hex(new FileInputStream(f));

	  try
	  {
		log.info("Processing {}...",f.getAbsoluteFile());
		
		Model mod = httpHelper.getPom(fileHash);

		if (mod != null)
		{
		  //System.out.print(f.getAbsolutePath() + "\t" + fileHash + "\t" + mod.getName());
		  reportItem.setReportItemStatus(ReportItemStatus.IDENTIFIED);
		  reportItem.setDescription(mod.getName());
		  

		  List<License> licenses = getLicense(httpHelper, mod);
		  if (licenses != null)
		  {
			for (License license:licenses)
			{
			  reportItem.addLicense(license.getName());
			}
		  }
		  else
		  {
			 reportItem.setReportItemStatus(ReportItemStatus.NO_LICENSE_FOUND);
		  }

		}
		else
		{
		  reportItem.setReportItemStatus(ReportItemStatus.NOT_IDENTIFIED);
		}
	  }
	  catch (Exception e)
	  {
		reportItem.setReportItemStatus(ReportItemStatus.ERR);
		reportItem.setError(e.getMessage());
	  }

	}

  }

}
