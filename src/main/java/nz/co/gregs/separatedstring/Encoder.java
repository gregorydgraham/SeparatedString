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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gregorygraham
 */
public class Encoder {

  private final SeparatedString separatedString;

  protected Encoder(SeparatedString sep) {
    separatedString = sep;
  }

  public Builder builder() {
    return new Builder(SeparatedString.copy(separatedString));
  }

  public Decoder decoder() {
    return new Decoder(SeparatedString.copy(separatedString));
  }

  public String encode() {
    return separatedString.encode();
  }

  @Override
  public String toString() {
    return separatedString.encode();
  }
  
  public String describe(){
    return separatedString.describe();
  }

  public String encode(String... strs) {
    return separatedString.encode(strs);
  }

  public String encode(List<Object> strs) {
    return separatedString.encode(strs);
  }

  public Encoder addAll(int index, Collection<?> c) {
    separatedString.addAll(index, c);
    return this;
  }

  public Encoder addAll(List<Object> c) {
    separatedString.addAll(c);
    return this;
  }

  public Encoder add(String key, String value) {
    separatedString.add(key, value);
    return this;
  }

  public Encoder add(String key, Object value) {
    separatedString.add(key, value);
    return this;
  }

  public Encoder addAllObjectMap(Map<String, Object> c) {
    separatedString.addAllObjectMap(c);
    return this;
  }

  public Encoder addAll(Map<String, String> c) {
    separatedString.addAll(c);
    return this;
  }

  public Encoder addLine(String... strs) {
    separatedString.addLine(strs);
    return this;
  }

  public Encoder addLine() {
    separatedString.addLine();
    return this;
  }

  public Encoder addAll(String... strs) {
    separatedString.addAll(strs);
    return this;
  }

  public <TYPE> Encoder addAll(Function<TYPE, String> stringProcessor, TYPE... objects) {
    separatedString.addAll(stringProcessor, objects);
    return this;
  }

  public <TYPE> Encoder addAll(Function<TYPE, String> stringProcessor, List<TYPE> objects) {
    separatedString.addAll(stringProcessor, objects);
    return this;
  }

  public Encoder add(int index, String element) {
    separatedString.add(index, element);
    return this;
  }

  public Encoder add(int index, Object element) {
    separatedString.add(index, element);
    return this;
  }

  public Encoder add(String string) {
    separatedString.add(string);
    return this;
  }

  public Encoder add(Object string) {
    separatedString.add(string);
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
  public Encoder containing(Object... strings) {
    separatedString.containing(strings);
    return this;
  }

  public boolean isNotEmpty() {
    return separatedString.isNotEmpty();
  }

  public boolean isEmpty() {
    return separatedString.isEmpty();
  }

  public SeparatedString removeAll(String... c) {
    return separatedString.removeAll(c);
  }

  public SeparatedString removeAll(List<Object> baddies) {
    return separatedString.removeAll(baddies);
  }

  public SeparatedString removeAll(Map<String, Object> c) {
    return separatedString.removeAll(c);
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
    return separatedString.remove(index);
  }

  protected SeparatedString getSeparatedString() {
    return separatedString;
  }

}
