package vn.healthcare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vn.healthcare.constant.BloodGroup;
import vn.healthcare.constant.Gender;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer height;

    private Double weight;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Column(length = 15)
    private String phone;

    private String name;

    private String birthday;

    private String email;

//    private Integer age;

    private String address;

    private String resetPasswordCode;

    private LocalDateTime resetPasswordTimeExpire;

    private String avatar;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JsonIgnore
    private String password;

    @Column(length = 12)
    private String identityCard;

    // @Column(length = 15)
    private String healthInsurance;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<BookSchedule> bookSchedules;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<PatientMedicalHistory> patientMedicalHistories;

    private LocalDateTime deleteAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
