package com.example.resourcemanagement.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.example.resourcemanagement.dto.request.ResourceRequest;
import com.example.resourcemanagement.entity.Resource;
import com.example.resourcemanagement.repository.ResourceRepository;

import jakarta.validation.Valid;

@Service
public class ResourceService {
	private final ResourceRepository resourceRepository;
	
	public ResourceService(ResourceRepository resourceRepository) {
		this.resourceRepository=resourceRepository;
	}
	
	public Resource toEntity(ResourceRequest request) {
		Resource resource = new Resource();
		resource.setName(request.getName());
		resource.setPrice(request.getPrice());
		resource.setDescription(request.getDescription());
		resource.setType(request.getType());
	
		return resource;
	}
	
	public List<Resource> getAllResources(){
		return resourceRepository.findAll();
	}

	public Resource createResource(@Valid ResourceRequest request) {
		// TODO Auto-generated method stub
		return resourceRepository.save(toEntity(request));
	}

	public Resource updateResource(@Valid Long id, @Valid ResourceRequest request) {
		// TODO Auto-generated method stub
		Resource resource=resourceRepository.findById(id).orElseThrow(()->new RuntimeException("Resource not found"));
		resource.setName(request.getName());
		resource.setPrice(request.getPrice());
		resource.setDescription(request.getDescription());
		resource.setType(request.getType());
		
		return resource;
	}

	public void deleteResource(Long id) {
		// TODO Auto-generated method stub
		
	}
}
