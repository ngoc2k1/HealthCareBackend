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
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DoctorWorkSchedule> doctorWorkSchedules;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookSchedule> bookSchedules;

    @Column(length = 15)
    private String phone;

    private String name;

    private String birthday;

    private String addressTest;

    private String email;

    private String resetPasswordCode;

    private LocalDateTime resetPasswordTimeExpire;

    private String avatar;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JsonIgnore
    private String password;

    private String identityCard;

    @Column(length = 15)
    private String healthInsurance;

    private LocalDateTime deleteAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
