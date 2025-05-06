/*
 * Copyright 2019 Gregory Graham.
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import nz.co.gregs.looper.Looper;
import static org.hamcrest.MatcherAssert.assertThat;
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
  public void testSimpleParsing() {
    final SeparatedString sepString = SeparatedStringBuilder.byCommas();
    sepString.addAll("aaa", "bbb", "ccc");
    final String encoded = sepString.toString();
    assertThat(encoded, is("aaa,bbb,ccc"));

    String[] parsed = sepString.parseToArray(encoded);
    assertThat(parsed.length, is(3));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("bbb"));
    assertThat(parsed[2], is("ccc"));
  }

  @Test
  public void testSimpleDateFormatting() {
    SeparatedString sepString = new SeparatedString().separatedBy(", ");
    sepString.setFormatFor(Instant.class, (Instant d) -> {
      return (DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneId.systemDefault()).format(d));
    });
    sepString.addAll("alpha", Instant.ofEpochMilli(0l), "beta");
    String result = sepString.encode();

    System.out.println("RESULT: " + result);
    assertThat(result, is("alpha, 1970/01/01, beta"));
  }

  @Test
  public void testCSVWithMultipleLines() {
    final SeparatedString csv = SeparatedStringBuilder.byCommas();
    final SeparatedString lineSep = SeparatedStringBuilder.byLines();
    csv.addLine("a", "1");
    csv.addLine("b", "2");
    csv.addLine("c", "3");
    assertThat(csv.encode(), is("a,1" + lineSep.getSeparator() + "b,2" + lineSep.getSeparator() + "c,3" + lineSep.getSeparator()));

    String[] parsed = csv.parseToArray(csv.encode());
    assertThat(parsed.length, is(7));
    assertThat(parsed[0], is("a"));
    assertThat(parsed[1], is("1"));
    assertThat(parsed[2], is("b"));
    assertThat(parsed[3], is("2"));
    assertThat(parsed[4], is("c"));
    assertThat(parsed[5], is("3"));
    // the last line is blank
    assertThat(parsed[6], is(""));

    csv.addAll("d", "4");
    parsed = csv.parseToArray(csv.encode());
    assertThat(parsed.length, is(8));
    assertThat(parsed[0], is("a"));
    assertThat(parsed[1], is("1"));
    assertThat(parsed[2], is("b"));
    assertThat(parsed[3], is("2"));
    assertThat(parsed[4], is("c"));
    assertThat(parsed[5], is("3"));
    assertThat(parsed[6], is("d"));
    assertThat(parsed[7], is("4"));

    List<List<String>> lines = csv.parseToLines(csv.encode());
    assertThat(lines.size(), is(4));
    assertThat(lines.get(0).get(0), is("a"));
    assertThat(lines.get(0).get(1), is("1"));
    assertThat(lines.get(1).get(0), is("b"));
    assertThat(lines.get(1).get(1), is("2"));
    assertThat(lines.get(2).get(0), is("c"));
    assertThat(lines.get(2).get(1), is("3"));
    assertThat(lines.get(3).get(0), is("d"));
    assertThat(lines.get(3).get(1), is("4"));
  }

  @Test
  public void testTSVWithMultipleLines() {
    final SeparatedString tsv = SeparatedStringBuilder.byTabs();
    final String lineSep = System.lineSeparator();
    tsv.addLine("a", "1");
    tsv.addLine("b", "2");
    tsv.addLine("c", "3");
    assertThat(tsv.encode(), is("a\t1" + lineSep + "b\t2" + lineSep + "c\t3" + lineSep));

    String[] parsed = tsv.parseToArray(tsv.encode());
    assertThat(parsed.length, is(7));
    assertThat(parsed[0], is("a"));
    assertThat(parsed[1], is("1"));
    assertThat(parsed[2], is("b"));
    assertThat(parsed[3], is("2"));
    assertThat(parsed[4], is("c"));
    assertThat(parsed[5], is("3"));
    // the last line is blank
    assertThat(parsed[6], is(""));

    tsv.addAll("d", "4");
    parsed = tsv.parseToArray(tsv.encode());
    assertThat(parsed.length, is(8));
    assertThat(parsed[0], is("a"));
    assertThat(parsed[1], is("1"));
    assertThat(parsed[2], is("b"));
    assertThat(parsed[3], is("2"));
    assertThat(parsed[4], is("c"));
    assertThat(parsed[5], is("3"));
    assertThat(parsed[6], is("d"));
    assertThat(parsed[7], is("4"));

    List<List<String>> lines = tsv.parseToLines(tsv.encode());
    assertThat(lines.size(), is(4));
    assertThat(lines.get(0).get(0), is("a"));
    assertThat(lines.get(0).get(1), is("1"));
    assertThat(lines.get(1).get(0), is("b"));
    assertThat(lines.get(1).get(1), is("2"));
    assertThat(lines.get(2).get(0), is("c"));
    assertThat(lines.get(2).get(1), is("3"));
    assertThat(lines.get(3).get(0), is("d"));
    assertThat(lines.get(3).get(1), is("4"));
  }

  @Test
  public void testSimpleParsingWithNulls() {
    final SeparatedString sepString = SeparatedStringBuilder.byCommas();
    sepString.addAll("aaa", "bbb", "ccc", null);
    final String encoded = sepString.toString();
    assertThat(encoded, is("aaa,bbb,ccc,"));

    String[] parsed = sepString.parseToArray(encoded);
    assertThat(parsed.length, is(4));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("bbb"));
    assertThat(parsed[2], is("ccc"));
    assertThat(parsed[3], is(""));
  }

  @Test
  public void testSimpleParsingWithNullsRetained() {
    final SeparatedString sepString = SeparatedStringBuilder.byCommas().withNullsRetained(true);
    sepString.addAll("aaa", "bbb", "ddd", "ccc", null);
    String encoded = sepString.toString();
    assertThat(encoded, is("aaa,bbb,ddd,ccc,null"));
    sepString.addAll("", "fff", "ggg", "hhh");
    encoded = sepString.toString();
    assertThat(encoded, is("aaa,bbb,ddd,ccc,null,,fff,ggg,hhh"));

    String[] parsed = sepString.parseToArray(encoded);
    assertThat(parsed.length, is(9));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("bbb"));
    assertThat(parsed[2], is("ddd"));
    assertThat(parsed[3], is("ccc"));
    assertThat(parsed[4], is("null"));
    assertThat(parsed[5], is(""));
    assertThat(parsed[6], is("fff"));
    assertThat(parsed[7], is("ggg"));
    assertThat(parsed[8], is("hhh"));
  }

  @Test
  public void testSimpleParsingWithNullsAs() {
    final SeparatedString sepString = SeparatedStringBuilder.byCommas().withNullsAs("[NULL]");
    sepString.addAll("aaa", "bbb", "ddd", "ccc", null);
    String encoded = sepString.toString();
    assertThat(encoded, is("aaa,bbb,ddd,ccc,[NULL]"));
    sepString.addAll("", "fff", "ggg", "hhh");
    encoded = sepString.toString();
    assertThat(encoded, is("aaa,bbb,ddd,ccc,[NULL],,fff,ggg,hhh"));

    String[] parsed = sepString.parseToArray(encoded);
    assertThat(parsed.length, is(9));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("bbb"));
    assertThat(parsed[2], is("ddd"));
    assertThat(parsed[3], is("ccc"));
    assertThat(parsed[4], is("[NULL]"));
    assertThat(parsed[5], is(""));
    assertThat(parsed[6], is("fff"));
    assertThat(parsed[7], is("ggg"));
    assertThat(parsed[8], is("hhh"));
  }

  @Test
  public void testCommaSpaceParsing() {
    final SeparatedString sepString = SeparatedStringBuilder.byCommaSpace();
    sepString.addAll("aaa", "bbb", "ddd", "ccc");
    final String encoded = sepString.toString();
    assertThat(encoded, is("aaa, bbb, ddd, ccc"));

    String[] parsed = sepString.parseToArray(encoded);
    assertThat(parsed.length, is(4));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("bbb"));
    assertThat(parsed[2], is("ddd"));
    assertThat(parsed[3], is("ccc"));
  }

  @Test
  public void testCommaSpaceParsingUsingEncodeStringArray() {
    final SeparatedString sepString = SeparatedStringBuilder.byCommaSpace();
    String encoded = sepString.encode("aaa", "bbb", "ddd", "ccc");
    assertThat(encoded, is("aaa, bbb, ddd, ccc"));

    String[] parsed = sepString.parseToArray(encoded);
    assertThat(parsed.length, is(4));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("bbb"));
    assertThat(parsed[2], is("ddd"));
    assertThat(parsed[3], is("ccc"));
  }

  @Test
  public void testCommaSpaceParsingUsingEncodeStringList() {
    List<String> arrayList = Arrays.asList(new String[]{"aaa", "bbb", "ddd", "ccc"});
    final SeparatedString sepString = SeparatedStringBuilder.byCommaSpace();
    String encoded = sepString.encode(arrayList);
    assertThat(encoded, is("aaa, bbb, ddd, ccc"));

    String[] parsed = sepString.parseToArray(encoded);
    assertThat(parsed.length, is(4));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("bbb"));
    assertThat(parsed[2], is("ddd"));
    assertThat(parsed[3], is("ccc"));
  }

  @Test
  public void testCommaSpaceParsingWithOnlyUniqueValues() {
    final SeparatedString sepString
            = SeparatedStringBuilder
                    .byCommaSpace()
                    .withOnlyUniqueValues();
    sepString.addAll("aaa", "bbb", "ddd", "ccc", "aaa", "ccc");
    final String encoded = sepString.toString();
    assertThat(encoded, is("aaa, bbb, ddd, ccc"));

    String[] parsed = sepString.parseToArray(encoded);
    assertThat(parsed.length, is(4));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("bbb"));
    assertThat(parsed[2], is("ddd"));
    assertThat(parsed[3], is("ccc"));
  }

  @Test
  public void testCommaSpaceParsingWithOnlyUniqueValuesFromInput() {
    final SeparatedString sepString
            = SeparatedStringBuilder
                    .byCommaSpace();
    sepString.addAll("aaa", "bbb", "ddd", "ccc", "aaa", "ccc");
    final String encoded = sepString.toString();
    assertThat(encoded, is("aaa, bbb, ddd, ccc, aaa, ccc"));

    String[] parsed = SeparatedStringBuilder
            .byCommaSpace().withOnlyUniqueValues().parseToArray(encoded);
    assertThat(parsed.length, is(4));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("bbb"));
    assertThat(parsed[2], is("ddd"));
    assertThat(parsed[3], is("ccc"));
  }

  @Test
  public void testQuotedParsing() {
    // 1000,117090058,117970084,"170,9 + 58","179,7 + 84","Flensburg Weiche, W 203 - Flensburg Grenze",Flensburg-Weiche - Flensb. Gr
    String[] parsed = SeparatedStringBuilder
            .byCommasWithQuotedTermsAndBackslashEscape()
            .parseToArray("1000,117090058,117970084,\"170,9 + 58\",\"179,7 + 84\",\"Flensburg Weiche, W 203 - Flensburg Grenze\",Flensburg-Weiche - Flensb. Gr");
//		Arrays.asList(parsed).stream().forEach((x) -> System.out.println(x));
    assertThat(parsed.length, is(7));
    assertThat(parsed[0], is("1000"));
    assertThat(parsed[1], is("117090058"));
    assertThat(parsed[2], is("117970084"));
    assertThat(parsed[3], is("170,9 + 58"));
    assertThat(parsed[4], is("179,7 + 84"));
    assertThat(parsed[5], is("Flensburg Weiche, W 203 - Flensburg Grenze"));
    assertThat(parsed[6], is("Flensburg-Weiche - Flensb. Gr"));
  }

  @Test
  public void testCustomParsing() {
    final SeparatedString sepString = SeparatedStringBuilder
            .byTabs()
            .withThisBeforeEachTerm("~\"")
            .withThisAfterEachTerm("\"~")
            .withEscapeChar("==");
// 1000,117090058,117970084,"170,9 + 58","179,7 + 84","Flensburg Weiche, W 203 - Flensburg Grenze",Flensburg-Weiche - Flensb. Gr
    sepString.addAll("1000", "117090058", "117970084", "170,9 + 58", "179,7 + 84", "Flensburg Weiche, W 203 - Flensburg Grenze", "Flensburg-Weiche - Flensb. Gr", "Albert \"The Pain\" Hallsburg");
    final String encoded = sepString.toString();
    assertThat(encoded, is("~\"1000\"~\t~\"117090058\"~\t~\"117970084\"~\t~\"170,9 + 58\"~\t~\"179,7 + 84\"~\t~\"Flensburg Weiche, W 203 - Flensburg Grenze\"~\t~\"Flensburg-Weiche - Flensb. Gr\"~\t~\"Albert \"The Pain\" Hallsburg\"~"));

    String[] parsed = sepString.parseToArray(encoded);
//		Arrays.asList(parsed).stream().forEach((x) -> System.out.println(x));
    assertThat(parsed.length, is(8));
    assertThat(parsed[0], is("1000"));
    assertThat(parsed[1], is("117090058"));
    assertThat(parsed[2], is("117970084"));
    assertThat(parsed[3], is("170,9 + 58"));
    assertThat(parsed[4], is("179,7 + 84"));
    assertThat(parsed[5], is("Flensburg Weiche, W 203 - Flensburg Grenze"));
    assertThat(parsed[6], is("Flensburg-Weiche - Flensb. Gr"));
    assertThat(parsed[7], is("Albert \"The Pain\" Hallsburg"));
  }

  @Test
  public void testBackslashEscapeChar() {
    final SeparatedString sepString = SeparatedStringBuilder
            .byCommaSpace()
            .withThisBeforeEachTerm("1")
            .withThisAfterEachTerm("8")
            .withEscapeChar("\\");
// 1000,117090058,117970084,"170,9 + 58","179,7 + 84","Flensburg Weiche, W 203 - Flensburg Grenze",Flensburg-Weiche - Flensb. Gr
    sepString.addAll("10\\00", "117090058", "117970084", "170,9 + 58", "179,7 + 84", "Flensburg Weiche, W 203 - Flensburg Grenze", "Flensburg-Weiche - Flensb. Gr", "Albert \"The Pain\" Hallsburg");
    final String encoded = sepString.toString();
    assertThat(encoded, is("1\\10\\\\008, 1\\1\\1709005\\88, 1\\1\\179700\\848, 1\\170,9 + 5\\88, 1\\179,7 + \\848, 1Flensburg Weiche\\, W 203 - Flensburg Grenze8, 1Flensburg-Weiche - Flensb. Gr8, 1Albert \"The Pain\" Hallsburg8"));

    String[] parsed = sepString.parseToArray(encoded);
    assertThat(parsed.length, is(8));
    assertThat(parsed[0], is("10\\00"));
    assertThat(parsed[1], is("117090058"));
    assertThat(parsed[2], is("117970084"));
    assertThat(parsed[3], is("170,9 + 58"));
    assertThat(parsed[4], is("179,7 + 84"));
    assertThat(parsed[5], is("Flensburg Weiche, W 203 - Flensburg Grenze"));
    assertThat(parsed[6], is("Flensburg-Weiche - Flensb. Gr"));
    assertThat(parsed[7], is("Albert \"The Pain\" Hallsburg"));
  }

  @Test
  public void testCanDecodeNestedSeparatedString() {
    final SeparatedString sepString = SeparatedStringBuilder
            .byCommaSpace()
            .withThisBeforeEachTerm("1")
            .withThisAfterEachTerm("8")
            .withEscapeChar("\\");
// 1000,117090058,117970084,"170,9 + 58","179,7 + 84","Flensburg Weiche, W 203 - Flensburg Grenze",Flensburg-Weiche - Flensb. Gr
    String[] values = {"10\\00", "117090058", "117970084", "170,9 + 58", "179,7 + 84", "Flensburg Weiche, W 203 - Flensburg Grenze", "Flensburg-Weiche - Flensb. Gr", "Albert \"The Pain\" Hallsburg"};
    sepString.addAll(values);//("10\\00", "117090058", "117970084", "170,9 + 58", "179,7 + 84", "Flensburg Weiche, W 203 - Flensburg Grenze", "Flensburg-Weiche - Flensb. Gr", "Albert \"The Pain\" Hallsburg");
    final String encoded = sepString.toString();
    assertThat(encoded, is("1\\10\\\\008, 1\\1\\1709005\\88, 1\\1\\179700\\848, 1\\170,9 + 5\\88, 1\\179,7 + \\848, 1Flensburg Weiche\\, W 203 - Flensburg Grenze8, 1Flensburg-Weiche - Flensb. Gr8, 1Albert \"The Pain\" Hallsburg8"));

    SeparatedString withNesting = SeparatedStringBuilder
            .byCommaSpace()
            .withThisBeforeEachTerm("1")
            .withThisAfterEachTerm("8")
            .withEscapeChar("\\");
    SeparatedString nested = withNesting.addAll("fivefivefive", encoded, "sixsixsix");
    final String nestedEncoded = nested.encode();
    assertThat(nestedEncoded, is("1fivefivefive8, 1\\1\\\\\\10\\\\\\\\00\\8\\, \\1\\\\\\1\\\\\\1709005\\\\\\8\\8\\, \\1\\\\\\1\\\\\\179700\\\\\\84\\8\\, \\1\\\\\\170,9 + 5\\\\\\8\\8\\, \\1\\\\\\179,7 + \\\\\\84\\8\\, \\1Flensburg Weiche\\\\\\, W 203 - Flensburg Grenze\\8\\, \\1Flensburg-Weiche - Flensb. Gr\\8\\, \\1Albert \"The Pain\" Hallsburg\\88, 1sixsixsix8"));
    final String[] nestedArray = nested.parseToArray(nestedEncoded);
    assertThat(nestedArray.length, is(3));
    assertThat(nestedArray[0], is("fivefivefive"));
    assertThat(nestedArray[1], is(encoded));
    assertThat(nestedArray[2], is("sixsixsix"));

    String[] parsed = sepString.parseToArray(nestedArray[1]);
    assertThat(parsed.length, is(values.length));
    Looper looper = Looper.loopUntilLimit(parsed.length);
    looper.loop(
            (in) -> {
              System.out.println((in.index())+" |parsed: "+parsed[in.index()]+" || "+values[in.index()]+" : values");
              assertThat(parsed[in.index()], is(values[in.index()]));
            });
  }

  @Test
  public void testCustomParsingWithRegexCharacters() {
    final SeparatedString sepString = SeparatedStringBuilder
            .byTabs()
            .withThisBeforeEachTerm("+")
            .withThisAfterEachTerm("+")
            .withEscapeChar("||");
// 1000,117090058,117970084,"170,9 + 58","179,7 + 84","Flensburg Weiche, W 203 - Flensburg Grenze",Flensburg-Weiche - Flensb. Gr
    sepString.addAll("1000", "117090058", "117970084", "170,9 + 58", "179,7 + 84", "Flensburg Weiche, W 203 - Flensburg Grenze", "Flensburg-Weiche - Flensb. Gr", "Albert \"The Pain\" Hallsburg");
    final String encoded = sepString.toString();
    assertThat(encoded, is("+1000+\t+117090058+\t+117970084+\t+170,9 ||+ 58+\t+179,7 ||+ 84+\t+Flensburg Weiche, W 203 - Flensburg Grenze+\t+Flensburg-Weiche - Flensb. Gr+\t+Albert \"The Pain\" Hallsburg+"));

    String[] parsed = sepString.parseToArray(encoded);
//		Arrays.asList(parsed).stream().forEach((x) -> System.out.println(x));
    assertThat(parsed.length, is(8));
    assertThat(parsed[0], is("1000"));
    assertThat(parsed[1], is("117090058"));
    assertThat(parsed[2], is("117970084"));
    assertThat(parsed[3], is("170,9 + 58"));
    assertThat(parsed[4], is("179,7 + 84"));
    assertThat(parsed[5], is("Flensburg Weiche, W 203 - Flensburg Grenze"));
    assertThat(parsed[6], is("Flensburg-Weiche - Flensb. Gr"));
    assertThat(parsed[7], is("Albert \"The Pain\" Hallsburg"));
  }

  @Test
  public void testWithWrappingCustomParsing() {
    final SeparatedString sepString = SeparatedStringBuilder
            .byTabs()
            .withThisBeforeEachTerm("~\"")
            .withThisAfterEachTerm("\"~")
            .withEscapeChar("==")
            .withPrefix("START")
            .withSuffix("END");
// 1000,117090058,117970084,"170,9 + 58","179,7 + 84","Flensburg Weiche, W 203 - Flensburg Grenze",Flensburg-Weiche - Flensb. Gr
    sepString.addAll("1000", "117090058~\"", "117970084", "170,9 + 58", "179,7 + 84", "Flensburg Weiche, W 203 - Flensburg Grenze", "Flensburg-Weiche - Flensb. Gr", "Albert \"The Pain\" Hallsburg");
    final String encoded = sepString.encode();
    assertThat(encoded, is("START~\"1000\"~\t~\"117090058==~\"\"~\t~\"117970084\"~\t~\"170,9 + 58\"~\t~\"179,7 + 84\"~\t~\"Flensburg Weiche, W 203 - Flensburg Grenze\"~\t~\"Flensburg-Weiche - Flensb. Gr\"~\t~\"Albert \"The Pain\" Hallsburg\"~END"));

    String[] parsed = sepString.parseToArray(encoded);
//		Arrays.asList(parsed).stream().forEach((x) -> System.out.println(x));
    assertThat(parsed.length, is(8));
    assertThat(parsed[0], is("1000"));
    assertThat(parsed[1], is("117090058~\""));
    assertThat(parsed[2], is("117970084"));
    assertThat(parsed[3], is("170,9 + 58"));
    assertThat(parsed[4], is("179,7 + 84"));
    assertThat(parsed[5], is("Flensburg Weiche, W 203 - Flensburg Grenze"));
    assertThat(parsed[6], is("Flensburg-Weiche - Flensb. Gr"));
    assertThat(parsed[7], is("Albert \"The Pain\" Hallsburg"));
  }

  @Test
  public void testEscapeParsing() {
    String[] parsed = SeparatedStringBuilder
            .byCommasWithQuotedTermsAndBackslashEscape()
            .parseToArray("aaa,\"b\\\"b\\\"b\",c\\,cc");
    assertThat(parsed.length, is(3));
    assertThat(parsed[0], is("aaa"));
    assertThat(parsed[1], is("b\"b\"b"));
    assertThat(parsed[2], is("c,cc"));
  }

  @Test
  public void testMapParsing() {
    final SeparatedString encoder = SeparatedStringBuilder.byCommas().withKeyValueSeparator("=");
    String encoded = encoder
            .add("left", "10px")
            .add("right", "20em")
            .add("border", 3)
            .encode();
    Map<String, String> parsed = encoder.parseToMap(encoded);
    assertThat(parsed.size(), is(3));
    assertThat(parsed.get("left"), is("10px"));
    assertThat(parsed.get("right"), is("20em"));
    assertThat(parsed.get("border"), is("3"));
    
    parsed = SeparatedStringBuilder
            .byCommasWithQuotedTermsAndBackslashEscape()
            .parseToMap("left=10px,right=20em,border=3");
    assertThat(parsed.size(), is(3));
    assertThat(parsed.get("left=10px"), isEmptyString());
    assertThat(parsed.get("right=20em"), isEmptyString());
    assertThat(parsed.get("border=3"), isEmptyString());

    parsed = SeparatedStringBuilder
            .byCommasWithQuotedTermsAndBackslashEscape()
            .withKeyValueSeparator("=")
            .parseToMap("left=10px,right=20em,border=3");
    assertThat(parsed.size(), is(3));
    assertThat(parsed.get("left"), is("10px"));
    assertThat(parsed.get("right"), is("20em"));
    assertThat(parsed.get("border"), is("3"));
  }

  @Test
  public void testloopControl() {
    // Test unaltered loops
    SeparatedString encoder = SeparatedStringBuilder.byCommas();
    String encoded = encoder
            .add("left")
            .add("right")
            .add("border")
            .encode();
    assertThat(encoded, is("left,right,border"));
    encoder = SeparatedStringBuilder.byCommas();
    encoded = encoder
            .add("left")
            .add("right")
            .add("border")
            .add("left")
            .encode();
    assertThat(encoded, is("left,right,border,left"));
    
    //Test closed loops 
    // closed loops are not altered
    encoder = SeparatedStringBuilder.byCommas().withClosedLoop();
    encoded = encoder
            .add("left")
            .add("right")
            .add("border")
            .add("left")
            .encode();
    assertThat(encoded, is("left,right,border,left"));
    // unclosed loops are altered
    encoder = SeparatedStringBuilder.byCommas().withClosedLoop();
    encoded = encoder
            .add("left")
            .add("right")
            .add("border")
            .encode();
    assertThat(encoded, is("left,right,border,left"));

    //Test open loops
    // closed loops are altered
    encoder = SeparatedStringBuilder.byCommas().withOpenLoop();
    encoded = encoder
            .add("left")
            .add("right")
            .add("border")
            .add("left")
            .encode();
    assertThat(encoded, is("left,right,border"));
    // open loops are unaltered
    encoder = SeparatedStringBuilder.byCommas().withOpenLoop();
    encoded = encoder
            .add("left")
            .add("right")
            .add("border")
            .encode();
    assertThat(encoded, is("left,right,border"));
  }

}
