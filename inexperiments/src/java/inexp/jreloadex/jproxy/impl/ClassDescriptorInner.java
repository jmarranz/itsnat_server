/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp.jreloadex.jproxy.impl;

/**
 *
 * @author jmarranz
 */
public class ClassDescriptorInner extends ClassDescriptor
{
    protected boolean local;
    
    public ClassDescriptorInner(String className) 
    {
        super(className);

        int pos = className.indexOf('$');
        SEGUIR definiendo local;
        // local = false;
    }      
    
    public boolean isInnerClass()
    {
        return true;
    }     
    
    public boolean isLocal()
    {
        return local;
    }
}
