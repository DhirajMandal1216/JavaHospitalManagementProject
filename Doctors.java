package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
    private Connection connection;


    public Doctors(Connection connection){
        this.connection=connection;

    }

    public void viewDoctors(){
        String query="SELECT * FROM doctor";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+------------+----------------------+----------------+");
            System.out.println("| Doctor id  | Name                  | Specialization |");
            System.out.println("+------------+----------------------+----------------+");
            while (resultSet.next()){
                int id=resultSet.getInt("id");
                String name=resultSet.getNString("name");
                String specialization=resultSet.getNString("specialization");
                System.out.printf("| %-10s | %-21s | %-14s |\n",id ,name,specialization);
                System.out.println("+------------+----------------------+----------------+");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getDoctorById(int id){
        String query="SELECT * FROM doctor WHERE id=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;

            }
            else{
                return false;            }
        }
        catch (SQLException g){
            g.printStackTrace();
        }
        return false;
    }
}
