package com.accountplace.api.controller.consulter;


import com.accountplace.api.dto.register.RegisterPlatformDTO;
import com.accountplace.api.entity.PlatformEntity;
import com.accountplace.api.dto.review.PlatformDto;
import com.accountplace.api.service.PlateformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data/platform")
public class PlatformController {

    //dependencies
    private final PlateformService plateformService;

    @Autowired
    private PlatformController (PlateformService plateformService) {
        this.plateformService = plateformService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlatformDto>> getAll() {
        try {
            List<PlatformDto> result = plateformService.listAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<PlatformDto> findById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(plateformService.findById(Integer.valueOf(id)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/search/{filter}")
    public ResponseEntity<List<PlatformDto>> findByName(@PathVariable("filter") String filter, @RequestParam("search_query") String search_query) {
        if (filter == null || filter.isEmpty()) { return ResponseEntity.notFound().build();}
        if (search_query ==  null || search_query.isEmpty()) { return ResponseEntity.notFound().build();}
        try {
            switch (filter) {
                case "name", "url" -> {
                    return ResponseEntity.ok(plateformService.searchByName(search_query));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/create")
    public ResponseEntity<PlatformDto> create(@RequestBody PlatformEntity bodyDTO) {
        try {
            PlatformEntity platform = new PlatformEntity();
            platform.setName(bodyDTO.getName());
            platform.setUrl(bodyDTO.getUrl());
            platform.setImgRef(bodyDTO.getImgRef());
            PlatformEntity result = plateformService.create(bodyDTO);
            return ResponseEntity.ok(plateformService.findById(result.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PlatformDto> update(@PathVariable("id") Integer id, @RequestBody RegisterPlatformDTO bodyDTO) {
        try {
            PlatformEntity platform = plateformService.getEntity(id);
            platform.setName(bodyDTO.getName());
            platform.setUrl(bodyDTO.getUrl());
            platform.setImgRef(bodyDTO.getImg());
            PlatformEntity result = plateformService.update(id, platform);
            return ResponseEntity.ok(plateformService.findById(result.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") Integer id) {
        try {
            String result = plateformService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
