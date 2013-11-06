package inexp.jreloadex.jproxy.impl;

import inexp.jreloadex.jproxy.impl.comp.JReloaderCompilerInMemory;
import java.io.File;
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
    protected Map<String,ClassDescriptorSourceFile> sourceFileMap;
    protected String classFolder; // Puede ser nulo (es decir NO salvar como .class los cambios)
    
    public JReloaderEngine(ClassLoader parentClassLoader,String pathSources,String classFolder,long scanPeriod)
    {
        this.parentClassLoader = parentClassLoader;
        this.classFolder = classFolder;
        this.customClassLoader = new JReloaderClassLoader(this,parentClassLoader);
        this.sourcesSearch = new JavaSourcesSearch(pathSources,parentClassLoader);       
        detectChangesInSources(); // Primera vez para detectar cambios en los .java respecto a los .class mientras el servidor estaba parado
        
        
        Timer timer = new Timer();        
        TimerTask task = new TimerTask()
        {
            @Override
            public void run() 
            {
                try
                {
                    detectChangesInSources();
                }        
                catch(Exception ex)
                {
                    ex.printStackTrace(System.err); // Si dejamos subir la excepción se acabó el timer
                }
            }
        };                
        timer.schedule(task, scanPeriod, scanPeriod);  // Ojo, después de recursiveJavaFileSearch()      
    }
    
    /*
    public synchronized boolean isHotLoadableClass(String className)
    {
        // Las innerclasses no están como tales en sourceFileMap pues sólo está la clase contenedora pero también la consideramos hotloadable
        int pos = className.lastIndexOf('$');
        if (pos != -1) className = className.substring(0, pos);

        return sourceFileMap.containsKey(className);
    }
    */
    
    public boolean isSaveClassesMode()
    {
        return (classFolder != null);
    }
    
    public synchronized ClassDescriptor getClassDescriptor(String className)
    {
        // Puede ser el de una innerclass
        // Las innerclasses no están como tales en sourceFileMap pues sólo está la clase contenedora pero también la consideramos hotloadable
        String parentClassName;
        int pos = className.lastIndexOf('$');        
        boolean inner;        
        if (pos != -1)
        {
            parentClassName = className.substring(0, pos);
            inner = true;
        }
        else
        {
            parentClassName = className;
            inner = false;
        }
        ClassDescriptorSourceFile sourceDesc = sourceFileMap.get(parentClassName);        
        if (!inner) return sourceDesc;
        return sourceDesc.getInnerClassDescriptor(className,true);
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
        for(ClassDescriptorSourceFile sourceFile : sourceFileMap.values())
        {
            sourceFile.resetLastLoadedClass(); // resetea también las innerclasses
        }
        
        this.customClassLoader = new JReloaderClassLoader(this,parentClassLoader);               
    }
    
    private void compileReloadAndSaveSource(ClassDescriptorSourceFile sourceFile)
    {
        deleteClasses(sourceFile); // Antes de que nos las carguemos en memoria la clase principal y las inner tras recompilar
            
        sourceFile.cleanOnSourceCodeChanged(); // El código fuente nuevo puede haber cambiado totalmente las innerclasses antiguas (añadido, eliminado)
        
        compiler.compileSourceFile(sourceFile,customClassLoader,sourceFileMap);
        
        reloadSource(sourceFile,false); // No hace falta que detectemos las innerclasses porque al compilar se "descubren" todas

        saveClasses(sourceFile);        
    }        
    
    private void reloadSource(ClassDescriptorSourceFile sourceFile,boolean detectInnerClasses)
    {
        Class clasz = customClassLoader.loadClass(sourceFile,true);      

        LinkedList<ClassDescriptorInner> innerClassDescList = sourceFile.getInnerClassDescriptors(); 
        if (innerClassDescList != null && !innerClassDescList.isEmpty())
        {
            for(ClassDescriptorInner innerClassDesc : innerClassDescList)
            {
                customClassLoader.loadClass(innerClassDesc,true);           
            }
        }        
        else if (detectInnerClasses)
        {
            // Aprovechando la carga de la clase, hacemos el esfuerzo de cargar todas las clases dependientes lo más posible
            clasz.getDeclaredClasses(); // Provoca que las inner clases miembro indirectamente se procesen y carguen a través del JReloaderClassLoader de la clase padre clasz
            
            // Ahora bien, lo anterior NO sirve para las anonymous inner classes, afortunadamente en ese caso podemos conocer y cargar por fuerza bruta
            // http://stackoverflow.com/questions/1654889/java-reflection-how-can-i-retrieve-anonymous-inner-classes?rq=1
            
            for(int i = 1; i < Integer.MAX_VALUE; i++)
            {
                String anonClassName = sourceFile.getClassName() + "$" + i;                 
                Class innerClasz = customClassLoader.loadInnerClass(sourceFile,anonClassName);
                if (innerClasz == null) break; // No hay más o no hay ninguna (si i es 1)
            } 
            
            // ¿Qué es lo que queda por cargar pero que no podemos hacer explícitamente?
            // 1) Las clases privadas autónomas que fueron definidas en el mismo archivo que la clase principal: no las soportamos pues no podemos identificar en el ClassLoader que es una clase "hot reloadable", no son inner classes en el sentido estricto
            // 2) Las clases privadas "inner" locales, es decir no anónimas declaradas dentro de un método, se cargarán la primera vez que se usen, no podemos conocerlas a priori
            //    porque siguen la notación className$NclassName  ej. JReloadExampleDocument$1AuxMemberInMethod. No hay problema con que se carguen con un class loader antiguo pues
            //    el ClassLoader de la clase padre contenedora será el encargado de cargarla en cuanto se pase por el método que la declara.
        }     
    }    
    
    private void saveClasses(ClassDescriptorSourceFile sourceFile)
    {
        if (classFolder == null) return;
        
        // Salvamos la clase principal
        {
            String classFilePath = ClassDescriptor.getAbsoluteClassFilePathFromClassNameAndClassPath(sourceFile.getClassName(),classFolder);
            JReloaderUtil.saveFile(classFilePath,sourceFile.getClassBytes());
        }

        // Salvamos las innerclasses si hay, no hay problema de clases inner no detectadas pues lo están todas pues sólo se salva tras una compilación
        LinkedList<ClassDescriptorInner> innerClassDescList = sourceFile.getInnerClassDescriptors();            
        if (innerClassDescList != null && !innerClassDescList.isEmpty())
        {
            for(ClassDescriptorInner innerClassDesc : innerClassDescList)
            {
                String classFilePath = ClassDescriptor.getAbsoluteClassFilePathFromClassNameAndClassPath(innerClassDesc.getClassName(),classFolder);
                JReloaderUtil.saveFile(classFilePath,innerClassDesc.getClassBytes());                
            }
        }                           
    }    
    
    private void deleteClasses(ClassDescriptorSourceFile sourceFile)
    {
        if (classFolder == null) return; // por si acaso
        
        // Salvamos la clase principal
        {
            String classFilePath = ClassDescriptor.getAbsoluteClassFilePathFromClassNameAndClassPath(sourceFile.getClassName(),classFolder);
            new File(classFilePath).delete();
        }

        // Salvamos las innerclasses si hay, como hay un reload forzado en caso de salvado activado, conoceremos todas las innerclases excepto por desgracia las locales (con nombre) que no podemos determinarlas hasta que no se usen
        // por eso no las soportamos en modo salvado
        LinkedList<ClassDescriptorInner> innerClassDescList = sourceFile.getInnerClassDescriptors();            
        if (innerClassDescList != null && !innerClassDescList.isEmpty())
        {
            for(ClassDescriptorInner innerClassDesc : innerClassDescList)
            {
                String classFilePath = ClassDescriptor.getAbsoluteClassFilePathFromClassNameAndClassPath(innerClassDesc.getClassName(),classFolder);
                new File(classFilePath).delete();              
            }
        }                           
    }          
    
    private synchronized void detectChangesInSources()
    {
        boolean firstTime = (sourceFileMap == null); // La primera vez sourceFileMap es null
        boolean forceFirstLoad = firstTime && (classFolder != null);  // En caso de primera vez y salvado a archivo activado, forzamos una primera carga de clases con un JReloaderClassLoader para conseguir tener desde un primero momento las inner clases (por desgracia excepto las locales) para que podamos eliminarlas, si da lugar, de forma determinista posteriormente
        
        LinkedList<ClassDescriptorSourceFile> updatedSourceFiles = new LinkedList<ClassDescriptorSourceFile>();
        LinkedList<ClassDescriptorSourceFile> newSourceFiles = new LinkedList<ClassDescriptorSourceFile>();        
        LinkedList<ClassDescriptorSourceFile> deletedSourceFiles = new LinkedList<ClassDescriptorSourceFile>();
        
        this.sourceFileMap = sourcesSearch.javaFileSearch(sourceFileMap,updatedSourceFiles,newSourceFiles,deletedSourceFiles);  

        if (forceFirstLoad || (!updatedSourceFiles.isEmpty() || !newSourceFiles.isEmpty() || !deletedSourceFiles.isEmpty())) // También el hecho de eliminar una clase debe implicar crear un ClassLoader nuevo para que dicha clase desaparezca de las clases cargadas aunque será muy raro que sólo eliminemos un .java y no añadamos/cambiemos otros, otro motico es porque si tenemos configurado el autosalvado de .class tenemos que eliminar en ese caso
        {   
            addNewClassLoader();
                        
            if (!updatedSourceFiles.isEmpty()) 
                for(ClassDescriptorSourceFile sourceFile : updatedSourceFiles)            
                    compileReloadAndSaveSource(sourceFile);
            
            if (!newSourceFiles.isEmpty())             
                for(ClassDescriptorSourceFile sourceFile : newSourceFiles)
                    compileReloadAndSaveSource(sourceFile);

            if (classFolder != null && !deletedSourceFiles.isEmpty())
                for(ClassDescriptorSourceFile sourceFile : deletedSourceFiles)
                    deleteClasses(sourceFile);                     
            
            
            for(Map.Entry<String,ClassDescriptorSourceFile> entry : sourceFileMap.entrySet())
            {
                //String className = entry.getKey();
                ClassDescriptorSourceFile sourceFile = entry.getValue();
                if (updatedSourceFiles.contains(sourceFile))
                    continue;
                if (newSourceFiles.contains(sourceFile))
                    continue;   
                
                reloadSource(sourceFile,true); // Ponemos detectInnerClasses a true porque son archivos fuente que posiblemente nunca se hayan tocado desde la carga inicial y por tanto quizás se desconocen las innerclasses
            }
         
        }
    }
    
}
