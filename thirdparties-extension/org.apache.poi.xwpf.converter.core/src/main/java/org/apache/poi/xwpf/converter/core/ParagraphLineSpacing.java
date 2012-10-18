package org.apache.poi.xwpf.converter.core;

//         w:spacing w:after="200" w:line="276" w:lineRule="auto" /> 
public class ParagraphLineSpacing
{

    private final Float spaceBefore;

    private final Float spaceAfter;

    private final Float leading;

    private final Float multipleLeading;

    public ParagraphLineSpacing( Float spaceBefore, Float spaceAfter, Float leading, Float multipleLeading )
    {
        this.spaceBefore = spaceBefore;
        this.spaceAfter = spaceAfter;
        this.leading = leading;
        this.multipleLeading = multipleLeading;
    }

    public Float getSpaceBefore()
    {
        return spaceBefore;
    }

    public Float getSpaceAfter()
    {
        return spaceAfter;
    }

    public Float getLeading()
    {
        return leading;
    }

    public Float getMultipleLeading()
    {
        return multipleLeading;
    }

}
