package vn.healthcare.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.healthcare.constant.StatusBook;
import vn.healthcare.dto.BaseResponse;
import vn.healthcare.dto.EditScheduleRequest;
import vn.healthcare.dto.TimeByDoctorResponse;
import vn.healthcare.entity.BookSchedule;
import vn.healthcare.entity.Doctor;
import vn.healthcare.entity.DoctorWorkSchedule;
import vn.healthcare.exception.NotFoundException;
import vn.healthcare.repository.DoctorRepository;
import vn.healthcare.repository.DoctorWorkScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorWorkScheduleService {
    private final DoctorWorkScheduleRepository doctorWorkScheduleRepository;
    private final DoctorRepository doctorRepository;

    public BaseResponse getAllDateByDoctor(Integer doctorId) {
        List<String> data = doctorWorkScheduleRepository
                .findAllDateByDoctor(doctorId, LocalDateTime.now());

        return BaseResponse.builder()
                .code(200)
                .msg("Hiển thị danh sách thành công")
                .data(data.stream().map(date -> date.replace("-", "/")))
                .build();
    }

    public BaseResponse getAllTimeByDoctorAndDate(Integer doctorId, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        LocalDateTime start = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(localDate, LocalTime.MAX);

        List<TimeByDoctorResponse> data = new ArrayList<>();
        List<DoctorWorkSchedule> doctorWorkSchedules = doctorWorkScheduleRepository
                .findAllByDoctor(doctorId, start, end);

        for (DoctorWorkSchedule doctorWorkSchedule : doctorWorkSchedules) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String value = timeFormatter.format(doctorWorkSchedule.getWorkStartAt()) + " - "
                    + timeFormatter.format(doctorWorkSchedule.getWorkEndAt());


            Boolean isBooked = false;

            BookSchedule bookSchedule = doctorWorkSchedule.getBookSchedule();
            if (Objects.nonNull(bookSchedule) &&
                    (bookSchedule.getStatusBook().equals(StatusBook.CHUA_KHAM) ||
                            bookSchedule.getStatusBook().equals(StatusBook.DA_KHAM))) {
                isBooked = true;
            }


            TimeByDoctorResponse response = TimeByDoctorResponse.builder()
                    .id(doctorWorkSchedule.getId())
                    .value(value)
                    .price(doctorWorkSchedule.getPrice())
                    .isBooked(isBooked)
                    .build();

            data.add(response);
        }

        return BaseResponse.builder()
                .code(200)
                .msg("Hiển thị danh sách thành công")
                .data(data)
                .build();
    }

    @Transactional
    public BaseResponse editSchedule(EditScheduleRequest request) {
        List<DoctorWorkSchedule> doctorWorkSchedules = new ArrayList<>();
        String dateString = request.getDate();



        Optional<Doctor> doctorOptional = doctorRepository.findById(request.getDoctorId());
        if(doctorOptional.isEmpty()) {

            throw new NotFoundException("Bác sĩ không tồn tại");
        }


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(dateString, dateFormatter);
        LocalDateTime max = LocalDateTime.of(localDate, LocalTime.MAX);
        LocalDateTime min = LocalDateTime.of(localDate, LocalTime.MIN);

        List<DoctorWorkSchedule> savedList = doctorWorkScheduleRepository
                .findAllByDoctor(request.getDoctorId(), min, max);


        List<Integer> ids = new ArrayList<>();

        for(EditScheduleRequest.Time item : request.getTimes()) {
            String start = item.getStart();
            String end = item.getEnd();
            String timeStartString = dateString + " " + start;
            String timeEndString = dateString + " " + end;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime timeStart = LocalDateTime.parse(timeStartString, formatter);
            LocalDateTime timeEnd = LocalDateTime.parse(timeEndString, formatter);

            DoctorWorkSchedule doctorWorkSchedule = DoctorWorkSchedule.builder()
                    .id(item.getId())
                    .doctor(Doctor.builder().id(request.getDoctorId()).build())
                    .price(item.getPrice())
                    .workStartAt(timeStart)
                    .workEndAt(timeEnd)
                    .build();
            if(Objects.nonNull(item.getId())) {
                ids.add(item.getId());
            }
            doctorWorkSchedules.add(doctorWorkSchedule);
        }
        List<Integer> deleteIds = new ArrayList<>();
        savedList.forEach(item -> {
            if(!ids.contains(item.getId())) {
                deleteIds.add(item.getId());
            }
        });

        doctorWorkScheduleRepository.deleteAllById(deleteIds);
        doctorWorkScheduleRepository.saveAll(doctorWorkSchedules);

        return BaseResponse.builder()
                .code(200)
                .msg("Chỉnh sửa thành công thành công")
                .build();
    }
}
