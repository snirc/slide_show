package snir.data.dao;

public class ImageSlideDTO {
	private final int id;
	private final String imageName;
	private final String imageUrl;
	private final int slideShowId;
	private final String slideShowName;
	private final int duration;

	public ImageSlideDTO(int id, String imageName, String imageUrl, int slideShowId, String slideShowName,
			int duration) {
		this.id = id;
		this.imageName = imageName;
		this.imageUrl = imageUrl;
		this.slideShowId = slideShowId;
		this.slideShowName = slideShowName;
		this.duration = 0;
	}

	public int getId() {
		return id;
	}

	public String getImageName() {
		return imageName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public int getSlideShowId() {
		return slideShowId;
	}

	public String getSlideShowName() {
		return slideShowName;
	}

	public int getDuration() {
		return duration;
	}
}
