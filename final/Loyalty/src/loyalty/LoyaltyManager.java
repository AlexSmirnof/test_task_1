package loyalty;

import atg.repository.Repository;
import atg.repository.MutableRepository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.MutableRepositoryItem;

import javax.transaction.TransactionManager;
import javax.transaction.SystemException;

import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;

import java.util.Collection;
import java.util.Iterator;

public class LoyaltyManager extends atg.nucleus.GenericService {
	
  private static final String ITEM_TYPE_NAME = "loyaltyTransaction";	
  private static final String LIST_PROPERTY_NAME = "loyaltyTransactions";
  private static final String AMOUNT_PROPERTY_NAME = "loyaltyAmount";

  private TransactionManager transactionManager;
  private Repository loyaltyRepository;
  private Repository userRepository;
  private String rolePath;
   
  public void setTransactionManager(TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }
  
  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  public void setLoyaltyRepository(Repository loyaltyRepository) {
    this.loyaltyRepository = loyaltyRepository;
  }
  
  public Repository getLoyaltyRepository() {
    return loyaltyRepository;
  }

  public void setUserRepository(Repository userRepository) {
    this.userRepository = userRepository;
  }
  
  public Repository getUserRepository() {
    return userRepository;
  }
    
  public String getRolePath() {
	return rolePath;
  }

  public void setRolePath(String rolePath) {
	this.rolePath = rolePath;
  }

public void addLoyaltyPointsToUser(String loyaltyTransactionId) throws LoyaltyTransactionException {
  	
  	if (isLoggingDebug()) {
          logDebug("adding loyalty points from" + loyaltyTransactionId + " to user`s transactions list and count amount");
	}

	MutableRepository mutRepository = (MutableRepository) getUserRepository();
	Repository loyaltyRepository = getLoyaltyRepository();

	RepositoryItem loyaltyTransaction;
	String userId; 
	try {
		loyaltyTransaction = loyaltyRepository.getItem(loyaltyTransactionId, ITEM_TYPE_NAME);
		userId = (String) loyaltyTransaction.getPropertyValue("user");
	} catch (RepositoryException e) {
		throw new LoyaltyTransactionException("Exception occured trying to get loyaltyTransactionItem from Repository" + "\nCause: " + e.getMessage());
	}
 
	try {
		TransactionDemarcation td = new TransactionDemarcation();
		td.begin(getTransactionManager(), TransactionDemarcation.REQUIRED);
		try {

			addLoyaltyTransactionToUser(mutRepository, loyaltyTransaction, userId);

		} catch (Exception e){
			 if (isLoggingError()){
                 logError("Exception occured trying to add loyaltyTransaction", e); 
			 }
             try {
                 getTransactionManager().setRollbackOnly();
             } catch (SystemException se) {
                 if (isLoggingError()) {
                     logError("Unable to set rollback for transaction", se);
                 }
             throw new LoyaltyTransactionException("Exception occured trying to add loyaltyTransaction" + "\nCause: " + e.getMessage());    
             }
		} finally {
			td.end();
		}
	} catch (TransactionDemarcationException e){
        if (isLoggingError()) {
            logError("creating transaction demarcation failed, no loyalty points added", e);
        }
        throw new LoyaltyTransactionException("Creating transaction demarcation failed, no loyalty points added"+ "\nCause: " + e.getMessage());
	}
  }

	private void addLoyaltyTransactionToUser(MutableRepository mutRepository, RepositoryItem loyaltyTransaction, String userId) throws Exception {
		
		MutableRepositoryItem mutUser = mutRepository.getItemForUpdate(userId, "user");
		Collection loyaltyTransactionsList = (Collection) mutUser.getPropertyValue(LIST_PROPERTY_NAME);
		loyaltyTransactionsList.add(loyaltyTransaction);
		
		int amount = countPoints(loyaltyTransactionsList, "amount");
		
		mutUser.setPropertyValue(LIST_PROPERTY_NAME, loyaltyTransactionsList);
		mutUser.setPropertyValue(AMOUNT_PROPERTY_NAME, (Integer) amount);
		mutRepository.updateItem(mutUser);
		
	}
	
	private int countPoints(Collection list, String propertyName){
		int result = 0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			RepositoryItem userLoyaltyTransaction = (RepositoryItem) iterator.next();
			int value = (Integer) userLoyaltyTransaction.getPropertyValue(propertyName);
			result += value;				
		}
		return result;
	}

}