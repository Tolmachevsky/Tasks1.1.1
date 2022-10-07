package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String TABLE_PARAMS = "create table if not exists users (id bigint primary key auto_increment, name varchar(256), lastName varchar(256), age tinyint)";
    private static final Connection connection = Util.openConnection();
    public void createUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.execute(TABLE_PARAMS);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("drop table if exists users");
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users (name, lastName, age) values(?,?,?)");
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from users where id = ?");
            preparedStatement.setLong(1,id);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        ArrayList <User> userArrayList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            statement.execute("select * from users");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                byte age = resultSet.getByte(4);
                User user = new User(name,lastName,age);
                user.setId(id);
                userArrayList.add(user);
            }
            statement.close();
            return userArrayList;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("truncate users");
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
