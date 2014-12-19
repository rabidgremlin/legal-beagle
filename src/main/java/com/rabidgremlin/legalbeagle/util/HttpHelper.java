package com.rabidgremlin.legalbeagle.util;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabidgremlin.legalbeagle.maven.MavenArtifact;
import com.rabidgremlin.legalbeagle.xmlbindings.Model;

public class HttpHelper
{
  private Unmarshaller u;
  private HttpHost proxy;
  private ObjectMapper mapper = new ObjectMapper();

  public HttpHelper() throws Exception
  {
	JAXBContext jc = JAXBContext.newInstance("com.rabidgremlin.legalbeagle.xmlbindings");
	u = jc.createUnmarshaller();

	// -Dhttp.proxyHost=prox -Dhttp.proxyPort=1234
	// TODO: better error handling here
	if (System.getProperty("http.proxyHost") != null)
	{
	  proxy = new HttpHost(System.getProperty("http.proxyHost"), Integer.getInteger("http.proxyPort"));
	}

  }

  private JAXBElement<Model> unMarshallPom(String pom)
  {
	try
	{
	  return (JAXBElement<Model>) u.unmarshal(new StringReader(pom));
	}
	catch (javax.xml.bind.UnmarshalException e)
	{
	  //System.out.println(e);
	  // return null;

	  if (e.getMessage().startsWith("unexpected element (uri:\"\", local:\"project\")"))
	  {
		try
		{
		  pom = pom.replaceFirst("<project>", "<project xmlns=\"http://maven.apache.org/POM/4.0.0\">");
		  
		  //System.out.println("***POM is " + pom);
		  
		  return (JAXBElement<Model>) u.unmarshal(new StringReader(pom));
		}
		catch (Exception ex)
		{
		  e.printStackTrace();
		  return null;
		}
	  }
	  else
	  {
		e.printStackTrace();
		return null;
	  }

	}
	catch (JAXBException e)
	{
	  return null;
	}

  }

  public Model getPom(MavenArtifact mavenArtifact) throws Exception
  {
	String requestUrl = "http://search.maven.org/remotecontent?filepath=" + mavenArtifact.getGroupId().replaceAll("\\.", "/") + "/"
		+ mavenArtifact.getArtifactId() + "/" + mavenArtifact.getVersion() + "/" + mavenArtifact.getArtifactId() + "-"
		+ mavenArtifact.getVersion() + ".pom";

	// System.out.println("url:" + requestUrl);

	// URL url = new
	// URL("http://search.maven.org/remotecontent?filepath=com/jolira/guice/3.0.0/guice-3.0.0.pom");

	// URL url = new
	// URL("http://repo1.maven.org/maven2/org/eclipse/jetty/jetty-webapp/7.3.0.v20110203/jetty-webapp-7.3.0.v20110203.pom");

	JAXBElement<Model> projectElem = unMarshallPom(executeGet(requestUrl).returnContent().asString());

	// System.out.println(projectElem.toString());
	// System.out.println(projectElem.getName());

	return projectElem.getValue();
  }

  public Model getPom(String sha1Hash) throws Exception
  {

	String requestUrl = "http://search.maven.org/solrsearch/select?q=1:%22" + sha1Hash + "%22&rows=20&wt=json";

	JsonNode root = mapper.readTree(executeGet(requestUrl).returnContent().asStream());

	// System.out.println(root.toString());

	JsonNode doc = root.path("response").path("docs").get(0);

	if (doc == null)
	{
	  return null;
	}

	return getPom(new MavenArtifact(doc.path("g").asText(), doc.path("a").asText(), doc.path("v").asText()));
  }

  private Response executeGet(String requestUrl) throws ClientProtocolException, IOException
  {
	if (proxy != null)
	{
	  return Request.Get(requestUrl).viaProxy(proxy).execute();
	}
	else
	{
	  return Request.Get(requestUrl).execute();
	}
  }
}
