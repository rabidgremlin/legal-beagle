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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ReportItem
{
  private File file;
  private ReportItemStatus reportItemStatus;
  private String description;
  private ArrayList<String> licenses = new ArrayList<String>();
  private String error;

  public ReportItem(File file)
  {
	this.file = file;
	reportItemStatus = ReportItemStatus.NOT_IDENTIFIED;
  }

  public ReportItemStatus getReportItemStatus()
  {
	return reportItemStatus;
  }

  public void setReportItemStatus(ReportItemStatus reportItemStatus)
  {
	this.reportItemStatus = reportItemStatus;
  }

  public String getDescription()
  {
	return description;
  }

  public void setDescription(String description)
  {
	this.description = description;
  }

  public File getFile()
  {
	return file;
  }

  public List<String> getLicenses()
  {
	return Collections.unmodifiableList(licenses);
  }

  public void addLicense(String license)
  {	
	licenses.add(license);
  }

  public String getError()
  {
	return error;
  }

  public void setError(String error)
  {
	this.error = error;
  }

  @Override
  public String toString()
  {
	return "ReportItem [file=" + file + ", reportItemStatus=" + reportItemStatus + ", description=" + description + ", licenses="
		+ licenses + ", error=" + error + "]";
  }

}
