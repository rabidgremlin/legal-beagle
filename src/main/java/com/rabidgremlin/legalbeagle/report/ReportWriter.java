package com.rabidgremlin.legalbeagle.report;

public interface ReportWriter
{
   public void generateReport(String output, Report report) throws Exception;
}
