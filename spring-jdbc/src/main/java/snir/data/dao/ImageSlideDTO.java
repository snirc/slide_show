package snir.data.dao;

public class ImageSlideDTO {
    private final int id;
    private final String imageName;
    private final String imageUrl;
    private final int slideShowId;
    private final String slideShowName;

    public ImageSlideDTO(int id, String imageName, String imageUrl, int slideShowId, String slideShowName) {
        this.id = id;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.slideShowId = slideShowId;
        this.slideShowName = slideShowName;
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
}

