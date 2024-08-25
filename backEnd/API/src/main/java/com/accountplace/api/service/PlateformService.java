package com.accountplace.api.service;

import com.accountplace.api.entity.PlatformEntity;
import com.accountplace.api.dto.review.PlatformDto;
import com.accountplace.api.repositories.PlateformRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class PlateformService {


    private final PlateformRepository platformRepository;

    @Autowired
    private PlateformService (PlateformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    public PlatformEntity getEntity(Integer id) {
        return platformRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public PlatformDto findById(Integer id) {
        PlatformEntity platformEntity = platformRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return this.convertToDto(platformEntity);
    }

    public List<PlatformDto> listAll() {
        List<PlatformEntity> platformEntities = platformRepository.findAll();
        return platformEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<PlatformDto> searchByName(String name) {
        List<PlatformEntity> platformEntities =  platformRepository.findBySearch(name);
        return platformEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PlatformEntity create(PlatformEntity platformEntity) {
        return platformRepository.save(platformEntity);
    }

    public PlatformEntity update(Integer id, PlatformEntity platformEntity) {
        return platformRepository.findById(id).map( plateform1 -> {
            plateform1.setName(platformEntity.getName());
            plateform1.setUrl(platformEntity.getUrl());
            plateform1.setImgRef(platformEntity.getImgRef());
            return platformRepository.save(plateform1);
        }).orElseThrow( () -> new RuntimeException("Plateform with " + id + " not found") );
    }

    public String delete(Integer id) {
        platformRepository.deleteById(id);
        return "Plateform with id " + id + " has been deleted successfully";
    }

    private PlatformDto convertToDto(PlatformEntity platformEntity) {
        return new PlatformDto(
            platformEntity.getId(),
            platformEntity.getName(),
            platformEntity.getUrl(),
            platformEntity.getImgRef()
        );
    }


}
