package org.itsnat.itsnatdroidtest.testact.local;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.itsnat.droid.ItsNatDroidException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEquals;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEqualsLinearLayoutLayoutParams;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEqualsRelativeLayoutLayoutParams;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEqualsViewGroupMarginLayoutParams;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertFalse;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertPositive;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertTrue;


/**
 * Created by jmarranz on 19/06/14.
 */
public class TestLocalXMLInflate
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

                TextView compTextView1 = (TextView) compRelLayout.getChildAt(0);
                TextView parsedTextView1 = (TextView) parsedRelLayout.getChildAt(0);
                assertEquals(compTextView1.getId(), parsedTextView1.getId());
                assertEquals(compTextView1.getText(), parsedTextView1.getText());
                assertEquals(compTextView1.getTextSize(), parsedTextView1.getTextSize());
                assertEquals(compTextView1.getTextColors(), parsedTextView1.getTextColors());
                assertEquals(compTextView1.getBackground(), parsedTextView1.getBackground());
                assertEqualsRelativeLayoutLayoutParams(compTextView1, parsedTextView1);

                TextView compTextView2 = (TextView) compRelLayout.getChildAt(1);
                TextView parsedTextView2 = (TextView) parsedRelLayout.getChildAt(1);
                assertEquals(compTextView2.getId(), parsedTextView2.getId());
                assertEquals(compTextView2.getText(), parsedTextView2.getText());
                assertEquals(compTextView2.getBackground(), parsedTextView2.getBackground());
                assertEqualsRelativeLayoutLayoutParams(compTextView2, parsedTextView2);
                // No tenemos una forma de testear "textAppearanceMedium" de forma directa, una forma es testear una de las propiedades que impone, ej el tamaño del texto
                assertEquals(compTextView2.getTextSize(), parsedTextView2.getTextSize());
            }
        }

        childCount++;

        // Testing custom currentTarget class
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
                TextView compTextView1 = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView1 = (TextView) parsedLinLayout.getChildAt(0);
                assertEquals(compTextView1.getText(), parsedTextView1.getText());
                assertEquals(compTextView1.getBackground(), compTextView1.getBackground());
                assertEqualsLinearLayoutLayoutParams(compTextView1, compTextView1);

                TextView compTextView2 = (TextView) compLinLayout.getChildAt(1);
                TextView parsedTextView2 = (TextView) parsedLinLayout.getChildAt(1);
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
                TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
                assertEqualsViewGroupMarginLayoutParams(compTextView, parsedTextView);
            }
        }

        childCount++;

        // Test LinearLayout gravity
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            {
                TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
                assertEquals(compTextView.getGravity(), parsedTextView.getGravity());
            }
        }

        childCount++;

        // Test View Attribs
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            {
                TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getAlpha(), parsedTextView.getAlpha());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
                assertEquals(compTextView.isClickable(), parsedTextView.isClickable());
                assertEquals(compTextView.getContentDescription(), parsedTextView.getContentDescription());
                assertEquals(compTextView.getDrawingCacheQuality(), parsedTextView.getDrawingCacheQuality());

                ScrollView compScrollView = (ScrollView) compLinLayout.getChildAt(1);
                ScrollView parsedScrollView = (ScrollView) parsedLinLayout.getChildAt(1);
                assertTrue(compScrollView.isScrollbarFadingEnabled()); // Correspondiente a requiresFadingEdge
                assertEquals(compScrollView.isScrollbarFadingEnabled(), parsedScrollView.isScrollbarFadingEnabled());
                assertPositive(compScrollView.getVerticalFadingEdgeLength());
                assertEquals(compScrollView.getVerticalFadingEdgeLength(), parsedScrollView.getVerticalFadingEdgeLength());
                assertPositive(compScrollView.getHorizontalFadingEdgeLength());
                assertEquals(compScrollView.getHorizontalFadingEdgeLength(), parsedScrollView.getHorizontalFadingEdgeLength());

                TextView compTextView2 = (TextView) compLinLayout.getChildAt(2);
                TextView parsedTextView2 = (TextView) parsedLinLayout.getChildAt(2);
                assertEquals(compTextView2.getText(), parsedTextView2.getText());
                assertTrue(compTextView2.getFilterTouchesWhenObscured());
                assertEquals(compTextView2.getFilterTouchesWhenObscured(), parsedTextView2.getFilterTouchesWhenObscured());
                assertTrue(compTextView2.isFocusable());
                assertEquals(compTextView2.isFocusable(), parsedTextView2.isFocusable());
                assertTrue(compTextView2.isFocusableInTouchMode());
                assertEquals(compTextView2.isFocusableInTouchMode(), parsedTextView2.isFocusableInTouchMode());
                assertFalse(compTextView2.isHapticFeedbackEnabled());
                assertEquals(compTextView2.isHapticFeedbackEnabled(), parsedTextView2.isHapticFeedbackEnabled());
                assertPositive(compTextView2.getId());
                assertEquals(compTextView2.getId(), parsedTextView2.getId());
                // No puedo testear android:isScrollContainer porque  isScrollContainer() se define en un Level superior

//System.out.println("DEFAULT VALUE: " + parsedTextView2.isScrollContainer());
//System.out.println("DEFAULT VALUE: " + execMethod(parsedTextView2,"isScrollContainer()",null,null));


                assertEquals(compTextView2.getRotation(), parsedTextView2.getRotation());
            }
        }


    }

    protected static Object execMethod(View view, String methodName, Class classParam,Object param)
    {
        try
        {
            if (classParam != null)
            {
                Method method = view.getClass().getMethod(methodName, classParam);
                return method.invoke(view, param);
            }
            else
            {
                Method method = view.getClass().getMethod(methodName);
                return method.invoke(view);
            }
        }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
