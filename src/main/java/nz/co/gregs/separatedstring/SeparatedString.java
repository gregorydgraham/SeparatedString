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

import java.util.*;
import nz.co.gregs.separatedstring.util.MapList;
import java.util.function.Function;
import java.util.stream.Collectors;
import nz.co.gregs.separatedstring.util.StringEntry;

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
	private final ArrayList<StringEntry> strings = new ArrayList<>();
	private String prefix = "";
	private String suffix = "";
	private String wrapBefore = "";
	private String wrapAfter = "";
	private String escapeChar = "";
	private String useWhenEmpty = "";
	private String keyValueSeparator = "";
	private boolean closedLoop = false;
	private boolean trimBlanks = false;
	private boolean retainNulls = false;
	private boolean uniqueValuesOnly = false;

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
	 * Sets the SeparatedString to trim leading and trailing blanks during
	 * processing.
	 *
	 * @return this SeparatedString with the trim blanks status set to true
	 */
	public SeparatedString withOnlyUniqueValues() {
		this.uniqueValuesOnly = true;
		return this;
	}

	/**
	 * Specified the separator to use during processing.
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
	 * this relationship. Assuming a map of (mother-&gt;Alice, father-&gt;Bob)
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
	 * for instance a
	 * {@code SeparatedString.commaSeparated().addAll("1","2","3").encode()} will
	 * return "1,2,3".</p>
	 *
	 * @return returns the SeparatedString's contents encoded as a String
	 */
	public String encode() {
		return toString();
	}

	/**
	 * Encodes the provided Strings as per the setup of the SeparatedString.
	 *
	 * <p>
	 * for instance a {@code SeparatedString.commaSeparated().encode("1","2","3")}
	 * will return "1,2,3".</p>
	 *
	 * <p>
	 * No values are added to this SeparatedString and no values within this
	 * SeparatedString are useds in the encoding</p>
	 *
	 * @param strs the Strings to be encoded as separated string
	 * @return returns the SeparatedString's contents encoded as a String
	 */
	public String encode(String... strs) {
		SeparatedString newVersion = duplicateSettingsOf(this);
		String encode = newVersion.addAll(strs).encode();
		return encode;
	}

	/**
	 * Encodes the provided Strings as per the setup of the SeparatedString.
	 *
	 * <p>
	 * for instance a {@code SeparatedString.commaSeparated().encode("1","2","3")}
	 * will return "1,2,3".</p>
	 *
	 * <p>
	 * No values are added to this SeparatedString and no values within this
	 * SeparatedString are used in the encoding</p>
	 *
	 * @param strs the Strings to be encoded as separated string
	 * @return returns the SeparatedString's contents encoded as a String
	 */
	public String encode(List<String> strs) {
		SeparatedString newVersion = duplicateSettingsOf(this);
		String encode = newVersion.addAll(strs).encode();
		return encode;
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
		final ArrayList<StringEntry> allTheEntries = strings;
		List<String> previousElements = new ArrayList<>(0);
		if (allTheEntries.isEmpty()) {
			return useWhenEmpty;
		} else {
			StringBuilder strs = new StringBuilder();
			String sep = "";

			String currentEntry = "";
			String firstEntry = null;
			for (StringEntry entry : allTheEntries) {
				if (trimBlanks && entry.isEmpty()) {
				} else {
					String str = formatStringEntry(entry);
					if (uniqueValuesOnly && previousElements.contains(str)) {
						break;
					} else {
						previousElements.add(str);
					}
					if (retainNulls) {
						currentEntry = getWrapBefore() + str + getWrapAfter();
					} else {
						currentEntry = getWrapBefore() + (str == null ? "" : str) + getWrapAfter();
					}
					if (firstEntry == null) {
						firstEntry = currentEntry;
					}
					strs.append(sep).append(currentEntry);
					sep = this.getSeparator();
				}
			}
			if (this.closedLoop && firstEntry != null && !firstEntry.equals(currentEntry)) {
				strs.append(sep).append(firstEntry);
			}
			return getPrefix() + strs.toString() + getSuffix();
		}
	}
  
  private String formatStringEntry(StringEntry element) {
		StringBuilder build = new StringBuilder();
		if (element.hasKey()) {
			build.append(replaceSequencesInString(element.getKey(), getReplacementSequences()));
			build.append(getKeyValueSeparator());
		}
		String value = element.getValue();
		if (value == null && !getRetainNulls()) {
			value = getEmptyValue();
		}
		if (value != null && value.isEmpty()) {
			value = getEmptyValue();
		}
		build.append(replaceSequencesInString(value, getReplacementSequences()));

		return build.toString();
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
	private String replaceSequencesInString(String s, MapList<String, String> sequences) {
		if (s == null) {
			return s;
		} else {
			String result = s;
			for (var seq : sequences) {
				final String seqKey = seq.getKey();
				if (seqKey != null && !seqKey.isEmpty()) {
					String value = seq.getValue();
					if (value == null || value.isEmpty()) {
						value = "";
					}
					result = result.replace(seqKey, value);
				}
			}
			return result;
		}
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
		return strings.isEmpty();
	}

	/**
	 * Removes all values in the collection from the values within this
	 * SeparatedString.
	 *
	 * @param c a collection of objects
	 * @return this SeparatedString
	 */
	public SeparatedString removeAll(Collection<?> c) {
		strings.removeAll(c);
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
			final List<StringEntry> entries
					= c.stream()
							.map((t) -> new StringEntry(t))
							.collect(Collectors.toList());
			strings.addAll(index, entries);
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
			final List<StringEntry> entries
					= c.stream()
							.map((t) -> new StringEntry(t))
							.collect(Collectors.toList());
			strings.addAll(entries);
		}
		return this;
	}

	/**
	 * Adds the key and value to the values within this SeparatedString.
	 *
	 * @param key the label for the value
	 * @param value the value to be stored
	 * @return this SeparatedString
	 */
	public SeparatedString add(String key, String value) {
		strings.add(StringEntry.of(key, value));
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
				strings.add(StringEntry.of(key, value));
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
		final List<StringEntry> asList
				= Arrays.asList(strs)
						.stream()
						.map((t) -> new StringEntry(t))
						.collect(Collectors.toList());
		if (asList != null) {
			strings.addAll(asList);
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
		final List<StringEntry> asList
				= objects.stream()
						.map(stringProcessor)
						.map(t -> StringEntry.of(t))
						.collect(Collectors.toList());
		if (asList != null) {
			strings.addAll(asList);
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
		strings.remove(index);
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
		strings.add(index, StringEntry.of(element));
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
		strings.add(StringEntry.of(string));
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
		if (string == null) {
			strings.add(null);
		} else {
			strings.add(StringEntry.of(string.toString()));
		}
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
   * Returns the character sequence used to distinguish between values within the SeparatedString.
   * 
   * <p>
   * For instance a classical CSV could a "," - "a,b,c,d,e" where as date string might use a "/" - "2022/02/13</p>
   * 
   * <p>
   * SeparatedString uses a String as the separator so more complex values are supported: ", " for instance produces the much prettier "a, b, c, d, e"</p>
   * 
	 * @return the separator value
	 */
	public String getSeparator() {
		return separator;
	}
  
	/**
   * Returns the string to be placed added BEFORE all other values in the SeparatedString
   * 
   * <p>to encode an array of values as "[a,b,c,d,e]" use <code>withPrefix("[").withSeparator(",").withsuffix("]")</code> for example.</p>
	 *
   * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/** Returns the string to be placed added AFTER all other values in the SeparatedString
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
   * Returns the value used to indicate the start of a value.
   * 
   * <p>Not to be confused with the separator, more commonly the doublequotes than the comma</p>
   * 
	 * @return the wrapBefore
	 */
	public String getWrapBefore() {
		return wrapBefore;
	}

	/**
   * Returns the value used to indicate the end of a value.
   * 
   * <p>Not to be confused with the separator, more commonly the doublequotes than the comma</p>
	 * @return the wrapAfter
	 */
	public String getWrapAfter() {
		return wrapAfter;
	}

	/**
   * Returns the value used to indicate that the following string should not be interpreted as a control sequence.
   * 
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
   * <p>to encode an array of values as "[a,b,c,d,e]" use <code>withPrefix("[").withSeparator(",").withsuffix("]")</code> for example.</p>
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
   * <p>to encode an array of values as "[a,b,c,d,e]" use <code>withPrefix("[").withSeparator(",").withsuffix("]")</code> for example.</p>
	 *
	 * @param placeAtTheEndOfTheString the last item in the encode result
	 * @return this SeparatedString
	 */
	public SeparatedString withSuffix(String placeAtTheEndOfTheString) {
		this.suffix = placeAtTheEndOfTheString;
		return this;
	}

	/**
	 * Sets whether or not to keep NULL values as "null" or changed into empty
	 * strings.
	 *
	 * <p>
	 * Defaults to false.</p>
	 *
	 * @param addNullsAsNull TRUE if null values should be added as "null" empty
	 * strings, FALSE if null values should be replaced with the empty string "".
	 * @return this SeparatedString
	 */
	public SeparatedString withNullsRetained(boolean addNullsAsNull) {
		this.retainNulls = addNullsAsNull;
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
		List<String> output = new ArrayList<>(0);
		Set<String> previousElements = new HashSet<>(0);
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
					checkUniquenessRequirementsAndAdd(output, val.toString(), previousElements);
					// and clear the val
					val = new StringBuilder();
				} else {
					// edge case: we're not in a value but we found a comma so its an empty value
					checkUniquenessRequirementsAndAdd(output, "", previousElements);
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
		checkUniquenessRequirementsAndAdd(output, val.toString(), previousElements);
		return new ArrayList<>(output);
	}

	private void checkUniquenessRequirementsAndAdd(List<String> list, String candidate, Set<String> previousEntries) {
		if (uniqueValuesOnly) {
			if (!previousEntries.contains(candidate)) {
				list.add(candidate);
				previousEntries.add(candidate);
			}
		} else {
			list.add(candidate);
		}
	}

	/**
	 * Decode the string assuming it conforms to this SeparatedString's encoding
	 * scheme, and return the values found as an array.
	 *
	 * @param input the string to be decoded
	 * @return the values found
	 */
	public String[] parseToArray(String input) {
		return parseToList(input).toArray(new String[0]);
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
	 * Checks if a suffix has been defined.
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

/**
 * Returns the value used when there is no value.
 * 
 * <p>If there is a special value to use when there is no value, &lt;NULL&gt; for instance, {@link #useWhenEmpty(java.lang.String) } 
 * sets the value to be used and this method is used to retrieve that value.</p>
 * 
 * @return the empty value
 */
  public String getEmptyValue() {
		return useWhenEmpty;
	}

/**
 * 
 * 
   * @return the list of replacement sequences
 */
  public MapList<String, String> getReplacementSequences() {
		return getCtrlSequences();
	}

	public boolean getRetainNulls() {
		return retainNulls;
	}

	private SeparatedString duplicateSettingsOf(SeparatedString sepString) {
		SeparatedString newVersion = new SeparatedString();
		newVersion.separator = sepString.separator;
		newVersion.prefix = sepString.prefix;
		newVersion.suffix = sepString.suffix;
		newVersion.wrapBefore = sepString.wrapBefore;
		newVersion.wrapAfter = sepString.wrapAfter;
		newVersion.escapeChar = sepString.escapeChar;
		newVersion.useWhenEmpty = sepString.useWhenEmpty;
		newVersion.keyValueSeparator = sepString.keyValueSeparator;
		newVersion.closedLoop = sepString.closedLoop;
		newVersion.trimBlanks = sepString.trimBlanks;
		newVersion.retainNulls = sepString.retainNulls;
		newVersion.uniqueValuesOnly = sepString.uniqueValuesOnly;
		return newVersion;
	}
}
