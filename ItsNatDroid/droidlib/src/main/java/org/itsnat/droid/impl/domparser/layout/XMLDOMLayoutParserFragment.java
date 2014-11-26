package org.itsnat.droid.impl.domparser.layout;

import android.content.res.AssetManager;

import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;

/**
 * Created by jmarranz on 14/11/14.
 */
public class XMLDOMLayoutParserFragment extends XMLDOMLayoutParserPageOrFragment
{
    public XMLDOMLayoutParserFragment(XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        super(xmlInflateRegistry,assetManager);
    }
}
