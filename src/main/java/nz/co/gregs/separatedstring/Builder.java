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
 *
 * @author gregorygraham
 */
public class Builder {

  private final SeparatedString separatedString;

  public static Builder start() {
    return new Builder();
  }

  protected Builder() {
    this.separatedString = new SeparatedString();
  }

  protected Builder(SeparatedString separatedString) {
    this.separatedString = separatedString;
  }

  public Encoder encoder() {
    return new Encoder(separatedString);
  }

  public Decoder decoder() {
    return new Decoder(separatedString);
  }

  public Builder trimBlanks() {
    separatedString.trimBlanks();
    return this;
  }

  public Builder withOnlyUniqueValues() {
    separatedString.withOnlyUniqueValues();
    return this;
  }

  public Builder separatedBy(String separator) {
    separatedString.separatedBy(separator);
    return this;
  }

  public Builder withEscapeChar(String esc) {
    separatedString.withEscapeChar(esc);
    return this;
  }

  public Builder withKeyValueSeparator(String probablyEquals) {
    separatedString.withKeyValueSeparator(probablyEquals);
    return this;
  }

  public <T> Builder setFormatFor(Class<T> clazz, Function<T, String> formatter) {
    separatedString.setFormatFor(clazz, formatter);
    return this;
  }

  public Builder withNullsAs(String useInsteadOfNull) {
    separatedString.withNullsAs(useInsteadOfNull);
    return this;
  }

  public Builder withClosedLoop() {
    separatedString.withClosedLoop();
    return this;
  }

  public Builder withOpenLoop() {
    separatedString.withOpenLoop();
    return this;
  }

  public Builder withEachTermPrecededAndFollowedWith(String wrapAroundEachTerm) {
    separatedString.withEachTermPrecededAndFollowedWith(wrapAroundEachTerm);
    return this;
  }

  public Builder withEachTermWrappedWith(String beforeEachTerm, String afterEachTerm) {
    separatedString.withEachTermWrappedWith(beforeEachTerm, afterEachTerm);
    return this;
  }

  public Builder withThisBeforeEachTerm(String placeAtTheBeginningOfEachTerm) {
    separatedString.withThisBeforeEachTerm(placeAtTheBeginningOfEachTerm);
    return this;
  }

  public Builder withThisAfterEachTerm(String placeAtTheEndOfEachTerm) {
    separatedString.withThisAfterEachTerm(placeAtTheEndOfEachTerm);
    return this;
  }

  public Builder withPrefix(String placeAtTheBeginningOfTheString) {
    separatedString.withPrefix(placeAtTheBeginningOfTheString);
    return this;
  }

  public Builder withSuffix(String placeAtTheEndOfTheString) {
    separatedString.withSuffix(placeAtTheEndOfTheString);
    return this;
  }

  public Builder withNullsRetained(boolean addNullsAsNull) {
    separatedString.withNullsRetained(addNullsAsNull);
    return this;
  }

  public final Builder useWhenEmpty(String string) {
    separatedString.useWhenEmpty(string);
    return this;
  }

  public Builder withLineStartSequence(String lineStartSequence) {
    separatedString.withLineStartSequence(lineStartSequence);
    return this;
  }

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
    return byCommasWithQuotedTermsAndBackslashEscape().withKeyValueSeparator("=");
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
  public static Builder startsWith(String precedingString) {
    return start().withPrefix(precedingString);
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
    return byCommas()
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

}
