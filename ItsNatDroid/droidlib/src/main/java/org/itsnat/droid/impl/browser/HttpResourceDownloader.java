package org.itsnat.droid.impl.browser;

import android.content.res.AssetManager;

import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMAttrRemote;
import org.itsnat.droid.impl.dom.XMLDOM;
import org.itsnat.droid.impl.domparser.XMLDOMParser;
import org.itsnat.droid.impl.util.MimeUtil;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 12/11/14.
 */
public class HttpResourceDownloader
{
    protected final String pageURLBase;
    protected final HttpRequestData httpRequestData;
    protected final XMLInflateRegistry xmlInflateRegistry;
    protected final AssetManager assetManager;

    public HttpResourceDownloader(String pageURLBase,HttpRequestData httpRequestData, XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        this.pageURLBase = pageURLBase;
        this.httpRequestData = httpRequestData;
        this.xmlInflateRegistry = xmlInflateRegistry;
        this.assetManager = assetManager;
    }

    public void downloadResources(List<DOMAttrRemote> attrRemoteList) throws Exception
    {
        downloadResources(attrRemoteList,null);
    }

    public void downloadResources(List<DOMAttrRemote> attrRemoteList,List<HttpRequestResultImpl> resultList) throws Exception
    {
        downloadResources(pageURLBase,attrRemoteList,resultList);
    }

    private void downloadResources(String urlBase,List<DOMAttrRemote> attrRemoteList,List<HttpRequestResultImpl> resultList) throws Exception
    {
        int len = attrRemoteList.size();
        final Thread[] threadArray = new Thread[len];
        final Exception[] exList = new Exception[len];

        {
            int i = 0;
            final boolean[] stop = new boolean[1];
            for (DOMAttrRemote attr : attrRemoteList)
            {
                Thread thread = downloadResource(urlBase,attr, stop, i,resultList, exList);
                threadArray[i] = thread;
                i++;
            }
        }

        {
            for (int i = 0; i < threadArray.length; i++)
            {
                threadArray[i].join();
            }

            for (int i = 0; i < exList.length; i++)
            {
                if (exList[i] != null)
                    throw exList[i]; // La primera que encontremos (es raro que haya más de una)
            }
        }
    }

    private Thread downloadResource(final String urlBase,final DOMAttrRemote attr, final boolean[] stop, final int i,
                                    final List<HttpRequestResultImpl> resultList,final Exception[] exList) throws Exception
    {
        Thread thread = new Thread()
        {
            public void run()
            {
                if (stop[0]) return;
                try
                {
                    String resourceMime = attr.getResourceMime();
                    String url = HttpUtil.composeAbsoluteURL(attr.getLocation(), urlBase);
                    HttpRequestResultImpl resultResource = HttpUtil.httpGet(url, httpRequestData, null, resourceMime);
                    processHttpRequestResultResource(url, attr, resultResource, resultList);
                }
                catch (Exception ex)
                {
                    exList[i] = ex;
                    stop[0] = true;
                }
            }
        };
        thread.start();
        return thread;
    }

    private void processHttpRequestResultResource(String urlBase, DOMAttrRemote attr, HttpRequestResultImpl resultRes, List<HttpRequestResultImpl> resultList) throws Exception
    {
        // Método llamado en multihilo

        if (resultList != null) resultList.add(resultRes);

        String resourceMime = attr.getResourceMime();
        if (MimeUtil.MIME_XML.equals(resourceMime))
        {
            String markup = resultRes.getResponseText();
            XMLDOM xmlDOM = XMLDOMParser.processDOMAttrDynamicXML(attr, markup, xmlInflateRegistry, assetManager);

            LinkedList<DOMAttrRemote> attrRemoteList = xmlDOM.getDOMAttrRemoteList();
            if (attrRemoteList != null)
                downloadResources(urlBase, attrRemoteList, resultList);
        }
        else if (MimeUtil.isMIMEImage(resourceMime))
        {
            attr.setResource(resultRes.getResponseByteArray());
        }
        else throw new ItsNatDroidException("Unsupported resource mime: " + resourceMime);
    }

}
