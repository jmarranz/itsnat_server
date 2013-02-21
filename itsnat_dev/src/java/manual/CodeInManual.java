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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
import manual.core.misc.remctrl.RemoteControlSupervision;
import manual.core.domutils.PersonExtended;
import manual.core.misc.remtmpl.GoogleResultTemplateSource;
import manual.core.misc.remtmpl.RemoteTemplateDocLoadListener;
import manual.core.misc.remtmpl.RemoteTemplateResultDocLoadListener;
import manual.coretut.CoreExampleLoadListener;
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
import org.itsnat.core.CometNotifier;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatSession;
import org.itsnat.core.ItsNatSessionCallback;
import org.itsnat.core.ItsNatVariableResolver;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.CommMode;
import org.itsnat.core.event.NodeAllAttribTransport;
import org.itsnat.core.event.ItsNatContinueEvent;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.NodeAttributeTransport;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.event.ItsNatTimerEvent;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.core.tmpl.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.core.NameValue;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementLabel;
import org.itsnat.core.domutil.ElementLabelRenderer;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ElementListFree;
import org.itsnat.core.domutil.ElementListRenderer;
import org.itsnat.core.domutil.ElementListStructure;
import org.itsnat.core.domutil.ElementRenderer;
import org.itsnat.core.domutil.ElementTable;
import org.itsnat.core.domutil.ElementTableFree;
import org.itsnat.core.domutil.ElementTableRenderer;
import org.itsnat.core.domutil.ElementTableStructure;
import org.itsnat.core.domutil.ElementTree;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeList;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatEventListenerChain;
import org.itsnat.core.event.NodeInnerTransport;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.css.CSS2Properties;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.css.ElementCSSInlineStyle;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import test.comp.Person;

/**
 *
 * @author jmarranz
 */
public class CodeInManual
{
    public CodeInManual()
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

    public static void A_CORE_BASED_WEB_APPLICATION()
    {

    }

    public static void A_COMPONENT_BASED_WEB_APPLICATION()
    {
        String pathPrefix = null;
        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();
        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.comp.example","text/html",
 			pathPrefix + "comp_example.xhtml");
    }

    public static void Pattern_based_view_manipulation_using_DOM_utilities()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        Element parentElem = doc.getElementById("listParent");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList elemList = factory.createElementList(parentElem,true);
        elemList.addElement("Tiger");
        elemList.addElement("Lion");
    }

    public static void Extra_parameters()
    {
        ItsNatDocument itsNatDoc = null;
        Element anElem = null;
        EventListener anListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
                String title = (String)itsNatEvt.getExtraParam("title");
                System.out.println("Page title: " + title);
            }
        };
        ParamTransport extraParam = new CustomParamTransport("title","document.title");
        itsNatDoc.addEventListener((EventTarget)anElem,"click",anListener,false,new ParamTransport[]{ extraParam });
    }

    public static void Custom_pre_send_JavaScript_code()
    {
        ItsNatDocument itsNatDoc = null;
        EventTarget anElem = null;
        EventListener anListener = null;
        String code = "    alert('Fired a ' + event.getNativeEvent().type + ' event');\n";
        itsNatDoc.addEventListener(anElem,"click",anListener,false,code);
        /*
        var func = function (event)
        {
           alert('Fired a ' + event.getNativeEvent().type + ' event');
        };
         */
    }

    public static void Load_and_unload_events()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                System.out.println(evt.getType()); // outs load/unload
            }
        };
        ((EventTarget)view).addEventListener("load",listener,false);
        ((EventTarget)view).addEventListener("unload",listener,false);
    }

    public static void Event_listeners_chain_control()
    {
        final ItsNatDocument itsNatDoc = null;
        EventListener global = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ItsNatEventListenerChain chain = ((ItsNatEvent)evt).getItsNatEventListenerChain();
                try
                {
                    chain.continueChain();
                }
                catch(Exception ex)
                {
                    itsNatDoc.addCodeToSend("alert('Unexpected Error! The page will reload.');");
                    itsNatDoc.addCodeToSend("window.location.reload(true);");

                    itsNatDoc.setInvalid();
                    chain.stop();
                }
           }
        };
        itsNatDoc.addEventListener(global);
    }

    public static void Detection_of_session_and_pages_lost_with_global_listener()
    {
        ItsNatHttpServlet itsNatServlet = null;
        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
                if (itsNatEvt.getItsNatDocument() == null)
                {
                    ItsNatServletResponse response = itsNatEvt.getItsNatServletResponse();
                    response.addCodeToSend("alert('Session or page was lost. Reloading...');");
                    response.addCodeToSend("window.location.reload(true);");

                    itsNatEvt.getItsNatEventListenerChain().stop();
                }
            }
        };
        itsNatServlet.addEventListener(listener);
    }

    public static void REMOTE_VS_INTERNAL_DOM_MODE()
    {
        Element elem = null;
        EventListener listener = null;
        boolean old = ((ItsNatNode)elem).isInternalMode();
        ((ItsNatNode)elem).setInternalMode(true);
        try
        {
            ((EventTarget)elem).addEventListener("DOMNodeInserted",listener,false);
        }
        finally
        {
            ((ItsNatNode)elem).setInternalMode(old);
        }
    }

    public static void REMOTE_DOM_MUTATION_EVENT_LISTENERS()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                MutationEvent mutEvent = (MutationEvent)evt;

                String type = mutEvent.getType();
                if (type.equals("DOMNodeInserted"))
                {
                    Element parent = (Element)mutEvent.getRelatedNode();
                    Node newNode = (Node)mutEvent.getTarget();
                    System.out.println("DOMNodeInserted " + newNode + " " + newNode.getNextSibling());
                }
                else if (type.equals("DOMNodeRemoved"))
                {
                    Element parent = (Element)mutEvent.getRelatedNode();
                    Node removedNode = (Node)mutEvent.getTarget();
                    System.out.println("DOMNodeRemoved " + removedNode + " " + parent);
                }
                else if (type.equals("DOMAttrModified"))
                {
                    Attr attr = (Attr)mutEvent.getRelatedNode();
                    Element elem = (Element)mutEvent.getTarget();
                    String attrValue = elem.getAttribute(mutEvent.getAttrName());
                    String changeName = null;
                    int changeType = mutEvent.getAttrChange();
                    switch(changeType)
                    {
                        case MutationEvent.ADDITION:
                            changeName = "addition";
                            break;
                        case MutationEvent.MODIFICATION:
                            changeName = "modification";
                            break;
                        case MutationEvent.REMOVAL:
                            changeName = "removal";
                            break;
                    }
                    System.out.println("DOMAttrModified (" + changeName + ") " + mutEvent.getAttrName() + " " + attrValue + " " + elem);
                }
                else if (type.equals("DOMCharacterDataModified"))
                {
                    CharacterData charNode = (CharacterData)mutEvent.getTarget();
                    System.out.println("DOMCharacterDataModified " + mutEvent.getNewValue());
                }
            }
        };

        itsNatDoc.addMutationEventListener((EventTarget)doc,listener,false);
    }

    public static void Attribute_synchronization()
    {
        ItsNatDocument itsNatDoc = null;
        EventTarget anElem = null;

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                Element elem = (Element)evt.getCurrentTarget();
                System.out.println(elem.getAttribute("style"));
            }
        };

        itsNatDoc.addEventListener(anElem,"click",listener,false,new NodeAttributeTransport("style") );

        new NodeAttributeTransport("style",false);

        itsNatDoc.addEventListener(anElem,"click",listener,false,new NodeAllAttribTransport() );

        new NodeAllAttribTransport(false);
    }

    public static void Node_content_synchronization()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        Element anElem = doc.getElementById("clickableId");

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                Element currTarget = (Element)evt.getCurrentTarget();
                Node newNode = currTarget.getLastChild();
                Text text = (Text)newNode.getFirstChild();
                System.out.println("New node : " + newNode + " " + text.getData());
            }
        };

        itsNatDoc.addEventListener((EventTarget)anElem,"click",listener,false,new NodeInnerTransport());
    }

    public static void Property_synchronization()
    {
        ItsNatDocument itsNatDoc = null;
        HTMLInputElement anElem = null;

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                HTMLInputElement elem = (HTMLInputElement)evt.getCurrentTarget();
                System.out.println(elem.getValue());
            }
        };

        itsNatDoc.addEventListener((EventTarget)anElem,"change",listener,false,new NodePropertyTransport("value"));

        // Alternative
        itsNatDoc.addEventListener((EventTarget)anElem,"change",listener,false,new NodePropertyTransport("value",String.class));

        new NodePropertyTransport("value",false);
    }

    public static void REMOTE_CONTINUE_EVENT_LISTENERS()
    {
        new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ItsNatEvent itsNatEvent = (ItsNatEvent)evt;
                ClientDocument clientDoc = itsNatEvent.getClientDocument();

                // We need the page title in this context

                EventListener listener = new EventListener()
                {
                    public void handleEvent(Event evt)
                    {
                        ItsNatContinueEvent contEvt = (ItsNatContinueEvent)evt;
                        String title = (String)contEvt.getExtraParam("title");
                        System.out.println("This is the title: " + title);
                    }
                };

                ParamTransport[] extraParams = new ParamTransport[] { new CustomParamTransport("title","document.title") };
                clientDoc.addContinueEventListener(null,listener,itsNatEvent.getCommMode(),extraParams,null,-1);
            }
        };
    }

    public static void REMOTE_USER_DEFINED_EVENT_LISTENERS()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        Element buttonElem = doc.getElementById("buttonId");

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ItsNatUserEvent userEvt = (ItsNatUserEvent)evt;
                String title = (String)userEvt.getExtraParam("title");
                System.out.println("Page title: " + title);
                String url = (String)userEvt.getExtraParam("url");
                System.out.println("URL: " + url);
            }
        };

        itsNatDoc.addUserEventListener((EventTarget)buttonElem,"myUserAction",listener);

        String code = "";
        code += "var itsNatDoc = document.getItsNatDoc();";
        code += "var evt = itsNatDoc.createUserEvent('myUserAction');";
        code += "evt.setExtraParam('title',document.title);";
        code += "evt.setExtraParam('url',document.location);";
        code += "itsNatDoc.dispatchUserEvent(this,evt);";
        buttonElem.setAttribute("onclick",code);
    }

    public static void TIMERS()
    {
        ItsNatDocument itsNatDoc = null;
        ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner();

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ItsNatTimerEvent timerEvt = (ItsNatTimerEvent)evt;
                ItsNatTimerHandle handle = timerEvt.getItsNatTimerHandle();
                long firstTime = handle.getFirstTime();
                if ((new Date().getTime() - firstTime) > 10000) // to avoid never ending ticks
                {
                    handle.cancel();
                    System.out.println("Timer canceled");
                }
                else System.out.println("Tick, next execution: " + new Date(handle.scheduledExecutionTime()));
            }
        };

        ItsNatTimer timer = clientDoc.createItsNatTimer();
        ItsNatTimerHandle handle = timer.schedule(null,listener,1000,2000);

        System.out.println("Scheduled task started, first time: " + new Date(handle.getFirstTime()) + " period: " + handle.getPeriod());
    }

    public static void REMOTE_ASYNCHRONOUS_TASKS()
    {
        final ItsNatDocument itsNatDoc = null;
        ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner();
        Runnable task = new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch(InterruptedException ex) { }

                synchronized(itsNatDoc) // MANDATORY, lock is false !!
                {
                    itsNatDoc.addCodeToSend("alert('Asynchronous task finished!');");
                }
            }
        };

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                itsNatDoc.addCodeToSend("alert('Finished really!');");
            }
        };
        clientDoc.addAsynchronousTask(task,listener); // by default lockDoc is false !!

        itsNatDoc.addCodeToSend("alert('An asynchronous task has started');");
    }

    public void COMET_NOTIFIER()
    {
    final ItsNatDocument itsNatDoc = null;

    final CometNotifier notifier = itsNatDoc.getClientDocumentOwner().createCometNotifier();
    EventListener listener = new EventListener()
    {
        public void handleEvent(Event evt)
        {
            itsNatDoc.addCodeToSend("alert('Tick From Event');");
        }
    };
    notifier.addEventListener(listener);

    Thread backgroundThr = new Thread()
    {
        public void run()
        {
            System.out.println("Background server task started");
            long t1 = System.currentTimeMillis();
            long t2 = t1;
            do
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch(InterruptedException ex) { }

                if (!notifier.isStopped())
                {
                    synchronized(itsNatDoc)
                    {
                        itsNatDoc.addCodeToSend("alert('Tick From Thread');");
                    }
                    notifier.notifyClient();
                }
                t2 = System.currentTimeMillis();
            }
            while( (t2 - t1) < 10*60*1000 ); // Max 10 minutes

            notifier.stop();

            System.out.println("Background server task finished");
        }
    };

    backgroundThr.start();

    }

    public static void STRING_TO_DOM_CONVERSION()
    {
        ItsNatDocument itsNatDoc = null;
        Element refElem = null;

        String code = "<b>New Markup Inserted</b><br />";
        DocumentFragment docFrag = itsNatDoc.toDOM(code);
        refElem.getParentNode().insertBefore(docFrag, refElem);
    }

    public static void MARKUP_FRAGMENTS_HTML_XML_fragments()
    {
        String pathPrefix = getServletContext().getRealPath("/");
        pathPrefix += "/WEB-INF/pages/manual/core/";

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        ItsNatDocFragmentTemplate fragTemplate;
        fragTemplate = itsNatServlet.registerItsNatDocFragmentTemplate("manual.core.htmlFragExample","text/html",pathPrefix + "fragment_example.xhtml");

        fragTemplate = itsNatServlet.registerItsNatDocFragmentTemplate("manual.core.xmlFragExample","text/xml",pathPrefix + "fragment_example.xml");
    }

    public static void MARKUP_FRAGMENTS_Dynamic_include()
    {
        ItsNatDocument itsNatDoc = null;
        Element includeParentElem = null;

        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();

        ItsNatHTMLDocFragmentTemplate fragTemplate = (ItsNatHTMLDocFragmentTemplate)servlet.getItsNatDocFragmentTemplate("manual.core.htmlFragExample");
        DocumentFragment docFrag = fragTemplate.loadDocumentFragmentBody(itsNatDoc);

        includeParentElem.appendChild(docFrag); // docFrag is empty now
    }

    public static void BASIC_DOM_UTILITIES_1()
    {
        Document doc = null;
        Element root = null;

        // Using TreeWalker
        DocumentTraversal travDoc = (DocumentTraversal)doc;
        TreeWalker walker = travDoc.createTreeWalker(root,NodeFilter.SHOW_ELEMENT,null,true);
        Element child = (Element)walker.firstChild();
        while(child != null)
        {
            // Do something with child ...
            child = (Element)walker.nextSibling();
        }
    }

    public static void BASIC_DOM_UTILITIES_2()
    {
        Element root = null;
        TreeWalker walker = null;

        Element child = (Element)walker.firstChild();
        if (child != null)
        {
            while(child != null)
            {
                // Do something with child ...
                child = (Element)walker.nextSibling();
            }
            walker.parentNode();
        }
    }

    public static void BASIC_DOM_UTILITIES_3()
    {
        Element root = null;
        Element child = ItsNatTreeWalker.getFirstChildElement(root);
        while(child != null)
        {
            // Do something with child ...
            child = ItsNatTreeWalker.getNextSiblingElement(child);
        }
    }

    public static void BASIC_DOM_UTILITIES_4()
    {
        Element elem = null; // <elem>Some Text</elem>
        ItsNatDOMUtil.setTextContent(elem,ItsNatDOMUtil.getTextContent(elem) + "(updated)");
    }

    public static void BASIC_DOM_UTILITIES_5()
    {
        Document doc = null;
        Element h1 = ItsNatDOMUtil.createElement("h1","Home Page",doc);
    }


    public static void FREE_ELEMENT_LISTS()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById("elementListId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementListFree elemList = factory.createElementListFree(parent,true);

        Element elem;

        elem = ItsNatDOMUtil.createElement("p","Item 1",doc);
        elemList.addElement(elem);

        elem = ItsNatDOMUtil.createElement("p","Item 2",doc);
        elemList.addElement(elem);

        elem = elemList.getElementAt(1);
        System.out.println(ItsNatDOMUtil.getTextContent(elem)); // "Item 2"

        elem = ItsNatDOMUtil.createElement("h1","Title",doc);
        elemList.setElementAt(0,elem); // Replaces <p>Item 1</p> => <h1>Title</h1>

        List<Element> list = elemList;
        for(ListIterator<Element> it = list.listIterator(); it.hasNext(); )
        {
            Element curElem = it.next();
            System.out.println(it.nextIndex() + ":" + ItsNatDOMUtil.getTextContent(curElem));
            it.remove();
        }
    }

    public static void FREE_ELEMENT_TABLES()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        Element tableParent = doc.getElementById("elementTableId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTableFree table = factory.createElementTableFree(tableParent,true);

        Element rowElem;
        Element cellElem;

        rowElem = doc.createElement("div");

            cellElem = doc.createElement("span");
            cellElem.appendChild(doc.createTextNode("Cell 0,0"));
            rowElem.appendChild(cellElem);

            cellElem = doc.createElement("span");
            cellElem.appendChild(doc.createTextNode("Cell 0,1"));
            rowElem.appendChild(cellElem);

        table.addRow(rowElem);

        rowElem = doc.createElement("div");

            cellElem = doc.createElement("span");
            cellElem.appendChild(doc.createTextNode("Cell 1,0"));
            rowElem.appendChild(cellElem);

            cellElem = doc.createElement("span");
            cellElem.appendChild(doc.createTextNode("Cell 1,1"));
            rowElem.appendChild(cellElem);

        table.addRow(rowElem);
    }

    public static void DOM_RENDERERS()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        Date value = new Date();

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementRenderer renderer = factory.createDefaultElementRenderer();
        renderer.render(null,value,doc.getElementById("elementId"),true);

        ElementRenderer customRenderer = new ElementRenderer()
        {
            public void render(Object userObj,Object value,Element elem,boolean isNew)
            {
                /*
                <table id="elementId2">
                    <tbody>
                        <tr><td>Year:</td><td>(year)</td></tr>
                        <tr><td>Month:</td><td>(month)</td></tr>
                        <tr><td>Day:</td><td>(day)</td></tr>
                    </tbody>
                </table>
                 */
                DateFormat format = DateFormat.getDateInstance(DateFormat.LONG,Locale.US);
                // Format: June 8,2007
                String date = format.format(value);
                int pos = date.indexOf(' ');
                String month = date.substring(0,pos);
                int pos2 = date.indexOf(',');
                String day = date.substring(pos + 1,pos2);
                String year = date.substring(pos2 + 1);

                HTMLTableElement table = (HTMLTableElement)elem;
                HTMLTableSectionElement tbody = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(table);

                HTMLTableRowElement yearRow = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbody);
                HTMLTableCellElement yearCell = (HTMLTableCellElement)yearRow.getCells().item(1);
                ItsNatDOMUtil.setTextContent(yearCell,year);

                HTMLTableRowElement monthRow = (HTMLTableRowElement)ItsNatTreeWalker.getNextSiblingElement(yearRow);
                HTMLTableCellElement monthCell = (HTMLTableCellElement)monthRow.getCells().item(1);
                ItsNatDOMUtil.setTextContent(monthCell,month);

                HTMLTableRowElement dayRow = (HTMLTableRowElement)ItsNatTreeWalker.getNextSiblingElement(monthRow);
                HTMLTableCellElement dayCell = (HTMLTableCellElement)dayRow.getCells().item(1);
                ItsNatDOMUtil.setTextContent(dayCell,day);
            }

            public void unrender(Object userObj,Element elem)
            {
            }
        };

        customRenderer.render(null,value,doc.getElementById("elementId2"),true);
    }

    public static void PATTERN_BASED_ELEMENT_LABELS()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementLabel label = factory.createElementLabel(doc.getElementById("elementId"),true,null);
        label.setLabelValue("Hello I'm Jose");
    }


    public static void PATTERN_BASED_ELEMENT_LISTS()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById("elementListId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList elemList = factory.createElementList(parent,true);
        elemList.addElement("Madrid");
        elemList.addElement("Barcelona");
        elemList.addElement("Sevilla");
    }

    public static void PATTERN_BASED_ELEMENT_LISTS_2()
    {
        ItsNatDocument itsNatDoc = null;
        Element parent = null;

        ElementListRenderer customRenderer = new ElementListRenderer()
        {
            public void renderList(ElementList list,int index,Object value,Element elem,boolean isNew)
            {
                String style;
                if (index == 0)
                    style = "font-style:italic;";
                else if (index == 1)
                    style = "font-weight:bold;";
                else
                    style = "font-size: 150%;";
                elem.setAttribute("style",style);
                ItsNatDOMUtil.setTextContent(elem,value.toString());
            }

            public void unrenderList(ElementList list,int index,Element contentElem)
            {
            }
        };
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList elemList = factory.createElementList(parent,true,
            null,customRenderer);
    }

    public static void PATTERN_BASED_ELEMENT_LISTS_3()
    {
        ItsNatDocument itsNatDoc = null;
        Element parent = null;
        ElementListRenderer customRenderer = null;

        ElementListStructure customStructure = new ElementListStructure()
        {
            public Element getContentElement(ElementList list,int index,Element elem)
            {
                /*
                    <tr><td><table><tbody><tr><td>Text</td>...
                 */
                HTMLTableRowElement rowElem = (HTMLTableRowElement)elem;
                HTMLTableCellElement cellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
                HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
                HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
                HTMLTableRowElement rowElem2 = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
                HTMLTableCellElement cellElem2 = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem2);
                return cellElem2;
            }
        };

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList elemList = factory.createElementList(parent,true,
            customStructure,customRenderer);
    }

    public static void PATTERN_BASED_ELEMENT_LISTS_4()
    {
        ElementListStructure customStructure = new ElementListStructure()
        {
            public Element getContentElement(ElementList list,int index,Element elem)
            {
                /*
                    <elem1>...<elemN>Text</elemN>...</elem1>
                 */
                Element parent = elem;
                Element child = elem;
                do
                {
                    parent = child;
                    child = ItsNatTreeWalker.getFirstChildElement(parent);
                }
                while(child != null);

                return parent;
            }
        };
    }

    public static void PATTERN_BASED_ELEMENT_TABLES()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        Element parent = doc.getElementById("tableId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTable elemTable = factory.createElementTable(parent,true);
        elemTable.setColumnCount(3);
        elemTable.addRow(new String[] {"Madrid","Plaza Mayor","Palacio Real"});
        elemTable.addRow(new String[] {"Sevilla","Plaza de España","Giralda"});
        elemTable.addRow(new String[] {"Segovia","Plaza del Azoguejo","Acueducto Romano"});
    }

    public static void PATTERN_BASED_ELEMENT_TABLES_2()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        ElementTableRenderer customRenderer = new ElementTableRenderer()
        {
            public void renderTable(ElementTable table,int row,int col,Object value,Element cellContentElem,boolean isNew)
            {
                String style;
                if (row == 0)
                    style = "font-style:italic;";
                else if (row == 1)
                    style = "font-weight:bold;";
                else
                    style = "font-size: 120%;";
                cellContentElem.setAttribute("style",style);
                ItsNatDOMUtil.setTextContent(cellContentElem,value.toString());
            }

            public void unrenderTable(ElementTable table,int row,int col,Element cellContentElem)
            {
            }
        };

        Element parent = doc.getElementById("tableId2");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTable elemTable = factory.createElementTable(parent,true,null,customRenderer);

    }

    public static void PATTERN_BASED_ELEMENT_TABLES_3()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        ElementTableRenderer customRenderer = null;

        ElementTableStructure customStructure = new ElementTableStructure()
        {
            public Element getRowContentElement(ElementTable table,int row,Element elem)
            {
                HTMLTableRowElement rowElem = (HTMLTableRowElement)elem;
                HTMLTableCellElement cellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
                HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
                HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
                HTMLTableRowElement rowElem2 = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
                return rowElem2;
            }

            public Element getCellContentElement(ElementTable table,int row,int col,Element elem)
            {
                HTMLTableCellElement cellElem = (HTMLTableCellElement)elem;
                HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
                HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
                HTMLTableRowElement rowElem = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
                HTMLTableCellElement cellElem2 = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
                return cellElem2;
            }
        };

        Element parent = doc.getElementById("tableId3");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTable elemTable = factory.createElementTable(parent,true,customStructure,customRenderer);

    }

    public static void PATTERN_BASED_ELEMENT_TABLES_4()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        Element parent = doc.getElementById("tableId4");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTable elemTable = factory.createElementTable(parent,true);
    }

    public static void PATTERN_BASED_ELEMENT_TREES()
    {
    ItsNatDocument itsNatDoc = null;
    Document doc = itsNatDoc.getDocument();
    Element parent = doc.getElementById("treeId");
    ElementGroupManager factory = itsNatDoc.getElementGroupManager();
    ElementTree elemTree = factory.createElementTree(false,parent,true);

    ElementTreeNode rootNode = elemTree.addRootNode("Spain");
    ElementTreeNodeList rootChildren = rootNode.getChildTreeNodeList();

    ElementTreeNode provincesNode = rootChildren.addTreeNode("Autonomous Communities");
    provincesNode.getChildTreeNodeList().addTreeNode("Asturias");
    provincesNode.getChildTreeNodeList().addTreeNode("Cantabria");
    provincesNode.getChildTreeNodeList().addTreeNode("Castilla La Mancha");

    ElementTreeNode ccaaNode = rootChildren.addTreeNode("Cities");
    ccaaNode.getChildTreeNodeList().addTreeNode("Madrid");
    ccaaNode.getChildTreeNodeList().addTreeNode("Barcelona");
    ccaaNode.getChildTreeNodeList().addTreeNode("Sevilla");
    }

    public static void PATTERN_BASED_ELEMENT_TREES_ROOT_FIXED()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById("treeId");

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTreeNode rootNode = factory.createElementTreeNode(parent,true);
        rootNode.setValue("Spain");

        ElementTreeNodeList rootChildren = rootNode.getChildTreeNodeList();
    }

    public static void PATTERN_BASED_ELEMENT_TREES_ROOTLESS()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById("treeId");

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTreeNodeList rootList = factory.createElementTreeNodeList(false,parent,true);
    }

    public static void PATTERN_BASED_ELEMENT_TREES_CUSTOM_RENDER_STRUCT()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById("treeId");

        ElementTreeNodeRenderer customRenderer = new ElementTreeNodeRenderer()
        {
            public void renderTreeNode(ElementTreeNode treeNode,Object value,Element labelElem,boolean isNew)
            {
                int level = treeNode.getDeepLevel();

                String style;
                if (level == 0)
                    style = "font-size:large;";
                else if (level == 1)
                    style = "font-weight:bold;";
                else
                    style = "font-style:italic;";

                labelElem.setAttribute("style",style);
                ItsNatDOMUtil.setTextContent(labelElem,value.toString());
            }

            public void unrenderTreeNode(ElementTreeNode treeNode,Element labelElem)
            {
            }
        };

        ElementTreeNodeStructure customStructure = new ElementTreeNodeStructure()
        {
            public Element getContentElement(ElementTreeNode treeNode,Element nodeParent)
            {
                Element child = ItsNatTreeWalker.getFirstChildElement(nodeParent);
                return ItsNatTreeWalker.getFirstChildElement(child);
            }

            public Element getHandleElement(ElementTreeNode treeNode,Element nodeParent)
            {
                return null;
            }

            public Element getIconElement(ElementTreeNode treeNode,Element nodeParent)
            {
                return null;
            }

            public Element getLabelElement(ElementTreeNode treeNode,Element nodeParent)
            {
                return getContentElement(treeNode,nodeParent);
            }

            public Element getChildListElement(ElementTreeNode treeNode,Element nodeParent)
            {
                Element contentParentElem = getContentElement(treeNode,nodeParent);
                return ItsNatTreeWalker.getNextSiblingElement(contentParentElem);
            }
        };

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTree elemTree = factory.createElementTree(false,parent,true,customStructure,customRenderer);

    }

    public static void PATTERN_BASED_ELEMENT_TREES_4()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById("treeId5");

        ElementTreeNodeRenderer customRenderer = null;

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTree elemTree = factory.createElementTree(true,parent,true,null,customRenderer);
    }

    public static void VARIABLE_RESOLVER()
    {
        final ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver();
        ClientDocument owner = itsNatDoc.getClientDocumentOwner();
        ItsNatHttpSession itsNatSession = (ItsNatHttpSession)owner.getItsNatSession();
        HttpSession session = itsNatSession.getHttpSession();
        session.setAttribute("sessionId",itsNatSession.getId());
        itsNatDoc.setAttribute("docId",itsNatDoc.getId());
        resolver.setLocalVariable("refreshInterval",new Integer(2000));
        resolver.setLocalVariable("commMode",new Integer(itsNatDoc.getCommMode()));
        resolver.setLocalVariable("linkText","Click to monitor your page");

        Element div = doc.getElementById("remoteCtrl");
        resolver.resolve(div);
    }

    public static void VARIABLE_RESOLVER_2()
    {
        final ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        ElementRenderer customRenderer = new ElementRenderer()
        {
            public void render(Object userObj,Object value,Element elem,boolean isNew)
            {
                DateFormat format =
                    DateFormat.getDateInstance(DateFormat.LONG,Locale.US);
                // Format: June 8,2007
                String date = format.format(value);
                int pos = date.indexOf(' ');
                String month = date.substring(0,pos);
                int pos2 = date.indexOf(',');
                String day = date.substring(pos + 1,pos2);
                String year = date.substring(pos2 + 1);

                ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);
                resolver.setLocalVariable("year",year);
                resolver.setLocalVariable("month",month);
                resolver.setLocalVariable("day",day);
                resolver.resolve(elem);
            }

            public void unrender(Object userObj,Element elem)
            {
            }
        };

        customRenderer.render(null,new Date(),doc.getElementById("elementId2"),true);
    }

    public static void VARIABLE_RESOLVER_3()
    {
        ItsNatDocument itsNatDoc = null;

        Object value = new PersonExtended("John","Smith",30,true);

        Document doc = itsNatDoc.getDocument();
        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);
        resolver.introspect("person",value);

        Element elem = doc.getElementById("elementId3");
        resolver.resolve(elem);
    }

    public static void VARIABLE_RESOLVER_4()
    {
        final ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        ElementLabelRenderer renderer = new ElementLabelRenderer()
        {
            protected ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);

            public void renderLabel(ElementLabel label,Object value,Element elem,boolean isNew)
            {
                resolver.setLocalVariable("variable_to_resolve",value);
                resolver.resolve(elem);
            }

            public void unrenderLabel(ElementLabel label,Element elem)
            {
            }
        };
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementLabel label = factory.createElementLabel(doc.getElementById("elementId"),true,renderer);


        label.setLabelValue("First Value");

        label.setUsePatternMarkupToRender(true);

        label.setLabelValue("Second Value");

        label.setLabelValue("Third Value");
    }


    public static void W3C_ELEMENTCSSINLINESTYLE_IMPLEMENTATION()
    {
        new EventListener()
        {
            public void handleEvent(Event evt)
            {
                Element currTarget = (Element)evt.getCurrentTarget(); // Link

                ElementCSSInlineStyle styleElem = (ElementCSSInlineStyle)currTarget;
                CSSStyleDeclaration cssDec = (CSSStyleDeclaration)styleElem.getStyle();

                CSSValueList border = (CSSValueList)cssDec.getPropertyCSSValue("border");
                int len = border.getLength();
                String cssText = "";
                for(int i = 0; i < len; i++)
                {
                    CSSValue value = border.item(i);
                    if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE)
                    {
                        CSSPrimitiveValue primValue = (CSSPrimitiveValue)value;
                        if (primValue.getPrimitiveType() == CSSPrimitiveValue.CSS_RGBCOLOR)
                        {
                            RGBColor rgb = primValue.getRGBColorValue();
                            System.out.println("Current border color: rgb(" + rgb.getRed().getCssText() + "," + rgb.getGreen().getCssText() + "," + rgb.getBlue().getCssText() + ")");
                        }
                        else cssText += primValue.getCssText() + " ";
                    }
                    else cssText += value.getCssText() + " ";
                }
                cssDec.setProperty("border",cssText,null); // Removed border color

                CSS2Properties cssDec2 = (CSS2Properties)styleElem.getStyle();
                String newColor = "rgb(255,100,150)";
                cssDec2.setBorderColor(newColor); // border-color property
                System.out.println("New border color: " + cssDec2.getBorderColor());
            }

        };
    }

    public void REMOTE_VIEW_CONTROL()
    {
        RemoteControlSupervision remCtrlSup = new RemoteControlSupervision();

        ItsNatServlet servlet = null;
        servlet.addItsNatAttachedClientEventListener(remCtrlSup);

        ItsNatDocumentTemplate docTemplate = null;
        docTemplate.addItsNatAttachedClientEventListener(remCtrlSup);

        ItsNatDocument itsNatDoc = null;
        itsNatDoc.addItsNatAttachedClientEventListener(remCtrlSup);
    }

    public void EXTREME_MASHUPS_1()
    {
ItsNatServlet servlet = null;
ItsNatDocumentTemplate docTemplate = servlet.registerItsNatDocumentTemplateAttachedServer("extremeMashupHTMLMime","text/html");
docTemplate.addItsNatServletRequestListener(new CoreExampleLoadListener());
docTemplate.setCommMode(CommMode.SCRIPT_HOLD);
    }

    public void EXTREME_MASHUPS_2()
    {
ItsNatServlet servlet = null;
ItsNatDocumentTemplate docTemplate = servlet.registerItsNatDocumentTemplateAttachedServer("extremeMashupXHTMLMime","application/xhtml+xml");
docTemplate.addItsNatServletRequestListener(new CoreExampleLoadListener());
docTemplate.setCommMode(CommMode.SCRIPT_HOLD);
    }

    public static void REMOTE_VIEW_CONTROL_2()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();
        ItsNatDocumentTemplate thisDocTemplate = itsNatDoc.getItsNatDocumentTemplate();

        ItsNatServlet itsNatServlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        ItsNatServletContext appCtx = itsNatServlet.getItsNatServletConfig().getItsNatServletContext();

        final List<ItsNatSession> sessionList = new LinkedList<ItsNatSession>();
        ItsNatSessionCallback cb = new ItsNatSessionCallback()
        {
            public boolean handleSession(ItsNatSession session)
            {
                sessionList.add(session);
                return true; // continue
            }
        };
        appCtx.enumerateSessions(cb);

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList sessionNodeList = factory.createElementList(doc.getElementById("otherSessionsId"),true);

        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);
        resolver.setLocalVariable("refreshInterval",new Integer(3000));
        resolver.setLocalVariable("commMode",new Integer(itsNatDoc.getCommMode()));

        for(int i = 0; i < sessionList.size(); i++)
        {
            ItsNatHttpSession otherSession = (ItsNatHttpSession)sessionList.get(i);

            ItsNatDocument[] remDocs = otherSession.getItsNatDocuments();

            for(int j = 0; j < remDocs.length; j++)
            {
                ItsNatDocument currRemDoc = remDocs[j];
                if (itsNatDoc == currRemDoc) continue;
                String id;
                synchronized(currRemDoc)
                {
                    ItsNatDocumentTemplate docTemplate = currRemDoc.getItsNatDocumentTemplate();
                    if (docTemplate != thisDocTemplate)
                        continue;
                }

                String docId = currRemDoc.getId(); // No sync is needed
                Element sessionElem = (Element)sessionNodeList.addElement();

                ItsNatVariableResolver resolver2 = resolver.createItsNatVariableResolver();
                resolver2.setLocalVariable("sessionId",otherSession.getId());
                resolver2.setLocalVariable("agentInfo",otherSession.getUserAgent());
                resolver2.setLocalVariable("docId",docId);
                resolver2.resolve(sessionElem);
            }
        }
    }

    public static void OTHER_NAMESPACES_INSIDE_XHTML()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        Element listParentElem = doc.getElementById("circleListId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList circleList = factory.createElementList(listParentElem,true);

        for(int i = 0; i < 5; i++)
        {
            Element circleElem = circleList.addElement();
            int cx;
            if (i > 0)
            {
                Element prevCircle = circleList.getElementAt(i - 1);
                cx = Integer.parseInt(prevCircle.getAttribute("cx"));
            }
            else cx = 30;
            cx += 50;
            circleElem.setAttribute("cx",Integer.toString(cx));
        }
    }

    public static void REMOTE_TEMPLATES()
    {
ItsNatHttpServlet itsNatServer = null;
ItsNatDocumentTemplate docTemplate;

docTemplate = itsNatServer.registerItsNatDocumentTemplate("remoteTemplateExample","text/html","http://www.google.com");
docTemplate.addItsNatServletRequestListener(new RemoteTemplateDocLoadListener());
docTemplate.setOnLoadCacheStaticNodes(false);

docTemplate = itsNatServer.registerItsNatDocumentTemplate("remoteTemplateExampleResult","text/html",new GoogleResultTemplateSource());
docTemplate.addItsNatServletRequestListener(new RemoteTemplateResultDocLoadListener());
docTemplate.setOnLoadCacheStaticNodes(false);
docTemplate.setEventsEnabled(false);
    }

    public static void JAVASCRIPT_GENERATION_UTILITIES()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("inputElemId");

        ScriptUtil scriptGen = itsNatDoc.getScriptUtil();

        String code;

        String msg = "A Java String transported as a JavaScript string";
        String inputElemJS = scriptGen.getNodeReference(inputElem);
        String newValue = scriptGen.getTransportableStringLiteral(msg);
        code = inputElemJS + ".value = " + newValue + ";";

        itsNatDoc.addCodeToSend(code);

        code = scriptGen.getSetPropertyCode(inputElem,"value",msg,true);
        itsNatDoc.addCodeToSend(code);

        code = scriptGen.getCallMethodCode(inputElem,"select",null,true);
        itsNatDoc.addCodeToSend(code);

/* JavaScript, quitar los comentarios para imprimir

    itsNatDoc.getNode("cn_33").value = "A Java String transported as a JavaScript string";
    itsNatDoc.getNode("cn_33").value = "A Java String transported as a JavaScript string";
    itsNatDoc.getNode("cn_33").select();
*/
    }

    public void	FAST_AND_SLOW_LOADING_MODES()
    {
        ItsNatHTMLDocument itsNatDoc = null;
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        HTMLTableElement tableElem = (HTMLTableElement)doc.createElement("table");
        doc.getBody().appendChild(tableElem);

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt) { }
        };

        ((EventTarget)tableElem).addEventListener("click",listener,false);

        ((EventTarget)tableElem).removeEventListener("click",listener,false);

        doc.getBody().removeChild(tableElem);
    }

    public static void GLOBAL_LOAD_PROCESSING()
    {
        ItsNatHttpServlet itsNatServlet = null;

        ItsNatServletRequestListener listener = new ItsNatServletRequestListener()
        {
            public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
            {
                ItsNatDocument itsNatDoc = request.getItsNatDocument();
                if (itsNatDoc != null)
                {
                    String docName = itsNatDoc.getItsNatDocumentTemplate().getName();
                    System.out.println("Loading " + docName);
                }
            }
        };

        itsNatServlet.addItsNatServletRequestListener(listener);

    }

    public static void GLOBAL_LOAD_PROCESSING_NO_TEMPLATE_FOUND()
    {
        ItsNatServletRequestListener listener = new ItsNatServletRequestListener()
        {
            public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
            {
                ItsNatDocument itsNatDoc = request.getItsNatDocument();
                if (itsNatDoc != null)
                {
                    // ...
                }
                else
                {
                    ServletRequest servReq = request.getServletRequest();
                    String docName = servReq.getParameter("itsnat_doc_name");
                    if (docName == null) docName = (String)servReq.getAttribute("itsnat_doc_name");
                    if (docName != null)
                    {
                        ItsNatServlet servlet = response.getItsNatServlet();
                        ServletRequest servRequest = request.getServletRequest();
                        @SuppressWarnings("unchecked")
                        Map<String,String[]> newParams = new HashMap<String,String[]>(servRequest.getParameterMap());
                        newParams.put("itsnat_doc_name",new String[]{"feashow.docNotFound"});
                        servRequest = servlet.createServletRequest(servRequest, newParams);
                        servlet.processRequest(servRequest,response.getServletResponse());
                    }
                }
            }
        };
    }

    public static void GLOBAL_LOAD_PROCESSING_PRETTY_URL()
    {
        ItsNatServletRequestListener listener = new ItsNatServletRequestListener()
        {
            public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
            {
                ItsNatDocument itsNatDoc = request.getItsNatDocument();
                if (itsNatDoc != null)
                {
                    // ...
                }
                else
                {
                    ServletRequest servReq = request.getServletRequest();
                    String docName = servReq.getParameter("itsnat_doc_name");
                    if (docName == null) docName = (String)servReq.getAttribute("itsnat_doc_name");
                    if (docName != null)
                    {
                        // ...
                    }
                    else
                    {
                        // Pretty URL case
                        HttpServletRequest servRequest = (HttpServletRequest)request.getServletRequest();
                        String pathInfo = servRequest.getPathInfo();
                        if (pathInfo == null)
                            throw new RuntimeException("Unexpected URL");

                        docName = pathInfo.substring(1); // Removes '/'
                        docName = docName.replace('/','.');     // => "name.name"
                        request.getServletRequest().setAttribute("itsnat_doc_name",docName);

                        ServletResponse servResponse = response.getServletResponse();
                        request.getItsNatServlet().processRequest(servRequest,servResponse);
                    }
                }
            }
        };

    }

    public static void EVENTS_FIRED_BY_THE_SERVER()
    {
        ItsNatDocument itsNatDoc = null;
        Document doc = itsNatDoc.getDocument();

        final Element linkElem = (Element)doc.getElementById("linkId");

        final Element[] buttonElems = new Element[3];
        for(int i = 0; i < buttonElems.length; i++)
            buttonElems[i] = doc.getElementById("buttonId" + i);

        final Element userButton = doc.getElementById("userButtonId");

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                EventTarget currTarget = evt.getCurrentTarget();
                if (currTarget == linkElem)
                {
                    ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
                    final ItsNatDocument itsNatDoc = itsNatEvt.getItsNatDocument();
                    final ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner();

                    Runnable dispCode = new Runnable()
                    {
                    public void run()
                    {
                        for(int i = 0; i < buttonElems.length; i++)
                        {
                            Element buttonElem;
                            MouseEvent mouseEvt;
                            synchronized(itsNatDoc)
                            {
                                Document doc = itsNatDoc.getDocument();
                                AbstractView view = ((DocumentView)doc).getDefaultView();

                                mouseEvt = (MouseEvent)itsNatDoc.createEvent("MouseEvents");
                                mouseEvt.initMouseEvent("click",true,true,view,0,
                                        0,0,0,0,false,false,false,false,(short)0/*left button*/,null);

                                buttonElem = buttonElems[i];
                            }

                            ((EventTarget)buttonElem).dispatchEvent(mouseEvt);
                            // Alternatives:
                            // itsNatDoc.dispatchEvent((EventTarget)buttonElem,mouseEvt);
                            // clientDoc.dispatchEvent((EventTarget)buttonElem,mouseEvt);
                        }

                        ItsNatUserEvent userEvt;
                        synchronized(itsNatDoc)
                        {
                            userEvt = (ItsNatUserEvent)itsNatDoc.createEvent("itsnat:UserEvents");
                            userEvt.initEvent("itsnat:user:myEvent",false,false);
                        }

                        ((EventTarget)userButton).dispatchEvent(userEvt);
                    }
                    };
                    clientDoc.startEventDispatcherThread(dispCode);
                }
                else
                {
                    System.out.println("Clicked: " + ((Element)currTarget).getAttribute("value"));
                }
            }
        };

        ((EventTarget)linkElem).addEventListener("click",listener,false);

        for(int i = 0; i < buttonElems.length; i++)
            ((EventTarget)buttonElems[i]).addEventListener("click",listener,false);

        itsNatDoc.addUserEventListener((EventTarget)userButton,"myEvent",listener);
        userButton.setAttribute("onclick","document.getItsNatDoc().fireUserEvent(this,'myEvent');");
    }


    // REFERRERS
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();

        ItsNatDocument itsNatDocRef = request.getItsNatDocumentReferrer();
        // ...
    }

    public void REFERRERS_PUSH()
    {
        final ItsNatDocument itsNatDoc = null;

        ItsNatServletRequestListener listener = new ItsNatServletRequestListener()
        {
            public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
            {
                ItsNatDocument itsNatDocTarget = request.getItsNatDocument();

                Document doc = itsNatDoc.getDocument();
                Element listParentElem = doc.getElementById("messageListId");
                Node contentNode = ItsNatDOMUtil.extractChildren(listParentElem.cloneNode(true));
                if (contentNode != null)
                {
                    Document docTarget = itsNatDocTarget.getDocument();
                    Element listParentElemTarget = docTarget.getElementById("messageListId");
                    contentNode = docTarget.importNode(contentNode,true);
                    listParentElemTarget.appendChild(contentNode);
                }
            }
        };
        itsNatDoc.addReferrerItsNatServletRequestListener(listener);
    }

    public static void OPERA_SUPPORT()
    {
        /* JavaScript, quitar los comentarios para imprimir
    window.opera.setOverrideHistoryNavigationMode("compatible");
    window.history.navigationMode = "compatible";

    window.location.reload(true);

         */

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
                          pathPrefix + "comp_example.xhtml");
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

