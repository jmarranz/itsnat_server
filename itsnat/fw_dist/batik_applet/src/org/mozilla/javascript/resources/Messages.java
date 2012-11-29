
package org.mozilla.javascript.resources;

/**
 * No se porqué pero Batik/Rhino hace requests al servidor buscando la clase
 * org.mozilla.javascript.resources.Messages
 * (es decir buscando /org/mozilla/javascript/resources/Messages.class)
 * cuando en dicho directorio está Messages.properties (y la versión _fr en francés)
 *
 * De esta manera evitamos ese acceso.
 * 
 * @author jmarranz
 */
public interface Messages
{

}
