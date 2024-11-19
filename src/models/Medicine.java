package src.models;

import java.util.UUID;

public class Medicine {

    // INSTANCE VARIABLES
    private UUID medicineId;
    private String medicineName;
    private int medicineQuantity;
    private int medicineAlert;

    // CONSTRUCTORS
    public Medicine(UUID medicineId, String medicineName, int medicineQuantity, int medicineAlert) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.medicineQuantity = medicineQuantity;
        this.medicineAlert = medicineAlert;
    }

    // INSTANCE METHODS
    public UUID getMedicineId() {
        return this.medicineId;
    }

    public void setMedicineId(UUID medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return this.medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getMedicineQuantity() {
        return this.medicineQuantity;
    }

    public void setMedicineQuantity(int medicineQuantity) {
        this.medicineQuantity = medicineQuantity;
    }

    public int getMedicineAlert() {
        return this.medicineAlert;
    }

    public void setMedicineAlert(int medicineAlert) {
        this.medicineAlert = medicineAlert;
    }
}