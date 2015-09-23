News
======

- Sept. 18 2015. ItsNat Server 1.4 released! First version in JCenter/Maven. [Release Notes](http://www.itsnat.org/static/download/RELEASE_NOTES.html)
- Nov.  16 2014. ItsNat Droid is a new project inside ItsNat, [more info](https://github.com/jmarranz/itsnat_droid).


ItsNat
======

ItsNat : Natural AJAX. Component Based Java Web Application Framework

Project web site: http://www.itsnat.org

Full interactive demo of features here: [ItsNat Feature Showcase](http://www.innowhere.com/itsnat_featshow/) (inline documentation and sample code)

Download Binaries and Docs
------

[Download](https://sourceforge.net/projects/itsnat/files/)

Distribution file includes binaries, examples, manual and javadocs.

Artefacts are uploaded to [JCenter](https://bintray.com/jmarranz/maven/itsnat_server/view) ([direct](https://jcenter.bintray.com/org/itsnat/itsnat_server/)) and [Maven Central](http://mvnrepository.com/artifact/org.itsnat/itsnat_server) ([direct](https://oss.sonatype.org/content/repositories/releases/org/itsnat/itsnat_server/)) repositories

Maven: 

```xml
<groupId>org.itsnat</groupId>
<artifactId>itsnat_server</artifactId>
<version>(version)</version>
<type>jar</type>
```


Core features
------

ItsNat provides many more (core) features:

- DHTML on the server. Automatic client synchronization from server

- Some support of automatic server synchronization from client

- Web-continuations ("continue" events)

- User defined event types

- AJAX Timers

- Long running asynchronous server tasks (client is notified when finished)

- COMET without special application servers

- Server side DOM utils (to simplify DOM manipulation like lists, trees, ElementCSSInlineStyle support etc)

- Resolution of ${} based variables in markup (helps to keep Java DOM code as agnostic as possible of the concrete layout)

- Markup fragments (dynamic parts of the page to be inserted in any time very useful in Single Page Interface)

- DOM subtrees in server not going to be used anymore can be removed only in server saving memory (disconnected child nodes)

- SVG (and other namespaces) embedded inline on XHTML and application/xhtml+xml MIME

- SVG (and other namespaces) embedded inline on X/HTML and text/html MIME on

>- Browsers with native SVG

>- MSIE with Adobe SVG Viewer (v3.0 or v6.0 beta) plugin including dynamic processing of SVG DOM

>- Any browser with Flash support using SVGWeb

- Pure SVG documents including AJAX in browsers with native SVG or MSIE with Adobe SVG

- Viewer or Renesis Player v1.1 or Savarese Ssrc SVG/XUL plugin.

- Pure XUL documents including AJAX in Gecko browsers (like FireFox) or MSIE with

- Savarese Ssrc SVG/XUL plugin.

- XML generation

- IFrame/Object/Embed/Applet Auto-Binding: in server child documents opened by IFRAME, OBJECT, EMBED or APPLET tags are automatically bound to the parent document in server . This feature works with:

>- X/HTML loaded by an IFRAME

>- SVG loaded by an IFRAME/OBJECT/EMBED in browsers with native SVG or MSIE with Adobe SVG Viewer or Renesis   or Savarese Ssrc SVG/XUL (only OBJECT and EMBED)

>- SVG loaded by Batik applet (custom version for ItsNat) in an APPLET/OBJECT/EMBED

- Java to JavaScript generation utilities

- Server-sent events (events fired by the server sent to the client simulating user actions) with the real browser   or simulated   for instance to test the client view simulating user actions   or simulating the client in server

- Referrers: in page navigation, the previous document in server can be obtained to copy any data avoiding session data in page based applications. Navigation includes back/forward/reload support  . Two modes: push  and pull

- Degraded modes: Events disabled   and JavaScript disabled   modes

- Extreme Mashups

- AJAX bookmarking (or bookmarking in Single Page Interface applications)

- Pretty URLs

- Automatic page remote/view control of other users/sessions!!

- Remote Templates

As of v1.3 ItsNat provides a new stateless mode avoiding any use of session and no need of server affinity.

Repository of Open Source Examples
------

Take a look here:

https://github.com/jmarranz/itsnat_server_examples_web


Hello World
------

1. Create a new Java web project with your preferred IDE, the name is not important we will use "itsnat". The default web.xml is valid as is.
2. Use Maven (seen later) or copy the jars located in /itsnat_featshow/target/itsnat_featshow-1.0-SNAPSHOT/WEB-INF/lib in the ItsNat distribution to the WEB-INF/lib folder of your project.


POM example if you use Maven (ItsNat 1.4):

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.itsnat</groupId>
  <artifactId>itsnat_example</artifactId>
  <packaging>war</packaging>
  <version>1.0-SNAPSHOT</version>

  <name>itsnat_example</name>
  <url>http://www.itsnat.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

  <dependencies>
      
        <dependency>
            <groupId>org.itsnat</groupId>
            <artifactId>itsnat_server</artifactId>
            <version>1.4</version>
        </dependency>

        <dependency>
            <groupId>com.innowhere</groupId>
            <artifactId>relproxy</artifactId>
            <version>0.8.6</version>
            <type>jar</type>
        </dependency>

        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-dom</artifactId>
            <version>1.7</version>
        </dependency>

        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-xml</artifactId>
            <version>1.7</version>
        </dependency>

        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-util</artifactId>
            <version>1.7</version>
        </dependency>

        <dependency>
            <groupId>net.sourceforge.nekohtml</groupId>
            <artifactId>nekohtml</artifactId>
            <version>1.9.12</version>
        </dependency>

        <dependency>
            <groupId>xalan</groupId>
            <artifactId>serializer</artifactId>
            <version>2.7.1</version>
        </dependency>
      
      
      
        <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>servlet-api</artifactId>
          <version>2.5</version>
          <scope>provided</scope>
        </dependency>
    
        <dependency>
          <groupId>javax.servlet.jsp</groupId>
          <artifactId>jsp-api</artifactId>
          <version>2.1</version>
          <scope>provided</scope>
        </dependency>
    
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>2.0.2</version>
        <configuration>
            <source>1.6</source>
            <target>1.6</target>
            <encoding>${project.build.sourceEncoding}</encoding>
        </configuration>
      </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-resources-plugin</artifactId>
            <version>2.4.3</version>
            <configuration>
                <encoding>${project.build.sourceEncoding}</encoding>
            </configuration>
        </plugin>
    </plugins>
  </build>

</project>

```

3. Create a new servlet using the wizard of your IDE. In this example it is named "servlet" in "org.itsnat.manual.core" package, but this name is not mandatory. Remove any code and add the following:

```java
package org.itsnat.manual.core;

import javax.servlet.*;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;

public class servlet extends HttpServletWrapper
{
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();
        ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();

        String pathPrefix = getServletContext().getRealPath("/");
        pathPrefix += "/WEB-INF/pages/manual/";

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.core.example","text/html",
                    pathPrefix + "core_example.html");
        docTemplate.addItsNatServletRequestListener(new CoreExampleLoadListener());

    }

}

```


The HttpServletWrapper class is an ItsNat utility class, it only forwards a normal request to ItsNat.
The standard servlet method "init" is used to configure ItsNat and to add an ItsNat template (core_example.xhtml) registered with the name "manual.core.example" and the class which process this template, CoreExampleLoadListener. A template is used to construct the final served page (ItsNat supports templates as page fragments too).
Create a new XHTML file with name "core_example.xhtml" in a new folder "WEB-INF/pages/manual/", this folder name and location is not mandatory. This file is a ItsNat template, as you can see is pure XHTML (HTML is supported too):

```
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">

    <head>
        <title>ItsNat Core Example</title>
    </head>

    <body>

        <h3>ItsNat Core Example</h3>
       
        <div itsnat:nocache="true" xmlns:itsnat="http://itsnat.org/itsnat">
            <div id="clickableId1">Clickable Elem 1</div>
            <div id="clickableId2">Clickable Elem 2</div>
        </div>

    </body>

</html>
```

Add a new Java class with name CoreExampleLoadListener (the package/location is not important and the class name is only an example) and the following code:

```
package org.itsnat.manual.core;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;

public class CoreExampleLoadListener implements ItsNatServletRequestListener
{
    public CoreExampleLoadListener()
    {
    }

    @Override
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();
        new CoreExampleDocument(itsNatDoc);
    }
}
```

The method processRequest (implementing the interface ItsNatServletRequestListener) is called when a user loads a new page with a URL:

```
http://<host>:<port>/<yourapp>/servlet?itsnat_doc_name=manual.core.example
```


ItsNat creates automatically an ItsNatHTMLDocument object when a new page is loaded, this object wraps the org.w3c.dom.Document object build using the specified page template (manual.core.example), this object is got calling ItsNatHTMLDocument.getDocument(). This DOM document represents the client page, any change performed to the DOM tree will be propagated to the client.
The CoreExampleLoadListener delegates to a new CoreExampleDocument (concrete class name is not impossed) instance:

```java
package org.itsnat.manual.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class CoreExampleDocument implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element clickElem1;
    protected Element clickElem2;

    public CoreExampleDocument(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        this.clickElem1 = doc.getElementById("clickableId1");
        this.clickElem2 = doc.getElementById("clickableId2");

        clickElem1.setAttribute("style","color:red;");
        Text text1 = (Text)clickElem1.getFirstChild();
        text1.setData("Click Me!");

        Text text2 = (Text)clickElem2.getFirstChild();
        text2.setData("Cannot be clicked");

        Element noteElem = doc.createElement("p");
        noteElem.appendChild(doc.createTextNode("Ready to receive clicks..."));
        doc.getBody().appendChild(noteElem);

        ((EventTarget)clickElem1).addEventListener("click",this,false);
    }

    @Override
    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == clickElem1)
        {
            removeClickable(clickElem1);
            setAsClickable(clickElem2);
        }
        else
        {
            setAsClickable(clickElem1);
            removeClickable(clickElem2);
        }

        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        ItsNatServletRequest itsNatReq = itsNatEvt.getItsNatServletRequest();
        ItsNatDocument itsNatDoc = itsNatReq.getItsNatDocument();

        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element noteElem = doc.createElement("p");
        noteElem.appendChild(
                doc.createTextNode("Clicked " + ((Element)currTarget).getAttribute("id")));
        doc.getBody().appendChild(noteElem);
    }

    public void setAsClickable(Element elem)
    {
        elem.setAttribute("style","color:red;");
        Text text = (Text)elem.getFirstChild();
        text.setData("Click Me!");
        ((EventTarget)elem).addEventListener("click",this,false);
    }

    public void removeClickable(Element elem)
    {
        elem.removeAttribute("style");
        Text text = (Text)elem.getFirstChild();
        text.setData("Cannot be clicked");
        ((EventTarget)elem).removeEventListener("click",this,false);
    }
}

```


As you can see, the page view is changed using standard W3C DOM methods at load time, when an event is received. To listen to events, the method EventTarget.addEventListener(EventListener) is called:

```java
    ((EventTarget)elem).addEventListener("click",this,false);
```

This method adds the CoreExampleDocument instance as a listener of DOM events sent by client, because this class implements org.w3c.dom.events.EventListener, ready to receive mouse click events in this case. 
When the user clicks the specified element in the client, an event is sent to the server using AJAX techniques and converted to a W3C DOM MouseEvent, and the method
handleEvent(Event) is called, any change is transparently propagated to the client as the event result.
The method EventTarget.removeEventListener is used to unregister a listener.

The following code block can be removed, is just to show (for "educative" reasons) how the Event object received is implemented by ItsNat, the returned ItsNatDocument is the same as the
attribute itsNatDoc of the CoreExampleDocument:

```java
    ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
    ItsNatServletRequest itsNatReq = itsNatEvt.getItsNatServletRequest();
    ItsNatDocument itsNatDoc = itsNatReq.getItsNatDocument();
```

Now run the application, you are going to see a link in red with the text "Click Me!" and the other link with text "Cannot be clicked" in black, clicking in the
clickable link the roles change and a new log sentence is added to the end.


This tutorial is also here: http://www.itsnat.org/support-tutorial-core

