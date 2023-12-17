package vn.healthcare.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.healthcare.config.JwtProvider;
import vn.healthcare.constant.Role;
import vn.healthcare.dto.*;
import vn.healthcare.entity.Patient;
import vn.healthcare.exception.BadRequestException;
import vn.healthcare.exception.ConflictException;
import vn.healthcare.exception.NotFoundException;
import vn.healthcare.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final ModelMapper mapper;

    public BaseResponse register(PatientRegisterRequest request) {
        if (patientRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new ConflictException("phone", "Số điện thoại đã tồn tại");
        }

        if (patientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("email", "Email đã tồn tại");
        }

        Patient patient = new Patient();
        patient.setPhone(request.getPhone());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setBirthday(request.getBirthday());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());

        patientRepository.save(patient);

        return BaseResponse.builder()
                .code(200)
                .msg("Đăng ký thành công")
                .build();
    }

    public BaseResponse resetPassword(ResetPasswordRequest request) {
        Optional<Patient> patientOptional = patientRepository.findByEmail(request.getEmail());
        if(patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            String code = RandomStringUtils.randomNumeric(8);
            patient.setResetPasswordCode(code);
            patient.setResetPasswordTimeExpire(LocalDateTime.now().plusMinutes(15));

            patientRepository.save(patient);

            emailService.sendMessageHtml(request.getEmail(), "Quên mật khẩu",
                    "reset-password", Collections.singletonMap("otp", code));


            return BaseResponse.builder()
                    .code(200)
                    .msg("Mã OTP đã được gửi vào email " + request.getEmail())
                    .build();
        }


        throw new NotFoundException("Email không tồn tại. Vui lòng nhập lại email.");
    }


    public BaseResponse resetPasswordChange(ResetPasswordChangeRequest request) {
        Optional<Patient> patientOptional = patientRepository
                .findByResetPasswordCode(request.getResetPasswordCode());

        if(patientOptional.isPresent()) {
            Patient patient = patientOptional.get();

            if(patient.getResetPasswordTimeExpire().isBefore(LocalDateTime.now())) {


                throw new BadRequestException("otp", "Mã xác nhận đã hết hạn");
            }

            patient.setPassword(passwordEncoder.encode(request.getNewPassword()));
            patient.setResetPasswordCode("");
            patient.setResetPasswordTimeExpire(null);

            patientRepository.save(patient);


            return BaseResponse.builder()
                    .code(200)
                    .msg("Đặt mật khẩu mới thành công")
                    .build();
        }


        throw new BadRequestException("Đặt mật khẩu mới thất bại");
    }


    public BaseResponse changePassword(ChangePasswordRequest request) {
        Integer patientId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Patient patient = patientRepository.findById(patientId).get();

        if (passwordEncoder.matches(request.getPassword(), patient.getPassword())) {
            if(request.getPassword().equals(request.getNewPassword())) {
                throw new BadRequestException("newPassword", "Mật khẩu mới không được giống mật khẩu hiện tại");
            }
            patient.setPassword(passwordEncoder.encode(request.getNewPassword()));

            patientRepository.save(patient);

            return BaseResponse.builder()
                    .code(200)
                    .msg("Đổi mật khẩu thành công")
                    .build();
        }


        throw new BadRequestException("pasword", "Mật khẩu không chính xác");
    }


    public BaseResponse login(LoginRequest request) {
        Optional<Patient> patientOptional = patientRepository.findByPhoneOrEmail(request.getPhoneOrEmail());

        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            if (passwordEncoder.matches(request.getPassword(), patient.getPassword())) {
                return BaseResponse.builder()
                        .code(200)
                        .msg("Đăng nhập thành công")
                        .data(new LoginResponse(jwtProvider.generateToken(patient.getId(), Role.PATIENT)))
                        .build();
            }
        }

        return BaseResponse.builder()
                .code(401)
                .msg("Vui lòng kiểm tra lại thông tin nhập")
                .build();
    }

    public BaseResponse getAllPatientBookedByDoctor(String name) {
        Integer doctorId = (Integer) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        List<Patient> patients;
        if(Objects.nonNull(name) && !name.isBlank()) {
            patients = patientRepository.findAllPatientBookedByDoctorAndByName(doctorId, name);
        } else {
            patients = patientRepository.findAllPatientBookedByDoctor(doctorId);
        }

        List<PatientResponse> data = new ArrayList<>();

        for(Patient patient : patients) {
            PatientResponse response = mapper.map(patient, PatientResponse.class);
            Integer index = patient.getBirthday().lastIndexOf("/") + 1;
            Integer age = LocalDate.now().getYear() - Integer.parseInt(patient
                    .getBirthday().substring(index));

            response.setAge(age);
            data.add(response);
        }

        return BaseResponse.builder()
                .msg("Hiển thị thành công")
                .code(200)
                .data(data)
                .build();
    }
    
    public BaseResponse getProfile() {
        Integer patientId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PatientResponse data = mapper.map(patientRepository.findById(patientId).get(), PatientResponse.class);

        Integer index = data.getBirthday().lastIndexOf("/") + 1;
        Integer age = LocalDate.now().getYear() - Integer.parseInt(data.getBirthday().substring(index));

        data.setAge(age);

        return BaseResponse.builder()
                .msg("Hiển thị thành công")
                .code(200)
                .data(data)
                .build();
    }


    public BaseResponse updateProfile(UpdatePatientRequest request) {
        Integer patientId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Patient patient = patientRepository.findById(patientId).get();

        patient.setName(request.getName());
        patient.setAvatar(request.getAvatar());
        patient.setBirthday(request.getBirthday());
        patient.setAddress(request.getAddress());
        patient.setGender(request.getGender());

        patient.setHealthInsurance(request.getHealthInsurance());
        patient.setIdentityCard(request.getIdentityCard());
        patient.setWeight(request.getWeight());
        patient.setHeight(request.getHeight());
        patient.setBloodGroup(request.getBloodGroup());

        patientRepository.save(patient);

        return BaseResponse.builder()
                .msg("Cập nhật thành công")
                .code(200)
                .build();
    }
    public BaseResponse getPatientById(Integer id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isEmpty()) {


            throw new NotFoundException("Bệnh nhân không tồn tại");
        }
        PatientResponse data = mapper.map(patientOptional.get(), PatientResponse.class);

        Integer index = data.getBirthday().lastIndexOf("/") + 1;
        Integer age = LocalDate.now().getYear() - Integer.parseInt(data.getBirthday().substring(index));

        data.setAge(age);

        return BaseResponse.builder()
                .msg("Hiển thị thành công")
                .code(200)
                .data(data)
                .build();
    }
}
