/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.shared;

/**
 * Esta clase está en shared para evitar que se recargue por parte de RelProxy, pues si se recarga se redefine de nuevo el atributo enable y pierde el valor dado
 * 
 * @author jmarranz
 */
public class TestSerializationConfig {
    
    public static boolean enable; // Este test produce una enorme pérdida de rendimiento    
}
