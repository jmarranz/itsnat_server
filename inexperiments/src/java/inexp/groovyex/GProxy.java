
package inexp.groovyex;

import groovy.util.GroovyScriptEngine;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 *
 * @author jmarranz
 */
public class GProxy 
{
    protected static GroovyScriptEngine engine;
    protected static boolean developmentMode = false;
    protected static GProxyListener reloadListener;
    
    public static void init(GroovyScriptEngine theEngine,boolean devMode,GProxyListener relListener)
    {
        engine = theEngine;
        developmentMode = devMode;
        reloadListener = relListener;
    }
    
    public static class VersionedObject<T>
    {    
        protected T obj;
        protected String path;    
        
        public VersionedObject(T obj)
        {
            this.obj = obj;
            this.path = obj.getClass().getName().replace('.','/');
        }        
        
        public T getCurrent()
        {
            return obj;
        }
        
        public T getNewVersion() throws Throwable 
        {
            Class<T> newClass = (Class<T>)engine.loadScriptByName(path + ".groovy");  //inexp/groovyex/GroovyExampleLoadListener.groovy
            
            if (newClass != obj.getClass())
            {
                Class oldClass = obj.getClass();
                
                ArrayList<Field> fieldListOld = new ArrayList<Field>();
                ArrayList<Object> valueListOld = new ArrayList<Object>();
                
                getTreeFields(oldClass,obj,fieldListOld,valueListOld);
                
                try
                {
                this.obj = newClass.getConstructor(new Class[0]).newInstance();            
                }
                catch(NoSuchMethodException ex)
                {
                    throw new RuntimeException("Cannot reload " + newClass.getName() + " a default empty of params constructor is required",ex);
                }
                
                ArrayList<Field> fieldListNew = new ArrayList<Field>();
                
                getTreeFields(newClass,obj,fieldListNew,null);                
                            
                if (fieldListOld.size() != fieldListNew.size()) throw new RuntimeException("Cannot reload " + newClass.getName() + " number of fields have changed, redeploy");
                
                for(int i = 0; i < fieldListOld.size(); i++) 
                {
                    Field fieldOld = fieldListOld.get(i);
                    Field fieldNew = fieldListNew.get(i);
                    if (!fieldOld.getType().equals(fieldNew.getType()))
                        throw new RuntimeException("Cannot reload " + newClass.getName() + " fields have changed, redeploy");
                    
                    fieldNew.setAccessible(true);
                    fieldNew.set(obj, valueListOld.get(i));
                }
            }
            
            return obj;
        }
    }
    
    public static class ReloadableInvocationHandler<T> implements InvocationHandler
    {
        protected VersionedObject<T> verObj;
        
        public ReloadableInvocationHandler(T obj)
        {
            this.verObj = new VersionedObject<T>(obj);
        }
        
        public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
        {
            T oldObj = verObj.getCurrent();

            T obj = verObj.getNewVersion();

            if (oldObj != obj && reloadListener != null)
                reloadListener.onReload(oldObj,obj,proxy, method,args);  
            
            return method.invoke(obj, args);
        }        
    }
    
    public static <T> T create(T obj,Class<T> clasz)
    {
        if (!developmentMode || engine == null)
            return obj;
        
        InvocationHandler handler = new ReloadableInvocationHandler<T>(obj);
        
        T proxy = (T)Proxy.newProxyInstance(obj.getClass().getClassLoader(),new Class[] { clasz }, handler);   
        return proxy;
    }
    
    private static void getTreeFields(Class clasz,Object obj,ArrayList<Field> fieldList,ArrayList<Object> valueList) throws IllegalAccessException
    {    
        getFields(clasz,obj,fieldList,valueList);
        Class superClass = clasz.getSuperclass();
        if (superClass != null)
            getTreeFields(superClass,obj,fieldList,valueList);
    }
    
    private static void getFields(Class clasz,Object obj,ArrayList<Field> fieldList,ArrayList<Object> valueList) throws IllegalAccessException
    {
        Field[] fieldListClass = clasz.getDeclaredFields();             
        for(int i = 0; i < fieldListClass.length; i++)
        {
            Field field = fieldListClass[i];           
            fieldList.add(field);
            if (valueList != null)
            {
                field.setAccessible(true);                   
                Object value = field.get(obj);            
                valueList.add(value);
            }
        }             
    }
}
