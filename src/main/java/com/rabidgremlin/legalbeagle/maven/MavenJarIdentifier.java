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
package com.rabidgremlin.legalbeagle.maven;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
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

  public void identifyFiles(Report report) throws Exception
  {

	for (ReportItem reportItem : report.getReportItems())
	{
	  File f = reportItem.getFile();

	  String fileHash = DigestUtils.sha1Hex(new FileInputStream(f));

	  try
	  {
		log.info("Processing {}...", f.getAbsoluteFile());

		Model mod = httpHelper.getPom(fileHash);

		if (mod != null)
		{
		  reportItem.setReportItemStatus(ReportItemStatus.IDENTIFIED);
		  reportItem.setDescription(mod.getName());

		  List<License> licenses = getLicense(httpHelper, mod);
		  if (licenses != null)
		  {
			for (License license : licenses)
			{
			  // some names have spaces and CR or LF in them
			  String licenseStr = license.getName().trim();
			  licenseStr = StringUtils.strip(licenseStr, "\n\r");

			  reportItem.addLicense(licenseStr);
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
