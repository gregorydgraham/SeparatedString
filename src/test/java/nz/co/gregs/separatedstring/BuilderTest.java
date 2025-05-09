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
import org.hamcrest.MatcherAssert;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

/**
 *
 * @author gregorygraham
 */
public class BuilderTest {

  public BuilderTest() {
  }

  @Test
  public void testCreateBuilder() {
    Builder builder = Builder.start();

    MatcherAssert.assertThat(builder, notNullValue(Builder.class));
  }

  @Test
  public void testCreateEncoder() {
    Builder builder = Builder.start();
    Encoder encoder = builder.encoder();

    MatcherAssert.assertThat(encoder, notNullValue(Encoder.class));
  }

  @Test
  public void testCreateDecoder() {
    Builder builder = Builder.start();
    Decoder decoder = builder.decoder();

    MatcherAssert.assertThat(decoder, notNullValue(Decoder.class));
  }

  @Test
  public void testTrimBlanks() {
    Builder builder = Builder.start();
    builder.trimBlanks();
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.isTrimBlanks(), is(true));
  }

  @Test
  public void testWithOnlyUniqueValues() {
    Builder builder = Builder.start();
    builder.withOnlyUniqueValues();
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.isUniqueValuesOnly(), is(true));
  }

  @Test
  public void testSeparator() {
    Builder builder = Builder.start();
    builder.separatedBy("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getSeparator(), is("~"));
  }

  @Test
  public void testWithEscapeChar() {
    Builder builder = Builder.start();
    builder.withEscapeChar("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getEscapeChar(), is("~"));
  }

  @Test
  public void testKeyValueSeparator() {
    Builder builder = Builder.start();
    builder.withKeyValueSeparator("~");
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getKeyValueSeparator(), is("~"));
  }

  @Test
  public void testSetFormatFor() {
    DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH24:mm:ss").withZone(ZoneId.systemDefault());
    Builder builder = Builder.start();
    final Function<Instant, String> formatter = d -> DATETIME_FORMAT.format(d);
    builder.setFormatFor(Instant.class, formatter);
    SeparatedString separatedString = builder.getSeparatedString();
    MatcherAssert.assertThat(separatedString.getFormatterFor(Instant.now()), is(formatter));
  }

}
