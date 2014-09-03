package org.itsnat.itsnatdroidtest.testact.local;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertNotNull;
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
                assertPositive(compTextView1.getPaddingLeft());
                assertEquals(compTextView1.getPaddingLeft(),parsedTextView1.getPaddingLeft());
                assertPositive(compTextView1.getPaddingRight());
                assertEquals(compTextView1.getPaddingRight(),parsedTextView1.getPaddingRight());
                assertPositive(compTextView1.getPaddingTop());
                assertEquals(compTextView1.getPaddingTop(),parsedTextView1.getPaddingTop());
                assertPositive(compTextView1.getPaddingBottom());
                assertEquals(compTextView1.getPaddingBottom(),parsedTextView1.getPaddingBottom());
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
                assertEquals(compTextView.getAlpha(),0.7f);
                assertEquals(compTextView.getAlpha(), parsedTextView.getAlpha());
                assertEquals(((ColorDrawable)compTextView.getBackground()).getColor(), 0xffdddddd);
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
                assertFalse(compTextView.isClickable());
                assertEquals(compTextView.isClickable(), parsedTextView.isClickable());
                assertEquals(compTextView.getContentDescription(), "For Testing View Attribs");
                assertEquals(compTextView.getContentDescription(), parsedTextView.getContentDescription());
                assertEquals(compTextView.getDrawingCacheQuality(), View.DRAWING_CACHE_QUALITY_HIGH);
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
                assertTrue(compTextView2.getKeepScreenOn());
                assertEquals(compTextView2.getKeepScreenOn(), parsedTextView2.getKeepScreenOn());
                assertEquals(compTextView2.getLayerType(), View.LAYER_TYPE_HARDWARE);
                assertEquals(compTextView2.getLayerType(), parsedTextView2.getLayerType());
                assertTrue(compTextView2.isLongClickable());
                assertEquals(compTextView2.isLongClickable(), parsedTextView2.isLongClickable());
                // No puedo testear android:minHeight porque  getMinimumHeight() se define en un Level superior
                // No puedo testear android:minWidth porque  getMinimumWidth() se define en un Level superior
                assertPositive(compTextView2.getNextFocusDownId());
                assertEquals(compTextView2.getNextFocusDownId(), parsedTextView2.getNextFocusDownId());
                assertPositive(compTextView2.getNextFocusForwardId());
                assertEquals(compTextView2.getNextFocusForwardId(), parsedTextView2.getNextFocusForwardId());
                assertPositive(compTextView2.getNextFocusLeftId());
                assertEquals(compTextView2.getNextFocusLeftId(), parsedTextView2.getNextFocusLeftId());
                assertPositive(compTextView2.getNextFocusRightId());
                assertEquals(compTextView2.getNextFocusRightId(), parsedTextView2.getNextFocusRightId());
                assertPositive(compTextView2.getNextFocusUpId());
                assertEquals(compTextView2.getNextFocusUpId(), parsedTextView2.getNextFocusUpId());
                // No puedo testear android:onClick porque no hay get nativo asociado
                assertPositive(compTextView2.getPaddingLeft());
                assertEquals(compTextView2.getPaddingLeft(),parsedTextView2.getPaddingLeft());
                assertPositive(compTextView2.getPaddingRight());
                assertEquals(compTextView2.getPaddingRight(),parsedTextView2.getPaddingRight());
                assertPositive(compTextView2.getPaddingTop());
                assertEquals(compTextView2.getPaddingTop(),parsedTextView2.getPaddingTop());
                assertPositive(compTextView2.getPaddingBottom());
                assertEquals(compTextView2.getPaddingBottom(),parsedTextView2.getPaddingBottom());
                assertPositive(compTextView2.getRotation());
                assertEquals(compTextView2.getRotation(),parsedTextView2.getRotation());
                assertPositive(compTextView2.getRotationX());
                assertEquals(compTextView2.getRotationX(),parsedTextView2.getRotationX());
                assertPositive(compTextView2.getRotationY());
                assertEquals(compTextView2.getRotationY(),parsedTextView2.getRotationY());
                assertFalse(compTextView2.isSaveEnabled());
                assertEquals(compTextView2.isSaveEnabled(),parsedTextView2.isSaveEnabled());
                assertPositive(compTextView2.getScaleX());
                assertEquals(compTextView2.getScaleX(),parsedTextView2.getScaleX());
                assertPositive(compTextView2.getScaleY());
                assertEquals(compTextView2.getScaleY(),parsedTextView2.getScaleY());
                // No testeamos android:scrollX y android:scrollY (con getScrollX() y getScrollY()) porque después de definirse correctamente
                // algo hace poner a cero los valores, quizás al insertar la View
                assertPositive(compTextView2.getScrollBarStyle());
                assertEquals(compTextView2.getScrollBarStyle(),parsedTextView2.getScrollBarStyle());
                assertFalse(compTextView2.isSoundEffectsEnabled());
                assertEquals(compTextView2.isSoundEffectsEnabled(),parsedTextView2.isSoundEffectsEnabled());
                assertEquals((String)compTextView2.getTag(),"theTag");
                assertEquals((String)compTextView2.getTag(),(String)parsedTextView2.getTag());
                assertPositive(compTextView2.getPivotX());
                assertEquals(compTextView2.getPivotX(),parsedTextView2.getPivotX());
                assertPositive(compTextView2.getPivotY());
                assertEquals(compTextView2.getPivotY(),parsedTextView2.getPivotY());
                assertPositive(compTextView2.getTranslationX());
                assertEquals(compTextView2.getTranslationX(),parsedTextView2.getTranslationX());
                assertPositive(compTextView2.getTranslationY());
                assertEquals(compTextView2.getTranslationY(),parsedTextView2.getTranslationY());
            }

        }

        childCount++;

        // Test ViewGroup Attribs
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            assertTrue(compLinLayout.addStatesFromChildren());
            assertEquals(compLinLayout.addStatesFromChildren(), parsedLinLayout.addStatesFromChildren());
            assertFalse(compLinLayout.isAlwaysDrawnWithCacheEnabled());
            assertEquals(compLinLayout.isAlwaysDrawnWithCacheEnabled(), parsedLinLayout.isAlwaysDrawnWithCacheEnabled());
            assertNotNull(compLinLayout.getLayoutTransition()); // Test de android:animateLayoutChanges
            assertNotNull(parsedLinLayout.getLayoutTransition()); // "
            assertFalse(compLinLayout.isAnimationCacheEnabled());
            assertEquals(compLinLayout.isAnimationCacheEnabled(), parsedLinLayout.isAnimationCacheEnabled());
            // No podemos testear android:clipChildren porque no tenemos el método get es Level 18
            // No podemos testear android:clipToPadding porque no tenemos un método get
            assertEquals(compLinLayout.getDescendantFocusability(),ViewGroup.FOCUS_AFTER_DESCENDANTS);
            assertEquals(compLinLayout.getDescendantFocusability(), parsedLinLayout.getDescendantFocusability());
            assertPositive(compLinLayout.getLayoutAnimation().getDelay()); // Testeamos el delay porque testear la igualdad del LayoutAnimationController es un rollo
            assertEquals(compLinLayout.getLayoutAnimation().getDelay(), parsedLinLayout.getLayoutAnimation().getDelay());
            assertEquals(compLinLayout.getPersistentDrawingCache(),parsedLinLayout.getPersistentDrawingCache());
            assertTrue(compLinLayout.isMotionEventSplittingEnabled());
            assertEquals(compLinLayout.isMotionEventSplittingEnabled(),parsedLinLayout.isMotionEventSplittingEnabled());
            {
                TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
            }
        }

        childCount++;

        // Test FrameLayout Attribs
        {
            FrameLayout compFrameLayout = (FrameLayout) comp.getChildAt(childCount);
            FrameLayout parsedFrameLayout = (FrameLayout) parsed.getChildAt(childCount);

            assertEquals(((ColorDrawable)compFrameLayout.getForeground()).getColor(), 0x55ddffdd);
            assertEquals(compFrameLayout.getForeground(), parsedFrameLayout.getForeground());
            // No podemos testear android:foregroundGravity porque getForegroundGravity() es Level 16
            assertTrue(compFrameLayout.getMeasureAllChildren());
            assertEquals(compFrameLayout.getMeasureAllChildren(), parsedFrameLayout.getMeasureAllChildren());
        }

        childCount++;

        // Test LinearLayout Attribs
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);

            assertFalse(compLinLayout.isBaselineAligned());
            assertEquals(compLinLayout.isBaselineAligned(), parsedLinLayout.isBaselineAligned());
            //assertEquals(compLinLayout.getBaselineAlignedChildIndex(), 1);
            //assertEquals(compLinLayout.getBaselineAlignedChildIndex(), parsedLinLayout.getBaselineAlignedChildIndex());
            // No podemos testear android:divider porque getDividerDrawable() es Level 16
            assertEquals(compLinLayout.getShowDividers(), 3);
            assertEquals(compLinLayout.getShowDividers(),parsedLinLayout.getShowDividers());
            assertPositive(compLinLayout.getDividerPadding());
            assertEquals(compLinLayout.getDividerPadding(),parsedLinLayout.getDividerPadding());

            assertTrue(compLinLayout.isMeasureWithLargestChildEnabled());
            assertEquals(compLinLayout.isMeasureWithLargestChildEnabled(),parsedLinLayout.isMeasureWithLargestChildEnabled());
            assertEquals(compLinLayout.getWeightSum(),1.0f);
            assertEquals(compLinLayout.getWeightSum(),parsedLinLayout.getWeightSum());



System.out.println("\n\n\nDEFAULT VALUE: " + compLinLayout.getShowDividers() + " " + parsedLinLayout.getShowDividers());
System.out.println("\n\n\n");
//System.out.println("DEFAULT VALUE: " + execMethod(parsedTextView2,"isScrollContainer()",null,null));

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
