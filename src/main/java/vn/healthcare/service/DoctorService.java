package vn.healthcare.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.healthcare.config.JwtProvider;
import vn.healthcare.constant.Role;
import vn.healthcare.dto.*;
import vn.healthcare.entity.Doctor;
import vn.healthcare.entity.Specialty;
import vn.healthcare.exception.BadRequestException;
import vn.healthcare.exception.ConflictException;
import vn.healthcare.exception.NotFoundException;
import vn.healthcare.repository.DoctorRepository;
import vn.healthcare.repository.SpecialtyRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;//tim cac instance trong IOC.-> roi ms tao cac instance trong Service nay 
    private final ModelMapper mapper;//newDoctor thi can truyen cac final Nhung co @Service roi, nen chi can goi ra
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final SpecialtyRepository specialtyRepository;

    public PageResponse getAllDoctorBySpecialtyAndPage(Integer specialtyId, Integer page) {
        Optional<Specialty> specialtyOptional = specialtyRepository.findById(specialtyId);
        if (specialtyOptional.isEmpty()) {
            throw new NotFoundException("Chuyên môn không tồn tại");
        }

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<Doctor> doctorPage = doctorRepository.findAllBySpecialtyId(specialtyId, pageable);

        List<DoctorResponse> data = new ArrayList<>();

        for (Doctor doctor : doctorPage.getContent()) {
            data.add(mapper.map(doctor, DoctorResponse.class));
        }

        return PageResponse.builder()
                .code(200)
                .msg("Hiển thị danh sách thành công")
                .currentPage(page)
                .perPage(10)
                .totalPage(doctorPage.getTotalPages())
                .data(data)
                .build();
    }

    public BaseResponse login(LoginRequest request) {
        Optional<Doctor> doctorOptional = doctorRepository.findByPhoneOrEmail(request.getPhoneOrEmail());

        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            if (passwordEncoder.matches(request.getPassword(), doctor.getPassword())) {
                return BaseResponse.builder()
                        .code(200)
                        .msg("Đăng nhập thành công")
                        .data(new LoginResponse(jwtProvider.generateToken(doctor.getId(), Role.DOCTOR)))
                        .build();
            }
        }

        return BaseResponse.builder()
                .code(401)
                .msg("Vui lòng kiểm tra lại thông tin nhập")
                .build();
    }


    public BaseResponse resetPassword(ResetPasswordRequest request) {
        Optional<Doctor> doctorOptional = doctorRepository.findByEmail(request.getEmail());
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            String code = RandomStringUtils.randomNumeric(8);
            doctor.setResetPasswordTimeExpire(LocalDateTime.now().plusMinutes(15));
            doctor.setResetPasswordCode(code);

            doctorRepository.save(doctor);

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
        Optional<Doctor> doctorOptional = doctorRepository
                .findByResetPasswordCode(request.getResetPasswordCode());

        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();

            if (doctor.getResetPasswordTimeExpire().isBefore(LocalDateTime.now())) {

                throw new BadRequestException("otp", "Mã xác nhận đã hết hạn");
            }

            doctor.setResetPasswordCode("");
            doctor.setPassword(passwordEncoder.encode(request.getNewPassword()));
            doctor.setResetPasswordTimeExpire(null);

            doctorRepository.save(doctor);


            return BaseResponse.builder()
                    .code(200)
                    .msg("Đặt mật khẩu mới thành công")
                    .build();
        }

        throw new BadRequestException("Đặt mật khẩu mới thất bại");
    }

    public BaseResponse changePassword(ChangePasswordRequest request) {
        Integer doctorId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Doctor doctor = doctorRepository.findById(doctorId).get();

        if (passwordEncoder.matches(request.getPassword(), doctor.getPassword())) {
            if(request.getPassword().equals(request.getNewPassword())) {
                throw new BadRequestException("newPassword", "Mật khẩu mới không được giống mật khẩu hiện tại");
            }
            doctor.setPassword(passwordEncoder.encode(request.getNewPassword()));
            doctorRepository.save(doctor);

            return BaseResponse.builder()
                    .code(200)
                    .msg("Đổi mật khẩu thành công")
                    .build();
        }

        throw new BadRequestException("pasword", "Mật khẩu hiện tại không chính xác");
    }


    public BaseResponse getProfile() {
        Integer doctorId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        DoctorResponse data = mapper.map(doctorRepository.findById(doctorId).get(), DoctorResponse.class);

        Integer index = data.getBirthday().lastIndexOf("/") + 1;
        Integer age = LocalDate.now().getYear() - Integer.parseInt(data.getBirthday().substring(index));

        data.setAge(age);

        return BaseResponse.builder()
                .msg("Hiển thị thành công")
                .code(200)
                .data(data)
                .build();
    }


    public BaseResponse updateProfile(UpdateDoctorProfileRequest request) {
        Integer doctorId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Doctor doctor = doctorRepository.findById(doctorId).get();

        doctor.setName(request.getName());
        doctor.setBirthday(request.getBirthday());
        doctor.setAddressTest(request.getAddressTest());
        doctor.setAvatar(request.getAvatar());
        doctor.setGender(request.getGender());

        doctor.setHealthInsurance(request.getHealthInsurance());
        doctor.setIdentityCard(request.getIdentityCard());

        doctorRepository.save(doctor);

        return BaseResponse.builder()
                .msg("Cập nhật thành công")
                .code(200)
                .build();
    }

    public PageResponse getAllDoctorByPage(Integer page) {
        List<DoctorResponse> data = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);

        for (Doctor doctor : doctorPage.getContent()) {
            DoctorResponse response = mapper.map(doctor, DoctorResponse.class);

            Integer index = doctor.getBirthday().lastIndexOf("/") + 1;
            Integer age = LocalDate.now().getYear() - Integer.parseInt(doctor.getBirthday().substring(index));

            response.setAge(age);
            data.add(response);
        }

        return PageResponse.builder()
                .code(200)
                .msg("Hiển thị danh sách thành công")
                .currentPage(page)
                .perPage(10)
                .totalPage(doctorPage.getTotalPages())
                .data(data)
                .build();
    }

    public BaseResponse updateDoctor(Integer id, UpdateDoctorRequest request) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isEmpty()) {


            throw new NotFoundException("Bác sĩ không tồn tại");
        }

        Optional<Doctor> doctorOptionalByEmail = doctorRepository.findByEmail(request.getEmail());
        if (doctorOptionalByEmail.isPresent() &&
                !doctorOptionalByEmail.get().getId().equals(id)) {


            throw new ConflictException("email", "Email đã tồn tại");
        }

        Optional<Doctor> doctorOptionalByPhone = doctorRepository.findByPhone(request.getPhone());
        if (doctorOptionalByPhone.isPresent() &&
                !doctorOptionalByPhone.get().getId().equals(id)) {


            throw new ConflictException("phone", "Số điện thoại đã tồn tại");
        }

        Doctor doctor = doctorOptional.get();

        if (Objects.nonNull(request.getPassword()) && !request.getPassword().isBlank()) {
            doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        doctor.setName(request.getName());
        doctor.setPhone(request.getPhone());
        doctor.setBirthday(request.getBirthday());
        doctor.setAddressTest(request.getAddressTest());
        doctor.setEmail(request.getEmail());
        doctor.setAvatar(request.getAvatar());
        doctor.setGender(request.getGender());
        doctor.setHealthInsurance(request.getHealthInsurance());
        doctor.setIdentityCard(request.getIdentityCard());
        doctor.setSpecialty(Specialty.builder().id(request.getSpecialtyId()).build());

        doctorRepository.save(doctor);

        return BaseResponse.builder()
                .msg("Cập nhật thành công")
                .code(200)
                .build();
    }

    public BaseResponse createDoctor(UpdateDoctorRequest request) {
        Optional<Doctor> doctorOptionalByPhone = doctorRepository.findByPhone(request.getPhone());
        if (doctorOptionalByPhone.isPresent()) {


            throw new ConflictException("phone", "Số điện thoại đã tồn tại");
        }

        Optional<Doctor> doctorOptionalByEmail = doctorRepository.findByEmail(request.getEmail());
        if (doctorOptionalByEmail.isPresent()) {


            throw new ConflictException("email", "Email đã tồn tại");
        }
        Doctor doctor = new Doctor();

        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setPhone(request.getPhone());
        doctor.setName(request.getName());
        doctor.setBirthday(request.getBirthday());
        doctor.setAddressTest(request.getAddressTest());
        doctor.setHealthInsurance(request.getHealthInsurance());
        doctor.setEmail(request.getEmail());
        doctor.setAvatar(request.getAvatar());
        doctor.setGender(request.getGender());
        doctor.setIdentityCard(request.getIdentityCard());
        doctor.setSpecialty(Specialty.builder().id(request.getSpecialtyId()).build());

        doctorRepository.save(doctor);

        return BaseResponse.builder()
                .msg("Tạo mới thành công")
                .code(201)
                .build();
    }

    public BaseResponse getDoctorById(Integer id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isEmpty()) {


            throw new NotFoundException("Bác sĩ không tồn tại");
        }

        DoctorResponse data = mapper.map(doctorOptional.get(), DoctorResponse.class);

        Integer index = data.getBirthday().lastIndexOf("/") + 1;
        Integer age = LocalDate.now().getYear() - Integer.parseInt(data.getBirthday().substring(index));

        data.setAge(age);

        return BaseResponse.builder()
                .msg("Hiển thị thành công")
                .code(200)
                .data(data)
                .build();
    }


    public BaseResponse deleteDoctor(Integer id) {

        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isEmpty() ) {

            throw new NotFoundException("Bác sĩ không tồn tại");
        }

        doctorRepository.deleteById(id);


        return BaseResponse.builder()
                .code(200)
                .msg("Xóa thành công")
                .build();
    }
}
