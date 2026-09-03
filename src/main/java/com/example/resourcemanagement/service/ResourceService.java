package com.example.resourcemanagement.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.example.resourcemanagement.dto.ResourceRequest;
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
}
