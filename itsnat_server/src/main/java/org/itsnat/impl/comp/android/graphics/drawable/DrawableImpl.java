/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/
package org.itsnat.impl.comp.android.graphics.drawable;

import org.itsnat.comp.android.graphics.drawable.Drawable;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.comp.android.view.ViewImpl;
import org.itsnat.impl.core.doc.droid.ItsNatStfulDroidDocumentImpl;

/**
 *
 * @author jmarranz
 */
public abstract class DrawableImpl implements Drawable
{
    protected ViewImpl parentView;
    protected String methodCalled;

    public DrawableImpl(ViewImpl parentView,String methodCalled)
    {
        this.parentView = parentView;
        this.methodCalled = methodCalled;
    }
    
    public ItsNatStfulDroidDocumentImpl getItsNatStfulDroidDocument()
    {
        return parentView.getItsNatStfulDroidDocument();
    }
    
    @Override
    public boolean setLevel(int level)
    {
        ItsNatStfulDroidDocumentImpl itsNatDoc = getItsNatStfulDroidDocument();
        ScriptUtil srcUtil = itsNatDoc.getScriptUtil();
        String viewRef = srcUtil.getNodeReference(parentView.getNode());
        String code = viewRef + "." + methodCalled + "().setLevel(" + level + ");";
        itsNatDoc.addCodeToSend(code);
        return false; // Por devolver algo
    }    
}
