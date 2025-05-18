/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nz.co.gregs.separatedstring.util;

/**
 * StringEntry is a simple Key + Value store where both key and value are assumed to be strings.
 *
 * @author gregorygraham
 */
public class StringEntry {

  private static StringEntry END_OF_LINE_MARKER = new StringEntry();
  private static StringEntry START_OF_LINE_MARKER = new StringEntry();

  private boolean hasKey = false;
  private String key;
  private Object entry;

  private StringEntry() {
  }

  public StringEntry(Object value) {
    entry = value;
  }

  public StringEntry(String key, Object value) {
    this(value);
    this.key = key;
    hasKey = true;
  }

  public static StringEntry of(String value) {
    return new StringEntry(value);
  }

  public static StringEntry of(Object value) {
    return new StringEntry(value);
  }

  public static StringEntry of(String key, String value) {
    return new StringEntry(key, value);
  }

  public static StringEntry of(String key, Object value) {
    return new StringEntry(key, value);
  }

  public static StringEntry of(StringEntry value) {
    if (value.hasKey()) {
      return new StringEntry(value.getKey(), value.getValue());
    } else {
      return new StringEntry(value.getValue());
    }
  }

  public static StringEntry getEndOfLineMarker() {
    return END_OF_LINE_MARKER;
  }

  public static StringEntry getStartOfLineMarker() {
    return START_OF_LINE_MARKER;
  }

  /**
   * @return the hasKey
   */
  public boolean hasKey() {
    return hasKey;
  }

  /**
   * @return the key
   */
  public String getKey() {
    return key;
  }

  /**
   * @return the entry
   */
  public Object getValue() {
    return entry;
  }

  public boolean isEndOfLineMarker() {
    return StringEntry.END_OF_LINE_MARKER.equals(this);
  }

  public boolean isStartOfLineMarker() {
    return StringEntry.START_OF_LINE_MARKER.equals(this);
  }

}
