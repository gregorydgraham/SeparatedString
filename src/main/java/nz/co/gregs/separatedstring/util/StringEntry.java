/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nz.co.gregs.separatedstring.util;

import nz.co.gregs.separatedstring.SeparatedString;

/**
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

	public String getFormattedString(SeparatedString sepStr) {
		StringBuilder build = new StringBuilder();
		if (hasKey()) {
			build.append(replaceSequencesInString(getKey(), sepStr.getReplacementSequences()));
			build.append(sepStr.getKeyValueSeparator());
		}
		String value = getValue();
		if (value == null && !sepStr.getRetainNulls()) {
			value = sepStr.getEmptyValue();
		}
		if (value != null && value.isEmpty()) {
			value = sepStr.getEmptyValue();
		}
		build.append(replaceSequencesInString(value, sepStr.getReplacementSequences()));

		return build.toString();
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

}
