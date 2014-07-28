package org.itsnat.itsnatdroidtest;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static org.itsnat.itsnatdroidtest.Assert.*;


/**
 * Created by jmarranz on 19/06/14.
 */
public class TestXMLInflate
{
    public static void test(ScrollView compRoot,ScrollView parsedRoot)
    {
        // comp = "Layout compiled"
        // No podemos testear layout_width/height en el ScrollView root porque un View está desconectado y al desconectar el width y el height se ponen a 0
        // assertEquals(comp.getWidth(),parsed.getWidth());
        // assertEquals(comp.getHeight(),parsed.getHeight());

        LinearLayout comp = (LinearLayout)compRoot.getChildAt(0);
        LinearLayout parsed = (LinearLayout)parsedRoot.getChildAt(0);

        assertEquals(comp.getPaddingLeft(),parsed.getPaddingLeft());
        assertEquals(comp.getPaddingRight(),parsed.getPaddingRight());
        assertEquals(comp.getPaddingTop(),parsed.getPaddingTop());
        assertEquals(comp.getPaddingBottom(),parsed.getPaddingBottom());
        assertEquals(comp.getOrientation(),parsed.getOrientation());

        int childCount = 1;

        // buttonReload
        {
            Button compButton = (Button) comp.getChildAt(childCount);
            Button parsedButton = (Button) parsed.getChildAt(childCount);
            assertEquals(compButton.getId(), parsedButton.getId());
            assertEquals(compButton.getText(), parsedButton.getText());
        }

        childCount++;

        // Testing misc attribs
        {
            RelativeLayout compRelLayout = (RelativeLayout) comp.getChildAt(childCount);
            RelativeLayout parsedRelLayout = (RelativeLayout) parsed.getChildAt(childCount);
            {
                int childCountLevel2 = 0;

                TextView compTextView1 = (TextView) compRelLayout.getChildAt(childCountLevel2);
                TextView parsedTextView1 = (TextView) parsedRelLayout.getChildAt(childCountLevel2);
                assertEquals(compTextView1.getId(), parsedTextView1.getId());
                assertEquals(compTextView1.getText(), parsedTextView1.getText());
                assertEquals(compTextView1.getTextSize(), parsedTextView1.getTextSize());
                assertEquals(compTextView1.getBackground(), parsedTextView1.getBackground());
                assertEqualsRelativeLayoutLayoutParams(compTextView1, parsedTextView1);

                childCountLevel2++;

                TextView compTextView2 = (TextView) compRelLayout.getChildAt(childCountLevel2);
                TextView parsedTextView2 = (TextView) parsedRelLayout.getChildAt(childCountLevel2);
                assertEquals(compTextView2.getId(), parsedTextView2.getId());
                assertEquals(compTextView2.getText(), parsedTextView2.getText());
                assertEquals(compTextView2.getBackground(), parsedTextView2.getBackground());
                assertEqualsRelativeLayoutLayoutParams(compTextView2, parsedTextView2);
                // No tenemos una forma de testear "textAppearanceMedium" de forma directa, una forma es testear una de las propiedades que impone, ej el tamaño del texto
                assertEquals(compTextView2.getTextSize(), parsedTextView2.getTextSize());
            }
        }

        childCount++;

        // Testing custom view class
        {
            TextView compCustomTextView = (TextView) comp.getChildAt(childCount);
            TextView parsedCustomTextView = (TextView) parsed.getChildAt(childCount);
            assertEquals(compCustomTextView.getText(), parsedCustomTextView.getText());
            assertEquals(compCustomTextView.getBackground(), parsedCustomTextView.getBackground());
        }

        childCount++;

        // Testing layout_gravity y layout_weight
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            {
                int childCountLevel2 = 0;

                TextView compTextView1 = (TextView) compLinLayout.getChildAt(childCountLevel2);
                TextView parsedTextView1 = (TextView) parsedLinLayout.getChildAt(childCountLevel2);
                assertEquals(compTextView1.getText(), parsedTextView1.getText());
                assertEquals(compTextView1.getBackground(), compTextView1.getBackground());
                assertEqualsLinearLayoutLayoutParams(compTextView1, compTextView1);

                childCountLevel2++;

                TextView compTextView2 = (TextView) compLinLayout.getChildAt(childCountLevel2);
                TextView parsedTextView2 = (TextView) parsedLinLayout.getChildAt(childCountLevel2);
                assertEquals(compTextView2.getText(), parsedTextView2.getText());
                assertEquals(compTextView2.getBackground(), parsedTextView2.getBackground());
                assertEqualsLinearLayoutLayoutParams(compTextView2, parsedTextView2);
            }
        }

        childCount++;

        // Test margins
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            {
                int childCountLevel2 = 0;

                TextView compTextView = (TextView) compLinLayout.getChildAt(childCountLevel2);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(childCountLevel2);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
                assertEqualsViewGroupMarginLayoutParams(compTextView, parsedTextView);
            }
        }
    }
}
