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
package nz.co.gregs.separatedstring.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import nz.co.gregs.separatedstring.SeparatedString;
import nz.co.gregs.separatedstring.SeparatedStringBuilder;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
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
		final String encoded = sepString.toString();
		assertThat(encoded, is("aaa,bbb,ddd,ccc,null"));

		String[] parsed = sepString.parseToArray(encoded);
		assertThat(parsed.length, is(5));
		assertThat(parsed[0], is("aaa"));
		assertThat(parsed[1], is("bbb"));
		assertThat(parsed[2], is("ddd"));
		assertThat(parsed[3], is("ccc"));
		assertThat(parsed[4], is("null"));
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
		sepString.addAll("aaa", "bbb", "ddd","ccc", "aaa", "ccc");
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
		sepString.addAll("10\\00", "117090058", "117970084", "170,9 + 58", "179,7 + 84", "Flensburg Weiche, W 203 - Flensburg Grenze", "Flensburg-Weiche - Flensb. Gr", "Albert \"The Pain\" Hallsburg");
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
		Map<String, String> parsed = SeparatedStringBuilder
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

}
