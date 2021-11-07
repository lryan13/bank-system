package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AccountServiceException;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_VIEW_TRANSFER_BY_ID = "View details of a transfer by it's ID";
	private static final String MAIN_MENU_OPTION_APPROVE_OR_REJECT_PENDING_REQUESTS = "Approve or reject pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_VIEW_TRANSFER_BY_ID, MAIN_MENU_OPTION_APPROVE_OR_REJECT_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;

	private final long pending = 1;
	private final long approved = 2;
	private final long rejected = 3;

	private final long request = 1;
	private final long send = 2;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService());
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu(){
		while(true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_VIEW_TRANSFER_BY_ID.equals(choice)) {
				viewTransferById();
			} else if (MAIN_MENU_OPTION_APPROVE_OR_REJECT_PENDING_REQUESTS.equals(choice)){
				approveOrRejectRequest();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		if(currentUser == null) {
			System.out.println("Please log in to see your balance");
			return;
		}
		try{
			BigDecimal balance = accountService.getBalance();
			System.out.format("*********************************\n" + "Your current balance is %s%n", NumberFormat.getCurrencyInstance().format(balance) + "\n*********************************");
		}
		catch (AccountServiceException e) {
			System.out.println("Account not found");
		}
	}

	private void viewTransferHistory() {

		try {
			Transfer[] transfers = accountService.getTransfersByUser(accountService.findAccountIdByUserId(currentUser.getUser().getId()));
			System.out.println("******************************************************\n" + "Transfers\n" + "ID          From/To          Amount          Status\n" + "******************************************************");
			for (Transfer transfer : transfers) {
				if(transfer.getAccountFromId() == accountService.findAccountIdByUserId(currentUser.getUser().getId()) && transfer.getTransferTypeId() == send) {
					if (transfer.getTransferStatusId() == rejected) {
						System.out.print(transfer.getTransferId() + "        " + "To: " + accountService.findUsernameByAccountId(transfer.getAccountToId()) + "          $" + transfer.getAmount() + "        (Rejected)\n");
					}
					else if (transfer.getTransferStatusId() == pending){
						System.out.print(transfer.getTransferId() + "        " + "To: " + accountService.findUsernameByAccountId(transfer.getAccountToId()) + "          $" + transfer.getAmount() + "        (Pending)\n");
					}
					else System.out.print(transfer.getTransferId() + "        " + "To: " + accountService.findUsernameByAccountId(transfer.getAccountToId()) + "          $" + transfer.getAmount() + "        (Approved)\n");
				}
				if(transfer.getAccountFromId() == accountService.findAccountIdByUserId(currentUser.getUser().getId()) && transfer.getTransferTypeId() == request) {
					if (transfer.getTransferStatusId() == rejected) {
						System.out.print(transfer.getTransferId() + "        " + "From: " + accountService.findUsernameByAccountId(transfer.getAccountToId()) + "        $" + transfer.getAmount() + "        (Rejected)\n");
					}
					else if (transfer.getTransferStatusId() == pending){
						System.out.print(transfer.getTransferId() + "        " + "From: " + accountService.findUsernameByAccountId(transfer.getAccountToId()) + "        $" + transfer.getAmount() + "        (Pending)\n");
					}
					else System.out.print(transfer.getTransferId() + "        " + "From: " + accountService.findUsernameByAccountId(transfer.getAccountToId()) + "        $" + transfer.getAmount() + "        (Approved)\n");
				}
				if(transfer.getAccountToId() == accountService.findAccountIdByUserId(currentUser.getUser().getId()) && transfer.getTransferTypeId() == request) {
					if (transfer.getTransferStatusId() == rejected) {
						System.out.print(transfer.getTransferId() + "        " + "To: " + accountService.findUsernameByAccountId(transfer.getAccountFromId()) + "          $" + transfer.getAmount() + "        (Rejected)\n");
					}
					else if (transfer.getTransferStatusId() == pending){
						System.out.print(transfer.getTransferId() + "        " + "To: " + accountService.findUsernameByAccountId(transfer.getAccountFromId()) + "          $" + transfer.getAmount() + "        (Pending)\n");
					}
					else System.out.print(transfer.getTransferId() + "        " + "To: " + accountService.findUsernameByAccountId(transfer.getAccountFromId()) + "          $" + transfer.getAmount() + "        (Approved)\n");
				}
				if(transfer.getAccountToId() == accountService.findAccountIdByUserId(currentUser.getUser().getId()) && transfer.getTransferTypeId() == send) {
					if (transfer.getTransferStatusId() == rejected) {
						System.out.print(transfer.getTransferId() + "        " + "From: " + accountService.findUsernameByAccountId(transfer.getAccountFromId()) + "        $" + transfer.getAmount() + "        (Rejected)\n");
					}
					else if (transfer.getTransferStatusId() == pending){
						System.out.print(transfer.getTransferId() + "        " + "From: " + accountService.findUsernameByAccountId(transfer.getAccountFromId()) + "        $" + transfer.getAmount() + "        (Pending)\n");
					}
					else System.out.print(transfer.getTransferId() + "        " + "From: " + accountService.findUsernameByAccountId(transfer.getAccountFromId()) + "        $" + transfer.getAmount() + "        (Approved)\n");
				}
			}
			System.out.println("***********");
		}
		catch (AccountServiceException e) {
			System.out.println("Account service exception");
		}
	}

	private boolean viewPendingRequests() {

		try {
			Transfer[] transfers = accountService.getPendingTransfers(accountService.findAccountIdByUserId(currentUser.getUser().getId()));
			if(transfers.length == 0){
				System.out.println("You have no pending transfers.");
				return false;
			}
			else {
				System.out.println("************************************\n" + "Pending Transfers\n" + "ID          To          Amount\n" + "************************************");
				for (Transfer transfer : transfers) {
					System.out.print(transfer.getTransferId() + "        " + accountService.findUsernameByAccountId(transfer.getAccountFromId()) + "          $" + transfer.getAmount() + "\n");
				}
				System.out.println("***********");
			}
		} catch (AccountServiceException e) {
			System.out.println("Account service exception");
		}
		return true;
	}

	private void sendBucks()  {

		try{
			Scanner scanner = new Scanner(System.in);
			User[] users = accountService.printRecipients();
			String recipient = ">";
			String usersString = "0";
			for(User user : users){
				usersString += user.toString();
			}
			System.out.println("Who do you want to send to (type '0' to cancel)?");
			while(!usersString.contains(recipient)){
				System.out.println("Please enter a valid user");
				recipient = scanner.nextLine();
			}
			if(recipient.equals("0")){
				mainMenu();
			}
				long recipientId = accountService.findIdByUsername(recipient.toUpperCase());
				long recipientAccountId = accountService.findAccountIdByUserId(recipientId);
				System.out.println("Enter amount of money to send: ");
				int amountToSend = Integer.parseInt(scanner.nextLine());
				BigDecimal amount = new BigDecimal(amountToSend);
				if(amount.compareTo(accountService.getBalance()) <= 0 && amount.compareTo(new BigDecimal("0")) > 0) {
					Transfer transfer = new Transfer();
					transfer.setTransferTypeId(approved);
					transfer.setTransferStatusId(approved);
					transfer.setAccountFromId(accountService.findAccountIdByUserId(currentUser.getUser().getId()));
					transfer.setAccountToId(recipientAccountId);
					transfer.setAmount(amount);
					accountService.createTransfer(transfer);
				} else {
					System.out.println("You can not make this transfer.");
				}
		} catch (AccountServiceException e) {
			System.out.println("Account service exception");
		}
	}

	private void requestBucks() {

		try{
			Scanner scanner = new Scanner(System.in);
			User[] users = accountService.printRecipients();
			String requestee = ">";
			String usersString = "0";
			for(User user : users){
				usersString += user.toString();
			}
			System.out.println("Who do you want to request from (type '0' to cancel)?");
			while(!usersString.contains(requestee)){
				System.out.println("Please enter a valid user");
				requestee = scanner.nextLine();
			}
			if(requestee.equals("0")){
				mainMenu();
			}
			long requesteeId = accountService.findIdByUsername(requestee.toUpperCase());
			long requesteeAccountId = accountService.findAccountIdByUserId(requesteeId);
			System.out.println("Enter amount of money to request: ");
			int amountToRequest = Integer.parseInt(scanner.nextLine());
			BigDecimal amount = new BigDecimal(amountToRequest);
			Transfer transfer = new Transfer();
			transfer.setTransferTypeId(pending);
			transfer.setTransferStatusId(pending);
			transfer.setAccountFromId(accountService.findAccountIdByUserId(currentUser.getUser().getId()));
			transfer.setAccountToId(requesteeAccountId);
			transfer.setAmount(amount);
			accountService.createRequest(transfer);
		} catch (AccountServiceException e) {
			System.out.println("Account service exception");
		}
	}

	private void approveOrRejectRequest() {
		if (viewPendingRequests()) {
			try {
				Scanner scanner = new Scanner(System.in);
				System.out.println("\nPlease choose a request to respond to (enter transaction id): ");
				Long id = Long.parseLong(scanner.nextLine());
				Transfer transfer = accountService.getTransferById(id);
				System.out.println("Approve[A} or Reject[R] request?: ");
				String decision = scanner.nextLine().toUpperCase();
				if (decision.equals("R")) {
					accountService.reject(transfer);
					System.out.println("Request successfully rejected.");
				} else if (decision.equals("A")) {
					if (accountService.getBalance().compareTo(transfer.getAmount()) < 0) {
						System.out.println("You do not have enough to complete this request. Request will be rejected.");
						accountService.reject(transfer);
						System.out.println("Request successfully approved.");
					} else accountService.approve(transfer);
				}
			} catch (AccountServiceException e) {
				System.out.println("Account service exception");
			}
		}
	}

	private String viewTransferById() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter transfer ID: ");
		long transferId = Long.parseLong(scanner.nextLine());
		String result = "";
		try {
			Transfer transfer = accountService.getTransferById(transferId);
			if(transfer != null) {
				String type = "";
				String status = "";
				if(transfer.getTransferTypeId() == 1){
					type = "Request";
				}
				if(transfer.getTransferTypeId() == 2) {
					type = "Send";
				}
				if(transfer.getTransferStatusId() == 1){
					status = "Pending";
				}
				if(transfer.getTransferStatusId() == 2){
					status = "Approved";
				}
				if(transfer.getTransferStatusId() == 3){
					status = "Rejected";
				}
				String from = accountService.findUsernameByAccountId(transfer.getAccountFromId());
				String to = accountService.findUsernameByAccountId(transfer.getAccountToId());
				result = "************************************\n" +
						"Transfer Details\n" +
						"************************************\n" +
						"ID: " + transfer.getTransferId() +
						"\nFrom: " + from +
						"\nTo: " + to +
						"\nType: " + type +
						"\nStatus: " + status +
						"\nAmount: " + transfer.getAmount() +
						"\n***********";
			} else System.out.println("transfer not found");
		} catch (AccountServiceException e) {
			System.out.println("error found");
		}
		System.out.println(result);
		return result;
	}

	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: can not have empty field.");
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				accountService.setAuthenticatedUser(currentUser);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: incorrect username/password.");
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
