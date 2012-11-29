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

package org.itsnat.impl.core.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSS2Properties;

/**
 *
 * @author jmarranz
 */
public class CSS2PropertiesImpl extends CSSStyleDeclarationImpl implements CSS2Properties
{

    /** Creates a new instance of CSS2PropertiesImpl */
    public CSS2PropertiesImpl(Element parent)
    {
        super(parent);
    }

    private String getPropertyValueInternal(String propertyName)
    {
        return getPropertyValue(propertyName);
    }

    private void setPropertyValueInternal(String propertyName,String value)
    {
        // Son tantas las propiedades que unificamos en un sóo sitio todo lo que se haga común por si acaso
        // hay que hacer algo más
        setProperty(propertyName,value,"");
    }

    public String getAzimuth()
    {
        return getPropertyValueInternal("azimuth");
    }

    public void setAzimuth(String azimuth) throws DOMException
    {
        setPropertyValueInternal("azimuth",azimuth);
    }

    public String getBackground()
    {
        return getPropertyValueInternal("background");
    }

    public void setBackground(String background) throws DOMException
    {
        setPropertyValueInternal("background",background);
    }

    public String getBackgroundAttachment()
    {
        return getPropertyValueInternal("background-attachment");
    }

    public void setBackgroundAttachment(String backgroundAttachment) throws DOMException
    {
        setPropertyValueInternal("background-attachment",backgroundAttachment);
    }

    public String getBackgroundColor()
    {
        return getPropertyValueInternal("background-color");
    }

    public void setBackgroundColor(String backgroundColor) throws DOMException
    {
        setPropertyValueInternal("background-color",backgroundColor);
    }

    public String getBackgroundImage()
    {
        return getPropertyValueInternal("background-image");
    }

    public void setBackgroundImage(String backgroundImage) throws DOMException
    {
        setPropertyValueInternal("background-image",backgroundImage);
    }

    public String getBackgroundPosition()
    {
        return getPropertyValueInternal("background-position");
    }

    public void setBackgroundPosition(String backgroundPosition) throws DOMException
    {
        setPropertyValueInternal("background-position",backgroundPosition);
    }

    public String getBackgroundRepeat()
    {
        return getPropertyValueInternal("background-repeat");
    }

    public void setBackgroundRepeat(String backgroundRepeat) throws DOMException
    {
        setPropertyValueInternal("background-repeat",backgroundRepeat);
    }

    public String getBorder()
    {
        return getPropertyValueInternal("border");
    }

    public void setBorder(String border) throws DOMException
    {
        setPropertyValueInternal("border",border);
    }

    public String getBorderCollapse()
    {
        return getPropertyValueInternal("border-collapse");
    }

    public void setBorderCollapse(String borderCollapse) throws DOMException
    {
        setPropertyValueInternal("border-collapse",borderCollapse);
    }

    public String getBorderColor()
    {
        return getPropertyValueInternal("border-color");
    }

    public void setBorderColor(String borderColor) throws DOMException
    {
        setPropertyValueInternal("border-color",borderColor);
    }

    public String getBorderSpacing()
    {
        return getPropertyValueInternal("border-spacing");
    }

    public void setBorderSpacing(String borderSpacing) throws DOMException
    {
        setPropertyValueInternal("border-spacing",borderSpacing);
    }

    public String getBorderStyle()
    {
        return getPropertyValueInternal("border-style");
    }

    public void setBorderStyle(String borderStyle) throws DOMException
    {
        setPropertyValueInternal("border-style",borderStyle);
    }

    public String getBorderTop()
    {
        return getPropertyValueInternal("border-top");
    }

    public void setBorderTop(String borderTop) throws DOMException
    {
        setPropertyValueInternal("border-top",borderTop);
    }

    public String getBorderRight()
    {
        return getPropertyValueInternal("border-right");
    }

    public void setBorderRight(String borderRight) throws DOMException
    {
        setPropertyValueInternal("border-right",borderRight);
    }

    public String getBorderBottom()
    {
        return getPropertyValueInternal("border-bottom");
    }

    public void setBorderBottom(String borderBottom) throws DOMException
    {
        setPropertyValueInternal("border-bottom",borderBottom);
    }

    public String getBorderLeft()
    {
        return getPropertyValueInternal("border-left");
    }

    public void setBorderLeft(String borderLeft) throws DOMException
    {
        setPropertyValueInternal("border-left",borderLeft);
    }

    public String getBorderTopColor()
    {
        return getPropertyValueInternal("border-top-color");
    }

    public void setBorderTopColor(String borderTopColor) throws DOMException
    {
        setPropertyValueInternal("border-top-color",borderTopColor);
    }

    public String getBorderRightColor()
    {
        return getPropertyValueInternal("border-right-color");
    }

    public void setBorderRightColor(String borderRightColor) throws DOMException
    {
        setPropertyValueInternal("border-right-color",borderRightColor);
    }

    public String getBorderBottomColor()
    {
        return getPropertyValueInternal("border-bottom-color");
    }

    public void setBorderBottomColor(String borderBottomColor) throws DOMException
    {
        setPropertyValueInternal("border-bottom-color",borderBottomColor);
    }

    public String getBorderLeftColor()
    {
        return getPropertyValueInternal("border-left-color");
    }

    public void setBorderLeftColor(String borderLeftColor) throws DOMException
    {
        setPropertyValueInternal("border-left-color",borderLeftColor);
    }

    public String getBorderTopStyle()
    {
        return getPropertyValueInternal("border-top-style");
    }

    public void setBorderTopStyle(String borderTopStyle) throws DOMException
    {
        setPropertyValueInternal("border-top-style",borderTopStyle);
    }

    public String getBorderRightStyle()
    {
        return getPropertyValueInternal("border-right-style");
    }

    public void setBorderRightStyle(String borderRightStyle) throws DOMException
    {
        setPropertyValueInternal("border-right-style",borderRightStyle);
    }

    public String getBorderBottomStyle()
    {
        return getPropertyValueInternal("border-bottom-style");
    }

    public void setBorderBottomStyle(String borderBottomStyle) throws DOMException
    {
        setPropertyValueInternal("border-bottom-style",borderBottomStyle);
    }

    public String getBorderLeftStyle()
    {
        return getPropertyValueInternal("border-left-style");
    }

    public void setBorderLeftStyle(String borderLeftStyle) throws DOMException
    {
        setPropertyValueInternal("border-left-style",borderLeftStyle);
    }

    public String getBorderTopWidth()
    {
        return getPropertyValueInternal("border-top-width");
    }

    public void setBorderTopWidth(String borderTopWidth) throws DOMException
    {
        setPropertyValueInternal("border-top-width",borderTopWidth);
    }

    public String getBorderRightWidth()
    {
        return getPropertyValueInternal("border-right-width");
    }

    public void setBorderRightWidth(String borderRightWidth) throws DOMException
    {
        setPropertyValueInternal("border-right-width",borderRightWidth);
    }

    public String getBorderBottomWidth()
    {
        return getPropertyValueInternal("border-bottom-width");
    }

    public void setBorderBottomWidth(String borderBottomWidth) throws DOMException
    {
        setPropertyValueInternal("border-bottom-width",borderBottomWidth);
    }

    public String getBorderLeftWidth()
    {
        return getPropertyValueInternal("border-left-width");
    }

    public void setBorderLeftWidth(String borderLeftWidth) throws DOMException
    {
        setPropertyValueInternal("border-left-width",borderLeftWidth);
    }

    public String getBorderWidth()
    {
        return getPropertyValueInternal("border-width");
    }

    public void setBorderWidth(String borderWidth) throws DOMException
    {
        setPropertyValueInternal("border-width",borderWidth);
    }

    public String getBottom()
    {
        return getPropertyValueInternal("bottom");
    }

    public void setBottom(String bottom) throws DOMException
    {
        setPropertyValueInternal("bottom",bottom);
    }

    public String getCaptionSide()
    {
        return getPropertyValueInternal("caption-side");
    }

    public void setCaptionSide(String captionSide) throws DOMException
    {
        setPropertyValueInternal("caption-side",captionSide);
    }

    public String getClear()
    {
        return getPropertyValueInternal("clear");
    }

    public void setClear(String clear) throws DOMException
    {
        setPropertyValueInternal("clear",clear);
    }

    public String getClip()
    {
        return getPropertyValueInternal("clip");
    }

    public void setClip(String clip) throws DOMException
    {
        setPropertyValueInternal("clip",clip);
    }

    public String getColor()
    {
        return getPropertyValueInternal("color");
    }

    public void setColor(String color) throws DOMException
    {
        setPropertyValueInternal("color",color);
    }

    public String getContent()
    {
        return getPropertyValueInternal("content");
    }

    public void setContent(String content) throws DOMException
    {
        setPropertyValueInternal("content",content);
    }

    public String getCounterIncrement()
    {
        return getPropertyValueInternal("counter-increment");
    }

    public void setCounterIncrement(String counterIncrement) throws DOMException
    {
        setPropertyValueInternal("counter-increment",counterIncrement);
    }

    public String getCounterReset()
    {
        return getPropertyValueInternal("counter-reset");
    }

    public void setCounterReset(String counterReset) throws DOMException
    {
        setPropertyValueInternal("counter-reset",counterReset);
    }

    public String getCue()
    {
        return getPropertyValueInternal("cue");
    }

    public void setCue(String cue) throws DOMException
    {
        setPropertyValueInternal("cue",cue);
    }

    public String getCueAfter()
    {
        return getPropertyValueInternal("cue-after");
    }

    public void setCueAfter(String cueAfter) throws DOMException
    {
        setPropertyValueInternal("cue-after",cueAfter);
    }

    public String getCueBefore()
    {
        return getPropertyValueInternal("cue-before");
    }

    public void setCueBefore(String cueBefore) throws DOMException
    {
        setPropertyValueInternal("cue-before",cueBefore);
    }

    public String getCursor()
    {
        return getPropertyValueInternal("cursor");
    }

    public void setCursor(String cursor) throws DOMException
    {
        setPropertyValueInternal("cursor",cursor);
    }

    public String getDirection()
    {
        return getPropertyValueInternal("direction");
    }

    public void setDirection(String direction) throws DOMException
    {
        setPropertyValueInternal("direction",direction);
    }

    public String getDisplay()
    {
        return getPropertyValueInternal("display");
    }

    public void setDisplay(String display) throws DOMException
    {
        setPropertyValueInternal("display",display);
    }

    public String getElevation()
    {
        return getPropertyValueInternal("elevation");
    }

    public void setElevation(String elevation) throws DOMException
    {
        setPropertyValueInternal("elevation",elevation);
    }

    public String getEmptyCells()
    {
        return getPropertyValueInternal("empty-cells");
    }

    public void setEmptyCells(String emptyCells) throws DOMException
    {
        setPropertyValueInternal("empty-cells",emptyCells);
    }

    public String getCssFloat()
    {
        return getPropertyValueInternal("float");
    }

    public void setCssFloat(String cssFloat) throws DOMException
    {
        setPropertyValueInternal("float",cssFloat);
    }

    public String getFont()
    {
        return getPropertyValueInternal("font");
    }

    public void setFont(String font) throws DOMException
    {
        setPropertyValueInternal("font",font);
    }

    public String getFontFamily()
    {
        return getPropertyValueInternal("font-family");
    }

    public void setFontFamily(String fontFamily) throws DOMException
    {
        setPropertyValueInternal("font-family",fontFamily);
    }

    public String getFontSize()
    {
        return getPropertyValueInternal("font-size");
    }

    public void setFontSize(String fontSize) throws DOMException
    {
        setPropertyValueInternal("font-size",fontSize);
    }

    public String getFontSizeAdjust()
    {
        return getPropertyValueInternal("font-size-adjust");
    }

    public void setFontSizeAdjust(String fontSizeAdjust) throws DOMException
    {
        setPropertyValueInternal("font-size-adjust",fontSizeAdjust);
    }

    public String getFontStretch()
    {
        return getPropertyValueInternal("font-strech");
    }

    public void setFontStretch(String fontStretch) throws DOMException
    {
        setPropertyValueInternal("font-strech",fontStretch);
    }

    public String getFontStyle()
    {
        return getPropertyValueInternal("font-style");
    }

    public void setFontStyle(String fontStyle) throws DOMException
    {
        setPropertyValueInternal("font-style",fontStyle);
    }

    public String getFontVariant()
    {
        return getPropertyValueInternal("font-variant");
    }

    public void setFontVariant(String fontVariant) throws DOMException
    {
        setPropertyValueInternal("font-variant",fontVariant);
    }

    public String getFontWeight()
    {
        return getPropertyValueInternal("font-weight");
    }

    public void setFontWeight(String fontWeight) throws DOMException
    {
        setPropertyValueInternal("font-weight",fontWeight);
    }

    public String getHeight()
    {
        return getPropertyValueInternal("height");
    }

    public void setHeight(String height) throws DOMException
    {
        setPropertyValueInternal("height",height);
    }

    public String getLeft()
    {
        return getPropertyValueInternal("left");
    }

    public void setLeft(String left) throws DOMException
    {
        setPropertyValueInternal("left",left);
    }

    public String getLetterSpacing()
    {
        return getPropertyValueInternal("letter-spacing");
    }

    public void setLetterSpacing(String letterSpacing) throws DOMException
    {
        setPropertyValueInternal("letter-spacing",letterSpacing);
    }

    public String getLineHeight()
    {
        return getPropertyValueInternal("line-height");
    }

    public void setLineHeight(String lineHeight) throws DOMException
    {
        setPropertyValueInternal("line-height",lineHeight);
    }

    public String getListStyle()
    {
        return getPropertyValueInternal("list-style");
    }

    public void setListStyle(String listStyle) throws DOMException
    {
        setPropertyValueInternal("list-style",listStyle);
    }

    public String getListStyleImage()
    {
        return getPropertyValueInternal("list-style-image");
    }

    public void setListStyleImage(String listStyleImage) throws DOMException
    {
        setPropertyValueInternal("list-style-image",listStyleImage);
    }

    public String getListStylePosition()
    {
        return getPropertyValueInternal("list-style-position");
    }

    public void setListStylePosition(String listStylePosition) throws DOMException
    {
        setPropertyValueInternal("list-style-position",listStylePosition);
    }

    public String getListStyleType()
    {
        return getPropertyValueInternal("list-style-type");
    }

    public void setListStyleType(String listStyleType) throws DOMException
    {
        setPropertyValueInternal("list-style-type",listStyleType);
    }

    public String getMargin()
    {
        return getPropertyValueInternal("margin");
    }

    public void setMargin(String margin) throws DOMException
    {
        setPropertyValueInternal("margin",margin);
    }

    public String getMarginTop()
    {
        return getPropertyValueInternal("margin-top");
    }

    public void setMarginTop(String marginTop) throws DOMException
    {
        setPropertyValueInternal("margin-top",marginTop);
    }

    public String getMarginRight()
    {
        return getPropertyValueInternal("margin-right");
    }

    public void setMarginRight(String marginRight) throws DOMException
    {
        setPropertyValueInternal("margin-right",marginRight);
    }

    public String getMarginBottom()
    {
        return getPropertyValueInternal("margin-bottom");
    }

    public void setMarginBottom(String marginBottom) throws DOMException
    {
        setPropertyValueInternal("margin-bottom",marginBottom);
    }

    public String getMarginLeft()
    {
        return getPropertyValueInternal("margin-left");
    }

    public void setMarginLeft(String marginLeft) throws DOMException
    {
        setPropertyValueInternal("margin-left",marginLeft);
    }

    public String getMarkerOffset()
    {
        return getPropertyValueInternal("marker-offset");
    }

    public void setMarkerOffset(String markerOffset) throws DOMException
    {
        setPropertyValueInternal("marker-offset",markerOffset);
    }

    public String getMarks()
    {
        return getPropertyValueInternal("marks");
    }

    public void setMarks(String marks) throws DOMException
    {
        setPropertyValueInternal("marks",marks);
    }

    public String getMaxHeight()
    {
        return getPropertyValueInternal("max-height");
    }

    public void setMaxHeight(String maxHeight) throws DOMException
    {
        setPropertyValueInternal("max-height",maxHeight);
    }

    public String getMaxWidth()
    {
        return getPropertyValueInternal("max-width");
    }

    public void setMaxWidth(String maxWidth) throws DOMException
    {
        setPropertyValueInternal("max-width",maxWidth);
    }

    public String getMinHeight()
    {
        return getPropertyValueInternal("min-height");
    }

    public void setMinHeight(String minHeight) throws DOMException
    {
        setPropertyValueInternal("min-height",minHeight);
    }

    public String getMinWidth()
    {
        return getPropertyValueInternal("min-width");
    }

    public void setMinWidth(String minWidth) throws DOMException
    {
        setPropertyValueInternal("min-width",minWidth);
    }

    public String getOrphans()
    {
        return getPropertyValueInternal("orphans");
    }

    public void setOrphans(String orphans) throws DOMException
    {
        setPropertyValueInternal("orphans",orphans);
    }

    public String getOutline()
    {
        return getPropertyValueInternal("outline");
    }

    public void setOutline(String outline) throws DOMException
    {
        setPropertyValueInternal("outline",outline);
    }

    public String getOutlineColor()
    {
        return getPropertyValueInternal("outline-color");
    }

    public void setOutlineColor(String outlineColor) throws DOMException
    {
        setPropertyValueInternal("outline-color",outlineColor);
    }

    public String getOutlineStyle()
    {
        return getPropertyValueInternal("outline-style");
    }

    public void setOutlineStyle(String outlineStyle) throws DOMException
    {
        setPropertyValueInternal("outline-style",outlineStyle);
    }

    public String getOutlineWidth()
    {
        return getPropertyValueInternal("outline-width");
    }

    public void setOutlineWidth(String outlineWidth) throws DOMException
    {
        setPropertyValueInternal("outline-width",outlineWidth);
    }

    public String getOverflow()
    {
        return getPropertyValueInternal("overflow");
    }

    public void setOverflow(String overflow) throws DOMException
    {
        setPropertyValueInternal("overflow",overflow);
    }

    public String getPadding()
    {
        return getPropertyValueInternal("padding");
    }

    public void setPadding(String padding) throws DOMException
    {
        setPropertyValueInternal("padding",padding);
    }

    public String getPaddingTop()
    {
        return getPropertyValueInternal("padding-top");
    }

    public void setPaddingTop(String paddingTop) throws DOMException
    {
        setPropertyValueInternal("padding-top",paddingTop);
    }

    public String getPaddingRight()
    {
        return getPropertyValueInternal("padding-right");
    }

    public void setPaddingRight(String paddingRight) throws DOMException
    {
        setPropertyValueInternal("padding-right",paddingRight);
    }

    public String getPaddingBottom()
    {
        return getPropertyValueInternal("padding-bottom");
    }

    public void setPaddingBottom(String paddingBottom) throws DOMException
    {
        setPropertyValueInternal("padding-bottom",paddingBottom);
    }

    public String getPaddingLeft()
    {
        return getPropertyValueInternal("padding-left");
    }

    public void setPaddingLeft(String paddingLeft) throws DOMException
    {
        setPropertyValueInternal("padding-left",paddingLeft);
    }

    public String getPage()
    {
        return getPropertyValueInternal("page");
    }

    public void setPage(String page) throws DOMException
    {
        setPropertyValueInternal("page",page);
    }

    public String getPageBreakAfter()
    {
        return getPropertyValueInternal("page-break-after");
    }

    public void setPageBreakAfter(String pageBreakAfter) throws DOMException
    {
        setPropertyValueInternal("page-break-after",pageBreakAfter);
    }

    public String getPageBreakBefore()
    {
        return getPropertyValueInternal("page-break-before");
    }

    public void setPageBreakBefore(String pageBreakBefore) throws DOMException
    {
        setPropertyValueInternal("page-break-before",pageBreakBefore);
    }

    public String getPageBreakInside()
    {
        return getPropertyValueInternal("page-break-inside");
    }

    public void setPageBreakInside(String pageBreakInside) throws DOMException
    {
        setPropertyValueInternal("page-break-inside",pageBreakInside);
    }

    public String getPause()
    {
        return getPropertyValueInternal("pause");
    }

    public void setPause(String pause) throws DOMException
    {
        setPropertyValueInternal("pause",pause);
    }

    public String getPauseAfter()
    {
        return getPropertyValueInternal("pause-after");
    }

    public void setPauseAfter(String pauseAfter) throws DOMException
    {
        setPropertyValueInternal("pause-after",pauseAfter);
    }

    public String getPauseBefore()
    {
        return getPropertyValueInternal("pause-before");
    }

    public void setPauseBefore(String pauseBefore) throws DOMException
    {
        setPropertyValueInternal("pause-before",pauseBefore);
    }

    public String getPitch()
    {
        return getPropertyValueInternal("pitch");
    }

    public void setPitch(String pitch) throws DOMException
    {
        setPropertyValueInternal("pitch",pitch);
    }

    public String getPitchRange()
    {
        return getPropertyValueInternal("pitch-range");
    }

    public void setPitchRange(String pitchRange) throws DOMException
    {
        setPropertyValueInternal("pitch-range",pitchRange);
    }

    public String getPlayDuring()
    {
        return getPropertyValueInternal("play-during");
    }

    public void setPlayDuring(String playDuring) throws DOMException
    {
        setPropertyValueInternal("play-during",playDuring);
    }

    public String getPosition()
    {
        return getPropertyValueInternal("position");
    }

    public void setPosition(String position) throws DOMException
    {
        setPropertyValueInternal("position",position);
    }

    public String getQuotes()
    {
        return getPropertyValueInternal("quotes");
    }

    public void setQuotes(String quotes) throws DOMException
    {
        setPropertyValueInternal("quotes",quotes);
    }

    public String getRichness()
    {
        return getPropertyValueInternal("richness");
    }

    public void setRichness(String richness) throws DOMException
    {
        setPropertyValueInternal("richness",richness);
    }

    public String getRight()
    {
        return getPropertyValueInternal("right");
    }

    public void setRight(String right) throws DOMException
    {
        setPropertyValueInternal("right",right);
    }

    public String getSize()
    {
        return getPropertyValueInternal("size");
    }

    public void setSize(String size) throws DOMException
    {
        setPropertyValueInternal("size",size);
    }

    public String getSpeak()
    {
        return getPropertyValueInternal("speak");
    }

    public void setSpeak(String speak) throws DOMException
    {
        setPropertyValueInternal("speak",speak);
    }

    public String getSpeakHeader()
    {
        return getPropertyValueInternal("speak-header");
    }

    public void setSpeakHeader(String speakHeader) throws DOMException
    {
        setPropertyValueInternal("speak-header",speakHeader);
    }

    public String getSpeakNumeral()
    {
        return getPropertyValueInternal("speak-numeral");
    }

    public void setSpeakNumeral(String speakNumeral) throws DOMException
    {
        setPropertyValueInternal("speak-numeral",speakNumeral);
    }

    public String getSpeakPunctuation()
    {
        return getPropertyValueInternal("speak-punctuation");
    }

    public void setSpeakPunctuation(String speakPunctuation) throws DOMException
    {
        setPropertyValueInternal("speak-punctuation",speakPunctuation);
    }

    public String getSpeechRate()
    {
        return getPropertyValueInternal("speech-rate");
    }

    public void setSpeechRate(String speechRate) throws DOMException
    {
        setPropertyValueInternal("speech-rate",speechRate);
    }

    public String getStress()
    {
        return getPropertyValueInternal("stress");
    }

    public void setStress(String stress) throws DOMException
    {
        setPropertyValueInternal("stress",stress);
    }

    public String getTableLayout()
    {
        return getPropertyValueInternal("table-layout");
    }

    public void setTableLayout(String tableLayout) throws DOMException
    {
        setPropertyValueInternal("table-layout",tableLayout);
    }

    public String getTextAlign()
    {
        return getPropertyValueInternal("text-align");
    }

    public void setTextAlign(String textAlign) throws DOMException
    {
        setPropertyValueInternal("text-align",textAlign);
    }

    public String getTextDecoration()
    {
        return getPropertyValueInternal("text-decoration");
    }

    public void setTextDecoration(String textDecoration) throws DOMException
    {
        setPropertyValueInternal("text-decoration",textDecoration);
    }

    public String getTextIndent()
    {
        return getPropertyValueInternal("text-indent");
    }

    public void setTextIndent(String textIndent) throws DOMException
    {
        setPropertyValueInternal("text-indent",textIndent);
    }

    public String getTextShadow()
    {
        return getPropertyValueInternal("text-shadow");
    }

    public void setTextShadow(String textShadow) throws DOMException
    {
        setPropertyValueInternal("text-shadow",textShadow);
    }

    public String getTextTransform()
    {
        return getPropertyValueInternal("text-transform");
    }

    public void setTextTransform(String textTransform) throws DOMException
    {
        setPropertyValueInternal("text-transform",textTransform);
    }

    public String getTop()
    {
        return getPropertyValueInternal("top");
    }

    public void setTop(String top) throws DOMException
    {
        setPropertyValueInternal("top",top);
    }

    public String getUnicodeBidi()
    {
        return getPropertyValueInternal("unicode-bidi");
    }

    public void setUnicodeBidi(String unicodeBidi) throws DOMException
    {
        setPropertyValueInternal("unicode-bidi",unicodeBidi);
    }

    public String getVerticalAlign()
    {
        return getPropertyValueInternal("vertical-align");
    }

    public void setVerticalAlign(String verticalAlign) throws DOMException
    {
        setPropertyValueInternal("vertical-align",verticalAlign);
    }

    public String getVisibility()
    {
        return getPropertyValueInternal("visibility");
    }

    public void setVisibility(String visibility) throws DOMException
    {
        setPropertyValueInternal("visibility",visibility);
    }

    public String getVoiceFamily()
    {
        return getPropertyValueInternal("voice-family");
    }

    public void setVoiceFamily(String voiceFamily) throws DOMException
    {
        setPropertyValueInternal("voice-family",voiceFamily);
    }

    public String getVolume()
    {
        return getPropertyValueInternal("volume");
    }

    public void setVolume(String volume) throws DOMException
    {
        setPropertyValueInternal("volume",volume);
    }

    public String getWhiteSpace()
    {
        return getPropertyValueInternal("white-space");
    }

    public void setWhiteSpace(String whiteSpace) throws DOMException
    {
        setPropertyValueInternal("white-space",whiteSpace);
    }

    public String getWidows()
    {
        return getPropertyValueInternal("widows");
    }

    public void setWidows(String widows) throws DOMException
    {
        setPropertyValueInternal("widows",widows);
    }

    public String getWidth()
    {
        return getPropertyValueInternal("width");
    }

    public void setWidth(String width) throws DOMException
    {
        setPropertyValueInternal("width",width);
    }

    public String getWordSpacing()
    {
        return getPropertyValueInternal("word-spacing");
    }

    public void setWordSpacing(String wordSpacing) throws DOMException
    {
        setPropertyValueInternal("word-spacing",wordSpacing);
    }

    public String getZIndex()
    {
        return getPropertyValueInternal("z-index");
    }

    public void setZIndex(String zIndex) throws DOMException
    {
        setPropertyValueInternal("z-index",zIndex);
    }

}
