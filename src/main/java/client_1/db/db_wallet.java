package client_1.db;


import client_1.inc.DateTime;
import client_1.inc.MathTools;
import client_1.model.WalletModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class db_wallet {
    public static String TABLE_NAME = "wallet";

    public static String ITEM_id = TABLE_NAME+"_id";
    public static String ITEM_symple = TABLE_NAME+"_symple";
    public static String ITEM_date = TABLE_NAME+"_date";
    public static String ITEM_pay = TABLE_NAME+"_pay";



    private DB db = null;

    public Create create;
    public Select select;
    public Insert insert;
    public Update update;
    public Delete delete;

    public db_wallet(DB db) {
        this.db = db;
        create = new Create();
        select = new Select();
        insert = new Insert();
        update = new Update();
        delete = new Delete();
    }

    public static WalletModel toModel(ResultSet rs){
        try {
            return new WalletModel(rs.getInt(ITEM_id), rs.getString(ITEM_symple), DateTime.General.stringToLocalDate(rs.getString(ITEM_date)), MathTools.toPrice(rs.getInt(ITEM_pay)));
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
                    "  "+ITEM_symple+" TEXT NOT NULL, " +
                    "  "+ITEM_date+" DATE NOT NULL, " +
                    "  "+ITEM_pay+" DECIMAL(10, 2) NOT NULL " +
                    ");";
            try {
                PreparedStatement stam = db.getStatement(sql);

                if(stam.executeUpdate() == 0) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public class Select{
        public ArrayList<WalletModel> selectAll() {
            ArrayList<WalletModel> arr = new ArrayList<>();
            String sql = "SELECT * FROM "+TABLE_NAME;
            try {
                PreparedStatement stam = db.getStatement(sql);

                ResultSet rs = stam.executeQuery();

                while (rs.next()) {
                    arr.add(toModel(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return arr;
        }

        public WalletModel select(int wallet_id) {
            String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+ITEM_id+" = ? ";
            try {
                PreparedStatement stam = db.getStatement(sql);

                stam.setInt(1, wallet_id);


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
        public WalletModel insert(WalletModel walletModel) {
            String sql = "INSERT INTO "+TABLE_NAME+" "
                    + " ( "+ITEM_id+" , "+ITEM_date+", "+ITEM_symple+" , "+ITEM_pay+" ) "
                    + " VALUES "
                    + " ( NULL , ? , ? , ? )";
            try {
                PreparedStatement stam = db.getStatementWithId(sql);

                stam.setString(1, DateTime.General.localDateToString(walletModel.getWallet_date()));
                stam.setString(2, walletModel.getWallet_symple());
                stam.setString(3, MathTools.toPriceAsString(walletModel.getWallet_pay()));

                int affectedRows = stam.executeUpdate();

                if (affectedRows != 0) {
                    ResultSet rs = stam.getGeneratedKeys();
                    if (rs.next()) {
                        return select.select(rs.getInt(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class Update{
        //update vat
        public boolean updateVat(WalletModel walletModel) {
            String sql = "UPDATE "+TABLE_NAME+" SET "
                    + " "+ITEM_date+" = ? , "
                    + " "+ITEM_symple+" = ? , "
                    + " "+ITEM_pay+" = ? "
                    + " WHERE "
                    + " "+ITEM_id+" = ? ";
            try {
                PreparedStatement stam = db.getStatement(sql);

                stam.setString(1, DateTime.General.localDateToString(walletModel.getWallet_date()));
                stam.setString(2, walletModel.getWallet_symple());
                stam.setString(3, MathTools.toPriceAsString(walletModel.getWallet_pay()));
                stam.setInt(4, walletModel.getWallet_id());


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
        //delete
        public boolean delete(int wallet_id) {
            String sql = "DELETE FROM "+TABLE_NAME+" WHERE "+ITEM_id+" = ? ";
            try {
                PreparedStatement stam = db.getStatement(sql);
                stam.setInt(1, wallet_id);

                int affectedRows = stam.executeUpdate();
                if (affectedRows > 0) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        //delete all
        public boolean deleteAll() {
            String sql = "DELETE FROM "+TABLE_NAME;
            try {
                PreparedStatement stam = db.getStatement(sql);

                int affectedRows = stam.executeUpdate();
                if (affectedRows > 0) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean deleteAll(ArrayList<Integer> arrDelete) {
            String sql = "DELETE FROM "+TABLE_NAME+" WHERE "+ITEM_id+" IN ("+arrDelete.stream().map(String::valueOf).collect(Collectors.joining(","))+")";
            try {
                PreparedStatement stam = db.getStatement(sql);

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
}
