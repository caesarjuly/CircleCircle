package buffer;

import java.util.HashMap;

import android.graphics.Bitmap;

public class PhotoBuffer extends Buffer implements Hash<Bitmap>{
	
	private static PhotoBuffer cb;

	private PhotoBuffer() {
		hashBuffer = new HashMap<String, Bitmap>();
	}

	public static PhotoBuffer getInstance() {
		if (cb == null) {
			cb = new PhotoBuffer();
		}
		return cb;
	}
	

	@Override
	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return hashBuffer.containsKey(key);
	}

	@Override
	public void put(String key, Bitmap value) {
		// TODO Auto-generated method stub
		hashBuffer.put(key, value);
	}

	@Override
	public Bitmap get(String key) {
		// TODO Auto-generated method stub
		return (Bitmap) hashBuffer.get(key);
	}

	@Override
	public Bitmap remove(String key) {
		// TODO Auto-generated method stub
		return (Bitmap) hashBuffer.remove(key);
	}

}
