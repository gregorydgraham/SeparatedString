/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A map that cares about the order of insertion, or a List that takes 2 values?
 *
 * <p>
 * Who knows.</p>
 *
 * @author gregorygraham
 * @param <KEY> the type of the key
 * @param <VALUE> the type of the value
 */
public class MapList<KEY, VALUE> extends ArrayList<Pair<KEY, VALUE>> {

	/**
	 * Creates a new list that can store key/value pairs.
	 *
	 */
	public MapList() {
		super();
	}

	/**
	 * Creates a new list that can store key/value pairs.
	 *
	 * @param c a collection of key/value pairs
	 */
	public MapList(Collection<? extends Pair<KEY, VALUE>> c) {
		super(c);
	}

	/**
	 * Creates a new list that can store key/value pairs.
	 *
	 * @param initialCapacity the initial capacity of the MapList
	 */
	public MapList(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Adds a key/value pair.
	 *
	 * <p>
	 * Internally the pair is stored as a Pair object.</p>
	 *
	 * <p>
	 * Before insertion, the key is checked to ensure it is non-null, not empty,
	 * and unique.</p>
	 *
	 * <p>
	 * Pairs are stored in insertion order, not other ordering is provided.</p>
	 * @param key the key of the pair, this is (generally) the part of the pair that is referenced or compared
	 * @param value the value of the pair
	 * @return TRUE if the pair is inserted, otherwise FALSE
	 */
	public boolean add(KEY key, VALUE value) {
		boolean result = false;
		// make sure the key actually has a value
		if (key != null) {
			// and that value is meaningful
			if (isNotEmpty(key)) {
				// and isn't already in the list
				boolean alreadyInList = stream().anyMatch(p -> p.getKey().equals(key));
				if (!alreadyInList) {
					// its a value, with meaning, and unique
					// add it :)
					result = super.add(Pair.let(key, value));
				}
			}
		}
		return result;
	}

	private boolean isNotEmpty(KEY key) {
		return !key.toString().isEmpty();
	}
}
