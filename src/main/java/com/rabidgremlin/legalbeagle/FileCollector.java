package com.rabidgremlin.legalbeagle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;

public class FileCollector extends DirectoryWalker<File>
{

  public FileCollector()
  {
	super();
  }

  public List<File> getJars(File startDirectory) throws Exception
  {
	List<File> results = new ArrayList<File>();
	walk(startDirectory, results);
	return results;
  }

  @Override
  protected void handleFile(File file, int depth, Collection<File> results) throws IOException
  {
	if (file.getAbsolutePath().endsWith(".jar"))
	{
	  results.add(file);
	}
  }

}
