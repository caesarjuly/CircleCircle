package buffer;

public interface Hash <T>{
	
	public boolean contains(String key);
	public void put(String key, T value);
	public T get(String key);
	public T remove(String key);
	
}
