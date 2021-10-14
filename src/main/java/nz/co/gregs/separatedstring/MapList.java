/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.gregs.separatedstring;

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

	public MapList() {
		super();
	}

	public MapList(Collection<? extends Pair<KEY, VALUE>> c) {
		super(c);
	}

	public MapList(int initialCapacity) {
		super(initialCapacity);
	}

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
