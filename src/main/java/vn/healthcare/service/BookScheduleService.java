package vn.healthcare.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.healthcare.constant.StatusBook;
import vn.healthcare.dto.*;
import vn.healthcare.entity.*;
import vn.healthcare.exception.BadRequestException;
import vn.healthcare.exception.ConflictException;
import vn.healthcare.exception.NotFoundException;
import vn.healthcare.repository.BookScheduleRepository;
import vn.healthcare.repository.DoctorWorkScheduleRepository;
import vn.healthcare.repository.PatientMedicalHistoryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class BookScheduleService {
    private final BookScheduleRepository bookScheduleRepository;
    private final ModelMapper mapper;
    private final DoctorWorkScheduleRepository doctorWorkScheduleRepository;
    private final PatientMedicalHistoryRepository patientMedicalHistoryRepository;
    public PageResponse getAllScheduleByDoctor(Integer page) {
        Integer doctorId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<BookScheduleResponse> data = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("doctorWorkSchedule.workStartAt"));

        Page<BookSchedule> bookSchedulePage = bookScheduleRepository.findAllByDoctorId(doctorId, pageable);
        for (BookSchedule bookSchedule : bookSchedulePage.getContent()) {
            BookScheduleResponse response = mapper.map(bookSchedule, BookScheduleResponse.class);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String timeTest = timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt()) + " - "
                    + timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkEndAt());
            response.setTimeTest(timeTest);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateTest = dateFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt());
            response.setDateTest(dateTest);

            Integer index = bookSchedule.getPatient().getBirthday().lastIndexOf("/") + 1;
            Integer age = LocalDate.now().getYear() - Integer.parseInt(bookSchedule.getPatient()
                    .getBirthday().substring(index));
            response.getPatient().setAge(age);

            data.add(response);
        }

        return PageResponse.builder()
                .msg("Hiển thị thành công")
                .code(200)
                .data(data)
                .totalPage(bookSchedulePage.getTotalPages())
                .perPage(10)
                .currentPage(page)
                .build();
    }
    
     public BaseResponse cancelSchedule(Integer scheduleId) {
        Integer patientId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<BookSchedule> bookScheduleOptional =
                bookScheduleRepository.findByIdAndPatientId(scheduleId, patientId);
        if (bookScheduleOptional.isEmpty()) {

            throw new NotFoundException("Không tìm thấy lịch khám");
        }


        BookSchedule bookSchedule = bookScheduleOptional.get();
        if (!bookSchedule.getStatusBook().equals(StatusBook.CHUA_KHAM)) {

            throw new BadRequestException("Lịch đã khám hoặc đã hủy. Không thể hủy");
        }
        // bookSchedule.setStatusBook(StatusBook.DA_HUY);
        bookScheduleRepository.deleteById(bookScheduleOptional.get().getId());

        return BaseResponse.builder()
                .msg("Hủy lịch thành công")
                .code(200)
                .build();
    }


    public PageResponse getAllScheduleByPatient(Integer page) {
        Integer patientId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<BookScheduleByPatientResponse> data = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("doctorWorkSchedule.workStartAt"));

        Page<BookSchedule> bookSchedulePage = bookScheduleRepository.findAllByPatientId(patientId, pageable);
        
        for (BookSchedule bookSchedule : bookSchedulePage.getContent()) {
            BookScheduleByPatientResponse response = mapper.map(bookSchedule, BookScheduleByPatientResponse.class);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String timeTest = timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt()) + " - "
                    + timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkEndAt());
            response.setTimeTest(timeTest);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateTest = dateFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt());
            response.setDateTest(dateTest);


            Integer index = bookSchedule.getPatient().getBirthday().lastIndexOf("/") + 1;
            Integer age = LocalDate.now().getYear() - Integer.parseInt(bookSchedule.getPatient()
                    .getBirthday().substring(index));
            response.getPatient().setAge(age);

            response.setPrice(bookSchedule.getDoctorWorkSchedule().getPrice());

            data.add(response);
        }
       return PageResponse.builder()
                .msg("Hiển thị thành công")
                .code(200)
                .data(data)
                .totalPage(bookSchedulePage.getTotalPages())
                .perPage(10)
                .currentPage(page)
                .build();
    }

    public BaseResponse getBookScheduleDetail(Integer id) {
        Optional<BookSchedule> bookScheduleOptional = bookScheduleRepository.findById(id);
        if (bookScheduleOptional.isEmpty()) {
            throw new NotFoundException("Không tìm thấy lịch khám");
        }

        BookSchedule bookSchedule = bookScheduleOptional.get();

        BookScheduleByPatientResponse response = mapper.map(bookSchedule, BookScheduleByPatientResponse.class);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String timeTest = timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt()) + " - "
                + timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkEndAt());
        response.setTimeTest(timeTest);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateTest = dateFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt());
        response.setDateTest(dateTest);
        response.setPrice(bookSchedule.getDoctorWorkSchedule().getPrice());


        Integer index = bookSchedule.getPatient().getBirthday().lastIndexOf("/") + 1;
        Integer age = LocalDate.now().getYear() - Integer.parseInt(bookSchedule.getPatient()
                .getBirthday().substring(index));

        response.getPatient().setAge(age);

        return BaseResponse.builder()
                .msg("Hiển thị thành công")
                .code(200)
                .data(response)
                .build();
    }

    public BaseResponse confirm(Integer id) {
        Integer doctorId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<BookSchedule> bookScheduleOptional = bookScheduleRepository.findByIdAndDoctorId(id, doctorId);
        if (bookScheduleOptional.isEmpty()) {
            throw new NotFoundException("Không tìm thấy lịch khám");
        }

        BookSchedule bookSchedule = bookScheduleOptional.get();
        bookSchedule.setStatusBook(StatusBook.DA_KHAM);
        bookScheduleRepository.save(bookSchedule);

        return BaseResponse.builder()
                .msg("Cập nhật thành công")
                .code(200)
                .build();
    }

    public BaseResponse updateSchedule(Integer id, UpdateScheduleRequest request) {
        Optional<BookSchedule> bookScheduleOptional = bookScheduleRepository.findById(id);
        if (bookScheduleOptional.isEmpty()) {
            throw new NotFoundException("Không tìm thấy lịch khám");
        }


        BookSchedule bookSchedule = bookScheduleOptional.get();
        if (!bookSchedule.getStatusBook().equals(StatusBook.CHUA_KHAM)) {

            throw new BadRequestException("Lịch đã khám hoặc đã hủy. Không thể hủy");
        }


        bookSchedule.setNamePatientTest(request.getNamePatientTest());
        bookSchedule.setQrCode(request.getQrcode());
        bookSchedule.setDoctorWorkSchedule(DoctorWorkSchedule.builder()
                .id(request.getDoctorWorkScheduleId())
                .build());
        bookSchedule.setStatusHealth(request.getStatusHealth());
        bookScheduleRepository.save(bookSchedule);

        return BaseResponse.builder()
                .msg("Cập nhật thành công")
                .code(200)
                .build();
    }

@Transactional
    public BaseResponse createSchedule(CreateScheduleRequest request) {
        Integer patientId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<BookSchedule> bookScheduleOptional =
                bookScheduleRepository.findByDoctorWorkScheduleId(request.getDoctorWorkScheduleId());
                

        DoctorWorkSchedule doctorWorkSchedule = doctorWorkScheduleRepository
                .findById(request.getDoctorWorkScheduleId())
                .get();

        BookSchedule bookSchedule = new BookSchedule();
        bookSchedule.setNamePatientTest(request.getNamePatientTest());
        bookSchedule.setQrCode(request.getQrcode());
        bookSchedule.setStatusHealth(request.getStatusHealth());
        bookSchedule.setDoctorWorkSchedule(DoctorWorkSchedule.builder()
                .id(request.getDoctorWorkScheduleId())
                .build());
        bookSchedule.setDoctor(Doctor.builder().id(doctorWorkSchedule.getDoctor().getId()).build());
        bookSchedule.setPatient(Patient.builder().id(patientId).build());
        bookSchedule.setStatusBook(StatusBook.CHUA_KHAM);


        BookSchedule saved = bookScheduleRepository.save(bookSchedule);

        PatientMedicalHistory history = new PatientMedicalHistory();
        history.setPatient(Patient.builder().id(patientId).build());
        history.setBookSchedule(saved);

        patientMedicalHistoryRepository.save(history);

        return BaseResponse.builder()
                .msg("Tạo thành công")
                .code(200)
                .build();
    }

    public BaseResponse getNotificationForPatient() {
        Integer patientId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        List<BookSchedule> bookSchedules = bookScheduleRepository
                .findAllByPatientIdAndStatusAndBefore8Hour(patientId, StatusBook.CHUA_KHAM);

        List<PatientNotificationResponse> data = new ArrayList<>();
        for (BookSchedule bookSchedule : bookSchedules) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateTest = dateFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt());

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String timeTest = timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt()) + " - "
                    + timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkEndAt());

            PatientNotificationResponse response = new PatientNotificationResponse();

            response.setBookSchedule(new PatientNotificationResponse.BookScheduleResponse(bookSchedule.getId(),
                    dateTest, timeTest));


            response.setDoctor(new PatientNotificationResponse.DoctorResponse(bookSchedule.getDoctor().getId(),
                    bookSchedule.getDoctor().getName(),
                    new PatientNotificationResponse.DoctorResponse.Specialty(bookSchedule
                            .getDoctor().getSpecialty().getName())));

            long hours = ChronoUnit.HOURS.between(LocalDateTime.now(),
                    bookSchedule.getDoctorWorkSchedule().getWorkStartAt());

            response.setContent("Bạn có 1 lịch khám sau " + hours + " giờ nữa");

            data.add(response);
        }

        return BaseResponse.builder()
                .msg("Hiển thị danh sách thành công")
                .code(200)
                .data(data)
                .build();
    }

    public PageResponse getNotificationForDoctor(Integer page) {
        Integer doctorId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        Page<BookSchedule> bookSchedulePage = bookScheduleRepository
                .findAllByDoctorIdAndStatusAndBefore8Hour(doctorId,
                        List.of(StatusBook.CHUA_KHAM, StatusBook.DA_HUY), pageable);

        List<DoctorNotificationResponse> data = new ArrayList<>();
        for (BookSchedule bookSchedule : bookSchedulePage.getContent()) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String timeTest = timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt()) + " - "
                    + timeFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkEndAt());

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateTest = dateFormatter.format(bookSchedule.getDoctorWorkSchedule().getWorkStartAt());


            DoctorNotificationResponse response = new DoctorNotificationResponse();

            Integer index = bookSchedule.getPatient().getBirthday().lastIndexOf("/") + 1;
            Integer age = LocalDate.now().getYear() - Integer.parseInt(bookSchedule.getPatient()
                    .getBirthday().substring(index));

            response.setBookSchedule(response.new BookScheduleResponse(bookSchedule.getId(),
                    dateTest, timeTest, bookSchedule.getStatusBook()));
            response.setPatient(response.new PatientResponse(bookSchedule.getPatient().getId(),
                    bookSchedule.getPatient().getName(),
                    age,
                    bookSchedule.getPatient().getGender()));

            if(bookSchedule.getStatusBook().equals(StatusBook.CHUA_KHAM)) {
                response.setContent("1 bệnh nhân vừa đặt lịch khám");
            } else if(bookSchedule.getStatusBook().equals(StatusBook.DA_HUY)) {
                response.setContent("1 bệnh nhân vừa hủy lịch khám");
            }
            data.add(response);
        }

        return PageResponse.builder()
                .msg("Hiển thị danh sách thành công")
                .code(200)
                .currentPage(page)
                .perPage(10)
                .totalPage(bookSchedulePage.getTotalPages())
                .data(data)
                .build();
    }
}
