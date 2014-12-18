package com.rabidgremlin.legalbeagle;

import com.beust.jcommander.Parameter;

public class CommandLineOptions
{
  @Parameter(names = {"--dir","-d"}, description = "The directory to scan. Will also scan all subdirectories.", required=true)
  public String dir;
  
  @Parameter(names = {"--output","-o"}, description = "Name of output file. Extension will determine type of output. Currently .tsv or .html are supported.",required=true)
  public String outputFile;
}
