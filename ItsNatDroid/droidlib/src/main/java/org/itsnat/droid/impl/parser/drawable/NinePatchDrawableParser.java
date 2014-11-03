package org.itsnat.droid.impl.parser.drawable;

import org.itsnat.droid.impl.model.ElementParsed;
import org.itsnat.droid.impl.model.ElementParsedDefault;

/**
 * Created by jmarranz on 31/10/14.
 */
public class NinePatchDrawableParser extends DrawableParser
{
    @Override
    protected ElementParsed createRootElement(String name)
    {
        return new ElementParsedDefault(name,null);
    }
}
