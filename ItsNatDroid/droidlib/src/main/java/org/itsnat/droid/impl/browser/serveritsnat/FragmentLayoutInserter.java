package org.itsnat.droid.impl.browser.serveritsnat;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.XMLParsedCache;
import org.itsnat.droid.impl.parser.layout.LayoutParser;
import org.itsnat.droid.impl.parser.layout.LayoutParserPage;
import org.itsnat.droid.impl.model.layout.ScriptInlineParsed;
import org.itsnat.droid.impl.model.layout.ScriptParsed;
import org.itsnat.droid.impl.model.layout.ScriptRemoteParsed;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.xmlinflater.page.InflatedLayoutPageImpl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Created by jmarranz on 29/10/14.
 */
public class FragmentLayoutInserter
{
    public ItsNatDocImpl itsNatDoc;

    public FragmentLayoutInserter(ItsNatDocImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public void insertFragment(ViewGroup parentView, String markup,View viewRef)
    {
        // Preparamos primero el markup añadiendo un false parentView que luego quitamos, el false parentView es necesario
        // para declarar el namespace android, el false parentView será del mismo tipo que el de verdad para que los
        // LayoutParams se hagan bien.

        InflatedLayoutPageImpl pageLayout = itsNatDoc.getPageImpl().getInflatedLayoutPageImpl();

        StringBuilder newMarkup = new StringBuilder();

        newMarkup.append("<" + parentView.getClass().getName());

        MapLight<String, String> namespaceMap = pageLayout.getNamespacesByPrefix();
        for (Iterator<Map.Entry<String, String>> it = namespaceMap.getEntryList().iterator(); it.hasNext(); )
        {
            Map.Entry<String, String> entry = it.next();
            newMarkup.append(" xmlns:" + entry.getKey() + "=\"" + entry.getValue() + "\">");
        }

        newMarkup.append(">");
        newMarkup.append(markup);
        newMarkup.append("</" + parentView.getClass().getName() + ">");

        markup = newMarkup.toString();

        LayoutParsed layoutParsed;

        XMLParsedCache<LayoutParsed> layoutParsedCache = pageLayout.getItsNatDroidImpl().getXMLLayoutInflateService().getLayoutParsedCache();
        LayoutParsed cachedLayout = layoutParsedCache.get(markup);
        if (cachedLayout != null)
            layoutParsed = cachedLayout;
        else
        {
            boolean loadingPage = false;
            LayoutParser layoutParser = new LayoutParserPage(pageLayout.getPageImpl().getItsNatServerVersion(),loadingPage);
            layoutParsed = layoutParser.parse(markup);
            layoutParsedCache.put(markup, layoutParsed);
        }

        LinkedList<ScriptParsed> scriptList = new LinkedList<ScriptParsed>();

        List<ScriptParsed> scriptListParsed = layoutParsed.getScriptList();
        if (scriptListParsed != null)
            scriptList.addAll(scriptListParsed);

        ViewGroup falseParentView = (ViewGroup) pageLayout.insertFragment(layoutParsed.getRootView()); // Los XML ids, los inlineHandlers etc habrán quedado memorizados
        int indexRef = viewRef != null ? InflatedLayoutPageImpl.getChildIndex(parentView,viewRef) : -1;
        while (falseParentView.getChildCount() > 0)
        {
            View child = falseParentView.getChildAt(0);
            falseParentView.removeViewAt(0);
            if (indexRef >= 0)
            {
                parentView.addView(child, indexRef);
                indexRef++;
            }
            else parentView.addView(child);
        }

        executeScriptList(scriptList);
    }

    private void executeScriptList(LinkedList<ScriptParsed> scriptList)
    {
        if (scriptList.isEmpty()) return;

        Interpreter interp = itsNatDoc.getPageImpl().getInterpreter();
        for (ScriptParsed script : scriptList)
        {
            if (script instanceof ScriptInlineParsed)
            {
                String code = script.getCode();
                try
                {
                    interp.eval(code);
                }
                catch (EvalError ex) { throw new ItsNatDroidScriptException(ex, code); }
                catch (Exception ex) { throw new ItsNatDroidScriptException(ex, code); }
            }
            else if (script instanceof ScriptRemoteParsed)
            {
                String src = ((ScriptRemoteParsed)script).getSrc();
                itsNatDoc.downloadScript(src); // Se carga asíncronamente sin un orden claro
            }
        }

    }
}
