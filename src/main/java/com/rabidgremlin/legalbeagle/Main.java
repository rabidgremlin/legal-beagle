package com.rabidgremlin.legalbeagle;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import com.rabidgremlin.legalbeagle.xmlbindings.Model;

//-Dhttp.proxyHost=10.0.0.100 -Dhttp.proxyPort=8800

public class Main
{
  public static void main(String[] args)
  {
	try
	{
	  //HttpURLConnection.setFollowRedirects(true);

	  JAXBContext jc = JAXBContext.newInstance("com.rabidgremlin.legalbeagle.xmlbindings");
	  Unmarshaller u = jc.createUnmarshaller();

	  //URL url = new URL("http://search.maven.org/remotecontent?filepath=com/jolira/guice/3.0.0/guice-3.0.0.pom");
	  
	  URL url = new URL("http://repo1.maven.org/maven2/org/eclipse/jetty/jetty-webapp/7.3.0.v20110203/jetty-webapp-7.3.0.v20110203.pom");

	  JAXBElement<Model> projectElem = (JAXBElement<Model>)u.unmarshal(url.openStream());

	  System.out.println(projectElem.toString());
	  System.out.println(projectElem.getName());
	  
	  Model project = projectElem.getValue();
	  System.out.println(project);
	  
	}
	catch (Exception e)
	{
	  e.printStackTrace();
	}

	/*
	 * try { FileCollector fc = new FileCollector(); List<File> files =
	 * fc.getJars(new File("C:/other/one-page-web-app-starter"));
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * for (File f : files) { String fileHash = DigestUtils.sha1Hex(new
	 * FileInputStream(f)); System.out.println("File Hash" + f + "  " +
	 * fileHash);
	 * 
	 * //
	 * http://search.maven.org/remotecontent?filepath=com/jolira/guice/3.0.0/guice
	 * -3.0.0.pom // http://search.maven.org/solrsearch/select?q=1:%222
	 * ceb567b8f3f21118ecdec129fe1271dbc09aa7a%22&rows=20&wt=json
	 * 
	 * URL url = new URL("http://search.maven.org/solrsearch/select?q=1:%22" +
	 * fileHash + "%22&rows=20&wt=json");
	 * 
	 * JsonNode root = mapper.readTree(url.openStream());
	 * 
	 * System.out.println(root.toString());
	 * 
	 * JsonNode docs = root.path("response").path("docs").get(0);
	 * 
	 * if (docs != null) { System.out.println(docs.toString()); } else {
	 * System.out.println("**** no match for: " + f.getAbsolutePath()); }
	 * 
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 */
  }
}
