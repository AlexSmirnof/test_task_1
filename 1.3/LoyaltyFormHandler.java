package loyalty;

import java.io.IOException;

import javax.servlet.ServletException;

import atg.droplet.DropletException;
import atg.repository.RepositoryException;
import atg.repository.servlet.RepositoryFormHandler;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.userdirectory.Role;
import atg.userdirectory.User;
import atg.userdirectory.UserDirectory;

 
public class LoyaltyFormHandler extends RepositoryFormHandler {

	private LoyaltyManager loyaltyManager;
	private UserDirectory  userDirectory;

	public UserDirectory getUserDirectory() {
		return userDirectory;
	} 

	public void setUserDirectory(UserDirectory userDirectory) {
		this.userDirectory = userDirectory;
	}

	public LoyaltyManager getLoyaltyManager() {
       	return loyaltyManager;
    }
    
    public void setLoyaltyManager(LoyaltyManager loyaltyManager) {
        this.loyaltyManager = loyaltyManager;
    }
    
    
    @Override
	protected void preCreateItem(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse)
			throws ServletException, IOException {
    	
    	 if(isLoggingDebug()) {
         	logDebug("preCreateItem method called");
         }

    	UserDirectory userDirectory = getUserDirectory();
    	String profileId = pRequest.getParameter("/loyalty/LoyaltyFormHandler.profileId");
    	User profileDir =  userDirectory.findUserByPrimaryKey(profileId);
    	String rolePath = getLoyaltyManager().getRolePath();
    	Role role = userDirectory.getRoleByPath(rolePath);
    	
    	if ( profileDir != null && profileDir.hasAssignedRole(role)){
    		@SuppressWarnings("unused")
			int amountValue;
    		try { 			
    			amountValue = (Integer) this.getValueProperty("amount");
    		} catch (Exception  e) {
    			if (isLoggingError()) {
                    logError("Validation error: input amount not a number", e);
                }
    			this.addFormException(new DropletException("Validation error: input amount not a number")); 
    		}
    	} else {
    		if (isLoggingError()) {
    			logError("Profile has no rights");
            }
    		this.addFormException(new DropletException("Your has no rights for this operation"));  
		}	

	}

    @Override
	protected void postCreateItem(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) 
             throws javax.servlet.ServletException, java.io.IOException {

        if(isLoggingDebug()) {
        	logDebug("postCreateItem method called, item created: " + getRepositoryItem());
        }

        LoyaltyManager loyaltyManager = getLoyaltyManager();

        try {

        	loyaltyManager.addLoyaltyPointsToUser(this.getRepositoryId());

        } catch (RepositoryException e) {
        	if (isLoggingError()) {
                logError("Cannot add loyalty points to user", e);
            }
            this.addFormException(new DropletException("Cannot add loyalty points to user"));    
        }

    }
}