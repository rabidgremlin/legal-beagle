package com.rabidgremlin.legalbeagle.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
