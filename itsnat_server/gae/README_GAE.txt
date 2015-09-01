
Google App Engine does not support org.w3c.dom.xpath.* classes (org.w3c.* content
is controlled by GAE and org.w3c.dom.xpath.* classes are not white listed).
W3C DOM XPath classes can be used in Batik DOM included in ItsNat,
in spite of ItsNat does not use these classes (and they are not loaded), GAE accidentally
tries to load some of them when serializing any document DOM when the session
is serialized, this causes an exception.

http://code.google.com/p/googleappengine/issues/detail?id=1370

To avoid this annoying problem, the class org.apache.batik.dom.AbstractDocument (the "offending" class)
has been modified removing all problematic XPath dependencies.

Use the modified jar file lib/batik-dom-1.7-gae.jar with the new AbstractDocument class instead of batik-dom.jar or batik-dom-1.7.jar in GAE.
