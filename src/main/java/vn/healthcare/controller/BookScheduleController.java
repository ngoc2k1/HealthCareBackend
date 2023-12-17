package vn.healthcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.healthcare.dto.BaseResponse;
import vn.healthcare.dto.CreateScheduleRequest;
import vn.healthcare.dto.PageResponse;
import vn.healthcare.dto.UpdateScheduleRequest;
import vn.healthcare.service.BookScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BookScheduleController {
    private final BookScheduleService bookScheduleService;

    // Api của BS xem danh sách lịch khám
    @PreAuthorize("hasAuthority('DOCTOR')")
    @GetMapping("/list-book-schedule-by-doctor")
    public PageResponse getAllScheduleByDoctor(@RequestParam(defaultValue = "1") Integer page) {
        return bookScheduleService.getAllScheduleByDoctor(page);
    }


    // Api của BN xem danh sách lịch khám
    @PreAuthorize("hasAuthority('PATIENT')")
    @GetMapping("/list-book-schedule-by-patient")
    public PageResponse getAllScheduleByPatient(@RequestParam(defaultValue = "1") Integer page) {
        return bookScheduleService.getAllScheduleByPatient(page);
    }

    // Api của BN xem chi tiết lịch khám
    @PreAuthorize("isAuthenticated()")//bat buoc login
    @GetMapping("/book-schedule/detail/{id}")
    public BaseResponse getDetailSchedule(@PathVariable Integer id) {
        return bookScheduleService.getBookScheduleDetail(id);
    }

    @PreAuthorize("hasAuthority('DOCTOR')")//chi BS
    @PutMapping("/book-schedule/confirm/{id}")
    public BaseResponse confirmBookSchedule(@PathVariable Integer id) {
        return bookScheduleService.confirm(id);
    }


    @PreAuthorize("hasAuthority('PATIENT')")
    @PutMapping("/book-schedule/cancel/{scheduleId}")
    public BaseResponse cancelBookSchedule(@PathVariable Integer scheduleId) {
        return bookScheduleService.cancelSchedule(scheduleId);
    }


    @PreAuthorize("hasAuthority('PATIENT')")
    @PutMapping("/book-schedule/update/{id}")
    public BaseResponse updateBookSchedule(@PathVariable Integer id,
                                           @RequestBody UpdateScheduleRequest request) {
        return bookScheduleService.updateSchedule(id, request);
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @PostMapping("/book-schedule/create")
    public ResponseEntity<BaseResponse> createBookSchedule(@RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookScheduleService.createSchedule(request));
    }
}
