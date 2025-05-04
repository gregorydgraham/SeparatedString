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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import nz.co.gregs.separatedstring.util.MapList;

/**
 * An extension of SeparatedString that allows for better handling of specific classes.
 *
 * @author Gregory Graham
 */
public class SeparatedFormattedString {

  private final HashMap<Class<?>, Function<?, String>> formatters = new HashMap<>();

  private final SeparatedString sepString = new SeparatedString();

  public boolean isTrimBlanks() {
    return sepString.isTrimBlanks();
  }

  public SeparatedFormattedString trimBlanks() {
    sepString.trimBlanks();
    return this;
  }

  public SeparatedFormattedString withOnlyUniqueValues() {
    sepString.withOnlyUniqueValues();
    return this;
  }

  public SeparatedFormattedString separatedBy(String separator) {
    sepString.separatedBy(separator);
    return this;
  }

  public SeparatedFormattedString withEscapeChar(String esc) {
    sepString.withEscapeChar(esc);
    return this;
  }

  public SeparatedFormattedString withKeyValueSeparator(String probablyEquals) {
    sepString.withKeyValueSeparator(probablyEquals);
    return this;
  }

  public List<String> decode(String str) {
    return sepString.decode(str);
  }

  public String encode() {
    return sepString.encode();
  }

  public String encode(String... strs) {
    return sepString.encode(strs);
  }

  public String encode(List<String> strs) {
    return sepString.encode(strs);

  }

  public synchronized String toString() {
    return sepString.toString();
  }

  public boolean isNotEmpty() {
    return sepString.isNotEmpty();
  }

  public boolean isEmpty() {
    return sepString.isEmpty();
  }

  public SeparatedFormattedString removeAll(Collection<?> c) {
    sepString.removeAll(c);
    return this;
  }

  public SeparatedFormattedString addAll(int index, Collection<String> c) {
    sepString.addAll(index, c);
    return this;
  }

  public SeparatedFormattedString addAll(Collection<String> c) {
    sepString.addAll(c);
    return this;
  }

  public SeparatedFormattedString add(String key, String value) {
    sepString.add(key, value);
    return this;
  }

  public SeparatedFormattedString addAll(Map<String, String> c) {
    sepString.addAll(c);
    return this;
  }

  public SeparatedFormattedString addAll(Map<String, String> c, String keyValueSeparator) {
    sepString.addAll(c, keyValueSeparator);
    return this;
  }

  public SeparatedFormattedString addLine(String... strs) {
    sepString.addLine(strs);
    return this;
  }

  public SeparatedFormattedString addLine() {
    sepString.addLine();
    return this;
  }

  public SeparatedFormattedString addAll(String... strs) {
    sepString.addAll(strs);
    return this;
  }

  public SeparatedFormattedString addAll(Object... strs) {
    for (Object str : strs) {
      sepString.add(formatObject(str));
    }
    return this;
  }

  private <T> String formatObject(T object) {
    Function<T, String> formatter = (Function<T, String>) formatters.get(object.getClass());
    if (formatter != null) {
      return formatter.apply(object);
    }
    return object.toString();
  }

  private <T extends Object> String useFormatter(T obj, Function<T, String> function) {
    if (function != null) {
      return function.apply(obj);
    } else {
      return obj.toString();
    }
  }

  public <TYPE> SeparatedFormattedString addAll(Function<TYPE, String> stringProcessor, TYPE... objects) {
    sepString.addAll(stringProcessor, objects);
    return this;
  }

  public <TYPE> SeparatedFormattedString addAll(Function<TYPE, String> stringProcessor, List<TYPE> objects) {
    sepString.addAll(stringProcessor, objects);
    return this;
  }

  public SeparatedFormattedString remove(int index) {
    sepString.remove(index);
    return this;
  }

  public SeparatedFormattedString add(int index, String element) {
    sepString.add(index, element);
    return this;
  }

  public SeparatedFormattedString add(String string) {
    sepString.add(string);
    return this;
  }

  public SeparatedFormattedString add(Object string) {
    sepString.add(string);
    return this;
  }

  public SeparatedFormattedString containing(String... strings) {
    sepString.containing(strings);
    return this;
  }

  public String getSeparator() {
    return sepString.getSeparator();
  }

  public String getPrefix() {
    return sepString.getPrefix();
  }

  public String getSuffix() {
    return sepString.getSuffix();
  }

  public String getWrapBefore() {
    return sepString.getWrapBefore();
  }

  public String getWrapAfter() {
    return sepString.getWrapAfter();
  }

  public String getEscapeChar() {
    return sepString.getEscapeChar();
  }

  public SeparatedFormattedString withClosedLoop() {
    sepString.withClosedLoop();
    return this;
  }

  public SeparatedFormattedString withoutClosedLoop() {
    sepString.withoutClosedLoop();
    return this;
  }

  public SeparatedFormattedString withEachTermPrecededAndFollowedWith(String wrapAroundEachTerm) {
    sepString.withEachTermPrecededAndFollowedWith(wrapAroundEachTerm);
    return this;
  }

  public SeparatedFormattedString withEachTermWrappedWith(String beforeEachTerm, String afterEachTerm) {
    sepString.withEachTermWrappedWith(beforeEachTerm, afterEachTerm);
    return this;
  }

  public SeparatedFormattedString withThisBeforeEachTerm(String placeAtTheBeginningOfEachTerm) {
    sepString.withThisBeforeEachTerm(placeAtTheBeginningOfEachTerm);
    return this;
  }

  public SeparatedFormattedString withThisAfterEachTerm(String placeAtTheEndOfEachTerm) {
    sepString.withThisAfterEachTerm(placeAtTheEndOfEachTerm);
    return this;
  }

  public SeparatedFormattedString withPrefix(String placeAtTheBeginningOfTheString) {
    sepString.withPrefix(placeAtTheBeginningOfTheString);
    return this;
  }

  public SeparatedFormattedString withSuffix(String placeAtTheEndOfTheString) {
    sepString.withSuffix(placeAtTheEndOfTheString);
    return this;

  }

  public SeparatedFormattedString withNullsRetained(boolean addNullsAsNull) {
    sepString.withNullsRetained(addNullsAsNull);
    return this;

  }

  public final SeparatedFormattedString endsWith(String placeAtTheEndOfTheString) {
    sepString.endsWith(placeAtTheEndOfTheString);
    return this;
  }

  public final SeparatedFormattedString useWhenEmpty(String string) {
    sepString.useWhenEmpty(string);
    return this;

  }

  public List<String> parseToList(String input) {
    return sepString.parseToList(input);
  }

  public List<List<String>> parseToLines(String input) {
    return sepString.parseToLines(input);
  }

  public String[] parseToArray(String input) {
    return sepString.parseToArray(input);
  }

  public Map<String, String> parseToMap(String input) {
    return sepString.parseToMap(input);
  }

  public String getKeyValueSeparator() {
    return sepString.getKeyValueSeparator();
  }

  public boolean hasEscapeChar() {
    return sepString.hasEscapeChar();
  }

  public boolean hasWrapping() {
    return sepString.hasWrapping();
  }

  public boolean hasPrefix() {
    return sepString.hasPrefix();
  }

  public boolean hasSuffix() {
    return sepString.hasSuffix();
  }

  public boolean hasSymetricWrapping() {
    return sepString.hasSymetricWrapping();

  }

  public boolean hasAsymetricWrapping() {
    return sepString.hasAsymetricWrapping();
  }

  public String getEmptyValue() {
    return sepString.getEmptyValue();

  }

  public MapList<String, String> getReplacementSequences() {
    return sepString.getReplacementSequences();
  }

  public boolean getRetainNulls() {
    return sepString.getRetainNulls();
  }

  public SeparatedFormattedString withLineEndSequence(String lineEndSequence) {
    sepString.withLineEndSequence(lineEndSequence);
    return this;
  }

  public SeparatedFormattedString() {
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
  public <T> SeparatedFormattedString setFormatFor(Class<T> clazz, Function<T, String> formatter) {
    this.formatters.put(clazz, formatter);
    return this;
  }

  private <T> String format(T object) {
    Function<T, String> formatter = (Function<T, String>) formatters.get(object.getClass());
    return formatter.apply(object);
  }

}
