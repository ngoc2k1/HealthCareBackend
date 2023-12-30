package vn.healthcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.healthcare.dto.*;
import vn.healthcare.service.BookScheduleService;
import vn.healthcare.service.DoctorService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final BookScheduleService bookScheduleService;

    @GetMapping("/doctor-by-specialty/{id}")
    public PageResponse getDoctorBySpecialty(@PathVariable Integer id,
                                             @RequestParam(defaultValue = "1") Integer page) {
        return doctorService.getAllDoctorBySpecialtyAndPage(id, page);
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public BaseResponse getDoctorProfile() {
        return doctorService.getProfile();
    }

    @PreAuthorize("hasAuthority('DOCTOR')")
    @PutMapping("/doctor/update")
    public BaseResponse updateDoctorProfile(@RequestBody UpdateDoctorProfileRequest request) {
        return doctorService.updateProfile(request);
    }


    @PreAuthorize("hasAuthority('DOCTOR')")
    @GetMapping("/doctor/notification")
    public PageResponse getDoctorNotification(@RequestParam(defaultValue = "1") Integer page) {
        return bookScheduleService.getNotificationForDoctor(page);
    }

  
    @PostMapping("/doctor/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        BaseResponse response = doctorService.login(loginRequest);
        return ResponseEntity.status(response.getCode()).body(response);
    }


    @PostMapping("/doctor/reset-password")
    public BaseResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return doctorService.resetPassword(request);
    }


    @PutMapping("/doctor/reset-password")
    public BaseResponse resetPasswordChange(@Valid @RequestBody ResetPasswordChangeRequest request) {
        return doctorService.resetPasswordChange(request);
    }


    @PutMapping("/doctor/change-password")
    public BaseResponse changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return doctorService.changePassword(request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list-doctor")
    public PageResponse getAllDoctorByPage(@RequestParam(defaultValue = "1") Integer page) {
        return doctorService.getAllDoctorByPage(page);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/doctor/{id}")
    public BaseResponse getDoctorById(@PathVariable Integer id) {
        return doctorService.getDoctorById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/doctor/update/{id}")
    public BaseResponse updateDoctor(@PathVariable Integer id,
                                     @Valid @RequestBody UpdateDoctorRequest request) {
        return doctorService.updateDoctor(id, request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/doctor/create")
    public ResponseEntity<BaseResponse> createDoctor(@Valid @RequestBody UpdateDoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(request));
    }

   
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/doctor/delete/{id}")
    public BaseResponse deleteSpecialty(@PathVariable Integer id) {
        return doctorService.deleteDoctor(id);
    }
}
