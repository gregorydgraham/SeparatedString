/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 * Stores a key/value pair.
 *
 * @author gregorygraham
 * @param <KEY> the type of the key
 * @param <VALUE> the type of the value
 */
public class Pair<KEY, VALUE> implements Comparable<KEY> {

	private final KEY key;
	private final VALUE value;

	/**
	 * Creates a new key/value pair.
	 *
	 * @param key the key of the pair, this is (generally) the part of the pair
	 * that is referenced or compared
	 * @param value the value of the pair
	 */
	public Pair(KEY key, VALUE value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Creates a new key/value pair.
	 *
	 * @param <A> the type of the key
	 * @param <B> the type of the value
	 * @param key the key of the pair, this is (generally) the part of the pair
	 * that is referenced or compared
	 * @param value the value of the pair
	 * @return the new Pair
	 */
	public static <A, B> Pair<A, B> let(A key, B value) {
		return new Pair<>(key, value);
	}

	/**
	 * Returns the key of the pair.
	 *
	 * @return the key
	 */
	public KEY getKey() {
		return key;
	}

	/**
	 * Return the value of the pair.
	 *
	 * @return the value
	 */
	public VALUE getValue() {
		return value;
	}

	@Override
	public int compareTo(KEY o) {
		return key.toString().compareTo(o.toString());
	}

}
