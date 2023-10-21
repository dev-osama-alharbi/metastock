package client_1.db;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.*;

public class DB {
    private static final String DB_FILE_NAME = "db.db";
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
//    private static String DB_HOST = NO_HOST_ENTERED;
    //	private static String DB_HOST = "localhost";
//    private static String DB_PORT = "3306";
    //	private static final String DB_DATABASE = "test1transmp";
//    public static String DB_DATABASE = "simpleattendance";
    private static final String DB_ENCODING = "?useUnicode=true&characterEncoding=utf-8";
    //	private static final String DB_URI = "jdbc:mariadb://"+DB_HOST+":"+DB_PORT+"/"+DB_DATABASE+DB_ENCODING;
    private static String DB_URI = "jdbc:sqlite:"+DB_FILE_NAME;
//    private static String DB_USERNAME = "root";
//    private static String DB_PASSWORD = "toor";

    public DBTable tables;
    private static Connection connection;

    private boolean isClose = true;


    public DB(){
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public void addNewHostPortUsernamePassword() {
//        Dialog<HashMap<String, String>> dialog = new Dialog<>();
//        dialog.setTitle("اتصال بقواعد البيانات");
//
//        // Set the button types.
//        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
//        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
//
//        GridPane gridPane = new GridPane();
//        gridPane.setHgap(10);
//        gridPane.setVgap(10);
//        gridPane.setPadding(new Insets(20, 150, 10, 10));
//
//        Label lblHostName = new Label("عنوان قاعدة البيانات");
//        TextField txtHostName = new TextField();
//        gridPane.add(txtHostName, 0, 0);
//        gridPane.add(lblHostName, 1, 0);
//
//        Label lblHostPort = new Label("منفذ قاعدة البيانات");
//        TextField txtHostPort = new TextField();
//        gridPane.add(txtHostPort, 0, 1);
//        gridPane.add(lblHostPort, 1, 1);
//
//        Label lblDBName = new Label("اسم قاعدة البيانات");
//        TextField txtDBName = new TextField();
//        gridPane.add(txtDBName, 0, 2);
//        gridPane.add(lblDBName, 1, 2);
//
//        Label lblUserName = new Label("اسم المستخدم");
//        TextField txtUserName = new TextField();
//        gridPane.add(txtUserName, 0, 3);
//        gridPane.add(lblUserName, 1, 3);
//
//        Label lblUserPass = new Label("كلمة المرور");
//        PasswordField passwordField = new PasswordField();
//        gridPane.add(passwordField, 0, 4);
//        gridPane.add(lblUserPass, 1, 4);
//
//        VBox box = new VBox();
//        box.getChildren().add(gridPane);
//        box.setSpacing(20);
//
//        Label infoLbl = new Label("الرجاء الاتصال بقواعد البيانات");
//        box.getChildren().add(infoLbl);
//
//        Label worningLbl1 = new Label("ان لم تستطع الاتصال بقواعد البيانات الرجاء التأكد من تشغيل قواعد البيانات");
//        worningLbl1.setTextFill(Color.RED);
//        box.getChildren().add(worningLbl1);
//
//        Label worningLbl2 = new Label("الرجاء التواصل مع الدعم الفني للبرنامج في حال وجود خطاء ما");
//        worningLbl2.setTextFill(Color.RED);
//        box.getChildren().add(worningLbl2);
//
//        dialog.getDialogPane().setContent(box);
//
//        // Request focus on the username field by default.
//        Platform.runLater(() -> txtHostName.requestFocus());
//
//        String VAR_HostName = "HostName",
//                VAR_HostPort = "HostPort",
//                VAR_DBName   = "DBName"  ,
//                VAR_UserName = "UserName",
//                VAR_Password = "Password";
//
//        dialog.setResultConverter(dialogButton -> {
//            if (dialogButton == loginButtonType) {
//                HashMap<String, String> h = new HashMap<>();
//                h.put(VAR_HostName, txtHostName.getText());
//                h.put(VAR_HostPort, txtHostPort.getText());
//                h.put(VAR_DBName, txtDBName.getText());
//                h.put(VAR_UserName, txtUserName.getText());
//                h.put(VAR_Password, passwordField.getText());
//                return h;
//            }else {
//                System.exit(0);
//                return null;
//            }
//        });
//
//        Inc.gui.stage.hide();
//        Optional<HashMap<String, String>> result = dialog.showAndWait();
//
//        result.ifPresent(h -> {
//            String host = h.get(VAR_HostName);
//            String port = h.get(VAR_HostPort);
//            String dbName = h.get(VAR_DBName);
//            String user = h.get(VAR_UserName);
//            String pass = h.get(VAR_Password);
//            if(host != null && port != null && user != null && pass != null && dbName != null) {
//                if(!host.isEmpty() && !port.isEmpty() && !user.isEmpty() && !pass.isEmpty() && !dbName.isEmpty()) {
//                    String e = SecureDbHostFile.encrypt(host+"\n"+port+"\n"+dbName+"\n"+user+"\n"+pass);
//                    SecureDbHostFile.saveFile(e);
////		    		new Alert(AlertType.NONE, "الرجاء إعاد تشغيل البرنامج", ButtonType.YES).showAndWait();
//                    Inc.gui.stage.show();
//                }else {
//                    new Alert(Alert.AlertType.ERROR, "توجد خانة فارغة! ", ButtonType.YES).showAndWait();
//                }
//            }
//        });
//    }

//    private void preparVar() {
//        String encryDB = SecureDbHostFile.getEncrypt();
//        if(encryDB == null) {
//            return;
//        }
//        HashMap<String, String> h = SecureDbHostFile.encryptToHashMap(encryDB);
//        if(h.size() == 5) {
//            DB_URI = "jdbc:mariadb://"+h.get(SecureDbHostFile.VAR_HostName)+":"+h.get(SecureDbHostFile.VAR_HostPort)+"/"+DB_ENCODING;
//            DB_USERNAME = h.get(SecureDbHostFile.VAR_UserName);
//            DB_PASSWORD = h.get(SecureDbHostFile.VAR_Password);
//            DB_DATABASE = h.get(SecureDbHostFile.VAR_DBName);
//            System.out.println("DB_USERNAME = "+DB_USERNAME);
//            System.out.println("DB_PASSWORD = "+DB_PASSWORD);
//            System.out.println("DB_DATABASE = "+DB_DATABASE);
//
//        }else {
//        }
//    }

    public void start(){
        try {
//            preparVar();
            connection = DriverManager.getConnection(DB_URI);
//			System.out.println("DB_DATABASE "+DB_DATABASE);
//			System.out.println("DB_USERNAME "+DB_USERNAME);
//			System.out.println("DB_PASSWORD "+DB_PASSWORD);
            isClose = false;
        }catch(SQLNonTransientConnectionException e) {
//            addNewHostPortUsernamePassword();
//            start();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tables = new DBTable(this);
//        if(!isHaveDb()){
//            installDb();
//        }
//        try {
//            getStatement("USE "+DB_DATABASE+" ;").executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        if(!isHaveTable()){
            installTabel();
        }

        new Thread(()->{
            boolean flag = true;
            while (flag) {
                try {
                    getStatement("SELECT 1 ").execute();
                    Thread.sleep(1800000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLNonTransientConnectionException s){
                    //java.sql.SQLNonTransientConnectionException: (conn=72) Connection is closed
                    //TODO you must reconnect
                    flag = false;
                    close();
                    start();
//					s.printStackTrace();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    private boolean isHaveDb() {
//        return checkDatabaseIsExists(DB_DATABASE);
//    }

//    private boolean checkDatabaseIsExists(String db_name) {
//        String sql = "show databases like ? ;";
//        try {
//            PreparedStatement stam = getStatement(sql);
//            stam.setString(1, db_name);
//            ResultSet rs = stam.executeQuery();
//            if (rs.next())
//                return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

//    private void installDb() {
//        try {
//            getStatement("CREATE SCHEMA "+DB_DATABASE+" DEFAULT CHARACTER SET utf8 ;").executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private boolean isHaveTable() {
        List<String> arr = new ArrayList<String>();
        List<String> arrDbInTable = new ArrayList<String>();
        arr.addAll(Arrays.asList(new String[]{
                db_wallet.TABLE_NAME,
                db_setting.TABLE_NAME}));
//        String sql = "show tables;";
        String sql = "SELECT name FROM sqlite_master WHERE type = 'table';";
        try {
            PreparedStatement stam = getStatement(sql);
            ResultSet rs = stam.executeQuery();
            while (rs.next()){
                String tableName = rs.getString(1);
                for (String string : arr) {
                    if(tableName.equals(string)){
                        arrDbInTable.add(string);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        if(arr.size() > arrDbInTable.size()){
            return false;
        }else{
            return true;
        }
    }

    private void installTabel() {
        tables.setting.create.createTable();
        tables.wallet.create.createTable();
    }

//	private boolean isHaveDB() {
//		File f = new File(dbFile);
//		return f.exists();
//	}

    public void close(){
        try {
            connection.close();
            isClose = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean isClose(){
        return this.isClose;
    }

    protected PreparedStatement getStatement(String sql){
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            return ps;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    protected PreparedStatement getStatementWithId(String sql){
        try {
            PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            return ps;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected CallableStatement getPrepareCall(String sql){
        try {
            CallableStatement ps = connection.prepareCall(sql);
            return ps;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getArrayIds(ArrayList<Integer> _EmployeesBuses_Students_ids) {
        String result = "";
        for (Integer i : _EmployeesBuses_Students_ids) {
            result = result + i+", ";
        }
        return result.substring(0, result.length() - 2);
    }












    public static class SecureDbHostFile{
        private static String secretKey = "Thi$ i$ the secretKey";
        //		private static String doublelSecretKey = "Thi$ i$ the doublelSecretKey";
        private static String salt = "Thi$ i$ the salt!!!";

        public static String encrypt(String strToEncrypt){
            try{
                byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                IvParameterSpec ivspec = new IvParameterSpec(iv);

                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
                SecretKey tmp = factory.generateSecret(spec);
                SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
                return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public static String decrypt(String strToDecrypt) {
            try
            {
                byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                IvParameterSpec ivspec = new IvParameterSpec(iv);

                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
                SecretKey tmp = factory.generateSecret(spec);
                SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void saveFile(String encrypt) {
            try {
                PrintWriter writer = new PrintWriter("db.encrypt", "UTF-8");
                writer.println(encrypt);
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        public static String getEncrypt() {
            try {
                String sCurrentLine;
                String result = "";
                if(!new File("db.encrypt").exists()) {
                    return null;
                }
                BufferedReader bufferedReader = new BufferedReader(new FileReader("db.encrypt"));
                while ((sCurrentLine = bufferedReader.readLine()) != null) {
                    result = result+sCurrentLine;
                }
                bufferedReader.close();
                return result;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }
            return null;
        }

        public static final String 	VAR_HostName = "HostName",
                VAR_HostPort = "HostPort",
                VAR_DBName 	 = "DBName",
                VAR_UserName = "UserName",
                VAR_Password = "Password";
        public static HashMap<String, String> encryptToHashMap(String encrypt){
            HashMap<String, String> h = new HashMap<>();
            try {
                String[] spl = decrypt(encrypt).split("\n");
                if(spl.length == 5) {
                    h.put(VAR_HostName, spl[0]);
                    h.put(VAR_HostPort, spl[1]);
                    h.put(VAR_DBName, spl[2]);
                    h.put(VAR_UserName, spl[3]);
                    h.put(VAR_Password, spl[4]);
                }
            } catch (Exception e) {
            }
            return h;
        }
    }
}
