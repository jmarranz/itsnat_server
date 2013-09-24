package inexp.jreloadex.jproxy.impl;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author jmarranz
 */
public class JavaSourcesSearch 
{
    protected String pathSources;
    
    public JavaSourcesSearch(String pathSources)
    {
        this.pathSources = new File(pathSources).getAbsolutePath(); // Para normalizar
    }
    
    public Map<String,HotLoadableClass> recursiveJavaFileSearch()
    {       
        return recursiveJavaFileSearch(null,null,null);
    }    
    
    public Map<String,HotLoadableClass> recursiveJavaFileSearch(Map<String,HotLoadableClass> oldClassMap,LinkedList<HotLoadableClass> updatedClasses,LinkedList<HotLoadableClass> newClasses)
    {
        Map<String,HotLoadableClass> newClassMap = new HashMap<String,HotLoadableClass>();
        String[] children = new File(pathSources).list(); 
        recursiveJavaFileSearch(pathSources,children,oldClassMap,newClassMap,updatedClasses,newClasses);
        return newClassMap;
    }
    
    private void recursiveJavaFileSearch(String parentPath,String[] relPathList,Map<String,HotLoadableClass> oldClassMap,Map<String,HotLoadableClass> newClassMap,LinkedList<HotLoadableClass> updatedClasses,LinkedList<HotLoadableClass> newClasses)
    {
        for(String relPath : relPathList)
        {
            File file = new File(parentPath + "/" + relPath);        
            if (file.isDirectory())
            {
                String[] children = file.list();   
                recursiveJavaFileSearch(file.getAbsolutePath(),children,oldClassMap,newClassMap,updatedClasses,newClasses);
            }
            else
            {
                int pos = relPath.lastIndexOf('.');
                if (pos > 0) 
                {
                    String ext = relPath.substring(pos+1);
                    if ("java".equals(ext))
                    {
                        String path = file.getAbsolutePath();
                        String className = getClassNameFromPath(path);
                        long timestamp = file.lastModified();
                        HotLoadableClass clasz;
                        if (oldClassMap != null)
                        {
                            clasz = oldClassMap.get(className);
                            
                            if (clasz != null) // Si es null es que la clase es nueva
                            {
                                long oldTimestamp = clasz.getTimestamp();
                                if (timestamp != oldTimestamp)
                                {
                                    clasz.updateTimestamp(timestamp);
                                    updatedClasses.add(clasz);
                                }
                                
                                oldClassMap.remove(className); // Para que sólo queden las clases que han sido eliminadas
                            }          
                            else // Clase nueva
                            {
                                clasz = new HotLoadableClass(className,path,timestamp);
                                newClasses.add(clasz);
                            }
                        }
                        else
                        {
                            // Primera vez
                            clasz = new HotLoadableClass(className,path,timestamp);
                        }

                        newClassMap.put(className,clasz);
                    }
                }
            }                
        }
    }    
    
    private String getClassNameFromPath(String path)
    {
        int pos = path.indexOf(pathSources); 
        if (pos != 0) // DEBE SER 0, NO debería ocurrir
            return null;
        path = path.substring(pathSources.length() + 1); // Sumamos +1 para quitar también el / separador del pathInput y el path relativo de la clase
        // Quitamos la extensión
        pos = path.lastIndexOf('.');        
        if (pos == -1) return null; // NO debe ocurrir
        path = path.substring(0, pos);        
        path = path.replace(File.separatorChar, '.');  // getAbsolutePath() normaliza con el caracter de la plataforma
        return path;
    }
}
