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

import java.util.function.Function;

/**
 * Simple access to creating a string of a variety of strings separated by a common character or sequence.
 *
 * <p>
 * A common pattern is to add string elements to a longer string with a format similar to: prefix|element1|separator|element2|suffix. For instance a file path
 * has prefix "/", separator "/", and suffix "". This class allows for convenient object-oriented processing of this pattern.</p>
 *
 * <p>
 * Advanced features allow for proper CSV formatting including quoting and escaping.</p>
 *
 * <p>
 * All values are strings, not characters, so complex output can be generated: a WHEN clause in SQL would be</p>
 * <p>
 * <code>SeparatedStringBulder.startsWith("WHEN").separatedBy(" AND ").addAll(allWhenClausesList).endsWith(groupByClauseString).toString();</code></p>
 *
 * <p>
 * The default separator is a space (" "). All other defaults are empty.</p>
 *
 * <p>
 * Supports string separator, prefix, suffix, quoting, before quote, after quote, escaping, maps, and is a fluent API.</p>
 *
 * @author Gregory Graham
 */
public class Builder {

  private final SeparatedString separatedString;

  /**
   * Get a separated string Builder.
   *
   * <p>
   * The same as the {@link #start() } method.</p>
   *
   * @return a separated string builder
   */
  public static Builder go() {
    return start();
  }

  /**
   * Get a separated string Builder.
   *
   * <p>
   * The same as the {@link #go() } method.</p>
   *
   * @return a separated string builder
   */
  public static Builder start() {
    return new Builder();
  }

  protected Builder() {
    this.separatedString = new SeparatedString();
  }

  protected Builder(SeparatedString separatedString) {
    this.separatedString = separatedString;
  }

  /**
   * Used to create the writer for the separated string.
   * 
   * <p>the encoder provides all the methods for producing the actual string based on the builder's settings</p>
   * 
   * @return a new separated string encoder based on the builder's settings
   */
  public Encoder encoder() {
    return new Encoder(SeparatedString.copy(separatedString));
  }

  /**
   * Used to create the parser for the separated string.
   * 
   * <p>the encoder provides all the methods for deciphering the actual strings using builder's settings</p>
   * 
   * <p>Be warned: not all builder settings will produce readable output, there is a reason there are so many file formats out there.</p>
   * 
   * @return a new separated string encoder based on the builder's settings
   */
  public Decoder decoder() {
    return new Decoder(SeparatedString.copy(separatedString));
  }

  /**
   * Sets the SeparatedString to trim leading and trailing blanks during processing.
   *
   * @return this SeparatedString with the trim blanks status set to true
   */
  public Builder withBlanksTrimmed() {
    separatedString.trimBlanks();
    return this;
  }

  /**
   * Sets the SeparatedString to trim leading and trailing blanks during processing.
   *
   * @return this SeparatedString with the trim blanks status set to true
   */
  public Builder withOnlyUniqueValues() {
    separatedString.withOnlyUniqueValues();
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
  public Builder separatedBy(String separator) {
    separatedString.separatedBy(separator);
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
   * "smith\, alice,staines\, bob,sullivan\, claire" allowing the {@link Decoder#decode(java.lang.String)  } to faithfully reproduce the correct values.</p>
   *
   * @param esc the character(s) to be placed before special character sequences
   * @return this SeparatedString
   */
  public Builder withEscapeChar(String esc) {
    separatedString.withEscapeChar(esc);
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
  public Builder withKeyValueSeparator(String probablyEquals) {
    separatedString.withKeyValueSeparator(probablyEquals);
    return this;
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
  public <T> Builder setFormatFor(Class<T> clazz, Function<T, String> formatter) {
    separatedString.setFormatFor(clazz, formatter);
    return this;
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
  public Builder withNullsAs(String useInsteadOfNull) {
    separatedString.withNullsAs(useInsteadOfNull);
    return this;
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
  public Builder withClosedLoop() {
    separatedString.withClosedLoop();
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
  public Builder withOpenLoop() {
    separatedString.withOpenLoop();
    return this;
  }

  /**
   * Implements support for quoting values to distinguish them from the other elements of the SeparatedString.
   *
   * <p>
   * for instance {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()} would produce "smith,
   * alice,staines, bob,sullivan, claire" forcing {@link Decoder#decode(java.lang.String)
   * } to report the values as ("smith", " alice","staines", " bob", "sullivan", " claire").</p>
   * <p>
   * using a wraparound term
   * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withEachTermPrecededAndFollowedWith("|").encode()} would
   * produce "|smith, alice|,|staines, bob|,|sullivan, claire|" allowing {@link Decoder#decode(java.lang.String)
   * } to report the values correctly.</p>
   *
   * @param wrapAroundEachTerm the character sequence to be placed before and after every value
   * @return the SeparatedString
   */
  public Builder withEachTermPrecededAndFollowedWith(String wrapAroundEachTerm) {
    separatedString.withEachTermPrecededAndFollowedWith(wrapAroundEachTerm);
    return this;
  }
  /**
   * Implements support for quoting values to distinguish them from the other elements of the SeparatedString.
   *
   * <p>
   * for instance {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()} would produce "smith,
   * alice,staines, bob,sullivan, claire" forcing {@link Decoder#decode(java.lang.String)
   * } to report the values as ("smith", " alice","staines", " bob", "sullivan", " claire").</p>
   * <p>
   * using a wraparound term
   * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withEachTermWrappedWith("{","}").encode()} would produce
   * "{smith, alice},{staines, bob},{sullivan, claire}" allowing {@link Decoder#decode(java.lang.String)
   * } to report the values correctly.</p>
   *
   * @param beforeEachTerm the character sequence to be placed before every value
   * @param afterEachTerm the character sequence to be placed after every value
   * @return this SeparatedString
   */
  public Builder withEachTermWrappedWith(String beforeEachTerm, String afterEachTerm) {
    separatedString.withEachTermWrappedWith(beforeEachTerm, afterEachTerm);
    return this;
  }
  /**
   * Implements support for quoting values to distinguish them from the other elements of the SeparatedString.
   *
   * <p>
   * for instance {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()} would produce "smith,
   * alice,staines, bob,sullivan, claire" forcing {@link Decoder#decode(java.lang.String)
   * } to report the values as ("smith", " alice","staines", " bob", "sullivan", " claire").</p>
   * <p>
   * using a wraparound term null
   * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withThisBeforeEachTerm("[").encode()} would produce
   * "[smith, alice,[staines, bob,[sullivan, claire" allowing {@link Decoder#decode(java.lang.String)} to report the values correctly.</p>
   *
   * <p>
   * You should probably also use {@link #withThisAfterEachTerm(java.lang.String)
   * }
   * </p>
   *
   * @param placeAtTheBeginningOfEachTerm the character sequence to be placed before every value
   * @return this SeparatedString
   */
  public Builder withThisBeforeEachTerm(String placeAtTheBeginningOfEachTerm) {
    separatedString.withThisBeforeEachTerm(placeAtTheBeginningOfEachTerm);
    return this;
  }

  /**
   * Implements support for quoting values to distinguish them from the other elements of the SeparatedString.
   *
   * <p>
   * for instance {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").encode()} would produce "smith,
   * alice,staines, bob,sullivan, claire" forcing {@link Decoder#decode(java.lang.String)
   * } to report the values as ("smith", " alice","staines", " bob", "sullivan", " claire").</p>
   * <p>
   * using a wraparound term null
   * {@code separatedString.addAll("smith, alice","staines, bob", "sullivan, claire").separatedBy(",").withThisAfterEachTerm("]").encode()} would produce
   * "smith, alice],staines, bob],sullivan, claire]" allowing {@link Decoder#decode(java.lang.String)} to report the values correctly.</p>
   *
   * <p>
   * You should probably also use {@link #withThisBeforeEachTerm(java.lang.String)
   * }
   * </p>
   *
   * @param placeAtTheEndOfEachTerm the character sequence to be placed before every value
   * @return this SeparatedString
   */
  public Builder withThisAfterEachTerm(String placeAtTheEndOfEachTerm) {
    separatedString.withThisAfterEachTerm(placeAtTheEndOfEachTerm);
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
  public Builder withPrefix(String placeAtTheBeginningOfTheString) {
    separatedString.withPrefix(placeAtTheBeginningOfTheString);
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
  public Builder withSuffix(String placeAtTheEndOfTheString) {
    separatedString.withSuffix(placeAtTheEndOfTheString);
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
  public Builder withNullsRetained(boolean addNullsAsNull) {
    separatedString.withNullsRetained(addNullsAsNull);
    return this;
  }

  /**
   * Provides a value to be used whenever the encoded result would be empty.
   *
   * @param string a string to use instead of returning an empty string while encoding.
   * @return this SeparatedString
   */
  public final Builder useWhenEmpty(String string) {
    separatedString.useWhenEmpty(string);
    return this;
  }

  /**
   * The character sequence that should be at the beginning of every line, probably nothing.
   * 
   * @param lineStartSequence
   * @return the sequence used at the beginning of a line
   */
  public Builder withLineStartSequence(String lineStartSequence) {
    separatedString.withLineStartSequence(lineStartSequence);
    return this;
  }

  /**
   * The character sequence that should be at the end of every line, probably \n.
   * 
   * @param lineEndSequence
   * @return the sequence used at the end of a line
   */
  public Builder withLineEndSequence(String lineEndSequence) {
    separatedString.withLineEndSequence(lineEndSequence);
    return this;
  }

  /**
   * Creates a Builder with recommended values for creating and consuming a CSV file.
   *
   * <p>
   * Please note: CSV file implementations vary wildly and this SeparatedString may not process for all CSV files, however it will produce reliable CSV files
   * and is recommended.</p>
   *
   * @return a Builder
   */
  public static Builder csv() {
    return forSeparator(", ")
            .withThisBeforeEachTerm("\"")
            .withThisAfterEachTerm("\"")
            .withEscapeChar("\\")
            .withKeyValueSeparator("=");
  }

  /**
   * Creates a SeparatedString with recommended values for creating and consuming a TSV (tab separated value file) file
   *
   * <p>
   * Please note: TSV file implementations vary wildly and this SeparatedString may not process for all TSV files, however it will produce reliable TSV files
   * and is recommended.</p>
   *
   * @return a SeparatedString
   */
  public static Builder tsv() {
    return byTabs().withEachTermPrecededAndFollowedWith("\"").withEscapeChar("\\").withKeyValueSeparator("=");
  }

  /**
   * Creates a SeparatedString with recommended values for creating an HTML ordered list.
   *
   * @return a SeparatedString
   */
  public static Builder htmlOrderedList() {
    final Builder forSeparator = Builder.forSeparator("\n");
    return forSeparator.withThisBeforeEachTerm("<li>").withThisAfterEachTerm("</li>").withPrefix("<ol>\n").withSuffix("\n</ol>\n");
  }

  /**
   * Creates a SeparatedString with recommended values for creating an HTML unordered list.
   *
   * @return a SeparatedString
   */
  public static Builder htmlUnorderedList() {
    return forSeparator("\n").withThisBeforeEachTerm("<li>").withThisAfterEachTerm("</li>").withPrefix("<ul>\n").withSuffix("\n</ul>\n");
  }

  /**
   * Creates a new SeparatedString that starts with provided value.
   *
   * <p>
   * This adds a prefix to the separated string result. For instance SeparatedString.byCommas().startsWith("LIST=").addAll("1","2","3") will produce
   * "LIST=1,2,3"
   *
   * @param precedingString the string to include at the very beginning of the generated separated string
   * @return a SeparatedString that will have precedingString at the beginning of the output
   */
  public Builder startsWith(String precedingString) {
    return withPrefix(precedingString);
  }

  /**
   * Creates a new SeparatedString that ends with provided value.
   *
   * <p>
   * This adds a prefix to the separated string result. For instance SeparatedString.byCommas().endsWith("=LIST").addAll("1","2","3") will produce
   * "1,2,3=LIST"
   *
   * @param followingString  the string to include at the very end of the generated separated string
   * @return a Builder that will have follwingString at the end of the output
   */
  public Builder endsWith(String followingString) {
    return withSuffix(followingString);
  }

  /**
   * Creates a SeparatedString that will use the provided separator between values.
   *
   * <p>
   * SeparatedString.forSeparator(",").addAll("1","2","3").toString() will produce "1,2,3"
   *
   * @param separator the characters, like ", ", used to separate that values within the separated string
   * @return a SeparatedString
   */
  public static Builder forSeparator(String separator) {
    return new Builder().separatedBy(separator);
  }

  /**
   * Creates a SeparatedString that separates the values with a single space.
   *
   * @return a SeparateString
   */
  public static Builder bySpaces() {
    return forSeparator(" ");
  }

  /**
   * Creates a SeparatedString that separates the values with a single comma.
   *
   * @return a SeparateString
   */
  public static Builder byCommas() {
    return forSeparator(",");
  }

  /**
   * Creates a SeparatedString that separates the values with a comma and a single space.
   *
   * @return a SeparateString
   */
  public static Builder byCommaSpace() {
    return forSeparator(", ");
  }

  /**
   * Creates a SeparatedString that separates the values with a single comma, but also encapsulating each value in quotes and defining backslash as an escape
   * sequence.
   *
   * <p>
   * This is a more robust SeparateString definition than byCommas().</p>
   *
   * @return a SeparateString
   */
  public static Builder byCommasWithQuotedTermsAndBackslashEscape() {
    return forSeparator(", ")
            .withThisBeforeEachTerm("\"")
            .withThisAfterEachTerm("\"")
            .withEscapeChar("\\");
  }

  /**
   * Creates a SeparatedString that separates the values with a single comma, but also encapsulating each value in quotes and defining \\ as an escape sequence.
   *
   * <p>
   * This is a more robust SeparateString definition than byCommas().</p>
   *
   * @return a SeparateString
   */
  public static Builder byCommasWithQuotedTermsAndDoubleBackslashEscape() {
    return byCommas()
            .withThisBeforeEachTerm("\"")
            .withThisAfterEachTerm("\"")
            .withEscapeChar("\\\\");
  }

  /**
   * Creates a SeparatedString that separates the values with a tab.
   *
   * <p>
   * You should probably use {@link #tsv() } instead as it produces more reliable encodings</p>
   *
   * @return a SeparateString
   */
  public static Builder byTabs() {
    return forSeparator("\t");
  }

  /**
   * Creates a SeparatedString that separates the values with a newline character.
   *
   * @return a SeparateString
   */
  public static Builder byLines() {
    return forSeparator("\n");
  }

  /**
   * Creates a SeparatedString that separates the values with a space.
   *
   * @return a SeparateString
   */
  public static Builder spaceSeparated() {
    return bySpaces();
  }

  /**
   * Creates a SeparatedString that separates the values with a comma.
   *
   * <p>
   * You should probably use {@link #csv() } instead as it produces more reliable encodings</p>
   *
   * @return a SeparateString
   */
  public static Builder commaSeparated() {
    return byCommas();
  }

  /**
   * Creates a SeparatedString that separates the values with a tab.
   *
   * <p>
   * You should probably use {@link #tsv() } instead as it produces more reliable encodings</p>
   *
   * @return a SeparateString
   */
  public static Builder tabSeparated() {
    return byTabs();
  }

  /**
   * Creates a SeparatedString that separates the values with a new line character.
   *
   * @return a SeparateString
   */
  public static Builder lineSeparated() {
    return byLines();
  }

  protected SeparatedString getSeparatedString() {
    return separatedString;
  }

  /**
   * Instructs the SeparatedString to not modify the end of the list to conform with any looping system, this is the default.
   *
   * <p>
   * While loops are unusual for most applications, they are useful when defining polygons in some GIS systems.</p>
   *
   * @return this SeparatedString
   */
  public Builder withNoLoop() {
    separatedString.withNoLoop();
    return this;
  }

}
