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

import java.util.List;
import java.util.function.Function;

/**
 * Creates a string formatter that produces simple HTML tables.
 *
 * <p>A simple wrapper around SeparatedString to make simple tables very simple</p>
 *
 * @author Gregory Graham
 */
public class HTMLTableString {

  private final SeparatedString htmlTableFormatter = new SeparatedString()
          .separatedBy("")
          .withLineStartSequence("<tr>")
          .withLineEndSequence("</tr>\n")
          .withThisBeforeEachTerm("<td>")
          .withThisAfterEachTerm("</td>")
          .withPrefix("<table>\n")
          .withSuffix("</table>\n");

  public String encode() {
    return htmlTableFormatter.encode();
  }

  public <T> SeparatedString setFormatFor(Class<T> clazz, Function<T, String> formatter) {
    return htmlTableFormatter.setFormatFor(clazz, formatter);
  }

  public String getNullRepresentation() {
    return htmlTableFormatter.getNullRepresentation();
  }

  public SeparatedString withNullsAs(String useInsteadOfNull) {
    return htmlTableFormatter.withNullsAs(useInsteadOfNull);
  }

  public boolean isNotEmpty() {
    return htmlTableFormatter.isNotEmpty();
  }

  public boolean isEmpty() {
    return htmlTableFormatter.isEmpty();
  }

  public SeparatedString removeAll(String... c) {
    return htmlTableFormatter.removeAll(c);
  }

  public SeparatedString removeAll(List<Object> baddies) {
    return htmlTableFormatter.removeAll(baddies);
  }

  public SeparatedString addLine(String... strs) {
    return htmlTableFormatter.addLine(strs);
  }

  public SeparatedString addLine(Object... strs) {
    return htmlTableFormatter.addLine(strs);
  }

  public SeparatedString addLine() {
    return htmlTableFormatter.addLine();
  }

  public SeparatedString withNullsRetained(boolean addNullsAsNull) {
    return htmlTableFormatter.withNullsRetained(addNullsAsNull);
  }

  public List<List<String>> parseToLines(String input) {
    return htmlTableFormatter.parseToLines(input);
  }

  public boolean getRetainNulls() {
    return htmlTableFormatter.getRetainNulls();
  }

}
