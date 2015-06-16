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
  is you send Beanshell code instead JavaScript in your &lt;script&gt; elements and addCodeToSend() APIs, and Android client events may be sent to server converted to DOM events
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

