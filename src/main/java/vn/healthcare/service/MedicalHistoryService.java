package vn.healthcare.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.healthcare.dto.*;
import vn.healthcare.entity.BookSchedule;
import vn.healthcare.entity.PatientMedicalHistory;
import vn.healthcare.exception.NotFoundException;
import vn.healthcare.repository.PatientMedicalHistoryRepository;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {
    private final PatientMedicalHistoryRepository patientMedicalHistoryRepository;
    private final ModelMapper mapper;

    public PageResponse getAllMedicalHistoryByPage(Integer page) {
        Integer patientId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<PatientMedicalHistory> historyPage = patientMedicalHistoryRepository
                .findAllByPatientId(patientId, pageable);

        List<MedicalHistoryResponse> data = new ArrayList<>();
        for (PatientMedicalHistory patientMedicalHistory : historyPage.getContent()) {
            MedicalHistoryResponse response = mapper.map(patientMedicalHistory, MedicalHistoryResponse.class);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy",
                    new Locale("vi"));
            if(Objects.nonNull(response.getRetestDate()) && !response.getRetestDate().isBlank()) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                response.setRetestDate(formatter.format(dateFormatter.parse(response.getRetestDate())));
            }
            response.getBookSchedule().setDateTest(formatter.format(patientMedicalHistory.getBookSchedule()
                    .getDoctorWorkSchedule().getWorkStartAt()));



            data.add(response);
        }

        return PageResponse.builder()
                .code(200)
                .msg("Hiển thị thành công")
                .currentPage(page)
                .perPage(10)
                .totalPage(historyPage.getTotalPages())
                .data(data)
                .build();
    }


    public PageResponse getAllMedicalHistoryForDoctorByPage(Integer page, Integer patientId) {
        Integer doctorId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<PatientMedicalHistory> historyPage = patientMedicalHistoryRepository
                .findAllByPatientIdAndDoctorId(patientId, doctorId, pageable);

        List<MedicalHistoryResponse> data = new ArrayList<>();
        for (PatientMedicalHistory patientMedicalHistory : historyPage.getContent()) {
            MedicalHistoryResponse response = mapper.map(patientMedicalHistory, MedicalHistoryResponse.class);
            response.getBookSchedule().setDoctor(null);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy",
                    new Locale("vi"));
            response.getBookSchedule().setDateTest(formatter.format(patientMedicalHistory.getBookSchedule()
                    .getDoctorWorkSchedule().getWorkStartAt()));

            if(Objects.nonNull(response.getRetestDate()) && !response.getRetestDate().isBlank()) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                response.setRetestDate(formatter.format(dateFormatter.parse(response.getRetestDate())));
            }

            data.add(response);
        }

        return PageResponse.builder()
                .code(200)
                .msg("Hiển thị thành công")
                .currentPage(page)
                .perPage(10)
                .totalPage(historyPage.getTotalPages())
                .data(data)
                .build();
    }


    public BaseResponse getDetailMedicalHistory(Integer id) {
        Optional<PatientMedicalHistory> historyOptional =
                patientMedicalHistoryRepository.findById(id);

        if (historyOptional.isEmpty()) {
            throw new NotFoundException("Không tìm thấy lịch sử khám");
        }
        MedicalHistoryResponse data = mapper.map(historyOptional.get(), MedicalHistoryResponse.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy",
                new Locale("vi"));
        data.getBookSchedule().setDateTest(formatter.format(historyOptional.get().getBookSchedule()
                .getDoctorWorkSchedule().getWorkStartAt()));

        if(Objects.nonNull(data.getRetestDate()) && !data.getRetestDate().isBlank()) {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            data.setRetestDate(formatter.format(dateFormatter.parse(data.getRetestDate())));
        }

        return BaseResponse.builder()
                .code(200)
                .msg("Hiển thị thành công")
                .data(data)
                .build();
    }

    public BaseResponse updateMedicalHistory(Integer id, UpdateMedicalHistoryRequest request) {
        Optional<PatientMedicalHistory> historyOptional =
                patientMedicalHistoryRepository.findById(id);

        if (historyOptional.isEmpty()) {
            throw new NotFoundException("Không tìm thấy lịch sử khám");
        }

        PatientMedicalHistory history = historyOptional.get();
        history.setRetestDate(request.getRetestDate());
        history.setJudgmentNote(request.getJudgmentNote());
        history.setTestResult(request.getTestResult());
        history.setPrescription(request.getPrescription());

        patientMedicalHistoryRepository.save(history);


        return BaseResponse.builder()
                .code(200)
                .msg("Cập nhật thành công")
                .build();
    }

    public BaseResponse createMedicalHistory(Integer id, CreateMedicalHistoryRequest request) {

        PatientMedicalHistory history = new PatientMedicalHistory();
        history.setRetestDate(request.getRetestDate());
        history.setBookSchedule(BookSchedule.builder().id(request.getBookScheduleId()).build());
        history.setJudgmentNote(request.getJudgmentNote());
        history.setTestResult(request.getTestResult());
        history.setPrescription(request.getPrescription());

        patientMedicalHistoryRepository.save(history);


        return BaseResponse.builder()
                .code(200)
                .msg("Cập nhật thành công")
                .build();
    }
}
