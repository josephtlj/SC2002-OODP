package src.services;

import src.daos.AppointmentOutcomeRecordDao;
import src.interfaces.AppointmentOutcomeRecordServiceInterface;
import src.models.Appointment;
import src.models.AppointmentOutcomeRecord;
import src.models.AppointmentTimeSlot;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentOutcomeRecordServices implements AppointmentOutcomeRecordServiceInterface {
    // ATTRIBUTES
    AppointmentOutcomeRecordDao appointmentOutcomeRecordDao;

    // CONSTRUCTOR
    public AppointmentOutcomeRecordServices(String patientID) {
        appointmentOutcomeRecordDao = new AppointmentOutcomeRecordDao(patientID);
    }

    
    /** 
     * @param patientID
     * @param date
     * @param timeSlot
     * @return AppointmentOutcomeRecord
     */
    // SERVICES
    public AppointmentOutcomeRecord findAppointmentOutcomeRecord(String patientID, LocalDate date,
            AppointmentTimeSlot timeSlot) {
        List<Appointment> completedAppointments = appointmentOutcomeRecordDao.getCompletedAppointmentsByPatientId(patientID);

        Optional<Appointment> appointmentOptional = completedAppointments.stream()
                .filter(a -> a.getAppointmentDate().equals(date)
                        && a.getAppointmentTimeSlot().getStartTime().equals(timeSlot.getStartTime())
                        && a.getAppointmentTimeSlot().getEndTime().equals(timeSlot.getEndTime())
                        && a.getPatientID().equals(patientID))
                .findFirst();

        if (appointmentOptional.isEmpty()) {
            return null;
        } else {
            return appointmentOutcomeRecordDao.findAppointmentOutcomeRecord(patientID, date, timeSlot);
        }

    }

    
    /** 
     * @param appointmentOutcomeRecord
     * @return boolean
     */
    public boolean updateAppointmentOutcomeRecord(AppointmentOutcomeRecord appointmentOutcomeRecord) {
        // CHECKING VALIDITY NOT NECESSARY AS findAppointmentOutcomeRecord() HAS ALREADY
        // DONE IT

        return appointmentOutcomeRecordDao.updateAppointmentOutcomeRecord(appointmentOutcomeRecord);
    }

    
    /** 
     * @param doctorID
     * @return List<Appointment>
     */
    public List<Appointment> getCompletedAppointments(String doctorID) {
        // VALIDITY OF DOCTORID NOT NECESSARY AS IT IS VALIDATED DURING LOGIN
        return appointmentOutcomeRecordDao.getCompletedAppointments(doctorID);
    }

    public List<Appointment> getCompletedAppointmentsInMonth(int month, List<Appointment> appointments) {

        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    public List<Appointment> getCompletedAppointmentsInDay(LocalDate date, List<Appointment> appointments) {
        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentDate().isEqual(date))
                .collect(Collectors.toList());
    }

    public boolean checkDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (date.isBefore(LocalDate.parse("16/11/2024", formatter))) {
            return true;
        } else {
            return false;
        }
    }

    // public List<AppointmentOutcomeRecord> readAllAppointmentOutcomeRecordsByDay(LocalDate date){
    //     return appointmentOutcomeRecordDao.getAllAppointmentOutcomeRecordsByDay(date);
    // }
}