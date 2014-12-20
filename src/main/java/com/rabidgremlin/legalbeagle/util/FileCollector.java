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
package com.rabidgremlin.legalbeagle.util;

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
