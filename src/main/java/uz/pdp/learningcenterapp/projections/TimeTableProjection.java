package uz.pdp.learningcenterapp.projections;

public interface TimeTableProjection {
    Integer getId();
    String getFullName();
    Boolean[] getAttendances();
}
