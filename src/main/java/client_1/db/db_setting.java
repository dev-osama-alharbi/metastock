package client_1.db;

import client_1.inc.DateTime;
import client_1.model.SettingModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class db_setting {
    public static String TABLE_NAME = "setting";

    public static String ITEM_id = TABLE_NAME+"_id";
    public static String ITEM_daily = TABLE_NAME+"_daily";
    public static String ITEM_intraday = TABLE_NAME+"_intraday";
    public static String ITEM_date = TABLE_NAME+"_date";
    public static String ITEM_numOfLastDays = TABLE_NAME+"_numOfLastDays";



    private DB db = null;

    public Create create;
    public Select select;
    public Insert insert;
    public Update update;
    public Delete delete;

    public db_setting(DB db) {
        this.db = db;
        create = new Create();
        select = new Select();
        insert = new Insert();
        update = new Update();
        delete = new Delete();
    }

    public static SettingModel toModel(ResultSet rs){
        try {
            return new SettingModel(rs.getString(ITEM_daily), rs.getString(ITEM_intraday), DateTime.General.stringToLocalDate(rs.getString(ITEM_date)), rs.getInt(ITEM_numOfLastDays));
        } catch (SQLException throwables) {
            return null;
        }
    }

    public class Create{
        //create table
        public boolean createTable() {
            String sql = "CREATE TABLE "+TABLE_NAME+" " +
                    "( " +
                    "  "+ITEM_id+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "  "+ITEM_daily+" TEXT NOT NULL, " +
                    "  "+ITEM_intraday+" TEXT NOT NULL, " +
                    "  "+ITEM_date+" DATE, " +
                    "  "+ITEM_numOfLastDays+" INTEGER NOT NULL" +
                    ");";
            try {
                PreparedStatement stam = db.getStatement(sql);

                if(stam.executeUpdate() == 0) {
                    insert.insert();
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public class Select{
        //select 1
        public SettingModel select() {
            String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+ITEM_id+" = 1 ";
            try {
                PreparedStatement stam = db.getStatement(sql);

                ResultSet rs = stam.executeQuery();

                if(rs.next()) {

                    return toModel(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class Insert{
        //insert on create all db
        public boolean insert() {
            String sql = "INSERT INTO "+TABLE_NAME+" "
                    + " ( "+ITEM_id+" , "+ITEM_daily+" , "+ITEM_intraday+", "+ITEM_date+", "+ITEM_numOfLastDays+" ) "
                    + " VALUES "
                    + " ( NULL , '' , '' , null, 1 )";
            try {
                PreparedStatement stam = db.getStatementWithId(sql);

                int affectedRows = stam.executeUpdate();

                if (affectedRows != 0) {
                    ResultSet rs = stam.getGeneratedKeys();
                    if (rs.next()) {
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public class Update{
        //update vat
        public boolean update(SettingModel model) {
            String sql = "UPDATE "+TABLE_NAME+" SET "
                    + " "+ITEM_daily+" = ? , "
                    + " "+ITEM_intraday+" = ? , "
                    + " "+ITEM_date+" = ? , "
                    + " "+ITEM_numOfLastDays+" = ? "
                    + " WHERE "
                    + " "+ITEM_id+" = 1 ";
            try {
                PreparedStatement stam = db.getStatement(sql);

                stam.setString(1, model.getDaily());
                stam.setString(2, model.getIntraday());
                stam.setString(3, DateTime.General.localDateToString(model.getDate()));
                stam.setInt(4, model.getNumOfLastDays());


                int affectedRows = stam.executeUpdate();
                if (affectedRows > 0) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public class Delete{
        //-----------------------
    }
}
