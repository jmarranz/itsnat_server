package inexp.jreloadex.jproxy.impl.comp;

import inexp.jreloadex.jproxy.impl.ClassDescriptor;
import inexp.jreloadex.jproxy.impl.ClassDescriptorSourceFile;
import inexp.jreloadex.jproxy.impl.JReloaderClassLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 *
 * @author jmarranz
 */
public class JReloaderCompilerInMemory
{
    protected JavaCompiler compiler;

    public JReloaderCompilerInMemory()
    {
        this.compiler = ToolProvider.getSystemJavaCompiler();
    }
    
    public void compileClass(ClassDescriptorSourceFile hotLoadClass,JReloaderClassLoader customClassLoader,Map<String,ClassDescriptorSourceFile> hotLoadableClasses)
    {
        File sourceFile = hotLoadClass.getSourceFile();
        LinkedList<JavaFileObjectOutputClass> outClassList = compile(sourceFile,customClassLoader,hotLoadableClasses);
        
        if (outClassList == null) throw new RuntimeException("Cannot reload class: " + hotLoadClass.getClassName());
        
        String className = hotLoadClass.getClassName();        
        
        // Puede haber más de un resultado cuando hay inner classes
        for(JavaFileObjectOutputClass outClass : outClassList)
        {
            String currClassName = outClass.binaryName();
            byte[] classBytes = outClass.getBytes();      
            ClassDescriptor innerClass = hotLoadClass.getInnerClassDescriptor(currClassName);
            if (innerClass != null)
            {
                innerClass.setClassBytes(classBytes);                       
            }
            else
            {
                if (!className.equals(currClassName))
                    throw new RuntimeException("Unexpected class " + currClassName);
                    
                hotLoadClass.setClassBytes(classBytes);                              
            }
        }
    }        
    
    private LinkedList<JavaFileObjectOutputClass> compile(File sourceFile,ClassLoader classLoader,Map<String,ClassDescriptorSourceFile> hotLoadableClasses)
    {
        return compile(sourceFile,classLoader,hotLoadableClasses,null);
    }

    private LinkedList<JavaFileObjectOutputClass> compile(File sourceFile,ClassLoader classLoader,Map<String,ClassDescriptorSourceFile> hotLoadableClasses,DiagnosticCollector<JavaFileObject> diagnostics)
    {
        // http://stackoverflow.com/questions/12173294/compiling-fully-in-memory-with-javax-tools-javacompiler
        // http://www.accordess.com/wpblog/an-overview-of-java-compilation-api-jsr-199/
        // http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/com/sun/tools/javac/util/JavacFileManager.java?av=h#JavacFileManager
        // http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7-b147/javax/tools/StandardLocation.java
        // http://books.brainysoftware.com/java6_sample/chapter2.pdf
        // http://atamur.blogspot.com.es/2009/10/using-built-in-javacompiler-with-custom.html
        // http://www.javablogging.com/dynamic-in-memory-compilation/ Si no queremos generar archivos
        // http://atamur.blogspot.com.es/2009/10/using-built-in-javacompiler-with-custom.html
        // http://stackoverflow.com/questions/264828/controlling-the-classpath-in-a-servlet?rq=1
        // http://stackoverflow.com/questions/1563909/how-to-set-classpath-when-i-use-javax-tools-javacompiler-compile-the-source
        // http://stackoverflow.com/questions/10767048/javacompiler-with-custom-classloader-and-filemanager

        List<File> sourceFileList = new ArrayList<File>();
        sourceFileList.add(sourceFile);

        StandardJavaFileManager fileManager = null;
        try
        {
            fileManager = compiler.getStandardFileManager(diagnostics, null, null);
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFileList);

            JavaFileManagerInMemory fileManagerInMemory = new JavaFileManagerInMemory(fileManager,classLoader,hotLoadableClasses);

            boolean success = compile(compilationUnits,fileManagerInMemory,diagnostics);
            if (!success) return null;

            LinkedList<JavaFileObjectOutputClass> classObj = fileManagerInMemory.getJavaFileObjectOutputClassList();
            return classObj;
        }
        finally
        {
           if (fileManager != null) try { fileManager.close(); } catch(IOException ex) { throw new RuntimeException(ex); }
        }
    }

    private boolean compile(Iterable<? extends JavaFileObject> compilationUnits,JavaFileManager fileManager,DiagnosticCollector<JavaFileObject> diagnostics)
    {
        boolean outError = false;
        if (diagnostics == null)
        {
            diagnostics = new DiagnosticCollector<JavaFileObject>();
            outError = true;
        }

        /*
        String systemClassPath = System.getProperty("java.class.path");
        String[] compileOptions = new String[]
            {"-classpath",engine.getPathSources()}; // No hacen falta los demás (sistema, Tomcat, /classes /lib etc) porque se obtienen via ClassLoader

        Iterable<String> compilationOptions = Arrays.asList(compileOptions);
        */

        Iterable<String> compilationOptions = null;

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, compilationOptions,null, compilationUnits);
        boolean success = task.call();

        if (outError)
        {
            List<Diagnostic<? extends JavaFileObject>> diagList = diagnostics.getDiagnostics();
            if (!diagList.isEmpty())
            {
                System.out.println("Problems compiling: " + compilationUnits);
                int i = 1;
                for (Diagnostic diagnostic : diagList)
                {
                   System.out.println(" Diagnostic " + i);
                   System.out.println("  code: " + diagnostic.getCode());
                   System.out.println("  kind: " + diagnostic.getKind());
                   System.out.println("  position: " + diagnostic.getPosition());
                   System.out.println("  start position: " + diagnostic.getStartPosition());
                   System.out.println("  end position: " + diagnostic.getEndPosition());
                   System.out.println("  source: " + diagnostic.getSource());
                   System.out.println("  message: " + diagnostic.getMessage(null));
                   i++;
                }
            }
        }

        return success;
    }


}
