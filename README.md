# Legal Beagle


[![Build Status](https://travis-ci.org/rabidgremlin/legal-beagle.svg?branch=master)](https://travis-ci.org/rabidgremlin/legal-beagle)

Tool to check licenses of components used in software. Currently identifies licenses for .jar files using file hashes and the Maven Central Repository ( http://search.maven.org/ )

## Usage


```
Usage: java -jar legalbeagle.jar [options]
  Options:
  * --dir, -d
       The directory to scan. Will also scan all subdirectories.
  * --output, -o
       Name of output file. Extension will determine type of output. Currently
       only .tsv is supported.
```

Currently outputs reports to tab-separated file (.tsv).	   

```
java -jar legalbeagle.jar -d c:\play\one-page-web-app-starter -o report.tsv
```

Generates output looking as follows:

```
File	Status	Description	License(s)	Error
C:\play\one-page-web-app-starter\build\ivy-2.4.0-rc1.jar	IDENTIFIED	Apache Ivy	The Apache Software License, Version 2.0	
C:\play\one-page-web-app-starter\build\lib\packtag-3.10.jar	NOT_IDENTIFIED			
C:\play\one-page-web-app-starter\lib\hamcrest-core-1.3.jar	IDENTIFIED	Hamcrest Core	New BSD License	
C:\play\one-page-web-app-starter\lib\junit-4.11.jar	IDENTIFIED	JUnit	Common Public License Version 1.0	
C:\play\one-page-web-app-starter\lib\servlet-api-2.5.jar	NO_LICENSE_FOUND			
C:\play\one-page-web-app-starter\src\www\WEB-INF\lib\antlr-2.7.7.jar	IDENTIFIED	AntLR Parser Generator	BSD License	
C:\play\one-page-web-app-starter\src\www\WEB-INF\lib\aopalliance-1.0.jar	IDENTIFIED	AOP alliance	Public Domain	
C:\play\one-page-web-app-starter\src\www\WEB-INF\lib\asm-3.3.1.jar	IDENTIFIED	ASM Core	BSD	
C:\play\one-page-web-app-starter\src\www\WEB-INF\lib\bcprov-jdk15-1.46.jar	IDENTIFIED	Bouncy Castle Provider	Bouncy Castle Licence	
C:\play\one-page-web-app-starter\src\www\WEB-INF\lib\cglib-2.2.1-v20090111.jar	IDENTIFIED	CGLIB	The Apache Software License, Version 2.0	
C:\play\one-page-web-app-starter\src\www\WEB-INF\lib\commons-beanutils-1.9.1.jar	IDENTIFIED	Apache Commons BeanUtils	The Apache Software License, Version 2.0	
```

## Releases
Latest release can be downloaded from https://github.com/rabidgremlin/legal-beagle/releases