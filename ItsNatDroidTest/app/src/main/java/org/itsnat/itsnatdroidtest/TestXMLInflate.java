package org.itsnat.itsnatdroidtest;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static org.itsnat.itsnatdroidtest.Assert.*;
import static org.itsnat.itsnatdroidtest.Assert.assertEquals;

/**
 * Created by jmarranz on 19/06/14.
 */
public class TestXMLInflate
{
    public static void test(RelativeLayout comp,RelativeLayout parsed)
    {
        // comp = "Layut compiled"
        // No podemos testear layout_width/height porque un View está desconectado y al desconectar el width y el height se ponen a 0
        // assertEquals(comp.getWidth(),fromxml.getWidth());
        // assertEquals(comp.getHeight(),fromxml.getHeight());

        assertEquals(comp.getPaddingLeft(),parsed.getPaddingLeft());
        assertEquals(comp.getPaddingRight(),parsed.getPaddingRight());
        assertEquals(comp.getPaddingTop(),parsed.getPaddingTop());
        assertEquals(comp.getPaddingBottom(),parsed.getPaddingBottom());

        TextView compTextView1 = (TextView)comp.getChildAt(0);
        TextView parsedTextView1 = (TextView)parsed.getChildAt(0);
        assertEquals(compTextView1.getId(),parsedTextView1.getId());
        assertEquals(compTextView1.getText(),parsedTextView1.getText());
        assertEquals(compTextView1.getTextSize(),parsedTextView1.getTextSize());
        assertEquals(compTextView1.getBackground(),parsedTextView1.getBackground());
        assertEqualsRelativeLayoutLayoutParams(compTextView1, parsedTextView1);

        TextView compTextView2 = (TextView)comp.getChildAt(1);
        TextView parsedTextView2 = (TextView)parsed.getChildAt(1);
        assertEquals(compTextView2.getId(),parsedTextView2.getId());
        assertEquals(compTextView2.getText(),parsedTextView2.getText());
        assertEquals(compTextView2.getBackground(),parsedTextView2.getBackground());
        assertEqualsRelativeLayoutLayoutParams(compTextView2,parsedTextView2);
        // No tenemos una forma de testear "textAppearanceMedium" de forma directa, una forma es testear una de las propiedades que impone, ej el tamaño del texto
        assertEquals(compTextView2.getTextSize(),parsedTextView2.getTextSize());

        TextView compCustomTextView = (TextView)comp.getChildAt(2);
        TextView parsedCustomTextView = (TextView)parsed.getChildAt(2);
        assertEquals(compCustomTextView.getId(),parsedCustomTextView.getId());
        assertEquals(compCustomTextView.getText(),parsedCustomTextView.getText());
        assertEquals(compCustomTextView.getBackground(),parsedCustomTextView.getBackground());
        assertEqualsRelativeLayoutLayoutParams(compCustomTextView,parsedCustomTextView);

        Button compButton = (Button)comp.getChildAt(3);
        Button parsedButton = (Button)parsed.getChildAt(3);
        assertEquals(compButton.getId(),parsedButton.getId());
        assertEquals(compButton.getText(),parsedButton.getText());
        assertEqualsRelativeLayoutLayoutParams(compButton, parsedButton);

    }
}
