package src.interfaces;

import java.time.LocalDate;
import java.util.List;

import src.models.Appointment;
import src.models.AppointmentTimeSlot;

public interface DoctorAppointmentServicesInterface 
{
    List<Appointment> getAllAppointments(String doctorID);

    boolean updateAppointmentStatus(String status, LocalDate date, AppointmentTimeSlot timeSlot, String patientID, String doctorID);

    boolean updateAppointmentAvailability(LocalDate date, int availability);

    public boolean isInCurrentYear(LocalDate date);
}