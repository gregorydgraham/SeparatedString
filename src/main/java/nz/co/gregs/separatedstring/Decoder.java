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
import java.util.Map;

/**
 *
 * @author gregorygraham
 */
public class Decoder {

  private final SeparatedString separatedString;

  protected Decoder(SeparatedString sep) {
    separatedString = sep;
  }

  public Builder builder() {
    return new Builder(SeparatedString.copy(separatedString));
  }

  public Encoder encoder() {
    return new Encoder(SeparatedString.copy(separatedString));
  }

  public List<String> decode(String str) {
    return separatedString.decode(str);
  }

  public List<String> decodeToList(String input) {
    return separatedString.parseToList(input);
  }

  public List<List<String>> decodeToLines(String input) {
    return separatedString.parseToLines(input);
  }

  public String[] decodeToArray(String input) {
    return separatedString.parseToArray(input);
  }

  public Map<String, String> decodeToMap(String input) {
    return separatedString.parseToMap(input);
  }

  protected SeparatedString getSeparatedString() {
    return separatedString;
  }
}
