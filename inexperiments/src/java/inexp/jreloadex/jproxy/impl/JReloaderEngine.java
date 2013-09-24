package inexp.jreloadex.jproxy.impl;

import inexp.jreloadex.jproxy.impl.comp.JReloaderCompilerInMemory;
import inexp.jreloadex.jproxy.impl.comp.JavaFileObjectOutputClass;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author jmarranz
 */
public class JReloaderEngine 
{
    protected JReloaderCompilerInMemory compiler = new JReloaderCompilerInMemory();
    protected ClassLoader parentClassLoader;
    protected JReloaderClassLoader customClassLoader;
    protected JavaSourcesSearch sourcesSearch;
    protected Map<String,HotLoadableClass> hotLoadableClasses;
    
    public JReloaderEngine(ClassLoader parentClassLoader,String pathSources,long scanPeriod)
    {
        this.parentClassLoader = parentClassLoader;
        this.customClassLoader = new JReloaderClassLoader(this,parentClassLoader);
        this.sourcesSearch = new JavaSourcesSearch(pathSources);
        this.hotLoadableClasses = sourcesSearch.recursiveJavaFileSearch();        
        
        Timer timer = new Timer();        
        TimerTask task = new TimerTask()
        {
            @Override
            public void run() 
            {
                try
                {
                    updateTimestamps();
                }        
                catch(Exception ex)
                {
                    ex.printStackTrace(System.err); // Si dejamos subir la excepción se acabó el timer
                }
            }
        };                
        timer.schedule(task, scanPeriod, scanPeriod);  // Ojo, después de recursiveJavaFileSearch()      
    }

    public synchronized boolean isHotLoadableClass(String className)
    {
        // Las innerclasses no están como tales en hotLoadableClasses pues sólo está la clase contenedora pero también la consideramos hotloadable
        int pos = className.lastIndexOf('$');
        if (pos != -1) className = className.substring(0, pos);
                    
        return hotLoadableClasses.containsKey(className);
    }
    

    public synchronized <T> Class<?> findClass(String className)
    {     
        // Si ya está cargada la devuelve, y si no se cargó por ningún JReloaderClassLoader se intenta cargar por el parent ClassLoader, por lo que siempre devolverá distinto de null si la clase está en el classpath, que debería ser lo normal       
        try 
        { 
            return customClassLoader.findClass(className); 
        }
        catch (ClassNotFoundException ex) 
        {
            return null;
        }
    }
    
    private synchronized void addNewClassLoader()
    {
        for(HotLoadableClass hotClass : hotLoadableClasses.values())
        {
            hotClass.setLastLoadedClass(null);
        }
        
        this.customClassLoader = new JReloaderClassLoader(this,parentClassLoader);               
    }
    
    private synchronized <T> Class<T> compileAndLoadClass(HotLoadableClass classDesc,boolean newClassLoader)
    {
        if (newClassLoader)
        {
            addNewClassLoader();
        }

        String className = classDesc.getClassName();
        String path = classDesc.getPath();
        LinkedList<JavaFileObjectOutputClass> outClasses = compiler.compile(path,customClassLoader,hotLoadableClasses);
        
        if (outClasses == null) throw new RuntimeException("Cannot reload class: " + className);
        
        Class claszReturn = null;
        // Puede haber más de un resultado cuando hay inner classes
        for(JavaFileObjectOutputClass outClass : outClasses)
        {
            String currClassName = outClass.binaryName();
            byte[] classBytes = outClass.getBytes();
            classDesc.setClassBytes(classBytes);        
            Class clasz = customClassLoader.defineClass(currClassName,classBytes);         
            classDesc.setLastLoadedClass(clasz);
            
            if (className.equals(currClassName))
                claszReturn = clasz;
        }

        return claszReturn; // No debería ser null
    }    
    
    protected synchronized void updateTimestamps()
    {
        //Map<String,HotLoadableClass> oldClassMap = hotLoadableClasses;
        
        LinkedList<HotLoadableClass> updatedClasses = new LinkedList<HotLoadableClass>();
        LinkedList<HotLoadableClass> newClasses = new LinkedList<HotLoadableClass>();        

        this.hotLoadableClasses = sourcesSearch.recursiveJavaFileSearch(hotLoadableClasses,updatedClasses,newClasses);  
        
        // oldClassMap contiene ahora mismo las clases que han sido eliminadas
        //oldClassMap = null;
        
        if (!updatedClasses.isEmpty() || !newClasses.isEmpty())
        {   
            addNewClassLoader();
                        
            for(HotLoadableClass hotClass : updatedClasses)
            {
                compileAndLoadClass(hotClass,false);
            }
            
            for(HotLoadableClass hotClass : newClasses)
            {
                compileAndLoadClass(hotClass,false);
            }               
            
            for(Map.Entry<String,HotLoadableClass> entry : hotLoadableClasses.entrySet())
            {
                String className = entry.getKey();
                HotLoadableClass hotClass = entry.getValue();
                if (updatedClasses.contains(hotClass))
                    continue;
                if (newClasses.contains(hotClass))
                    continue;   
                
                Class newClasz = hotClass.getLastLoadedClass();
                if (newClasz == null)
                {  
                    byte[] classBytes = hotClass.getClassBytes();
                    if (classBytes == null)
                    {
                        Class clasz;
                        try { clasz = parentClassLoader.loadClass(className); } catch (ClassNotFoundException ex) { throw new RuntimeException(ex); }                  
                        String simpleClassName = clasz.getName();
                        int pos = simpleClassName.lastIndexOf(".");
                        if(pos != -1) simpleClassName = simpleClassName.substring(pos + 1);
                        simpleClassName = simpleClassName + ".class";
                        URL url = clasz.getResource(simpleClassName);  
                        classBytes = JReloaderUtil.readURL(url);
                    }

                    newClasz = customClassLoader.defineClass(className,classBytes);         
                    hotClass.setLastLoadedClass(newClasz);
                }
                else
                {
                    // Ha sido ha cargado/definido indirectamente
                    if (newClasz.getClassLoader() != customClassLoader) throw new RuntimeException();
                }
            }
         
        }
    }
    
}
