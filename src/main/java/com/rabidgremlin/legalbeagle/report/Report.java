package com.rabidgremlin.legalbeagle.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Report
{
  private ArrayList<ReportItem> reportItems = new ArrayList<ReportItem>();

  public List<ReportItem> getReportItems()
  {
	return Collections.unmodifiableList(reportItems);
  }

  public void addFiles(List<File> files)
  {
	for (File file : files)
	{
	  reportItems.add(new ReportItem(file));
	}
  }
}
