package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflater.ClassDesc;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.page.XMLInflaterDrawablePage;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class ClassDescDrawable<Tdrawable extends Drawable> extends ClassDesc<Drawable>
{
    //protected static MethodContainer<ViewGroup.LayoutParams> methodGenerateLP = new MethodContainer<ViewGroup.LayoutParams>(ViewGroup.class, "generateDefaultLayoutParams", null);

    //protected Constructor<View> constructor1P;
    //protected Constructor<View> constructor3P;

    public ClassDescDrawable(ClassDescDrawableMgr classMgr, String className, ClassDescDrawable parentClass)
    {
        super(classMgr, className, parentClass);
    }

    public ClassDescDrawableMgr getClassDescDrawableMgr()
    {
        return (ClassDescDrawableMgr) classMgr;
    }

    public ClassDescDrawable getParentClassDescDrawable()
    {
        return (ClassDescDrawable) getParentClassDesc();
    }

    public static PageImpl getPageImpl(XMLInflaterDrawable xmlInflaterDrawable)
    {
        return (xmlInflaterDrawable instanceof XMLInflaterDrawablePage) ? ((XMLInflaterDrawablePage) xmlInflaterDrawable).getPageImpl() : null;
    }

    protected AttrDescDrawable getAttrDescDrawable(String name)
    {
        return (AttrDescDrawable) getAttrDesc(name);
    }

    protected boolean isAttributeIgnored(String namespaceURI, String name)
    {
        return false;
    }

    public boolean setAttribute(Drawable draw, DOMAttr attr,XMLInflaterDrawable xmlInflaterDrawable, Context ctx)
    {
        if (!isInit()) init();

        String namespaceURI = attr.getNamespaceURI();
        String name = attr.getName(); // El nombre devuelto no contiene el namespace

        if (isAttributeIgnored(namespaceURI, name)) return false; // Se trata de forma especial en otro lugar

        InflatedDrawable inflated = xmlInflaterDrawable.getInflatedDrawable();

        if (InflatedXML.XMLNS_ANDROID.equals(namespaceURI))
        {
            AttrDescDrawable attrDesc = getAttrDescDrawable(name);
            if (attrDesc != null)
            {
                attrDesc.setAttribute(draw, attr,xmlInflaterDrawable,ctx);
            }
            else
            {
                // Es importante recorrer las clases de abajo a arriba pues algún atributo se repite en varios niveles tal y como minHeight y minWidth
                // y tiene prioridad la clase más derivada

                ClassDescDrawable parentClass = getParentClassDescDrawable();
                if (parentClass != null)
                {
                    parentClass.setAttribute(draw, attr, xmlInflaterDrawable,ctx);
                }
                else
                {
                    // No se encuentra opción de proceso custom
                    AttrDrawableInflaterListener listener = xmlInflaterDrawable.getAttrDrawableInflaterListener();
                    if (listener != null)
                    {
                        PageImpl page = getPageImpl(xmlInflaterDrawable); // Puede ser nulo
                        String value = attr.getValue();
                        listener.setAttribute(page, draw, namespaceURI, name, value);
                    }
                }
            }
        }
        else
        {
            // No se encuentra opción de proceso custom
            AttrDrawableInflaterListener listener = xmlInflaterDrawable.getAttrDrawableInflaterListener();
            if (listener != null)
            {
                PageImpl page = getPageImpl(xmlInflaterDrawable); // Puede ser nulo
                String value = attr.getValue();
                listener.setAttribute(page, draw, namespaceURI, name, value);
            }
        }

        return true;
    }


    public boolean removeAttribute(Drawable draw, String namespaceURI, String name, XMLInflaterDrawable xmlInflaterDrawable, Context ctx)
    {
        if (!isInit()) init();

        if (isAttributeIgnored(namespaceURI, name)) return false; // Se trata de forma especial en otro lugar

        InflatedDrawable inflated = xmlInflaterDrawable.getInflatedDrawable();

        if (InflatedXML.XMLNS_ANDROID.equals(namespaceURI))
        {
            AttrDescDrawable attrDesc = getAttrDescDrawable(name);
            if (attrDesc != null)
            {
                attrDesc.removeAttribute(draw,xmlInflaterDrawable,ctx);
            }
            else
            {
                ClassDescDrawable parentClass = getParentClassDescDrawable();
                if (parentClass != null)
                {
                    parentClass.removeAttribute(draw, namespaceURI, name, xmlInflaterDrawable,ctx);
                }
                else
                {
                    // No se encuentra opción de proceso custom
                    AttrDrawableInflaterListener listener = xmlInflaterDrawable.getAttrDrawableInflaterListener();
                    if (listener != null)
                    {
                        PageImpl page = getPageImpl(xmlInflaterDrawable); // Puede ser nulo
                        listener.removeAttribute(page, draw, namespaceURI, name);
                    }
                }
            }
        }
        else
        {
            // No se encuentra opción de proceso custom
            AttrDrawableInflaterListener listener = xmlInflaterDrawable.getAttrDrawableInflaterListener();
            if (listener != null)
            {
                PageImpl page = getPageImpl(xmlInflaterDrawable); // Puede ser nulo
                listener.removeAttribute(page, draw, namespaceURI, name);
            }
        }

        return true;
    }

    public abstract Class<Tdrawable> getDrawableClass();
    public abstract Drawable createRootDrawable(DOMElement rootElem,InflatedDrawable inflatedDrawable,Context ctx);
}
