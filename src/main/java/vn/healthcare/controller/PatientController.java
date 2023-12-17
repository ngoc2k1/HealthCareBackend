package vn.healthcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.healthcare.dto.*;
import vn.healthcare.service.BookScheduleService;
import vn.healthcare.service.PatientService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final BookScheduleService bookScheduleService;

    @PreAuthorize("hasAuthority('DOCTOR')")
    @GetMapping("/list-patient")
    public BaseResponse getAllPatientBookedByDoctor(@RequestParam(required = false) String name) {
        return patientService.getAllPatientBookedByDoctor(name);
    }
  
    @PreAuthorize("hasAuthority('PATIENT')")
    @GetMapping("/patient")
    public BaseResponse getPatientProfile() {
        return patientService.getProfile();
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @PutMapping("/patient/update")
    public BaseResponse updatePatientProfile(@RequestBody UpdatePatientRequest request) {
        return patientService.updateProfile(request);
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @GetMapping("/patient/notification")
    public BaseResponse getPatientNotification() {
        return bookScheduleService.getNotificationForPatient();
    }


    @PostMapping("/patient/register")
    public ResponseEntity<BaseResponse> register(@RequestBody PatientRegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(patientService.register(registerRequest));
    }

    @PostMapping("/patient/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        BaseResponse response = patientService.login(loginRequest);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/patient/reset-password")
    public BaseResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return patientService.resetPassword(request);
    }


    @PutMapping("/patient/reset-password")
    public BaseResponse resetPasswordChange(@Valid @RequestBody ResetPasswordChangeRequest request) {
        return patientService.resetPasswordChange(request);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/patient/change-password")
    public BaseResponse changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return patientService.changePassword(request);
    }
    @PreAuthorize("hasAuthority('DOCTOR')")
    @GetMapping("/patient/{id}")
    public BaseResponse getPatientById(@PathVariable Integer id) {
        return patientService.getPatientById(id);
    }
}
