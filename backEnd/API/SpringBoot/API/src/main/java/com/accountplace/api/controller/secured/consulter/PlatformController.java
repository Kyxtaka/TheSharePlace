package com.accountplace.api.controller.secured.consulter;


import com.accountplace.api.dto.crud.update.PublicPlatformDTO;
import com.accountplace.api.dto.requestBody.register.RegisterPlatformDTO;
import com.accountplace.api.entity.PlatformEntity;
import com.accountplace.api.service.consulter.PlatformConsulterService;
import com.accountplace.api.service.crud.PlatformCrudService;
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
    private final PlatformConsulterService platformConsulterService;
    private final PlatformCrudService platformCrudService;

    @Autowired
    private PlatformController (PlatformConsulterService platformConsulterService, PlatformCrudService platformCrudService) {
        this.platformConsulterService = platformConsulterService;
        this.platformCrudService = platformCrudService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PublicPlatformDTO>> getAll() {
        try {
            List<PublicPlatformDTO> result = platformConsulterService.listAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<PublicPlatformDTO> findById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(platformConsulterService.findById(Integer.valueOf(id)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/search/{filter}")
    public ResponseEntity<List<PublicPlatformDTO>> findByName(@PathVariable("filter") String filter, @RequestParam("search_query") String search_query) {
        if (filter == null || filter.isEmpty()) { return ResponseEntity.notFound().build();}
        if (search_query ==  null || search_query.isEmpty()) { return ResponseEntity.notFound().build();}
        try {
            switch (filter) {
                case "name", "url" -> {
                    return ResponseEntity.ok(platformConsulterService.searchByName(search_query));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

//    @PostMapping("/create")
//    public ResponseEntity<PublicPlatformDTO> create(@RequestBody PlatformEntity bodyDTO) {
//        try {
//            PlatformEntity platform = new PlatformEntity();
//            platform.setName(bodyDTO.getName());
//            platform.setUrl(bodyDTO.getUrl());
//            platform.setImgRef(bodyDTO.getImgRef());
//            PlatformEntity result = platformCrudService.create(bodyDTO);
//            return ResponseEntity.ok(platformConsulterService.findById(result.getId()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<PublicPlatformDTO> update(@PathVariable("id") Integer id, @RequestBody RegisterPlatformDTO bodyDTO) {
//        try {
//            PlatformEntity platform = platformConsulterService.(id);
//            platform.setName(bodyDTO.getName());
//            platform.setUrl(bodyDTO.getUrl());
//            platform.setImgRef(bodyDTO.getImg());
//            PlatformEntity result = platformConsulterService.update(id, platform);
//            return ResponseEntity.ok(platformConsulterService.findById(result.getId()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") Integer id) {
        try {
            String result = platformCrudService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
