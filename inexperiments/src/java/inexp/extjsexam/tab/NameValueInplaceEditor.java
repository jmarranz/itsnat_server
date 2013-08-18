
package inexp.extjsexam.tab;

import java.util.EventObject;
import javax.swing.event.CellEditorListener;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class NameValueInplaceEditor implements ItsNatTableCellEditor
{
    protected ItsNatTableCellEditor delegate;
    protected NameValue value;

    public NameValueInplaceEditor(ItsNatTableCellEditor delegate)
    {
        this.delegate = delegate;
    }

    public ItsNatComponent getTableCellEditorComponent(ItsNatTable table,
                int row,
                int column,
                Object value,
                boolean isSelected,
                Element cellContentElem)
    {
        this.value = (NameValue)value;
        return delegate.getTableCellEditorComponent(table,row,column,value.toString(),isSelected,cellContentElem);
    }

    public void cancelCellEditing()
    {
        delegate.cancelCellEditing();
    }

    public boolean stopCellEditing()
    {
        return delegate.stopCellEditing();
    }

    public Object getCellEditorValue()
    {
        String name = (String)delegate.getCellEditorValue();
        NameValue newValue = new NameValue(name,this.value.getValue());
        this.value = null;
        return newValue;
    }

    public boolean isCellEditable(EventObject anEvent)
    {
        return delegate.isCellEditable(anEvent);
    }

    public boolean shouldSelectCell(EventObject anEvent)
    {
        return delegate.shouldSelectCell(anEvent);
    }

    public void addCellEditorListener(CellEditorListener l)
    {
        delegate.addCellEditorListener(l);
    }

    public void removeCellEditorListener(CellEditorListener l)
    {
        delegate.removeCellEditorListener(l);
    }

}
