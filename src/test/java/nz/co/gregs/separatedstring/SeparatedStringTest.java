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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

/**
 *
 * @author gregorygraham
 */
public class SeparatedStringTest {

  public SeparatedStringTest() {
  }

  @Test
  public void testBuilder() {
    Builder builder = SeparatedString.builder();

    MatcherAssert.assertThat(builder, notNullValue(Builder.class));

    SeparatedString separatedString = builder.getSeparatedString();

    MatcherAssert.assertThat(separatedString.getEmptyValue(), is(""));
    MatcherAssert.assertThat(separatedString.getEscapeChar(), is(""));
    MatcherAssert.assertThat(separatedString.getKeyValueSeparator(), is(""));
    MatcherAssert.assertThat(separatedString.getLineEnd(), is(""));
    MatcherAssert.assertThat(separatedString.getLineStart(), is(""));
    MatcherAssert.assertThat(separatedString.getNullRepresentation(), is("null"));
    MatcherAssert.assertThat(separatedString.getPrefix(), is(""));
    MatcherAssert.assertThat(separatedString.getSeparator(), is(" "));
    MatcherAssert.assertThat(separatedString.getSuffix(), is(""));
    MatcherAssert.assertThat(separatedString.getWrapAfter(), is(""));
    MatcherAssert.assertThat(separatedString.getWrapBefore(), is(""));
    MatcherAssert.assertThat(separatedString.isClosedLoop(), is(false));
    MatcherAssert.assertThat(separatedString.isEmpty(), is(true));
    MatcherAssert.assertThat(separatedString.isNotLoop(), is(true));
    MatcherAssert.assertThat(separatedString.isOpenLoop(), is(false));
    MatcherAssert.assertThat(separatedString.isRetainingNulls(), is(false));
    MatcherAssert.assertThat(separatedString.isTrimBlanks(), is(false));
    MatcherAssert.assertThat(separatedString.isUniqueValuesOnly(), is(false));
  }

  @Test
  public void testCreateEncoder() {
    Builder builder = SeparatedString.builder();
    Encoder encoder = builder.encoder();

    MatcherAssert.assertThat(encoder, notNullValue(Encoder.class));
  }

  @Test
  public void testCreateDecoder() {
    Builder builder = SeparatedString.builder();
    Decoder decoder = builder.decoder();

    MatcherAssert.assertThat(decoder, notNullValue(Decoder.class));
  }

  @Test
  public void testCreateBuilder() {
    Builder builder = SeparatedString.builder();
    Builder otherBuilder = new Builder(builder.getSeparatedString());

    MatcherAssert.assertThat(otherBuilder, notNullValue(Builder.class));
  }

  @Test
  public void testTrimBlanks() {
    Builder builder = SeparatedString.builder();
    builder.withBlanksTrimmed();
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.isTrimBlanks(), is(true));
  }

  @Test
  public void testWithOnlyUniqueValues() {
    Builder builder = SeparatedString.getBuilder();
    builder.withOnlyUniqueValues();
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.isUniqueValuesOnly(), is(true));
  }

  @Test
  public void testSeparator() {
    Builder builder = SeparatedString.builder();
    builder.separatedBy("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getSeparator(), is("~"));
  }

  @Test
  public void testWithEscapeChar() {
    Builder builder = SeparatedString.builder();
    builder.withEscapeChar("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getEscapeChar(), is("~"));
  }

  @Test
  public void testKeyValueSeparator() {
    Builder builder = SeparatedString.builder();
    builder.withKeyValueSeparator("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getKeyValueSeparator(), is("~"));
  }

  @Test
  public void testSetFormatFor() {
    DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH24:mm:ss").withZone(ZoneOffset.UTC);
    Builder builder = SeparatedString.builder();
    final Function<Instant, String> formatter = d -> DATETIME_FORMAT.format(d);
    builder.setFormatFor(Instant.class, formatter);
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getFormatterFor(Instant.now()), is(formatter));
  }

  @Test
  public void testWithNullAs() {
    Builder builder = SeparatedString.builder();
    builder.withNullsAs("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getNullRepresentation(), is("~"));
  }

  @Test
  public void testWithNoLoop() {
    Builder builder = SeparatedString.builder();
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.isNotLoop(), is(true));
    MatcherAssert.assertThat(separatedString.isOpenLoop(), is(false));
    MatcherAssert.assertThat(separatedString.isClosedLoop(), is(false));

    builder = SeparatedString.builder();
    builder.withClosedLoop();
    builder.withNoLoop();
    separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.isNotLoop(), is(true));
    MatcherAssert.assertThat(separatedString.isOpenLoop(), is(false));
    MatcherAssert.assertThat(separatedString.isClosedLoop(), is(false));
  }

  @Test
  public void testWithOpenLoop() {
    Builder builder = SeparatedString.builder();
    builder.withOpenLoop();
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.isNotLoop(), is(false));
    MatcherAssert.assertThat(separatedString.isOpenLoop(), is(true));
    MatcherAssert.assertThat(separatedString.isClosedLoop(), is(false));
  }

  @Test
  public void testWithClosedLoop() {
    Builder builder = SeparatedString.builder();
    builder.withClosedLoop();
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.isNotLoop(), is(false));
    MatcherAssert.assertThat(separatedString.isOpenLoop(), is(false));
    MatcherAssert.assertThat(separatedString.isClosedLoop(), is(true));
  }

  @Test
  public void testWithEachTermPrecededAndFollowedWith() {
    Builder builder = SeparatedString.builder();
    builder.withEachTermPrecededAndFollowedWith("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getWrapBefore(), is("~"));
    MatcherAssert.assertThat(separatedString.getWrapAfter(), is("~"));
  }

  @Test
  public void testWithEachTermWrappedWith() {
    Builder builder = SeparatedString.builder();
    builder.withEachTermWrappedWith("~", "!");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getWrapBefore(), is("~"));
    MatcherAssert.assertThat(separatedString.getWrapAfter(), is("!"));
  }

  @Test
  public void testWithThisBeforeEachTerm() {
    Builder builder = SeparatedString.builder();
    builder.withThisBeforeEachTerm("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getWrapBefore(), is("~"));
  }

  @Test
  public void testWithThisAfterEachTerm() {
    Builder builder = SeparatedString.builder();
    builder.withThisAfterEachTerm("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getWrapAfter(), is("~"));
  }

  @Test
  public void testWithPrefix() {
    Builder builder = SeparatedString.builder();
    builder.withPrefix("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getPrefix(), is("~"));
  }

  @Test
  public void testWithSuffix() {
    Builder builder = SeparatedString.builder();
    builder.withSuffix("!");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getSuffix(), is("!"));
  }

  @Test
  public void testWithNullsRetained() {
    Builder builder = SeparatedString.builder();
    builder.withNullsRetained(true);
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.isRetainingNulls(), is(true));
  }

  @Test
  public void testUseWhenEmpty() {
    Builder builder = SeparatedString.builder();
    builder.useWhenEmpty("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getEmptyValue(), is("~"));
  }

  @Test
  public void testWithLineEndSequence() {
    Builder builder = SeparatedString.builder();
    builder.withLineEndSequence("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getLineEnd(), is("~"));
  }

  @Test
  public void testWithLineStartSequence() {
    Builder builder = SeparatedString.builder();
    builder.withLineStartSequence("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getLineStart(), is("~"));
  }

  @Test
  public void testStartsWith() {
    Builder builder = SeparatedString.builder().startsWith("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getPrefix(), is("~"));
  }


}
