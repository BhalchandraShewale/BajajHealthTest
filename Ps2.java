package problemstatement2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Ps2 {

	private static final String API_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus/create/user";

	public static void main(String[] args) {
		// Test cases
		testCreateUserWithValidData();
		testCreateUserWithDuplicatePhoneNumber();
		testCreateUserWithoutRollNumber();
		testCreateUserWithDuplicateEmail();
		testCreateUserWithInvalidPhoneNumber();
		testCreateUserWithMissingFields();
	}

	private static void testCreateUserWithValidData() {
		String jsonData = "{\"firstName\": \"Test\", \"lastName\": \"User\", \"phoneNumber\": 9999999990, \"emailId\": \"test.user@test.com\"}";
		sendPostRequest(jsonData, "1");
	}

	private static void testCreateUserWithDuplicatePhoneNumber() {
		String jsonData1 = "{\"firstName\": \"Test\", \"lastName\": \"User1\", \"phoneNumber\": 9999999991, \"emailId\": \"test.user1@test.com\"}";
		String jsonData2 = "{\"firstName\": \"Test\", \"lastName\": \"User2\", \"phoneNumber\": 9999999991, \"emailId\": \"test.user2@test.com\"}";

		// First request
		sendPostRequest(jsonData1, "1");
		// Second request should fail
		sendPostRequest(jsonData2, "1");
	}

	private static void testCreateUserWithoutRollNumber() {
		String jsonData = "{\"firstName\": \"Test\", \"lastName\": \"User\", \"phoneNumber\": 9999999992, \"emailId\": \"test.user2@test.com\"}";
		sendPostRequest(jsonData, null); // No roll number
	}

	private static void testCreateUserWithDuplicateEmail() {
		String jsonData1 = "{\"firstName\": \"Test\", \"lastName\": \"User1\", \"phoneNumber\": 9999999993, \"emailId\": \"duplicate@test.com\"}";
		String jsonData2 = "{\"firstName\": \"Test\", \"lastName\": \"User2\", \"phoneNumber\": 9999999994, \"emailId\": \"duplicate@test.com\"}";

		// First request
		sendPostRequest(jsonData1, "1");
		// Second request should fail
		sendPostRequest(jsonData2, "1");
	}

	private static void testCreateUserWithInvalidPhoneNumber() {
		String jsonData = "{\"firstName\": \"Test\", \"lastName\": \"User\", \"phoneNumber\": 12345, \"emailId\": \"test.invalid@test.com\"}";
		sendPostRequest(jsonData, "1");
	}

	private static void testCreateUserWithMissingFields() {
		String jsonData = "{\"firstName\": \"\", \"lastName\": \"\", \"phoneNumber\": 9999999995, \"emailId\": \"\"}";
		sendPostRequest(jsonData, "1");
	}

	private static void sendPostRequest(String jsonData, String rollNumber) {
		try {
			URL url = new URL(API_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			if (rollNumber != null) {
				conn.setRequestProperty("roll-number", rollNumber);
			}

			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonData.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			int responseCode = conn.getResponseCode();
			System.out.println("Response Code: " + responseCode);

			// Read response
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println("Response: " + response.toString());
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}