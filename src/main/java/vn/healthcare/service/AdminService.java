package vn.healthcare.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.healthcare.config.JwtProvider;
import vn.healthcare.constant.Role;
import vn.healthcare.dto.BaseResponse;
import vn.healthcare.dto.LoginRequest;
import vn.healthcare.dto.LoginResponse;
import vn.healthcare.entity.Admin;
import vn.healthcare.entity.Patient;
import vn.healthcare.repository.AdminRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    public BaseResponse login(LoginRequest request) {
        Optional<Admin> adminOptional = adminRepository.findByPhoneOrEmail(request.getPhoneOrEmail());

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            if (passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                return BaseResponse.builder()
                        .code(200)
                        .msg("Đăng nhập thành công")
                        .data(new LoginResponse(jwtProvider.generateToken(admin.getId(), Role.ADMIN)))
                        .build();
            }
        }

        return BaseResponse.builder()
                .code(401)
                .msg("Vui lòng kiểm tra lại thông tin nhập")
                .build();
    }
}
