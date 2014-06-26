package org.itsnat.droid.impl.xmlinflater.classtree;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescViewBase
{
    protected String className;
    protected Class<View> clasz;
    protected Constructor<View> constructor;
    protected HashMap<String,AttrDesc> attrDescMap;
    protected ClassDescViewBase parent;
    protected boolean inited;

    public ClassDescViewBase(String className,ClassDescViewBase parent)
    {
        this.className = className;
        this.parent = parent;
    }

    public String getClassName()
    {
        return className;
    }

    public Class<View> getViewClass()
    {
        return (Class<View>)clasz;
    }

    private Class<View> initClass()
    {
        if (clasz != null) return clasz;
        try { return this.clasz = (Class<View>)Class.forName(className); }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
    }

    protected boolean isInit()
    {
        return inited;
    }

    protected void init()
    {
        initClass();

        this.attrDescMap = new HashMap<String,AttrDesc>();
    }

    protected void addAttrDesc(AttrDesc attrDesc)
    {
        attrDescMap.put(attrDesc.getName(),attrDesc);
    }

    protected AttrDesc getAttrDesc(String name)
    {
        return attrDescMap.get(name);
    }

    public boolean setAttribute(View view,String namespace,String name,String value,OneTimeAttrProcess oneTimeAttrProcess,InflatedLayoutImpl inflated)
    {
        if (!isInit()) init();

        if (namespace != null && namespace.isEmpty() && name.equals("style")) return false; // Se trata de forma especial

        if (XMLLayoutInflateService.XMLNS_ANDROID.equals(namespace))
        {
            AttrDesc attrDesc = getAttrDesc(name);
            if (attrDesc != null)
            {
                attrDesc.setAttribute(view, value, oneTimeAttrProcess);
            }
            else
            {
                if (parent != null)
                {
                    parent.setAttribute(view, namespace, name, value, oneTimeAttrProcess, inflated);
                }
                else
                {
                    // No se encuentra opción de proceso custom
                    AttrCustomInflaterListener listener = inflated.getAttrCustomInflaterListener();
                    if (listener != null) listener.setAttribute(view,namespace, name, value);
                }
            }
        }
        else if (isElementIdAttrAsDOM(namespace,name))
        {
            inflated.setElementIdAsDOM(value,view);
        }
        else
        {
            // No se encuentra opción de proceso custom
            AttrCustomInflaterListener listener = inflated.getAttrCustomInflaterListener();
            if (listener != null) listener.setAttribute(view,namespace, name, value);
        }

        return true;
    }


    public boolean removeAttribute(View view,String namespace,String name,InflatedLayoutImpl inflated)
    {
        if (!isInit()) init();

        if (namespace != null && namespace.isEmpty() && name.equals("style")) return false; // Se trata de forma especial

        if (XMLLayoutInflateService.XMLNS_ANDROID.equals(namespace))
        {
            AttrDesc attrDesc = getAttrDesc(name);
            if (attrDesc != null)
            {
                attrDesc.removeAttribute(view);
            }
            else
            {
                if (parent != null)
                {
                    parent.removeAttribute(view, namespace, name, inflated);
                }
                else
                {
                    // No se encuentra opción de proceso custom
                    AttrCustomInflaterListener listener = inflated.getAttrCustomInflaterListener();
                    if (listener != null) listener.removeAttribute(view, namespace, name);
                }
            }
        }
        else if (isElementIdAttrAsDOM(namespace,name))
        {
            inflated.unsetElementIdAsDOM(view);
        }
        else
        {
            // No se encuentra opción de proceso custom
            AttrCustomInflaterListener listener = inflated.getAttrCustomInflaterListener();
            if (listener != null) listener.removeAttribute(view, namespace, name);
        }

        return true;
    }

    public static boolean isElementIdAttrAsDOM(String namespace,String name)
    {
        return (namespace == null || "".equals(namespace)) && "id".equals(name);
    }

    public View createAndAddViewObject(View viewParent,int index,int idStyle, Context ctx)
    {
        View view = createViewObject(ctx, idStyle);
        if (viewParent != null)
        {
            if (index < 0) ((ViewGroup)viewParent).addView(view);
            else ((ViewGroup)viewParent).addView(view, index);
        }
        else fixViewRootLayoutParams(view); // view es la vista root
        return view;
    }

    private View createViewObject(Context ctx,int idStyle)
    {
        Class<View> clasz = initClass();

        try
        {
            if (constructor == null) constructor = clasz.getConstructor(Context.class);

            if (idStyle != 0)
            {
                /* NO FUNCIONA
                Constructor<View> constructor = clasz.getConstructor(Context.class, AttributeSet.class, int.class);
                AttributeSet attributes = createEmptyAttributeSet(ctx);
                return constructor.newInstance(ctx, attributes, idStyle);
                */

                // http://stackoverflow.com/questions/3142067/android-set-style-in-code
                return constructor.newInstance(new ContextThemeWrapper(ctx,idStyle));
            }
            else
            {
                return constructor.newInstance(ctx);
            }
        }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InstantiationException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }



    private void fixViewRootLayoutParams(View view)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) throw new ItsNatDroidException("Unexpected");

        // Esto ocurre con el View root del layout porque hasta el final no podemos insertarlo en el ViewGroup contenedor que nos ofrece Android por ej en la Actividad, no creo que sea necesario algo diferente a un ViewGroup.LayoutParams
        // aunque creo que no funciona el poner valores concretos salvo el match_parent que afortunadamente es el único que interesa para
        // un View root que se inserta.
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
    }

    private static AttributeSet createEmptyAttributeSet_NO_SE_USA(Context ctx)
    {
        // Este método experimental es para create un AttributeSet vacío a partir de un XML compilado, se trataria
        // de crear un archivo XML tal y como "<tag />" ir al apk generado y copiar el archivo compilado, abrirlo
        // y copiar el contenido compilado y guardarlo finalmente como un byte[] constante
        // El problema es que no he conseguido usar AttributeSet vacío para lo que lo quería.
        // El método lo dejo inutilizado por si en el futuro se necesita un AttributeSet

        // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.4.2_r1/android/content/res/XmlBlock.java?av=f

        InputStream input = null; // ctx.getResources().openRawResource(R.raw.prueba_compilado);
        byte[] content = IOUtil.read(input);

        try
        {
            Class<?> xmlBlockClass = Class.forName("android.content.res.XmlBlock");

            Constructor xmlBlockClassConstr = xmlBlockClass.getConstructor(byte[].class);
            Object xmlBlock = xmlBlockClassConstr.newInstance(content);

            Method newParserMethod = xmlBlock.getClass().getMethod("newParser");
            XmlResourceParser parser = (XmlResourceParser)newParserMethod.invoke(xmlBlock);

            AttributeSet attributes = Xml.asAttributeSet(parser);
            return attributes;
        }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InstantiationException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
    }


}
