
package org.apache.batik.bridge.resources;

/**
 * No se porqué pero Batik/Rhino hace requests al servidor buscando la clase
 * org.apache.batik.bridge.resources.Messages
 * (es decir buscando /org/apache/batik/bridge/resources/Messages.class)
 * cuando en dicho directorio está sólo Messages.properties
 *
 * De esta manera evitamos ese acceso.
 * 
 * @author jmarranz
 */
public interface Messages
{

}
