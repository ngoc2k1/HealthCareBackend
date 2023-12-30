package vn.healthcare.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vn.healthcare.constant.StatusBook;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Data//get set
@Entity
@Builder
public class BookSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tăng từ 1
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // @ManyToOne
    @OneToOne
    @JoinColumn(name = "doctor_work_schedule_id")
    private DoctorWorkSchedule doctorWorkSchedule;

    @OneToMany(mappedBy = "bookSchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //bookSchedule bị xóa thì patientHistory bị xóa theo trong database
    //k tự động query patientHistory khi query bookSchedule -> chậm performance
    private List<PatientMedicalHistory> patientMedicalHistories;

    @Enumerated(EnumType.STRING)
    private StatusBook statusBook;

    private String namePatientTest;

    @Column(columnDefinition = "text")
    private String statusHealth;

    @Column(columnDefinition = "text")
    private String qrCode;

    private LocalDateTime deleteAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
