package uz.pdp.learningcenterapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class TimeTableStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private TimeTable timeTable;
    @ManyToOne
    private User student;
    private Integer paid;
    @OneToMany(mappedBy = "timeTableStudent", fetch = FetchType.EAGER)
    private List<StudentAttendance> studentAttendances;
}
