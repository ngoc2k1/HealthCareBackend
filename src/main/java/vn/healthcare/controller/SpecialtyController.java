package vn.healthcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.healthcare.dto.BaseResponse;
import vn.healthcare.dto.SpecialtyRequest;
import vn.healthcare.service.SpecialtyService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    // Passed
    // Done
    @GetMapping("/list-specialty")
    public BaseResponse getListSpecialty() {
        return specialtyService.getAllSpecialty();
    }


    // New
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/specialty/{id}")
    public BaseResponse getSpecialtyById(@PathVariable Integer id) {
        return specialtyService.getSpecialtyById(id);
    }

    // New
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/specialty/create")
    public ResponseEntity<BaseResponse> createSpecialty(@Valid @RequestBody SpecialtyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(specialtyService.createSpecialty(request));
    }

    // New
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/specialty/update/{id}")
    public BaseResponse updateSpecialty(@Valid @RequestBody SpecialtyRequest request,
                                        @PathVariable Integer id) {
        return specialtyService.updateSpecialty(id, request);
    }

    // New
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/specialty/delete/{id}")
    public BaseResponse deleteSpecialty(@PathVariable Integer id) {
        return specialtyService.deleteSpecialty(id);
    }
}
