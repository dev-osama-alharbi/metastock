package client_1.db;

public class DBTable {
	public db_wallet wallet = null;
	public db_setting setting = null;
	
	private DB db = null;
	public DBTable(DB db) {
		this.db = db;
		this.wallet = new db_wallet(this.db);
		this.setting = new db_setting(this.db);
	}
}
