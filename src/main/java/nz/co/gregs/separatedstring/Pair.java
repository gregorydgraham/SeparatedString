/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.gregs.separatedstring;

/**
 *
 * @author gregorygraham
 */
class Pair<KEY, VALUE> {
	
	private final KEY key;
	private final VALUE value;

	public Pair(KEY key, VALUE value) {
		this.key = key;
		this.value = value;
	}

	public static <A, B> Pair<A, B> let(A key, B value) {
		return new Pair<>(key, value);
	}

	public KEY getKey() {
		return key;
	}

	public VALUE getValue() {
		return value;
	}
	
}
