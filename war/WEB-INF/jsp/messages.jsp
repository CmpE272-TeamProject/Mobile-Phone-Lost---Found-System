<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
	<title><fmt:message key="appTitle" /></title>
 	<link rel="stylesheet" href="css/bootstrap-responsive.css" type="text/css"/>
	<link rel="stylesheet" href="css/bootstrap-responsive.min.css" type="text/css"/>
	<link rel="stylesheet" href="css/bootstrap.css" type="text/css"/>
	<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
 
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<script type="text/javascript">
	    function loadGoogleMap(latitude, longitude) {
	    	var latlng = new google.maps.LatLng(longitude,latitude);
	        var mapOptions = {
	            zoom : 12,
	            center : latlng,
	            mapTypeId : google.maps.MapTypeId.ROADMAP,
	            mapTypeControlOptions : {
	                style : google.maps.MapTypeControlStyle.DROPDOWN_MENU,
	                position : google.maps.ControlPosition.TOP_CENTER
	            }
	        };
	        var map = new google.maps.Map(document.getElementById("map_canvas"),
	                mapOptions);
	
	        var marker = new google.maps.Marker( {
	            position : latlng,
	            map : map,
	            title : "Message was sent from here !!!"
	        });
	}
	</script>

<style>
    .error { color: red; }
</style>  
  
  
</head>
<body>

<form:form method="post" commandName="message">
  
 
<table width="100%" height="100%"  background="Images/wp4.jpeg">
	<tr><td>
		<table width="100%" height="60%" background="Images/images23.jpg" cellspacing="0" style="color: black; background-color: #F8F8F8 ;font-size: 60pt"  >
			<tr>
				<td><img src="Images/icon1.jpeg" height="150" width="150"/></td>
					<td  align="center" >
						<b>Mobile</b>
					<img src="Images/images17.jpeg" class="img-circle" />
				</td>
			</tr>
		</table>
	</td></tr>

	<tr><td width="100%" height="100%" >
		<table height="100%" width="100%">
			<tr>
				<td width=40%" align="center" valign="top">
					<div id="container" class="container" >
						<table width="70%" align="center" border="0" cellspacing="0" cellpadding="5" style="background-color: #F8F8F8 ;">    
						    <!--  <tr>
								<td align="center" style="color: #336633; font-size: 20pt"><b>
									<fmt:message key="messagePage.heading" /></b>
								</td>
							</tr> -->
								    
							 <c:if test="${model.messages !=null}">
							    <tr><td>
								    <table width="100%" border="2" cellpadding="5">
									    <tr align="center" style="background-color: #99CCFF; font-size: 15pt">
									    	<font color="black">
								    			<td width="40%"> <b>Date Received </b> </td>
								    			<td width="40%"> <b> Message Sent </b> </td>
								    			<td width="40%"> <b> Phone Location When Notification Sent </b> </td>
								    		</font>
								   		 </tr>
								    
										<c:forEach items="${model.messages}" var="prod">
								    	<tr>
									    	<td align="center" width="40%">
									           	 <c:out value="${prod.dateReceived}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									        </td>
									        <td align="left"  width="40%">
									    	    <c:out value="${prod.message}"/>
									        </td>
									        <td align="center">
										        <button class="btn btn-success" type="button" onclick="loadGoogleMap(${prod.longitude},${prod.latitude})">Map</button>
									        </td>
								   	 	</tr> 
								 		</c:forEach>
								      </table>
								 	</td></tr>
								</c:if>
								    
								    
								<c:if test="${empty model.messages}">
								  <tr>
								     <td align="center" width="100%" style="color: #900000; font-size: 20pt">
								     	 <b> Sorry !!! No Messages Received !!!</b>
								     </td>    
								  </tr>
								  <br/><br/><br/><br/> 
							 	</c:if>   
								
								<br><br>
								    
								<tr>
								    <td width="100%" colspan=2 align="center" style="color: #008080; font-size: 18pt">
								    <!-- <input type="submit" align="center" class="btn btn-primary" value="Logout"></td> -->
								      <a href="login.htm">Logout</a>
								    </td>
						    	</tr>
						</table>
					<br>
					</div>
				</td>
				<td align="center" width="60%" height="85%">
					<div align="left" valign="center" id="map_canvas" style="width: 90%; height: 80%">
						<img src="Images/image24.jpg" style="height: 80%; width: 100%;"/>
					</div>
				</td>
			</tr>
		</table>
	</td></tr>
</table> 
 
</form:form>

</body>
</html>