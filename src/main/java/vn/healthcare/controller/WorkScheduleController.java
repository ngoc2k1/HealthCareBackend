package vn.healthcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.healthcare.dto.BaseResponse;
import vn.healthcare.dto.EditScheduleRequest;
import vn.healthcare.service.DoctorWorkScheduleService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class WorkScheduleController {
    private final DoctorWorkScheduleService doctorWorkScheduleService;

    @GetMapping("/date-by-doctor/{id}")
    public BaseResponse dateByDoctor(@PathVariable Integer id) {
        return doctorWorkScheduleService.getAllDateByDoctor(id);
    }

    @GetMapping("/time-by-doctor/{id}")
    public BaseResponse timeByDoctor(@PathVariable Integer id,
                                     @RequestParam String date) {
        return doctorWorkScheduleService.getAllTimeByDoctorAndDate(id, date);
    }

    // New
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit-schedule")
    public BaseResponse editSchedule(@RequestBody @Valid EditScheduleRequest request) {
        return doctorWorkScheduleService.editSchedule(request);
    }
}
