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
package com.rabidgremlin.legalbeagle.report;

import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsvReportWriter implements ReportWriter
{
  private final Logger log = LoggerFactory.getLogger(TsvReportWriter.class);

  public void generateReport(String output, Report report) throws Exception
  {
	log.info("Writing report to {}...", output);

	PrintWriter out = new PrintWriter(new File(output));
	
	out.println("File\tStatus\tDescription\tLicense(s)\tError");

	for (ReportItem reportItem : report.getReportItems())
	{
	  out.print(reportItem.getFile().getAbsolutePath());
	  out.print("\t");
	  out.print(reportItem.getReportItemStatus());
	  out.print("\t");	  

	  if (reportItem.getDescription() != null)
	  {
		out.print(reportItem.getDescription());
	  }
	  out.print("\t");

	  out.print(StringUtils.join(reportItem.getLicenses(), "; "));
	  out.print("\t");
	  
	  if (reportItem.getError() != null)
	  {
		out.print(reportItem.getError());
	  }
	  
	  
	  out.println();

	}

	out.flush();
	out.close();
  }

}
