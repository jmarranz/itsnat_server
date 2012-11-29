package itsnatspring;

import java.text.DateFormat;
import java.util.Locale;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author jmarranz
 */
public class DateFormatBean implements InitializingBean
{
    protected int style;
    protected Locale locale;
    protected DateFormat dateFormat;
    
    public DateFormatBean() { }
    
    public int getStyle()
    {
        return style;
    }

    public void setStyle(int style)
    {
        this.style = style;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }
    
    public DateFormat getDateFormat()
    {
        return dateFormat;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.dateFormat = DateFormat.getDateInstance(style, locale);
    }
}
