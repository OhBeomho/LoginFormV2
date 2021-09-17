package form.login.obho;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Account {
	public static final File ACCOUNTS_FILE = new File("C:/Accounts/LoginFormV2Accounts/accounts.txt");
	private static final String ACCOUNT_FORMAT = "%s|%s|%s|%tY년 %<tm월 %<td일!%<tH시 %<tM분 %<tS초"; // name|email|password|signUpDate

	private String name, email, password, signUpDate;

	public Account(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public Account(String name, String email, String password, String signUpDate) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.signUpDate = signUpDate;
	}

	public String getSignUpDate() {
		return signUpDate;
	}

	public void setSignUpDate(String signUpDate) {
		this.signUpDate = signUpDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void saveAccount() {
		Date now = new Date();

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(ACCOUNTS_FILE, true))) {
			String data = String.format(ACCOUNT_FORMAT, name, email, password, now);
			bw.write(data);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
