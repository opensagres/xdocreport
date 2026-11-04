# PDF converter test fonts

These fonts are used only by the synthetic DOCX/ODT Unicode tests. They remain under the SIL Open Font License 1.1; their copyright notices and full licence texts are included beside the font files.

- `NotoSansSC.ttf` is a test subset of the OFL-licensed Noto Sans SC font. It was generated with Apache PDFBox FontBox 3.0.8 `TTFSubsetter`. Its PostScript name has the `XDocReportTest` prefix; its primary family name does not use the reserved name `Source`. It retains U+0020-U+007E, U+00C9, U+0301, U+20AC, U+4E2D and U+6751, covering the Latin, combining and CJK test cases.
- `NotoSansDeseret.ttf` supplies U+10437 for the supplementary code point regression.

Other characters are intentionally absent from the Noto Sans SC subset.