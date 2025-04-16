package com.snir.slide_show.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SlideController {
	//@Autowired SlideService slideService;

	@RequestMapping("/")
	public ResponseEntity<Object> index() {
		System.out.println("OK .............");
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// POST /addImage
	@PostMapping("/addImage")
	public ResponseEntity<String> addImage(@RequestParam String imageUrl, @RequestParam int duration) {
		// Validate URL and check if it contains an image (JPEG, PNG, WEBP, etc.)
		// Add logic to save the image and duration
		return ResponseEntity.status(HttpStatus.CREATED).body("Image added successfully.");
	}

	// DELETE /deleteImage/{id}
	@DeleteMapping("/deleteImage/{id}")
	public ResponseEntity<String> deleteImage(@PathVariable Long id) {
		// Add logic to delete the image by ID
		return ResponseEntity.ok("Image deleted successfully.");
	}

	// POST /addSlideshow
	@PostMapping("/addSlideshow")
	public ResponseEntity<String> addSlideshow(@RequestBody Map<String, Object> slideshowRequest) {
		// Extract images and durations from the request body
		// Add logic to save the slideshow
		return ResponseEntity.status(HttpStatus.CREATED).body("Slideshow added successfully.");
	}

	// DELETE /deleteSlideshow/{id}
	@DeleteMapping("/deleteSlideshow/{id}")
	public ResponseEntity<String> deleteSlideshow(@PathVariable Long id) {
		// Add logic to delete the slideshow by ID
		System.out.println("GET .............");
		return ResponseEntity.ok("Slideshow deleted successfully.");
	}

	// GET /images/search
	@GetMapping("/images/search")
	public ResponseEntity<List<Map<String, Object>>> searchImages(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer duration) {
		// Add logic to search for images and associated slideshows
		return ResponseEntity.ok(List.of()); // Return a list of matching images and slideshows
	}

	// GET /slideShow/{id}/slideshowOrder
	@GetMapping("/slideShow/{id}/slideshowOrder")
	public ResponseEntity<List<Map<String, Object>>> getSlideshowOrder(@PathVariable Long id) {
		// Add logic to retrieve images in a slideshow ordered by addition date
		return ResponseEntity.ok(List.of()); // Return ordered images
	}

	// POST /slideShow/{id}/proof-of-play/{imageId}
	@PostMapping("/slideShow/{id}/proof-of-play/{imageId}")
	public ResponseEntity<String> recordProofOfPlay(@PathVariable Long id, @PathVariable Long imageId) {
		// Add logic to record the proof-of-play event
		return ResponseEntity.ok("Proof of play recorded successfully.");
	}
}
