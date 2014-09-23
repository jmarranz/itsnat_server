package org.itsnat.droid.impl.xmlinflater.classtree;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.InflatedLayoutPageImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescViewBased
{
    protected String className;
    protected Class<View> clasz;
    protected Constructor<View> constructor1P;
    protected Constructor<View> constructor3P;
    protected HashMap<String,AttrDesc> attrDescMap;
    protected ClassDescViewBased parentClass;
    protected boolean initiated;

    public ClassDescViewBased(String className,ClassDescViewBased parentClass)
    {
        this.className = className;
        this.parentClass = parentClass;
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
        if (clasz == null) this.clasz = (Class<View>)MiscUtil.resolveClass(className);
        return clasz;
    }

    protected boolean isInit()
    {
        return initiated;
    }

    protected void init()
    {
        initClass();

        this.attrDescMap = new HashMap<String,AttrDesc>();
    }

    protected static boolean isStyleAttribute(String namespaceURI,String name)
    {
        return ValueUtil.isEmpty(namespaceURI) && name.equals("style");
    }

    protected static boolean isSpinnerModeAttribute(View view,String namespaceURI,String name)
    {
        return (view instanceof Spinner) && XMLLayoutInflateService.XMLNS_ANDROID.equals(namespaceURI) && name.equals("spinnerMode");
    }

    protected void addAttrDesc(AttrDesc attrDesc)
    {
        attrDescMap.put(attrDesc.getName(),attrDesc);
    }

    protected AttrDesc getAttrDesc(String name)
    {
        return attrDescMap.get(name);
    }

    public boolean setAttribute(View view,String namespaceURI,String name,String value,OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending,InflatedLayoutImpl inflated)
    {
        if (!isInit()) init();

        if (isStyleAttribute(namespaceURI,name) ||
            isSpinnerModeAttribute(view,namespaceURI,name)) return false; // Se tratan de forma especial en otro lugar

        if (XMLLayoutInflateService.XMLNS_ANDROID.equals(namespaceURI))
        {
            AttrDesc attrDesc = getAttrDesc(name);
            if (attrDesc != null)
            {
                attrDesc.setAttribute(view, value, oneTimeAttrProcess,pending);
            }
            else
            {
                // Es importante recorrer las clases de abajo a arriba pues algún atributo se repite en varios niveles tal y como minHeight y minWidth
                // y tiene prioridad la clase más derivada
                if (parentClass != null)
                {
                    parentClass.setAttribute(view, namespaceURI, name, value, oneTimeAttrProcess,pending,inflated);
                }
                else
                {
                    // No se encuentra opción de proceso custom
                    PageImpl page = (inflated instanceof InflatedLayoutPageImpl) ? ((InflatedLayoutPageImpl)inflated).getPageImpl() : null;
                    AttrCustomInflaterListener listener = inflated.getAttrCustomInflaterListener();
                    if (listener != null) listener.setAttribute(page,view,namespaceURI, name, value);
                }
            }
        }
        else if (isXMLIdAttrAsDOM(namespaceURI, name))
        {
            inflated.setXMLId(value, view);
        }
        else
        {
            // No se encuentra opción de proceso custom
            AttrCustomInflaterListener listener = inflated.getAttrCustomInflaterListener();
            PageImpl page = (inflated instanceof InflatedLayoutPageImpl) ? ((InflatedLayoutPageImpl)inflated).getPageImpl() : null;
            if (listener != null) listener.setAttribute(page,view,namespaceURI, name, value);
        }

        return true;
    }


    public boolean removeAttribute(View view,String namespaceURI,String name,InflatedLayoutImpl inflated)
    {
        if (!isInit()) init();

        if (isStyleAttribute(namespaceURI,name)) return false; // Se trata de forma especial en otro lugar

        if (XMLLayoutInflateService.XMLNS_ANDROID.equals(namespaceURI))
        {
            AttrDesc attrDesc = getAttrDesc(name);
            if (attrDesc != null)
            {
                attrDesc.removeAttribute(view);
            }
            else
            {
                if (parentClass != null)
                {
                    parentClass.removeAttribute(view, namespaceURI, name, inflated);
                }
                else
                {
                    // No se encuentra opción de proceso custom
                    PageImpl page = (inflated instanceof InflatedLayoutPageImpl) ? ((InflatedLayoutPageImpl)inflated).getPageImpl() : null;
                    AttrCustomInflaterListener listener = inflated.getAttrCustomInflaterListener();
                    if (listener != null) listener.removeAttribute(page,view, namespaceURI, name);
                }
            }
        }
        else if (isXMLIdAttrAsDOM(namespaceURI, name))
        {
            inflated.unsetXMLId(view);
        }
        else
        {
            // No se encuentra opción de proceso custom
            AttrCustomInflaterListener listener = inflated.getAttrCustomInflaterListener();
            PageImpl page = (inflated instanceof InflatedLayoutPageImpl) ? ((InflatedLayoutPageImpl)inflated).getPageImpl() : null;
            if (listener != null) listener.removeAttribute(page,view, namespaceURI, name);
        }

        return true;
    }

    public static boolean isXMLIdAttrAsDOM(String namespaceURI, String name)
    {
        return (namespaceURI == null || "".equals(namespaceURI)) && "id".equals(name);
    }

    public View createAndAddViewObject(View viewParent,int index,int idStyle, Context ctx)
    {
        View view = createViewObject(ctx, idStyle);
        addViewObject(viewParent,view,index);
        return view;
    }

    protected void addViewObject(View viewParent,View view,int index)
    {
        if (viewParent != null)
        {
            if (index < 0) ((ViewGroup)viewParent).addView(view);
            else ((ViewGroup)viewParent).addView(view, index);
        }
        else fixViewRootLayoutParams(view); // view es la vista root
    }

    private View createViewObject(Context ctx,int idStyle)
    {
        Class<View> clasz = initClass();

        try
        {
            if (idStyle != 0)
            {
                // http://stackoverflow.com/questions/3142067/android-set-style-in-code
                // En teoría un parámetro es suficiente (con ContextThemeWrapper) pero curiosamente por ej en ProgressBar son necesarios los tres parámetros
                // de otra manera el idStyle es ignorado, por tanto aunque parece redundate el paso del idStyle, ambos params son necesarios en algún caso
                if (constructor3P == null) constructor3P = clasz.getConstructor(Context.class, AttributeSet.class, int.class);
                return constructor3P.newInstance(new ContextThemeWrapper(ctx,idStyle),(AttributeSet)null,idStyle);

                // ALTERNATIVA QUE NO FUNCIONA (idStyle es ignorado):
                //if (constructor3P == null) constructor3P = clasz.getConstructor(Context.class, AttributeSet.class, int.class);
                //return constructor3P.newInstance(ctx, null, idStyle);
            }
            else
            {
                if (constructor1P == null) constructor1P = clasz.getConstructor(Context.class);
                return constructor1P.newInstance(ctx);
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

    protected static AttributeSet readAttributeSetLayout(Context ctx,int layoutId)
    {
        // Método para crear un AttributeSet del elemento root a partir de un XML compilado

        XmlResourceParser parser = ctx.getResources().getLayout(layoutId);

        try { while (parser.next() != XmlPullParser.START_TAG) {}  }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }

        AttributeSet attributes = Xml.asAttributeSet(parser);
        return attributes;
    }

    protected static AttributeSet readAttributeSet_ANTIGUO(Context ctx)
    {
        // NO SE USA, el método Resources.getLayout() ya devuelve un XmlResourceParser compilado
        // Y antes funcionaba pero ya NO (Android 4.4).


        // Este método experimental es para create un AttributeSet a partir de un XML compilado, se trataria
        // de crear un archivo XML tal y como "<tag />" ir al apk generado y copiar el archivo compilado, abrirlo
        // y copiar el contenido compilado y guardarlo finalmente como un byte[] constante
        // El problema es que no he conseguido usar AttributeSet vacío para lo que lo quería.
        // El método lo dejo inutilizado por si en el futuro se necesita un AttributeSet

        // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.4.2_r1/android/content/res/XmlBlock.java?av=f

        Resources res = ctx.getResources();
        InputStream input = null; // res.openRawResource(R.raw.prueba_compilado_raw);
        byte[] content = IOUtil.read(input);

        try
        {
            Class<?> xmlBlockClass = Class.forName("android.content.res.XmlBlock");

            Constructor xmlBlockClassConstr = xmlBlockClass.getDeclaredConstructor(byte[].class);
            xmlBlockClassConstr.setAccessible(true);
            Object xmlBlock = xmlBlockClassConstr.newInstance(content);

            Method xmlBlockNewParserMethod = xmlBlockClass.getDeclaredMethod("newParser");
            xmlBlockNewParserMethod.setAccessible(true);
            XmlResourceParser parser = (XmlResourceParser)xmlBlockNewParserMethod.invoke(xmlBlock);

            while (parser.next() != XmlPullParser.START_TAG) {}

            AttributeSet attributes = Xml.asAttributeSet(parser);
            return attributes;
        }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InstantiationException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
    }


}
