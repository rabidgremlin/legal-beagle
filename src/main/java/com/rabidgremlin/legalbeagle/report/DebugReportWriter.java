package com.rabidgremlin.legalbeagle.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugReportWriter implements ReportWriter
{
  private final Logger log = LoggerFactory.getLogger(DebugReportWriter.class);

  public void generateReport(String output, Report report) throws Exception
  {
     for (ReportItem reportItem:report.getReportItems())
     {
       log.info("{}",reportItem);
     }
  }

}
