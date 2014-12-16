package com.rabidgremlin.legalbeagle;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class Main
{
  public static void main(String[] args)
  {
	try
	{
	  FileCollector fc = new FileCollector();
	  List<File> files = fc.getJars(new File("C:/other/one-page-web-app-starter"));
	  
	  for (File f: files)
	  {
		System.out.println(f.getAbsolutePath());
	  }
	  
	}
	catch (Exception e)
	{
	  e.printStackTrace();
	}
  }
}
