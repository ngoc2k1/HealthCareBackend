package vn.healthcare.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.healthcare.dto.BaseResponse;
import vn.healthcare.dto.SpecialtyRequest;
import vn.healthcare.dto.SpecialtyResponse;
import vn.healthcare.entity.Specialty;
import vn.healthcare.exception.ConflictException;
import vn.healthcare.exception.NotFoundException;
import vn.healthcare.repository.SpecialtyRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final ModelMapper mapper;

    public BaseResponse getAllSpecialty() {
        return BaseResponse.builder()
                .code(200)
                .msg("Hiển thị danh sách thành công")
                .data(specialtyRepository.findAll())
                .build();
    }

    public BaseResponse getSpecialtyById(Integer id) {
        Optional<Specialty> optionalSpecialty = specialtyRepository.findById(id);
        if (optionalSpecialty.isEmpty() ) {
            throw new NotFoundException("Không tìm thấy chuyên môn");
        }

        return BaseResponse.builder()
                .code(200)
                .msg("Hiển thị thành công")
                .data(mapper.map(optionalSpecialty.get(), SpecialtyResponse.class))
                .build();
    }

    public BaseResponse createSpecialty(SpecialtyRequest request) {
        if (specialtyRepository.findByName(request.getName()).isPresent()) {

            throw new ConflictException("name", "Tên đã tồn tại");
        }
        Specialty specialty = Specialty.builder()
                .name(request.getName())
                .image(request.getImage())
                .build();

        specialtyRepository.save(specialty);

        return BaseResponse.builder()
                .code(201)
                .msg("Tạo thành công")
                .build();
    }

    public BaseResponse updateSpecialty(Integer id, SpecialtyRequest request) {
        Optional<Specialty> optionalSpecialtyByName = specialtyRepository.findByName(request.getName());
        if (optionalSpecialtyByName.isPresent() &&
                !optionalSpecialtyByName.get().getId().equals(id)) {

            throw new ConflictException("name", "Tên đã tồn tại");
        }

        Optional<Specialty> optionalSpecialtyById = specialtyRepository.findById(id);
        if (optionalSpecialtyById.isEmpty() ) {

            throw new NotFoundException("Chuyên môn không tồn tại");
        }

        Specialty specialty = optionalSpecialtyById.get();
        specialty.setImage(request.getImage());
        specialty.setName(request.getName());

        specialtyRepository.save(specialty);

        return BaseResponse.builder()
                .code(200)
                .msg("Cập nhật thành công")
                .build();
    }

    public BaseResponse deleteSpecialty(Integer id) {

        Optional<Specialty> optionalSpecialtyById = specialtyRepository.findById(id);
        if (optionalSpecialtyById.isEmpty() ) {

            throw new NotFoundException("Chuyên môn không tồn tại");
        }

        specialtyRepository.deleteById(id);


        return BaseResponse.builder()
                .code(200)
                .msg("Xóa thành công")
                .build();
    }
}
