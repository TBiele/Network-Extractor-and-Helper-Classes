import java.util.HashMap;

/**
 * A HashMap that returns a default value for each key that is not contained (from https://stackoverflow.com/a/7519422).
 * @param <K> Type of the key
 * @param <V> Type of the value
 */
public class DefaultHashMap<K,V> extends HashMap<K,V> {
	private static final long serialVersionUID = 1L;
	protected V defaultValue;
	public DefaultHashMap(V defaultValue) {
	  this.defaultValue = defaultValue;
	}
	@Override
	public V get(Object k) {
	  return containsKey(k) ? super.get(k) : defaultValue;
	}
}
