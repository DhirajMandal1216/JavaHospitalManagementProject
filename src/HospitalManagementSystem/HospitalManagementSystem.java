package HospitalManagementSystem;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="Dhiraj@1612";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);
        try {
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection, scanner);
            Doctors doctors=new Doctors(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patients");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice");
                int choice=scanner.nextInt();
                switch (choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                         break;
                        //Add patient
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                        //View Patient
                    case 3:
                        doctors.viewDoctors();
                        System.out.println();
                        break;
                        //view Doctors
                    case 4:
                        bookAppointment(patient,doctors,connection,scanner);
                        System.out.println();
                        break;
                        //book appointment
                    case 5:
                        System.out.println("Thank you for Hospital Management System");
                        return;
                    default:
                        System.out.println("Enter value choice");
                        break;
                }
            }

        }
        catch (SQLException f){
            f.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient,Doctors doctors,Connection connection,Scanner scanner){
        System.out.println("Enter Patient id: ");
        int patientId=scanner.nextInt();
        System.out.println("Enter Doctor id: ");
        int doctorId=scanner.nextInt();
        System.out.println("Enter appointment Date(YYYY-MM-DD)");
        String appointmentDate =scanner.next();
        if(patient.getPatientId(patientId) && doctors.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String appointmentQuery="INSERT INTO appointments(patient_id,doctors_id,appointment_date) VALUES(?, ?, ?)" ;
                try {
                    PreparedStatement preparedStatement= connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int affectedRows=preparedStatement.executeUpdate();
                    if(affectedRows>0){
                        System.out.println("Appointment Book");
                    }
                    else {
                        System.out.println("Failed to book Appointment");
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Doctor not Available on this date!!");
            }
        }
        else {
            System.out.println("Either doctor or patient doesn't exist");
        }

    }
    public static  boolean checkDoctorAvailability(int doctorsId ,String appointmentDate ,Connection connection){
        String query="SELECT COUNT(*) FROM appointments WHERE doctors_id= ? AND appointment_date= ?";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorsId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                int count = resultSet.getInt(1);
                if (count==0){
                    return true;
                }
                else{
                    return false;
                }
            }
            else {
                return false;
            }
        }
        catch (SQLException r){
            r.printStackTrace();
        }
        return false;
    }

}
