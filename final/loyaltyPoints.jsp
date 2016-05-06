<%@ taglib uri="/dspTaglib" prefix="dsp" %>
<dsp:page>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/loyalty/LoyaltyFormHandler"/>

<HTML>
  <HEAD>
    <TITLE>Dynamusic Loyalty Points</TITLE>
  </HEAD>
  <BODY>

    <dsp:include page="common/header.jsp">
       <dsp:param name="pagename" value="Loyalty Points"/>
    </dsp:include>
    <table width="700" cellpadding="8">
      <tr>
<!-- Sidebar -->
        <td width="100" bgcolor="ghostwhite" valign="top">
                <dsp:include page="common/sidebar.jsp"></dsp:include>
        </td>

<!-- Page Body -->
        <td valign="top">
          <font face="Verdana,Geneva,Arial" size="-1">

          <dsp:form method="post" action="<%=request.getRequestURI()%>" >

              <dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
                <dsp:oparam name="output">
                  <b><dsp:valueof param="message"/></b><br>
                </dsp:oparam>
                <dsp:oparam name="outputStart">
                  <LI>
                </dsp:oparam>
                <dsp:oparam name="outputEnd">
                  </LI>
                </dsp:oparam>
              </dsp:droplet>
           	       
	      User:
            <dsp:select bean="LoyaltyFormHandler.value.user" required="<%=true%>">
              <dsp:droplet name="/loyalty/ListForEach">
		        <dsp:param name="repositoryName" value="/atg/userprofiling/ProfileAdapterRepository"/>
                <dsp:param name="itemDescriptorName" value="user"/>
                <dsp:param name="displayedPropertyName" value="login"/>
                <dsp:param name="paramValuePropertyName" value="id"/>
                <dsp:oparam name="output">
                  <dsp:option paramvalue="value">
                    <dsp:valueof param="element"/>
                  </dsp:option>  
                </dsp:oparam>  
                <dsp:oparam name="empty">
                    (No users founds)     
                </dsp:oparam>
                <dsp:oparam name="error">
                    (Error occured: unable to find users)     
                </dsp:oparam>
              </dsp:droplet>  
            </dsp:select>
            <br><br>

	      Amount:
	          <dsp:input bean="LoyaltyFormHandler.value.amount" type="text" name="amount" size="5" required="<%=true%>"/>
            <br><br>

	      Description:	
        <br>
	          <dsp:textarea bean="LoyaltyFormHandler.value.description" name="description" 
                          rows="12" cols="80" maxlength="1000" wrap="SOFT"/>
            <br><br>
    
              <dsp:input type="hidden" bean="LoyaltyFormHandler.profileId" beanvalue="Profile.id"/>
	          <dsp:input bean="LoyaltyFormHandler.create" type="Submit" value="Add"/>	
	          <dsp:input bean="LoyaltyFormHandler.cancel" type="Submit" value="Cancel"/>
            
          </dsp:form>
                      
          </font>
        </td>
      </tr>
    </table>
  </BODY>
</HTML>
</dsp:page>