package itsnatspring;

import java.text.NumberFormat;
import java.util.Locale;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author jmarranz
 */
public class NumberFormatBean implements InitializingBean
{
    protected Locale locale;
    protected NumberFormat numberFormat;
    
    public NumberFormatBean() { }
    
    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }
    
    public NumberFormat getNumberFormat()
    {
        return numberFormat;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.numberFormat = NumberFormat.getInstance(locale);
    }
}
