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
 * Simple access to creating a string of a variety of strings separated by a common character or sequence.
 *
 * <p>
 * A common pattern is to add string elements to a longer string with a format similar to: prefix|element1|separator|element2|suffix. For instance a file path
 * has prefix "/", separator "/", and suffix "". This class allows for convenient object-oriented processing of this pattern.
 *
 * <p>
 * Advanced features allow for proper CSV formatting including quoting and escaping.
 *
 * <p>
 * All values are strings, not characters, so complex output can be generated: a WHEN clause in SQL would be
 * <p>
 * <code>SeparatedStringBulder.startsWith("WHEN").separatedBy(" AND ").addAll(allWhenClausesList).endsWith(groupByClauseString).toString();</code>
 *
 * <p>
 * The default separator is a space (" "). All other defaults are empty.
 *
 * <p>
 * Supports string separator, prefix, suffix, quoting, before quote, after quote, escaping, maps, and is a fluent API.
 *
 * @author gregorygraham
 */
public class SeparatedString {

  private final ArrayList<StringEntry> strings = new ArrayList<>();
  private final HashMap<Class<?>, Function<?, String>> formatters = new HashMap<>();

  private String separator = " ";
  private String prefix = "";
  private String suffix = "";
  private String wrapBefore = "";
  private String wrapAfter = "";
  private String escapeChar = "";
  private String useWhenEmpty = "";
  private String keyValueSeparator = "";
  private ClosedLoop closedLoop = ClosedLoop.NotLoop;
  private boolean trimBlanks = false;
  private boolean retainNulls = false;
  private String retainNullString = "null";
  private boolean uniqueValuesOnly = false;
  private String lineEnd = "";
  private String lineStart = "";

  private static SeparatedString duplicateSettingsOf(SeparatedString sepString) {
    SeparatedString newVersion = new SeparatedString();
    for (StringEntry entry : sepString.strings) {
      newVersion.strings.add(StringEntry.of(entry));
    }
    for (Map.Entry<Class<?>, Function<?, String>> entry : sepString.formatters.entrySet()) {
      newVersion.formatters.put(entry.getKey(), entry.getValue());
    }
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
    newVersion.retainNullString = sepString.getNullRepresentation();
    newVersion.uniqueValuesOnly = sepString.isUniqueValuesOnly();
    newVersion.lineEnd = sepString.lineEnd;
    newVersion.lineStart = sepString.lineStart;
    return newVersion;
  }

  public enum ClosedLoop {
    Closed, Open, NotLoop;

    private static boolean isClosed(ClosedLoop closedLoop) {
      return Closed.equals(closedLoop);
    }

    private static boolean isOpen(ClosedLoop closedLoop) {
      return Open.equals(closedLoop);
    }
  }

  SeparatedString() {
  }

  protected static SeparatedString copy(SeparatedString old) {
    return duplicateSettingsOf(old);
  }

  /**
   * Returns whether or not leading and trailing blanks will be trimmed.
   *
   * @return true if leading and trailing blanks will be removed during processing.
   */
  public boolean isTrimBlanks() {
    return trimBlanks;
  }

  /**
   * Sets the SeparatedString to trim leading and trailing blanks during processing.
   *
   * @return this SeparatedString with the trim blanks status set to true
   */
  public SeparatedString trimBlanks() {
    this.trimBlanks = true;
    return this;
  }

  /**
   * Sets the SeparatedString to trim leading and trailing blanks during processing.
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
   * The main reason this library, and class, exists. Sets the separator to use when processing.</p>
   *
   * <p>
   * for instance {@code separatedString.addAll("alice","bob", "claire").separatedBy(",").encode()} will produce a string like "alice,bob,claire".
   *
   * @param separator the character(s) to be placed between each element
   * @return this SeparatedString with the separator set
   */
  public SeparatedString separatedBy(String separator) {
    if (separator != null) {
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
   * for instance {@code separatedString.addAll("alice","bob", "claire").separatedBy(",").withEscapeChar("\\").encode()} will produce a string like
   * "alice,bob,claire". A more useful example though would be
   * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withEscapeChar("\\").encode()} which would produce
   * "smith\, alice,staines\, bob,sullivan\, claire" allowing the {@link #decode(java.lang.String) } to faithfully reproduce the correct values.</p>
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
   * For maps to be reasonably encoded there needs to be a relationship between the key and the value. SeparatedString uses the keyValueSeparator to encode this
   * relationship. Assuming a map of (mother-&gt;Alice, father-&gt;Bob) {@code separatedString.addAll(map).withKeyValueSeparator("=").separatedBy(",")} would
   * produce "mother=Alice,father=Bob".</p>
   *
   * @param probablyEquals the character(s) to be placed between each key and it's associated value
   * @return this separatedString
   */
  public SeparatedString withKeyValueSeparator(String probablyEquals) {
    this.keyValueSeparator = probablyEquals;
    return this;
  }

  /**
   * Attempts to decode the provided string using the settings of this SeparatedString.
   *
   * <p>
   * For instance {@code separatedString.separatedBy(",").decode("alice,bob,claire")} would produce a list consisting of "alice","bob", and "claire"</p>
   *
   * @param str a string consisting of separated values
   * @return a list of values from the string found using the settings of this SeparatedString
   */
  public List<String> decode(String str) {
    return parseToList(str);
  }

  /**
   * Encodes the contents as per the setup of the SeparatedString.
   *
   * <p>
   * for instance a {@code SeparatedString.commaSeparated().addAll("1","2","3").encode()} will return "1,2,3".</p>
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
   * for instance a {@code SeparatedString.commaSeparated().encode("1","2","3")} will return "1,2,3".</p>
   *
   * <p>
   * No values are added to this SeparatedString and no values within this SeparatedString are useds in the encoding</p>
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
   * Encodes the provided Objects as per the setup of the SeparatedString.
   *
   * <p>
   * for instance a {@code SeparatedString.commaSeparated().encode("1",2,"3")} will return "1,2,3".</p>
   *
   * <p>
   * No values are added to this SeparatedString and no values within this SeparatedString are used in the encoding</p>
   *
   * @param strs the Strings to be encoded as separated string
   * @return returns the SeparatedString's contents encoded as a String
   */
  public String encode(List<Object> strs) {
    SeparatedString newVersion = duplicateSettingsOf(this);
    String encode = newVersion.addAll(strs).encode();
    return encode;
  }

  /**
   * Encodes the contents as per the setup of the SeparatedString.
   *
   * <p>
   * for instance a {@code SeparatedString.commaSeparated().addAll("1",2,"3").toString()} will return "1,2,3".</p>
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
        // Handle the 2 special cases first
        if (StringEntry.isEndOfLineMarker(entry)) {
          // END OF LINE
          // Append the line ending to the ultimate result
          strs.append(getLineEnd());
          // blank the separator because we're starting a new line
          sep = "";
          // and reloop
        } else if (StringEntry.isStartOfLineMarker(entry)) {
          // START OF LINE
          // Append the line starter to the ultimate result
          strs.append(getLineStart());
          // blank the separator because we're starting a new line
          sep = "";
        } else {
          // now get the formatted value
          String entryString = formatStringEntry(entry);
          if (trimBlanks && entryString.isEmpty()) {
            // if it's empty and we're trimming blanks we don't need to handle the value any further
          }
          if (isUniqueValuesOnly() && previousElements.contains(entryString)) {
            // this has already occurred so skip to the next loop
            break;
          } else {
            // in all other cases store the value for future reference
            previousElements.add(entryString);
          }
          // Apply wrapping
          currentEntry = getWrapBefore() + entryString + getWrapAfter();
          // Store the first entry for use with looping
          if (firstEntry == null) {
            firstEntry = currentEntry;
          }
          // Append the separator (which may be blank) and the wrapped value to the ultimate result
          strs.append(sep).append(currentEntry);
          sep = this.getSeparator();
        }
      }

      if (ClosedLoop.isClosed(this.closedLoop) && firstEntry != null && !firstEntry.equals(currentEntry)) {
        strs.append(sep).append(firstEntry);
      }

      String infix = strs.toString();
      if (ClosedLoop.isOpen(this.closedLoop) && firstEntry != null && firstEntry.equals(currentEntry)) {
        infix = infix.replaceAll(sep + currentEntry + "$", "");
      }
      return getPrefix() + infix + getSuffix();
    }
  }

  private String formatStringEntry(StringEntry element) {
    StringBuilder build = new StringBuilder();
    if (element != null) {
      if (element.hasKey()) {
        build.append(replaceSequencesInString(element.getKey(), getReplacementSequences()));
        build.append(getKeyValueSeparator());
      }
      build.append(formatEntryValue(element.getValue()));
    } else {
      build.append(formatEntryValue(element));
    }
    return build.toString();
  }

  protected <T> String formatEntryValue(T value) {
    String string = format(value);
    return replaceSequencesInString(string, getReplacementSequences());
  }

  /**
   * Sets the formatter to use for the object.
   *
   * <p>
   * Normally the toString() method is called on the object, when that does not produce the correct results use this method to supply a better method.</p>
   *
   * @param <T> the type of the object to be formatted
   * @param clazz the class of the object to formatted
   * @param formatter the method that takes an object of type T and produces a correctly formatted String
   * @return this SeparatedObjects
   */
  public <T> SeparatedString setFormatFor(Class<T> clazz, Function<T, String> formatter) {
    this.formatters.put(clazz, formatter);
    return this;
  }

  private <T> String format(T object) {
    String result;
    if (object == null) {
      // there is no object so just report it as empty or null
      if (getRetainNulls()) {
        // null needs to be reported as null
        result = getNullRepresentation();
      } else {
        // the default is to report it as empty
        result = this.getEmptyValue();
      }
    } else {
      // look for a formatter
      Function<T, String> formatter = getFormatterFor(object);
      if (formatter != null) {
        // apply the formatter and return the result
        result = formatter.apply(object);
      } else {
        // no formatter so return the default encoding
        result = object.toString();
      }
    }
    return result;
  }

  protected <T> Function<T, String> getFormatterFor(T object) {
    return (Function<T, String>) formatters.get(object.getClass());
  }

  /**
   * Sets the SeparatedString to retain values and report them using the specified value.
   *
   * <p>
   * for instance to have nulls appears as [NULL] call <code>sepStr.withNullsAs("[NULL]");</code></p>
   *
   * @param useInsteadOfNull the value used to indicate a null value
   * @return this SeparatedString
   */
  public SeparatedString withNullsAs(String useInsteadOfNull) {
    retainNulls = true;
    retainNullString = useInsteadOfNull;
    return this;
  }

  private synchronized MapList<String, String> getCtrlSequences() {
    // the collection we want could be 
    // a map as its a key/value relationship
    // or a set as we don't want duplicate entries
    // but I'm using a list as we need the escape sequence to be processed first.
    // This discussion has been implemented as MapList
    //
    // the key/value relationship is handled by the <String, String> entry class
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
      if (trimBlanks) {
        result = result.replaceAll("^ *", "");
        result = result.replaceAll(" *$", "");
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
   * Removes all values in the collection from the values within this SeparatedString.
   *
   * @param c a collection of objects
   * @return this SeparatedString
   */
  public SeparatedString removeAll(String... c) {
    return removeAll(List.of((Object[]) c));
  }

  /**
   * Removes all values in the collection from the values within this SeparatedString.
   *
   * @param baddies a collection of objects
   * @return this SeparatedString
   */
  public SeparatedString removeAll(List<Object> baddies) {
    final StringEntry[] safeArray = strings.toArray(new StringEntry[]{});
    for (StringEntry next : safeArray) {
      if (next.hasKey()) {
        final String key = next.getKey();
        if (key != null && baddies.contains(key)) {
          strings.remove(next);
        }
      } else {
        final Object value = next.getValue();
        if (value!=null && baddies.contains(value)) {
          strings.remove(next);
        } else if (baddies.contains(formatEntryValue(value))) {
          strings.remove(next);
        }
      }
    }
    return this;
  }

  /**
   * Removes all values in the collection from the values within this SeparatedString.
   *
   * @param c a collection of objects
   * @return this SeparatedString
   */
  public SeparatedString removeAll(Map<String, Object> c) {
    removeAll(List.of(c.keySet().toArray()));
    return this;
  }

  /**
   * Adds all values in the collection to the values within this SeparatedString.
   *
   * @param index the position the values should start at in the list of values within this SeparatedString
   * @param c a collection of objects
   * @return this SeparatedString
   */
  public SeparatedString addAll(int index, Collection<?> c) {
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
   * Adds all values in the collection to the values within this SeparatedString.
   *
   * @param c a collection of objects
   * @return this SeparatedString
   */
  public SeparatedString addAll(List<Object> c) {
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
   * Adds the key and value to the values within this SeparatedString.
   *
   * @param key the label for the value
   * @param value the value to be stored
   * @return this SeparatedString
   */
  public SeparatedString add(String key, Object value) {
    strings.add(StringEntry.of(key, value));
    return this;
  }

  /**
   * Adds all values in the collection to the values within this SeparatedString.
   *
   * @param c a collection of objects
   * @return this SeparatedString
   */
  public SeparatedString addAll(Map<String, Object> c) {
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
   * @param keyValueSeparator the key/value separator to use during processing (overrides any previous key/value separator)
   * @return this SeparatedString
   * @deprecated use {@link #withKeyValueSeparator(java.lang.String)} to set the key/value separator
   */
  @Deprecated
  public SeparatedString addAll(Map<String, Object> c, String keyValueSeparator) {
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
  public SeparatedString addLine(String... strs) {
    return addLine((Object[]) strs);
  }

  /**
   * Adds all values as a line within this SeparatedString.
   *
   * <p>
   * When multi-line data is being used use add line to insert the values as a whole line.</p>
   *
   * @param strs several strings to be added as values
   * @return this SeparatedString
   */
  public SeparatedString addLine(Object... strs) {
    strings.add(StringEntry.getStartOfLineMarker());
    for (Object str : strs) {
      strings.add(new StringEntry(str));
    }
    strings.add(StringEntry.getEndOfLineMarker());
    checkLineEndIsSet();
    return this;
  }

  /**
   * Adds a line marker to the current contents of the SeparatedString.
   *
   * <p>
   * Line markers are used to separate rows of values and should generally be used with the {@link #addLine(java.lang.String...) } method to add all the values
   * at once.</p>
   *
   * <p>
   * However this method allows you to make full use of all the SeparatedString methods, then complete the line with <code>addLine()</code></p>
   *
   * @return this SeparatedString
   */
  public SeparatedString addLine() {
    strings.add(StringEntry.getStartOfLineMarker());
    strings.add(StringEntry.getEndOfLineMarker());
    checkLineEndIsSet();
    return this;
  }

  private void checkLineEndIsSet() {
    if (lineEnd.isEmpty()) {
      this.lineEnd = System.lineSeparator();
    }
  }

  /**
   * Adds all values to the values within this SeparatedString.
   *
   * @param strs several strings to be added as values
   * @return this SeparatedString
   */
  public SeparatedString addAll(String... strs) {
    for (String str : strs) {
      strings.add(new StringEntry(str));
    }
    return this;
  }

  /**
   * Adds all values to the values within this SeparatedString.
   *
   * @param <TYPE> the type of the objects to be added to the values
   * @param stringProcessor a method that transforms objects of type TYPE to Strings
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
   * @param stringProcessor a method that transforms objects of type TYPE to Strings
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
   * <p>
   * Will do nothing if the index is out of bounds</p>
   *
   * @param index the index of the value to remove
   * @return this SeparatedString
   */
  public SeparatedString remove(int index) {
    if (index >= 0 && index < strings.size()) {
      strings.remove(index);
    }
    return this;
  }

  /**
   * Inserts the specified element at the specified position in this list.Shifts the element currently at that position (if any) and any subsequent elements to
   * the right (adds one to their indices).
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
   * Inserts the specified element at the specified position in this list.Shifts the element currently at that position (if any) and any subsequent elements to
   * the right (adds one to their indices).
   *
   * @param index index at which the specified element is to be inserted
   * @param element element to be inserted
   * @return this
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public SeparatedString add(int index, Object element) {
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
    if (string == null) {
      strings.add(null);
    } else {
      strings.add(StringEntry.of(string));
    }
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
   * <p>
   * <code>containing(Object...)</code> is a synonym for {@link #addAll(java.lang.String...) } that allows any object class to be added. It would be preferable
   * to have an <code>addAll(Object...)</code> but that would mask the {@link #addAll(java.lang.String...) } method and make the API more difficult to
   * understand. Arguably this method is also difficult to understand but I've judged it more important to have obvious access to String addition than the more
   * obtuse options for random objects.</p>
   *
   *
   * @param strings elements to be inserted
   * @return this
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public SeparatedString containing(Object... strings) {
    for (Object string : strings) {
      this.strings.add(new StringEntry(string));
    }
    return this;
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
   * <p>
   * to encode an array of values as "[a,b,c,d,e]" use <code>withPrefix("[").withSeparator(",").withsuffix("]")</code> for example.</p>
   *
   *
   * @return the prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Returns the string to be placed added AFTER all other values in the SeparatedString
   *
   * @return the suffix
   */
  public String getSuffix() {
    return suffix;
  }

  /**
   * Returns the value used to indicate the start of a value.
   *
   * <p>
   * Not to be confused with the separator, more commonly the doublequotes than the comma</p>
   *
   * @return the wrapBefore
   */
  public String getWrapBefore() {
    return wrapBefore;
  }

  /**
   * Returns the value used to indicate the end of a value.
   *
   * <p>
   * Not to be confused with the separator, more commonly the doublequotes than the comma</p>
   *
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
   * Instructs the SeparatedString to repeat the first element at the end if necessary.
   *
   * <p>
   * While closed loops are unusual for most applications, this is useful when defining polygons in some GIS systems.</p>
   *
   * <p>
   * Note: that for the list 1,2,3 this option will produce 1,2,3,1 as the trailing 1 is "closing the loop".</p>
   *
   * @return this SeparatedString
   */
  public SeparatedString withClosedLoop() {
    this.closedLoop = ClosedLoop.Closed;
    return this;
  }

  /**
   * Instructs the SeparatedString to NOT repeat the first element at the end.
   *
   * <p>
   * While open loops are unusual for most applications, this is useful when defining polygons in some GIS systems.</p>
   *
   * <p>
   * Note: that for the list 1,2,3,4,5,1 this option will produce 1,2,3,4,5 as the trailing 1 is "closing the loop".</p>
   *
   * @return this SeparatedString
   */
  public SeparatedString withOpenLoop() {
    this.closedLoop = ClosedLoop.Open;
    return this;
  }

  /**
   * Implements support for quoting values to distinguish them from the other elements of the SeparatedString.
   *
   * <p>
   * for instance {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()} would produce "smith,
   * alice,staines, bob,sullivan, claire" forcing {@link #decode(java.lang.String)
   * } to report the values as ("smith", " alice","staines", " bob", "sullivan", " claire").</p>
   * <p>
   * using a wraparound term
   * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withEachTermPrecededAndFollowedWith("|").encode()} would
   * produce "|smith, alice|,|staines, bob|,|sullivan, claire|" allowing {@link #decode(java.lang.String)
   * } to report the values correctly.</p>
   *
   * @param wrapAroundEachTerm the character sequence to be placed before and after every value
   * @return the SeparatedString
   */
  public SeparatedString withEachTermPrecededAndFollowedWith(String wrapAroundEachTerm) {
    this.wrapBefore = wrapAroundEachTerm;
    this.wrapAfter = wrapAroundEachTerm;
    return this;
  }

  /**
   * Implements support for quoting values to distinguish them from the other elements of the SeparatedString.
   *
   * <p>
   * for instance {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()} would produce "smith,
   * alice,staines, bob,sullivan, claire" forcing {@link #decode(java.lang.String)
   * } to report the values as ("smith", " alice","staines", " bob", "sullivan", " claire").</p>
   * <p>
   * using a wraparound term
   * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withEachTermWrappedWith("{","}").encode()} would produce
   * "{smith, alice},{staines, bob},{sullivan, claire}" allowing {@link #decode(java.lang.String)
   * } to report the values correctly.</p>
   *
   * @param beforeEachTerm the character sequence to be placed before every value
   * @param afterEachTerm the character sequence to be placed after every value
   * @return this SeparatedString
   */
  public SeparatedString withEachTermWrappedWith(String beforeEachTerm, String afterEachTerm) {
    this.wrapBefore = beforeEachTerm;
    this.wrapAfter = afterEachTerm;
    return this;
  }

  /**
   * Implements support for quoting values to distinguish them from the other elements of the SeparatedString.
   *
   * <p>
   * for instance {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()} would produce "smith,
   * alice,staines, bob,sullivan, claire" forcing {@link #decode(java.lang.String)
   * } to report the values as ("smith", " alice","staines", " bob", "sullivan", " claire").</p>
   * <p>
   * using a wraparound term null
   * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withThisBeforeEachTerm("[").encode()} would produce
   * "[smith, alice,[staines, bob,[sullivan, claire" allowing {@link #decode(java.lang.String)} to report the values correctly.</p>
   *
   * <p>
   * You should probably also use {@link #withThisAfterEachTerm(java.lang.String)
   * }
   * </p>
   *
   * @param placeAtTheBeginningOfEachTerm the character sequence to be placed before every value
   * @return this SeparatedString
   */
  public SeparatedString withThisBeforeEachTerm(String placeAtTheBeginningOfEachTerm) {
    this.wrapBefore = placeAtTheBeginningOfEachTerm;
    return this;
  }

  /**
   * Implements support for quoting values to distinguish them from the other elements of the SeparatedString.
   *
   * <p>
   * for instance {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()} would produce "smith,
   * alice,staines, bob,sullivan, claire" forcing {@link #decode(java.lang.String)
   * } to report the values as ("smith", " alice","staines", " bob", "sullivan", " claire").</p>
   * <p>
   * using a wraparound term null
   * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withThisAfterEachTerm("]").encode()} would produce
   * "smith, alice],staines, bob],sullivan, claire]" allowing {@link #decode(java.lang.String)} to report the values correctly.</p>
   *
   * <p>
   * You should probably also use {@link #withThisBeforeEachTerm(java.lang.String)
   * }
   * </p>
   *
   * @param placeAtTheEndOfEachTerm the character sequence to be placed before every value
   * @return this SeparatedString
   */
  public SeparatedString withThisAfterEachTerm(String placeAtTheEndOfEachTerm) {
    this.wrapAfter = placeAtTheEndOfEachTerm;
    return this;
  }

  /**
   * Adds a value to be placed at the beginning of the string before any values.
   *
   * <p>
   * to encode an array of values as "[a,b,c,d,e]" use <code>withPrefix("[").withSeparator(",").withsuffix("]")</code> for example.</p>
   *
   * @param placeAtTheBeginningOfTheString the very first item in the encoded result
   * @return this SeparatedString
   */
  public SeparatedString withPrefix(String placeAtTheBeginningOfTheString) {
    this.prefix = placeAtTheBeginningOfTheString;
    return this;
  }

  /**
   * Adds a value to be placed at the very end of the encoded string/
   *
   * <p>
   * to encode an array of values as "[a,b,c,d,e]" use <code>withPrefix("[").withSeparator(",").withsuffix("]")</code> for example.</p>
   *
   * @param placeAtTheEndOfTheString the last item in the encode result
   * @return this SeparatedString
   */
  public SeparatedString withSuffix(String placeAtTheEndOfTheString) {
    this.suffix = placeAtTheEndOfTheString;
    return this;
  }

  /**
   * Sets whether or not to keep NULL values as "null" or changed into empty strings.
   *
   * <p>
   * Defaults to false.</p>
   *
   * @param addNullsAsNull TRUE if null values should be added as "null" empty strings, FALSE if null values should be replaced with the empty string "".
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
   * @param string a string to use instead of returning an empty string while encoding.
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
   * @return a list of values as defined by the string and the settings of this SeparatedString
   */
  public List<String> parseToList(String input) {
    return parse(input).getValues();
  }

  /**
   * Decode the string provided and return a list of values.
   *
   *
   * @param input the string to be decoded
   * @return a list of values as defined by the string and the settings of this SeparatedString
   */
  public List<List<String>> parseToLines(String input) {
    return parse(input).getLines();
  }

  private synchronized ParseResults parse(String input) {
    ParseResults results = new ParseResults();
    Set<String> previousElements = new HashSet<>(0);
    if (input == null || input.isEmpty()) {
      return results;
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
    List<String> currentLine = new ArrayList<>(0);
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
    final String lineEndString = getLineEnd();
    // loop through all the characters
    int i = 0;
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
        // having handled escaped chars, a backslash must be the start of an escape sequence
        isInEscape = true;
        // move past the escape sequence but remember we'll increment at the end of the loop
        i = i + escapeSeq.length() - 1;
      } else if (!isInEscape && !isInQuotes && !isInValue && getLineStart().length() > 0 && str.startsWith(getLineStart())) {
        // OK, we checked everything and it still looks like the beginning of a line so jump forward
        // move past the LineStart sequence but remember we'll increment at the end of the loop
        i = i + getLineStart().length() - 1;
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
        if (separatorString.length() == 0) {
          // When there is no separator we need to add the value to the list
          checkUniquenessRequirementsAndAdd(results.getValues(), format(val), previousElements, currentLine);
          // and clear the val
          val = new StringBuilder();
        }
        // move past the escape sequence but remember we'll increment at the end of the loop
        i = i + quoteEnd.length() - 1;
      } else if (separatorString.length() > 0 && str.startsWith(separatorString)) {
        // Comma MIGHT be the end of a value but we need to check first
        if (isInQuotes) {
          // Inside a quoted string, a comma is just another char
          val.append(separatorString);
        } else if (isInValue) {
          // but in an unquoted value it is the end of the value
          isInValue = false;
          // and we need to add the value to the list
          checkUniquenessRequirementsAndAdd(results.getValues(), format(val), previousElements, currentLine);
          // and clear the val
          val = new StringBuilder();
        } else {
          // edge case: we're not in a value but we found a comma so its an empty value
          checkUniquenessRequirementsAndAdd(results.getValues(), "", previousElements, currentLine);
        }
        // Move past the separator but remember we'll increment at the end of the loop
        i = i + separatorString.length() - 1;
      } else if (!lineEndString.isEmpty() && str.startsWith(lineEndString)) {
        // we have found a new line
        isInValue = false;
        // and we need to add the value to the list
        checkUniquenessRequirementsAndAdd(results.getValues(), format(val), previousElements, currentLine);
        // and clear the val
        val = new StringBuilder();
        // add the completed line
        results.getLines().add(currentLine);
        // and make a new blank currentLine
        currentLine = new ArrayList<>(0);
        // Move past the newline but remember we'll increment at the end of the loop
        i = i + lineEndString.length() - 1;
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
    checkUniquenessRequirementsAndAdd(results.getValues(), format(val), previousElements, currentLine);
    // add the completed line
    results.getLines().add(currentLine);
    return results;

  }

  private class ParseResults {

    private final List<List<String>> lines = new ArrayList<>();
    private final List<String> values = new ArrayList<>();

    public List<List<String>> getLines() {
      return lines;
    }

    public List<String> getValues() {
      return values;
    }
  }

  private void checkUniquenessRequirementsAndAdd(List<String> list, String candidate, Set<String> previousEntries, List<String> currentLine) {
    if (isUniqueValuesOnly()) {
      if (!previousEntries.contains(candidate)) {
        list.add(candidate);
        currentLine.add(candidate);
        previousEntries.add(candidate);
      }
    } else {
      list.add(candidate);
      currentLine.add(candidate);
    }
  }

  /**
   * Decode the string assuming it conforms to this SeparatedString's encoding scheme, and return the values found as an array.
   *
   * @param input the string to be decoded
   * @return the values found
   */
  public String[] parseToArray(String input) {
    return parseToList(input).toArray(new String[0]);
  }

  /**
   * Decode the string assuming it conforms to this SeparatedString's encoding scheme, and return the values found as an array.
   *
   * @param input the string to be decoded
   * @return the values found
   */
  public Map<String, Object> parseToMap(String input) {
    Map<String, Object> map = new HashMap<>(0);
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
   * Checks whether the wrapBefore and wrapAfter are the same.
   *
   * <p>
   * If they're both empty, this will return TRUE.</p>
   *
   * @return TRUE if the wrapBefore and wrapAfter are the same.
   */
  public boolean hasSymetricWrapping() {
    final boolean wrapBeforeIsNull = (wrapBefore == null);
    if (wrapBeforeIsNull && wrapAfter == null) {
      return true;
    } else if (wrapBeforeIsNull) {
      return false;
    } else {
      return wrapBefore.equals(wrapAfter);
    }
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
    final boolean hasSymetricWrapping = hasSymetricWrapping();
    return !hasSymetricWrapping;
  }

  /**
   * Returns the value used when there is no value.
   *
   * <p>
   * If there is a special value to use when there is no value, &lt;NULL&gt; for instance, {@link #useWhenEmpty(java.lang.String) }
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

  /**
   * Returns true if null values will treated differently from empty strings
   *
   * @return if (null != "") then TRUE else FALSE
   */
  public boolean getRetainNulls() {
    return retainNulls;
  }

  /**
   * Returns true if null values will treated differently from empty strings
   *
   * @return if (null != "") then TRUE else FALSE
   */
  public boolean isRetainingNulls() {
    return retainNulls;
  }

  protected String getLineEnd() {
    return lineEnd;
  }

  protected String getLineStart() {
    return lineStart;
  }

  public SeparatedString withLineStartSequence(String lineStartSequence) {
    lineStart = lineStartSequence;
    return this;
  }

  public SeparatedString withLineEndSequence(String lineEndSequence) {
    this.lineEnd = lineEndSequence;
    return this;
  }

  public String getNullRepresentation() {
    return retainNullString;
  }

  /**
   * @return the uniqueValuesOnly
   */
  protected boolean isUniqueValuesOnly() {
    return uniqueValuesOnly;
  }

  protected boolean isClosedLoop() {
    return ClosedLoop.Closed.equals(closedLoop);
  }

  protected boolean isOpenLoop() {
    return ClosedLoop.Open.equals(closedLoop);
  }

  protected boolean isNotLoop() {
    return ClosedLoop.NotLoop.equals(closedLoop);
  }

  protected SeparatedString withNoLoop() {
    closedLoop = ClosedLoop.NotLoop;
    return this;
  }

}
