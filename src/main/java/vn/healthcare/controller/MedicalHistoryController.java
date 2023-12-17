package vn.healthcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.healthcare.dto.BaseResponse;
import vn.healthcare.dto.PageResponse;
import vn.healthcare.dto.UpdateMedicalHistoryRequest;
import vn.healthcare.service.MedicalHistoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class MedicalHistoryController {
    private final MedicalHistoryService medicalHistoryService;

    //hiển thị danh sách lịch sử khám bệnh của bệnh nhân
    @PreAuthorize("hasAuthority('PATIENT')")
    @GetMapping("/list-medical-history-by-patient")
    public PageResponse getListMedicalHistory(@RequestParam(defaultValue = "1") Integer page) {
        return medicalHistoryService.getAllMedicalHistoryByPage(page);
    }

    //hiển thị danh sách lịch sử khám bệnh của bệnh nhân mà bác sĩ này khám

    @PreAuthorize("hasAuthority('DOCTOR')")
    @GetMapping("/list-medical-history-by-doctor/{patientId}")
    public PageResponse getListMedicalHistoryForDoctor(@RequestParam(defaultValue = "1") Integer page,
                                                       @PathVariable Integer patientId) {
        return medicalHistoryService.getAllMedicalHistoryForDoctorByPage(page, patientId);
    }

    //hiển thị chi tiết 1 lịch sử khám bệnh khi BN/BS chọn 1 item trong list
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/medical-history/detail/{id}")
    public BaseResponse getDetailMedicalHistory(@PathVariable Integer id) {
        return medicalHistoryService.getDetailMedicalHistory(id);
    }

    //chỉ bác sĩ có quyền sửa thông tin trong lịch sử khám bệnh (lskb)
    //id của lskb
    @PreAuthorize("hasAuthority('DOCTOR')")
    @PutMapping("/medical-history/update/{id}")
    public BaseResponse updateMedicalHistory(@PathVariable Integer id,
                                             @RequestBody UpdateMedicalHistoryRequest request) {
        return medicalHistoryService.updateMedicalHistory(id, request);
    }
}
