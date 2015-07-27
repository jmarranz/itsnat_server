/*
 * CodeInManual.java
 *
 * Created on 27 de abril de 2007, 22:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import manual.comp.CityListCustomStructure;
import manual.comp.custom.LoginComponent;
import manual.comp.custom.LoginCreationItsNatComponentListener;
import manual.comp.custom.ValidateLoginListener;
import manual.comp.labels.PersonCustomLabelEditor;
import manual.comp.labels.PersonCustomLabelRenderer;
import manual.comp.lists.ComboBoxSelectionDecorator;
import manual.comp.lists.ListSelectionDecorator;
import manual.comp.tables.TableRowSelectionDecoration;
import manual.comp.trees.FreeTreeDecorator;
import org.itsnat.comp.button.ItsNatButtonGroup;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatFormattedTextField.ItsNatFormatter;
import org.itsnat.comp.text.ItsNatFormatterFactoryDefault;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormal;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.comp.inc.ItsNatFreeInclude;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButton;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.comp.label.ItsNatLabel;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.comp.label.ItsNatLabelRenderer;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchor;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.comp.ItsNatHTMLInput;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
import org.itsnat.comp.iframe.FileUploadRequest;
import org.itsnat.comp.iframe.HTMLIFrameFileUpload;
import org.itsnat.comp.iframe.ItsNatHTMLIFrame;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.comp.label.ItsNatHTMLLabel;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.text.ItsNatHTMLInputFile;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.CommMode;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.core.NameValue;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import test.web.comp.Person;

/**
 *
 * @author jmarranz
 */
public class CodeInManualComp
{
    public CodeInManualComp()
    {
    }

    public static ServletContext getServletContext()
    {
        return null;
    }

    public static ItsNatHttpServlet getItsNatHttpServlet()
    {
        return null;
    }


    // COMPONENTS

    
    public static void LIFE_CYCLE()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        ItsNatHTMLAnchor linkComp = (ItsNatHTMLAnchor)componentMgr.createItsNatComponentById("linkId");

        componentMgr.addItsNatComponent(linkComp);

        linkComp = (ItsNatHTMLAnchor)componentMgr.addItsNatComponentById("linkId");

        linkComp = (ItsNatHTMLAnchor)componentMgr.findItsNatComponentById("linkId");
    }

    public static void LIFE_CYCLE_2()
    {
        ItsNatComponentManager componentMgr = null;

        ItsNatFreeButtonNormal buttonComp = (ItsNatFreeButtonNormal)componentMgr.createItsNatComponentById("buttonId","freeButtonNormal",null);
    }

    public static void LIFE_CYCLE_3()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        componentMgr.buildItsNatComponents(doc.getDocumentElement());

        ItsNatFreeButtonNormal buttonComp = (ItsNatFreeButtonNormal)componentMgr.findItsNatComponentById("buttonId");
    }

    public static void LIFE_CYCLE_4()
    {
        ItsNatFreeButtonNormal buttonComp = null;

        buttonComp.dispose();

        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        componentMgr.removeItsNatComponents(doc.getDocumentElement(),true);
    }

    public static void DOM_EVENTS()
    {
        ItsNatFreeButtonNormal buttonComp = null;

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                System.out.println("Event " + evt.getType());
            }

        };
        buttonComp.addEventListener("click",listener);

        buttonComp.disableEventListener("click");
        buttonComp.enableEventListener("mousedown");
        buttonComp.enableEventListener("mouseup");
    }

    public static void BUTTONS()
    {
        javax.swing.ButtonModel dataModel = null;
        Event evt = null;

        String type = evt.getType();

        if (type.equals("click"))
        {
            dataModel.setArmed(true);
            dataModel.setPressed(true);
            dataModel.setPressed(false);
            dataModel.setArmed(false);
        }
        else if (type.equals("mousedown"))
        {
            dataModel.setArmed(true);
            dataModel.setPressed(true);
        }
        else if (type.equals("mouseup"))
        {
            dataModel.setPressed(false);
            dataModel.setArmed(false);
        }
        else if (type.equals("mouseover"))
        {
            dataModel.setRollover(true);
        }
        else if (type.equals("mouseout"))
        {
            dataModel.setRollover(false);
            dataModel.setArmed(false);
        }
    }

    public static void BUTTONS_NORMAL_BUTTONS()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        ItsNatHTMLAnchor linkComp = (ItsNatHTMLAnchor)componentMgr.createItsNatComponentById("linkId");

        EventListener evtListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                System.out.println("Clicked :" + evt.getCurrentTarget());
            }
        };
        linkComp.addEventListener("click",evtListener);

        ButtonModel dataModel = linkComp.getButtonModel();

        ActionListener actListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Clicked :" + e);
            }
        };
        dataModel.addActionListener(actListener);

        ChangeListener chgListener = new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                System.out.println("Button state has changed:");
                ButtonModel model = (ButtonModel)e.getSource();

                String fact = "";
                if (model.isArmed())
                    fact += "armed ";
                if (model.isPressed())
                    fact += "pressed ";
                if (model.isRollover())
                    fact += "rollover ";
                if (model.isSelected()) // ever false with normal buttons
                    fact += "selected ";

                if (!fact.equals(""))
                    System.out.println(fact);
            }
        };
        dataModel.addChangeListener(chgListener);
    }

    public static void BUTTONS_TOGGLE_BUTTONS()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        final ItsNatHTMLInputRadio inputComp1 = (ItsNatHTMLInputRadio)componentMgr.createItsNatComponentById("inputId1");
        final ItsNatHTMLInputRadio inputComp2 = (ItsNatHTMLInputRadio)componentMgr.createItsNatComponentById("inputId2");

        EventListener evtListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                EventTarget currentTarget = evt.getCurrentTarget();
                String button;
                if (currentTarget == inputComp1.getHTMLInputElement())
                    button = "button 1";
                else
                    button = "button 2";

                System.out.println("Clicked " + button);
            }
        };

        inputComp1.addEventListener("click",evtListener);
        inputComp2.addEventListener("click",evtListener);

        ItsNatButtonGroup itsNatGrp1 = inputComp1.getItsNatButtonGroup();
        ItsNatButtonGroup itsNatGrp2 = inputComp2.getItsNatButtonGroup();
        if ( ((itsNatGrp1 == null) || (itsNatGrp2 == null)) ||
             (itsNatGrp1.getButtonGroup() != itsNatGrp2.getButtonGroup()) )
        {
            ButtonGroup group = new ButtonGroup();
            ItsNatButtonGroup htmlGroup = componentMgr.getItsNatButtonGroup(group);
            htmlGroup.addButton(inputComp1);
            htmlGroup.addButton(inputComp2);
        }

        ToggleButtonModel dataModel1 = inputComp1.getToggleButtonModel();
        ToggleButtonModel dataModel2 = inputComp2.getToggleButtonModel();

        ActionListener actListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String button;
                if (e.getSource() == inputComp1.getToggleButtonModel())
                    button = "button 1";
                else
                    button = "button 2";

                System.out.println("Clicked :" + button);
            }
        };
        dataModel1.addActionListener(actListener);
        dataModel2.addActionListener(actListener);

        ItemListener itemListener = new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                String fact;
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED)
                    fact = "selected";
                else
                    fact = "deselected";
                fact += " button ";
                if (e.getItem() == inputComp1.getToggleButtonModel())
                    fact += "1";
                else
                    fact += "2";

                System.out.println(fact);
            }
        };
        dataModel1.addItemListener(itemListener);
        dataModel2.addItemListener(itemListener);
    }

    public static void BUTTONS_TOGGLE_BUTTONS2()
    {
        final ItsNatFreeRadioButton buttonComp1 = null;
        final ItsNatFreeRadioButton buttonComp2 = null;

        ItemListener itemListener = new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                int state = e.getStateChange();

                if (e.getItem() == buttonComp1.getToggleButtonModel())
                    updateDecoration(buttonComp1,state);
                else
                    updateDecoration(buttonComp2,state);
            }

            public void updateDecoration(ItsNatFreeRadioButton buttonComp,int state)
            {
                Element buttonElem = buttonComp.getElement();
                ToggleButtonModel model = buttonComp.getToggleButtonModel();
                if (state == ItemEvent.SELECTED) // or with: (model.isSelected())
                    buttonElem.setAttribute("style","background: rgb(253,147,173);");
                else
                    buttonElem.removeAttribute("style");
            }
        };

    }

    public static void TEXT_BASED_COMPONENTS()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        final ItsNatHTMLInputText inputComp = (ItsNatHTMLInputText)componentMgr.createItsNatComponentById("inputId");
        inputComp.setText("Change this text and lost the focus");

        EventListener evtListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                System.out.println("Text changed: " + inputComp.getHTMLInputElement().getValue());
                // Alternative: inputComp.getText();
            }
        };
        inputComp.addEventListener("change",evtListener);

        DocumentListener docListener = new DocumentListener()
        {
            public void insertUpdate(DocumentEvent e)
            {
                javax.swing.text.Document docModel = e.getDocument();
                int offset = e.getOffset();
                int len = e.getLength();

                try
                {
                    System.out.println("Inserted, pos " + offset + "," + len + " chars,\"" + docModel.getText(offset,len) + "\"");
                }
                catch(BadLocationException ex)
                {
                    throw new RuntimeException(ex);
                }
            }

            public void removeUpdate(DocumentEvent e)
            {
                javax.swing.text.Document docModel = e.getDocument();
                int offset = e.getOffset();
                int len = e.getLength();

                System.out.println("Removedm, pos " + offset + "," + len + " chars");
            }

            public void changedUpdate(DocumentEvent e)
            {
                // A PlainDocument has no attributes
            }
        };
        PlainDocument dataModel = (PlainDocument)inputComp.getDocument();
        dataModel.addDocumentListener(docListener);

        inputComp.focus();
        inputComp.select();
    }

    public static void TEXT_BASED_COMPONENTS_FORMATTED()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        ItsNatHTMLInputTextFormatted inputComp = (ItsNatHTMLInputTextFormatted)componentMgr.createItsNatComponentById("inputId","formattedTextField",null);
        try{ inputComp.setValue(new Date()); } catch(PropertyVetoException ex) { throw new RuntimeException(ex); }

        PropertyChangeListener propListener = new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                Date value = (Date)evt.getNewValue();
                System.out.println("Value changed to: " + value);
            }
        };
        inputComp.addPropertyChangeListener("value",propListener);

        VetoableChangeListener vetoListener = new VetoableChangeListener()
        {
            public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException
            {
                Date newDate = (Date)evt.getNewValue();
                if (newDate.compareTo(new Date()) > 0)
                    throw new PropertyVetoException("Future date is not allowed",evt);
            }
        };
        inputComp.addVetoableChangeListener(vetoListener);
    }

    public static void TEXT_BASED_COMPONENTS_FORMATTED_2()
    {
        ItsNatHTMLInputTextFormatted inputComp = null;

        ItsNatFormatterFactoryDefault factory = (ItsNatFormatterFactoryDefault)inputComp.createDefaultItsNatFormatterFactory();

        ItsNatFormatter dispFormatter = inputComp.createItsNatFormatter(DateFormat.getDateInstance(DateFormat.LONG,Locale.US));
        factory.setDisplayFormatter(dispFormatter);
        ItsNatFormatter editFormatter = inputComp.createItsNatFormatter(DateFormat.getDateInstance(DateFormat.SHORT,Locale.US));
        factory.setEditFormatter(editFormatter);

        inputComp.setItsNatFormatterFactory(factory);

        try{ inputComp.setValue(new Date()); } catch(PropertyVetoException ex) { throw new RuntimeException(ex); }
    }

    public static void LABELS()
    {
        ItsNatHTMLDocument itsNatDoc = null;
        ItsNatHTMLComponentManager componentMgr = itsNatDoc.getItsNatHTMLComponentManager();

        ItsNatHTMLLabel label = (ItsNatHTMLLabel)componentMgr.createItsNatComponentById("labelId");
        try { label.setValue(new Integer(3));  } // Initial value
        catch(PropertyVetoException ex) { throw new RuntimeException(ex); }

        ItsNatHTMLSelectComboBox editorComp = componentMgr.createItsNatHTMLSelectComboBox(null,null);
        DefaultComboBoxModel model = (DefaultComboBoxModel)editorComp.getComboBoxModel();
        for(int i=0; i < 5; i++) model.addElement(new Integer(i));

        ItsNatLabelEditor editor = componentMgr.createDefaultItsNatLabelEditor(editorComp);
        label.setItsNatLabelEditor(editor);

        EventListener evtListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                System.out.println("Edition starts...");
            }
        };
        label.addEventListener("dblclick",evtListener);

        PropertyChangeListener propListener = new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Changed label, old: " + evt.getOldValue() + ", new: " + evt.getNewValue());
            }
        };
        label.addPropertyChangeListener("value",propListener);
    }

    public static void LABELS_CUSTOM_EDITOR()
    {
        ItsNatHTMLDocument itsNatDoc = null;
        ItsNatHTMLComponentManager componentMgr = itsNatDoc.getItsNatHTMLComponentManager();

        ItsNatFreeLabel comp = (ItsNatFreeLabel)componentMgr.createItsNatComponentById("labelId","freeLabel",null);
        try { comp.setValue(new Person("Jose M.","Arranz")); }
        catch(PropertyVetoException ex) { throw new RuntimeException(ex); }

        ItsNatLabelEditor editor = new PersonCustomLabelEditor();
        comp.setItsNatLabelEditor(editor);
    }

    public static void LABELS_CUSTOM_RENDERER()
    {
    ItsNatHTMLDocument itsNatDoc = null;
    ItsNatHTMLComponentManager componentMgr = itsNatDoc.getItsNatHTMLComponentManager();

    ItsNatFreeLabel comp = (ItsNatFreeLabel)componentMgr.createItsNatComponentById("labelId","freeLabel",null);

    ItsNatLabelRenderer renderer = new PersonCustomLabelRenderer();
    comp.setItsNatLabelRenderer(renderer);

    try { comp.setValue(new Person("Jose M.","Arranz")); }
    catch(PropertyVetoException ex) { throw new RuntimeException(ex); }

    // ...

    }

    public static void LISTS_COMBO_BOXES()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        ItsNatHTMLSelectComboBox comboComp = (ItsNatHTMLSelectComboBox)componentMgr.createItsNatComponentById("compId");

        DefaultComboBoxModel dataModel = (DefaultComboBoxModel)comboComp.getComboBoxModel();
        dataModel.addElement("Madrid");
        dataModel.addElement("Sevilla");
        dataModel.addElement("Segovia");
        dataModel.addElement("Barcelona");
        dataModel.addElement("Oviedo");
        dataModel.addElement("Valencia");

        dataModel.setSelectedItem("Segovia");

        EventListener evtListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                System.out.println(evt.getCurrentTarget() + " " + evt.getType());
            }
        };
        comboComp.addEventListener("change",evtListener);

        ListDataListener dataListener = new ListDataListener()
        {
            public void intervalAdded(ListDataEvent e)
            {
                listChangedLog(e);
            }

            public void intervalRemoved(ListDataEvent e)
            {
                listChangedLog(e);
            }

            public void contentsChanged(ListDataEvent e)
            {
                listChangedLog(e);
            }

            public void listChangedLog(ListDataEvent e)
            {
                int index0 = e.getIndex0();
                int index1 = e.getIndex1();

                String action = "";
                int type = e.getType();
                switch(type)
                {
                    case ListDataEvent.INTERVAL_ADDED:   action = "Added"; break;
                    case ListDataEvent.INTERVAL_REMOVED: action = "Removed"; break;
                    case ListDataEvent.CONTENTS_CHANGED: action = "Changed"; break;
                }

                String interval = "";
                if (index0 != -1)
                    interval = " interval " + index0 + "-" + index1;

                System.out.println(action + " " + interval);
            }
        };
        dataModel.addListDataListener(dataListener);

        ItemListener itemListener = new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                String fact;
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED)
                    fact = "Selected";
                else
                    fact = "Deselected";

                System.out.println(fact + " " + e.getItem());
            }
        };
        comboComp.addItemListener(itemListener);
    }

    public static void LISTS_COMBO_BOXES_2()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        ItsNatFreeComboBox comboComp = (ItsNatFreeComboBox)componentMgr.createItsNatComponentById("compId","freeComboBox",null);

        DefaultComboBoxModel dataModel = (DefaultComboBoxModel)comboComp.getComboBoxModel();

        // ...
        comboComp.addItemListener(new ComboBoxSelectionDecorator(comboComp));

        dataModel.setSelectedItem("Segovia");

        EventListener evtListener = null;
         // ...
        comboComp.addEventListener("click",evtListener);
    }

    public static void LISTS_MULTIPLE_SELECTION()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        ItsNatHTMLSelectMult listComp = (ItsNatHTMLSelectMult)componentMgr.createItsNatComponentById("compId");

        DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
        dataModel.addElement("Madrid");
        dataModel.addElement("Sevilla");
        dataModel.addElement("Segovia");
        dataModel.addElement("Barcelona");
        dataModel.addElement("Oviedo");
        dataModel.addElement("Valencia");

        ListSelectionModel selModel = listComp.getListSelectionModel();
        selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        selModel.setSelectionInterval(2,3);

        EventListener evtListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                System.out.println(evt.getCurrentTarget() + " " + evt.getType());
            }
        };
        listComp.addEventListener("change",evtListener);

        ListDataListener dataListener = new ListDataListener()
        {
            public void intervalAdded(ListDataEvent e)
            {
                listChangedLog(e);
            }

            public void intervalRemoved(ListDataEvent e)
            {
                listChangedLog(e);
            }

            public void contentsChanged(ListDataEvent e)
            {
                listChangedLog(e);
            }

            public void listChangedLog(ListDataEvent e)
            {
                int index0 = e.getIndex0();
                int index1 = e.getIndex1();

                String action = "";
                int type = e.getType();
                switch(type)
                {
                    case ListDataEvent.INTERVAL_ADDED:   action = "Added"; break;
                    case ListDataEvent.INTERVAL_REMOVED: action = "Removed"; break;
                    case ListDataEvent.CONTENTS_CHANGED: action = "Changed"; break;
                }

                String interval = "";
                if (index0 != -1)
                    interval = " interval " + index0 + "-" + index1;

                System.out.println(action + " " + interval);
            }
        };
        dataModel.addListDataListener(dataListener);

        ListSelectionListener selListener = new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting())
                    return;

                int first = e.getFirstIndex();
                int last = e.getLastIndex();

                ListSelectionModel selModel = (ListSelectionModel)e.getSource();
                String fact = "";
                for(int i = first; i <= last; i++)
                {
                    boolean selected = selModel.isSelectedIndex(i);
                    if (selected)
                        fact += ", selected ";
                    else
                        fact += ", deselected ";
                    fact += i;
                }

                System.out.println("Selection changes" + fact);
            }
        };
        selModel.addListSelectionListener(selListener);
    }

    public static void LISTS_MULTIPLE_SELECTION_2()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        ItsNatFreeListMultSel listComp = (ItsNatFreeListMultSel)componentMgr.createItsNatComponentById("compId","freeList",null);

        DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
        // ...

        ListSelectionModel selModel = listComp.getListSelectionModel();
        selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        selModel.addListSelectionListener(new ListSelectionDecorator(listComp));

        selModel.setSelectionInterval(2,3);

        EventListener evtListener = null;
        // ...
        listComp.addEventListener("click",evtListener);
        // ...
    }

    public static void LISTS_USER_DEFINED_STRUCTURES()
    {
        ItsNatComponentManager componentMgr = null;

        ItsNatListStructure struct = new ItsNatListStructure()
        {
            public Element getContentElement(ItsNatList list, int index, Element parentElem)
            {
                HTMLTableRowElement rowElem = (HTMLTableRowElement)parentElem;
                HTMLTableCellElement firstCell = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
                HTMLTableCellElement secondCell = (HTMLTableCellElement)ItsNatTreeWalker.getNextSiblingElement(firstCell);
                return secondCell;
            }
        };

        NameValue[] artifacts = new NameValue[]{ new NameValue("useStructure",struct) };
        ItsNatFreeListMultSel listComp = (ItsNatFreeListMultSel)componentMgr.createItsNatComponentById("compId","freeList",artifacts);
    }

    public static void TABLES()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        ItsNatHTMLTable tableComp = (ItsNatHTMLTable)componentMgr.createItsNatComponentById("compId");

        DefaultTableModel dataModel = (DefaultTableModel)tableComp.getTableModel();
        dataModel.addColumn("City");
        dataModel.addColumn("Public square");
        dataModel.addColumn("Monument");
        dataModel.addRow(new String[] {"Madrid","Plaza Mayor","Palacio Real"});
        dataModel.addRow(new String[] {"Sevilla","Plaza de España","Giralda"});
        dataModel.addRow(new String[] {"Segovia","Plaza del Azoguejo","Acueducto Romano"});

        ListSelectionModel rowSelModel = tableComp.getRowSelectionModel();
        ListSelectionModel columnSelModel = tableComp.getColumnSelectionModel();

        rowSelModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        columnSelModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        tableComp.setRowSelectionAllowed(true);
        tableComp.setColumnSelectionAllowed(false);

        rowSelModel.addListSelectionListener(new TableRowSelectionDecoration(tableComp));

        rowSelModel.setSelectionInterval(1,1);

        TableModelListener dataListener = new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                int firstRow = e.getFirstRow();
                int lastRow = e.getLastRow();

                String action = "";
                int type = e.getType();
                switch(type)
                {
                    case TableModelEvent.INSERT: action = "Added"; break;
                    case TableModelEvent.DELETE: action = "Removed"; break;
                    case TableModelEvent.UPDATE: action = "Changed"; break;
                }

                String interval = "";
                if (firstRow != -1)
                    interval = " interval " + firstRow + "-" + lastRow;

                System.out.println(action + " " + interval);
            }
        };
        dataModel.addTableModelListener(dataListener);

        ListSelectionListener rowSelListener = new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting())
                    return;

                ListSelectionModel rowSelModel = (ListSelectionModel)e.getSource();

                int first = e.getFirstIndex();
                int last = e.getLastIndex();

                String fact = "";
                for(int i = first; i <= last; i++)
                {
                    boolean selected = rowSelModel.isSelectedIndex(i);
                    if (selected)
                        fact += ", selected ";
                    else
                        fact += ", deselected ";
                    fact += i;
                }

                System.out.println("Selection changes" + fact);
            }
        };
        rowSelModel.addListSelectionListener(rowSelListener);
    }

    public static void TREES()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        ItsNatFreeTree treeComp = (ItsNatFreeTree)componentMgr.createItsNatComponentById("compId","freeTree",null);

        new FreeTreeDecorator(treeComp).bind();

        DefaultTreeModel dataModel = (DefaultTreeModel)treeComp.getTreeModel();

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Grey’s Anatomy");
        dataModel.setRoot(rootNode);

        DefaultMutableTreeNode parentNode;

            parentNode = addNode("Characters",rootNode,dataModel);

                addNode("Meredith Grey",parentNode,dataModel);
                addNode("Cristina Yang",parentNode,dataModel);
                addNode("Alex Karev",parentNode,dataModel);
                addNode("George O'Malley",parentNode,dataModel);

            parentNode = addNode("Actors",rootNode,dataModel);

                addNode("Ellen Pompeo",parentNode,dataModel);
                addNode("Sandra Oh",parentNode,dataModel);
                addNode("Justin Chambers",parentNode,dataModel);
                addNode("T.R. Knight",parentNode,dataModel);

        TreeSelectionModel selModel = treeComp.getTreeSelectionModel();
        selModel.setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);

        selModel.addSelectionPath(new TreePath(parentNode.getPath())); // Actors

        TreeModelListener dataListener = new TreeModelListener()
        {
            public void treeNodesChanged(TreeModelEvent e)
            {
                treeChangedLog(e);
            }

            public void treeNodesInserted(TreeModelEvent e)
            {
                treeChangedLog(e);
            }

            public void treeNodesRemoved(TreeModelEvent e)
            {
                treeChangedLog(e);
            }

            public void treeStructureChanged(TreeModelEvent e)
            {
                treeChangedLog(e);
            }

            public void treeChangedLog(TreeModelEvent e)
            {
                System.out.println(e.toString());
            }
        };
        dataModel.addTreeModelListener(dataListener);

        TreeSelectionListener selListener = new TreeSelectionListener()
        {
            public void valueChanged(TreeSelectionEvent e)
            {
                TreeSelectionModel selModel = (TreeSelectionModel)e.getSource();

                TreePath[] paths = e.getPaths();
                String fact = "";
                for(int i = 0; i < paths.length; i++)
                {
                    TreePath path = paths[i];
                    boolean selected = selModel.isPathSelected(path);
                    if (selected)
                        fact += ", selected ";
                    else
                        fact += ", deselected ";
                    fact += path.getLastPathComponent();
                }

                System.out.println("Selection changes" + fact);
            }
        };
        selModel.addTreeSelectionListener(selListener);

        TreeWillExpandListener willExpandListener = new TreeWillExpandListener()
        {
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException
            {
                // Will expand
            }

            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException
            {
                // Will collapse
            }
        };
        treeComp.addTreeWillExpandListener(willExpandListener);
    }

    public static DefaultMutableTreeNode addNode(Object userObject,DefaultMutableTreeNode parentNode,DefaultTreeModel dataModel)
    {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(userObject);
        int count = dataModel.getChildCount(parentNode);
        dataModel.insertNodeInto(childNode,parentNode,count);
        return childNode;
    }

    public static void TREES_2()
    {
        TreeWillExpandListener willExpandListener = new TreeWillExpandListener()
        {
            public void treeWillExpand(TreeExpansionEvent event)
                            throws ExpandVetoException
            {
                // Will expand
            }

            public void treeWillCollapse(TreeExpansionEvent event)
                            throws ExpandVetoException
            {
                throw new ExpandVetoException(event);
            }
        };
    }

    public static void TREES_ROOTLESS()
    {
        ItsNatComponentManager compMgr = null;

        NameValue[] artifacts = new NameValue[]{new NameValue("rootless","true")};
        ItsNatFreeTree treeComp = (ItsNatFreeTree)compMgr.createItsNatComponentById("compId","freeTree",artifacts);
    }

    public static void TREES_TREE_TABLE()
    {
        ItsNatComponentManager compMgr = null;

        NameValue[] artifacts = new NameValue[]{new NameValue("treeTable","true")};
        ItsNatFreeTree treeComp = (ItsNatFreeTree)compMgr.createItsNatComponentById("compId","freeTree",artifacts);
    }

    public static void FORMS()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        final ItsNatHTMLForm formComp = (ItsNatHTMLForm)componentMgr.createItsNatComponentById("formId");

        formComp.setEventListenerParams("submit",false,CommMode.XHR_SYNC,null,null,-1);
        formComp.setEventListenerParams("reset",false,CommMode.XHR_SYNC,null,null,-1);

        EventListener evtListener = new EventListener()
        {
           public void handleEvent(Event evt)
            {
                System.out.println(evt.getCurrentTarget() + " " + evt.getType());

                EventTarget currentTarget = evt.getCurrentTarget();
                if (currentTarget == formComp.getHTMLFormElement())
                {
                    if (evt.getType().equals("submit"))
                    {
                        System.out.println("Submit canceled");
                        evt.preventDefault(); // Cancels the submission, only works in SYNC mode
                    }
                    // reset is not cancellable
                }
                else // Link
                {
                    formComp.reset(); // submit() method is defined too
                }
           }
        };
        formComp.addEventListener("submit",evtListener);
        formComp.addEventListener("reset",evtListener);

        ItsNatHTMLAnchor linkComp = (ItsNatHTMLAnchor)componentMgr.createItsNatComponentById("linkId");
        linkComp.addEventListener("click",evtListener);
    }

    public static void INCLUDES()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        final ItsNatFreeInclude includeComp = (ItsNatFreeInclude)componentMgr.createItsNatComponentById("includeId","freeInclude",null);

        final ItsNatHTMLInput buttonComp = (ItsNatHTMLInput)componentMgr.createItsNatComponentById("buttonId");

        EventListener evtListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                if (includeComp.isIncluded())
                    uninclude();
                else
                    include();
            }

            public void include()
            {
                includeComp.includeFragment("feashow.fragmentExample",true);
                buttonComp.getHTMLInputElement().setValue("Remove");
            }

            public void uninclude()
            {
                includeComp.removeFragment();
                buttonComp.getHTMLInputElement().setValue("Include");
            }
        };
        buttonComp.addEventListener("click",evtListener);
    }

    public static void FILE_UPLOAD()
    {
final ItsNatDocument itsNatDoc = null;
Document doc = itsNatDoc.getDocument();

ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
final ItsNatHTMLInputFile input = (ItsNatHTMLInputFile)compMgr.createItsNatComponentById("fileUploadInputId");
final ItsNatHTMLIFrame iframe = (ItsNatHTMLIFrame)compMgr.createItsNatComponentById("fileUploadIFrameId");
final Element progressElem = doc.getElementById("progressId");

ItsNatHTMLButton button = (ItsNatHTMLButton)compMgr.createItsNatComponentById("fileUploadButtonId");
EventListener listener = new EventListener()
{
    public void handleEvent(Event evt)
    {
        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();
        ItsNatTimer timer = clientDoc.createItsNatTimer();
        EventListener timerListener = new EventListener() {
            public void handleEvent(Event evt) { }   // Nothing to do, this timer just update the client with the current state of progressElem
        };
        final ItsNatTimerHandle timerHnd = timer.schedule(null,timerListener,0,1000);

        final HTMLIFrameFileUpload iframeUpload = iframe.getHTMLIFrameFileUpload(clientDoc,input.getHTMLInputElement());

        ItsNatServletRequestListener listener = new ItsNatServletRequestListener()
        {
            public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
            {
                FileUploadRequest fileUpReq = iframeUpload.processFileUploadRequest(request, response);

                try
                {
                    ServletResponse servRes = response.getServletResponse();
                    Writer out = servRes.getWriter();
                    out.write("<html><head /><body>");
                    out.write("<p>Content Type: \"" + fileUpReq.getContentType() + "\"</p>");
                    out.write("<p>Field Name: \"" + fileUpReq.getFieldName() + "\"</p>");
                    out.write("<p>File Name: \"" + fileUpReq.getFileName() + "\"</p>");
                    out.write("<p>File Size: " + fileUpReq.getFileSize() + "</p>");
                    out.write("</body></html>");

                    long fileSize = fileUpReq.getFileSize();
                    if (fileSize == 0) return;

                    byte[] buffer = new byte[10*1024];
                    InputStream fileUp = fileUpReq.getFileUploadInputStream();
                    long count = 0;
                    int read = 0;
                    do
                    {
                        if (iframeUpload.isDisposed())
                            return;

                        try { Thread.sleep(50); }catch(InterruptedException ex){ }
                        count += read;

                        long per = (count * 100) / fileSize;
                        synchronized(itsNatDoc)
                        {
                            Text text = (Text)progressElem.getFirstChild();
                            text.setData(String.valueOf(per));
                        }

                        read = fileUp.read(buffer);
                    }
                    while (read != -1);
                }
                catch(IOException ex)
                {
                    throw new RuntimeException(ex);
                }
                finally
                {
                    synchronized(itsNatDoc)
                    {
                        timerHnd.cancel();
                    }
                }
            }
        };
        iframeUpload.addItsNatServletRequestListener(listener);
    }
};
button.addEventListener("click", listener);

    }


    public static void MODAL_LAYERS()
    {
        final ItsNatDocument itsNatDoc = null;
        float opacity = (float)0.1; // Semitransparent
        String background = "black";

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        final ItsNatModalLayer modalLayer = compMgr.createItsNatModalLayer(null,false,opacity,background,null);
        int zIndex = modalLayer.getZIndex();

        // Note:  right:X%; alongside width and left fools BlackBerry
        String code = "<p style='position:absolute; z-index:" + zIndex + "; background:yellow; width:70%; height:70%; left:15%; top:15%; padding:10px;'>" +
                      "<b>Modal Layer 2</b><br /><br />" +
                      "<a href='javascript:;'>Click To Exit</a>" +
                      "</p>";

        final HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        DocumentFragment frag = itsNatDoc.toDOM(code);
        final Element elem = (Element)frag.getFirstChild();
        doc.getBody().appendChild(elem);

        NodeList links = elem.getElementsByTagName("a");
        final HTMLAnchorElement linkExit = (HTMLAnchorElement)links.item(0);

        EventListener listenerExit = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ((EventTarget)linkExit).removeEventListener("click",this,false);
                doc.getBody().removeChild(elem);
                modalLayer.dispose();
            }
        };

        ((EventTarget)linkExit).addEventListener("click",listenerExit,false);
    }

    public static void Modal_Layers_Detection_of_unexpected_events()
    {
    final ItsNatDocument itsNatDoc = null;
    ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
    final ItsNatModalLayer modalLayer = compMgr.createItsNatModalLayer(null,false,0.1f,"black",null);

    EventListener listener = new EventListener()
    {
        public void handleEvent(Event evt)
        {
            StringBuilder code = new StringBuilder();
            code.append("if (confirm('Received an unexpected event by a hidden element. Reload?')) ");
            code.append("  window.location.reload(true);");

            itsNatDoc.addCodeToSend(code.toString());
        }
    };
    modalLayer.addUnexpectedEventListener(listener);
    }

    public static void USER_DEFINED_COMPONENTS()
    {
        String pathPrefix = null;
        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();
        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.comp.example","text/html",
                          pathPrefix + "comp_example.html");
        // ...
        docTemplate.addCreateItsNatComponentListener(new LoginCreationItsNatComponentListener());
    }

    public static void USER_DEFINED_COMPONENTS_2()
    {
        final ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        final LoginComponent loginComp = (LoginComponent)componentMgr.createItsNatComponentById("loginCompId","login",null);

        ValidateLoginListener validator = new ValidateLoginListener()
        {
            public boolean validate(String user,String password)
            {
                if (!user.equals("admin"))
                {
                    System.out.println("Bad user");
                    return false;
                }

                if (!password.equals("1234"))
                {
                    System.out.println("Bad password");
                    return false;
                }

                Element loginElem = (Element)loginComp.getNode();
                loginElem.setAttribute("style","display:none");

                Document doc = loginElem.getOwnerDocument();
                Element infoElem = doc.createElement("p");
                infoElem.appendChild(doc.createTextNode("VALID LOGIN!"));
                loginElem.getParentNode().insertBefore(infoElem,loginElem);

                return true;
            }
        };
        loginComp.setValidateLoginListener(validator);
    }

    public static void AUTOMATIC_COMPONENT_BUILD()
    {
        ItsNatDocument itsNatDoc = null;
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        Element parentElem = itsNatDoc.getDocument().getDocumentElement();
        componentMgr.buildItsNatComponents(parentElem);

        ItsNatHTMLInputTextFormatted inputTextFormat = (ItsNatHTMLInputTextFormatted)componentMgr.addItsNatComponentById("inputTextFormattedId");

        ItsNatLabel label = (ItsNatLabel)componentMgr.findItsNatComponentById("labelId");

        ItsNatFreeLabel freeLabel = (ItsNatFreeLabel)componentMgr.findItsNatComponentById("freeLabelId");

        ItsNatDocumentTemplate docTemplate = null;
        ItsNatListStructure customStruc = new CityListCustomStructure();
        docTemplate.registerArtifact("cityCustomStruc",customStruc);

        itsNatDoc.registerArtifact("cityCustomStruc",customStruc);

        ItsNatFreeListMultSel listCustomStruc = (ItsNatFreeListMultSel)componentMgr.findItsNatComponentById("listCustomStructureId");
        CityListCustomStructure structure = (CityListCustomStructure)listCustomStruc.getItsNatListStructure();

        componentMgr.removeItsNatComponents(parentElem,true);

        docTemplate.setAutoBuildComponents(true);
    }

    public static void MARKUP_DRIVEN_MODE_IN_FORM_CONTROLS()
    {
    ItsNatDocument itsNatDoc = null;
    Element parentElem = null;

    ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
    compMgr.setMarkupDrivenComponents(true);
    compMgr.buildItsNatComponents(parentElem);
    compMgr.setAutoBuildComponents(true);
    }

    public static void NON_AJAX_MODE_AND_COMPONENTS()
    {
        ItsNatServletRequest itsNatRequest = null;
        ItsNatDocument itsNatDoc = itsNatRequest.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        ItsNatHttpSession itsNatSession = (ItsNatHttpSession)itsNatRequest.getItsNatSession();
        HttpSession session = itsNatSession.getHttpSession();
        ItsNatDocument itsNatDocPrev = (ItsNatDocument)session.getAttribute("previous_doc");
        session.removeAttribute("previous_doc"); // No longer available

        ItsNatHTMLSelectMult prevListComp = (ItsNatHTMLSelectMult)itsNatDocPrev.getItsNatComponentManager().findItsNatComponentById("listId");
        DefaultListModel model = (DefaultListModel)prevListComp.getListModel();
        prevListComp.dispose(); // to disconnect the data model from the old markup

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLSelectMult listComp = (ItsNatHTMLSelectMult)compMgr.addItsNatComponentById("listId");
        listComp.setListModel(model);  // Reusing the data model
        // ...
    }

    public static void COMPONENTS_IN_MOBILE_DEVICES_BROWSERS()
    {
    ItsNatDocument itsNatDoc = null;
    ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
    Document doc = itsNatDoc.getDocument();

    Element compElem = doc.getElementById("compId");
    ItsNatFreeComboBox comboComp = compMgr.createItsNatFreeComboBox(compElem, null, null);

    DefaultComboBoxModel dataModel = (DefaultComboBoxModel)comboComp.getComboBoxModel();
    dataModel.addElement("Madrid");
    dataModel.addElement("Sevilla");
    dataModel.addElement("Segovia");
    dataModel.addElement("Barcelona");

    dataModel.setSelectedItem("Segovia");
    }

}

