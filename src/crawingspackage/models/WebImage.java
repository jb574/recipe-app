package crawingspackage.models;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;

/**
 * A class capable of handling the images we store from BigOven to show to our
 * users when they search
 * 
 * @author Guillaume Boell
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class WebImage {
	@PrimaryKey
	@Persistent
	private long imageId;
	@Persistent
	private String imageUrl;
	@Persistent
	private Blob imageData;
	@Persistent
	String imageContentType;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	public Blob getImageData() {
		return imageData;
	}

	public void setImageData(Blob imageData) {
		this.imageData = imageData;
	}

	public byte[] getImageBytes() {
		return imageData.getBytes();
	}

	public void setImageBytes(byte[] data) {
		this.imageData = new Blob(data);
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
}
