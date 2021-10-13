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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple access to creating a string of a variety of strings separated by a
 * common character or sequence.
 *
 * <p>
 * A common pattern is to add string elements to a longer string with a format
 * similar to: prefix|element1|separator|element2|suffix. For instance a file
 * path has prefix "/", separator "/", and suffix "". This class allows for
 * convenient object-oriented processing of this pattern.
 *
 * <p>
 * Advanced features allow for proper CSV formatting including quoting and
 * escaping.
 *
 * <p>
 * All values are strings, not characters, so complex output can be generated: a
 * WHEN clause in SQL would be
 * <p>
 * <code>SeparatedStringBulder.startsWith("WHEN").separatedBy(" AND ").addAll(allWhenClausesList).endsWith(groupByClauseString).toString();</code>
 *
 * <p>
 * The default separator is a space (" "). All other defaults are empty.
 *
 * <p>
 * Supports string separator, prefix, suffix, quoting, before quote, after
 * quote, escaping, maps, and is a fluent API.
 *
 * @author gregorygraham
 */
public class SeparatedStringBuilder {

	private SeparatedStringBuilder() {
	}

	/**
	 * Creates a SeparatedString with recommended values for creating and
	 * consuming a CSV file.
	 *
	 * <p>
	 * Please note: CSV file implementations vary wildly and this SeparatedString
	 * may not process for all CSV files, however it will produce reliable CSV
	 * files and is recommended.</p>
	 *
	 * @return a SeparatedString
	 */
	public static SeparatedString csv() {
		return SeparatedStringBuilder.byCommasWithQuotedTermsAndBackslashEscape().withKeyValueSeparator("=");
	}

	/**
	 * Creates a SeparatedString with recommended values for creating and
	 * consuming a TSV (tab separated value file) file
	 *
	 * <p>
	 * Please note: TSV file implementations vary wildly and this SeparatedString
	 * may not process for all TSV files, however it will produce reliable TSV
	 * files and is recommended.</p>
	 *
	 * @return a SeparatedString
	 */
	public static SeparatedString tsv() {
		return SeparatedStringBuilder.byTabs().withEachTermPrecededAndFollowedWith("\"").withEscapeChar("\\").withKeyValueSeparator("=");
	}

	/**
	 * Creates a SeparatedString with recommended values for creating an HTML
	 * ordered list.
	 *
	 * @return a SeparatedString
	 */
	public static SeparatedString htmlOrderedList() {
		return SeparatedStringBuilder.forSeparator("\n").withThisBeforeEachTerm("<li>").withThisAfterEachTerm("</li>").withPrefix("<ol>").withSuffix("</ol>\n");
	}

	/**
	 * Creates a SeparatedString with recommended values for creating an HTML
	 * unordered list.
	 *
	 * @return a SeparatedString
	 */
	public static SeparatedString htmlUnorderedList() {
		return SeparatedStringBuilder.forSeparator("\n").withThisBeforeEachTerm("<li>").withThisAfterEachTerm("</li>").withPrefix("<ul>").withSuffix("</ul>\n");
	}

	/**
	 * Creates a new SeparatedString that starts with provided value.
	 *
	 * <p>
	 * This adds a prefix to the separated string result. For instance
	 * SeparatedString.byCommas().startsWith("LIST=").addAll("1","2","3") will
	 * produce "LIST=1,2,3"
	 *
	 * @param precedingString the string to include at the very beginning of the
	 * generated separated string
	 * @return a SeparatedString that will have precedingString at the beginning
	 * of the output
	 */
	public static SeparatedString startsWith(String precedingString) {
		return new SeparatedString().withPrefix(precedingString);
	}

	/**
	 * Creates a SeparatedString for the map's keys and values.
	 *
	 * <p>
	 * Remember to set the
	 * {@link SeparatedString#getKeyValueSeparator() key-value separator}
	 *
	 * @param nameValuePairs a collection of name/value pairs to be included in
	 * the separated string
	 * @param nameValueSeparator the character (like "=" or ":") used to separate
	 * the name and value
	 * @return a SeparatedString
	 */
	public static SeparatedString of(Map<String, String> nameValuePairs, String nameValueSeparator) {
		ArrayList<String> list = new ArrayList<>();
		nameValuePairs.entrySet().forEach((entry) -> {
			String key = entry.getKey();
			String val = entry.getValue();
			list.add(key + nameValueSeparator + val);
		});
		if (list.isEmpty()) {
			return new SeparatedString();
		} else {
			return new SeparatedString().addAll(list);
		}
	}

	/**
	 * Creates a separated string with the provided values.
	 *
	 * <p>
	 * SeparatedString.of("1","2","3").toString() will produce "1 2 3".
	 *
	 * @param allStrings a list of strings to include within the separated string
	 * @return SeparatedString
	 */
	public static SeparatedString of(String... allStrings) {
		return new SeparatedString().addAll(allStrings);
	}

	/**
	 * Creates a separated string with the provided values.
	 *
	 * <p>
	 * SeparatedString.of("1","2","3").toString() will produce "1 2 3".
	 *
	 * @param allStrings a list of strings to include within the separated string
	 * @return SeparatedString
	 */
	public static SeparatedString of(List<String> allStrings) {
		final SeparatedString separatedString = new SeparatedString();
		if (allStrings != null) {
			final String[] toArray = allStrings.toArray(new String[]{});
			return separatedString.addAll(toArray);
		}
		return separatedString;
	}

	/**
	 * Creates a SeparatedString that will use the provided separator between
	 * values.
	 *
	 * <p>
	 * SeparatedString.forSeparator(",").addAll("1","2","3").toString() will
	 * produce "1,2,3"
	 *
	 * @param separator the characters, like ", ", used to separate that values
	 * within the separated string
	 * @return a SeparatedString
	 */
	public static SeparatedString forSeparator(String separator) {
		return new SeparatedString().separatedBy(separator);
	}

	public static SeparatedString bySpaces() {
		return forSeparator(" ");
	}

	public static SeparatedString byCommas() {
		return forSeparator(",");
	}

	public static SeparatedString byCommaSpace() {
		return forSeparator(", ");
	}

	public static SeparatedString byCommasWithQuotedTermsAndBackslashEscape() {
		return byCommas()
				.withThisBeforeEachTerm("\"")
				.withThisAfterEachTerm("\"")
				.withEscapeChar("\\");
	}

	public static SeparatedString byCommasWithQuotedTermsAndDoubleBackslashEscape() {
		return byCommas()
				.withThisBeforeEachTerm("\"")
				.withThisAfterEachTerm("\"")
				.withEscapeChar("\\\\");
	}

	public static SeparatedString byTabs() {
		return forSeparator("\t");
	}

	public static SeparatedString byLines() {
		return forSeparator("\n");
	}

	public static SeparatedString spaceSeparated() {
		return bySpaces();
	}

	public static SeparatedString commaSeparated() {
		return byCommas();
	}

	public static SeparatedString tabSeparated() {
		return byTabs();
	}

	public static SeparatedString lineSeparated() {
		return byLines();
	}
}
