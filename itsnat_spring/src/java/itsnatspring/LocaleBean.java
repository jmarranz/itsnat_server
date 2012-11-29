package itsnatspring;

import java.util.Locale;
import org.springframework.beans.factory.InitializingBean;

public class LocaleBean implements InitializingBean
{
    protected String language;
    protected String country;
    protected Locale locale;

    public LocaleBean() { }
    
    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public Locale getLocale()
    {
        return locale;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        Locale[] availablelocales = Locale.getAvailableLocales();
        for(Locale locale : availablelocales)
            if (locale.getLanguage().equals(language) && locale.getCountry().equals(country))
            {
                this.locale = locale;
                return;
            }        
        this.locale = new Locale(language,country);
    }
}
