package src.controllers;

import java.util.List;

import src.models.Medicine;
import src.models.Prescription;

import src.interfaces.PharmacistServiceInterface;

public class PharmacistController {
    private final PharmacistServiceInterface pharmacistService;
    private final MedicineController medicineController;
    private final ReplenishmentRequestController replenishmentRequestController;

    public PharmacistController(PharmacistServiceInterface pharmacistService,
            MedicineController medicineController, ReplenishmentRequestController replenishmentRequestController) {
        this.pharmacistService = pharmacistService;
        this.medicineController = medicineController;
        this.replenishmentRequestController = replenishmentRequestController;
    }

    public boolean handleUpdatePassword(String hospitalId, String newPassword, String confirmPassword) {
        try {
            pharmacistService.updatePassword(hospitalId, newPassword, confirmPassword);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Medicine> handleViewMedicationInventory() {
        try {
            return medicineController.handleViewMedicationInventory();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean handleSubmitReplenishmentRequest(String medicineName, int replenishmentQuantity) {
        try {
            return replenishmentRequestController.handleSubmitReplenishmentRequest(medicineName, replenishmentQuantity);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}