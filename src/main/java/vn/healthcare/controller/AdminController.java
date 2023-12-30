package vn.healthcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.healthcare.dto.BaseResponse;
import vn.healthcare.dto.LoginRequest;
import vn.healthcare.service.AdminService;

import javax.validation.Valid;

@RestController//khai bao api
@RequestMapping("/v1")
@RequiredArgsConstructor //auto tạo constructor của class AdminController chứa field adminService có final 

public class AdminController {
    private final AdminService adminService;

    @PostMapping("/admin/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        BaseResponse response = adminService.login(loginRequest);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}
