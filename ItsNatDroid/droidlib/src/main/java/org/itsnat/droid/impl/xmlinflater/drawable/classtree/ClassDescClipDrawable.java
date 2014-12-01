package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableRoot;
import org.itsnat.droid.impl.xmlinflater.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescClipDrawable extends ClassDescRootElementDrawable<ClipDrawable>
{
    // Para el atributo clipOrientation
    // No podemos usar OrientationUtil porque los valores numéricos SON DIFERENTES (1 y 2 en vez de 0 y 1), hay que joderse con la falta de homogeneidad
    private static Map<String, Integer> valueMap = new HashMap<String, Integer>( 2 );
    static
    {
        valueMap.put("horizontal", ClipDrawable.HORIZONTAL /* 1 */);
        valueMap.put("vertical", ClipDrawable.VERTICAL /* 2 */);
    }

    public ClassDescClipDrawable(ClassDescDrawableMgr classMgr)
    {
        super(classMgr,"clip");
    }

    @Override
    public ElementDrawableRoot createRootElementDrawable(DOMElement rootElem, XMLInflaterDrawable inflaterDrawable, Context ctx)
    {
        XMLInflateRegistry xmlInflateRegistry = classMgr.getXMLInflateRegistry();


        Drawable drawable = null;
        DOMAttr attrSrc = rootElem.findDOMAttribute(InflatedXML.XMLNS_ANDROID, "src");
        if (attrSrc != null) // Puede ser nulo, en dicho caso el drawable debe estar definido inline como elementos hijo
        {
            drawable = xmlInflateRegistry.getDrawable(attrSrc,ctx,inflaterDrawable);
        }

        int gravity;
        DOMAttr attrGravity = rootElem.findDOMAttribute(InflatedXML.XMLNS_ANDROID, "gravity");
        if (attrGravity != null)
            gravity = AttrDesc.parseMultipleName(attrGravity.getValue(), GravityUtil.valueMap);
        else
            gravity = Gravity.LEFT;

        int orientation;
        DOMAttr attrClipOrientation = rootElem.findDOMAttribute(InflatedXML.XMLNS_ANDROID, "clipOrientation");
        if (attrClipOrientation != null)
            orientation = AttrDesc.<Integer>parseSingleName(attrClipOrientation.getValue(),valueMap);
        else
            orientation = ClipDrawable.HORIZONTAL;

        // Si el drawable está definido como elemento hijo gana éste por delante del atributo src
        ArrayList<ElementDrawable> childList = inflaterDrawable.processRootChildElements(rootElem);
        if (childList.size() > 1)
            throw new ItsNatDroidException("Expected just a single element or none, processing ClipDrawable");
        if (childList.size() == 1)
        {
            ElementDrawable childDrawable = childList.get(0);

            /*
            String name = domElement.getName();
            ClassDescRootElementDrawable classDescBridge = (ClassDescRootElementDrawable)getClassDescDrawableMgr().get(name);
            drawable = classDescBridge.createRootElementDrawable(domElement,inflaterDrawable,ctx);
            */

            drawable = null; // PROVISIONAL
        }

        if (drawable == null)
            throw new ItsNatDroidException("Drawable is not defined in src attribute or as a child element, processing ClipDrawable");

        return new ElementDrawableRoot(new ClipDrawable(drawable,gravity,orientation),childList);
    }

    @Override
    protected boolean isAttributeIgnored(ClipDrawable draw,String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(draw,namespaceURI,name))
            return true;

        if (InflatedXML.XMLNS_ANDROID.equals(namespaceURI))
        {
            // Se usan en tiempo de construcción
            return ("clipOrientation".equals(name) || "drawable".equals(name) || "gravity".equals(name));
        }
        return false;
    }


    @Override
    public Class<ClipDrawable> getDrawableClass()
    {
        return ClipDrawable.class;
    }

    protected void init()
    {
        super.init();

    }
}
