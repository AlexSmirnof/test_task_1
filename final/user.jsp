<%@ taglib uri="/dspTaglib" prefix="dsp" %>
<dsp:page>

<%-- Required input param: itemId (id of the user to display --%>
  

<HTML>
  <HEAD>
    <TITLE>Dynamusic User Page</TITLE>
  </HEAD>
  <BODY>
    <dsp:include page="common/header.jsp">
       <dsp:param name="pagename" value="User"/>
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
          
          <!-- *** Start page content *** -->

            <table width="560">
              <tr>
                <td>
                <dsp:droplet name="/atg/targeting/RepositoryLookup">
                  <dsp:param name="id" bean="/atg/userprofiling/Profile.id"/>
                  <dsp:param name="itemDescriptor" value="user"/>
                  <dsp:param bean="/atg/userprofiling/ProfileAdapterRepository" name="repository"/>

                  <dsp:oparam name="output">
                
                     <table>
                       <tr>
                         <td>
                      	   Name: 
                         </td>
                         <td>
                           <b><dsp:valueof param="element.firstName"/> <dsp:valueof param="element.lastName"/></b>
                         </td>
                        </tr>
                        <tr>
                         <td> Email address: </td>
                         <td><b><dsp:valueof param="element.email"/>    </b></td>
                        </tr>
                        <tr>
                           <td>Location</td>
                           <td><b><dsp:valueof param="element.homeAddress.state"/></b></td>
                        </tr>
                        <tr>
                         <td>Favorite genres </td>
                         <td>
                        <dsp:droplet name="/atg/dynamo/droplet/ForEach">
                          <dsp:param name="array" param="element.prefGenres"/>
                          <dsp:oparam name="outputStart">
                            <b>
                          </dsp:oparam>
                          <dsp:oparam name="outputEnd">
                            </b>
                          </dsp:oparam>
                          <dsp:oparam name="output">
                            <li><dsp:valueof param="element"/><br>
                          </dsp:oparam>
                          </dsp:droplet>
                        <tr>
                          <td>User info</td>
                          <td><b><dsp:valueof param="element.info"/></b></td>
                        </tr>
                        <tr><td><hr>Loyalty Points</td></tr>
                        <tr>
                          <td>Total Amount:</td>
                          <td><b><dsp:valueof param="element.loyaltyAmount"/></b></td>
                        </tr>  
                        <tr>
                          <td valign="top">Loyalty Transactions:</td>
                          <td>
                            <dsp:droplet name="/atg/dynamo/droplet/Range">
                              <dsp:param name="array" param="element.loyaltyTransactions"/>
                              <dsp:param name="howMany" value="3"/>
                              <dsp:param name="sortProperties" value="-created"/>
                              <dsp:oparam name="outputStart"><ul></dsp:oparam>
                              <dsp:oparam name="outputEnd"></ul></dsp:oparam>
                              <dsp:oparam name="output">
                                <li>
                                  Amount:<dsp:valueof param="element.amount"/>;<br>
                                  Date:<dsp:valueof param="element.created"/>;<br>
                                  Description:<dsp:valueof param="element.description"/>;<br>
                                  <br>
                                </li>
                              </dsp:oparam>
                              <dsp:oparam name="empty">No transactions</dsp:oparam>
                            </dsp:droplet>
                          </td>
                        </tr>
                        <tr>
                          <td><dsp:a href="sendmessage.jsp">
                            <dsp:param name="userid" param="itemId"/>
                              <br><hr>Send <dsp:valueof param="element.firstName"/> mail
                         </dsp:a>
                         </td>
                       </tr>
                     </table>
     
                  </dsp:oparam>
                  <dsp:oparam name="empty">
                     No such user found.    
                  </dsp:oparam>

                </dsp:droplet>

                </td>
              </tr>
            </table>
            
          <!-- *** End real content *** -->
          
          </font>
        </td>
      </tr>
    </table>
  </BODY>
</HTML>
</dsp:page>
