<%@ taglib uri="/dspTaglib" prefix="dsp" %>
<dsp:page>
<dsp:importbean bean="/atg/userprofiling/Profile"/>


          <font face="Verdana,Geneva,Arial" 
                size="-1" color="steelblue">
            <b>
              <a href="home.jsp">Home</a><br>
              &nbsp;<br>
              <a href="artists.jsp">Artists</a> <br>
              <a href="venues.jsp">Venues</a> <br>
              <a href="search.jsp">Search</a> <br>
              <dsp:droplet name="/atg/dynamo/droplet/Switch">
                <dsp:param bean="Profile.transient" name="value"/>
                <dsp:oparam name="true">
                    <br>
                    <a href="login.jsp">Log In</a> <br>
                </dsp:oparam>
                <dsp:oparam name="false">
                    <a href="updateProfile.jsp">Profile</a> <br>
                    <a href="playlists.jsp">Playlists</a> <br>
                    <a href="uploadsong.jsp">Upload Song</a> <br>
		                <a href="user.jsp">User Info</a> <br>	 
                    <dsp:droplet name="/atg/dynamo/droplet/HasEffectivePrincipal">
                      <dsp:param name="type" value="role"/>
                      <dsp:param name="id" value="loyaltyAdministrator"/>
                        <dsp:oparam name="output"> 
                          <a href="loyaltyPoints.jsp">Add Points</a> <br>
                        </dsp:oparam>
                    </dsp:droplet>
                    <br>
                    <a href="logout.jsp">Log Out</a> <br>
                </dsp:oparam>
              </dsp:droplet>
              
            </b>
          </font>
 
 </dsp:page>

