package inexp.jreloadex.jproxy;

import java.lang.reflect.Method;

/**
 *
 * @author jmarranz
 */
public interface JProxyListener
{
    public void onReload(Object objOld,Object objNew,Object proxy, Method method, Object[] args);
}
