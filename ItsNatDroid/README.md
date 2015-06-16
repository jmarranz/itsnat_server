News
======


ItsNat Droid Client SDK
======

ItsNat Droid Client SDK is a Java based Android development library to develop dynamic Android applications on demand downloading native Android UI layouts similar to the web paradigm.

What is ItsNat Droid?
------

ItsNat Droid makes possible to use Android following the web paradigm but using only native Android APIs and UIs.

ItsNat Droid was born to compete with PhoneGap providing similar approaches but using native Android layouts/resources and Beanshell as scripting language instead of clumsy
HTML/CSS/JavaScript trying to simulate native components and behavior.

It tries to break the "Amazon shop app dilemma": fully exploit of Android native UI capabilities vs UI flexible maintenance in server without app upgrade (just to change a color)
using web tech.

http://www.theserverside.com/news/2240174316/How-Amazon-discovered-hybrid-HTML5-Java-Android-app-development

*"the most compelling reason to incorporate HTML5 into your mobile applications is to get the ability to update the app without requiring an upgrade on the device user's side.
This capability makes it both easier and safer to manage apps -- permitting developers to roll out or draw back updates as needed. In the brave new world of continuous deployment
and live testing, that's a huge advantage"*

ItsNat Droid breaks the "impossibility" of loading native Android resources (layouts, drawables, animations...) dynamically:

http://stackoverflow.com/questions/6575965/theoretical-question-load-external-xml-layout-file-in-android
http://stackoverflow.com/questions/18641798/looking-for-xmlresourceparser-implementation


Beanshell, "scripting Java < 5" with some extensions (like using var as in JavaScript),  is used for scripting, because it is a JVM based language has a direct and tight
integration with Android native APIs without an explicit API provided by the framework, that is, through Beanshell scripts you can call any Java code of your application as it
was normal Java native code, and the reverse is true, you can access Beanshell objects and methods from Java native code. By this way you can decide how much native Java code
"built-in" functionality is in your application to get the max speed and how much is in Beanshell (slower) without a pre-build rigid bridge necessary in Android WebView
(in a WebView the Java API able to be called by JavaScript must be previously declared through Java interfaces method to method) used in HTML/CSS/JavaScript mobile solutions.

Interoperatibility levels
------

In spite of ItsNat Droid Client contains the name "ItsNat", it can be used outside ItsNat server.

ItsNat Droid client has several levels of interoperatibility:

1. Parsing a XML file containing an Android layout, this XML can be read from any source. Dependent native resources are loaded when specified in layout.

2. Built-in "page" system similar to the web paradigm of page, instead an HTML page you download an Android layout in XML form. By using a simple API you can download remote native
 Android layouts from ANY web server and be able of doing Single Page Interface with Android layouts.

   Take a look to this very simple and raw example of a SPI application web server agnostic.

   As you can see &lt;script&gt; elements containing Beanshell scripts are an extension to Android layouts. There are some other extensions like using the "id" standard attribute as
alternative to android:id (valid also)

3. Total integration with ItsNat server.

  Yes, Android layout is a XML format so can be managed as DOM by ItsNat server. Everything is said, you can programming server centric stateful or stateless Android applications
  the same way you're used with ItsNat Web. This is not totally new for you because ItsNat Web already supports raw XML, XUL and SVG layouts including events. The main difference
  is you send Beanshell code instead JavaScript in your <script> elements and addCodeToSend() APIs, and Android client events may be sent to server converted to DOM events
  (you decide what events are processed in server side).

  For instance (layout and layout fragment) in a web server:

  https://github.com/jmarranz/itsnat/blob/development/itsnat_dev/web/WEB-INF/pages/droid/test/test_droid_core.xml
  https://github.com/jmarranz/itsnat/blob/development/itsnat_dev/web/WEB-INF/pages/droid/test/test_droid_core_fragment.xml

  Some server side code snippets manipulating layouts:

  https://github.com/jmarranz/itsnat/blob/development/itsnat_dev/src/java/test/droid/core/TestDroidFragmentInsertionInnerXML.java
  https://github.com/jmarranz/itsnat/blob/development/itsnat_dev/src/java/test/droid/core/TestDroidFragmentInsertionUsingAPI.java
  https://github.com/jmarranz/itsnat/blob/development/itsnat_dev/src/java/test/droid/core/TestDroidToDOM.java

  Do you remember the powerful Remote Control capability of ItsNat Web? Yes you can do the same with Android, another Android device can be monitor an Android layout of another
  device (with permission of course), most of non-web stuff of ItsNat server is supported.

  There is no "ItsNat modes" (web/Android) in ItsNat server, Android layouts can coexist in the same web application with Web layouts, the main difference is when an Android layout
  (registered with MIME android/layout) is requested  ItsNat manages this type with some differences regarding to web layouts (all of them using JavaScript).


On development
------

ItsNat Droid is on heavy development, there is no release in spite you can download the source code and play (master branch)

https://github.com/jmarranz/itsnat/tree/master/

Development happens on development branch, this is the branch to get the latest:

https://github.com/jmarranz/itsnat/tree/development/

Android client code is Java 6 compatible and supports Android +4.0.3 devices, developed with Android Studio and lives here:

https://github.com/jmarranz/itsnat/tree/master/ItsNatDroid

In development branch:

https://github.com/jmarranz/itsnat/tree/development/ItsNatDroid


You can play with the "apptest" Android application with many visual tests (also there're a lot of tests in code), no you're not going to find here something pretty, just
systematic testing. You must configure the URL of your ItsNat server (when loading this app click the Back button of your device for URL configuration, save your URL and then
click on "GO TO TESTS" button). Some examples like "TEST LOCAL X" are local and can run without ItsNat server, the key to understand these tests is by clicking "RELOAD" button
you get the same layout but processed dynamically.

All said before is already done and running, but some ambitious new things are pending like be able of dynamically load dependent resources like drawables and animations located
on server (some drawable is already working) and higher level components attached to DOM elements for easier management like in ItsNat Web.

License of ItsNat Droid Client (ItsNat server is LGPL v3)
------

[Apache v2](LICENSE-2.0.txt)

Nomenclature
------

Because there is a new kid on the block, new names arise to clarify:

- ItsNat Server: when referring only to the server part of ItsNat.
- ItsNat Droid: when referring to client and Droid part of ItsNat server for Android programming
- ItsNat Droid Client: when referring only to the Android library for development Android applications based on ItsNat
- ItsNat Web: when referring only to web (raw XML/HTML/SVG/XUL + JavaScript) development, not Android.

Specific Google Group
------

For ItsNat Droid only stuff, a new Goggle Group has been created:

https://groups.google.com/forum/#!forum/itsnat-droid

For Droid only discussions use this specific group.





























ItsNat : Natural AJAX. Component Based Java Web Application Framework

Project web site: http://www.itsnat.org

Full interactive demo of features here: [ItsNat Feature Showcase](http://www.innowhere.com/itsnat/feashow_servlet?itsnat_doc_name=feashow.main) (inline documentation and sample code)

Core features

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

### Hello World

1. Create a new Java web project with your preferred IDE, the name is not important we will use "itsnat" .
2. Copy the following jars to the WEB-INF/lib (these jars are located in fw_dist/lib in the ItsNat distribution): ItsNat.jar, batik-dom.jar, batik-util.jar, batik-xml.jar, nekohtml.jar, serializer.jar, xercesImpl.jar, xml-apis.jar
3. Create a new servlet using the wizard of your IDE. In this example it is named "servlet", but this name is not mandatory. The default web.xml is valid as is. Remove any code and add the following:

```
import javax.servlet.*;
import org.itsnat.core.DocumentTemplate;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;

public class servlet extends HttpServletWrapper
{
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        String pathPrefix = getServletContext().getRealPath("/");
        pathPrefix += "WEB-INF/pages/manual/";
       
        DocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerDocumentTemplate("manual.core.example","text/html",pathPrefix + "core_example.xhtml");
        docTemplate.addItsNatServletRequestListener(new CoreExampleLoadListener());

    }

}

```


The HttpServletWrapper class is an ItsNat utility class, it only forwards a normal request to ItsNat.
The standard servlet method "init" is used to configure ItsNat and to add an ItsNat template (core_example.xhtml) registered with the name "manual.core.example" and the class which process this template, CoreExampleLoadListener. A template is used to construct the final served page (ItsNat supports templates as page fragments too).
Create a new XHTML file with name "core_example.xhtml" in a new folder "WEB-INF/pages/manual/", this folder name and location is not mandatory. This file is a ItsNat template, as you can see is pure XHTML (HTML is supported too):

```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

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
    public CoreExampleLoadListener() {}

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();
        new CoreExampleProcessor(itsNatDoc);
    }

}
```

The method processRequest (implementing the interface ItsNatServletRequestListener) is called when a user loads a new page with a URL:

```
http://<host>:<port>/<yourapp>/servlet?itsnat_doc_name=manual.core.example
```


ItsNat creates automatically an ItsNatHTMLDocument object when a new page is loaded, this object wraps the org.w3c.dom.Document object build using the specified page template (manual.core.example), this object is got calling ItsNatHTMLDocument.getDocument(). This DOM document represents the client page, any change performed to the DOM tree will be propagated to the client.
The CoreExampleLoadListener delegates to a new CoreExampleProcessor instance:

```
package org.itsnat.manual.coreimport org.itsnat.core.html.ItsNatHTMLDocument;

import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class CoreExampleProcessor implements EventListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element clickElem1;
    protected Element clickElem2;

    public CoreExampleProcessor(ItsNatHTMLDocument itsNatDoc)
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
  	itsNatDoc.addEventListener((EventTarget)clickElem1,"click",this,false);
    }

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
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element noteElem = doc.createElement("p");
        noteElem.appendChild(doc.createTextNode("Clicked " + ((Element)currTarget).getAttribute("id")));
        doc.getBody().appendChild(noteElem);
    }

    public void setAsClickable(Element elem)
    {
        elem.setAttribute("style","color:red;");
        Text text = (Text)elem.getFirstChild();
        text.setData("Click Me!"); 
	itsNatDoc.addEventListener((EventTarget)elem,"click",this,false);
    }

    public void removeClickable(Element elem)
    {
        elem.removeAttribute("style");
        Text text = (Text)elem.getFirstChild();
        text.setData("Cannot be clicked"); 
	itsNatDoc.removeEventListener((EventTarget)elem,"click",this,false);
    }

}
```


As you can see, the page view is changed using standard W3C DOM methods at load time, when an event is received. To listen to events, the method ItsNatHTMLDocument.addEventListener is called, this method is very similar to org.w3c.dom.events.EventTarget.addEventListener:

```
itsNatDoc.addEventListener((EventTarget)clickElem1,"click",this,false)
```

This method adds the CoreExampleProcessor instance as a listener, because this class implements org.w3c.dom.events.EventListener, ready to receive mouse click events. When the user clicks the specified element in the client an event is sent to the server using AJAX techniques and converted to a W3C DOM MouseEvent, and the method handleEvent(Event) is called, any change is transparently propagated to the client as the event result.
The method ItsNatHTMLDocument.removeEventListener is used to unregister a listener, again mimics org.w3c.dom.events.EventTarget.removeEventListener.
Now run the application. The following image shows the client page state after the user clicks the first element:



Source: http://itsnat.sourceforge.net/index.php?_page=support.tutorial.core
