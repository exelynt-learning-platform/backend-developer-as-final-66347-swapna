package com.example.resourcemanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resourcemanagement.dto.request.ResourceRequest;
import com.example.resourcemanagement.entity.Resource;
import com.example.resourcemanagement.service.ResourceService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/resources")
public class ResourceController {
	
	private final ResourceService resourceService;
	
	public ResourceController(ResourceService resourceService) {
		this.resourceService=resourceService;
	}

	@GetMapping
	public ResponseEntity<List<Resource>> getAllResources() {
		return ResponseEntity.ok(resourceService.getAllResources());
	}
	
	@PostMapping("path")
	public ResponseEntity<Resource> createResource(
			@Valid @RequestBody ResourceRequest request ) {
		return ResponseEntity.status(HttpStatus.CREATED).body(resourceService.createResource(request));
	}
	
}
