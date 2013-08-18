
package inexp.waiaria;

import org.itsnat.core.ItsNatNode;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLImageElement;

/**
 *
 * @author jmarranz
 */
public class WAIARIADocument implements EventListener
{
    private final static String ROLE_TREEITEM = "treeitem";
    private final static String ROLE_TREE = "tree";
    private final static String ROLE_GROUP = "group";
    private final static String STATE_EXPANDED = "aria-expanded";

    private final static short KEY_ENTER=0x0D;
    private final static short KEY_END = 0x23;
    private final static short KEY_HOME = 0x24;
    private final static short KEY_LEFT = 0x25;
    private final static short KEY_UP = 0x26;
    private final static short KEY_RIGHT = 0x27;
    private final static short KEY_DOWN = 0x28;

    private final static String COLLAPSE = "waiaria/img/minus.gif";
    private final static String EXPAND= "waiaria/img/plus.gif";
    
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element treeElem;

    public WAIARIADocument(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        this.treeElem = doc.getElementById("tree");
        ((EventTarget)treeElem).addEventListener("click", this, false);
        ((EventTarget)treeElem).addEventListener("dblclick", this, false);
        ((EventTarget)treeElem).addEventListener("keydown", this, false);
        ((EventTarget)treeElem).addEventListener("keypress", this, false);
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("click"))
        {
            if (evt.getTarget() instanceof HTMLImageElement)
                imgToggle(evt);
            
            if (!treeItemClick(evt))
                evt.preventDefault();
        }
        else if (type.equals("dblclick") || type.equals("keydown") || type.equals("keypress"))
        {
            if (!treeItemEvent(evt))
                evt.preventDefault();
        }

    }

    public EventTarget getTarget(Event event) {
      return event.getTarget();
    }    

    public void debugOut(String debugStr)
    {
        itsNatDoc.addCodeToSend("alert('" + debugStr + "');");
    }
    
    public Node getTree(Node treeItem)
    {
      Node tree = treeItem;
      try
      {

        while (!getRole(tree).equals(ROLE_TREE))
        {
              tree = tree.getParentNode();
        }
      }
      catch(Exception ex)
      {
        debugOut("error in getTree(); " + ex.getMessage());
        return null; // No tree container for tree item
      }
      return tree;
    }

    public String getRole(Node node)
    {
        if (!(node instanceof Element)) return "";
      try
      {
        return ((Element)node).getAttribute("role");
      }
      catch(Exception ex)
      {
        return "";
      }
    }

    public boolean isText(Node node)
    {
        return (node != null) && node.getNodeType() == Node.TEXT_NODE;
    }

    public boolean isValidTreeItem(Node treeItem)
    {
        boolean bIsTreeItem = false;
        Node testItem = treeItem;

        if (isText(testItem))
        {
            testItem = testItem.getNextSibling();
        }

        if (getRole(testItem).equals(ROLE_TREEITEM) )
        {
            bIsTreeItem = true;
        }

        return bIsTreeItem;
    }

    public Node getFirstTreeItem(Node tree)
    {
      Node first = tree.getFirstChild();
      Node treeItem = null;
      // FF reports firstChild as text node while IE does not
      while ( (first!=null) && isText(first) ) {
        first = first.getNextSibling();  // tree had better have a child that is not text!
      }
      if ((first!=null) && first.hasChildNodes()) {
          for (int i=0; i<first.getChildNodes().getLength(); i++) {
          if (getRole(first.getChildNodes().item(i)).equals(ROLE_TREEITEM)) {
            treeItem = first.getChildNodes().item(i);
            break;
          }
        }
      }
      return treeItem;
    }

    public void setFocus(Node newFocus)
    {
      if (newFocus != null) {
            String ref = itsNatDoc.getScriptUtil().getNodeReference(newFocus);
            StringBuilder code = new StringBuilder();
            code.append("gNewFocus = " + ref + ";");  // must store in a global variable so object is available when setTimeout occurs.
            code.append("setTimeout('gNewFocus.focus();', 0);");
            itsNatDoc.addCodeToSend(code);
      }
    }

    public void treeItemFocus(Element treeItem, boolean bFocus)
    {
        // Cache last focused tree item on the tree
        // The last focused item is always the only item in the tab order for this tree,
        // so we need to set its tabindex to 0, and the others to -1
        // var treeItem = getTarget(event);
        Node tree = getTree(treeItem);
        Element lastFocus = (Element)((ItsNatNode)tree).getUserValue("lastFocus");
        if (lastFocus == null) {
            lastFocus = (Element)getFirstTreeItem(tree); //debugOut(tree.lastFocus.firstChild.nodeValue);
            ((ItsNatNode)tree).setUserValue("lastFocus",lastFocus);
        }
        lastFocus.setAttribute("tabindex","-1");
        lastFocus.setAttribute("class","treeitem");
        ((ItsNatNode)tree).setUserValue("lastFocus",treeItem);
        treeItem.setAttribute("tabindex","0");
        treeItem.setAttribute("class","treeitemfocus");

        if (bFocus == true) {
            setFocus(treeItem);
        }
    }


    public boolean treeItemClick(Event event)
    {
        Node treeItem = (Node)getTarget(event);
        boolean bEventContinue = true;
        if (isValidTreeItem(treeItem) ) {
            // only process if click was on an actual tree item
            treeItemFocus((Element)treeItem, false);  // update the tabindex and lastFocused variables, item already has focus from being clicked upon
            bEventContinue = false;
        }
        else {
            if (!((Element)treeItem).getTagName().toLowerCase().equals("img")) {
              //reset focus to existing treeItem (can always find Tree from any child)
              Element curTreeItem = (Element)((ItsNatNode)getTree(treeItem)).getUserValue("lastFocus");
              if (curTreeItem != null)
                setFocus(curTreeItem);
                bEventContinue = false;
            }
            // else not image so return true - only images are for expand/collapse which have their own event handler
        }
        return bEventContinue;
    }

    public boolean isGroupClosed(Node groupItem)
    {
        Element treeItemForGroup = (Element)getRelativeTreeItem(groupItem, -1, false);
        return (treeItemForGroup == null) || treeItemForGroup.getAttribute("aria-expanded").equals("false");
    }

    public Node getTreeItemFromPar(Node itemDiv)
    {
      Node treeItem = null;
      if ((itemDiv != null) && itemDiv.hasChildNodes()) {
        for (int i=0; i<itemDiv.getChildNodes().getLength(); i++) {
          if (getRole(itemDiv.getChildNodes().item(i)).equals(ROLE_TREEITEM)) {
            treeItem = itemDiv.getChildNodes().item(i);
            break;
          }
        }
      }
      return treeItem;
    }

    public Node getLastTreeItem(Node tree)
    {
      Node lastTreeItem = tree;
      do {
        lastTreeItem = lastTreeItem.getLastChild();
        if (isText(lastTreeItem)) {
          lastTreeItem = lastTreeItem.getPreviousSibling();
        }
      }
      while (getRole(lastTreeItem).equals(ROLE_GROUP));
      return  getTreeItemFromPar(lastTreeItem);
    }

    public Node getRelativeTreeItem(Node treeItem,int delta,boolean wrap)
    {
    // treeItem is really the parent div of the treeItem
      // delta: -1 = prev, 1 = next
      if (delta == 1)
      {  // Next
        Node newTreeItem = treeItem.getNextSibling();
        while (isText(newTreeItem)) {
          newTreeItem = newTreeItem.getNextSibling();
        }
      // if(newTreeItem)
        //  debugOut(newTreeItem.id + " " + getRole(newTreeItem) );

        Node treeItemSpan = null;
        if (newTreeItem != null) {
          String role = getRole((Element)newTreeItem);
          if (role.equals(ROLE_GROUP)) {
            if (isGroupClosed(newTreeItem)) {
              return getRelativeTreeItem(newTreeItem, 1, wrap);
            } // end of closed group
            else {
              newTreeItem = newTreeItem.getFirstChild();
              if (isText(newTreeItem)) {
                newTreeItem = newTreeItem.getNextSibling();
              }
            } // end of open group
           } // end of if group
           if ( (newTreeItem != null) && (treeItemSpan == null)) {
              //extract the actual treeItem span from the div
              for(int i=0; i < newTreeItem.getChildNodes().getLength(); i++ ) {
                treeItemSpan = newTreeItem.getChildNodes().item(i); //debugOut(treeItemSpan.tagName + " " + getRole(treeItemSpan) + "\n");
                if (treeItemSpan.getNodeName().equals("span") && getRole((Element)treeItemSpan).equals(ROLE_TREEITEM)) {
                  return treeItemSpan;
                }
              }
            }
           } // end of if newTreeItem
       } // end of next
      else {    // Prev
        Node newTreeItem = treeItem.getPreviousSibling();
        if (isText(newTreeItem)) {
          newTreeItem = newTreeItem.getPreviousSibling();
        }
        if (newTreeItem != null) {
          String role;
          while ((role = getRole((Element)newTreeItem)).equals(ROLE_GROUP)) {
            if (isGroupClosed(newTreeItem)) {
              return getRelativeTreeItem(newTreeItem, -1, wrap);
            } // end of if closed group
            else {
              newTreeItem = newTreeItem.getLastChild();
              if (isText(newTreeItem)) {
                newTreeItem = newTreeItem.getPreviousSibling();
              }
            } // end of open group
          }  // end of while

          Node treeItemSpan = null;
           if ((newTreeItem != null) && (treeItemSpan == null)) {
              //extract the actual treeItem span from the div
              for(int j=0; j < newTreeItem.getChildNodes().getLength(); j++ ) {
                treeItemSpan = newTreeItem.getChildNodes().item(j);
                if (treeItemSpan.getNodeName().equals("span") && getRole((Element)treeItemSpan).equals(ROLE_TREEITEM)) {
                  return treeItemSpan;
                }
              }
            }
        } // end of if newTreeItem
      } // end of prev
      if (getRole(treeItem.getParentNode()).equals(ROLE_TREE)) {
        if (!wrap) {
          return null;
        }
        Node tree = getTree(treeItem);
        return (delta == 1)? getFirstTreeItem(tree) : getLastTreeItem(tree);
      }
      return getRelativeTreeItem(treeItem.getParentNode(), delta, wrap); // Recursive
    }

    public void toggleExpandCollapse(Element treeItem, boolean isItemOpen)
    {
      treeItem.setAttribute("aria-expanded", isItemOpen? "false" : "true");
      Node image = treeItem.getPreviousSibling();
      if (isText(image)) {
        image = image.getPreviousSibling();
      }
      Node group = treeItem.getParentNode();
      group = group.getNextSibling();
      if (isText(group)) {
        group = group.getNextSibling();
      }
      if (!isItemOpen) {
        ((Element)image).setAttribute("src",COLLAPSE);
        ((Element)group).setAttribute("class","group");  //treeitem is span within div followed by group div
      }
      else {
        ((Element)image).setAttribute("src",EXPAND);
        ((Element)group).setAttribute("class","collapsedgroup");
      }
    }

    public boolean treeItemEvent(Event event)
    { // debugOut(event.type + " : " + event.keyCode);
      Element treeItem = (Element)getTarget(event); //event.target;
      if (treeItem == this.treeElem) return false; // Por ejemplo cuando el usuario ha pulsado una letra pero no ha seleccionado antes ningún elemento del árbol, esto no estaba en el código original de JavaScript (que tiene el mismo error sin esta línea)

      if (treeItem.getTagName().toLowerCase().equals("img")) {
        Node tree = getTree(treeItem);
        // get the treeItem from tree.lastFocus
        if (((ItsNatNode)tree).getUserValue("lastFocus") == null) {
          ((ItsNatNode)tree).setUserValue("lastFocus", getFirstTreeItem(tree) );
        }
        treeItem = (Element)((ItsNatNode)tree).getUserValue("lastFocus");
      }
      String hasChildrenStr = treeItem.getAttribute("aria-expanded");
      boolean hasChildren = !hasChildrenStr.equals("");
      boolean isItemOpen = hasChildrenStr.equals("true");
      int focusDelta = 0;  // (-1 = up, 0 = no change, 1 = down)

      boolean toggleitem = false;
      if (event.getType().equals("dblclick") && (((MouseEvent)event).getButton() == 0)) {
        toggleitem = hasChildren;
        ((ItsNatNode)getTree(treeItem)).setUserValue("_incrementalString","");
      }
      else if (event.getType().equals("keydown")) {

        if (((ItsNatKeyEvent)event).getAltKey()) {
          return true;  // Browser should use this, the tree view doesnot need alt-modified keys
        }
        // XXX Implement multiple selection (ctrl+arrow, shift+arrow, ctrl+space)
        if (((ItsNatKeyEvent)event).getKeyCode() == KEY_HOME) {
          try {
            treeItemFocus((Element)getFirstTreeItem(getTree(treeItem)),true);
          } catch(Exception ex) { debugOut(ex.getMessage()); }
          
          return false;
        }
        if (((ItsNatKeyEvent)event).getKeyCode() == KEY_ENTER) {
          toggleitem = hasChildren;
        }
        else if (((ItsNatKeyEvent)event).getKeyCode() == KEY_DOWN) {
          focusDelta = 1;
        }
        else if (((ItsNatKeyEvent)event).getKeyCode() == KEY_UP) {
          focusDelta = -1;
        }
        else if (((ItsNatKeyEvent)event).getKeyCode() == KEY_LEFT) {
          if (isItemOpen) {
            toggleitem = true;
          }
          else {
            Node groupItem = treeItem.getParentNode().getParentNode();  // relies on treeitems being spans nested in divs
            if (getRole((Element)groupItem).equals(ROLE_GROUP)) {
              Node nextItem = getRelativeTreeItem(groupItem, -1, false);
              treeItemFocus((Element)nextItem, true);
            }
          }
        }
        else if (((ItsNatKeyEvent)event).getKeyCode() == KEY_RIGHT) {
          if (isItemOpen) {
            focusDelta = 1;
          }
          else {
            toggleitem = hasChildren;
          }
        }
        else if (((ItsNatKeyEvent)event).getKeyCode() == KEY_END) {
          try {
            Node newTreeItemFocus = getLastTreeItem(getTree(treeItem));
            treeItemFocus((Element)newTreeItemFocus,true);
          } catch(Exception ex) { debugOut(ex.getMessage()); }
          
          return false;   // Consume the event
        }
        else {
          return true;  // We didn't need key, don't consume event
        }
      }
      else if (event.getType().equals("keypress")) {
        // Implement incremental find here, instead of keydown, because we
        // need to capture printable characters in a keypress handler
        Node tree = getTree(treeItem);
        ((ItsNatNode)tree).setUserValue("_incrementalString","");
        int key;
        if (((ItsNatKeyEvent)event).getCharCode() != 0) {
          key = ((ItsNatKeyEvent)event).getCharCode();
        }
        else {
          key = ((ItsNatKeyEvent)event).getKeyCode();  // IE uses keyCode - for keypress event the value should be the same as event.charCode
        }
        if (key > 32 && !((ItsNatKeyEvent)event).getAltKey() && !((ItsNatKeyEvent)event).getCtrlKey() &&
           !((ItsNatKeyEvent)event).getShiftKey() && !((ItsNatKeyEvent)event).getMetaKey()) {
          //jmarranz: key = String.fromCharCode(key);
        }
        else {
          ((ItsNatNode)tree).setUserValue("_incrementalString","");
          return true;
        }
        key = Character.toLowerCase((char)key);
        long _lastKeyTime = 0;
        if (((ItsNatNode)tree).getUserValue("_lastKeyTime") != null)
            _lastKeyTime = ((Long)((ItsNatNode)tree).getUserValue("_lastKeyTime")).longValue();

        if (event.getTimeStamp() - _lastKeyTime > 1000) {
          ((ItsNatNode)tree).setUserValue("_incrementalString",Character.toString((char)key));
        }
        else {
          // IE does not have event.timeStamp so will always end up in this else in IE
            String _incrementalString = (String)((ItsNatNode)tree).getUserValue("_incrementalString");
            _incrementalString += key;
            ((ItsNatNode)tree).setUserValue("_incrementalString",_incrementalString);
         }
        ((ItsNatNode)tree).setUserValue("_lastKeyTime", new Long(event.getTimeStamp()));

        String incrementalString = (String)((ItsNatNode)tree).getUserValue("_incrementalString");
        int length = incrementalString.length();
        int charIndex = 1;
        while (charIndex < length && incrementalString.charAt(charIndex) == incrementalString.charAt(charIndex - 1))
          charIndex++;
        // If all letters in incremental string are same, just try to match the first one
        if (charIndex == length) {
          length = 1;
          incrementalString = incrementalString.substring(0, length);
        }
        Node origTreeItem = treeItem;
        if (length == 1) {
          treeItem = (Element)getRelativeTreeItem(treeItem.getParentNode(), 1, true);
        }
        do {
          String text = ((Text)treeItem.getFirstChild()).getData();
          if (((Text)treeItem.getFirstChild()).getData().substring(0, length).toLowerCase().equals(incrementalString)) {
            treeItemFocus((Element)treeItem, true);
            return false;
          }
          treeItem = (Element)getRelativeTreeItem(treeItem.getParentNode(), 1, true);
        } while (treeItem != origTreeItem);
        return false;
      }
      else if (event.getType().equals("click")) {
          ((ItsNatNode)getTree(treeItem)).setUserValue("_incrementalString","");
      }
      else {
        return true; // continue propagating event;
      }
      if (toggleitem) {
        toggleExpandCollapse(treeItem, isItemOpen);

        return false; // consume event
      }
      if (focusDelta != 0) {
        //try {
          // treeitem is a span, must pass parent div to getRelativeTreeItem();
          Node nextItem = getRelativeTreeItem(treeItem.getParentNode(), focusDelta, false);
          if (nextItem != null) {
            treeItemFocus((Element)nextItem, true);
          }
        //} catch(ex) { debugOut("error in focusDelta " + ex.message); }
        return false;
      }
      return true;  // Browser can still use event
    }


    public boolean isContainedChild(Node parent,Node item)
    {
      boolean bFound = false;
      if (parent.hasChildNodes()) {
        for (int i=0; i<parent.getChildNodes().getLength(); i++) {
          if (parent.getChildNodes().item(i) == item) {
            bFound = true;
          }
          else {
            bFound = isContainedChild(parent.getChildNodes().item(i),item);
          }
          if (bFound == true) {
            break;
          }
        }
      }
      return bFound;
    }

    public void imgToggle(Event event)
    {
      Element imgItem = (Element)getTarget(event);
      if ( (imgItem == null) || !imgItem.getTagName().toLowerCase().equals("img")) {
        itsNatDoc.addCodeToSend("alert('ERROR');");
      }
      Node treeItemDiv = imgItem.getParentNode();  // parent of expand/collapse img is div
      Element treeItem = (Element)getTreeItemFromPar(treeItemDiv);
      boolean hasChildren = !treeItem.getAttribute("aria-expanded").equals("");
      boolean isItemOpen = hasChildren && treeItem.getAttribute("aria-expanded").equals("true");
      Node tree= getTree(treeItem);
      if (isItemOpen) {
        // if closing: need to determine if current focus is on a treeitem within the hierarchy of this item - if so,
        // need to update focus to this item.
        // next sibling of expand/collapse item is group; check all children of group and its next siblings to see if any have focus
        Node searchStart = treeItemDiv.getNextSibling();
        while ((searchStart != null) && isText(searchStart) ) {
          searchStart = searchStart.getNextSibling();
        }
        boolean bFound = false;
        bFound = isContainedChild(searchStart, (Node)((ItsNatNode)tree).getUserValue("lastFocus"));
        if (bFound) {
          treeItemFocus(treeItem,false); //update stored focus item; focus will actually be set with setFocus() call below
        }
      }
      toggleExpandCollapse(treeItem, isItemOpen);
      setFocus((Node)((ItsNatNode)tree).getUserValue("lastFocus"));
    }

}
