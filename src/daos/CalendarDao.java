package src.daos;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import src.utils.ENUM.CalendarDayStatus;
import src.interfaces.CalendarDaoInterface;

public class CalendarDao implements CalendarDaoInterface {
    // ATTRIBUTES
    private static String CALENDARDB_PATH;
    private File CalendarFile;

    // CONSTRUCTOR
    public CalendarDao(String ID) {
        // LOAD CONFIGURATION FROM CONFIG.PROPERTIES FILE
        try (InputStream input = new FileInputStream("src/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            CALENDARDB_PATH = prop.getProperty("CALENDARDB_PATH", "src/data/Calendar");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // LOCATE THE CORRECT CSV FILE WITH MATCHING ID
        File calendarDir = new File(CALENDARDB_PATH);
        if (calendarDir.exists() && calendarDir.isDirectory()) {
            File[] files = calendarDir.listFiles(file -> file.getName().equals(ID + "_Cal.csv"));

            if (files != null && files.length > 0) {
                this.CalendarFile = files[0];
            } else {
                System.out.print("No files found.");
            }
        } else {
            System.err.println("Calendar directory not found at: " + CALENDARDB_PATH);
        }
    }

    
    /** 
     * @param date
     * @return CalendarDayStatus
     */
    // METHOD TO GET STATUS FOR SPECIFIC DATE
    public CalendarDayStatus getStatus(LocalDate date) {
        try (BufferedReader br = new BufferedReader(new FileReader(CalendarFile))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                LocalDate existingDate = LocalDate.parse(parts[0],
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (existingDate.equals(date)) {
                    return CalendarDayStatus.valueOf(parts[1]); // Return the status if the date matches
                }
            }
        } catch (IOException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return CalendarDayStatus.NA; // Date will always be found
    }

    
    /** 
     * @param calendarFile
     * @param status
     * @return List<LocalDate>
     */
    // METHOD TO READ DATES FOR SPECIFIC STATUS
    public List<LocalDate> getDatesByStatus(File calendarFile, CalendarDayStatus status) {
        List<LocalDate> dates = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(calendarFile))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                LocalDate date = LocalDate.parse(parts[0], java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (CalendarDayStatus.valueOf(parts[1]) == status) {
                    dates.add(date);
                }
            }
        } catch (IOException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        return dates;
    }

    
    /** 
     * @param date
     * @return int
     */
    // METHOD TO GET THE NUMBER OF USERS WHO HAVE APPLIED FOR ANNUAL LEAVE ON A
    // SPECIFIC DATE
    public int getNumberOfAnnualLeaveDays(LocalDate date) {
        int annualLeaveCount = 0;

        // Get all files in the calendar folder
        File calendarDir = new File(CALENDARDB_PATH);
        if (calendarDir.exists() && calendarDir.isDirectory()) {
            // Iterate through all files in the folder
            File[] files = calendarDir.listFiles((dir, name) -> name.endsWith("_Cal.csv"));

            if (files != null) {
                for (File file : files) {
                    // Read the file and check if the given date has "ANNUAL_LEAVE" status
                    List<LocalDate> annualLeaveDates = getDatesByStatus(file, CalendarDayStatus.ANNUAL_LEAVE);
                    if (annualLeaveDates.contains(date)) {
                        annualLeaveCount++;
                    }
                }
            }
        }

        return annualLeaveCount;
    }

    // METHOD TO GET THE NUMBER OF USERS WHO HAVE APPLIED FOR MEDICAL LEAVE ON A
    // SPECIFIC DATE
    public int getNumberOfMedicalLeaveDays(LocalDate date) {
        int medicalLeaveCount = 0;

        // Get all files in the calendar folder
        File calendarDir = new File(CALENDARDB_PATH);
        if (calendarDir.exists() && calendarDir.isDirectory()) {
            // Iterate through all files in the folder
            File[] files = calendarDir.listFiles((dir, name) -> name.endsWith("_Cal.csv"));

            if (files != null) {
                for (File file : files) {
                    // Read the file and check if the given date has "MEDICAL_LEAVE" status
                    List<LocalDate> medicalLeaveDates = getDatesByStatus(file, CalendarDayStatus.MEDICAL_LEAVE);
                    if (medicalLeaveDates.contains(date)) {
                        medicalLeaveCount++;
                    }
                }
            }
        }

        return medicalLeaveCount;
    }

    // METHOD TO APPLY ANNUAL LEAVE AND UPDATE CSV FILE
    public void applyAnnualLeave(LocalDate date) {
        changeStatus(date, CalendarDayStatus.ANNUAL_LEAVE);
    }

    // METHOD TO APPLY MEDICAL LEAVE AND UPDATE CSV FILE
    public void applyMedicalLeave(LocalDate date) {
        changeStatus(date, CalendarDayStatus.MEDICAL_LEAVE);
    }

    // METHOD TO CANCEL ANNUAL LEAVE AND UPDATE CSV FILE
    public void cancelAnnualLeave(LocalDate date) {
        changeStatus(date, CalendarDayStatus.AVAILABLE);
    }

    // METHOD TO CANCEL MEDICAL LEAVE AND UPDATE CSV FILE
    public void cancelMedicalLeave(LocalDate date) {
        changeStatus(date, CalendarDayStatus.AVAILABLE);
    }

    // GENERAL METHOD TO CHANGE STATUS AND UPDATE THE CSV FILE
    public boolean changeStatus(LocalDate date, CalendarDayStatus status) {
        boolean isUpdated = false;

        // Read the file and store all lines
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CalendarFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                lines.add(line);
                String[] parts = line.split(",");
                
                LocalDate existingDate = LocalDate.parse(parts[0],
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (existingDate.equals(date)) {
                    // Update the status if the date is found
                    String updatedLine = parts[0] + "," + status.name();
                    lines.set(lines.size() - 1, updatedLine);
                    isUpdated = true;
                }
            }

            if (!isUpdated) {
                return false; // If the date wasn't found, return false (though it's assumed the date will
                              // always be there)
            }

            // Write the updated data back to the CSV
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CalendarFile))) {
                for (String Line : lines) {
                    bw.write(Line);
                    bw.newLine();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        /*
         * //CHECK IF USER IS A DOCTOR, IF YES APPOINTMENT AVAILABILITY HAS TO BE
         * CHANGED
         * if(CalendarFile.getName().startsWith("D"))
         * {
         * switch(status)
         * {
         * case ANNUAL_LEAVE:
         * break;
         * case MEDICAL_LEAVE:
         * break;
         * }
         * }
         */

        return true;
    }

    // RETURN MEDICAL_LEAVE DATES
    public List<LocalDate> getMedicalLeaveDates() {
        return getDatesByStatus(this.CalendarFile, CalendarDayStatus.MEDICAL_LEAVE);
    }

    // RETURN ANNUAL_LEAVE DATES
    public List<LocalDate> getAnnualLeaveDates() {
        return getDatesByStatus(this.CalendarFile, CalendarDayStatus.ANNUAL_LEAVE);
    }

    // RETURN MEETING DATES
    public List<LocalDate> getMeetingDates() {
        return getDatesByStatus(this.CalendarFile, CalendarDayStatus.MEETING);
    }

    // RETURN TRAINING DATES
    public List<LocalDate> getTrainingDates() {
        return getDatesByStatus(this.CalendarFile, CalendarDayStatus.TRAINING);
    }

    // RETURN AVAILABLE DATES
    public List<LocalDate> getAvailableDates() {
        return getDatesByStatus(this.CalendarFile, CalendarDayStatus.AVAILABLE);
    }

    // RETURN OTHERS DATES
    public List<LocalDate> getOthersDates() {
        return getDatesByStatus(this.CalendarFile, CalendarDayStatus.OTHERS);
    }

    // RETURN NA DATES
    public List<LocalDate> getNaDates() {
        return getDatesByStatus(this.CalendarFile, CalendarDayStatus.NA);
    }
}