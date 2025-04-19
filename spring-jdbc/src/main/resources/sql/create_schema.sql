CREATE SCHEMA IF NOT EXISTS slide;

CREATE TABLE IF NOT EXISTS slide.image (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS slide.slide_show (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS slide.slide_show_images (
    slide_show_id INT,
    image_id INT,
    PRIMARY KEY (slide_show_id, image_id),
    FOREIGN KEY (slide_show_id) REFERENCES slide.slide_show(id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES slide.image(id) ON DELETE CASCADE
);
