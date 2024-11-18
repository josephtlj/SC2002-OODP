package models;

import java.util.List;
import daos.DoctorDao;
import views.AppointmentOutcomeRecordView;
import views.DiagnosisTreatmentRecordView;
import views.DoctorAppointmentView;
import views.DoctorChangePasswordView;
import views.DoctorMedicalRecordView;
import Enum.DoctorAppointmentActionType;
import Enum.DoctorDepartment;
import Enum.DoctorMedicalRecordActionType;

public class Doctor extends User
{
    //ATTRIBUTES
    private Calendar doctorCalendar;
    private DoctorDepartment department;
    private DoctorDao doctorDao;
    
    //CONSTRUCTOR
    public Doctor(String ID, String name, String department,String password, boolean IsFirstLogin, byte[] salt) 
    {
        super(ID, password, Role.valueOf("Doctor"),salt, IsFirstLogin); 
        this.department= DoctorDepartment.valueOf(department); 
        this.doctorCalendar= new Calendar(ID);
    }

    //GET METHODS
    public String getDepartment()
    {
        return this.department.name();
    }

    //RETRIEVE ALL DOCTORS FROM DATABASE
    public List<Doctor> getAllDoctors()
    {
        return doctorDao.getAllDoctors();
    }

    //UPDATE DOCTOR'S PASSWORD
    public void updateDoctorPassword()
    {
        DoctorChangePasswordView changePasswordView = new DoctorChangePasswordView(super.getHospitalId());
    }

    //MANAGE APPOINTMENTS
    public void ManageAppointments(DoctorAppointmentActionType actionType)
    {
        DoctorAppointmentView doctorAppointmentView= new DoctorAppointmentView(super.getHospitalId(), actionType);
    }

    //MANAGE SCHEDULE
    public void ManangeSchedule()
    {
        doctorCalendar.manageCalendar(super.getHospitalId());
    }

    //MANAGE MEDICAL RECORD
    public void ManageMedicalRecord(DoctorMedicalRecordActionType medicalRecordActionType)
    {
        if(medicalRecordActionType== DoctorMedicalRecordActionType.VIEW)
        {
            DoctorMedicalRecordView medicalRecordView= new DoctorMedicalRecordView();
        }
        else
        {
            DiagnosisTreatmentRecordView treatmentRecordView= new DiagnosisTreatmentRecordView();
        }
    }

    //MANAGE APPOINTMENT OUTCOME RECORD
    public void ManageAppointmentOutcomeRecord()
    {
        AppointmentOutcomeRecordView outcomeRecordView= new AppointmentOutcomeRecordView(super.getHospitalId());
    }
    
    public void logout()
    {
        System.out.println("Doctor logged out.");
    }
}