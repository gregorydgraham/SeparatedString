/*
 * Copyright 2025 Gregory Graham.
 *
 * Commercial licenses are available, please contact info@gregs.co.nz for details.
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/ 
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * 
 * You are free to:
 *     Share - copy and redistribute the material in any medium or format
 *     Adapt - remix, transform, and build upon the material
 * 
 *     The licensor cannot revoke these freedoms as long as you follow the license terms.               
 *     Under the following terms:
 *                 
 *         Attribution - 
 *             You must give appropriate credit, provide a link to the license, and indicate if changes were made. 
 *             You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 *         NonCommercial - 
 *             You may not use the material for commercial purposes.
 *         ShareAlike - 
 *             If you remix, transform, or build upon the material, 
 *             you must distribute your contributions under the same license as the original.
 *         No additional restrictions - 
 *             You may not apply legal terms or technological measures that legally restrict others from doing anything the 
 *             license permits.
 * 
 * Check the Creative Commons website for any details, legalese, and updates.
 */
package nz.co.gregs.separatedstring;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

/**
 *
 * @author Gregory Graham
 */
public class EncoderTest {

  public EncoderTest() {
  }

  @Test
  public void testEncoder() {
    Builder builder = Builder.go();

    assertThat(builder, notNullValue(Builder.class));

    SeparatedString separatedString = builder.getSeparatedString();

    assertThat(separatedString.getEmptyValue(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
    assertThat(separatedString.getKeyValueSeparator(), is(""));
    assertThat(separatedString.getLineEnd(), is(""));
    assertThat(separatedString.getLineStart(), is(""));
    assertThat(separatedString.getNullRepresentation(), is("null"));
    assertThat(separatedString.getPrefix(), is(""));
    assertThat(separatedString.getSeparator(), is(" "));
    assertThat(separatedString.getSuffix(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.isClosedLoop(), is(false));
    assertThat(separatedString.isEmpty(), is(true));
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isRetainingNulls(), is(false));
    assertThat(separatedString.isTrimBlanks(), is(false));
    assertThat(separatedString.isUniqueValuesOnly(), is(false));

    Encoder encoder = builder.encoder();
    assertThat(encoder, notNullValue(Encoder.class));

    separatedString = encoder.getSeparatedString();

    assertThat(separatedString.getEmptyValue(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
    assertThat(separatedString.getKeyValueSeparator(), is(""));
    assertThat(separatedString.getLineEnd(), is(""));
    assertThat(separatedString.getLineStart(), is(""));
    assertThat(separatedString.getNullRepresentation(), is("null"));
    assertThat(separatedString.getPrefix(), is(""));
    assertThat(separatedString.getSeparator(), is(" "));
    assertThat(separatedString.getSuffix(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.isClosedLoop(), is(false));
    assertThat(separatedString.isEmpty(), is(true));
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isRetainingNulls(), is(false));
    assertThat(separatedString.isTrimBlanks(), is(false));
    assertThat(separatedString.isUniqueValuesOnly(), is(false));
  }

  @Test
  public void testCreateEncoder() {
    final Encoder encoder = Builder.go().encoder();
    Encoder otherEncoder = new Encoder(encoder.getSeparatedString());
    assertThat(otherEncoder, notNullValue(Encoder.class));
  }

  @Test
  public void testCreateDecoder() {
    Builder builder = Builder.start();
    Encoder encoder = builder.encoder();
    Decoder decoder = encoder.decoder();

    assertThat(decoder, notNullValue(Decoder.class));
  }

  @Test
  public void testCreateBuilder() {
    Encoder encoder = Builder.start().encoder();
    Builder otherBuilder = new Builder(encoder.getSeparatedString());

    assertThat(otherBuilder, notNullValue(Builder.class));
  }

  @Test
  public void testTrimBlanks() {
    Encoder encoder = Builder.start().separatedBy(",").withBlanksTrimmed().encoder();
    SeparatedString separatedString = encoder.getSeparatedString();
    assertThat(separatedString.isTrimBlanks(), is(true));
    encoder.add("blart");
    encoder.add("blert   ");
    encoder.add("    blirt  ");
    String encode = encoder.encode();
    assertThat(encode, is("blart,blert,blirt"));
  }

  @Test
  public void testWithOnlyUniqueValues() {
    Builder builder = Builder.start();
    builder.withOnlyUniqueValues();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.isUniqueValuesOnly(), is(true));
  }

  @Test
  public void testSeparator() {
    Builder builder = Builder.start();
    builder.separatedBy("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is("~"));
  }

  @Test
  public void testWithEscapeChar() {
    Builder builder = Builder.start();
    builder.withEscapeChar("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getEscapeChar(), is("~"));
  }

  @Test
  public void testKeyValueSeparator() {
    Builder builder = Builder.start();
    builder.withKeyValueSeparator("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getKeyValueSeparator(), is("~"));
  }

  @Test
  public void testSetFormatFor() {
    DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH24:mm:ss").withZone(ZoneId.systemDefault());
    Builder builder = Builder.start();
    final Function<Instant, String> formatter = d -> DATETIME_FORMAT.format(d);
    builder.setFormatFor(Instant.class, formatter);
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getFormatterFor(Instant.now()), is(formatter));
  }

  @Test
  public void testWithNullAs() {
    Builder builder = Builder.start();
    builder.withNullsAs("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getNullRepresentation(), is("~"));
  }

  @Test
  public void testWithNoLoop() {
    Builder builder = Builder.start();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isClosedLoop(), is(false));

    builder = Builder.start();
    builder.withClosedLoop();
    builder.withNoLoop();
    separatedString = builder.getSeparatedString();
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isClosedLoop(), is(false));
  }

  @Test
  public void testWithOpenLoop() {
    Builder builder = Builder.start();
    builder.withOpenLoop();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.isNotLoop(), is(false));
    assertThat(separatedString.isOpenLoop(), is(true));
    assertThat(separatedString.isClosedLoop(), is(false));
  }

  @Test
  public void testWithClosedLoop() {
    Builder builder = Builder.go();
    builder.withClosedLoop();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.isNotLoop(), is(false));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isClosedLoop(), is(true));
  }

  @Test
  public void testWithEachTermPrecededAndFollowedWith() {
    Builder builder = Builder.start();
    builder.withEachTermPrecededAndFollowedWith("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getWrapBefore(), is("~"));
    assertThat(separatedString.getWrapAfter(), is("~"));
  }

  @Test
  public void testWithEachTermWrappedWith() {
    Builder builder = Builder.start();
    builder.withEachTermWrappedWith("~", "!");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getWrapBefore(), is("~"));
    assertThat(separatedString.getWrapAfter(), is("!"));
  }

  @Test
  public void testWithThisBeforeEachTerm() {
    Builder builder = Builder.start();
    builder.withThisBeforeEachTerm("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getWrapBefore(), is("~"));
  }

  @Test
  public void testWithThisAfterEachTerm() {
    Builder builder = Builder.start();
    builder.withThisAfterEachTerm("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getWrapAfter(), is("~"));
  }

  @Test
  public void testWithPrefix() {
    Builder builder = Builder.start();
    builder.withPrefix("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getPrefix(), is("~"));
  }

  @Test
  public void testWithSuffix() {
    Builder builder = Builder.start();
    builder.withSuffix("!");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSuffix(), is("!"));
  }

  @Test
  public void testWithNullsRetained() {
    Builder builder = Builder.start();
    builder.withNullsRetained(true);
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.isRetainingNulls(), is(true));
  }

  @Test
  public void testUseWhenEmpty() {
    Builder builder = Builder.start();
    builder.useWhenEmpty("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getEmptyValue(), is("~"));
  }

  @Test
  public void testWithLineEndSequence() {
    Builder builder = Builder.start();
    builder.withLineEndSequence("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getLineEnd(), is("~"));
  }

  @Test
  public void testWithLineStartSequence() {
    Builder builder = Builder.start();
    builder.withLineStartSequence("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getLineStart(), is("~"));
  }

  @Test
  public void testCSV() {
    Builder csv = Builder.csv();
    SeparatedString separatedString = csv.getSeparatedString();
    assertThat(separatedString.getSeparator(), is(", "));
    assertThat(separatedString.getWrapBefore(), is("\""));
    assertThat(separatedString.getWrapAfter(), is("\""));
    assertThat(separatedString.getEscapeChar(), is("\\"));
    assertThat(separatedString.getKeyValueSeparator(), is("="));
  }

  @Test
  public void testTSV() {
    Builder tsv = Builder.tsv();
    SeparatedString separatedString = tsv.getSeparatedString();

    assertThat(separatedString.getSeparator(), is("\t"));
    assertThat(separatedString.getWrapBefore(), is("\""));
    assertThat(separatedString.getWrapAfter(), is("\""));
    assertThat(separatedString.getEscapeChar(), is("\\"));
    assertThat(separatedString.getKeyValueSeparator(), is("="));
  }

  @Test
  public void testHtmlOrderedList() {
    Builder tsv = Builder.htmlOrderedList();
    SeparatedString separatedString = tsv.getSeparatedString();

    assertThat(separatedString.getSeparator(), is("\n"));
    assertThat(separatedString.getWrapBefore(), is("<li>"));
    assertThat(separatedString.getWrapAfter(), is("</li>"));
    assertThat(separatedString.getPrefix(), is("<ol>\n"));
    assertThat(separatedString.getSuffix(), is("\n</ol>\n"));
  }

  @Test
  public void testHtmlUnorderedList() {
    Builder tsv = Builder.htmlUnorderedList();
    SeparatedString separatedString = tsv.getSeparatedString();

    assertThat(separatedString.getSeparator(), is("\n"));
    assertThat(separatedString.getWrapBefore(), is("<li>"));
    assertThat(separatedString.getWrapAfter(), is("</li>"));
    assertThat(separatedString.getPrefix(), is("<ul>\n"));
    assertThat(separatedString.getSuffix(), is("\n</ul>\n"));
  }

  @Test
  public void testStartsWith() {
    Builder builder = Builder.startsWith("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getPrefix(), is("~"));
  }

  @Test
  public void testForSeparator() {
    Builder builder = Builder.forSeparator("~");
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is("~"));
  }

  @Test
  public void testBySpaces() {
    Builder builder = Builder.bySpaces();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is(" "));
  }

  @Test
  public void testByCommas() {
    Builder builder = Builder.byCommas();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is(","));
  }

  @Test
  public void testByCommaSpace() {
    Builder builder = Builder.byCommaSpace();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is(", "));
  }

  @Test
  public void testByCommasWithQuotedTermsAndBackslashEscape() {
    Builder builder = Builder.byCommasWithQuotedTermsAndBackslashEscape();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is(", "));
    assertThat(separatedString.getWrapBefore(), is("\""));
    assertThat(separatedString.getWrapAfter(), is("\""));
    assertThat(separatedString.getEscapeChar(), is("\\"));
  }

  @Test
  public void testByCommasWithQuotedTermsAndDoubleBackslashEscape() {
    Builder builder = Builder.byCommasWithQuotedTermsAndDoubleBackslashEscape();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is(","));
    assertThat(separatedString.getWrapBefore(), is("\""));
    assertThat(separatedString.getWrapAfter(), is("\""));
    assertThat(separatedString.getEscapeChar(), is("\\\\"));
  }

  @Test
  public void testByTabs() {
    Builder builder = Builder.byTabs();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is("\t"));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
  }

  @Test
  public void testByLines() {
    Builder builder = Builder.byLines();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is("\n"));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
  }

  @Test
  public void testSpaceSeparated() {
    Builder builder = Builder.spaceSeparated();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is(" "));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
  }

  @Test
  public void testCommaSeparated() {
    Builder builder = Builder.commaSeparated();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is(","));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
  }

  @Test
  public void testTabSeparated() {
    Builder builder = Builder.tabSeparated();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is("\t"));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
  }

  @Test
  public void testLineSeparated() {
    Builder builder = Builder.lineSeparated();
    SeparatedString separatedString = builder.getSeparatedString();
    assertThat(separatedString.getSeparator(), is("\n"));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
  }

}
