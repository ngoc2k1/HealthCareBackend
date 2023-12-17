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
@Builder//tao 1 bien -> ....Builder Pattern
public class BookSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tu tang tu 1
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "doctor_work_schedule_id")
    private DoctorWorkSchedule doctorWorkSchedule;

    @OneToMany(mappedBy = "bookSchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)//patientHis bi xoa theo
    private List<PatientMedicalHistory> patientMedicalHistories;//k tu dong query Histo khi query Book -> cham performance

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
