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

	public static StringEntry of(String value) {
		return new StringEntry(value);
	}

	public static StringEntry of(String key, String value) {
		return new StringEntry(key, value);
	}

	private boolean hasKey = false;
	private String key;
	private String entry;

	public StringEntry() {
	}

	public StringEntry(String value) {
		entry = value;
	}

	public StringEntry(String key, String value) {
		this(value);
		this.key = key;
		hasKey = true;
	}

	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		return entry.isEmpty();
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
	public String getValue() {
		return entry;
	}

}
