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

import util.MapList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
public class SeparatedString {

	private String separator = " ";
	private final ArrayList<String> strings = new ArrayList<>();
	private String prefix = "";
	private String suffix = "";
	private String wrapBefore = "";
	private String wrapAfter = "";
	private String escapeChar = "";
	private String useWhenEmpty = "";
	private String keyValueSeparator = "";
	private boolean closedLoop = false;
	private boolean trimBlanks = false;

	SeparatedString() {
	}

	/**
	 * Returns whether or not leading and trailing blanks will be trimmed.
	 *
	 * @return true if leading and trailing blanks will be removed during
	 * processing.
	 */
	public boolean isTrimBlanks() {
		return trimBlanks;
	}

	/**
	 * Sets the SeparatedString to trim leading and trailing blanks during
	 * processing.
	 *
	 * @return this SeparatedString with the trim blanks status set to true
	 */
	public SeparatedString trimBlanks() {
		this.trimBlanks = true;
		return this;
	}

	/**
	 * Specified the separator to use dring processing.
	 *
	 * <p>
	 * The main reason this library, and class, exists. Sets the separator to use
	 * when processing.</p>
	 *
	 * <p>
	 * for instance
	 * {@code separatedString.addAll("alice","bob", "claire").separatedBy(",").encode()}
	 * will produce a string like "alice,bob,claire".
	 *
	 * @param separator the character(s) to be placed between each element
	 * @return this SeparatedString with the separator set
	 */
	public SeparatedString separatedBy(String separator) {
		if (separator != null && !separator.isEmpty()) {
			this.separator = separator;
		}
		return this;
	}

	/**
	 * Sets the escape sequence to use during processing.
	 *
	 * <p>
	 * While not vital, it is recommended that you set an escape sequence</p>
	 *
	 * <p>
	 * for instance
	 * {@code separatedString.addAll("alice","bob", "claire").separatedBy(",").withEscapeChar("\\").encode()}
	 * will produce a string like "alice,bob,claire". A more useful example though
	 * would be
	 * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withEscapeChar("\\").encode()}
	 * which would produce "smith\, alice,staines\, bob,sullivan\, claire"
	 * allowing the {@link #decode(java.lang.String) } to faithfully reproduce the
	 * correct values.</p>
	 *
	 * @param esc the character(s) to be placed before special character sequences
	 * @return this SeparatedString
	 */
	public SeparatedString withEscapeChar(String esc) {
		this.escapeChar = esc;
		return this;
	}

	/**
	 * Sets the key/value separator during processing.
	 *
	 * <p>
	 * For use with maps</p>
	 *
	 * <p>
	 * For maps to be reasonably encoded there needs to be a relationship between
	 * the key and the value. SeparatedString uses the keyValueSeparator to encode
	 * this relationship. Assuming a map of (mother->Alice, father->Bob)
	 * {@code separatedString.addAll(map).withKeyValueSeparator("=").separatedBy(",")}
	 * would produce "mother=Alice,father=Bob".</p>
	 *
	 * @param probablyEquals the character(s) to be placed between each key and
	 * it's associated value
	 * @return this separatedString
	 */
	public SeparatedString withKeyValueSeparator(String probablyEquals) {
		this.keyValueSeparator = probablyEquals;
		return this;
	}

	/**
	 * Attempts to decode the provided string using the settings of this
	 * SeparatedString.
	 *
	 * <p>
	 * For instance
	 * {@code separatedString.separatedBy(",").decode("alice,bob,claire")} would
	 * produce a list consisting of "alice","bob", and "claire"</p>
	 *
	 * @param str a string consisting of separated values
	 * @return a list of values from the string found using the settings of this
	 * SeparatedString
	 */
	public List<String> decode(String str) {
		return parseToList(str);
	}

	/**
	 * Encodes the contents as per the setup of the SeparatedString.
	 *
	 * <p>
	 * for instance a {@code SeparatedString.commaSeparated().addAll("1","2","3")}
	 * will return "1,2,3".</p>
	 *
	 * @return returns the SeparatedString's contents encoded as a String
	 */
	public String encode() {
		return toString();
	}

	/**
	 * Encodes the contents as per the setup of the SeparatedString.
	 *
	 * <p>
	 * for instance a {@code SeparatedString.commaSeparated().addAll("1","2","3")}
	 * will return "1,2,3".</p>
	 *
	 * @return returns the SeparatedString's contents encoded as a String
	 */
	@Override
	public synchronized String toString() {
		final ArrayList<String> allTheElements = getStrings();
		if (allTheElements.isEmpty()) {
			return useWhenEmpty;
		} else {
			StringBuilder strs = new StringBuilder();
			String sep = "";

			String currentElement = "";
			String firstElement = null;
			for (String element : allTheElements) {
				if (trimBlanks && element.isEmpty()) {
				} else {
					String str = escapeCtrlSequences(element);
					currentElement = getWrapBefore() + str + getWrapAfter();
					if (firstElement == null) {
						firstElement = currentElement;
					}
					strs.append(sep).append(currentElement);
					sep = this.getSeparator();
				}
			}
			if (this.closedLoop && firstElement != null && !firstElement.equals(currentElement)) {
				strs.append(sep).append(firstElement);
			}
			return getPrefix() + strs.toString() + getSuffix();
		}
	}

	private String escapeCtrlSequences(String s) {
		return replaceSequencesInString(s, getCtrlSequences());
	}

	private String replaceSequencesInString(String s, MapList<String, String> sequences) {
		String result = s;
		for (var entry : sequences) {
			result = result.replace(entry.getKey(), entry.getValue());
		}
		return result;
	}

	private synchronized MapList<String, String> getCtrlSequences() {
		// the collection we want could be 
		// a map as its a key/value relationship
		// or a set as we don't want duplicate entries
		// but I'm using a list as we need the escape sequence to be processed first.
		// This discussion has been implemented as MapList
		//
		// the key/value relationship is handled by the Pair<String, String> entry class
		// and duplicate etc handling is covered in addToList()
		MapList<String, String> list = new MapList<>(8);
		// the escape sequence needs to be first so we don't escape our own escapes
		list.add(escapeChar, escapeChar + escapeChar);
		list.add(separator, escapeChar + separator);
		list.add(keyValueSeparator, escapeChar + keyValueSeparator);
		list.add(prefix, escapeChar + prefix);
		list.add(suffix, escapeChar + suffix);
		list.add(useWhenEmpty, escapeChar + useWhenEmpty);
		list.add(wrapAfter, escapeChar + wrapAfter);
		list.add(wrapBefore, escapeChar + wrapBefore);
		return list;
	}

	/**
	 * Returns true if there are currently values stored in this SeparatedString.
	 *
	 * @return true if there are values
	 */
	public boolean isNotEmpty() {
		return !isEmpty();
	}

	/**
	 * Returns true if there are NO values stored in this SeparatedString.
	 *
	 * @return true if there are NO values
	 */
	public boolean isEmpty() {
		return getStrings().isEmpty();
	}

	/**
	 * Removes all values in the collection from the values within this
	 * SeparatedString.
	 *
	 * @param c a collection of objects
	 * @return this SeparatedString
	 */
	public SeparatedString removeAll(Collection<?> c) {
		getStrings().removeAll(c);
		return this;
	}

	/**
	 * Adds all values in the collection to the values within this
	 * SeparatedString.
	 *
	 * @param index the position the values should start at in the list of values
	 * within this SeparatedString
	 * @param c a collection of objects
	 * @return this SeparatedString
	 */
	public SeparatedString addAll(int index, Collection<String> c) {
		if (c != null) {
			getStrings().addAll(index, c);
		}
		return this;
	}

	/**
	 * Adds all values in the collection to the values within this
	 * SeparatedString.
	 *
	 * @param c a collection of objects
	 * @return this SeparatedString
	 */
	public SeparatedString addAll(Collection<String> c) {
		if (c != null && !c.isEmpty()) {
			getStrings().addAll(c);
		}
		return this;
	}

	/**
	 * Adds all values in the collection to the values within this
	 * SeparatedString.
	 *
	 * @param c a collection of objects
	 * @return this SeparatedString
	 */
	public SeparatedString addAll(Map<String, String> c) {
		if (c != null && !c.isEmpty()) {
			c.forEach((key, value) -> {
				getStrings().add(key + getKeyValueSeparator() + value);
			});
		}
		return this;
	}

	/**
	 * Adds all map entries to the values within this SeparatedString.
	 *
	 * @param c a map of objects
	 * @param keyValueSeparator the key/value separator to use during processing
	 * (overrides any previous key/value separator)
	 * @return this SeparatedString
	 */
	public SeparatedString addAll(Map<String, String> c, String keyValueSeparator) {
		withKeyValueSeparator(keyValueSeparator);
		addAll(c);
		return this;
	}

	/**
	 * Adds all values to the values within this SeparatedString.
	 *
	 * @param strs several strings to be added as values
	 * @return this SeparatedString
	 */
	public SeparatedString addAll(String... strs) {
		final List<String> asList = Arrays.asList(strs);
		if (asList != null) {
			getStrings().addAll(asList);
		}
		return this;
	}

	/**
	 * Adds all values to the values within this SeparatedString.
	 *
	 * @param <TYPE> the type of the objects to be added to the values
	 * @param stringProcessor a method that transforms objects of type TYPE to
	 * Strings
	 * @param objects the objects to be added
	 * @return this SeparatedString
	 */
	public <TYPE> SeparatedString addAll(Function<TYPE, String> stringProcessor, TYPE... objects) {
		return addAll(stringProcessor, Arrays.asList(objects));
	}

	/**
	 * Adds all values in the list to the values within this SeparatedString.
	 *
	 * @param <TYPE> the type of the objects to be added to the values
	 * @param stringProcessor a method that transforms objects of type TYPE to
	 * Strings
	 * @param objects the objects to be added
	 * @return this SeparatedString
	 */
	public <TYPE> SeparatedString addAll(Function<TYPE, String> stringProcessor, List<TYPE> objects) {
		final List<String> asList = objects.stream().map(stringProcessor).collect(Collectors.toList());
		if (asList != null) {
			getStrings().addAll(asList);
		}
		return this;
	}

	/**
	 * Removes the value at the index from the values within this SeparatedString.
	 *
	 * @param index the index of the value to remove
	 * @return this SeparatedString
	 */
	public SeparatedString remove(int index) {
		getStrings().remove(index);
		return this;
	}

	/**
	 * Inserts the specified element at the specified position in this list.Shifts
	 * the element currently at that position (if any) and any subsequent elements
	 * to the right (adds one to their indices).
	 *
	 * @param index index at which the specified element is to be inserted
	 * @param element element to be inserted
	 * @return this
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	public SeparatedString add(int index, String element) {
		getStrings().add(index, element);
		return this;
	}

	/**
	 * Inserts the specified element into the list of known values.
	 *
	 * @param string element to be inserted
	 * @return this
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	public SeparatedString add(String string) {
		getStrings().add(string);
		return this;
	}

	/**
	 * Inserts the specified element into the list of known values.
	 *
	 * @param string element to be inserted
	 * @return this
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	public SeparatedString add(Object string) {
		getStrings().add(string.toString());
		return this;
	}

	/**
	 * Inserts the specified elements into the list of known values.
	 *
	 * @param strings elements to be inserted
	 * @return this
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	public SeparatedString containing(String... strings) {
		return addAll(strings);
	}

	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @return the strings
	 */
	public ArrayList<String> getStrings() {
		return strings;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @return the wrapBefore
	 */
	public String getWrapBefore() {
		return wrapBefore;
	}

	/**
	 * @return the wrapAfter
	 */
	public String getWrapAfter() {
		return wrapAfter;
	}

	/**
	 * @return the escapeChar
	 */
	public String getEscapeChar() {
		return escapeChar;
	}

	/**
	 * Instructs the SeparatedString to repeat the first element at the end if
	 * necessary.
	 *
	 * <p>
	 * While closed loops are unusual for most applications, this is useful when
	 * defining polygons in some GIS systems.</p>
	 *
	 * @return this SeparatedString
	 */
	public SeparatedString withClosedLoop() {
		this.closedLoop = true;
		return this;
	}

	/**
	 * Instructs the SeparatedString to NOT repeat the first element at the end.
	 *
	 * <p>
	 * While closed loops are unusual for most applications, this is useful when
	 * defining polygons in some GIS systems.</p>
	 *
	 * @return this SeparatedString
	 */
	public SeparatedString withoutClosedLoop() {
		this.closedLoop = false;
		return this;
	}

	/**
	 * Implements support for quoting values to distinguish them from the other
	 * elements of the SeparatedString.
	 *
	 * <p>
	 * for instance
	 * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()}
	 * would produce "smith, alice,staines, bob,sullivan, claire" forcing {@link #decode(java.lang.String)
	 * } to report the values as ("smith", " alice","staines", " bob", "sullivan",
	 * " claire").</p>
	 * <p>
	 * using a wraparound term
	 * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withEachTermPrecededAndFollowedWith("|").encode()}
	 * would produce "|smith, alice|,|staines, bob|,|sullivan, claire|" allowing {@link #decode(java.lang.String)
	 * } to report the values correctly.</p>
	 *
	 * @param wrapAroundEachTerm the character sequence to be placed before and
	 * after every value
	 * @return the SeparatedString
	 */
	public SeparatedString withEachTermPrecededAndFollowedWith(String wrapAroundEachTerm) {
		this.wrapBefore = wrapAroundEachTerm;
		this.wrapAfter = wrapAroundEachTerm;
		return this;
	}

	/**
	 * Implements support for quoting values to distinguish them from the other
	 * elements of the SeparatedString.
	 *
	 * <p>
	 * for instance
	 * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()}
	 * would produce "smith, alice,staines, bob,sullivan, claire" forcing {@link #decode(java.lang.String)
	 * } to report the values as ("smith", " alice","staines", " bob", "sullivan",
	 * " claire").</p>
	 * <p>
	 * using a wraparound term
	 * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withEachTermWrappedWith("{","}").encode()}
	 * would produce "{smith, alice},{staines, bob},{sullivan, claire}" allowing {@link #decode(java.lang.String)
	 * } to report the values correctly.</p>
	 *
	 * @param beforeEachTerm the character sequence to be placed before every
	 * value
	 * @param afterEachTerm the character sequence to be placed after every value
	 * @return this SeparatedString
	 */
	public SeparatedString withEachTermWrappedWith(String beforeEachTerm, String afterEachTerm) {
		this.wrapBefore = beforeEachTerm;
		this.wrapAfter = afterEachTerm;
		return this;
	}

	/**
	 * Implements support for quoting values to distinguish them from the other
	 * elements of the SeparatedString.
	 *
	 * <p>
	 * for instance
	 * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()}
	 * would produce "smith, alice,staines, bob,sullivan, claire" forcing {@link #decode(java.lang.String)
	 * } to report the values as ("smith", " alice","staines", " bob", "sullivan",
	 * " claire").</p>
	 * <p>
	 * using a wraparound term null
	 * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withThisBeforeEachTerm("[").encode()}
	 * would produce "[smith, alice,[staines, bob,[sullivan, claire" allowing
	 * {@link #decode(java.lang.String)} to report the values correctly.</p>
	 *
	 * <p>
	 * You should probably also use {@link #withThisAfterEachTerm(java.lang.String)
	 * }
	 * </p>
	 *
	 * @param placeAtTheBeginningOfEachTerm the character sequence to be placed
	 * before every value
	 * @return this SeparatedString
	 */
	public SeparatedString withThisBeforeEachTerm(String placeAtTheBeginningOfEachTerm) {
		this.wrapBefore = placeAtTheBeginningOfEachTerm;
		return this;
	}

	/**
	 * Implements support for quoting values to distinguish them from the other
	 * elements of the SeparatedString.
	 *
	 * <p>
	 * for instance
	 * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()}
	 * would produce "smith, alice,staines, bob,sullivan, claire" forcing {@link #decode(java.lang.String)
	 * } to report the values as ("smith", " alice","staines", " bob", "sullivan",
	 * " claire").</p>
	 * <p>
	 * using a wraparound term null
	 * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withThisAfterEachTerm("]").encode()}
	 * would produce "smith, alice],staines, bob],sullivan, claire]" allowing
	 * {@link #decode(java.lang.String)} to report the values correctly.</p>
	 *
	 * <p>
	 * You should probably also use {@link #withThisBeforeEachTerm(java.lang.String)
	 * }
	 * </p>
	 *
	 * @param placeAtTheEndOfEachTerm the character sequence to be placed before
	 * every value
	 * @return this SeparatedString
	 */
	public SeparatedString withThisAfterEachTerm(String placeAtTheEndOfEachTerm) {
		this.wrapAfter = placeAtTheEndOfEachTerm;
		return this;
	}

	/**
	 * Adds a value to be placed at the beginning of the string before any values.
	 *
	 * @param placeAtTheBeginningOfTheString the very first item in the encoded
	 * result
	 * @return this SeparatedString
	 */
	public SeparatedString withPrefix(String placeAtTheBeginningOfTheString) {
		this.prefix = placeAtTheBeginningOfTheString;
		return this;
	}

	/**
	 * Adds a value to be placed at the very end of the encoded string/
	 *
	 * @param placeAtTheEndOfTheString the last item in the encode result
	 * @return this SeparatedString
	 */
	public SeparatedString withSuffix(String placeAtTheEndOfTheString) {
		this.suffix = placeAtTheEndOfTheString;
		return this;
	}

	/**
	 * Adds a value to be placed at the very end of the encoded string/
	 *
	 * @param placeAtTheEndOfTheString the last item in the encode result
	 * @return this SeparatedString
	 */
	public final SeparatedString endsWith(String placeAtTheEndOfTheString) {
		return withSuffix(placeAtTheEndOfTheString);
	}

	/**
	 * Provides a value to be used whenever the encoded result would be empty.
	 *
	 * @param string a string to use instead of returning an empty string while
	 * encoding.
	 * @return this SeparatedString
	 */
	public final SeparatedString useWhenEmpty(String string) {
		this.useWhenEmpty = string;
		return this;
	}

	/**
	 * Decode the string provided and return a list of values.
	 *
	 *
	 * @param input the string to be decoded
	 * @return a list of values as defined by the string and the settings of this
	 * SeparatedString
	 */
	public List<String> parseToList(String input) {
		List<String> output = new ArrayList<>();
		if (input == null || input.isEmpty()) {
			return output;
		}
		String line = input;
		if (hasPrefix()) {
			line = line.replaceAll("^" + getPrefix(), "");
		}
		if (hasSuffix()) {
			line = line.replaceAll(getSuffix() + "$", "");
		}
		// we'll be scanning through the line so we need to collect the characters as we go
		StringBuilder val = new StringBuilder();
		// we'll need to keep track of what is happening
		// firstly, are we inside a value?
		boolean isInValue = false;
		// secondly is the value in quotes?
		boolean isInQuotes = false;
		// finally, has an escape been started?
		boolean isInEscape = false;
		// some other things we're going to use
		final boolean hasEscapeChar = this.hasEscapeChar();
		final String escapeSeq = getEscapeChar();
		boolean hasQuoting = this.hasWrapping();
		final String quoteStart = getWrapBefore();
		final String quoteEnd = getWrapAfter();
		boolean quotesAreEqual = quoteStart.equals(quoteEnd);
		final String separatorString = getSeparator();
		// loop through all the characters
		int i = 0;
		//for (int i = 0; i < line.length(); i++) {
		while (i < line.length()) {
			// get the current character
			char chr = line.charAt(i);
			String str = line.substring(i);
			// the escaped character is highest priority
			if (isInEscape) {
				// there is an escape in play so just add the character whatever it is
				val.append(chr);
				// only one character can be escaped so end the escape
				isInEscape = false;
			} else if (hasEscapeChar && str.startsWith(escapeSeq)) {
				// having handeled escaped chars, a backslash must be the start of an escape sequence
				isInEscape = true;
				// move past the escape sequence but remember we'll increment at the end of the loop
				i = i + escapeSeq.length() - 1;
			} else if (hasQuoting && str.startsWith(quoteStart)) {
				if (quotesAreEqual) {
					// turn the quotes on and off as required
					isInQuotes = !isInQuotes;
				} else if (!isInQuotes) {
					isInQuotes = true;
				}
				// move past the escape sequence but remember we'll increment at the end of the loop
				i = i + quoteStart.length() - 1;
			} else if (hasQuoting && str.startsWith(quoteEnd)) {
				if (quotesAreEqual) {
					// turn the quotes on and off as required
					isInQuotes = !isInQuotes;
				} else if (isInQuotes) {
					isInQuotes = false;
				}
				// move past the escape sequence but remember we'll increment at the end of the loop
				i = i + quoteEnd.length() - 1;
			} else if (str.startsWith(separatorString)) {
				// Comma MIGHT be the end of a value but we need to check first
				if (isInQuotes) {
					// Inside a quoted string, a comma is just another char
					val.append(separatorString);
				} else if (isInValue) {
					// but in an unquoted value it is the end of the value
					isInValue = false;
					// and we need to add the value to the list
					output.add(val.toString());
					// and clear the val
					val = new StringBuilder();
				} else {
					// edge case: we're not in a value but we found a comma so its an empty value
					output.add("");
				}
				// Move past the separator
				i = i + separatorString.length() - 1;
			} else if (chr == ' ') {
				// leading spaces are, generally, not part of the value
				// so ensure we're inside a value before adding them
				if (isInValue) {
					val.append(chr);
				}
			} else {
				// We've covered every case, so this must be part of a value
				isInValue = true;
				// so we need to append it to the value
				val.append(chr);
			}
			i++;
		}
		// The last value doesn't have a terminator so we'll need to add it as well
		// Note that this means all lines have at least one value even when they're empty
		output.add(val.toString());
		return output;
	}

	/**
	 * Decode the string assuming it conforms to this SeparatedString's encoding
	 * scheme, and return the values found as an array.
	 *
	 * @param input the string to be decoded
	 * @return the values found
	 */
	public String[] parseToArray(String input) {
		return parseToList(input).toArray(new String[]{});
	}

	/**
	 * Decode the string assuming it conforms to this SeparatedString's encoding
	 * scheme, and return the values found as an array.
	 *
	 * @param input the string to be decoded
	 * @return the values found
	 */
	public Map<String, String> parseToMap(String input) {
		Map<String, String> map = new HashMap<>(0);
		if (input == null || input.isEmpty()) {
			return map;
		}
		List<String> parseList = parseToList(input);
		if (parseList == null || parseList.isEmpty()) {
			return map;
		}
		final String keyValueSep = getKeyValueSeparator();
		if (keyValueSep.isEmpty()) {
			for (String str : parseList) {
				map.put(str, "");
			}
		} else {
			for (String str : parseList) {
				if (keyValueSep.isEmpty()) {
					map.put(str, "");
				} else {
					String[] split = str.split(keyValueSep);
					if (split.length == 2) {
						map.put(split[0], split[1]);
					} else if (split.length == 1) {
						map.put(split[0], "");
					}
				}
			}
		}
		return map;
	}

	/**
	 * Returns the string used to separate a key from its alue during encoding
	 *
	 * @return the key/value separator
	 */
	public String getKeyValueSeparator() {
		return keyValueSeparator;
	}

	/**
	 * indicates whether this SeparatedString has a defined escape sequence.
	 *
	 * @return TRUE if an escape sequence has been defined
	 */
	public boolean hasEscapeChar() {
		return !escapeChar.isEmpty();
	}

	/**
	 * Indicates whether any wrapping sequences have been defined.
	 *
	 * @return TRUE if wrap before or wrap after are not empty.
	 */
	public boolean hasWrapping() {
		return !getWrapBefore().isEmpty()
				|| !getWrapAfter().isEmpty();
	}

	/**
	 * Checks if a prefix has been defined
	 *
	 * @return TRUE if a prefix has been defined
	 */
	public boolean hasPrefix() {
		return !prefix.isEmpty();
	}

	/**
	 * Checks if a suffux has been defined.
	 *
	 * @return TRUE if a suffix has been defined
	 */
	public boolean hasSuffix() {
		return !suffix.isEmpty();
	}

	/**
	 * Checks whether the prefix and suffix are the same.
	 *
	 * <p>
	 * If they're both empty, this will return TRUE.</p>
	 *
	 * @return TRUE if the suffix and prefix are the same.
	 */
	public boolean hasSymetricWrapping() {
		return prefix != null && prefix.equals(suffix);
	}

	/**
	 * Checks whether the prefix and suffix are different.
	 *
	 * <p>
	 * If they're both empty, this will return FALSE.</p>
	 *
	 * @return TRUE if the suffix and prefix have different values.
	 */
	public boolean hasAsymetricWrapping() {
		return !hasSymetricWrapping();
	}
}
