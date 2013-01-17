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

package org.itsnat.comp;

import org.itsnat.comp.button.normal.ItsNatHTMLAnchor;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchorLabel;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.button.normal.ItsNatHTMLButtonLabel;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.normal.ItsNatHTMLInputImage;
import org.itsnat.comp.button.normal.ItsNatHTMLInputReset;
import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
import org.itsnat.comp.iframe.ItsNatHTMLIFrame;
import org.itsnat.comp.label.ItsNatHTMLLabel;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.text.ItsNatHTMLInputFile;
import org.itsnat.comp.text.ItsNatHTMLInputHidden;
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.core.NameValue;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 * Utility interface to manage (create, register etc) ItsNat HTML based components of
 * the associated {@link org.itsnat.core.ItsNatDocument}.
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.core.html.ItsNatHTMLDocument#getItsNatHTMLComponentManager()
 */
public interface ItsNatHTMLComponentManager extends ItsNatComponentManager
{
    /**
     * Creates a new HTML label component.
     *
     * @param element the DOM element associated. If null a new label is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML label component.
     */
    public ItsNatHTMLLabel createItsNatHTMLLabel(HTMLLabelElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML anchor component.
     *
     * @param element the DOM element associated. If null a new anchor is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML anchor component.
     */
    public ItsNatHTMLAnchor createItsNatHTMLAnchor(HTMLAnchorElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML anchor component with label.
     *
     * @param element the DOM element associated. If null a new anchor is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML anchor component.
     */
    public ItsNatHTMLAnchorLabel createItsNatHTMLAnchorLabel(HTMLAnchorElement element,NameValue[] artifacts);


    /**
     * Creates a new HTML form component.
     *
     * @param element the DOM element associated. If null a new form is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML form component.
     */
    public ItsNatHTMLForm createItsNatHTMLForm(HTMLFormElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input text component. Text is not formatted.
     *
     * @param element the DOM element associated. If null a new input text is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input text (not formatter) component.
     */
    public ItsNatHTMLInputText createItsNatHTMLInputText(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input text component. Text is formatted using user defined formatters
     *
     * @param element the DOM element associated. If null a new input text is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input text (formatter) component.
     */
    public ItsNatHTMLInputTextFormatted createItsNatHTMLInputTextFormatted(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input password component.
     *
     * @param element the DOM element associated. If null a new input password is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input password component.
     */
    public ItsNatHTMLInputPassword createItsNatHTMLInputPassword(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input check box component.
     *
     * @param element the DOM element associated. If null a new input check box is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input check box component.
     */
    public ItsNatHTMLInputCheckBox createItsNatHTMLInputCheckBox(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input radio button component.
     *
     * @param element the DOM element associated. If null a new input radio is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input radio button component.
     */
    public ItsNatHTMLInputRadio createItsNatHTMLInputRadio(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input submit component.
     *
     * @param element the DOM element associated. If null a new input submit is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input submit component.
     */
    public ItsNatHTMLInputSubmit createItsNatHTMLInputSubmit(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input reset component.
     *
     * @param element the DOM element associated. If null a new input reset is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input reset component.
     */
    public ItsNatHTMLInputReset createItsNatHTMLInputReset(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input button component.
     *
     * @param element the DOM element associated. If null a new input button is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input button component.
     */
    public ItsNatHTMLInputButton createItsNatHTMLInputButton(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input image component.
     *
     * @param element the DOM element associated. If null a new input image is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input image component.
     */
    public ItsNatHTMLInputImage createItsNatHTMLInputImage(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input hidden component.
     *
     * @param element the DOM element associated. If null a new input hidden is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input hidden component.
     */
    public ItsNatHTMLInputHidden createItsNatHTMLInputHidden(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML input file component.
     *
     * @param element the DOM element associated. If null a new input file is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML input file component.
     */
    public ItsNatHTMLInputFile createItsNatHTMLInputFile(HTMLInputElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML select component as a list.
     *
     * @param element the DOM element associated. If null a new (empty) select is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML select component as a list.
     */
    public ItsNatHTMLSelectMult createItsNatHTMLSelectMult(HTMLSelectElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML select component as a combo box.
     *
     * @param element the DOM element associated. If null a new (empty) select is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML select component as a combo box.
     */
    public ItsNatHTMLSelectComboBox createItsNatHTMLSelectComboBox(HTMLSelectElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML text area component.
     *
     * @param element the DOM element associated. If null a new textarea is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML text area component.
     */
    public ItsNatHTMLTextArea createItsNatHTMLTextArea(HTMLTextAreaElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML button component.
     *
     * @param element the DOM element associated. If null a new button is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML button component.
     */
    public ItsNatHTMLButton createItsNatHTMLButton(HTMLButtonElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML button component with a label.
     *
     * @param element the DOM element associated. If null a new button is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML button component.
     */
    public ItsNatHTMLButtonLabel createItsNatHTMLButtonLabel(HTMLButtonElement element,NameValue[] artifacts);

    /**
     * Creates a new HTML table component.
     *
     * @param element the DOM element associated. If null a new (empty) table is created.
     * @param structure the specified structure, if null then is obtained from artifacts ("useStructure"), if not defined then from markup ("useStructure" attribute), if not defined the default structure is used.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML table component.
     */
    public ItsNatHTMLTable createItsNatHTMLTable(HTMLTableElement element,ItsNatTableStructure structure,NameValue[] artifacts);

    /**
     * Creates a new HTML iframe component. 
     *
     * @param element the DOM element associated. If null a new iframe is created.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new HTML iframe component.
     */
    public ItsNatHTMLIFrame createItsNatHTMLIFrame(HTMLIFrameElement element,NameValue[] artifacts);
}
